package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class CVTestTeleOP extends LinearOpMode
{
	private static Hardware hardware;
	@Override
	public void runOpMode() throws InterruptedException
	{
		hardware = new Hardware(hardwareMap, telemetry);

		hardware.doubleVision.putTelemetry();
		waitForStart();

		while (opModeIsActive()) {
			hardware.doubleVision.putTelemetry();
			try {
				Thread.sleep(100);
			} catch (InterruptedException __) {}
		}
	}
}