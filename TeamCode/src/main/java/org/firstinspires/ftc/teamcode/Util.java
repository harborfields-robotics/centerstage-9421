package org.firstinspires.ftc.teamcode;
import java.lang.Math;

/**
  * A utility class containing various methods that don't fit anywhere else.
  */
public final class Util
{
	private Util() {}

	/**
	  * Returns whether two doubles are within epsilon of each other.
	  * This allows one to say two doubles are equal under a certain precision.
	  * @param x the first double to compare
	  * @param y the second double to compare
	  * @param epsilon the precision of the comparison
	  * @returns whether |x - y| < epsilon
	  */
	public static boolean doublesAreEqual(double x, double y, double epsilon)
	{
		return Math.abs(x - y) < epsilon;
	}

	/**
	  * Returns the value of a controller input, excluding a deadzone of a certain size.
	  * To be more specific, the returns zero if |value| < size, otherwise 
	  * @param value the value of the controller input, a value in the range [-1, 1]
	  * @param size the size of the zone to exclude, a value in the range 
	  * @return a value in the range [-1, 1]
	  */
	public static double controllerValueWithDeadzone(double value, double size)
	{
		final double EPSILON = 0.001;
		if (doublesAreEqual(Math.abs(value), size, EPSILON)
			|| doublesAreEqual(size, 1, EPSILON))
			return 0;
		return (value - size * Math.signum(value)) / (1 - size);
	}

	/**
	  * Sleeps for the specified length of time. If an InterruptedException occurs, no error is thrown.
	  * @param millis the length of the delay in milliseconds
	  * @return true if the thread was not interrupted, false otherwise
	  */
	public static boolean sleep(long millis)
	{
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	public static double lerp(double t, double a, double b)
	{
		return a + t * (b - a);
	}

	public static double inverseLerp(double x, double a, double b)
	{
		return (x - a) / (b - a);
	}

	public static double remap(double x, double a0, double b0, double a1, double b1)
	{
		return lerp(inverseLerp(x, a0, b0), a1, b1);
	}
}
