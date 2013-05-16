package au.gov.ga.conn4d.utils;

import com.vividsolutions.jts.geom.LineSegment;

public class GeometryMath {
	public static double[] mxb(LineSegment ls){
		double m=(ls.p1.y-ls.p0.y)/(ls.p1.x-ls.p0.x);
		double b = ls.p0.y-(m*ls.p0.x);
		return new double[]{m,b};
	}
	
	public static String toString(LineSegment ls) {
		return "LINESTRING(" + ls.p0.x + " " + ls.p0.y + " " + ls.p0.z + ", "
				+ ls.p1.x + " " + ls.p1.y + " " + ls.p1.z + ")";
	}
}
