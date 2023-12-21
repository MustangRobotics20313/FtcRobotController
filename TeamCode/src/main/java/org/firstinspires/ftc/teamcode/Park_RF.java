package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Autonomous

public class Park_RF extends LinearOpMode {

    private DcMotor rr;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor fl;
    private DcMotor c1;
    private DcMotor c2;


    @Override
    public void runOpMode() {
        rr = hardwareMap.get(DcMotor.class, "rr");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        fl = hardwareMap.get(DcMotor.class, "fl");
        c1 = hardwareMap.get(DcMotor.class, "c1");
        c2 = hardwareMap.get(DcMotor.class, "c2");

        fr.setDirection(DcMotor.Direction.REVERSE);
        rl.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        //forward to spike mark

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

        rr.setPower(0.5);
        fr.setPower(-0.5);
        rl.setPower(-0.5);
        fl.setPower(0.5);

        sleep(4250);

        rr.setPower(0);
        fr.setPower(0);
        rl.setPower(0);
        fl.setPower(0);

        sleep(500);

        //strafe towards backstage

        rr.setPower(0.5);
        fr.setPower(-0.5);
        rl.setPower(-0.5);
        fl.setPower(0.5);

        sleep(500);

        rr.setPower(0);
        fr.setPower(0);
        rl.setPower(0);
        fl.setPower(0);

        sleep(500);

        //release pixels
        sleep(200);

        c1.setPower(0.5);
        c2.setPower(-0.5);

        sleep(500);

        c1.setPower(0);
        c2.setPower(0);


    }
}