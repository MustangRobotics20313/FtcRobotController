package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;


@Autonomous(name = "Auton_April", group = "Concept")
@Disabled

public class Auton_April extends LinearOpMode {

    final double DESIRED_DISTANCE = 12.0; //  this is how close the camera should get to the target (inches)

    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
    //  applied to the drive motors to correct the error.
    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
    final double SPEED_GAIN  =  0.02  ;   //  Forward Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".  eg: Ramp up to 25% power at a 25 degree Yaw error.   (0.25 / 25.0)
    final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_SPEED = 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_STRAFE= 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)

    private DcMotor rl   = null;  //  Used to control the left front drive wheel
    private DcMotor rr  = null;  //  Used to control the right front drive wheel
    private DcMotor fl    = null;  //  Used to control the left back drive wheel
    private DcMotor fr   = null;  //  Used to control the right back drive wheel

    boolean targetFound     = false;    // Set to true when an AprilTag target is detected
    double  forward           = 0;        // Desired forward power/speed (-1 to +1)
    double  strafe          = 0;        // Desired strafe power/speed (-1 to +1)
    double  turn            = 0;        // Desired turning power/speed (-1 to +1)

    double rangeError;
    double headingError;
    double yawError;

    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
    private static int DESIRED_TAG_ID;     // Choose the tag you want to approach or set to -1 for ANY tag.
    private VisionPortal visionPortal;               // Used to manage the video source.
    private AprilTagProcessor aprilTag;              // Used for managing the AprilTag detection process.
    private AprilTagDetection desiredTag = null;     // Used to hold the data for a detected AprilTag

    // TFOD_MODEL_ASSET points to a model file stored in the project Asset location,
    // this is only used for Android Studio when using models in Assets.
    private static final String TFOD_MODEL_ASSET = "blue_red_jan_update.tflite";
    // TFOD_MODEL_FILE points to a model file stored onboard the Robot Controller 's storage,
    // this is used when uploading models directly to the RC using the model upload interface.
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/myCustomModel.tflite";
    // Define the labels recognized in the model for TFOD (must be in training order!)
    private static final String[] LABELS = {
            "blue element", "pixel", "redElement"
    };

    private TfodProcessor tfod; //stores instance of TensorFlow Object Detection processor


    double x;
    double y;
    String position;


    private Servo servoAuton;
    private Servo servoBoard;


    @Override
    public void runOpMode() {

        servoAuton = hardwareMap.get(Servo.class, "servoAuton");
        servoBoard = hardwareMap.get(Servo.class, "servoBoard");
        servoAuton.scaleRange(0,1);
        servoBoard.scaleRange(0,1);

        initTfod();

        while (!opModeIsActive()) {
            if (getPosition() > 0 && getPosition() < 100) {
                position = "left";
                DESIRED_TAG_ID = 0;

            } else if (getPosition() > 100 && getPosition() < 500) {
                position = "middle";
                DESIRED_TAG_ID = 1;

            } else if (getPosition() > 500 && getPosition() < 600) {
                position = "right";
                DESIRED_TAG_ID = 2;

            } else {
                position = "unable to detect";
            }

            telemetryTfod();
            telemetry.addData("Spike mark:", position);
            telemetry.update();
        }

        waitForStart();


        if (opModeIsActive() && !isStopRequested()) {
            while (opModeIsActive()) {
                visionPortal.close();

                SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
                Pose2d startPose = new Pose2d(0, 0, (Math.toRadians(90)));

                Vector2d leftSpike = new Vector2d(-4, 29);
                Vector2d middleSpike = new Vector2d(0, 36.75);
                Vector2d rightSpike = new Vector2d(8.25, 35);

                Vector2d leftBoard = new Vector2d(1 , -30);
                Vector2d middleBoard = new Vector2d(42, -3);
                Vector2d rightBoard = new Vector2d(12, 39);


                drive.setPoseEstimate(startPose);

                if (position == "left"){
                    //Spline to left spike mark, drop purple pixel

                    TrajectorySequence dropLeftSpike = drive.trajectorySequenceBuilder(startPose)
                            .forward(12)
                            .splineTo(leftSpike, Math.toRadians(180))
                            .waitSeconds(0.5)
                            .forward(5)
                            .forward(-8)
                            .strafeRight(1.75)
                            .build();

                    drive.followTrajectorySequence(dropLeftSpike);

                    servoAuton.setPosition(0.83);
                    sleep(2000);


                    drive.setPoseEstimate(startPose);
                    TrajectorySequence leftPixelBoard = drive.trajectorySequenceBuilder(startPose)
                            .addTemporalMarker(2, () ->{servoAuton.setPosition(0);})
                            .forward(-7)
                            .turn(Math.toRadians(180))
                            .waitSeconds(0.5)
                            .splineTo(leftBoard, Math.toRadians(270))
                            .build();

                    drive.followTrajectorySequence(leftPixelBoard);

                    rangeError      = (desiredTag.ftcPose.range - DESIRED_DISTANCE);
                    headingError    = desiredTag.ftcPose.bearing;
                    yawError        = desiredTag.ftcPose.yaw;

                    // Use the speed and turn "gains" to calculate how we want the robot to move.
                    forward  = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
                    turn   = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN) ;
                    strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);


                    moveRobot(forward, strafe, turn);
                    sleep(10);

                    servoBoard.setPosition(0.6);
                    sleep(2000);

                    drive.setPoseEstimate(startPose);
                    TrajectorySequence adjustMiddle = drive.trajectorySequenceBuilder(startPose)
                            .forward(-4)
                            .strafeRight(35)
                            .build();

                    drive.followTrajectorySequence(adjustMiddle);

                    break;

                }

                else if (position == "middle"){
                    //Spline to middle spike mark, drop purple pixel, put yellow pixel on board, park

                    TrajectorySequence dropMiddleSpike = drive.trajectorySequenceBuilder(startPose)
                            .splineTo(middleSpike, Math.toRadians(90))
                            .forward(-7)
                            .build();

                    drive.followTrajectorySequence(dropMiddleSpike);

                    servoAuton.setPosition(0.83);
                    sleep(3000);

                    drive.setPoseEstimate(startPose);
                    TrajectorySequence middlePixelBoard = drive.trajectorySequenceBuilder(startPose)
                            .addTemporalMarker(2, () ->{servoAuton.setPosition(0);})
                            .forward(-7)
                            .turn(Math.toRadians(-90))
                            .splineTo(middleBoard, Math.toRadians(0))
                            .build();

                    drive.followTrajectorySequence(middlePixelBoard);

                    servoBoard.setPosition(0.6);
                    sleep(2000);

                    drive.setPoseEstimate(startPose);
                    TrajectorySequence adjustMiddle = drive.trajectorySequenceBuilder(startPose)
                            .forward(-4)
                            .strafeRight(35)
                            .build();

                    drive.followTrajectorySequence(adjustMiddle);

                    break;

                }

                else if (position == "right"){
                    //Spline to middle spike mark, drop purple pixel, put yellow pixel on board, park

                    TrajectorySequence dropRightSpike = drive.trajectorySequenceBuilder(startPose)
                            .splineTo(rightSpike, Math.toRadians(0))
                            .waitSeconds(0.5)
                            .forward(5)
                            .forward(-9.5)
                            .build();

                    drive.followTrajectorySequence(dropRightSpike);

                    servoAuton.setPosition(0.83);
                    sleep(2000);
                    //servoAuton.setPosition(0);

                    drive.setPoseEstimate(startPose);
                    TrajectorySequence rightPixelBoard = drive.trajectorySequenceBuilder(startPose)
                            .addTemporalMarker(2, () ->{servoAuton.setPosition(0);})
                            .forward(-2.5)
                            .strafeRight(17)
                            .forward(5)
                            .splineTo(rightBoard, Math.toRadians(90))
                            .build();

                    drive.followTrajectorySequence(rightPixelBoard);

                    servoBoard.setPosition(0.6);
                    sleep(2000);

                    drive.setPoseEstimate(startPose);
                    TrajectorySequence adjustRight = drive.trajectorySequenceBuilder(startPose)
                            .forward(-4)
                            .strafeRight(35)
                            .build();

                    drive.followTrajectorySequence(adjustRight);


                    break;

                }

                else {
                    //Spline to middle spike mark, drop purple pixel, put yellow pixel on board, park

                    TrajectorySequence dropMiddleSpike = drive.trajectorySequenceBuilder(startPose)
                            .splineTo(middleSpike, Math.toRadians(90))
                            .forward(-7)
                            .build();

                    drive.followTrajectorySequence(dropMiddleSpike);

                    servoAuton.setPosition(0.83);
                    sleep(3000);

                    drive.setPoseEstimate(startPose);
                    TrajectorySequence middlePixelBoard = drive.trajectorySequenceBuilder(startPose)
                            .addTemporalMarker(2, () ->{servoAuton.setPosition(0);})
                            .forward(-7)
                            .turn(Math.toRadians(-90))
                            .splineTo(middleBoard, Math.toRadians(0))
                            .build();

                    drive.followTrajectorySequence(middlePixelBoard);

                    servoBoard.setPosition(0.6);
                    sleep(2000);

                    drive.setPoseEstimate(startPose);
                    TrajectorySequence adjustMiddle = drive.trajectorySequenceBuilder(startPose)
                            .forward(-4)
                            .strafeRight(24)
                            .build();

                    drive.followTrajectorySequence(adjustMiddle);


                    break;
                }


            }


        }

    }  // end runOpMode()


    private void initTfod() {

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()

                // With the following lines commented out, the default TfodProcessor Builder
                // will load the default model for the season. To define a custom model to load,
                // choose one of the following:
                //   Use setModelAssetName() if the custom TF Model is built in as an asset (AS only).
                //   Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
                .setModelAssetName(TFOD_MODEL_ASSET)
                //.setModelFileName(TFOD_MODEL_FILE)

                // The following default settings are available to un-comment and edit as needed to
                // set parameters for custom models.
                .setModelLabels(LABELS)
                //.setIsModelTensorFlow2(true)
                //.setIsModelQuantized(true)
                //.setModelInputSize(300)
                //.setModelAspectRatio(16.0 / 9.0)

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "webcam"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));


        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(tfod);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        //tfod.setMinResultConfidence(0.75f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);

    }   // end method initTfod()

    //telemetry for all Tfod
    private void telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();

        if (getRecognition() > 1){
            if ((currentRecognitions.get(0).getWidth()*currentRecognitions.get(0).getHeight()) > (currentRecognitions.get(1).getWidth() * currentRecognitions.get(1).getHeight())){
                currentRecognitions.remove(0);
            }
            else {
                currentRecognitions.remove(1);
            }

        }

        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }   // end for() loop


    }   // end method telemetryTfod()

    public double getPosition() {
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        if (getRecognition() > 1){
            if ((currentRecognitions.get(0).getWidth()*currentRecognitions.get(0).getHeight()) > (currentRecognitions.get(1).getWidth() * currentRecognitions.get(1).getHeight())){
                currentRecognitions.remove(0);
            }
            else {
                currentRecognitions.remove(1);
            }

        }

        for (Recognition recognition : currentRecognitions) {
            x = (recognition.getLeft() + recognition.getRight()) / 2;
            y = (recognition.getTop() + recognition.getBottom()) / 2; }
        return (x);

    }

    public double getRecognition(){
        List<Recognition> currentRecognitions = tfod.getRecognitions();

        return (currentRecognitions.size());
    }

    private void initAprilTag() {
        // Create the AprilTag processor by using a builder.
        aprilTag = new AprilTagProcessor.Builder().build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(2);

        // Create the vision portal by using a builder.
        if (USE_WEBCAM) {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(aprilTag)
                    .build();
        } else {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(BuiltinCameraDirection.BACK)
                    .addProcessor(aprilTag)
                    .build();
        }
    }

    public void findAprilTag(){
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            // Look to see if we have size info on this tag.
            if (detection.metadata != null) {
                //  Check to see if we want to track towards this tag.
                if ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID)) {
                    // Yes, we want to use this tag.
                    targetFound = true;
                    desiredTag = detection;
                    break;  // don't look any further.
                } else {
                    // This tag is in the library, but we do not want to track it right now.
                    telemetry.addData("Skipping", "Tag ID %d is not desired", detection.id);
                }
            } else {
                // This tag is NOT in the library, so we don't have enough information to track to it.
                telemetry.addData("Unknown", "Tag ID %d is not in TagLibrary", detection.id);
            }
        }


    }

    public void moveRobot(double x, double y, double yaw) {

        fl  = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl  = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");

        fl.setDirection(DcMotor.Direction.REVERSE);
        rl.setDirection(DcMotor.Direction.REVERSE);

        // Calculate wheel powers.
        double leftFrontPower    =  x -y -yaw;
        double rightFrontPower   =  x +y +yaw;
        double leftBackPower     =  x +y -yaw;
        double rightBackPower    =  x -y +yaw;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        // Send powers to the wheels.
        fl.setPower(leftFrontPower);
        fr.setPower(rightFrontPower);
        rl.setPower(leftBackPower);
        rr.setPower(rightBackPower);
    }
}   // end class

