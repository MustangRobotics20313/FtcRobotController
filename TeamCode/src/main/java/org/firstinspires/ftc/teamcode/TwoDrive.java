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
    private DcMotor c2;

    //private ServoImplEx servo;

    double ServoPosition;
    double ServoSpeed;



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
        c1 = hardwareMap.get(DcMotor.class, "c1");
        c2 = hardwareMap.get(DcMotor.class, "c2");

        //servo = hardwareMap.get(ServoImplEx.class, "servo");

        //servo.setPwmRange(range);

        //intake = hardwareMap.get(DcMotor.class, "intake");
        //four = hardwareMap.get(DcMotor.class, "four");




        waitForStart();
        while(opModeIsActive()) {

            //Left Stick: General driving + rotating

            fl.setPower(-gamepad1.left_stick_y);
            fr.setPower(gamepad1.left_stick_y);
            rr.setPower(gamepad1.left_stick_y);
            rl.setPower(-gamepad1.left_stick_y);


            fl.setPower(-gamepad1.left_stick_x);
            fr.setPower(-gamepad1.left_stick_x);
            rr.setPower(-gamepad1.left_stick_x);
            rl.setPower(-gamepad1.left_stick_x);


            //Right Stick: Straffing


            fl.setPower(gamepad1.right_stick_x);
            fr.setPower(gamepad1.right_stick_x);
            rr.setPower(-gamepad1.right_stick_x);
            rl.setPower(-gamepad1.right_stick_x);

            //Lift

            if (gamepad2.x == true ) {
                lift.setPower(2);
            } else if (gamepad2.b == true) {
                lift.setPower(-2);
            } else {
                lift.setPower(0);
            }



            //Rotating arm

            if (gamepad2.left_trigger >0 ) {
                rotate.setPower(1);
            } else if (gamepad2.right_trigger >0) {
                rotate.setPower(-1);
            } else {
                rotate.setPower(0);
            }


            //Servo

            /*ServoPosition = 0.5;
            ServoSpeed = 0.01;

            if (gamepad1.y) {
            ServoPosition += ServoSpeed;
            }
            if (gamepad1.a) {
            ServoPosition += -ServoSpeed;
            }

            ServoPosition = Math.min(Math.max(ServoPosition, 0), 1);
            servo.setPosition(ServoPosition);

            sleep(20); */

            servo.scaleRange(0,1);

            if (gamepad1.b == true){
                servo.setPosition(1);
            }
            else {
                servo.setPosition(0.5);
            }


            // Compliant wheels

            if (gamepad2.left_bumper) {
                c1.setPower(0.5);
                c2.setPower(-0.5);

            } else if (gamepad2.right_bumper) {
                c1.setPower(-1);
                c2.setPower(1);

            } else {
                c1.setPower(0);
                c2.setPower(0);
            }

            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Front right power\t: ", fl.getPower());
            telemetry.addData("Rear left power\t: ", fl.getPower());
            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("c1\t: ", c1.getPower());
            telemetry.addData("c2\t: ", c2.getPower());
            telemetry.update();



        }

    }

}