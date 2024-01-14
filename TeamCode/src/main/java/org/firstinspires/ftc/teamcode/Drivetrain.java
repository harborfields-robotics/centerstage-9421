package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import java.util.Arrays;
import java.util.List;
import java.lang.Math;

/**
  * The Drivetrain class handles the movement of a holonomic drivetrain, that is, one using four mecanum wheels.
  * This inclueds odometry using dead wheels.
  */
public class Drivetrain
{
	public DcMotorEx motorFL, motorFR, motorBL, motorBR;
	public DcMotor encoderLeft, encoderBack, encoderRight;
	public List<DcMotorEx> motors;
	public List<DcMotorEx> encoders;
	public Hardware hardware;

	public static final double
		TICKS_PER_TILE = 620,
		TICKS_PER_DEGREE = 7.45,
		TICKS_PER_SIDE_DIFFERENCE = 30,
		STRAFE_CONSTANT = 1.3;

	public Drivetrain(Hardware hardware)
	{
		this.hardware = hardware;

		this.motorFL = hardware.get(DcMotorEx.class, Hardware.FL_MOTOR_NAME);
		this.motorBL = hardware.get(DcMotorEx.class, Hardware.BL_MOTOR_NAME);
		this.motorBR = hardware.get(DcMotorEx.class, Hardware.BR_MOTOR_NAME);
		this.motorFR = hardware.get(DcMotorEx.class, Hardware.FR_MOTOR_NAME);
		this.motors = Arrays.asList(motorFL, motorBL, motorBR, motorFR);

		// this.encoderLeft = hardware.get(DcMotor.class, Hardware.LEFT_ENCODER_NAME);
		// this.encoderBack = hardware.get(DcMotor.class, Hardware.BACK_ENCODER_NAME);
		// this.encoderRight = hardware.get(DcMotor.class, Hardware.RIGHT_ENCODER_NAME);
		// this.encoders = Arrays.asList(encoderLeft, encoderBack, encoderRight);

		for (DcMotor m: motors) {
			m.setPower(0);
			m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			m.setMode(STOP_AND_RESET_ENCODER);
			m.setMode(RUN_WITHOUT_ENCODER);
		}

		motorFL.setDirection(DcMotor.Direction.FORWARD);
		motorFR.setDirection(DcMotor.Direction.REVERSE);
		motorBL.setDirection(DcMotor.Direction.FORWARD);
		motorBR.setDirection(DcMotor.Direction.REVERSE);

		// for (DcMotor e: encoders)
		// 	e.setMode(STOP_AND_RESET_ENCODER);
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
	   @param deltaTheta the amount to rotate clockwise, a value in the range [-1, 1]
	  * @param power a value each motor's power is multiplied by
	  */
	public void driveLoop(double deltaY, double deltaX, double deltaTheta, double power)
	{
		double powers[] = computeMotorPowers(deltaY, deltaX, deltaTheta);
		for (int i = 0; i < powers.length; i++)
			motors.get(i).setPower(power * powers[i]);
	}
}
