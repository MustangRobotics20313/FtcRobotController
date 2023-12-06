package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class AutoTestWoodrow extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;

  @Override
  public void runOpMode() {
      fl = hardwareMap.get(DcMotor.class, "fl");
      fr = hardwareMap.get(DcMotor.class, "fr");
      rl = hardwareMap.get(DcMotor.class, "rl");
      rr = hardwareMap.get(DcMotor.class, "rr");

      fr.setDirection(DcMotor.Direction.REVERSE);
      rr.setDirection(DcMotor.Direction.REVERSE);

      waitForStart();


          fl.setPower(0.5);
          fr.setPower(0.5);
          fr.setPower(0.5);
          rr.setPower(0.5);
          sleep(2000);
          telemetry.addData("Auto", "moving forward 5 seconds");
          fl.setPower(0);
          fr.setPower(0);
          fr.setPower(0);
          rr.setPower(0);

  }
}
