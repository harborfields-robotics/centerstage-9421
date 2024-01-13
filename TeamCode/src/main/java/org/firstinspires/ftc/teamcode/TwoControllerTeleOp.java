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
	Gamepad 2 (Scoring):
		* left stick y - slides
		* right trigger - intake
		* left trigger - outtake
		* square - outtake one pixel
   */

@TeleOp(name="Two Controller OpMode", group="OpMode")
public class TwoControllerTeleOp extends OpMode
{
	private Hardware hardware;


	public final double TRIGGER_THRESHOLD = 0.1;
	public final double SLOW_RATE = 0.25;

	boolean toggle = false;

	@Override
	public void init()
	{
		hardware = new Hardware(hardwareMap, telemetry);
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

		/* scoring (gamepad 2) */
		hardware.slides.setPower(-gamepad2.left_stick_y * 0.75);
		if(gamepad2.right_bumper)
			hardware.slides.wristServo.setPosition(0.5);
		if(gamepad2.cross)
			hardware.slides.wristServo.setPosition(hardware.slides.wristServo.getPosition() + .05);
		if(gamepad2.triangle)
			hardware.slides.elbowServo.setPosition(.75);
		if(gamepad2.left_bumper)
			hardware.slides.elbowServo.setPosition(1);
		if (gamepad2.right_trigger > TRIGGER_THRESHOLD)
			hardware.intake.intake();
		else if (gamepad2.left_trigger > TRIGGER_THRESHOLD)
			hardware.intake.outtake();
		else
			hardware.intake.stop();

		if (gamepad2.square)
			hardware.intake.outtakeOne();

		hardware.loop();

		telemetry.addData("Slides Position", hardware.slides.motor.getCurrentPosition());
		telemetry.addData("Slides Set Position", hardware.slides.getCurrentSetPositionIndex());
		telemetry.addData("Elbow Position", hardware.slides.elbowServo.getPosition());
		telemetry.addData("Wrist Position", hardware.slides.wristServo.getPosition());

		telemetry.update();
	}
}