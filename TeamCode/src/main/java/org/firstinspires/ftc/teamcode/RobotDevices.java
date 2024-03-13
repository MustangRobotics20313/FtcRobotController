package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class RobotDevices {

    public DcMotorEx fl;
    public DcMotorEx fr;
    public DcMotorEx rl;
    public DcMotorEx rr;

    public DcMotorEx left_slide;
    public DcMotorEx right_slide;

    public RobotDevices(HardwareMap hm) {
        fl = hm.get(DcMotorEx.class, "fl");
        fr = hm.get(DcMotorEx.class, "fr");
        rl = hm.get(DcMotorEx.class, "rl");
        rr = hm.get(DcMotorEx.class, "rr");

        left_slide = hm.get(DcMotorEx.class, "left_slide");
        right_slide = hm.get(DcMotorEx.class, "right_slide");
    }

    public void disableEncoders() {
        fl.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        fr.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rl.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rr.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void reverseMotors() {
        fl.setDirection(DcMotorEx.Direction.REVERSE);
        rl.setDirection(DcMotorEx.Direction.REVERSE);
    }

    public void setDriveMotorBraking() {
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

}
