package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
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
    private Servo servoRotate;
    private DigitalChannel touch;

    double ServoPosition;
    double ServoSpeed;

    PwmControl.PwmRange range = new PwmControl.PwmRange(500, 2500);


    @Override
    public void runOpMode() {

        servoRotate = hardwareMap.get(Servo.class, "servoRotate");
        //touch = hardwareMap.get(DigitalChannel.class, "touch");

        waitForStart();
        while(opModeIsActive()){

            /* if (touch.isPressed()) {
                telemetry.addData("Touch: ", 1);

            }

            else {
                telemetry.addData("Touch: ", 0);

            } */

            servoRotate.scaleRange(0,1);

            if (gamepad2.dpad_up){
                servoRotate.setPosition(0.75);
            }

            else if (gamepad2.dpad_down){
                servoRotate.setPosition(0.85);
            }

            telemetry.addData("Servo Position: ", servoRotate.getPosition());
            telemetry.update();



        }



    }
}
