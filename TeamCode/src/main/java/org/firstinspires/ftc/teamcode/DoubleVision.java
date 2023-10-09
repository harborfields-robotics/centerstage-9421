package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.matrices.*;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import android.util.Size;

import java.util.*;
import java.lang.*;

public class DoubleVision
{
	private AprilTagProcessor aprilTag;
	private TfodProcessor tfod;
	private VisionPortal visionPortal;
	private Hardware hardware;

	public static final String TFOD_MODEL_PATH = "/sdcard/FIRST/tflitemodels/CenterStage.tflite";
	public static final String TFOD_MODEL_LABELS[] = { "pixel" };
	public static final Size CAMERA_RESOLUTION = new Size(640, 480);

	public DoubleVision(Hardware hardware)
	{
		this.hardware = hardware;
		this.aprilTag = new AprilTagProcessor.Builder().build();
		this.tfod = new TfodProcessor.Builder()
			.setModelFileName(TFOD_MODEL_PATH)
			.setModelLabels(TFOD_MODEL_LABELS)
			.build();
		this.visionPortal = new VisionPortal.Builder()
			.setCamera(hardware.hardwareMap.get(WebcamName.class, Hardware.WEBCAM_NAME))
			.setCameraResolution(CAMERA_RESOLUTION)
			.addProcessors(tfod, aprilTag)
			.build();
	}

	public void putTelemetry()
	{
		telemetryAprilTag();
		telemetryTfod();
	}

    private void telemetryAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        hardware.telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                hardware.telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                hardware.telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                hardware.telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                hardware.telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                hardware.telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                hardware.telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }
    }

    private void telemetryTfod() {
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        hardware.telemetry.addData("# Objects Detected", currentRecognitions.size());
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            hardware.telemetry.addData(""," ");
            hardware.telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            hardware.telemetry.addData("- Position", "%.0f / %.0f", x, y);
            hardware.telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }
    }
}
