package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp
public class TwoDrive extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor lift;
    private DcMotor rotate;
    private Servo servo;
    private DcMotor c1;
    private Servo servoDoor;
    private Servo servoAuton;


    //private ServoImplEx servo;

    double ServoPosition;
    double ServoSpeed;
    String speed = "standard";

    //PwmControl.PwmRange range = new PwmControl.PwmRange(usPulseLower.553, usPulseUpper.2425);

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
        servoAuton = hardwareMap.get(Servo.class, "servoAuton");
        c1 = hardwareMap.get(DcMotor.class, "c1");

        fr.setDirection(DcMotor.Direction.REVERSE);
        rl.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();
        while(opModeIsActive()) {

            //Left Stick: General driving + rotating

            fl.setPower(gamepad1.left_stick_y);
            fr.setPower(gamepad1.left_stick_y);
            rr.setPower(-gamepad1.left_stick_y);
            rl.setPower(-gamepad1.left_stick_y);

            //Rotating

            fl.setPower(-gamepad1.left_stick_x);
            fr.setPower(gamepad1.left_stick_x);
            rr.setPower(-gamepad1.left_stick_x);
            rl.setPower(gamepad1.left_stick_x);


            //Right Stick: Straffing


            fl.setPower(-gamepad1.right_stick_x);
            fr.setPower(gamepad1.right_stick_x);
            rr.setPower(gamepad1.right_stick_x);
            rl.setPower(-gamepad1.right_stick_x);

            //Lift

            lift.setPower(-gamepad2.left_stick_y);

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
            servoDoor.scaleRange(0,1);
            servoAuton.scaleRange(0,1);

            //drone launching servo

            if (gamepad1.b == true){
                servo.setPosition(0.5);
            }
            else {
                servo.setPosition(1);
            }

            //autonomous servo

            if (gamepad2.b == true){
                servoAuton.setPosition(0);
            }

            else if (gamepad2.x == true){
                servoAuton.setPosition(0.5);

            }

            //door servo

            if (gamepad2.dpad_down){
                servoDoor.setPosition(0);
            }

            else if (gamepad2.dpad_up){
                servoDoor.setPosition(1);
            }

            else {
                servoDoor.setPosition(1);
            }


            // Compliant wheels

            if (gamepad2.left_bumper) {
                c1.setPower(1);


            } else if (gamepad2.right_bumper) {
                c1.setPower(-0.5);


            } else {
                c1.setPower(0);

            }

            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Front right power\t: ", fl.getPower());
            telemetry.addData("Rear left power\t: ", fl.getPower());
            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("c1\t: ", c1.getPower());
            telemetry.addData("Speed\t: ", speed);
            telemetry.update();



        }

    }

}