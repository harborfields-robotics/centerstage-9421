package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.Intake;

@TeleOp(name="One Controller OpMode", group="Linear OpMode")
public class OneControllerTeleOp extends LinearOpMode
{
	private static boolean
		smoothControls = false,
		reverseControls = false;
	private static Hardware hardware;

	public Servo wristServo, elbowServo;

	public DcMotor teethMotor;
	public static final double TRIGGER_THRESHOLD = 0.3;

	@Override
	public void runOpMode() throws InterruptedException
	{
		hardware = new Hardware(hardwareMap, telemetry);

		waitForStart();

		DriveThread driveThread = new DriveThread(hardware);
		driveThread.start();

		while (opModeIsActive()) {
			telemetry.clear();
			if(gamepad1.right_trigger > .1)
				hardware.intake.intake();
			else if(gamepad1.left_trigger > .1)
				hardware.intake.outtake();
			else
				hardware.intake.stop();

			telemetry.addData("Servo Position", hardware.slides.elbowServo.getPosition());
			if (gamepad1.right_stick_y != 0)
				hardware.slides.setPower(gamepad1.right_stick_y);
			if (gamepad1.dpad_up)
				new Thread(() -> hardware.slides.runToSetPosition(hardware.slides.getCurrentSetPositionIndex() + 1)).start();
			else if (gamepad1.dpad_down)
				new Thread(() -> hardware.slides.runToSetPosition(hardware.slides.getCurrentSetPositionIndex() - 1)).start();
			telemetry.addData("Slides Position", hardware.slides.motor.getCurrentPosition());
			telemetry.addData("Slides Set Position", hardware.slides.getCurrentSetPositionIndex());

			telemetry.update();


		}
	}

	// TODO: factor out logic into drivetrain method
	public class DriveThread extends Thread
	{
		private Hardware hardware;
		public static final double
			SLOW_MULTIPLIER = 0.2,
			ACCELERATION = 0.05;

		public DriveThread(Hardware hardware)
		{
			this.hardware = hardware;
		}

		@Override
		public void run()
		{
			while (opModeIsActive()) {
				if (gamepad1.back) {
					smoothControls = true;
					reverseControls = false;
				} else if (gamepad1.right_bumper) {
					smoothControls = false;
					reverseControls = false;
				} else if (gamepad1.left_bumper) {
					smoothControls = false;
					reverseControls = true;
				}

				double y = -gamepad1.left_stick_y, // Remember, this is reversed!
					   x = gamepad1.left_stick_x,
					   rx = gamepad1.right_stick_x;

				if (gamepad1.left_trigger > TRIGGER_THRESHOLD) {
					y *= SLOW_MULTIPLIER;
					x *= SLOW_MULTIPLIER;
					rx *= SLOW_MULTIPLIER;
				}

				if (reverseControls) {
					y = -y;
					x = -x;
				}

				if (smoothControls) {
					hardware.drivetrain.driveLoopSmooth(y, x, rx, ACCELERATION);
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else
					hardware.drivetrain.driveLoop(y, x, rx);
			}
		}
	}
}
