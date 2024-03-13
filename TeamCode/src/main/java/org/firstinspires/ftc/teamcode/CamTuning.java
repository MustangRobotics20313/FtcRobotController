package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.checkerframework.checker.units.qual.Length;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import org.tensorflow.lite.task.vision.detector.Detection;

import java.util.List;


@Autonomous(name = "CamTuning", group = "Concept")

public class CamTuning extends LinearOpMode {

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

    @Override
    public void runOpMode() {

        /* servoAuton = hardwareMap.get(Servo.class, "servoAuton");
        servoAuton.scaleRange(0,1); */

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
                telemetryTfod();
                visionPortal.close();

                telemetry.addData("Spike mark:", position);
                telemetry.update();

                }


            }
        }


        // end runOpMode()



    //Initialize the TensorFlow Object Detection processor.

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

}   // end class