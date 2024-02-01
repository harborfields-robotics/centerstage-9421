package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
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

        while (opModeIsActive()) {
            if (gamepad1.right_trigger > 0.2)
                hardware.slides.setElbowPosition(Math.abs(gamepad1.right_stick_x));
            if (gamepad1.left_trigger > 0.2)
                hardware.slides.setWristPosition(Math.abs(gamepad1.left_stick_x));
            hardwareMap
                    .getAll(Servo.class)
                    .forEach((Servo i) -> telemetry.addData(hardware.getDeviceName(i), i.getPosition()));
            telemetry.update();
        }
    }
}