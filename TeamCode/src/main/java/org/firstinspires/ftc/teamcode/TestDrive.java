package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp
public class TestDrive extends LinearOpMode {

    private enum DriveDirection{
        FORWARD,
        BACKWARD
    }

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor slide;
    private DcMotor lift;
    private Servo servo;
    private DcMotor intake;
    private Servo servoRotate;
    private Servo servoAuton;
    private Servo servoPixel;
    private Servo servoHang;
    private TouchSensor touch;


    //private ServoImplEx servo;

    double ServoPosition;
    double ServoSpeed;
    String speed = "standard";
    private double SPEED_MULTIPLIER = 0.5;
    private double DIRECTION_SWITCH_S = 1;
    private double DIRECTION_SWITCH_FB= 1;

    private final int HEIGHT_1 = 1200;
    private final int HEIGHT_2 = 1600;
    private final int HEIGHT_3 = 1200;
    private final int ZERO = -20;
    private int count = 0;

    double drive;
    double strafe;
    double twist;



    private final double SLIDE_POWER = 0.5;
    public final double LIFT_POWER = 0.75;

    //PwmControl.PwmRange range = new PwmControl.PwmRange(usPulseLower.553, usPulseUpper.2425);

    private DriveDirection direction = DriveDirection.FORWARD;

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        slide = hardwareMap.get(DcMotor.class, "slide");
        intake = hardwareMap.get(DcMotor.class, "intake");
        lift = hardwareMap.get(DcMotor.class, "lift");
        servo = hardwareMap.get(Servo.class, "servo");
        servoAuton = hardwareMap.get(Servo.class, "servoAuton");
        servoRotate = hardwareMap.get(Servo.class, "servoRotate");
        servoPixel = hardwareMap.get(Servo.class, "servoPixel");
        servoHang = hardwareMap.get(Servo.class, "servoHang");
        touch = hardwareMap.get(TouchSensor.class, "touch");

        fl.setDirection(DcMotor.Direction.REVERSE);
        rl.setDirection(DcMotor.Direction.REVERSE);
        slide.setDirection(DcMotor.Direction.REVERSE);

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        while(opModeIsActive()) {

            if (count == 0) {
                servoRotate.setPosition(0.865);
                count += 1;
            }

            mecanumDrive();

            if (gamepad1.dpad_up) {
                speed = "fast";
                SPEED_MULTIPLIER = 0.85;
            }

            else if (gamepad1.dpad_down){
                speed = "slow";
                SPEED_MULTIPLIER = 0.5;
            }


            //Lift
            lift.setPower(-gamepad2.left_stick_y);

            if (gamepad2.left_stick_y == 0) {
                lift.setPower(0);
            }

            //slide

            slide();


            //Rotating arm

            /* if (gamepad2.left_trigger >0 ) {
                rotate.setPower(1);
            } else if (gamepad2.right_trigger >0) {
                rotate.setPower(-1);
            } else {
                rotate.setPower(0);
            } */


            //Servos

            //servo.scaleRange(0,1);
            servoAuton.scaleRange(0,1);
            //servoBoard.scaleRange(0,1);
            servoRotate.scaleRange(0,1);
            servoPixel.scaleRange(0,1);
            servoHang.scaleRange(0,1);


            //drone launching servo

            if (gamepad1.b == true){
                servo.setPosition(0.6);
            }
            else {
                servo.setPosition(0);
            }

            //pixel servo

           if (gamepad2.right_bumper){
                servoPixel.setPosition(0.5);
            }

           else{
               servoPixel.setPosition(0.1);

           }

            //rotating  servo

            if (gamepad2.dpad_up) {
                servoRotate.setPosition(0.745);
            }

            else if (gamepad2.dpad_down) {
                servoRotate.setPosition(0.865);
            }

            //hanging servo

            if (gamepad2.x){
                servoHang.setPosition(0.5);
            }

            else {
               servoHang.setPosition(0);
            }

            //

            // Intake system

            if (gamepad1.right_trigger > 0) {
                intake.setPower(1);
                servoPixel.setPosition(0.5);


            } else if (gamepad1.left_trigger > 0) {
                intake.setPower(-0.5);


            } else {
                intake.setPower(0);

            }

            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Front right power\t: ", fl.getPower());
            telemetry.addData("Rear left power\t: ", fl.getPower());
            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Lift encoder\t",  slide.getCurrentPosition());
            telemetry.addData("Speed\t: ", speed);
            telemetry.update();


        }

    }

    private void mecanumDrive() {

        drive = gamepad1.left_stick_y ;
        strafe = -gamepad1.right_stick_x;
        twist = gamepad1.left_stick_x;

        if (gamepad1.a) {
            if(direction == DriveDirection.FORWARD) {
                direction = DriveDirection.BACKWARD;
            } else if(direction == DriveDirection.BACKWARD) {
                direction = DriveDirection.FORWARD;
            }
        }

        double v1 = drive + strafe - twist;
        double v2 = drive - strafe + twist;
        double v3 = drive - strafe - twist;
        double v4 = drive + strafe + twist;

        if(direction == DriveDirection.FORWARD) {
            fl.setPower(v1 * SPEED_MULTIPLIER);
            fr.setPower(v2 * SPEED_MULTIPLIER);
            rl.setPower(v3 * SPEED_MULTIPLIER);
            rr.setPower(v4 * SPEED_MULTIPLIER);
        } else if (direction == DriveDirection.BACKWARD) {
            rr.setPower(v1 * SPEED_MULTIPLIER);
            rl.setPower(v2 * SPEED_MULTIPLIER);
            fr.setPower(v3 * SPEED_MULTIPLIER);
            fl.setPower(v4 * SPEED_MULTIPLIER);
        }
    }


    private void slide() {

        if (touch.isPressed()){
            slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        }

        /* if (gamepad2.right_stick_y > 0 || gamepad2.right_stick_y < 0){
        slide.setPower(gamepad2.right_stick_y); } */

        /* if (gamepad2.dpad_up) { //manual control
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setPower(0.85);
            slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        } else if (gamepad2.dpad_down) { //manual control
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setPower(-0.85);
            slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); */

        if (gamepad2.b) { //Height 1
            slide.setTargetPosition(HEIGHT_1);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            if (slide.getCurrentPosition() > HEIGHT_1){
                slide.setPower(-SLIDE_POWER);
            }
            else if (slide.getCurrentPosition() < HEIGHT_1){
                slide.setPower(SLIDE_POWER);
            }

        }

        else if (gamepad2.y) { //Height 2
            slide.setTargetPosition(HEIGHT_2);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > HEIGHT_2) {
                slide.setPower(-SLIDE_POWER);
            } else if (slide.getCurrentPosition() < HEIGHT_2) {
                slide.setPower(SLIDE_POWER);

            }
        }

        else if (gamepad2.a) { //zero position
            slide.setTargetPosition(-15);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            if (slide.getCurrentPosition() > -15) {
                slide.setPower(-SLIDE_POWER);
            } else if (slide.getCurrentPosition() < -15) {
                slide.setPower(SLIDE_POWER);

            }
        }


            if (gamepad2.left_trigger == 0 && gamepad2.right_trigger == 0 && slide.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            slide.setPower(0);
        }

        }}