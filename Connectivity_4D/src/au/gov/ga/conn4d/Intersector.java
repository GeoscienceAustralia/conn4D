package au.gov.ga.conn4d;

/**
 * Performs intersection operations for coordinates against a spatial layer.
 * 
 * @author Johnathan Kool
 * 
 */

public interface Intersector {
	public final long NO_INTERSECTION = Long.MIN_VALUE;

	/**
	 * Retrieves a long value representing the feature intersecting the point
	 * given by the x and y coordinate values. If the coordinates do not
	 * intersect an object, NO_INTERSECTION is returned as a value.
	 * 
	 * @param x - x position
	 * @param y - y position
	 */

	public long intersect(double x, double y);

	/**
	 * Retrieves a long value representing the feature intersecting the line
	 * given by the x and y coordinate values. If the coordinates do not
	 * intersect an object, NO_INTERSECTION is returned as a value.
	 * 
	 * @param x1 - initial x position
	 * @param y1 - initial y position
	 * @param x2 - end x position
	 * @param y2 - end y position
	 */

	public long intersect(double x1, double y1, double x2, double y2);

	/**
	 * Identifies whether the given coordinates intersect any features in the
	 * spatial layer.
	 * 
	 * @param x - x position
	 * @param y - y position
	 */

	public boolean intersects(double x, double y);
}
