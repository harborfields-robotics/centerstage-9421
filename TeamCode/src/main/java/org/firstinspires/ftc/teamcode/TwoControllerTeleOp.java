package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

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
		boolean slowMode = gamepad1.left_trigger > TRIGGER_THRESHOLD;
		hardware.drivetrain.driveLoop(
				gamepad1.left_stick_y,
				gamepad1.left_stick_x,
				gamepad1.right_stick_x,
				slowMode ? SLOW_RATE : 1.0);
		if (gamepad1.dpad_up)
			hardware.launcher.launch();
		if (gamepad1.dpad_down)
			hardware.launcher.unlaunch();

		/* scoring (gamepad 2) */
		double slidePower = -Math.pow(gamepad2.left_stick_y, 3) * 0.75;
		if (gamepad2.left_bumper && gamepad2.right_bumper)
			hardware.hardwareMap.getAll(DcMotor.class).forEach(Hardware::resetEncoder);
		else if (gamepad2.left_bumper) {
			hardware.slides.leftMotor.setPower(slidePower);
			hardware.slides.rightMotor.setPower(slidePower);
		} else
			hardware.slides.setPower(slidePower);

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
		hardware.slides.setWristPosition(
				hardware.slides.getWristPosition()
				+ ((gamepad2.dpad_right ? 0 : 1) - (gamepad2.dpad_left ? 0 : 1))
				* WRIST_MOVE_RATE * Hardware.deltaTime()); 
		hardware.slides.setElbowPosition(
				hardware.slides.getElbowPosition()
				+ ((gamepad2.dpad_up ? 0 : 1) - (gamepad2.dpad_down ? 0 : 1))
				* ELBOW_MOVE_RATE * Hardware.deltaTime()); 
		hardware.loop();

		telemetry.update();
	}
}
