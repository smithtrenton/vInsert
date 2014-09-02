package org.vinsert.api.util;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

/**
 * A static API for random and gaussian random number generation
 * @author Cheddy
 *
 */
public class RandomFunctions {

	/**
	 * Static API so cannot be instantiated
	 */
	private RandomFunctions() {
	}

	/**
	 * The random number generator - private so nothing can tamper with it
	 */
	private static Random random = new Random();

	/**
	 * Generates a random number between the given bounds
	 * 
	 * @param min
	 * @param max
	 * @return A random number between the bounds
	 */
	public static int random(int min, int max) {
		if (min > max) {
			int tmp = min;
			min = max;
			max = tmp;
		}
		return random.nextInt(max - min) + min;
	}

	/**
	 * Generates a random number with the specified maximum
	 * 
	 * @param max
	 * @return A random number with the specified maximum
	 */
	public static int random(int max) {
		return random.nextInt(max);
	}

	/**
	 * Generates a gaussian normal random number centred around <i>mean</i> with
	 * a standard deviation <i>standardDev</i>
	 * 
	 * @param mean
	 * @param standardDev
	 * @return The generated gaussian number
	 */
	public static double randomGaus(double mean, double standardDev) {
		Random r = new Random();
		return r.nextGaussian() * standardDev + mean;
	}

	/**
	 * Generates a gaussian normal random number between the lower and upper
	 * bounds centred around <i>mean</i> with a standard deviation
	 * <i>standardDev</i>
	 * 
	 * @param lower
	 * @param upper
	 * @param mean
	 * @param standardDev
	 * @return The generated gaussian number
	 */
	public static double randomGausInRange(double lower, double upper, double mean, double standardDev) {
		if (lower > upper) {
			double tmp = lower;
			lower = upper;
			upper = tmp;
		}
		double val = randomGaus(mean, standardDev);
		while (val < lower || val > upper) {
			val = randomGaus(mean, standardDev);
		}
		return val;
	}

	/**
	 * Generates a random click point within a rectangle
	 * 
	 * @param rect
	 * @return A random point in the rectangle
	 */
	public static Point randomRectanglePoint(Rectangle rect) {
		return new Point(random(rect.x, rect.x + rect.width), random(rect.y, rect.y + rect.height));
	}

	/**
	 * Generates a gaussian normal random click point within the rectangle using
	 * a standard deviation of 1/3 of the bounds
	 * 
	 * @param rect
	 * @return The generated gaussian point
	 */
	public static Point gRandomRectanglePoint(Rectangle rect) {
		return new Point((int) randomGausInRange(rect.x, rect.x + rect.width, rect.getCenterX(), rect.width / 3), (int) randomGausInRange(rect.y, rect.y + rect.height,
				rect.getCenterY(), rect.height / 3));
	}

}
