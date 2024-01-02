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

	public boolean atEncoderTarget()
	{
		return motors.stream()
			.map(Hardware::motorAtTarget)
			.reduce(false, Boolean::logicalOr);
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

	// TODO: remove all; use RoadRunner methods instead
	public void strafeLeft(double tileCount, double power)
	{
		motors.forEach(m -> m.setMode(STOP_AND_RESET_ENCODER));
		int ticks = (int)(tileCount * TICKS_PER_TILE * STRAFE_CONSTANT);
		motorFL.setTargetPosition(-ticks);
		motorBL.setTargetPosition(ticks);
		motorBR.setTargetPosition(-ticks);
		motorFR.setTargetPosition(ticks);

		for (DcMotor m: motors) {
			m.setMode(RUN_TO_POSITION);
			m.setPower(power);
		}

		while (!atEncoderTarget()) {
			hardware.telemetry.addData("Drivetrain#strafeLeft", "Robot is strafing %s for %s tiles...",
					tileCount < 0 ? "right" : "left", tileCount);
			motors.forEach(m ->
					hardware.telemetry.addData("Drivetrain#strafeLeft",
						"%s: s --> %s", m.getCurrentPosition(), m.getTargetPosition()));
		}
		for (DcMotor m: motors) {
			m.setMode(RUN_WITHOUT_ENCODER);
			m.setPower(0);
		}
	}

	public void strafeRight(double tileCount, double power)
	{
		strafeLeft(-tileCount, power);
	}

	public void driveForward(double num_tiles, double power)
	{
		motors.forEach(m -> m.setMode(STOP_AND_RESET_ENCODER));
		setMotorTargets((int) (num_tiles * TICKS_PER_TILE),
				(int) (num_tiles * TICKS_PER_TILE),
				(int) (num_tiles * TICKS_PER_TILE),
				(int) (num_tiles * TICKS_PER_TILE));
		motors.forEach(m -> m.setMode(RUN_TO_POSITION));
		motors.forEach(m -> m.setPower(0));
		while (motorFL.getCurrentPosition() < motorFL.getTargetPosition() && motorBL.getCurrentPosition() < motorBL.getTargetPosition() && motorFR.getCurrentPosition() < motorFR.getTargetPosition() && motorBR.getCurrentPosition() < motorBR.getTargetPosition()) {
			hardware.telemetry.addData("motorFL target: ", motorFL.getTargetPosition());
			hardware.telemetry.addData("motorBL target: ", motorBL.getTargetPosition());
			hardware.telemetry.addData("motorFR target: ", motorFR.getTargetPosition());
			hardware.telemetry.addData("motorBR target: ", motorBR.getTargetPosition());
			hardware.telemetry.addData("motorFL position: ", motorFL.getCurrentPosition());
			hardware.telemetry.addData("motorBL position: ", motorBL.getCurrentPosition());
			hardware.telemetry.addData("motorFR position: ", motorFR.getCurrentPosition());
			hardware.telemetry.addData("motorBR position: ", motorBR.getCurrentPosition());
			hardware.telemetry.update();
		}
		motors.forEach(m -> m.setPower(0));
		motors.forEach(m -> m.setMode(RUN_WITHOUT_ENCODER));
	}

	public void driveBackward(double num_tiles, double power)
	{
		motors.forEach(m -> m.setMode(STOP_AND_RESET_ENCODER));
		setMotorTargets((int) (-num_tiles * TICKS_PER_TILE),
				(int) (-num_tiles * TICKS_PER_TILE),
				(int) (-num_tiles * TICKS_PER_TILE),
				(int) (-num_tiles * TICKS_PER_TILE));
		motors.forEach(m -> m.setMode(RUN_TO_POSITION));
		while (motorFL.getCurrentPosition() > motorFL.getTargetPosition() && motorBL.getCurrentPosition() > motorBL.getTargetPosition() && motorFR.getCurrentPosition() > motorFR.getTargetPosition() && motorBR.getCurrentPosition() > motorBR.getTargetPosition()) {
			motors.forEach(m -> m.setPower(power));
		}
		motors.forEach(m -> m.setPower(0));
		motors.forEach(m -> m.setMode(RUN_WITHOUT_ENCODER));
	}

	public void smoothDriveForward(double num_tiles, double power)
	{
		motors.forEach(m -> m.setMode(STOP_AND_RESET_ENCODER));
		setMotorTargets((int) (num_tiles * TICKS_PER_TILE),
				(int) (num_tiles * TICKS_PER_TILE),
				(int) (num_tiles * TICKS_PER_TILE),
				(int) (num_tiles * TICKS_PER_TILE));
		motors.forEach(m -> m.setMode(RUN_TO_POSITION));

		while (motorFL.getPower() < power && motorFL.getCurrentPosition() < motorFL.getTargetPosition()) {
			motors.forEach(m -> m.setPower(m.getPower() + (power / 10)));
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {}
		}
		motors.forEach(m -> m.setPower(power));

		while (motorFL.getCurrentPosition() < motorFL.getTargetPosition() - (TICKS_PER_TILE * 0.12)) {
			hardware.telemetry.addData("motorFL target: ", motorFL.getTargetPosition());
			hardware.telemetry.addData("motorBL target: ", motorBL.getTargetPosition());
			hardware.telemetry.addData("motorFR target: ", motorFR.getTargetPosition());
			hardware.telemetry.addData("motorBR target: ", motorBR.getTargetPosition());
			hardware.telemetry.addData("motorFL position: ", motorFL.getCurrentPosition());
			hardware.telemetry.addData("motorBL position: ", motorBL.getCurrentPosition());
			hardware.telemetry.addData("motorFR position: ", motorFR.getCurrentPosition());
			hardware.telemetry.addData("motorBR position: ", motorBR.getCurrentPosition());
			hardware.telemetry.update();
		}
		motors.forEach(m -> m.setPower(power));
		while (motorFL.getPower() > 0 && motorFL.getCurrentPosition() < motorFL.getTargetPosition()) {
			motors.forEach(m -> m.setPower(m.getPower() - (power / 10)));
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {}
		}

		motors.forEach(m -> m.setPower(0));
		motors.forEach(m -> m.setMode(RUN_WITHOUT_ENCODER));
		motors.forEach(m -> m.setPower(0));
	}

	// DOES NOT WORK PROPERLY
	public void drive(double angle, double num_tiles, double power) throws InterruptedException {
		// motorFL and motorBR are y+x
		// motorFR and motorBL are y-x
		double x = Math.cos(Math.toRadians(angle));
		double y = Math.sin(Math.toRadians(angle));
		motors.forEach(m -> m.setMode(RUN_USING_ENCODER));
		setMotorTargets(motorFL.getTargetPosition() + (int)(num_tiles * y+x),
				motorFR.getTargetPosition() + (int)(num_tiles * y-x),
				motorBL.getTargetPosition() + (int)(num_tiles * y-x),
				motorBR.getTargetPosition() + (int)(num_tiles * y+x));
		motors.forEach(m -> m.setMode(RUN_TO_POSITION));
		motorFL.setPower(Math.abs(y + x));
		motorBL.setPower(Math.abs(y - x));
		motorBR.setPower(Math.abs(y + x));
		motorFR.setPower(Math.abs(y - x));
		while (motorFL.getCurrentPosition() != motorFL.getTargetPosition() && motorBL.getCurrentPosition() != motorBL.getTargetPosition() && motorFR.getCurrentPosition() != motorFR.getTargetPosition() && motorBR.getCurrentPosition() != motorBR.getTargetPosition()) {
			Thread.sleep(10);
		}
		motors.forEach(m -> m.setPower(0));
		motors.forEach(m -> m.setMode(RUN_WITHOUT_ENCODER));
	}

	public void fixEncoderTicksDifferential(double power) {
		motors.forEach(m -> m.setMode(RUN_USING_ENCODER));
		setMotorTargets(
				(int)(motorFL.getTargetPosition() + TICKS_PER_SIDE_DIFFERENCE),
				(int)(motorFR.getTargetPosition()),
				(int)(motorBL.getTargetPosition() + TICKS_PER_SIDE_DIFFERENCE),
				(int)(motorBR.getTargetPosition()));
		motors.forEach(m -> m.setMode(RUN_TO_POSITION));
		while (motorFL.getCurrentPosition() != motorFL.getTargetPosition() && motorBL.getCurrentPosition() != motorBL.getTargetPosition() && motorFR.getCurrentPosition() != motorFR.getTargetPosition() && motorBR.getCurrentPosition() != motorBR.getTargetPosition()) {
			motors.forEach(m -> m.setPower(power));
		}
		motors.forEach(m -> m.setPower(0));

	}

	public void turnLeft(double degrees, double power) {
		motors.forEach(m -> m.setMode(STOP_AND_RESET_ENCODER));
		motors.forEach(m -> m.setMode(RUN_USING_ENCODER));
		setMotorTargets(motorFL.getCurrentPosition() - (int)(degrees * TICKS_PER_DEGREE),
				motorFR.getCurrentPosition() + (int)(degrees * TICKS_PER_DEGREE),
				motorBL.getCurrentPosition() - (int)(degrees * TICKS_PER_DEGREE),
				motorBR.getCurrentPosition() + (int)(degrees * TICKS_PER_DEGREE)
				);
		motors.forEach(m -> m.setMode(RUN_TO_POSITION));
		while (motorFL.getCurrentPosition() > motorFL.getTargetPosition() &&
				motorBL.getCurrentPosition() > motorBL.getTargetPosition() &&
				motorFR.getCurrentPosition() < motorFR.getTargetPosition() &&
				motorBR.getCurrentPosition() < motorBR.getTargetPosition()) {
			motors.forEach(m -> m.setPower(power));
			hardware.telemetry.addData(">> turnLeft() is running...    ", 11115);
			hardware.telemetry.addData("FL target: ", motorFL.getTargetPosition());
			hardware.telemetry.addData("BL target: ", motorBL.getTargetPosition());
			hardware.telemetry.addData("FR target: ", motorFR.getTargetPosition());
			hardware.telemetry.addData("BR target: ", motorBR.getTargetPosition());
			hardware.telemetry.addData("FL position: ", motorFL.getCurrentPosition());
			hardware.telemetry.addData("BL position: ", motorBL.getCurrentPosition());
			hardware.telemetry.addData("FR position: ", motorFR.getCurrentPosition());
			hardware.telemetry.addData("BR position: ", motorBR.getCurrentPosition());
			hardware.telemetry.update();
		}
		motors.forEach(m -> m.setPower(0));
		motors.forEach(m -> m.setMode(RUN_WITHOUT_ENCODER));
	}

	public void turnRight(double degrees, double power) {
		motors.forEach(m -> m.setMode(STOP_AND_RESET_ENCODER));
		motors.forEach(m -> m.setMode(RUN_USING_ENCODER));
		setMotorTargets(motorFL.getCurrentPosition() + (int)(degrees * TICKS_PER_DEGREE),
			motorFR.getCurrentPosition() - (int)(degrees * TICKS_PER_DEGREE),
			motorBL.getCurrentPosition() + (int)(degrees * TICKS_PER_DEGREE),
			motorBR.getCurrentPosition() - (int)(degrees * TICKS_PER_DEGREE)
		);
		motors.forEach(m -> m.setMode(RUN_TO_POSITION));
		while (motorFL.getCurrentPosition() < motorFL.getTargetPosition() &&
				motorBL.getCurrentPosition() < motorBL.getTargetPosition() &&
				motorFR.getCurrentPosition() > motorFR.getTargetPosition() &&
				motorBR.getCurrentPosition() > motorBR.getTargetPosition()) {
			motors.forEach(m -> m.setPower(power));
			hardware.telemetry.addData(">> turnLeft() is running...    ", 11115);
			hardware.telemetry.addData("FL target: ", motorFL.getTargetPosition());
			hardware.telemetry.addData("BL target: ", motorBL.getTargetPosition());
			hardware.telemetry.addData("FR target: ", motorFR.getTargetPosition());
			hardware.telemetry.addData("BR target: ", motorBR.getTargetPosition());
			hardware.telemetry.addData("FL position: ", motorFL.getCurrentPosition());
			hardware.telemetry.addData("BL position: ", motorBL.getCurrentPosition());
			hardware.telemetry.addData("FR position: ", motorFR.getCurrentPosition());
			hardware.telemetry.addData("BR position: ", motorBR.getCurrentPosition());
			hardware.telemetry.update();
		}
		motors.forEach(m -> m.setPower(0));
		motors.forEach(m -> m.setMode(RUN_WITHOUT_ENCODER));
	}
}
