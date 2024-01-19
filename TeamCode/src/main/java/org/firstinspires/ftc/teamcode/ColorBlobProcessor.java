package org.firstinspires.ftc.teamcode;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Mat;

public class ColorBlobProcessor implements VisionProcessor
{
    public void init(int width, int height, CameraCalibration calibration)
    {}
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext)
    {}
    public Object processFrame(Mat frame, long captureTimeNanos)
    {
        return null;
    }
}
