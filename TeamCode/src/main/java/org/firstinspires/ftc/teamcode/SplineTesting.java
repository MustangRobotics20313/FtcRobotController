package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;


@Autonomous(name = "SplineTesting", group = "Concept")
@Disabled

public class SplineTesting extends LinearOpMode {

    private String position;

    @Override
    public void runOpMode() {

        /* servoAuton = hardwareMap.get(Servo.class, "servoAuton");
        servoAuton.scaleRange(0,1); */

        waitForStart();

        if (opModeIsActive() && !isStopRequested()) {
            while (opModeIsActive()) {

                SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
                Pose2d startPose = new Pose2d(0, 0, (Math.toRadians(90)));

                Vector2d leftSpike = new Vector2d(-4, 30);
                Vector2d middleSpike = new Vector2d(0, 40);
                Vector2d rightSpike = new Vector2d(8.25, 36);

                drive.setPoseEstimate(startPose);

                //Spline to left spike mark, drop purple pixel, put yellow pixel on board, park

                /* TrajectorySequence dropLeftSpike = drive.trajectorySequenceBuilder(startPose)
                        .forward(12)
                        .splineTo(leftSpike, Math.toRadians(180))
                        .waitSeconds(0.5)
                        .forward(5)
                        .forward(-6.25)
                        .strafeRight(1.75)
                        .build();

                drive.followTrajectorySequence(dropLeftSpike);

                break; */

                //Spline to middle spike mark, drop purple pixel, put yellow pixel on board, park

                /* TrajectorySequence dropMiddleSpike = drive.trajectorySequenceBuilder(startPose)
                            .splineTo(middleSpike, Math.toRadians(90))
                            .forward(-7)
                            .build();

                    drive.followTrajectorySequence(dropMiddleSpike);

                    break; */




                    //Spline to middle spike mark, drop purple pixel, put yellow pixel on board, park

                TrajectorySequence dropRightSpike = drive.trajectorySequenceBuilder(startPose)
                        .splineTo(rightSpike, Math.toRadians(0))
                        .waitSeconds(0.5)
                        .forward(5)
                        .forward(-7)
                        .build();

                drive.followTrajectorySequence(dropRightSpike);

                    break;


            }


        }
    }

    }  // end runOpMode()


