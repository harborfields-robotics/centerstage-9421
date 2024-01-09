package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import java.util.*;
import java.util.stream.*;

/**
  * The Slides class handles the movement of the slides and the sandwitch.
  */
public class Slides
{
    public DcMotor motor;
	public Servo wristServo, elbowServo;

	/**
	  * The circumference of the slides' spool, in inches.
	  */
	public static final double SPOOL_CIRCUMFERENCE = 112 / 25.4; // 112 mm
		
	/**
	  * The highest possible position of the slides, in encoder ticks.
	  */
	public static final int MAX_POSITION = 6125;

	/**
	  * The lowest possible position of the slides, in encoder ticks.
	  * This should be the rest position of the slides.
	  */
	public static final int MIN_POSITION = 0;

	/**
	  * The size, in encoder ticks, of the safety zone at both ends of the slides' range.
	  * The slides will be prevented from moving further into a dead zone.
	  */
	public static final int DEADZONE_SIZE = 20;

	/**
	  * The array of pre-defined slides positions for ease of driving.
	  * Each element is the value of the motor encoder at the set position's height
	  * @see #getCurrentSetPositionIndex()
	  * @see #runToSetPosition(int)
	  */
	public static final int SET_POSITIONS[] = { MIN_POSITION, MAX_POSITION * 1/3, MAX_POSITION * 2/3, MAX_POSITION };

    public Slides(Hardware hardware)
    {
        this.motor = hardware.get(DcMotor.class, Hardware.SLIDES_MOTOR_NAME);
		this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		// XXX: does this reverse the encoder direction as well?
		this.motor.setDirection(DcMotorSimple.Direction.REVERSE);

		this.wristServo = hardware.get(Servo.class, Hardware.WRIST_SERVO_NAME);
		this.elbowServo = hardware.get(Servo.class, Hardware.ELBOW_SERVO_NAME);
    }

	/**
	  * Sets the power of the slides motor, being careful not to exceed its limits.
	  * This method relies on {@link #loop()} being called frequently to ensure the limits will not be exceeded in the future.
	  * @param power the new power of the motor
	  * @see #loop()
	  */
	public void setPower(double power)
	{
		int position = motor.getCurrentPosition();
		if (power > 0 && MAX_POSITION - position <= DEADZONE_SIZE)
			power = 0;
		if (power < 0 && MIN_POSITION - position >= -DEADZONE_SIZE)
			power = 0;
		motor.setPower(power);
	}

	/**
	  * Gets the current power of the slides motor.
	  */
	public double getPower()
	{
		return motor.getPower();
	}

	/**
	  * Returns the index of the set position closest to the slides' current height.
	  * @return the index of this position in {@link #SET_POSITIONS}
	  */
	public int getCurrentSetPositionIndex()
	{
		int position = motor.getCurrentPosition();
		int minIndex = 0;
		double minDiff = Double.POSITIVE_INFINITY;
		for (int i = 0; i < SET_POSITIONS.length; i++) {
			int diff = Math.abs(position - SET_POSITIONS[i]);
			if (diff < minDiff) {
				minDiff = diff;
				minIndex = i;
			}
		}
		return minIndex;
	}

	/**
	  * Instructs the slides motor to run to the set position of the given index without blocking the current thread.
	  * The index is clamped to fall within the bounds of the array.
	  * @param index the index of the target set position in {@link #SET_POSITIONS}
	  */
	public void runToSetPosition(int index)
	{
		runToPosition(SET_POSITIONS[Range.clip(index, 0, SET_POSITIONS.length - 1)]);
	}

	/**
	  * Instructs the slides motor to run to the target encoder position.
	  * This uses the motor's internal PIDF loop.
	  * @param position the target encoder position
	  */
	public void runToPosition(int position)
	{
		DcMotor.RunMode mode = motor.getMode();
		motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		// XXX: This uses the motor's internal PID so we may have to tune it
		motor.setTargetPosition(position);
		try {
			while (motor.isBusy())
				Thread.sleep(50);
		} catch (InterruptedException e) {}
		motor.setMode(mode);
	}
}
