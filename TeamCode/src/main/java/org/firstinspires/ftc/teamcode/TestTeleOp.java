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

        hardware.drivetrain.encoders.forEach(Hardware::resetEncoder);

        while (opModeIsActive()) {
            telemetry.addData("left", hardware.drivetrain.encoderLeft.getCurrentPosition());
            telemetry.addData("back", hardware.drivetrain.encoderBack.getCurrentPosition());
            telemetry.addData("right", hardware.drivetrain.encoderRight.getCurrentPosition());
            telemetry.update();
        }
    }
}