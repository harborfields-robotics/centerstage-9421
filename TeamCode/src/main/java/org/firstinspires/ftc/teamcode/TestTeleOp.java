package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Testing OpMode", group="Testing OpMode")
public class TestTeleOp extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private Hardware hardware;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        hardware = new Hardware(hardwareMap, telemetry);

        waitForStart();
        runtime.reset();
        boolean started = false;

        while (opModeIsActive()) {
            hardware.slides.setPower(gamepad1.right_stick_y);
            if (gamepad1.circle && !started) {
                started = true;
                hardware.slides.wristServo.setPosition(0);
                hardware.slides.elbowServo.setPosition(0);
            }
            if (started) {
                if (gamepad1.dpad_up)
                    hardware.slides.wristServo.setPosition(hardware.slides.wristServo.getPosition() + 0.005);
                if (gamepad1.dpad_down)
                    hardware.slides.wristServo.setPosition(hardware.slides.wristServo.getPosition() - 0.005);
                if (gamepad1.dpad_left)
                    hardware.slides.elbowServo.setPosition(hardware.slides.elbowServo.getPosition() + 0.005);
                if (gamepad1.dpad_right)
                    hardware.slides.elbowServo.setPosition(hardware.slides.elbowServo.getPosition() - 0.005);
            }
            telemetry.addData("wrist", hardware.slides.wristServo.getPosition());
            telemetry.addData("elbow", hardware.slides.elbowServo.getPosition());
            telemetry.addData("slides", hardware.slides.motor.getCurrentPosition());
            telemetry.update();
        }
    }
}