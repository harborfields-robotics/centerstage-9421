package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.internal.camera.delegating.DelegatingCaptureSequence;

/* Controller Layout
   ------------------
	Gamepad 1 (Movement):
		* left stick - movement
		* right stick x - strafing
		* right stick y - forward/backward
		* left stick x - rotation
		* left trigger - slow mode (hold)
		* dpad up - release launcher servo
	Gamepad 2 (Scoring):
		* left stick y - slides
		* right trigger - intake continuous
		* left trigger - outtake continuous
		* right bumper - elbow return position
		* left bumper - elbow drop position
		* cross - wrist drop position
		* circle - wrist in drive position
		* square - wrist in grab position
		* triangle - elbow out position
   */

@TeleOp(name="Two Controller OpMode", group="OpMode")
public class TwoControllerTeleOp extends OpMode
{
	private Hardware hardware;
	public final double TRIGGER_THRESHOLD = 0.1;
	public final double SLOW_RATE = 0.60;
	public final double WRIST_MOVE_RATE = 0.10;
	public final double ELBOW_MOVE_RATE = 0.10;
	private long counts = 0;

	@Override
	public void init()
	{
		hardware = new Hardware(hardwareMap, telemetry);
	}

	@Override
	public void start()
	{
	}

	@Override
	public void loop()
	{
		telemetry.clear();

		/* movement (gamepad 1) */
		boolean slideSafety = !(gamepad1.left_bumper || gamepad2.left_bumper) && hardware.slides.limitSwitch.isPressed();
		boolean slowMode = gamepad1.left_trigger > TRIGGER_THRESHOLD;

		double y = -Math.min(gamepad1.left_stick_y, slideSafety ? 0 : 1); // Remember, Y stick value is reversed
		double x =  gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
		double rx = slideSafety ? 0 : gamepad1.right_stick_x;

		// Denominator is the largest motor power (absolute value) or 1
		// This ensures all the powers maintain the same ratio,
		// but only if at least one is out of the range [-1, 1]
		// double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
		double denominator = 1/(0.5 * (slowMode ? SLOW_RATE : 1));
		double[] powers = {
			(y + x + rx) / denominator,
			(y - x + rx) / denominator,
			(y + x - rx) / denominator,
			(y - x - rx) / denominator
		};
		for (int i = 0; i < powers.length; i++)
			hardware.drivetrain.motors.get(i).setPower(powers[i]);

		if (gamepad1.dpad_up)
			hardware.launcher.launch();
		if (gamepad1.dpad_down)
			hardware.launcher.unlaunch();

		/* scoring (gamepad 2) */
		double slidePower = -Math.pow(gamepad2.left_stick_y, 3) * 0.75;
		if (gamepad2.left_bumper)
			hardware.slides.winchMotor.setPower(gamepad2.right_stick_x);
		else
			hardware.slides.setWinchPower(gamepad2.right_stick_x);
		if (gamepad2.left_bumper && gamepad2.right_bumper)
			hardware.hardwareMap.getAll(DcMotor.class).forEach(Hardware::resetEncoder);
		else if (gamepad2.left_bumper) {
//			hardware.slides.stopHolding();
			hardware.slides.leftMotor.setPower(slidePower);
			hardware.slides.rightMotor.setPower(slidePower);
		} else {
			if (slidePower != 0)
				hardware.slides.setPower(slidePower);
			//else
			//	hardware.slides.holdPosition();
		}

		if (gamepad2.right_trigger > TRIGGER_THRESHOLD)
			hardware.intake.intake();
		else if (gamepad2.left_trigger > TRIGGER_THRESHOLD)
			hardware.intake.outtake();
		else
			hardware.intake.stop();

		if (gamepad2.circle && !gamepad2.start)
			hardware.slides.setArmPosition(Slides.ArmPosition.SCORE_MAX);
		if (gamepad2.cross && !gamepad2.start)
			hardware.slides.setArmPosition(Slides.ArmPosition.DOWN);
		if (gamepad2.triangle)
			hardware.slides.setArmPosition(Slides.ArmPosition.UP);
		if (gamepad2.square)
				if (hardware.slides.slideState == Slides.SlideState.HOLDING_POSITION)
					hardware.slides.stopHolding();
				else
					hardware.slides.holdPosition();
		// hardware.slides.setWristPosition(
		// 		hardware.slides.getWristPosition()
		// 		+ ((gamepad2.dpad_right ? 0 : 1) - (gamepad2.dpad_left ? 0 : 1))
		// 		* WRIST_MOVE_RATE * Hardware.deltaTime());
		hardware.slides.leftElbowServo.setPosition(
				hardware.slides.leftElbowServo.getPosition()
				+ ((gamepad2.dpad_up ? 0 : 1) - (gamepad2.dpad_down ? 0 : 1))
				* ELBOW_MOVE_RATE * Hardware.deltaTime());
		hardware.slides.rightElbowServo.setPosition(
				hardware.slides.rightElbowServo.getPosition()
						+ ((gamepad2.dpad_right ? 0 : 1) - (gamepad2.dpad_left ? 0 : 1))
						* ELBOW_MOVE_RATE * Hardware.deltaTime());

		telemetry.addData("slide pos", hardware.slides.rightMotor.getCurrentPosition());
		telemetry.addData("slide pwr", hardware.slides.rightMotor.getPower());
		telemetry.addData("slide act", hardware.slides.slideState);
		telemetry.addData("winch pos", hardware.slides.winchMotor.getCurrentPosition());
		telemetry.addData("deltaTime", Hardware.deltaTime());
		telemetry.update();

		hardware.loop();
	}
}