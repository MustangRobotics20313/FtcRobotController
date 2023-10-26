package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@TeleOp
public class GoBildaChassisDrive extends LinearOpMode {

    private enum SlideState {
        RETRACTED,
        RETRACTING,
        EXTENDED,
        EXTENDING
    }

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotorEx slide;
    private Servo grabber;

    private TouchSensor slideSensor;
    private OpenCvCamera camera;
    //private ContourPipeline pipeline;

    //units of ticks
    private final int RETRACTED_POSITION = 0;
    private final int LOW_POSITION = 500;
    private final int MIDDLE_POSITION = 1000;
    private final int HIGH_POSITION = 2000;

    private final int STACK_FIVE_POSITION = 700;
    private final int STACK_FOUR_POSITION = 540;
    private final int STACK_THREE_POSITION = 400;
    private final int STACK_TWO_POSITION = 240;

    private final double SLIDE_POWER = 0.9;

    @Override
    public void runOpMode() {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);
        //pipeline = new ContourPipeline();

        //camera.setPipeline(pipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
                dashboard.startCameraStream(camera, 0);
            }

            @Override
            public void onError(int errorCode) {}
        });

        SlideState state = SlideState.RETRACTED;

        fl = hardwareMap.get(DcMotor.class, "fl"); //HAS RIGHT SIDE DEADWHEEL
        fr = hardwareMap.get(DcMotor.class, "fr"); //HAS REAR DEADWHEEL
        rl = hardwareMap.get(DcMotor.class, "rl"); //HAS LEFT SIDE DEADWHEEL
        rr = hardwareMap.get(DcMotor.class, "rr");
        slide = hardwareMap.get(DcMotorEx.class, "slide");
        grabber = hardwareMap.get(Servo.class, "grabber");
        slideSensor = hardwareMap.get(TouchSensor.class, "slideSensor");

        slide.setDirection(DcMotor.Direction.REVERSE);
        grabber.scaleRange(0, 1);

        telemetry.setMsTransmissionInterval(200);

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fr.setDirection(DcMotor.Direction.REVERSE);
        rr.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        while(opModeIsActive()) {

            slide();
            grabber();

            switch(state) {
                case RETRACTED:
                    mecanum(1);
                    if (slide.getCurrentPosition() > 300 && slide.getVelocity() > 0) {
                        state = SlideState.EXTENDING;
                    }
                    break;
                case EXTENDING:
                    mecanum(0.75);
                    if (slide.getCurrentPosition() > 3500) {
                        state = SlideState.EXTENDED;
                    }
                case EXTENDED:
                    mecanum(0.5);
                    if (slide.getCurrentPosition() < 3500 && slide.getVelocity() < 0) {
                        state = SlideState.RETRACTING;
                    }
                    break;
                case RETRACTING:
                    mecanum(0.75);
                    if (slide.getCurrentPosition() < 300) {
                        state = SlideState.RETRACTED;
                    }
                    break;
            }

            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Front right power\t: ", fl.getPower());
            telemetry.addData("Rear left power\t: ", fl.getPower());
            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Slide power\t: ", slide.getPower());
            telemetry.addData("Slide position\t: ", slide.getCurrentPosition());
            telemetry.addData("Grabber position: ", grabber.getPosition());

            telemetry.addData("Left encoder\t:", fr.getCurrentPosition());
            telemetry.addData("Right encoder\t:", fl.getCurrentPosition());

            telemetry.update();
        }
    }

    private void slide() {
        if (gamepad1.left_bumper) { //full extension
            slide.setTargetPosition(HIGH_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slide.setPower(SLIDE_POWER);
        } else if (gamepad1.right_bumper) { //full retraction
            slide.setTargetPosition(RETRACTED_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slide.setPower(-SLIDE_POWER);
        } else if (gamepad1.left_trigger > 0) { //manual control
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setPower(SLIDE_POWER);
        } else if (gamepad1.right_trigger > 0) { //manual control
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setPower(-SLIDE_POWER);
        } else if (gamepad1.a) { //extend to low junction
            slide.setTargetPosition(LOW_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > LOW_POSITION) {
                slide.setPower(-SLIDE_POWER);
            } else {
                slide.setPower(SLIDE_POWER);
            }
        } else if (gamepad1.b) { //extend to medium junction
            slide.setTargetPosition(MIDDLE_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > MIDDLE_POSITION) {
                slide.setPower(-SLIDE_POWER);
            } else {
                slide.setPower(SLIDE_POWER);
            }
        } else if (gamepad2.a) {
            slide.setTargetPosition(STACK_FIVE_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > STACK_FIVE_POSITION) {
                slide.setPower(-SLIDE_POWER);
            } else {
                slide.setPower(SLIDE_POWER);
            }
        } else if (gamepad2.b) {
            slide.setTargetPosition(STACK_FOUR_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > STACK_FOUR_POSITION) {
                slide.setPower(-SLIDE_POWER);
            } else {
                slide.setPower(SLIDE_POWER);
            }
        } else if (gamepad2.x) {
            slide.setTargetPosition(STACK_THREE_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > STACK_THREE_POSITION) {
                slide.setPower(-SLIDE_POWER);
            } else {
                slide.setPower(SLIDE_POWER);
            }
        } else if (gamepad2.y) {
            slide.setTargetPosition(STACK_TWO_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > STACK_TWO_POSITION) {
                slide.setPower(-SLIDE_POWER);
            } else {
                slide.setPower(SLIDE_POWER);
            }
        }

        if (gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0 && slide.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            slide.setPower(0);
        }

        if (slideSensor.isPressed()) {
            slide.setPower(0);
            slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    private void grabber() {
        if (gamepad1.left_stick_button) {
            grabber.setPosition(0.2);
        } else if (gamepad1.right_stick_button) {
            grabber.setPosition(0.5);
        }
    }

    private void mecanum(double multiplier) {
        double drive = gamepad1.left_stick_y;
        double strafe = -gamepad1.right_stick_x;
        double twist = gamepad1.left_stick_x;

        double v1 = drive + strafe - twist;
        double v2 = drive - strafe + twist;
        double v3 = drive - strafe - twist;
        double v4 = drive + strafe + twist;

        fl.setPower(v1 * 0.75 * multiplier);
        fr.setPower(v2 * 0.75 * multiplier);
        rl.setPower(v3 * 0.75 * multiplier);
        rr.setPower(v4 * 0.75 * multiplier);
    }
}