package org.firstinspires.ftc.teamcode;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import com.acmerobotics.dashboard.config.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Config
public class ColorBlobProcessor implements VisionProcessor
{
    private Alliance alliance;
    private Hardware hardware;
    private SpikeMark spikeMark = null;

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
    }

	public List<Mat> split(Mat frame)
	{
        return null;
	}

    public Object processFrame(Mat frame, long captureTimeNanos)
	{
        List<Mat> channels = new ArrayList<>();
		Mat twotone = new Mat();
        Core.split(frame, channels);
		if (alliance == Alliance.RED)
			Imgproc.threshold(channels.get(0), twotone, /* thresh */ 0.0, /* maxval */ 1.0, /* type */ Imgproc.THRESH_BINARY);
		else
			Imgproc.threshold(channels.get(2), twotone, /* thresh */ 0.0, /* maxval */ 1.0, /* type */ Imgproc.THRESH_BINARY);
        Mat left = twotone.submat(0, twotone.rows()/3, 0, twotone.cols());
        Mat middle = twotone.submat(twotone.rows() / 3, twotone.rows() * 2/3, 0, twotone.cols());
        Mat right = twotone.submat(twotone.rows() * 2/3, twotone.rows(), 0, twotone.cols());
        double[] means = Stream.of(left, middle, right)
                .map(Core::mean)
                .map(Util::scalarAbs)
                .mapToDouble(x -> x)
                .toArray();
        double max = Double.NEGATIVE_INFINITY;
        int idx = -1;
        for (int i = 0; i < means.length; i++) {
            if (means[i] > max) {
                idx = i;
                max = means[i];
            }
        }
        spikeMark = new SpikeMark[]{SpikeMark.LEFT, SpikeMark.MIDDLE, SpikeMark.RIGHT}[idx];
        return null;
    }

    public SpikeMark getSpikeMark()
    {
        return spikeMark;
    }
}