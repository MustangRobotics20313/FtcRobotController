package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import org.tensorflow.lite.task.vision.detector.Detection;

import java.util.List;


@Autonomous(name = "Tensor_BF", group = "Concept")

public class Tensor_BF extends LinearOpMode {

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    // TFOD_MODEL_ASSET points to a model file stored in the project Asset location,
    // this is only used for Android Studio when using models in Assets.
    private static final String TFOD_MODEL_ASSET = "blueE_redE_pixel.tflite";
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


    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor lift;
    private DcMotor rotate;
    private Servo servoAuton;
    private Servo servoDrone;
    private DcMotor c1;

    @Override
    public void runOpMode() {

        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        lift = hardwareMap.get(DcMotor.class, "lift");
        rotate = hardwareMap.get(DcMotor.class, "rotate");
        servoAuton = hardwareMap.get(Servo.class, "servo");
        c1 = hardwareMap.get(DcMotor.class, "c1");

        fl.setDirection(DcMotor.Direction.REVERSE);
        rl.setDirection(DcMotor.Direction.REVERSE);

        servoAuton.scaleRange(0,1);

        initTfod();

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                //move forward

                fl.setPower(0.5);
                fr.setPower(0.5);
                rr.setPower(0.5);
                rl.setPower(0.5);

                sleep(200);

                fl.setPower(0);
                fr.setPower(0);
                rr.setPower(0);
                rl.setPower(0);

                if (getRecognition() > 0){
                    position = "center";

                    //180 degree turn

                    fl.setPower(0.5);
                    fr.setPower(-0.5);
                    rr.setPower(-0.5);
                    rl.setPower(0.5);

                    sleep(400);

                    //drop pixel
                    servoAuton.setPosition(0);
                }

                else {

                    //turn left
                    fl.setPower(-0.5);
                    fr.setPower(0.5);
                    rr.setPower(0.5);
                    rl.setPower(-0.5);

                    sleep(150);

                    fl.setPower(0);
                    fr.setPower(0);
                    rr.setPower(0);
                    rl.setPower(0);

                    if (getRecognition() > 0){
                        position = "left";

                        //180 degree turn

                        fl.setPower(0.5);
                        fr.setPower(-0.5);
                        rr.setPower(-0.5);
                        rl.setPower(0.5);

                        sleep(400);

                        //drop pixel

                        servoAuton.setPosition(0);

                        //turn to re-center robot
                        fl.setPower(0.5);
                        fr.setPower(-0.5);
                        rr.setPower(-0.5);
                        rl.setPower(0.5);

                        sleep(200);

                        fl.setPower(0);
                        fr.setPower(0);
                        rr.setPower(0);
                        rl.setPower(0);


                    }

                    else {

                        position = "right";

                        //turn right
                        fl.setPower(0.5);
                        fr.setPower(-0.5);
                        rr.setPower(-0.5);
                        rl.setPower(0.5);

                        sleep(400);

                        fl.setPower(0);
                        fr.setPower(0);
                        rr.setPower(0);
                        rl.setPower(0);

                        sleep(200);

                        //180 degree turn

                        fl.setPower(0.5);
                        fr.setPower(-0.5);
                        rr.setPower(-0.5);
                        rl.setPower(0.5);

                        //drop pixel

                        servoAuton.setPosition(0);

                        //turn to re-center robot
                        fl.setPower(0.5);
                        fr.setPower(-0.5);
                        rr.setPower(-0.5);
                        rl.setPower(0.5);

                        sleep(200);

                        fl.setPower(0);
                        fr.setPower(0);
                        rr.setPower(0);
                        rl.setPower(0);

                    }
                }

                //move towards starting position

                fl.setPower(-0.5);
                fr.setPower(-0.5);
                rr.setPower(-0.5);
                rl.setPower(-0.5);

                sleep(150);

                fl.setPower(0);
                fr.setPower(0);
                rr.setPower(0);
                rl.setPower(0);


                //strafe towards backstage

                rr.setPower(-0.5);
                fr.setPower(0.5);
                rl.setPower(-0.5);
                fl.setPower(0.5);

                sleep(4250);

                rr.setPower(0);
                fr.setPower(0);
                rl.setPower(0);
                fl.setPower(0);


                telemetryTfod();

                // Push telemetry to the Driver Station.

                telemetry.update();


                // Share the CPU.
                sleep(20);
            }
        }

        // Save more CPU resources when camera is no longer needed.
        visionPortal.close();

    }   // end runOpMode()



















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

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

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

    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    private void telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
            telemetry.addData("Spike mark: ", position);
        }   // end for() loop


    }   // end method telemetryTfod()

    public double getPosition() {
        return x; }

    public double getRecognition(){
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        return (currentRecognitions.size());
    }


}   // end class