package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

/*
 * This is an example of a more complex path to really test the tuning.
 */
@Autonomous(group = "drive")



public class SplineTest extends LinearOpMode {

    private DcMotor slide;
    private Servo servoRotate;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        slide = hardwareMap.get(DcMotor.class, "slide");
        servoRotate = hardwareMap.get(Servo.class, "servoRotate");
        servoRotate.scaleRange(0,1);

        slide.setDirection(DcMotor.Direction.REVERSE);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        if (isStopRequested()) return;

        Trajectory traj = drive.trajectoryBuilder(new Pose2d())
                .splineTo(new Vector2d(20, 20), 0)
                .build();

        drive.followTrajectory(traj);

        sleep(2000);

        slide.setTargetPosition(800);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setPower(0.5);
        sleep(1000);
        servoRotate.setPosition(0.5);
        sleep(1000);

        /* drive.followTrajectory(
                drive.trajectoryBuilder(traj.end(), true)
                        .splineTo(new Vector2d(0, 0), Math.toRadians(180))
                        .build()
        ); */

    }

    public void lift() {

        slide.setTargetPosition(800);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setPower(0.5);

        /* if (slide.getCurrentPosition() > 800) {
            slide.setPower(-0.5);
        } else if (slide.getCurrentPosition() < 800) {
            slide.setPower(0.5);
        } */

    }

}

