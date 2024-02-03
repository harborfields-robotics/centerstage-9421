package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Reset Encoders", group="Testing OpMode")
public class ResetEncoders extends LinearOpMode
{
    @Override
    public void runOpMode()
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        hardwareMap.getAll(DcMotor.class).forEach(Hardware::resetEncoder);
        telemetry.addLine("All encoders reset to zero.");
        telemetry.update();
    }
}