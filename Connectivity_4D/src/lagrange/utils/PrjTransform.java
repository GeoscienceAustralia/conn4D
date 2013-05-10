package lagrange.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

public interface PrjTransform {

	public double[] project(double[] coords);

	public double[] project(double x, double y);

	public Coordinate project(Coordinate c);
	
	public Coordinate[] project(Coordinate[] ca);
	
	public LineSegment project(LineSegment ls);
	
	public double[] inverse(double[] coords);

	public double[] inverse(double x, double y);

	public Coordinate inverse(Coordinate c);
	
	public Coordinate[] inverse(Coordinate[] ca);
	
	public LineSegment inverse(LineSegment ls);
}
