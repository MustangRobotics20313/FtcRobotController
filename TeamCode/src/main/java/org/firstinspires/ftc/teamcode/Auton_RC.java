package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;


@Autonomous(name = "Auton_RC", group = "Concept")

public class Auton_RC extends LinearOpMode {

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

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

    private VisionPortal visionPortal; //stores instance of the vision portal
    double x;
    double y;
    String position;


    private Servo servoAuton;
    private Servo servoRotate;
    private Servo servoPixel;
    private DcMotor slide;


    @Override
    public void runOpMode() {

        slide = hardwareMap.get(DcMotor.class, "slide");
        servoAuton = hardwareMap.get(Servo.class, "servoAuton");
        servoRotate = hardwareMap.get(Servo.class, "servoRotate");
        servoPixel = hardwareMap.get(Servo.class, "servoPixel");
        servoAuton.scaleRange(0,1);
        servoRotate.scaleRange(0,1);
        servoPixel.scaleRange(0,1);

        slide.setDirection(DcMotor.Direction.REVERSE);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        initTfod();

        while (!opModeIsActive()) {
            if (getPosition() > 0 && getPosition() < 100) {
                position = "left";
            } else if (getPosition() > 100 && getPosition() < 500) {
                position = "middle";
            } else if (getPosition() > 500 && getPosition() < 600) {
                position = "right";
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

            servoPixel.setPosition(0.1);
            servoRotate.setPosition(0.865);

            SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
            Pose2d startPose = new Pose2d(0, 0, (Math.toRadians(90)));

            Vector2d leftSpike = new Vector2d(-4, 22);
            Vector2d middleSpike = new Vector2d(-1.5, 37);
            Vector2d rightSpike = new Vector2d(8.25, 35);

            Vector2d leftBoard = new Vector2d(11 , -45);
            Vector2d middleBoard = new Vector2d(42.5, -2.5);
            Vector2d rightBoard = new Vector2d(12, 40);


            drive.setPoseEstimate(startPose);

            if (position == "left"){
                //Spline to left spike mark, drop purple pixel

                TrajectorySequence dropLeftSpike = drive.trajectorySequenceBuilder(startPose)
                        .forward(4)
                        .splineTo(leftSpike, Math.toRadians(180))
                        .waitSeconds(0.5)
                        .build();

                drive.followTrajectorySequence(dropLeftSpike);

                servoAuton.setPosition(0.85);
                sleep(1500);


                drive.setPoseEstimate(startPose);
                TrajectorySequence leftPixelBoard = drive.trajectorySequenceBuilder(startPose)
                        .addTemporalMarker(2, () ->
                        {servoAuton.setPosition(0);
                            lift();})
                        .addTemporalMarker(1000,() ->
                        {servoRotate.setPosition(0.745);
                            sleep(1000); })
                        .forward(-7)
                        .turn(Math.toRadians(180))
                        .waitSeconds(0.5)
                        .splineTo(leftBoard, Math.toRadians(270))
                        .build();

                drive.followTrajectorySequence(leftPixelBoard);


                servoPixel.setPosition(0.4);
                sleep(2000);

                drive.setPoseEstimate(startPose);
                TrajectorySequence adjustLeft = drive.trajectorySequenceBuilder(startPose)
                         .addTemporalMarker(0.5,() ->
                            {servoRotate.setPosition(0.85);
                                sleep(2000); })
                            .addTemporalMarker(2,() ->
                            { resetLift();
                                sleep(1000); })
                        .forward(-4)
                        .strafeRight(35)
                        .build();

                drive.followTrajectorySequence(adjustLeft);

                break;

            }

            else if (position == "middle"){
                //Spline to middle spike mark, drop purple pixel, put yellow pixel on board, park

                TrajectorySequence dropMiddleSpike = drive.trajectorySequenceBuilder(startPose)
                        .splineTo(middleSpike, Math.toRadians(90))
                        .forward(-7)
                        .build();

                drive.followTrajectorySequence(dropMiddleSpike);

                servoAuton.setPosition(0.85);
                sleep(1500);

                drive.setPoseEstimate(startPose);
                TrajectorySequence middlePixelBoard = drive.trajectorySequenceBuilder(startPose)
                        .addTemporalMarker(2, () ->
                        {servoAuton.setPosition(0);
                            lift();})
                        .addTemporalMarker(1000,() ->
                        {servoRotate.setPosition(0.745);
                            sleep(1000); })
                        .forward(-4)
                        .turn(Math.toRadians(-90))
                        .splineTo(middleBoard, Math.toRadians(0))
                        .build();

                drive.followTrajectorySequence(middlePixelBoard);

                servoPixel.setPosition(0.4);
                sleep(1000);

                drive.setPoseEstimate(startPose);
                TrajectorySequence adjustMiddle = drive.trajectorySequenceBuilder(startPose)

                        .addTemporalMarker(0.5,() ->
                            {servoRotate.setPosition(0.85);
                                sleep(2000); })
                            .addTemporalMarker(2,() ->
                            { resetLift();
                                sleep(1000); })
                        .forward(-4)
                        .strafeRight(24)
                        .forward(10)
                        .build();

                drive.followTrajectorySequence(adjustMiddle);

                break;

            }

            else if (position == "right"){
                //Spline to middle spike mark, drop purple pixel, put yellow pixel on board, park

                TrajectorySequence dropRightSpike = drive.trajectorySequenceBuilder(startPose)
                        .splineTo(rightSpike, Math.toRadians(0))
                        .forward(-7)
                        .build();

                drive.followTrajectorySequence(dropRightSpike);

                servoAuton.setPosition(0.85);
                sleep(1500);
                //servoAuton.setPosition(0);

                drive.setPoseEstimate(startPose);
                TrajectorySequence rightPixelBoard = drive.trajectorySequenceBuilder(startPose)
                       .addTemporalMarker(2, () ->
                            {servoAuton.setPosition(0);
                                lift();})
                            .addTemporalMarker(1000,() ->
                            {servoRotate.setPosition(0.745);
                                sleep(1000); })
                        .forward(-2.5)
                        .strafeRight(17)
                        .forward(5)
                        .splineTo(rightBoard, Math.toRadians(90))
                        .build();

                drive.followTrajectorySequence(rightPixelBoard);

                servoPixel.setPosition(0.4);
                sleep(2000);

                drive.setPoseEstimate(startPose);
                TrajectorySequence adjustRight = drive.trajectorySequenceBuilder(startPose)
                         .addTemporalMarker(0.5,() ->
                            {servoRotate.setPosition(0.85);
                                sleep(2000); })
                            .addTemporalMarker(2,() ->
                            { resetLift();
                                sleep(1000); })
                        .forward(-4)
                        .strafeRight(25)
                        .forward(10)
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

                /* drive.setPoseEstimate(startPose);
                TrajectorySequence middlePixelBoard = drive.trajectorySequenceBuilder(startPose)
                        .addTemporalMarker(2, () ->{servoAuton.setPosition(0);})
                        .forward(-7)
                        .turn(Math.toRadians(-90))
                        .splineTo(middleBoard, Math.toRadians(0))
                        .build();

                drive.followTrajectorySequence(middlePixelBoard);

                lift();
                servoRotate.setPosition(0.5);
                sleep(2000);

                servoPixel.setPosition(0.4);
                sleep(2000);

                drive.setPoseEstimate(startPose);
                TrajectorySequence adjustMiddle = drive.trajectorySequenceBuilder(startPose)
                        .forward(-4)
                        .strafeRight(24)
                        .build();

                drive.followTrajectorySequence(adjustMiddle); */


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

    public void lift(){

        slide.setTargetPosition(900);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setPower(0.5);


    }

    public void resetLift(){

        slide.setTargetPosition(0);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setPower(-0.5);


    }

}

  // end class