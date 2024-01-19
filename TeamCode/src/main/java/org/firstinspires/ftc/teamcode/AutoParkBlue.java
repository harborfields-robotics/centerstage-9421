package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name="Park as Blue", group="OpMode")
public class AutoParkBlue extends OpMode
{
	Hardware hardware;

	public void init()
	{
		hardware = new Hardware(hardwareMap, telemetry);
	}

	public void loop()
	{
		hardware.drivetrain.driveLoop(0, -1, 0, 0.5);
		Util.sleep(150);
		hardware.drivetrain.stop();
	}
}