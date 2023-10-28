package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class RevChassisDrive extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private Servo servoPixel;
    private DcMotor motorB;
    private DcMotor motorT;
    private ElapsedTime runtime = new ElapsedTime();

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

        motorB = hardwareMap.dcMotor.get("bjoint");
        motorT = hardwareMap.dcMotor.get("midjoint");

        waitForStart();
        //Code during initialization

        runtime.reset()
        while (opModeIsActive() && (runtime.seconds() <= 2)){
            fl.setPower(0.5);
            fr.setPower(0.5);
            fr.setPower(0.5);
            rr.setPower(0.5);
        }
        telemetry.addData("Auto", "moving forward 5 seconds");

        fl.setPower(0);
        fr.setPower(0);
        fr.setPower(0);
        rr.setPower(0);

        while(opModeIsActive()) {

            mecanum(0.75);

            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Front right power\t: ", fl.getPower());
            telemetry.addData("Rear left power\t: ", fl.getPower());
            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Servo power\t: ", servoPixel.getPower());
            // \t is an escape sequence that allows for a tab in the string

            telemetry.update();

            if (gamepad1.right_bumper) {
                servoPixel.setPosition(1.0); //open to max pos
                sleep(500); //wait half second
                servoPixel.setPosition(0.0); //close to min pos
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
