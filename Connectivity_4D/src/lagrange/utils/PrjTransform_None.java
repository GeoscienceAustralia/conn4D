package lagrange.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

/**
 * PrjTransform implementation that performs no transformation.
 *  
 * @author Johnathan Kool
 */

public class PrjTransform_None implements PrjTransform{
	
	/**
	 * Projects an array of doubles from one coordinate system into another.
	 * In this case, no transformation occurs. 
	 */
	@Override
	public double[] project(double[] coords){return coords;}

	/**
	 * Projects an x,y pair from one coordinate system into another.
	 * In this case, no transformation occurs. 
	 */
	@Override
	public double[] project(double x, double y){return new double[]{x,y};}

	/**
	 * Projects a Coordinate from one coordinate system into another.
	 * In this case, no transformation occurs. 
	 */
	@Override
	public Coordinate project(Coordinate c){return c;}

	/**
	 * Projects an array of Coordinates from one coordinate system into another.
	 * In this case, no transformation occurs. 
	 */
	@Override
	public Coordinate[] project(Coordinate[] ca){return ca;}
	
	/**
	 * Projects a LineSegment from one coordinate system into another.
	 * In this case, no transformation occurs. 
	 */
	@Override
	public LineSegment project(LineSegment ls){return ls;}
	
	/**
	 * Reverses the transformation from one coordinate system into another
	 * for an array of doubles.  In this case, no transformation occurs. 
	 */
	@Override
	public double[] inverse(double[] coords){return coords;}

	/**
	 * Reverses the transformation from one coordinate system into another
	 * for an x,y pair.  In this case, no transformation occurs. 
	 */
	@Override
	public double[] inverse(double x, double y){return new double[]{x,y};}

	/**
	 * Reverses the transformation from one coordinate system into another
	 * for a Coordinate.  In this case, no transformation occurs. 
	 */
	@Override
	public Coordinate inverse(Coordinate c){return c;}
	
	/**
	 * Reverses the transformation from one coordinate system into another
	 * for an array of Coordinates.  In this case, no transformation occurs. 
	 */
	@Override
	public Coordinate[] inverse(Coordinate[] ca){return ca;}
	
	/**
	 * Reverses the transformation from one coordinate system into another
	 * for a LineSegment.  In this case, no transformation occurs. 
	 */
	@Override
	public LineSegment inverse(LineSegment ls){return ls;}
}
