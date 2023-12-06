package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class RevChassisDrive extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private Servo servoPixel;

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");

        //fr.setDirection(DcMotor.Direction.REVERSE);
        //rr.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();
        //Code during initialization

        while (opModeIsActive()) {

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

        }
    }
}
