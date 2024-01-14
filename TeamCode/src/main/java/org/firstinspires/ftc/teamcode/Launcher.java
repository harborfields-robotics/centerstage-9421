package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;

/**
  * The Launcher class represents the paper airplane launcher.
  */
public class Launcher {
	public Servo servo;
	private Hardware hardware;

	public static final double PRIMED_POSITION = 0;
	public static final double FIRING_POSITION = 1;
	private boolean hasLaunched = false;

	/**
	 * The time we began launching the airplaine, as returned by {@link System#nanoTime()}.
	 */
	private long startTime;

	public Launcher(Hardware hardware) {
		this.hardware = hardware;
		this.servo = this.hardware.get(Servo.class, Hardware.LAUNCHER_SERVO_NAME);
	}

	/**
	 * Launches the paper airplane by releasing the rubber band.
	 * The launch will fail if the airplane was already launched
	 *
	 * @return whether the launch was successful
	 */
	public boolean launch()
	{
		servo.setPosition(1);
	}
}