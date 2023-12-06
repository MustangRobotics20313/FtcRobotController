/* package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class servoOpMode extends LinearOpMode {
    private Servo servoPixel;

    @Override
    public void runOpMode() {
        servoPixel = hardwareMap.servo.get("servoPixel");
        servoPixel.scaleRange(0.0,1.0);


        waitForStart();

        while (opModeIsActive()) {
            //open servo
            servoPixel.setPosition(1.0); //1 = max position
            sleep (500); //wait half second
            servoPixel.setPosition(0.0);
        }
    }
}
*/