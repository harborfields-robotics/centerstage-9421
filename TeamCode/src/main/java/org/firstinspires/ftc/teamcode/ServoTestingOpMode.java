package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


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

        Servo[] servos = {
            hardware.slides.leftWristServo,
            hardware.slides.rightWristServo,
            hardware.slides.leftElbowServo,
            hardware.slides.rightElbowServo
        };

        int selected = 2;

        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            if (gamepad1.dpad_up)
                selected++;
            else if (gamepad1.dpad_down)
                selected--;
            selected = Range.clip(selected, 0, servos.length - 1);
            if (gamepad1.dpad_right)
                servos[selected].setPosition(servos[selected].getPosition() + 0.05);
            if (gamepad1.dpad_left)
                servos[selected].setPosition(servos[selected].getPosition() - 0.05);
            telemetry.addData(hardware.getDeviceName(servos[selected]), servos[selected].getPosition());
            telemetry.update();
            Util.sleep(100);
        }
    }
}