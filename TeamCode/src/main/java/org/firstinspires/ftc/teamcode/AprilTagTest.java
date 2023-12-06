/* package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@TeleOp
public class AprilTagTest extends LinearOpMode {
private static final String TFOD_MODEL_ASSIST = "NN";
private static final String[] LABELS = {};

    @Override
    public void runOpMode() throws InterruptedException {

        //customize processor by adding methods after line 15 and before .build();
        AprilTagProcessor tagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true) //draws on the take where the axes are pointing
                .setDrawCubeProjection(true) //draws cube off of tag to show where camera things tag is pointing
                .setDrawTagID(true) //draws tag ID (e.g. ID 02) on the tag
                .setDrawTagOutline(true) //outlines square shape
                .setLensIntrinsics() //tells processor how camera interprets 3D world
                .build();
        waitForStart();

        VisionPortal visionPortal = new VisionPortal.Builder()
                .addProcessor(tagProcessor) //allows camera to feed image to processor we just made
                .setCamera(hardwareMap.get(WebcamName.class, "camera"))
                .setCameraResolution(new Size(640, 480)); //mid range size so that it does not sacrifice speed and has decent vision from distance

                //.build();

        while (!isStopRequested() && opModeIsActive()) {

            if (tagProcessor.getDetections().size() > 0) { //ensures processor sees at least one tag
                AprilTagDetection tag = tagProcessor.getDetections().get(0); //tag is equal to first detected tag unless none visible

                //from the view of the camera:
                // x is left right
                // y is straight out
                // z is up and down
                //range is total dist to tag using x/y/pythagorean theorem
                //bearing is how much camera needs to ROTATE left or right for tag to be center screen
                //elevation is how much camera needs to ROTATE up or down for tag to be centered
                tag.ftcPose.x;
        }
    }
}


*/
// there are two main classes used to detect AprilTags
// AprilTagProcessor process images and finds images/relevant data
// VisionPortal feeds images into processor or optionally to a webcam