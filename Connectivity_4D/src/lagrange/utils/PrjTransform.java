package lagrange.utils;

import com.vividsolutions.jts.geom.Coordinate;

public interface PrjTransform {
	public double[] project(double[] coords);

	public double[] project(double x, double y);

	public Coordinate project(Coordinate c);
	
	public Coordinate[] project(Coordinate[] ca);
	
	public double[] inverse(double[] coords);

	public double[] inverse(double x, double y);

	public Coordinate inverse(Coordinate c);
	
	public Coordinate[] inverse(Coordinate[] ca);
}
