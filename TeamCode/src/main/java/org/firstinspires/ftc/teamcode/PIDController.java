package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.Serializable;

public class PIDController implements Serializable
{
    private DcMotor motor;
    private double prevTime, prevError;
    private double errorSum, errorSlope;
    private String motorName;
    private ElapsedTime timer = new ElapsedTime();
    private double target;
    private PIDConstants constants;

    public PIDController(String motorName, Hardware hardware)
    {
        this.motorName = motorName;
    }

    public void setup(Hardware hardware)
    {
        motor = hardware.hardwareMap.get(DcMotor.class, motorName);
        // motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double getError()
    {
        return target - motor.getCurrentPosition();
    }

    public double getTimeSeconds()
    {
        return timer.seconds();
    }

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
