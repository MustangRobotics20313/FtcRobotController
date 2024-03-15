package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;


@TeleOp
public class PIDAprilTagAlignment extends LinearOpMode {

    RobotDevices devices = new RobotDevices(hardwareMap, RobotDevices.DeviceType.ALL);
    private DcMotorEx fl = devices.m1;
    private DcMotorEx fr = devices.m2;
    private DcMotorEx rl = devices.m3;
    private DcMotorEx rr = devices.m4;
    private OpenCvCamera camera = devices.camera;
    private FtcDashboard dashboard = devices.dashboard;


    @Override
    public void runOpMode() {
        devices.disableEncoders();
        devices.reverseDriveMotors();
        devices.setDriveMotorBraking();

        waitForStart();

        while(opModeIsActive()) {

        }
    }

    private void startCam() {
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
                dashboard.startCameraStream(camera, 0);
            }

            @Override
            public void onError(int errorCode) {}
        });
    }
}
