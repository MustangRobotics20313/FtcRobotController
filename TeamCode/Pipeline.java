package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

class Pipeline extends OpenCvPipeline {

    Mat YCbCr = new Mat();
    Mat leftCrop;
    Mat rightCrop;
    Mat middleCrop;
    double leftavgfin;
    double rightavgfin;
    double midavgfin;
    String position;
    Mat outPut = new Mat();
    Scalar rectColor = new Scalar(255.0, 0.0, 0.0);

    public double laf() { return leftavgfin; }

    public double raf() { return rightavgfin; }

    public double maf() { return midavgfin; }

    public String getPosition(){ return position; }


    public Mat processFrame(Mat input){

        Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);


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

        leftavgfin = leftavg.val[2];
        rightavgfin = rightavg.val[2];
        midavgfin = midavg.val[2];

        if (leftavgfin > rightavgfin && leftavgfin > midavgfin){
            position = "left";

        }
        else if (rightavgfin > leftavgfin && rightavgfin > midavgfin){
            position = "right";
        }

        else if (midavgfin > leftavgfin && midavgfin > rightavgfin){
            position = "middle";

        }

        return(outPut);
    }



}


