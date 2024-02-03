package org.firstinspires.ftc.teamcode;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ColorBlobProcessor implements VisionProcessor
{
    Alliance alliance;
    Hardware hardware;

    public ColorBlobProcessor(Hardware hardware, Alliance alliance) {
        this.hardware = hardware;
        this.alliance = alliance;
    }

    public void init(int width, int height, CameraCalibration calibration) {
    }

    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
    }

    public Object processFrame(Mat frame, long captureTimeNanos) {
        List<Mat> channels = new ArrayList<>();
        Core.split(frame, channels);
        for (int i = 0; i < channels.size(); i++)
            Imgcodecs.imwrite(String.format("/sdcard/FIRST/emily-img-%d.png", i), channels.get(i));
        return null;
    }
}