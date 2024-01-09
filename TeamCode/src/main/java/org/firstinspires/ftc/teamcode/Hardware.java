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
	public Intake intake;
	public Launcher launcher;

	// TODO: update configuration names
	public static final String
		WEBCAM_NAME         = "webcam",        /* USB port             */

		FL_MOTOR_NAME       = "fl-motor",      /* control hub port 0   */
		BL_MOTOR_NAME       = "bl-motor",      /* control hub port 1   */
		BR_MOTOR_NAME       = "br-motor",      /* control hub port 2   */
		FR_MOTOR_NAME       = "fr-motor",      /* control hub port 3   */

		// They're really more like lips to be honest but they _look_ like teeth
		TEETH_MOTOR_NAME    = "intake-motor",  /* expansion hub port 1 */
		TONGUE_SERVO_NAME   = "wheel-servo",   /* expansion hub port 0 */

		// I guess his mouth is on his hand
		SLIDES_MOTOR_NAME   = "slides-motor",  /* expansion hub port 0 */
		WRIST_SERVO_NAME    = "wrist-servo",   /* expansion hub port 1 */
		ELBOW_SERVO_NAME    = "elbow-servo",   /* expansion hub port 2 */

		// FIXME: encoders must use the names of existing motors due to space constraints
		LEFT_ENCODER_NAME   = "left-encoder",  /* expansion hub port 1 */
		BACK_ENCODER_NAME   = "back-encoder",  /* expansion hub port 2 */
		RIGHT_ENCODER_NAME  = "right-encoder", /* expansion hub port 3 */

		LAUNCHER_MOTOR_NAME = "launcher-motor"; /* expansion hub port ? */


	public Hardware(HardwareMap hardwareMap, Telemetry telemetry)
	{
		this.hardwareMap = hardwareMap;
		this.telemetry = telemetry;
		this.telemetry.setAutoClear(false);
		this.drivetrain = new Drivetrain(this);
		this.doubleVision = new DoubleVision(this);
		this.slides = new Slides(this);
		this.intake = new Intake(this);
		this.launcher = new Launcher(this);
	}

	public <T> T get(Class<T> type, String name)
	{
		return hardwareMap.get(type, name);
	}

	public static void resetEncoder(DcMotor motor)
	{
		DcMotor.RunMode mode = motor.getMode();
		motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		motor.setMode(mode);
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
