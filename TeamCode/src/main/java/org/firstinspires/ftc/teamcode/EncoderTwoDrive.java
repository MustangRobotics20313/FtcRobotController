package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.dashboard.config.Config;

@TeleOp
@Config
public class EncoderTwoDrive extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor lift;
    private DcMotor rotate;
    private Servo servo;
    private Servo servoDoor;
    private DcMotor c1;




    double ServoPosition;
    double ServoSpeed;

    //units of ticks

    private final int PIXEL_RELEASE_POSITION = 4500;
    private final int HANGING_POSITION = 3860;


    private final double ROTATE_POWER = 0.75;
    public static int encoderPosition = 4500;

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        lift = hardwareMap.get(DcMotor.class, "lift");
        rotate = hardwareMap.get(DcMotor.class, "rotate");
        servo = hardwareMap.get(Servo.class, "servo");
        servoDoor = hardwareMap.get(Servo.class, "servoDoor");
        c1 = hardwareMap.get(DcMotor.class, "c1");


        fl.setDirection(DcMotor.Direction.REVERSE);
        rl.setDirection(DcMotor.Direction.REVERSE);
        rotate.setDirection(DcMotor.Direction.REVERSE);

        rotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();
        while(opModeIsActive()) {



            //Left Stick: General driving + rotating

            fl.setPower(gamepad1.left_stick_y);
            fr.setPower(gamepad1.left_stick_y);
            rr.setPower(-gamepad1.left_stick_y);
            rl.setPower(-gamepad1.left_stick_y);


            fl.setPower(gamepad1.left_stick_x);
            fr.setPower(-gamepad1.left_stick_x);
            rr.setPower(-gamepad1.left_stick_x);
            rl.setPower(gamepad1.left_stick_x);


            //Right Stick: Strafing


            fl.setPower(gamepad1.right_stick_x);
            fr.setPower(-gamepad1.right_stick_x);
            rr.setPower(gamepad1.right_stick_x);
            rl.setPower(-gamepad1.right_stick_x);

            //Lift

            lift.setPower(-gamepad2.left_stick_y);


            //Rotating arm

            rotate();
            // resetting rotate encoder
            if (gamepad2.y) {
                rotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }


            //Servos

            servo.scaleRange(0,1);
            servoDoor.scaleRange(0,1);

            if (gamepad1.b == true){
                servo.setPosition(1);
            }
            else {
                servo.setPosition(0);
            }

            if (gamepad2.dpad_down){
                servoDoor.setPosition(0.75);
            }

            else if (gamepad2.dpad_up){
                servoDoor.setPosition(0.6);
            }

            else {
                servoDoor.setPosition(0.6);
            }


            // Compliant wheels

            if (gamepad2.left_bumper) {
                c1.setPower(0.5);


            } else if (gamepad2.right_bumper) {
                c1.setPower(-1);


            } else {
                c1.setPower(0);

            }

            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Front right power\t: ", fl.getPower());
            telemetry.addData("Rear left power\t: ", fl.getPower());
            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("c1\t: ", c1.getPower());
            telemetry.addData("Rotating encoder\t:", rotate.getCurrentPosition());
            telemetry.update();


        }




    }

    private void rotate() {

        if (gamepad2.left_trigger > 0) { //manual control
            rotate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rotate.setPower(ROTATE_POWER);
        } else if (gamepad2.right_trigger > 0) { //manual control
            rotate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rotate.setPower(-ROTATE_POWER);
        } else if (gamepad2.b) { //parallel to board
            rotate.setTargetPosition(4500);
            rotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rotate.setPower(-0.75);
        } else if (gamepad2.y) { //hanging position
            rotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rotate.setTargetPosition(3860);
            if (rotate.getCurrentPosition() > -3860) {
                rotate.setPower(-ROTATE_POWER);
            } else {
                rotate.setPower(ROTATE_POWER);
            }
        }

        if (gamepad2.left_trigger == 0 && gamepad2.right_trigger == 0 && rotate.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            rotate.setPower(0);
        }
    }
}