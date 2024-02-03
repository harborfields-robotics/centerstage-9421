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

	public Launcher(Hardware hardware) {
		this.hardware = hardware;
		this.servo = this.hardware.get(Servo.class, Hardware.LAUNCHER_SERVO_NAME);
	}

	/**
	 * Launches the paper airplane by releasing the rubber band.
	 */
	public void launch() {
		servo.setPosition(1);
	}

	/**
	 * Returns the launcher to the state before launching.
	 */
	public void unlaunch()
	{
		servo.setPosition(0.40);
	}
}
