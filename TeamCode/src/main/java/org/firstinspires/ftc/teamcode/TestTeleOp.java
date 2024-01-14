package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Testing OpMode", group="Testing OpMode")
public class TestTeleOp extends LinearOpMode
{
    private DcMotor motor;
    private ElapsedTime runtime = new ElapsedTime();
	private Hardware hardware;

    @Override
    public void runOpMode()
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

		hardware = new Hardware(hardwareMap, telemetry);

        waitForStart();
        runtime.reset();

		boolean enabled = false;

        while (opModeIsActive()) {

			if (enabled) {

			}

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("slides", hardware.slides.motor.getCurrentPosition());
            telemetry.addData("wrist", hardware.slides.wristServo.getPosition());
            telemetry.addData("elbow", hardware.slides.elbowServo.getPosition());
			telemetry.update();
        }
    }
}
