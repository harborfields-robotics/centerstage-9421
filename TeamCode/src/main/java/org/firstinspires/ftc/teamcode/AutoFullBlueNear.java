package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Config
@Autonomous
public class AutoFullBlueNear extends LinearOpMode
{
    public static Hardware hardware;
    public static SpikeMark spikeMark;

    public TrajectoryBuilder builder()
    {
        return hardware.roadrunner.trajectoryBuilder(new Pose2d());
    }

    public void go(Trajectory trajectory)
    {
        hardware.roadrunner.followTrajectory(trajectory);
    }

    @Override
    public void runOpMode()
    {
        hardware = new Hardware(hardwareMap, telemetry);
        while ((spikeMark = hardware.doubleVision.getSpikeMark()) == null)
            Util.sleep(100);
        switch (spikeMark) {
            case LEFT:
                go(builder().forward(18).strafeLeft(12).build());
                hardware.intake.outtakeOneAuto();
                go(builder().forward(6).build());
                hardware.roadrunner.turn(Math.toRadians(90));
                break;
            case MIDDLE:
                go(builder().forward(28).build());
                hardware.intake.outtakeOneAuto();
                go(builder().back(28).build());
                hardware.roadrunner.turn(Math.toRadians(90));
                go(builder().forward(12).build());
                break;
            case RIGHT:
                go(builder().forward(18).strafeRight(12).build());
                hardware.intake.outtakeOneAuto();
                go(builder().forward(6).build());
                hardware.roadrunner.turn(Math.toRadians(90));
                go(builder().forward(24).build());
                break;
        }
    }
}