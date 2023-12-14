package org.firstinspires.ftc.teamcode;

public class PIDConstants
{
    public double Kp, Ki, Kd;
    public double ticksPerRotation, radius;
    public PIDConstants(double Kp, double Kd, double Ki)
    {
        this.Kp = Kp;
        this.Kd = Kd;
        this.Ki = Ki;
        // this.ticksPerRotation = ticksPerRotation;
        // this.radius = radius;
    }

    public PIDConstants() {}
}
