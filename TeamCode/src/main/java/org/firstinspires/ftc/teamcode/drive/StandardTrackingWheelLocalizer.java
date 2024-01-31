package org.firstinspires.ftc.teamcode.drive;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.util.Encoder;
import org.firstinspires.ftc.teamcode.Hardware;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
@Config
public class StandardTrackingWheelLocalizer extends ThreeTrackingWheelLocalizer {
    public static double WHEEL_RADIUS = (4 + 5/16.0) / (2 * Math.PI); // in
    // trial 1 L / trial 1 R; trial 2 L / trial 2 R
    // -135851 / 136857; -135999 / 137180
    private static final double TICKS_IN_3_TILES = (135851 + 136857 + 135999 + 137180) / 4.0;
    public static double TICKS_PER_REV = (TICKS_IN_3_TILES / (12 * 2 * 3)) * (2 * Math.PI * WHEEL_RADIUS);
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public static double LATERAL_DISTANCE = 11 + 5/16.0; // in; distance between the left and right wheels
    public static double FORWARD_OFFSET = 7.5; // in; offset of the lateral wheel

	public static double X_MULTIPLIER = 1;
	public static double Y_MULTIPLIER = 1;

    public Encoder leftEncoder, rightEncoder, frontEncoder;

    private List<Integer> lastEncPositions, lastEncVels;

    public StandardTrackingWheelLocalizer(HardwareMap hardwareMap, List<Integer> lastTrackingEncPositions, List<Integer> lastTrackingEncVels) {
        super(Arrays.asList(
                new Pose2d(0, LATERAL_DISTANCE / 2, 0), // left
                new Pose2d(0, -LATERAL_DISTANCE / 2, 0), // right
                new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
        ));

        lastEncPositions = lastTrackingEncPositions;
        lastEncVels = lastTrackingEncVels;

        DcMotorEx tmpMotor;
        tmpMotor = hardwareMap.get(DcMotorEx.class, Hardware.LEFT_ENCODER_NAME);
        tmpMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Hardware.leftEncoder = leftEncoder = new Encoder(tmpMotor);
        tmpMotor = hardwareMap.get(DcMotorEx.class, Hardware.BACK_ENCODER_NAME);
        tmpMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Hardware.backEncoder = frontEncoder = new Encoder(tmpMotor);
        tmpMotor = hardwareMap.get(DcMotorEx.class, Hardware.RIGHT_ENCODER_NAME);
        tmpMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Hardware.rightEncoder = rightEncoder = new Encoder(tmpMotor);
        tmpMotor = null;

        // TODO: reverse any encoders using Encoder.setDirection(Encoder.Direction.REVERSE)
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        int leftPos = leftEncoder.getCurrentPosition();
        int rightPos = rightEncoder.getCurrentPosition();
        int frontPos = frontEncoder.getCurrentPosition();

        lastEncPositions.clear();
        lastEncPositions.add(leftPos);
        lastEncPositions.add(rightPos);
        lastEncPositions.add(frontPos);

        return Arrays.asList(
                encoderTicksToInches(leftPos) * X_MULTIPLIER,
                encoderTicksToInches(rightPos) * X_MULTIPLIER,
                encoderTicksToInches(frontPos) * Y_MULTIPLIER
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        int leftVel = (int) leftEncoder.getCorrectedVelocity();
        int rightVel = (int) rightEncoder.getCorrectedVelocity();
        int frontVel = (int) frontEncoder.getCorrectedVelocity();

        lastEncVels.clear();
        lastEncVels.add(leftVel);
        lastEncVels.add(rightVel);
        lastEncVels.add(frontVel);

        return Arrays.asList(
                encoderTicksToInches(leftVel) * X_MULTIPLIER,
                encoderTicksToInches(rightVel) * X_MULTIPLIER,
                encoderTicksToInches(frontVel) * Y_MULTIPLIER
        );
    }
}
