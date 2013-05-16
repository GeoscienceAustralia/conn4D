package au.gov.ga.conn4d.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

public class PrjTransform_WGS2CEQD implements PrjTransform{

	private final static double R_EARTH = 6378137d;

	@Override
	public double[] project(double[] coords) {
		return function(coords);
	}

	@Override
	public double[] project(double x, double y) {
		return function(new double[] { x, y });
	}

	@Override
	public Coordinate project(Coordinate c) {
		if (c==null){return null;}
		double[] tmp = function(new double[] { c.x, c.y });
		return new Coordinate(tmp[0], tmp[1], c.z);
	}
	
	@Override
	public Coordinate[] project(Coordinate[] ca){
		if(ca==null){return null;}
		Coordinate[] out = new Coordinate[ca.length];
		for(int i = 0; i < ca.length; i++){
			out[i] = project(ca[i]);
		}
		return out;
	}
	
	@Override
	public LineSegment project(LineSegment ls){
		if(ls==null){return null;}
		return new LineSegment(project(ls.p0),project(ls.p1));	
	}
	
	@Override
	public double[] inverse(double[] coords) {
		return function_inv(coords);
	}

	@Override
	public double[] inverse(double x, double y) {
		return function_inv(new double[] { x, y });
	}

	@Override
	public Coordinate inverse(Coordinate c) {
		if(c==null){return null;}
		double[] tmp = function_inv(new double[] { c.x, c.y });
		return new Coordinate(tmp[0], tmp[1], c.z);
	}
	
	@Override
	public Coordinate[] inverse(Coordinate[] ca){
		if(ca==null){return null;}
		Coordinate[] out = new Coordinate[ca.length];
		for(int i = 0; i < ca.length; i++){
			out[i] = inverse(ca[i]);
		}
		return out;
	}

	
	@Override
	public LineSegment inverse(LineSegment ls){
		if(ls==null){return null;}
		return new LineSegment(inverse(ls.p0),inverse(ls.p1));	
	}
	private double[] function(double[] coords) {
		if(coords==null){return null;}
		double latitude_origin = Math.toRadians(0);
		double central_meridian = Math.toRadians(0);
		double longitude = Math.toRadians(coords[0]);
		double latitude = Math.toRadians(coords[1]);
		double[] out = new double[coords.length];
		out[1] = R_EARTH * latitude;
		out[0] = R_EARTH * (longitude - central_meridian)
				* Math.cos(latitude_origin);
		if (coords.length == 3) {
			out[2] = coords[2];
		}
		return out;
	}

	// Cylindrical Equidistant Meters to longitude and latitude

	private double[] function_inv(double[] coords) {
		if(coords==null){return null;}
		double latitude_origin = Math.toRadians(0);
		double central_meridian = Math.toRadians(0);
		double[] out = new double[coords.length];
		out[1] = Math.toDegrees(coords[1] / R_EARTH);
		out[0] = Math.toDegrees(central_meridian
				+ (coords[0] / (R_EARTH * Math.cos(latitude_origin))));
		if (coords.length == 3) {
			out[2] = coords[2];
		}
		return out;
	}
}
