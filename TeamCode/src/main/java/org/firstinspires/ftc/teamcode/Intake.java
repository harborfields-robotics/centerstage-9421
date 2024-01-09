package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

public class Intake
{
	private DcMotor teethMotor;
	private Servo tongueServo;

	public static final double OUTTAKE_ONE_DURATION_NS = 0.75 * 1e9;

	public static enum State { INTAKING, OUTTAKING, STOPPED };

	private State state = State.STOPPED;

	public Intake(Hardware hardware)
	{
		this.teethMotor = hardware.get(DcMotor.class, Hardware.TEETH_MOTOR_NAME);
		this.teethMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		this.teethMotor.setDirection(DcMotorSimple.Direction.REVERSE);
		this.tongueServo = hardware.get(Servo.class, Hardware.TONGUE_SERVO_NAME);
		this.tongueServo.setPosition(0.5);
	}

	public void outtake()
	{
		state = State.OUTTAKING;
		tongueServo.setPosition(1);
		teethMotor.setPower(1);
	}

	public void intake()
	{
		state = State.INTAKING;
		tongueServo.setPosition(0);
		teethMotor.setPower(-1);
	}

	public void stop()
	{
		state = State.OUTTAKING;
		tongueServo.setPosition(0.5);
		teethMotor.setPower(0);
	}

	public State getState()
	{
		return state;
	}

	public void outtakeOne()
	{
		long start = System.nanoTime();
		outtake();
		try {
			while ((System.nanoTime() - start) < OUTTAKE_ONE_DURATION_NS)
				Thread.sleep(50);
		} catch (InterruptedException e) {}
		stop();
	}

	public void outtakeAll()
	{
		long start = System.nanoTime();
		outtake();
		try {
			while ((System.nanoTime() - start) < OUTTAKE_ONE_DURATION_NS * 2.5)
				Thread.sleep(50);
		} catch (InterruptedException e) {}
		stop();
	}
}
