package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class DualSlideTesting extends LinearOpMode {
    private DcMotorEx left_slide;
    private DcMotorEx right_slide;


    @Override
    public void runOpMode() {
        left_slide = hardwareMap.get(DcMotorEx.class, "left_slide");
        right_slide = hardwareMap.get(DcMotorEx.class, "right_slide");

        left_slide.setDirection(DcMotorEx.Direction.REVERSE);

        left_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                left_slide.setPower(0.1);
            } else if (gamepad1.b) {
                left_slide.setPower(-0.1);
            } else {
                left_slide.setPower(0);
            }

            if (gamepad1.x) {
                right_slide.setPower(0.1);
            } else if (gamepad1.y) {
                right_slide.setPower(-0.1);
            } else {
                right_slide.setPower(0);
            }

            if (gamepad1.options) {
                if (left_slide.getZeroPowerBehavior() == DcMotor.ZeroPowerBehavior.BRAKE) {
                    left_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                    right_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                } else {
                    left_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    right_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                }
            }

            /*
             if (gamepad1.left_bumper) {
                right_slide.setPower(0.1);
                left_slide.setPower(0.1);
             } else if (gamepad1.right_bumper) {
                right_slide.setPower(-0.1);
                left_slide.setPower(-0.1);
             } else {
                left_slide.setPower(0);
                right_slide.setPower(0);
             }
             */
        }
    }
}
