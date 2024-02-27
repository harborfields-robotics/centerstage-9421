package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.List;


@TeleOp(name="Motor Testing OpMode", group="Testing OpMode")
public class MotorTestingOp extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private Hardware hardware;

    @Override
    public void runOpMode()
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        hardware = new Hardware(hardwareMap, telemetry);

        List<DcMotorEx> motors = hardwareMap.getAll(DcMotorEx.class);
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
            selected = Range.clip(selected, 0, motors.size() - 1);
            motors.get(selected).setPower(
                    Math.signum(gamepad1.right_stick_x)
                    * (0.1 + 0.1 * gamepad1.right_trigger)
                    * Math.hypot(gamepad1.right_stick_x, gamepad1.right_stick_y)
                    * 5
            );
            if (gamepad1.circle) {
                Hardware.resetEncoder(motors.get(selected));
            } else if (gamepad1.triangle) {
                motors.forEach(Hardware::resetEncoder);
            }
            hardware.slides.setPower(-gamepad1.left_stick_y);
            telemetry.addData("device", hardware.getDeviceName(motors.get(selected)));
            telemetry.addData("position", motors.get(selected).getCurrentPosition());
            telemetry.addData("power", motors.get(selected).getPower());
            telemetry.addData("slides", hardware.slides.leftMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}