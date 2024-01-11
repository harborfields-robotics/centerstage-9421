package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="CV Testing OpMode", group="Testing OpMode")
public class CVTestTeleOP extends LinearOpMode
{
	private static Hardware hardware;
	@Override
	public void runOpMode() throws InterruptedException
	{
		hardware = new Hardware(hardwareMap, telemetry);

		telemetry.setAutoClear(true);
		hardware.doubleVision.putTelemetry();
		waitForStart();

		while (opModeIsActive()) {
			hardware.doubleVision.putTelemetry();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
	}
}
