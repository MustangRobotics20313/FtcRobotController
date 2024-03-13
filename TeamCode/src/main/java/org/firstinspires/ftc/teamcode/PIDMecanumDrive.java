package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.RobotDevices;



@TeleOp
public class PIDMecanumDrive extends LinearOpMode {
    RobotDevices devices = new RobotDevices(hardwareMap);
    private DcMotorEx fl = devices.fl;
    private DcMotorEx fr = devices.fr;
    private DcMotorEx rl = devices.rl;
    private DcMotorEx rr = devices.rr;

    double kS;
    double kV;
    double kA;


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

        double fl_reference = drive + strafe - twist;
        double fr_reference = drive - strafe + twist;
        double rl_reference = drive - strafe - twist;
        double rr_reference = drive + strafe + twist;


    }
}
