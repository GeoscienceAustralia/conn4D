package lagrange.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

public class PrjTransform_None implements PrjTransform{
	@Override
	public double[] project(double[] coords){return coords;}

	@Override
	public double[] project(double x, double y){return new double[]{x,y};}

	@Override
	public Coordinate project(Coordinate c){return c;}
	
	@Override
	public Coordinate[] project(Coordinate[] ca){return ca;}
	
	@Override
	public LineSegment project(LineSegment ls){return ls;}
	
	@Override
	public double[] inverse(double[] coords){return coords;}

	@Override
	public double[] inverse(double x, double y){return new double[]{x,y};}

	@Override
	public Coordinate inverse(Coordinate c){return c;}
	
	@Override
	public Coordinate[] inverse(Coordinate[] ca){return ca;}
	
	@Override
	public LineSegment inverse(LineSegment ls){return ls;}
}
