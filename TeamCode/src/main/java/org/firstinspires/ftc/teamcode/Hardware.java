package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import org.firstinspires.ftc.teamcode.drive.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Encoder;

import java.util.Set;

/**
  * The Hardware class contains all other hardware subsystems, including telemetry and computer vision.
  */
public class Hardware
{
    public HardwareMap hardwareMap;
	public Drivetrain drivetrain;
	public static Telemetry telemetry;
	public DoubleVision doubleVision;
	public Slides slides;
	public Intake intake;
	public Launcher launcher;
	public SampleMecanumDrive drive;

	public static Encoder leftEncoder, backEncoder, rightEncoder;
	private static double lastTimestamp;

	// TODO: update configuration names
	public static final String
		WEBCAM_NAME             = "Webcam 1",          /* USB port             */

		FL_MOTOR_NAME           = "fl-motor",          /* control hub port 0   */
		BL_MOTOR_NAME           = "bl-motor",          /* control hub port 1   */
		BR_MOTOR_NAME           = "br-motor",          /* control hub port 2   */
		FR_MOTOR_NAME           = "fr-motor",          /* control hub port 3   */

		// They're really more like lips to be honest but they _look_ like teeth
		TEETH_MOTOR_NAME        = "teeth-motor",       /* expansion hub port 1 */
		TONGUE_SERVO_NAME       = "tongue-servo",      /* expansion hub port 0 */

		// I guess his mouth is on his hand
		LEFT_SLIDES_MOTOR_NAME  = "left-slides-motor", /* expansion hub port 0 */
		RIGHT_SLIDES_MOTOR_NAME = "right-slides-motor", /* expansion hub port 0 */
		LIMIT_SWITCH_NAME       = "limit-switch",      /* control hub port 0   */
		RIGHT_WRIST_SERVO_NAME  = "right-wrist-servo", /* expansion hub port 1 */
		LEFT_WRIST_SERVO_NAME   = "left-wrist-servo",  /* expansion hub port 3 */
		RIGHT_ELBOW_SERVO_NAME  = "right-elbow-servo", /* expansion hub port 2 */
		LEFT_ELBOW_SERVO_NAME   = "left-elbow-servo",  /* expansion hub port 4 */

		// FIXME: encoders must use the names of existing motors due to space constraints
		LEFT_ENCODER_NAME       = "fr-motor",       /* expansion hub port 3 */
		BACK_ENCODER_NAME       = "left-slides-motor", /* expansion hub port 2 */
		RIGHT_ENCODER_NAME      = "right-encoder",     /* expansion hub port 1 */

		LAUNCHER_SERVO_NAME     = "launcher-servo";    /* control hub port 5 */

	/**
	  * Creates a hardware object and initializes all hardware components.
	  */
	public Hardware(HardwareMap hardwareMap, Telemetry telemetry)
	{
		this.hardwareMap = hardwareMap;
		this.telemetry = telemetry; //new MultipleTelemetry(telemetry, dashboard.getTelemetry());
		// this.telemetry.setAutoClear(false);
		this.drivetrain = new Drivetrain(this);
		this.doubleVision = new DoubleVision(this);
		this.slides = new Slides(this);
		this.intake = new Intake(this);
		this.launcher = new Launcher(this);
		lastTimestamp = System.nanoTime();
	}

	public static double deltaTime()
	{
		long now = System.nanoTime();
		double delta = (now - lastTimestamp) / 1e9;
		lastTimestamp = now;
		return delta;
	}

	/**
	  * Runs the processing loops for hardware components.
	  * Processing loops handle behavior and checks that should occur frequently, ideally once per loop of the OpMode.
	  */
	public void loop()
	{
		intake.loop();
		slides.loop();
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
