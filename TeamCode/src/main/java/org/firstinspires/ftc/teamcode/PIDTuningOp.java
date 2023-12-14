package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

@TeleOp(name="PID Tuning", group="Linear OpMode")
public class PIDTuningOp extends LinearOpMode
{
    private Hardware hardware;
    @Override
    public void runOpMode() throws InterruptedException
    {
        hardware = new Hardware(hardwareMap, telemetry);

        String names[] = hardwareMap
                .getAllNames(DcMotor.class)
                .toArray(new String[0]);
        DcMotor motor;
        PIDConstants constants;
        PIDController controller;
        boolean inMenu = true;
        int selectedIndex = 0;

        waitForStart();

        while (opModeIsActive()) {
            if (inMenu) {
                for (int i = 0; i < names.length; i++)
                    telemetry.addLine(String.format("[%02d]%s %s", i + 1, i == selectedIndex ? " *" : "", names[i]));

                if (gamepad1.dpad_down)
                    selectedIndex++;
                if (gamepad1.dpad_up)
                    selectedIndex--;
                selectedIndex = Range.clip(selectedIndex, 0, names.length - 1);

                if (gamepad1.x) {
                    motor = hardwareMap.get(DcMotor.class, names[selectedIndex]);
                    constants = hardware.lookupPIDConstants(names[selectedIndex]);
                    controller = new PIDController(motor, constants);
                    inMenu = false;
                }
            } else {

            }
        }
    }
 }
