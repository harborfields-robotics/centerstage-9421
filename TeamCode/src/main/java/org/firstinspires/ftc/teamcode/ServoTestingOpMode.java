package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.List;


@TeleOp(name="Servo Testing OpMode", group="Testing OpMode")
public class ServoTestingOpMode extends LinearOpMode
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

        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {
                servos.get(selected).getController().pwmDisable();
                selected++;
                Util.sleep(500);
            } else if (gamepad1.dpad_down) {
                servos.get(selected).getController().pwmDisable();
                selected--;
                Util.sleep(500);
            }
            selected = Range.clip(selected, 0, servos.size() - 1);
            if (gamepad1.dpad_right) {
                servos.get(selected).setPosition(servos.get(selected).getPosition() + 0.05 * Math.pow(1 - gamepad1.left_trigger, 2));
                Util.sleep(500);
            } else if (gamepad1.dpad_left) {
                servos.get(selected).setPosition(servos.get(selected).getPosition() - 0.05 * Math.pow(1 - gamepad1.left_trigger, 2));
                Util.sleep(500);
            } else if (gamepad1.right_trigger > 0.1)
                servos.get(selected).setPosition(Math.hypot(gamepad1.right_stick_x, gamepad1.right_stick_y));
            else if (gamepad1.circle) {
                servos.get(selected).getController().pwmDisable();
                Util.sleep(500);
            } else if (gamepad1.triangle) {
                servos.forEach((Servo i) -> i.getController().pwmDisable());
                Util.sleep(500);
            } else if (gamepad1.square) {
                hardware.intake.outtake();
                Util.sleep(500);
            } else if (gamepad1.cross) {
                hardware.intake.intake();
                Util.sleep(500);
            }
            Util.sleep(500);
            hardware.slides.setPower(gamepad1.left_stick_y);
            telemetry.addData(hardware.getDeviceName(servos.get(selected)), servos.get(selected).getPosition());
            telemetry.addData("slides", hardware.slides.motor.getCurrentPosition());
            telemetry.update();
        }
    }
}