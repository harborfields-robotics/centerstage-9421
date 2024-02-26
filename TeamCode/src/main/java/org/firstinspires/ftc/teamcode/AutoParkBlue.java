package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.acmerobotics.dashboard.config.Config;

@Config
@Autonomous(name="Park as Blue", group="OpMode")
public class AutoParkBlue extends OpMode
{
	Hardware hardware;
	public static double duration = 2.500;

	public void init()
	{
		hardware = new Hardware(hardwareMap, telemetry);
	}

	public void start()
	{
		// Util.sleep(10 * 1000);
		long start = System.nanoTime();
		while ((System.nanoTime() - start) < duration * 1e9) {
			hardware.drivetrain.driveLoop(0, -1, 0, 1.0);
			Util.sleep(50);
		}

		hardware.drivetrain.stop();
		stop();
	}

	public void loop() {}
}
