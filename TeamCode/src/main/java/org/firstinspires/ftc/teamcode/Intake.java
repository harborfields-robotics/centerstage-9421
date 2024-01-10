package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

/**
  * The class representing the intake/outtake functionality of the robot.
  * This encompasses both the teeth and the tongue.
  */
public class Intake
{
	private DcMotor teethMotor;
	private Servo tongueServo;

	/**
	  * The time it takes to outtake one pixel, in nanoseconds.
	  */
	public static final double OUTTAKE_ONE_DURATION_NS = 0.75 * 1e9;

	/**
	  * Represents all possible states of the intake.
	  */
	public static enum State
	{
		INTAKING_CONTINUOUS,
		OUTTAKING_CONTINUOUS,
		OUTTAKING_ONE,
		OUTTAKING_ALL,
		STOPPED
	}

	private State state = State.STOPPED;

	/** 
	  * The time we began an operation, as returned by {@link System#nanoTime()}.
	  */
	private long startTime;

	public Intake(Hardware hardware)
	{
		this.teethMotor = hardware.get(DcMotor.class, Hardware.TEETH_MOTOR_NAME);
		this.teethMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		this.teethMotor.setDirection(DcMotorSimple.Direction.REVERSE);
		this.tongueServo = hardware.get(Servo.class, Hardware.TONGUE_SERVO_NAME);
		this.tongueServo.setPosition(0.5);
	}

	/**
	  * Runs a single step of the processing loop.
	  * Handles behavior and checks that should occur once per the main loop of the opmode.
	  * @see Hardware#loop()
	  */
	public void loop()
	{
		switch (state) {
			case OUTTAKING_ONE:
				if (System.nanoTime() - startTime > OUTTAKE_ONE_DURATION_NS)
					stop();
				break;
			case OUTTAKING_ALL:
				if (System.nanoTime() - startTime > OUTTAKE_ONE_DURATION_NS * 2.5)
					stop();
				break;
		}
	}

	/**
	  * Continuously outtakes pixels.
	  */
	public void outtake()
	{
		state = State.OUTTAKING_CONTINUOUS;
		tongueServo.setPosition(1);
		teethMotor.setPower(1);
	}

	/**
	  * Continuously intakes pixels.
	  */
	public void intake()
	{
		state = State.INTAKING_CONTINUOUS;
		tongueServo.setPosition(0);
		teethMotor.setPower(-1);
	}

	/**
	  * Stops any operation and return to the rest state.
	  */
	public void stop()
	{
		state = State.STOPPED;
		tongueServo.setPosition(0.5);
		teethMotor.setPower(0);
	}

	public State getState()
	{
		return state;
	}

	/**
	  * Outtakes one pixel from the sandwitch without blocking the thread.
	  * @see #loop()
	  */
	public void outtakeOne()
	{
		long startTime = System.nanoTime();
		outtake();
		state = State.OUTTAKING_ONE;
	}

	/**
	  * Outtakes both pixels in the sandwitch without blocking the thread.
	  * @see #loop()
	  */
	public void outtakeAll()
	{
		long startTime = System.nanoTime();
		outtake();
		state = State.OUTTAKING_ALL;
	}
}
