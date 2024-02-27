package org.firstinspires.ftc.teamcode;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import java.util.ArrayList;
import java.util.List;

@Config
public class ColorBlobProcessor implements VisionProcessor
{
    private Alliance alliance;
    private Hardware hardware;
    public static double TR = 128, TB = 12128, MR = 255, MB = 255;
    private Mat processed;

    public ColorBlobProcessor(Hardware hardware, Alliance alliance)
	{
        this.hardware = hardware;
        this.alliance = alliance;
    }

    public void init(int width, int height, CameraCalibration calibration)
	{
    }

    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext)
	{
        Util.canvasDrawMat(canvas, processed);
    }

	public List<Mat> split(Mat frame)
	{
        return null;
	}

    public Object processFrame(Mat frame, long captureTimeNanos)
	{
        List<Mat> channels = new ArrayList<>();
		Mat twotone = new Mat(),
			left = new Mat(),
			right = new Mat(),
			middle = new Mat();
        Core.split(frame, channels);
		if (alliance == Alliance.RED)
			Imgproc.threshold(channels.get(0), twotone, /* thresh */ TR, /* maxval */ MR, /* type */ Imgproc.THRESH_BINARY);
		else
			Imgproc.threshold(channels.get(2), twotone, /* thresh */ TB, /* maxval */ MB, /* type */ Imgproc.THRESH_BINARY);
        processed = twotone;
        return null;
    }
}