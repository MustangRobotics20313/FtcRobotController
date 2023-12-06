package org.firstinspires.ftc.teamcode;

//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

//import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.FtcDashboard;

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
public class SpikeMarkAuton2 extends LinearOpMode {

    private DcMotor rr;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor fl;
    private DcMotor c1;
    private DcMotor c2;
    OpenCvWebcam webcam;
    public String position;


    @Override
    public void runOpMode(){

        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        rr = hardwareMap.get(DcMotor.class, "rr");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        fl = hardwareMap.get(DcMotor.class, "fl");
        //c1 = hardwareMap.get(DcMotor.class, "c1");
        //c2 = hardwareMap.get(DcMotor.class, "c2");

        fr.setDirection(DcMotor.Direction.REVERSE);
        rr.setDirection(DcMotor.Direction.REVERSE);

        Testline pipeline = new Testline();

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id",hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        webcam.setPipeline(pipeline);

        position = "null";



        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            public void onOpened(){
                webcam.startStreaming(640,360, OpenCvCameraRotation.UPRIGHT);


            }

            public void onError(int errorCode){

            }
        });

        waitForStart();

        position = pipeline.getPosition();
        if (position == "middle"){
            telemetry.addData("Position: ", "test");
            telemetry.update();



            /* //move forward
            rr.setPower(0.5);
            fr.setPower(0.5);
            rl.setPower(0.5);
            fl.setPower(0.5);

            sleep(800);

            rr.setPower(0);
            fr.setPower(0);
            rl.setPower(0);
            fl.setPower(0); */
        }

        else if (position == "left") {
            telemetry.addData("Position: ", "test");
            telemetry.update();




            /* //turn left
            rr.setPower(0.5);
            fr.setPower(0.5);
            rl.setPower(-0.5);
            fl.setPower(-0.5);

            sleep (250);


            //move forward
            rr.setPower(0.5);
            fr.setPower(0.5);
            rl.setPower(0.5);
            fl.setPower(0.5);

            sleep(800);

            rr.setPower(0);
            fr.setPower(0);
            rl.setPower(0);
            fl.setPower(0); */

        }

        else  if (position == "right"){

            telemetry.addData("Position: ", "test");
            telemetry.update();




            /* //turn right
            rr.setPower(-0.5);
            fr.setPower(-0.5);
            rl.setPower(0.5);
            fl.setPower(0.5);

            sleep (250);
            //move forward
            rr.setPower(0.5);
            fr.setPower(0.5);
            rl.setPower(0.5);
            fl.setPower(0.5);

            sleep(800);

            rr.setPower(0);
            fr.setPower(0);
            rl.setPower(0);
            fl.setPower(0); */

        }

        else{
            telemetry.addData("No position", position);
            telemetry.addData("Left Average Value", pipeline.laf());
            telemetry.addData("Middle Average Value", pipeline.maf());
            telemetry.addData("Right Average Value", pipeline.raf());
            telemetry.update();
        }


        /* //forward to spike mark

        rr.setPower(0.5);
        fr.setPower(0.5);
        rl.setPower(0.5);
        fl.setPower(0.5);

        sleep(800);

        rr.setPower(0);
        fr.setPower(0);
        rl.setPower(0);
        fl.setPower(0);

        sleep(500);

        //move backwards towards wall

        rr.setPower(-0.5);
        fr.setPower(-0.5);
        rl.setPower(-0.5);
        fl.setPower(-0.5);

        sleep(750);

        rr.setPower(0);
        fr.setPower(0);
        rl.setPower(0);
        fl.setPower(0);

        sleep(500);

        //strafe towards backstage

        rr.setPower(-0.5);
        fr.setPower(0.5);
        rl.setPower(0.5);
        fl.setPower(-0.5);

        sleep(2500);

        rr.setPower(0);
        fr.setPower(0);
        rl.setPower(0);
        fl.setPower(0);

        //release pixels
        sleep(200);

        c1.setPower(0.5);
        c2.setPower(-0.5);

        sleep(500);

        c1.setPower(0);
        c2.setPower(0); */



    }

}

