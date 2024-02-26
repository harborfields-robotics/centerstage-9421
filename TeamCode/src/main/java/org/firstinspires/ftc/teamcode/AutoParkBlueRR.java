package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Disabled
@Autonomous(name="Park as Blue (RoadRunner)", group="OpMode")
public class AutoParkBlueRR extends OpMode
{
	Hardware hardware;
	// VoltageSensor voltageSensor;

	public void init()
	{
		hardware = new Hardware(hardwareMap, telemetry);
		// hardwareMap.voltageSensor.iterator().next();
	}

	public void start()
	{
		Util.sleep(10 * 1000);
		hardware.drivetrain.stop();
		stop();
	}

	public void loop() {}
}
