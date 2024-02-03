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
                hardware.slides.setElbowPosition(hardware.slides.getElbowPosition()
                + Math.hypot(gamepad1.right_stick_x, gamepad1.right_stick_y) * 0.1 * hardware.deltaTime() * Math.signum(gamepad1.right_stick_x));
            if (gamepad1.left_trigger > 0.2)
                hardware.slides.setWristPosition(hardware.slides.getWristPosition()
                + Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y) * 0.1 * hardware.deltaTime() * Math.signum(gamepad1.left_stick_x));
            hardware.slides.setPower((gamepad1.dpad_up ? 1 : 0) - (gamepad1.dpad_down ? 1 : 0));
            hardwareMap
                    .getAll(Servo.class)
                    .forEach((Servo i) -> telemetry.addData(hardware.getDeviceName(i), i.getPosition()));
            telemetry.addData("slides", hardware.slides.motor.getCurrentPosition());
            telemetry.update();
        }
    }
}