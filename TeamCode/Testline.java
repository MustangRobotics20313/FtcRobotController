package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

class Testline extends OpenCvPipeline {

    Mat YCbCr = new Mat();
    Mat leftCrop;
    Mat rightCrop;
    Mat middleCrop;
    double leftavgfin;
    double rightavgfin;
    double midavgfin;
    String position = "null";
    Mat outPut = new Mat();
    Scalar rectColor = new Scalar(225.0, 0.0, 0.0);

    public double laf() { return leftavgfin; }

    public double raf() { return rightavgfin; }

    public double maf() { return midavgfin; }

    public String getPosition(){ return position; }


    public Mat processFrame(Mat input){
        input.copyTo(outPut);
        Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);


        Mat matLeft = outPut.submat(120,150,10,50);
        Mat matCenter = outPut.submat(120,150,80,120);
        Mat matRight = outPut.submat(120,150,150,190);

        Rect leftRect = new Rect(1,1,210,359);
        Rect middleRect = new Rect(210,1,210,359);
        Rect rightRect = new Rect(420,1,210,359);


        Imgproc.rectangle(outPut,leftRect, rectColor, 2);
        Imgproc.rectangle(outPut,rightRect, rectColor, 2);
        Imgproc.rectangle(outPut, middleRect, rectColor, 2);


        double leftTotal = Core.sumElems(matLeft).val[2];
        double rightTotal = Core.sumElems(matRight).val[2];
        double centerTotal = Core.sumElems(matCenter).val[2];



        if (leftTotal > rightTotal && leftTotal > centerTotal){
            position = "left";

        }
        else if (rightTotal > leftTotal && rightTotal > centerTotal){
            position = "right";
        }

        else if (centerTotal > leftTotal && centerTotal > rightTotal){
            position = "middle";

        }

        return(outPut);
    }



}


