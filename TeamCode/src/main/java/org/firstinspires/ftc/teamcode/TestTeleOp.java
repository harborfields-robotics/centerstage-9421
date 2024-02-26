package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor; import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Testing OpMode", group="Testing OpMode")
public class TestTeleOp extends LinearOpMode
{
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

		Servo[] servos = { };
        while (opModeIsActive()) {
			hardware.slides.leftWristServo.setPosition(1);
			Util.sleep(1000);
			hardware.slides.leftWristServo.setPosition(0);
			Util.sleep(1000);
        }
    }
}
