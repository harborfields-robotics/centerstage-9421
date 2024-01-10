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

		boolean slowMode = gamepad1.right_trigger > TRIGGER_THRESHOLD;
		hardware.drivetrain.driveLoop(
				gamepad1.left_stick_y,
				gamepad1.left_stick_x,
				gamepad1.right_stick_x,
				slowMode ? 1 : SLOW_RATE);
		hardware.slides.setPower(
				Util.controllerValueWithDeadzone(
					gamepad1.right_stick_y, 0.25));

		telemetry.update();
	}
}
