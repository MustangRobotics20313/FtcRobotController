package org.firstinspires.ftc.teamcode;

public class FeedforwardController {
    private double kS;
    private double kV;
    private double kA;

    public FeedforwardController(double ks, double kv, double ka) {
        this.kS = ks;
        this.kV = kv / 39.3701;  // converts units of kV from [V*s/m] to [V*s/in]
        this.kA = ka / 39.3701;  // converts units of kA from [V*s^2/m] to [V*s^2/in]
    }

    public double calculate(double velocity, double acceleration) {
        return kS * Math.signum(velocity) + kV * velocity + kA * acceleration;
    }
}
