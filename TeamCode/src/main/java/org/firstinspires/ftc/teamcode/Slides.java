package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slides
{
    public DcMotor motor;

    public Slides(Hardware hardware)
    {
        this.motor = hardware.hardwareMap.get(DcMotor.class, Hardware.SLIDES_NAME);
        this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
