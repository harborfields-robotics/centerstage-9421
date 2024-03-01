package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.Arrays;
import java.util.List;

/**
  * The Drivetrain class handles the movement of a holonomic drivetrain, that is, one using four mecanum wheels.
  * This inclueds odometry using dead wheels.
  */
public class Drivetrain
{
	public DcMotorEx motorFL, motorFR, motorBL, motorBR;
	public DcMotorEx encoderLeft, encoderBack, encoderRight;
	public List<DcMotorEx> motors;
	public static final String[] MOTOR_NAMES = { "FL", "BL", "BR", "FR" };
	public static final String[] ENCODER_NAMES = { "left", "back", "right" };
	public List<DcMotorEx> encoders;
	public Hardware hardware;

	public Drivetrain(Hardware hardware)
	{
		this.hardware = hardware;

		this.motorFL = hardware.get(DcMotorEx.class, Hardware.FL_MOTOR_NAME);
		this.motorBL = hardware.get(DcMotorEx.class, Hardware.BL_MOTOR_NAME);
		this.motorBR = hardware.get(DcMotorEx.class, Hardware.BR_MOTOR_NAME);
		this.motorFR = hardware.get(DcMotorEx.class, Hardware.FR_MOTOR_NAME);
		this.motors = Arrays.asList(motorFL, motorBL, motorBR, motorFR);

		this.encoderLeft = hardware.get(DcMotorEx.class, Hardware.LEFT_ENCODER_NAME);
		this.encoderBack = hardware.get(DcMotorEx.class, Hardware.BACK_ENCODER_NAME);
		this.encoderRight = hardware.get(DcMotorEx.class, Hardware.RIGHT_ENCODER_NAME);
		this.encoders = Arrays.asList(encoderLeft, encoderBack, encoderRight);

		for (DcMotor m: motors) {
			m.setPower(0);
			m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			m.setMode(STOP_AND_RESET_ENCODER);
			m.setMode(RUN_WITHOUT_ENCODER);
		}

		motorBL.setDirection(REVERSE);
	}

	/**
	  * Returns a new array of motor powers, normalized to fall within the range [-1, 1].
	  * Does this by dividing all powers by the element of largest absolute power.
	  * This method does not modify the original array.
	  * @param powers the unnormalized powers
	  * @return those powers normalized to preserve ratio
	  */
	public static double[] normalizePowers(double[] powers)
	{
		double[] normalized = new double[powers.length];
		double max = 0;
		for (double p: powers)
			if (Math.abs(p) > max)
				max = Math.abs(p);
		for (int i = 0; i < normalized.length; i++)
			normalized[i] = powers[i] / max;
		return normalized;
	}

	/**
	  * Sets the power of a motor smoothly over multiple calls to this method.
	  * @param motor the motor whose power will be set
	  * @param target the final motor power
	  * @param delta the amount to accelerate by per call to this method
	  */
	public void setPowerSmooth(DcMotor motor, double target, double delta)
	{
		double power = motor.getPower();
		if (Math.abs(power - target) < delta)
			motor.setPower(target);
		else
			motor.setPower(power + delta * Math.signum(target - power));
	}

	/**
	 * Returns the motor powers necessary to move in some direction.
	 * @param deltaY the amount to move forward, a value in the range [-1, 1]
	 * @param deltaX the amount to strafe right, a value in the range [-1, 1]
	 * @param deltaTheta the amount to rotate clockwise, a value in the range [-1, 1]
	 * @return the motor powers in the order {FL, BL, BR, FR}
	 */
	public static double[] computeMotorPowers(double deltaY, double deltaX, double deltaTheta)
	{
		return normalizePowers(
				new double[]{
					deltaY - deltaX - deltaTheta,
					deltaY + deltaX - deltaTheta,
					deltaY - deltaX + deltaTheta,
					deltaY + deltaX + deltaTheta,
				});
	}

	/**
	  * Smoothly sets the motor powers in order to drive in the specified direction.
	  * @param deltaY the amount to move forward, a value in the range [-1, 1]
	  * @param deltaX the amount to strafe right, a value in the range [-1, 1]
	  * @param deltaTheta the amount to rotate clockwise, a value in the range [-1, 1]
	  * @param power a value each motor's power is multiplied by
	  * @param acceleration the amount to accelerate by per call to this method
	  * @see #setPowerSmooth(DcMotor, double, double)
	  */
	public void driveLoopSmooth(double deltaY, double deltaX, double deltaTheta, double power, double acceleration)
	{
		double powers[] = computeMotorPowers(deltaY, deltaX, deltaTheta);
		for (int i = 0; i < powers.length; i++)
			setPowerSmooth(motors.get(i), power * powers[i], acceleration);
	}

	/**
	  * Sets the motor powers in order to drive in the specified direction with a specific maximum power.
	  * @param deltaY the amount to move forward, a value in the range [-1, 1]
	  * @param deltaX the amount to strafe right, a value in the range [-1, 1]
	  * @param deltaTheta the amount to rotate clockwise, a value in the range [-1, 1]
	  * @param power a value each motor's power is multiplied by
	  */
	public void driveLoop(double deltaY, double deltaX, double deltaTheta, double power)
	{
		double[] powers = computeMotorPowers(deltaY, deltaX, deltaTheta);
		for (int i = 0; i < powers.length; i++)
			motors.get(i).setPower(power * powers[i]);
	}

	public void stop()
	{
		for (DcMotor m: motors)
			m.setPower(0);
	}

	/*
	public int[] calculateHeadingTargets(Heading heading)
	{
		int[] targets = new int[4];
		double dy = heading.x / (DriveConstants.WHEEL_RADIUS * Math.PI * 2) * DriveConstants.TICKS_PER_REV;
		for (int i = 0; i < targets.length; i++)
			targets[i] += dy;
		return targets;
	}

	public void followHeading(Heading heading)
	{
		int[] targets = calculateHeadingTargets(heading);
		DcMotor.RunMode[] modes = motors.stream()
				.map(DcMotor::getMode)
				.toArray(DcMotor.RunMode[]::new);
		for (int i = 0; i < targets.length; i++)
			motors.get(i).setTargetPosition(targets[i]);
		for (int i = 0; i < motors.size(); i++)
			motors.get(i).setMode(modes[i]);
	} */

	public void telemetrize()
	{
		for (DcMotor m: motors)
			hardware.telemetry.addData(hardware.getDeviceName(m),
					"%d ticks, %.2f pwr", m.getCurrentPosition(), m.getPower());
	}
}