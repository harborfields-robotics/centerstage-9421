package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.List;


@TeleOp(name="Servo Testing OpMode", group="Testing OpMode")
public class ServoTestingOp extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private Hardware hardware;

    @Override
    public void runOpMode()
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        hardware = new Hardware(hardwareMap, telemetry);

        List<Servo> servos = hardwareMap.getAll(Servo.class);
        int selected = 0;
        boolean dpadUp = false, dpadDown = false, dpadLeft = false, dpadRight = false;

        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {
                if (!dpadUp) {
                    selected++;
                }
                dpadUp = true;
            } else
                dpadUp = false;
            if (gamepad1.dpad_down) {
                if (!dpadDown) {
                    selected--;
                }
                dpadDown = true;
            } else
                dpadDown = false;
            if (gamepad1.dpad_right) {
                if (!dpadRight) {
                }
                dpadRight = true;
            } else
                dpadRight = false;
            if (gamepad1.dpad_left) {
                if (!dpadLeft)
                    ;
                dpadLeft = true;
            } else
                dpadLeft = false;
            selected = Range.clip(selected, 0, servos.size() - 1);
            servos.get(selected).setPosition(
                    servos.get(selected).getPosition()
                    + Math.signum(gamepad1.right_stick_x)
                    * (0.1 + 0.1 * gamepad1.right_trigger)
                    * Hardware.deltaTime()
                    * Math.hypot(gamepad1.right_stick_x, gamepad1.right_stick_y)
            );
            if (gamepad1.circle) {
                servos.get(selected).getController().pwmDisable();
            } else if (gamepad1.triangle) {
                servos.forEach((Servo i) -> i.getController().pwmDisable());
            } else if (gamepad1.square) {
                hardware.intake.outtake();
            } else if (gamepad1.cross) {
                hardware.intake.intake();
            } else
                hardware.intake.stop();
            hardware.slides.setPower(-gamepad1.left_stick_y);
            telemetry.addData(hardware.getDeviceName(servos.get(selected)), servos.get(selected).getPosition());
            telemetry.addData("slides", hardware.slides.leftMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}