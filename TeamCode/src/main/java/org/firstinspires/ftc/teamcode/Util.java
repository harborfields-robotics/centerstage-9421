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
}