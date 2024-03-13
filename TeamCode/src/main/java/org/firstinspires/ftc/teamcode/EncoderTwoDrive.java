package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp
@Config
@Disabled

public class EncoderTwoDrive extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor slide;
    private DcMotor rotate;
    private DcMotor rig;
    private Servo servo;
    private Servo servoBoard;
    private TouchSensor touch;


    //units of ticks

    private final int HEIGHT_1 = 900;
    private final int HEIGHT_2 = 1200;
    private final int HEIGHT_3 = 1400;
    private final int ZERO = 0;


    private final double SLIDE_POWER = 0.5;
    public final double LIFT_POWER = 0.75;

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        slide = hardwareMap.get(DcMotor.class, "slide");
        //rotate = hardwareMap.get(DcMotor.class, "rotate");
        rig = hardwareMap.get(DcMotor.class, "rig");
        servo = hardwareMap.get(Servo.class, "servo");
        servoBoard = hardwareMap.get(Servo.class, "servoBoard");
        touch = hardwareMap.get(TouchSensor.class, "touch");

        rr.setDirection(DcMotor.Direction.REVERSE);
        fl.setDirection(DcMotor.Direction.REVERSE);

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        String direction = "collection";

        waitForStart();
        while(opModeIsActive()) {

            //Switching directions

            /* if (gamepad1.x){
                if (direction == "collection") {
                    fr.setDirection(DcMotor.Direction.REVERSE);
                    rr.setDirection(DcMotor.Direction.REVERSE);
                    direction = "scoring";

                }

                else if (direction == "scoring"){
                    fl.setDirection(DcMotor.Direction.REVERSE);
                    rl.setDirection(DcMotor.Direction.REVERSE);
                    direction = "collection";

                }
            } */

            //Left Stick: General driving + rotating

            fl.setPower(gamepad1.left_stick_y);
            fr.setPower(gamepad1.left_stick_y);
            rr.setPower(-gamepad1.left_stick_y);
            rl.setPower(-gamepad1.left_stick_y);

            //Rotating

            fl.setPower(gamepad1.left_stick_x);
            fr.setPower(-gamepad1.left_stick_x);
            rr.setPower(gamepad1.left_stick_x);
            rl.setPower(-gamepad1.left_stick_x);

            //Right Stick: Straffing

            fl.setPower(-gamepad1.right_stick_x);
            fr.setPower(gamepad1.right_stick_x);
            rr.setPower(gamepad1.right_stick_x);
            rl.setPower(-gamepad1.right_stick_x);

            //slide

            slide();

            // resetting rotate encoder
            if (touch.isPressed()) {
                slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }

            // rotating + rigging

            if (gamepad2.left_stick_y > 0){
                rig.setPower(-1);
            }

            else if (gamepad2.left_stick_y < 0){
                rig.setPower(1);
            }

            else {
                rig.setPower(0);
            }

            /* if (gamepad2.left_bumper){
                rotate.setPower(-0.75);
            }

            else if (gamepad2.right_bumper){
                rotate.setPower(0.75);
            }
            else {
                rotate.setPower(0);
            } */

            //Servos

            servo.scaleRange(0,1);
            servoBoard.scaleRange(0,1);
            //servoDoor.scaleRange(0,1);

            if (gamepad1.b == true){
                servo.setPosition(0.3);
            }
            else {
                servo.setPosition(0.7);
            }

            if (gamepad1.dpad_down){
                servoBoard.setPosition(0);
            }

            else if (gamepad1.dpad_up){
                servoBoard.setPosition(1);
            }

            else {
                servoBoard.setPosition(1);
            }

            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Front right power\t: ", fl.getPower());
            telemetry.addData("Rear left power\t: ", fl.getPower());
            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Slide encoder\t", slide.getCurrentPosition());
            telemetry.update();

        }

    }

    private void slide() {

        if (touch.isPressed()){
            slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        }

        if (gamepad2.dpad_up) { //manual control
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setPower(0.9);
            slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        } else if (gamepad2.dpad_down) { //manual control
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setPower(-0.9);
            slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        } else if (gamepad2.b) { //Height 1
            slide.setTargetPosition(HEIGHT_1);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            if (slide.getCurrentPosition() > HEIGHT_1){
                slide.setPower(-LIFT_POWER);
            }
            else if (slide.getCurrentPosition() < HEIGHT_1){
                slide.setPower(LIFT_POWER);
            }

        }

        else if (gamepad2.y) { //Height 2
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slide.setTargetPosition(HEIGHT_2);
            if (slide.getCurrentPosition() > HEIGHT_2) {
                slide.setPower(-LIFT_POWER);
            } else if (slide.getCurrentPosition() < HEIGHT_2){
                slide.setPower(LIFT_POWER);
            }

            else if (gamepad2.a) { //Height 2
                slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slide.setTargetPosition(ZERO);
                if (slide.getCurrentPosition() > ZERO) {
                    slide.setPower(-LIFT_POWER);
                } else if (slide.getCurrentPosition() < ZERO){
                    slide.setPower(LIFT_POWER);
                }
        }

        if (gamepad2.left_trigger == 0 && gamepad2.right_trigger == 0 && slide.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            slide.setPower(0);
        }
    }
}}