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

        hardware.loop();
        while (opModeIsActive()) {
            hardware.slides.setElbowPosition(
                    hardware.slides.getElbowPosition()
                    + Math.signum(gamepad1.right_stick_x)
                    * Math.hypot(gamepad1.right_stick_x, gamepad1.right_stick_y)
                    * Hardware.deltaTime()
                    * 0.1);

            hardware.slides.setWristPosition(
                    hardware.slides.getWristPosition()
                            + Math.signum(gamepad1.left_stick_x)
                            * Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y)
                            * Hardware.deltaTime()
                            * 0.1);

            telemetry.addData("elbow", hardware.slides.getElbowPosition());
            telemetry.addData("left elbow", hardware.slides.leftElbowServo.getPosition());
            telemetry.addData("right elbow", hardware.slides.rightElbowServo.getPosition());
            telemetry.addData("wrist", hardware.slides.getWristPosition());
            telemetry.addData("left wrist", hardware.slides.leftWristServo.getPosition());
            telemetry.addData("right wrist", hardware.slides.rightWristServo.getPosition());
            telemetry.addData("pdelta/.1",
                    + Math.signum(gamepad1.right_stick_x)
                    * Math.hypot(gamepad1.right_stick_x, gamepad1.right_stick_y));
            telemetry.addData("deltaTime", Hardware.deltaTime());
            telemetry.update();
            hardware.loop();
            Util.sleep(10);
        }
    }
}
