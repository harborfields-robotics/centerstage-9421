package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Auto", group="OpMode")
public class Auto extends OpMode
{
	Hardware hardware;

	public void init()
	{
		hardware = new Hardware(hardwareMap, telemetry);
		for (DcMotorEx m: hardware.drivetrain.motors) {
			m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
			// m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
	}

	public void loop() {
		telemetry.addLine("...");
		telemetry.update();
		// for (DcMotorEx m: hardware.drivetrain.motors) {
		// 	m.setVelocity(Math.PI / 16);
		// }
	}
}
