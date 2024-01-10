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

/**
  * The Hardware class contains all other hardware subsystems, including telemetry and computer vision.
  */
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

	/**
	  * Creates a hardware object and initializes all hardware components.
	  */
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

	/**
	  * Runs the processing loops for hardware components.
	  * Processing loops handle behavior and checks that should occur frequently, ideally once per loop of the OpMode.
	  */
	public void loop()
	{
		intake.loop();
		slides.loop();
		launcher.loop();
	}

	/**
	  * Initializes and returns a hardware object representing the named device.
	  * @param type the type of the device
	  * @param name the name of the device in the configuration
	  * @return an object representing the device
	  * @see com.qualcomm.robotcore.hardware.HardwareMap#get(Class<? extends T>, String)
	  */
	public <T> T get(Class<? extends T> type, String name)
	{
		return hardwareMap.get(type, name);
	}

	/**
	  * Initializes and returns a hardware object representing the named device, or null if the device does not exist.
	  * @param type the type of the device
	  * @param name the name of the device in the configuration
	  * @return an object representing the device
	  * @see com.qualcomm.robotcore.hardware.HardwareMap#tryGet(Class<? extends T>, String)
	  */
	public <T> T tryGet(Class<? extends T> type, String name)
	{
		return hardwareMap.tryGet(type, name);
	}

	/**
	  * Resets the encoder value to zero and returns the motor to its previous state.
	  * @param motor the motor to operate on
	  */
	public static void resetEncoder(DcMotor motor)
	{
		DcMotor.RunMode mode = motor.getMode();
		motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		motor.setMode(mode);
	}

	/**
	  * Returns the name of a hardware device as it appears in the configuration.
	  * @param device an object representing the hardware device
	  * @return the name of the device in the configuration
	  */
	public String getDeviceName(HardwareDevice device)
	{
		Set<String> names = hardwareMap.getNamesOf(device);
		return names.isEmpty() ? "<unknown device>" : names.toArray(new String[0])[0];
	}
}
