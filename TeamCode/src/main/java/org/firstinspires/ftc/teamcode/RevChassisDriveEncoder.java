package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class RevChassisDriveEncoder extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private Servo servoPixel;
    private DcMotorEx motorB;
    private DcMotorEx motorT;

    @Override
    public void runOpMode() {

        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");

        fr.setDirection(DcMotor.Direction.REVERSE);
        rr.setDirection(DcMotor.Direction.REVERSE);

        servoPixel = hardwareMap.servo.get("servoPixel");
        servoPixel.scaleRange(0.0,1.0); //sets min and max number for servo

        motorB = hardwareMap.get(DcMotorEx.class,"bjoint");
        motorT = hardwareMap.get(DcMotorEx.class,"midjoint");

        motorT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        //Code during initialization


        while(opModeIsActive()) {

            mecanum(0.75);
            joints();
            grabber();

            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Front right power\t: ", fl.getPower());
            telemetry.addData("Rear left power\t: ", fl.getPower());
            telemetry.addData("Front left power\t: ", fl.getPower());
            // \t is an escape sequence that allows for a tab in the string

            telemetry.update();




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

    private void joints() {
        if(gamepad1.y){
            motorB.setPower(.5); //moves up
        }else if(gamepad1.x){
            motorB.setPower(-.5); //moves down
        }else{
            motorB.setPower(0);
        }
        if(gamepad1.b){
            motorT.setPower(.5); //moves up
        }else if(gamepad1.a){
            motorT.setPower(-.5); //moves down
        }else{
            motorT.setPower(0);

        }
    }

    private void grabber() {
        if (gamepad1.left_stick_button) {
            servoPixel.setPosition(0.2);
        } else if (gamepad1.right_stick_button) {
            servoPixel.setPosition(0.5);
        }
    }



}