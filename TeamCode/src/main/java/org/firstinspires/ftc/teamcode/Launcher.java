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

	/**
	  * Whether the airplane is currently being launched.
	  */
	private boolean launching = false;

	/** 
	  * The time we began launching the airplaine, as returned by {@link System#nanoTime()}.
	  */
	private long startTime;

	public Launcher(Hardware hardware)
	{
		this.hardware = hardware;
		this.motor = hardware.get(DcMotor.class, Hardware.LAUNCHER_MOTOR_NAME);
	}

	/**
	  * Runs a single step of the processing loop.
	  * Handles behavior and checks that should occur once per the main loop of the opmode.
	  * @see Hardware#loop()
	  */
	public void loop()
	{
		if (launching) {
			if (System.nanoTime() - startTime > LAUNCH_MS * 1e6) {
				motor.setPower(0);
				launching = false;
			}
		}
	}

	/**
	  * Launches the paper airplane without blocking the current thread.
	  * This method requires {@link #loop()} to be called repeatedly in order to complete.
	  * @see #loop()
	  * @see #launchBlocking()
	  */
	public void launch()
	{
		motor.setPower(LAUNCH_POWER);
		launching = true;
	}

	/**
	  * Launches the paper airplane, blocking the thread until finished.
	  * @see #launch()
	  */ 
	public void launchBlocking() throws InterruptedException
	{
		launch();
		Thread.sleep(LAUNCH_MS);
		motor.setPower(0);
		launching = false;
	}
}
