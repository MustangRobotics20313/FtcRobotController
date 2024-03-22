package org.firstinspires.ftc.teamcode;

import android.hardware.Sensor;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp
@Config
@Disabled

public class SlideTesting extends LinearOpMode {

    private DcMotor lift;
    private TouchSensor touch;

    //units of ticks

    private final int HEIGHT_1 = 900;
    private final int HEIGHT_2 = 1200;
    private final int HEIGHT_3 = 1400;


    private final double SLIDE_POWER = 0.5;
    public final double LIFT_POWER = 0.75;



    @Override
    public void runOpMode() {

        lift = hardwareMap.get(DcMotor.class, "lift");
        touch = hardwareMap.get(TouchSensor.class, "touch");

        //lift.setDirection(DcMotor.Direction.REVERSE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        String direction = "collection";

        waitForStart();
        while(opModeIsActive()) {

            //Lift

            lift();

            telemetry.addData("Lift encoder\t", lift.getCurrentPosition());
            telemetry.update();

        }

    }

    private void lift() {

        if (touch.isPressed()){
            lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        }

        if (gamepad2.dpad_up) { //manual control
            lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            lift.setPower(0.9);
            lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        } else if (gamepad2.dpad_down) { //manual control
            lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            lift.setPower(-0.9);
            lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        } else if (gamepad2.b) { //Height 1
            lift.setTargetPosition(HEIGHT_1);
            lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            if (lift.getCurrentPosition() > HEIGHT_1){
                lift.setPower(-LIFT_POWER);
            }
            else if (lift.getCurrentPosition() < HEIGHT_1){
                lift.setPower(LIFT_POWER);
            }

        }

        else if (gamepad2.a) { //Height 2
            lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            lift.setTargetPosition(HEIGHT_2);
            if (lift.getCurrentPosition() > HEIGHT_2) {
                lift.setPower(-LIFT_POWER);
            } else if (lift.getCurrentPosition() < HEIGHT_2){
                lift.setPower(LIFT_POWER);
            }
        }

        if (gamepad2.left_trigger == 0 && gamepad2.right_trigger == 0 && lift.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            lift.setPower(0);
        }
    }
}