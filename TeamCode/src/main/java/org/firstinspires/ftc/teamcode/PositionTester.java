package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Position Testing OpMode", group="Testing OpMode")
public class PositionTester extends LinearOpMode
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
            if (gamepad1.right_trigger > 0.2 && gamepad1.circle)
                stop();
            else if (gamepad1.circle)
                hardware.slides.leftElbowServo.getController().pwmDisable();
            if (gamepad1.square) {
                hardware.slides.leftElbowServo.setPosition(.718);
                hardware.slides.rightElbowServo.setPosition(.230);
                //hardware.slides.leftWristServo.setPosition(.417);
                hardware.slides.rightWristServo.setPosition(.417);
            }
            hardware.slides.setPower(((gamepad1.dpad_up ? 1 : 0) - (gamepad1.dpad_down ? 1 : 0)) * 50 * Hardware.deltaTime());
            // telemetry.addData("slides", hardware.slides.motor.getCurrentPosition());
            telemetry.update();
        }
    }
}