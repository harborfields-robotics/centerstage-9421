package org.firstinspires.ftc.teamcode;

import android.transition.Slide;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.lang.Math;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hardware
{
	public HardwareMap hardwareMap;
	public Drivetrain drivetrain;
	public Telemetry telemetry;
	public DoubleVision doubleVision;
	public Slides slides;

	public DcMotor intakeMotor;

	public Map<String, PIDConstants> pidConstants = new HashMap<>(Stream.of(new Object[][]{
			{ MOTOR_FL_NAME, new PIDConstants(0, 0, 0) },
			{ MOTOR_BL_NAME, new PIDConstants(0, 0, 0) },
			{ MOTOR_BR_NAME, new PIDConstants(0, 0, 0) },
			{ MOTOR_FR_NAME, new PIDConstants(0, 0, 0) },
			{ SLIDES_NAME,   new PIDConstants(0, 0, 0) },
	}).collect(Collectors.toMap(i -> (String) i[0], i -> (PIDConstants) i[1])));

	public Servo wristServo, wheelServo, elbowServo;

	public static final String
		WEBCAM_NAME = "webcam", /* USB port */
		MOTOR_FL_NAME = "motor-fl", /* control hub port 0 */
		MOTOR_BL_NAME = "motor-bl", /* control hub port 1 */
		MOTOR_BR_NAME = "motor-br", /* control hub port 2 */
		MOTOR_FR_NAME = "motor-fr", /* control hub port 3 */
		SLIDES_NAME = "slides-motor", /* expansion hub port 0 */
		INTAKE_NAME = "intake-motor", /* expansion hub port 1 */
		WHEEL_NAME = "wheel-servo", /*expansion servo port 0 */
		WRIST_NAME = "wrist-servo",/*expansion servo port 1 */
		ELBOW_NAME = "elbow-servo"; /*expansion servo port 2 */

	public Hardware(HardwareMap hardwareMap, Telemetry telemetry)
	{
		this.hardwareMap = hardwareMap;
		this.telemetry = telemetry;
		this.drivetrain = new Drivetrain(this);
		this.doubleVision = new DoubleVision(this);
		this.slides = new Slides(this);

		this.intakeMotor = hardwareMap.get(DcMotor.class, INTAKE_NAME);
		this.intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		this.wheelServo = hardwareMap.get(Servo.class, WHEEL_NAME);
		this.wristServo = hardwareMap.get(Servo.class, WRIST_NAME);
		this.elbowServo = hardwareMap.get(Servo.class, ELBOW_NAME);
		this.elbowServo = hardwareMap.get(Servo.class, ELBOW_NAME);
	}

	public static void resetMotorEncoder(DcMotor motor)
	{
		DcMotor.RunMode mode = motor.getMode();
		motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		motor.setMode(mode);
	}

	public PIDConstants lookupPIDConstants(String motorName)
	{
		return pidConstants.get(motorName);
	}

	public String getDeviceName(HardwareDevice device)
	{
		Set<String> names = hardwareMap.getNamesOf(device);
		return names.isEmpty() ? "<unknown device>" : names.toArray(new String[0])[0];
	}

	public static boolean motorAtTarget(DcMotor motor, int initialPosition)
	{
		int position = motor.getCurrentPosition(),
			target = motor.getTargetPosition();
		return target < initialPosition ? position <= target : position >= target;
	}

	public static boolean motorAtTarget(DcMotor motor)
	{
		return motorAtTarget(motor, 0);
	}
}