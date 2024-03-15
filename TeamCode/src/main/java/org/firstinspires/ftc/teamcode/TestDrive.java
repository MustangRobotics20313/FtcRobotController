package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp
public class TestDrive extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor lift;
    private DcMotor rotate;
    private DcMotor slide;
    private Servo servo;
    private DcMotor c1;
    private Servo servoRotate;
    private Servo servoAuton;
    private Servo servoBoard;
    private Servo servoPixel;
    private TouchSensor touch;


    //private ServoImplEx servo;

    double ServoPosition;
    double ServoSpeed;
    String speed = "standard";
    private double SPEED_MULTIPLIER = 0.5;

    private final int HEIGHT_1 = 800;
    private final int HEIGHT_2 = 400;
    private final int HEIGHT_3 = 1400;
    private final int ZERO = -20;



    private final double SLIDE_POWER = 0.5;
    public final double LIFT_POWER = 0.75;

    //PwmControl.PwmRange range = new PwmControl.PwmRange(usPulseLower.553, usPulseUpper.2425);

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        lift = hardwareMap.get(DcMotor.class, "lift");
        rotate = hardwareMap.get(DcMotor.class, "rotate");
        slide = hardwareMap.get(DcMotor.class, "slide");
        servo = hardwareMap.get(Servo.class, "servo");
        servoAuton = hardwareMap.get(Servo.class, "servoAuton");
        servoRotate = hardwareMap.get(Servo.class, "servoRotate");
        servoBoard = hardwareMap.get(Servo.class, "servoBoard");
        servoPixel = hardwareMap.get(Servo.class, "servoPixel");
        touch = hardwareMap.get(TouchSensor.class, "touch");

        fl.setDirection(DcMotor.Direction.REVERSE);
        rl.setDirection(DcMotor.Direction.REVERSE);

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();
        while(opModeIsActive()) {

            mecanumDrive();

            if (gamepad1.dpad_up) {
                speed = "fast";
                SPEED_MULTIPLIER = 0.8;
            }

            else if (gamepad1.dpad_down){
                speed = "slow";
                SPEED_MULTIPLIER = 0.3;
            }


            /*
            //Left Stick: General driving + rotating

            fl.setPower(gamepad1.left_stick_y*1.5);
            fr.setPower(gamepad1.left_stick_y *1.5);
            rr.setPower(gamepad1.left_stick_y *1.5);
            rl.setPower(gamepad1.left_stick_y *1.5);

            //Rotating

            fl.setPower(gamepad1.left_stick_x* 1.5);
            fr.setPower(-gamepad1.left_stick_x* 1.5);
            rr.setPower(-gamepad1.left_stick_x * 1.5);
            rl.setPower(gamepad1.left_stick_x * 1.5);


            //Right Stick: Straffing


            fl.setPower(-gamepad1.right_stick_x * 1.5);
            fr.setPower(gamepad1.right_stick_x * 1.5);
            rr.setPower(-gamepad1.right_stick_x * 1.5);
            rl.setPower(gamepad1.right_stick_x * 1.5);


             */


            //Lift

            lift.setPower(-gamepad2.left_stick_y);

            if (gamepad2.left_stick_y == 0) {
                lift.setPower(0);
            }

            //slide

            slide();
            if (touch.isPressed()){
                slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            }


            //Rotating arm

            if (gamepad2.left_trigger >0 ) {
                rotate.setPower(1);
            } else if (gamepad2.right_trigger >0) {
                rotate.setPower(-1);
            } else {
                rotate.setPower(0);
            }


            //Servos

            servo.scaleRange(0,1);
            servoAuton.scaleRange(0,1);
            servoBoard.scaleRange(0,1);
            servoRotate.scaleRange(0,1);
            servoPixel.scaleRange(0,1);

            //drone launching servo

            if (gamepad1.b == true){
                servo.setPosition(0.1 );
            }
            else {
                servo.setPosition(0.3);
            }

            servoBoard.setPosition(0.4);

            //pixel servo

            if (gamepad2.left_bumper == true){
                servoPixel.setPosition(0.27);
            }

            else if (gamepad2.right_bumper == true){
                servoPixel.setPosition(0.6);

            }

            //rotating  servo

            if (gamepad2.dpad_down){
                servoRotate.setPosition(0.45);
            }

            else if (gamepad2.dpad_up) {
                servoRotate.setPosition(0.2);
            }



            // Compliant wheels

            /* if (gamepad2.left_bumper) {
                c1.setPower(1);


            } else if (gamepad2.right_bumper) {
                c1.setPower(-0.5);


            } else {
                c1.setPower(0);

            } */

            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Front right power\t: ", fl.getPower());
            telemetry.addData("Rear left power\t: ", fl.getPower());
            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Lift encoder\t",  slide.getCurrentPosition());
            telemetry.addData("Speed\t: ", speed);
            telemetry.update();


        }

    }

    private void mecanumDrive(){

        speed = "fast";

        //speedier driving?!?! idk let's find out

        double drive = gamepad1.left_stick_y;
        double strafe = -gamepad1.right_stick_x;
        double twist = -gamepad1.left_stick_x;

        double v1 = drive + strafe - twist;
        double v2 = drive - strafe + twist;
        double v3 = drive - strafe - twist;
        double v4 = drive + strafe + twist;

        fl.setPower(v1 * SPEED_MULTIPLIER);
        fr.setPower(v2 * SPEED_MULTIPLIER);
        rl.setPower(v3 * SPEED_MULTIPLIER);
        rr.setPower(v4 * SPEED_MULTIPLIER);

    }


    private void slide() {

        if (touch.isPressed()){
            slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        }

        if (gamepad2.right_stick_y > 0 || gamepad2.right_stick_y < 0){
        slide.setPower(gamepad2.right_stick_y); }

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
                slide.setPower(-LIFT_POWER);
            }
            else if (slide.getCurrentPosition() < HEIGHT_1){
                slide.setPower(LIFT_POWER);
            }

        }

        else if (gamepad2.y) { //Height 2
            slide.setTargetPosition(HEIGHT_2);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > HEIGHT_2) {
                slide.setPower(-LIFT_POWER);
            } else if (slide.getCurrentPosition() < HEIGHT_2) {
                slide.setPower(LIFT_POWER);

            }
        }

        else if (gamepad2.a) { //Height 2
            slide.setTargetPosition(-15);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            if (slide.getCurrentPosition() > -15) {
                slide.setPower(-LIFT_POWER);
            } else if (slide.getCurrentPosition() < -15) {
                slide.setPower(LIFT_POWER);

            }
        }


            if (gamepad2.left_trigger == 0 && gamepad2.right_trigger == 0 && slide.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            slide.setPower(0);
        }

        }}