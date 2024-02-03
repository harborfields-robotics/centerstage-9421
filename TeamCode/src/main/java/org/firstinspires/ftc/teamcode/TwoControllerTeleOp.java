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
	public final double SLOW_RATE = 0.40;
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
		if (gamepad1.dpad_up)
			hardware.launcher.launch();
		if (gamepad1.dpad_down)
			hardware.launcher.unlaunch();

		/* scoring (gamepad 2) */
		hardware.slides.setPower(gamepad2.left_stick_y * 0.75);

		if (gamepad2.right_trigger > TRIGGER_THRESHOLD)
			hardware.intake.intake();
		else if (gamepad2.left_trigger > TRIGGER_THRESHOLD)
			hardware.intake.outtake();
		else
			hardware.intake.stop();

		if (gamepad2.right_bumper) {
			hardware.slides.leftElbowServo.setPosition(Slides.LEFT_ELBOW_REST_POSITION);
			hardware.slides.rightElbowServo.setPosition(Slides.RIGHT_ELBOW_REST_POSITION);
		}
		if (gamepad2.left_bumper) {
			hardware.slides.leftElbowServo.setPosition(Slides.LEFT_ELBOW_OUT_POSITION);
			hardware.slides.rightElbowServo.setPosition(Slides.RIGHT_ELBOW_OUT_POSITION);
		}
		/*if (gamepad2.cross)
			hardware.slides.wristServo.setPosition(Slides.WRIST_DROP_POSITION);
		if (gamepad2.square) {
			hardware.slides.wristServo.setPosition(Slides.WRIST_GRAB_POSITION);
			hardware.slides.elbowServo.setPosition(Slides.ELBOW_OUT_POSITION);
		}
		if (gamepad2.circle)
			hardware.slides.wristServo.setPosition(Slides.WRIST_REST_POSITION);

		if (gamepad2.dpad_up)
			hardware.slides.wristServo.setPosition(hardware.slides.wristServo.getPosition() + 0.005);
		if (gamepad2.dpad_down)
			hardware.slides.wristServo.setPosition(hardware.slides.wristServo.getPosition() - 0.005);
		if (gamepad2.dpad_left)
			hardware.slides.elbowServo.setPosition(hardware.slides.elbowServo.getPosition() + 0.005);
		if (gamepad2.dpad_right)
			hardware.slides.elbowServo.setPosition(hardware.slides.elbowServo.getPosition() - 0.005);
		*/
		hardware.loop();

		telemetry.addData("Slides Position", hardware.slides.motor.getCurrentPosition());
		telemetry.addData("Slides Set Position", hardware.slides.getCurrentSetPositionIndex());
		telemetry.addData("Left Elbow Position", hardware.slides.leftElbowServo.getPosition());
		telemetry.addData("Right Elbow Position", hardware.slides.rightElbowServo.getPosition());
		// telemetry.addData("Wrist Position", hardware.slides.wristServo.getPosition());

		telemetry.update();
	}
}
