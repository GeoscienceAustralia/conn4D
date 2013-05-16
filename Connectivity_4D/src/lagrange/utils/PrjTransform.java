package lagrange.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

/**
 * Projection Transformation interface.
 *  
 * @author Johnathan Kool
 */

public interface PrjTransform {

	/**
	 * Projects an array of doubles from one coordinate system into another.
	 */
	public double[] project(double[] coords);

	/**
	 * Projects an x,y pair from one coordinate system into another. 
	 */
	public double[] project(double x, double y);

	/**
	 * Projects a Coordinate from one coordinate system into another. 
	 */
	public Coordinate project(Coordinate c);
	
	/**
	 * Projects a Coordinate array from one coordinate system into another. 
	 */
	public Coordinate[] project(Coordinate[] ca);
	
	/**
	 * Projects a LineSegment from one coordinate system into another. 
	 */
	public LineSegment project(LineSegment ls);
	
	/**
	 * Reverses the transformation from one coordinate system into another
	 * for an array of doubles. 
	 */
	public double[] inverse(double[] coords);

	/**
	 * Reverses the transformation from one coordinate system into another
	 * for an array of doubles. 
	 */
	public double[] inverse(double x, double y);

	/**
	 * Reverses the transformation from one coordinate system into another
	 * for a Coordinate. 
	 */
	public Coordinate inverse(Coordinate c);
	
	/**
	 * Reverses the transformation from one coordinate system into another
	 * for a Coordinate array. 
	 */
	public Coordinate[] inverse(Coordinate[] ca);
	
	/**
	 * Reverses the transformation from one coordinate system into another
	 * for a LineSegment. 
	 */
	public LineSegment inverse(LineSegment ls);
}
