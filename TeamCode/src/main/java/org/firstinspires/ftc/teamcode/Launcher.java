package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;

/**
  * The Launcher class represents the paper airplane launcher.
  */
public class Launcher
{
	public DcMotor motor;
	private Hardware hardware;

	/**
	  * The motor power to be used for launching.
	  */
	public static final double LAUNCH_POWER = 1.0;

	/**
	  * The amount of time to run the motor, in milliseconds.
	  */
	public static final long LAUNCH_MS = 3 * 1000;

	public Launcher(Hardware hardware)
	{
		this.hardware = hardware;
		this.motor = hardware.get(DcMotor.class, Hardware.LAUNCHER_MOTOR_NAME);
	}

	/**
	  * Launches the paper airplane.
	  */
	public void launch()
	{
		motor.setPower(LAUNCH_POWER);
		try {
			Thread.sleep(LAUNCH_MS);
		} catch (InterruptedException e) {}
		motor.setPower(0);
	}
}
