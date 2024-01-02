package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import java.util.*;
import java.util.stream.*;

public class Slides
{
    private DcMotor motor;
	private Servo wristServo, elbowServo;

	public static final double
		SPOOL_CIRCUMFERENCE = 112 / 25.4, // 112 mm
		SLIDES_ANGLE = 37; // ? degrees
		
	public static final int
		MAX_POSITION = 6125,
		MIN_POSITION = 0,
		DEADZONE_SIZE = 20,
		SET_POSITIONS[] = { MIN_POSITION, 2000, 4000, MAX_POSITION };

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

	public void setPower(double power)
	{
		int position = motor.getPosition();
		if (power > 0 && MAX_POSITION - position <= DEADZONE_SIZE)
			power = 0;
		if (power < 0 && MIN_POSITION - position >= -DEADZONE_SIZE)
			power = 0;
		motor.setPower(power);
	}

	public void getPower()
	{
		return motor.getPower();
	}

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

	public void runToSetPosition(int position)
	{
		runToPosition(SET_POSITIONS[Range.clip(position, 0, SET_POSITIONS.length - 1)]);
	}

	public void runToPosition(int position)
	{
		DcMotor.RunMode mode = motor.getMode();
		motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		// XXX: This uses the motor's internal PID so we may have to tune it
		motor.setTargetPosition(position);
		try {
			while (motor.isBusy())
				Thread.sleep(50);
		} catch (InterruptedException _) {}
		motor.setMode(mode);
	}
}
