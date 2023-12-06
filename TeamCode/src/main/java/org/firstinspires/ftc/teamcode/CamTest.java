package org.firstinspires.ftc.teamcode;

//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

//import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import org.checkerframework.common.util.report.qual.ReportOverride;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
public class CamTest extends OpMode {

    OpenCvWebcam webcam;

    @Override
    public void init() {

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id",hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcam.setPipeline(new examplePipeline());

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            public void onOpened(){
                webcam.startStreaming(640,360, OpenCvCameraRotation.UPRIGHT);

            }

            public void onError(int errorCode){

            }
        });

    }

    @Override
    public void loop() {

    }

    }



    class examplePipeline extends OpenCvPipeline {

        Mat YCbCr = new Mat();
        Mat leftCrop;
        Mat rightCrop;
        Mat middleCrop;
        double leftavgfin;
        double rightavgfin;
        double midavgfin;
        Mat outPut = new Mat();
        Scalar rectColor = new Scalar(255.0, 0.0, 0.0);

        public Mat processFrame(Mat input){

            Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);
            //telemetry.addLine("pipeline running");

            /* Rect leftRect = new Rect(1,1,319,359);
            Rect middleRect = new Rect(320,1,319,359);
            Rect rightRect = new Rect(320,1,319,359); */

            Rect leftRect = new Rect(1,1,210,359);
            Rect middleRect = new Rect(210,1,210,359);
            Rect rightRect = new Rect(420,1,210,359);


            input.copyTo(outPut);
            Imgproc.rectangle(outPut,leftRect, rectColor, 2);
            Imgproc.rectangle(outPut,rightRect, rectColor, 2);
            Imgproc.rectangle(outPut, middleRect, rectColor, 2);


            leftCrop = YCbCr.submat(leftRect);
            rightCrop = YCbCr.submat(rightRect);
            middleCrop = YCbCr.submat(middleRect);

            Core.extractChannel(leftCrop, leftCrop, 2);
            Core.extractChannel(rightCrop, rightCrop, 2);
            Core.extractChannel(middleCrop, middleCrop, 2);

            Scalar leftavg = Core.mean(leftCrop);
            Scalar rightavg = Core.mean(rightCrop);
            Scalar midavg = Core.mean(middleCrop);

            leftavgfin = leftavg.val[0];
            rightavgfin = rightavg.val[0];
            midavgfin = midavg.val[0];

            if (leftavgfin > rightavgfin && leftavgfin > midavgfin){
                //telemetry.addLine("Left");

            }
            else if (rightavgfin > leftavgfin && rightavgfin > midavgfin){
                //telemetry.addLine("Right");
            }

            else if (midavgfin > leftavgfin && midavgfin > rightavgfin){
                //telemetry.addLine("Middle");
            }

            return(outPut);
        }


    }

