package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Auto", group="OpMode")
public class Auto extends OpMode
{
	Hardware hardware;
	DcMotor motorFR;
	DcMotor motorFL;
	DcMotor motorBR;
	DcMotor motorBL;

	public void init()
	{
		hardware = new Hardware(hardwareMap, telemetry);
		// hi :)
		motorFR = hardwareMap.dcMotor.get("fr-motor");
		motorFL = hardwareMap.dcMotor.get("fl-motor");
		motorBR = hardwareMap.dcMotor.get("br-motor");
		motorBL = hardwareMap.dcMotor.get("bl-motor");
	}

	public void loop()
	{
		DriveRight(1);
		Util.sleep(2000);
		Stop();
		Util.sleep(2000);
	}

	public void DriveRight(double power)
	{
		motorFR.setPower(-power);
		motorFL.setPower(power);
		motorBR.setPower(power);
		motorBL.setPower(-power);
	}

	public void Stop()
	{
		DriveRight(0);
	}
}
