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
	public Servo leftWristServo, rightWristServo, leftElbowServo, rightElbowServo;

	/**
	  * The circumference of the slides' spool, in inches.
	  */
	public static final double SPOOL_CIRCUMFERENCE = 112 / 25.4; // 112 mm
		
	/**
	  * The highest possible position of the slides, in encoder ticks.
	  */
	public static final int MAX_POSITION = 2100;

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

	public static class Position
	{
		public double left, right;
		public Position(double left, double right)
		{
			this.left = left;
			this.right = right;
		}
	}

	public static final double
		WRIST_DOWN_POSITION = 0.656,
		WRIST_UP_POSITION = 0.859,
		WRIST_MAX_POSITION = 0.974;

	public static final double
		ELBOW_DOWN_POSITION = 0.881,
		ELBOW_UP_POSITION = 0.932,
		ELBOW_MAX_POSITION = 0.252;

	// WRIST DROP MAX: 0.725L 0.139R
	// WRIST DRIVE: 0.072L 0.757R
	// WRIST UP: 0.000L 0.936R
	// SLIDES MAX: 2106

	/**
	  * Represents the possible states of the slides.
	  */
	public static enum SlideState
	{
		STOPPED,
		RUNNING_TO_POSITION,
		RUNNING_CONTINUOUS
	}

	public static enum ArmPosition
	{
		STOPPED, DOWN, UP, SCORE_MIN, SCORE_MAX
	}

	private SlideState slideState = SlideState.STOPPED;
	private ArmPosition armPosition = ArmPosition.STOPPED;

    public Slides(Hardware hardware)
    {
        this.motor = hardware.get(DcMotor.class, Hardware.SLIDES_MOTOR_NAME);
        this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		// XXX: does this reverse the encoder direction as well?
		this.motor.setDirection(DcMotorSimple.Direction.FORWARD);

		this.leftWristServo = hardware.get(Servo.class, Hardware.LEFT_WRIST_SERVO_NAME);
		this.rightWristServo = hardware.get(Servo.class, Hardware.RIGHT_WRIST_SERVO_NAME);
		this.leftElbowServo = hardware.get(Servo.class, Hardware.LEFT_ELBOW_SERVO_NAME);
		this.rightElbowServo = hardware.get(Servo.class, Hardware.RIGHT_ELBOW_SERVO_NAME);
    }

	/**
	  * The processing loop for the slides.
	  * Handles behavior and checks that should occur once per frame.
	  * @see Hardware#loop()
	  */
	public void loop()
	{
		switch (slideState) {
			case RUNNING_TO_POSITION:
				if (!motor.isBusy()) {
					motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
					slideState = SlideState.STOPPED;
				}
				break;
			case RUNNING_CONTINUOUS:
				if (!canMoveWithPower(motor.getPower()))
					motor.setPower(0);
				break;
			case STOPPED:
				break;
		}
	}

	/**
	  * Sets the power of the slides motor, being careful not to exceed its limits.
	  * This method relies on {@link #loop()} being called frequently to ensure the limits will not be exceeded in the future.
	  * @param power the new power of the motor
	  * @see #loop()
	  */
	public void setPower(double power)
	{
		if (slideState == SlideState.RUNNING_TO_POSITION)
			motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		if (!canMoveWithPower(power))
			power = 0;
		slideState = SlideState.RUNNING_CONTINUOUS;
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
	  * @see #SET_POSITIONS
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
	  * Returns whether the motor is allowed to move given a proposed power value (whether it will not move further into a deadzone)
	  * @param power the current/new power of the motor
	  * @return whether the motor can safely run using that power value
	  */
	public boolean canMoveWithPower(double power)
	{
		int position = motor.getCurrentPosition();
		return !(power > 0 && MAX_POSITION - position <= DEADZONE_SIZE
				|| power < 0 && MIN_POSITION - position >= -DEADZONE_SIZE);
	}

	/**
	  * Instructs the slides motor to run to the set position of the given index without blocking the current thread.
	  * The index is clamped to fall within the bounds of the array.
	  * This relies on the processing loop being called frequently.
	  * @param index the index of the target set position in {@link #SET_POSITIONS}
	  * @see #SET_POSITIONS
	  * @see #loop()
	  */
	public void runToSetPosition(int index)
	{
		runToPosition(SET_POSITIONS[Range.clip(index, 0, SET_POSITIONS.length - 1)]);
	}

	/**
	  * Instructs the slides motor to run to the target encoder position without blocking the current thread.
	  * This uses the motor's internal PIDF loop.
	  * This relies on the processing loop being called frequently.
	  * @param position the target encoder position
	  * @see #loop()
	  */
	public void runToPosition(int position)
	{
		motor.setTargetPosition(position);
		motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		// XXX: This uses the motor's internal PID so we may have to tune it
		motor.setPower(1);
		slideState = SlideState.RUNNING_TO_POSITION;
	}

	/**
	 * Instructs the wrist servo to move to the target position.
	 * @param leftPosition the target position for the left wrist servo, a value in the range [0, 1]
	 * @param rightPosition the target position for the right wrist servo, a value in the range [0, 1]
	 */
	public void setWristPosition(double leftPosition, double rightPosition)
	{
		leftWristServo.setPosition(leftPosition);
		rightWristServo.setPosition(rightPosition);
	}

	public void setWristPosition(double position)
	{
		setWristPosition(1 - position, position);
	}

	public double getWristPosition()
	{
		return rightWristServo.getPosition();
	}

	public void setWristPosition(Position position)
	{
		setWristPosition(position.left, position.right);
	}

	/**
	 * Instructs the elbow servos to move to the target position.
	 * @param leftPosition the target position for the left elbow servo, a value in the range [0, 1]
	 * @param rightPosition the target position for the right elbow servo, a value in the range [0, 1]
	 */
	public void setElbowPosition(double leftPosition, double rightPosition)
	{
		leftElbowServo.setPosition(leftPosition);
		rightElbowServo.setPosition(rightPosition);
	}

	public void setElbowPosition(double position)
	{
		setElbowPosition(1 - position, position);
	}

	public double getElbowPosition()
	{
		return rightElbowServo.getPosition();
	}

	public void setElbowPosition(Position position)
	{
		setElbowPosition(position.left, position.right);
	}

	public void setArmPosition(ArmPosition slideState)
	{
		switch (armPosition) {
			case DOWN:
			case STOPPED:
				switch (slideState) {
					case UP:
						setWristPosition(WRIST_UP_POSITION);
						setElbowPosition(ELBOW_UP_POSITION);
						break;
					case SCORE_MAX:
						setArmPosition(ArmPosition.UP);
						setArmPosition(ArmPosition.SCORE_MAX);
						break;
				} break;
			case UP:
				switch (slideState) {
					case SCORE_MAX:
						setElbowPosition(ELBOW_MAX_POSITION);
						setWristPosition(WRIST_MAX_POSITION);
						break;
					case DOWN:
						setElbowPosition(ELBOW_DOWN_POSITION);
						setWristPosition(WRIST_DOWN_POSITION);
						break;
				} break;
			case SCORE_MAX:
				switch (slideState) {
					case DOWN:
						setElbowPosition(ELBOW_DOWN_POSITION);
						setWristPosition(WRIST_DOWN_POSITION);
						break;
					case UP:
						setArmPosition(ArmPosition.DOWN);
						setElbowPosition(ELBOW_UP_POSITION);
						setWristPosition(WRIST_UP_POSITION);
						break;
				} break;
		}
		armPosition = slideState;
	}
}
