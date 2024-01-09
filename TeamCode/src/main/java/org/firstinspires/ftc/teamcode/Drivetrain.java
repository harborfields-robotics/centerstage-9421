package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import java.util.Arrays;
import java.util.List;
import java.lang.Math;

public class Drivetrain
{
	public DcMotor motorFL, motorFR, motorBL, motorBR;
	public DcMotor encoderLeft, encoderBack, encoderRight;
	public List<DcMotor> motors;
	public List<DcMotor> encoders;
	public Hardware hardware;

	public static final double
		TICKS_PER_TILE = 620,
		TICKS_PER_DEGREE = 7.45,
		TICKS_PER_SIDE_DIFFERENCE = 30,
		STRAFE_CONSTANT = 1.3;

	public Drivetrain(Hardware hardware)
	{
		this.hardware = hardware;

		this.motorFL = hardware.get(DcMotor.class, Hardware.FL_MOTOR_NAME);
		this.motorBL = hardware.get(DcMotor.class, Hardware.BL_MOTOR_NAME);
		this.motorBR = hardware.get(DcMotor.class, Hardware.BR_MOTOR_NAME);
		this.motorFR = hardware.get(DcMotor.class, Hardware.FR_MOTOR_NAME);
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

		motorFL.setDirection(DcMotor.Direction.REVERSE);
		motorFR.setDirection(DcMotor.Direction.FORWARD);
		motorBL.setDirection(DcMotor.Direction.REVERSE);
		motorBR.setDirection(DcMotor.Direction.FORWARD);

		// for (DcMotor e: encoders)
		// 	e.setMode(STOP_AND_RESET_ENCODER);
	}

	public void setMotorTargets(int fl, int fr, int bl, int br)
	{
		motorFL.setTargetPosition(fl);
		motorFR.setTargetPosition(fr);
		motorBL.setTargetPosition(bl);
		motorBR.setTargetPosition(br);
	}

	/* XXX: this method changes the order of the input array? */
	public double[] normalizePowers(double[] powers) {
		Arrays.sort(powers);
		if (powers[3] > 1) {
			powers[0] /= powers[3];
			powers[1] /= powers[3];
			powers[2] /= powers[3];
			powers[3] /= powers[3];
		}
		return powers;
	}

	public void setPowerSmooth(DcMotor motor, double target, double delta)
	{
		motor.setPower(motor.getPower() + delta * Math.signum(target - motor.getPower()));
	}

	public double[] computeMotorPowers(double deltaY, double deltaX, double deltaTheta)
	{
		return new double[]{
			Range.clip(deltaY + deltaX + deltaTheta, -1, 1),
			Range.clip(deltaY - deltaX + deltaTheta, -1, 1),
			Range.clip(deltaY + deltaX - deltaTheta, -1, 1),
			Range.clip(deltaY - deltaX - deltaTheta, -1, 1)
		};
	}

	public void driveLoopSmooth(double deltaY, double deltaX, double deltaTheta, double acceleration)
	{
		double powers[] = computeMotorPowers(deltaY, deltaX, deltaTheta);
		for (int i = 0; i < powers.length; i++)
			setPowerSmooth(motors.get(i), powers[i], acceleration);
	}

	public void driveLoop(double deltaY, double deltaX, double deltaTheta)
	{
		double powers[] = computeMotorPowers(deltaY, deltaX, deltaTheta);
		for (int i = 0; i < powers.length; i++)
			motors.get(i).setPower(powers[i]);
	}
}
