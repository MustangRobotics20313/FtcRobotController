package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

/**
 *
 */
public class RobotDevices {

    // drive motors
    public DcMotorEx m1;
    public DcMotorEx m2;
    public DcMotorEx m3;
    public DcMotorEx m4;

    // extra motors
    public DcMotorEx m5;
    public DcMotorEx m6;
    public DcMotorEx m7;
    public DcMotorEx m8;

    // servos

    public Servo s1;
    public Servo s2;
    public Servo s3;
    public Servo s4;
    public Servo s5;
    public Servo s6;


    // camera
    public OpenCvCamera camera;

    // FTCDashboard
    public FtcDashboard dashboard;

    // hardware map
    private final HardwareMap hardwareMap;

    /**
     * Constructor for RobotDevices device manager/initializer object.
     * Initializes hardware map and dashboard objects by default.
     * @param hm    Hardware map object passed in from the OpMode
     *              This object is passed in through the constructor for initializing all devices
     * @param type  Denotes the types of devices to be initialized
     * @see   BrakeState
     */
    public RobotDevices(HardwareMap hm, DeviceType type) {
        this.hardwareMap = hm;
        this.dashboard = FtcDashboard.getInstance();

        switch(type) {
            case ALL:
                initializeMotors();
                initializeServos();
                initializeCamera();
                break;
            case DRIVE:
                initializeMotors();
                initializeServos();
                break;
            case SERVOS:
                initializeServos();
                break;
            case MOTORS:
                initializeMotors();
                break;
            case DRIVE_MOTORS:
            default:
                initializeDriveMotors();
                break;

        }
    }

    /**
     * Disables encoders on drive motors.
     */
    public void disableEncoders() {
        m1.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        m2.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        m3.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        m4.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Reverses left side drive motors.
     */
    public void reverseDriveMotors  () {
        m1.setDirection(DcMotorEx.Direction.REVERSE);
        m3.setDirection(DcMotorEx.Direction.REVERSE);
    }

    /**
     * Sets drive motor zero power behavior to brake.
     */
    public void setDriveMotorBraking() {
        m1.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        m2.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        m3.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        m4.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Sets drive motor zero power behavior to float.
     */
    public void setDriveMotorFloat() {
        m1.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        m2.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        m3.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        m4.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
    }

    /**
     * Calls helper methods to initialize both drive motors and extra motors.
     */
    private void initializeMotors() {
        initializeDriveMotors();
        initializeScoringMotors();
    }

    /**
     * Initializes all 4 drive motors.
     */
    private void initializeDriveMotors() {
        m1 = hardwareMap.get(DcMotorEx.class, "fl");
        m2 = hardwareMap.get(DcMotorEx.class, "fr");
        m3 = hardwareMap.get(DcMotorEx.class, "rl");
        m4 = hardwareMap.get(DcMotorEx.class, "rr");
    }

    /**
     * Initializes scoring motors.
     */
    private void initializeScoringMotors() {
        m5 = hardwareMap.get(DcMotorEx.class, "left_slide");
        m6 = hardwareMap.get(DcMotorEx.class, "right_slide");
        m7 = hardwareMap.get(DcMotorEx.class, "intake");
    }

    /**
     * Initializes all servos.
     */
    private void initializeServos() {
        // servo = hardwareMap.get(Servo.class, "servo_name");
    }

    /**
     * Initializes camera
     */
    private void initializeCamera() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier
                ("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);
    }

    /**
     * Presets of which devices to initialize.
     */
    public enum DeviceType {
        ALL,
        DRIVE,
        MOTORS,
        DRIVE_MOTORS,
        SERVOS
    }

    /**
     * Presets of motor zero power behavior for opmode state machines.
     */
    public enum BrakeState {
        BRAKE,
        FLOAT
    }
}
