package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;

/**
  * The Launcher class represents the paper airplane launcher.
  */
public class Launcher
{
	public Servo servo;
	private Hardware hardware;

	/**
	  * Whether the airplane has been launched already.
	  */
	private boolean launched = false;

	/**
	  * The position of the servo needed to release the airplane.
	  */
	public static final double RELEASE_POSITION = 1;

	/**
	  * The position of the servo before the airplane has been launched.
	  */
	public static final double PRIMED_POSITION = 0;

	public Launcher(Hardware hardware)
	{
		this.hardware = hardware;
		this.servo = hardware.get(Servo.class, Hardware.LAUNCHER_MOTOR_NAME);
		this.servo.setPosition(PRIMED_POSITION);
	}

	/**
	  * Launches the paper airplane by releasing the slingshot.
	  * The launch will fail if the airplane has already been launched.
	  * @return if the launch could be completed
	  */
	public boolean launch()
	{
		if (launched)
			return false;
		servo.setPosition(RELEASE_POSITION);
		launched = true;
		return true;
	}
}
