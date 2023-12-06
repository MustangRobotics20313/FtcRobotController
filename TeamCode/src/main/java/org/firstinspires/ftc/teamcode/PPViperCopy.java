package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

//uncalled imports are likely due to a result of the viper
// code being omitted from the original

@TeleOp

public class PPViperCopy extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
            //Declares motors
            //Make sure the ID's match your configuration
        DcMotor FrontLeft = hardwareMap.dcMotor.get("frontLeft");
        //change to "fl" to match current hardware configuration on driver station?
        DcMotor BackLeft = hardwareMap.dcMotor.get("backLeft");
        DcMotor FrontRight = hardwareMap.dcMotor.get("frontRight");
        DcMotor BackRight = hardwareMap.dcMotor.get("backRight");
        //this copy excludes viper motor from original program, superfluous to strafing

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");

            double y = 0.75*gamepad1.left_stick_y; //reversed, 0.75 slows motors
            double x = 0.75*-gamepad1.left_stick_x*1.1; //counteracts imperfect strafing
            double rx = 0.75*-gamepad1.right_stick_x;

            // Denominator is the largest motor power (abs. value) or 1
            // this is to ensure all powers maintain same ratio but only when
            // at least one is out of the range [-1, 1]

            double denominator = Math.abs(y) + Math.abs(x) + Math.abs(rx);
            //original code has "Math.abs(rx), 1)" but results in error?
            double frontLeftPower = (y+x+rx)/denominator;
            double backLeftPower = (y-x+rx)/denominator;
            double frontRightPower = (y-x-rx)/denominator;
            double backRightPower = (y+x-rx)/denominator;

            FrontLeft.setPower(frontLeftPower);
            BackLeft.setPower(backLeftPower);
            FrontRight.setPower(frontRightPower);
            BackRight.setPower(backRightPower);

        }
    }
}
