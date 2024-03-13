package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.FeedforwardController;



@TeleOp
@Config
public class PIDMecanumDrive extends LinearOpMode {
    RobotDevices devices = new RobotDevices(hardwareMap);
    private DcMotorEx fl = devices.fl;
    private DcMotorEx fr = devices.fr;
    private DcMotorEx rl = devices.rl;
    private DcMotorEx rr = devices.rr;

    public static double scale_factor = 10; // [in/s / [joystick unit]]
    public static double acceleration = 10; // [in/(s^2)]

    FeedforwardController ff = new FeedforwardController(0.1, 0.2, 0.3);


    @Override
    public void runOpMode() {
        devices.disableEncoders();
        devices.reverseMotors();
        devices.setDriveMotorBraking();

        waitForStart();

        while(opModeIsActive()) {
            mecanumDrive();
        }
    }

    private void mecanumDrive() {
        double drive = gamepad1.left_stick_y;
        double strafe = -gamepad1.right_stick_x;
        double twist = -gamepad1.left_stick_x;

        double fl_reference = (drive + strafe - twist) * scale_factor;
        double fr_reference = (drive - strafe + twist) * scale_factor;
        double rl_reference = (drive - strafe - twist) * scale_factor;
        double rr_reference = (drive + strafe + twist) * scale_factor;

        fl.setPower(ff.calculate(fl_reference, acceleration));
        fr.setPower(ff.calculate(fr_reference, acceleration));
        rl.setPower(ff.calculate(rl_reference, acceleration));
        rr.setPower(ff.calculate(rr_reference, acceleration));
    }
}
