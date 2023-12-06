package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class LinearActuatorTest extends LinearOpMode{

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor hangMotor;

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");

        fr.setDirection(DcMotor.Direction.REVERSE);
        rr.setDirection(DcMotor.Direction.REVERSE);

        hangMotor = hardwareMap.dcMotor.get("hangMotor");

        waitForStart();
        //Code during initialization

        while (opModeIsActive()) {

            mecanum(0.75);

            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Front right power\t: ", fl.getPower());
            telemetry.addData("Rear left power\t: ", fl.getPower());
            telemetry.addData("Front left power\t: ", fl.getPower());
            // \t is an escape sequence that allows for a tab in the string

            telemetry.update();

            if (gamepad1.a) {
                hangMotor.setPower(0.5);
            }

            if (gamepad1.b) {
                hangMotor.setPower(0.5);
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
