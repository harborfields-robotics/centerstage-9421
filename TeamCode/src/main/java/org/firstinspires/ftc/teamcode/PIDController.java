package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.Serializable;

public class PIDController implements Serializable
{
    private DcMotor motor;
    private ElapsedTime timer = new ElapsedTime();
    private PIDConstants constants;
    private double errorSum, errorSlope;
    private double prevTime, prevError;
    private double target;

    public PIDController(DcMotor motor, PIDConstants constants)
    {
        this.motor = motor;
        this.constants = constants;
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double getError()
    {
        return target - motor.getCurrentPosition();
    }

    public double getTimeSeconds()
    {
        return timer.seconds();
    }
    public boolean isAtTarget() { return getError() == 0; }

    public void loop()
    {
        if (prevTime == 0) {
            prevTime = getTimeSeconds();
            prevError = getError();
            return;
        }

        double error = getError(), time = getTimeSeconds();
        errorSum += (error + prevError) * (time - prevTime) / 2;
        errorSlope = (error - prevError) / (time - prevTime);
        double power = constants.Kp * error + constants.Ki * errorSum + constants.Kd * errorSlope;
        motor.setPower(power);
    }
}
