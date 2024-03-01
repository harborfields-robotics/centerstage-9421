package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
@TeleOp
public class GMZeroMecanumDrive extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotorEx frontLeftMotor = hardwareMap.get(DcMotorEx.class, "fl-motor");
        DcMotorEx backLeftMotor = hardwareMap.get(DcMotorEx.class, "bl-motor");
        DcMotorEx frontRightMotor = hardwareMap.get(DcMotorEx.class, "fr-motor");
        DcMotorEx backRightMotor = hardwareMap.get(DcMotorEx.class, "br-motor");
        DcMotorEx rightSlidesMotor = hardwareMap.get(DcMotorEx.class, "right-slides-motor");
        DcMotorEx leftSlidesMotor = hardwareMap.get(DcMotorEx.class, "left-slides-motor");
        DcMotorEx winchMotor = hardwareMap.get(DcMotorEx.class, "winch-motor");

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftSlidesMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            // double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double denominator = 1/(0.5);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);
            rightSlidesMotor.setPower(gamepad2.left_stick_y);
            leftSlidesMotor.setPower(gamepad2.left_stick_y);
            winchMotor.setPower(gamepad2.right_stick_x);
        }
    }
}