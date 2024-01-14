package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Auto", group="Linear OpMode")
public class Auto extends LinearOpMode
{
    Hardware hardware;
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        hardware = new Hardware(hardwareMap, telemetry);
        motorFR = hardwareMap.dcMotor.get("fr-motor");
        motorFL = hardwareMap.dcMotor.get("fl-motor");
        motorBR = hardwareMap.dcMotor.get("br-motor");
        motorBL = hardwareMap.dcMotor.get("bl-motor");
        //waitForStart();
        while (opModeIsActive()) {
            DriveRight(1);
            sleep(2000);
            Stop();
        }
    }
        public void DriveRight(double power)
        {
            motorFR.setPower(-power);
            motorFL.setPower(power);
            motorBR.setPower(power);
            motorBL.setPower(-power);
        }

        public void Stop()
        {
            DriveRight(0);
        }

}