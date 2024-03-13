package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp
public class ServoTesting extends LinearOpMode {
    //private Servo servoTest;
    private Servo servoTest;

    double ServoPosition;
    double ServoSpeed;

    PwmControl.PwmRange range = new PwmControl.PwmRange(500, 2500);


    @Override
    public void runOpMode() {

        servoTest = hardwareMap.get(Servo.class, "servoTest");

        waitForStart();
        while(opModeIsActive()){

            servoTest.scaleRange(0,1);

            if (gamepad2.dpad_up){
                servoTest.setPosition(0.75);
            }

            else if (gamepad2.dpad_down){
                servoTest.setPosition(0.25);
            }

            telemetry.addData("Servo Position: ", servoTest.getPosition());
            telemetry.update();

        }



    }
}
