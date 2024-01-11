package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.Intake;

@TeleOp(name="One Controller OpMode", group="OpMode")
public class OneControllerTeleOp extends OpMode
{
	private Hardware hardware;

	public static final double TRIGGER_THRESHOLD = 0.1;
	public static final double SLOW_RATE = 0.25;

	@Override
	public void init()
	{
		hardware = new Hardware(hardwareMap, telemetry);
	}

	@Override
	public void loop()
	{
		telemetry.clear();

		boolean slowMode = gamepad1.left_trigger > TRIGGER_THRESHOLD;
		hardware.drivetrain.driveLoop(
				gamepad1.left_stick_y,
				gamepad1.left_stick_x,
				Util.controllerValueWithDeadzone(gamepad1.right_stick_x, 0.25),
				slowMode ? SLOW_RATE : 1);
		hardware.slides.setPower(-gamepad1.right_stick_y * 0.75);

		if (gamepad1.right_trigger > TRIGGER_THRESHOLD)
			hardware.intake.intake();
		else if (gamepad1.circle)
			hardware.intake.outtake();
		else
			hardware.intake.stop();

		if (gamepad1.square)
			hardware.intake.outtakeOne();

		if (gamepad1.triangle)
			hardware.slides.setWristPosition(0.5);

		hardware.loop();

		telemetry.update();
	}
}