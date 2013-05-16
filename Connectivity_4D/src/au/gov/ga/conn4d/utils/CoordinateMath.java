package au.gov.ga.conn4d.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

/**
 * 
 * Functions for performing vector operations on the jts.Coordinate object
 * 
 * @author Johnathan Kool
 *
 */

public class CoordinateMath {
	
	public static final Coordinate NaN = new Coordinate(Double.NaN,Double.NaN,Double.NaN);

	/**
	 * Adds two coordinate locations
	 * 
	 * @param a - First coordinate/vector location
	 * @param b - Second coordinate/vector location
	 * @return
	 */
	
	public final static Coordinate add(Coordinate a, Coordinate b) {
		return new Coordinate(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	/**
	 * Returns the angle between two lines in 3D space.  In this case, the
	 * direction of the angle is relative to the sweep from the first arm
	 * to the second.  
	 * 
	 * @param p1 - the terminus of one angle arm
	 * @param vertex - the vertex of the angle
	 * @param p2 - the terminus of the second angle arm.
	 * @return
	 */
	
	public final static double angle3DSigned(Coordinate p1, Coordinate vertex, Coordinate p2){
		return angle3DSigned(p1,vertex,p2,p1);
	}
	
	/**
	 * Returns the angle between two lines in 3D space.  In this case, the
	 * direction of the angle is relative to the reference Coordinate.  
	 * 
	 * @param p1 - the terminus of one angle arm
	 * @param vertex - the vertex of the angle
	 * @param p2 - the terminus of the second angle arm.
	 * @param reference - the reference coordinate
	 * @return
	 */
	
	public final static double angle3DSigned(Coordinate p1, Coordinate vertex, Coordinate p2, Coordinate reference){
		Coordinate v1 = subtract(p1,vertex);
		Coordinate v2 = subtract(p2,vertex);
		Coordinate v3 = subtract(reference,vertex);
		Coordinate vn = cross(v1,v3);
		
		double denom = magnitude(v1)*magnitude(v2);
		double sina = magnitude(cross(v1,v2))/denom;
		double cosa = dot(v1,v2)/denom;
		
		double angle = Math.atan2(sina, cosa);
		double sign = dot(vn,cross(v1,v2));
		
		return sign < 0d ? -angle:angle;
	}

	/**
	 * Returns the average position of two Coordinates.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	
	public static final Coordinate average(Coordinate a, Coordinate b){
		return dilate(add(a,b),1d/2d);
	}
	
	/**
	 * Returns the average position (centroid) of an array of Coordinates.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	
	public static final Coordinate average(Coordinate[] coordinates){
		
		if(coordinates==null||coordinates.length==0){
			return NaN;
		}
		
		int n = coordinates.length;
		Coordinate c = new Coordinate(0,0,0);
		for(int i = 0; i < n; i++){
			c = add(c, coordinates[i]);
		}
		return dilate(c,1d/n);
	}
	
	/**
	 * Transforms a Coordinate from Cylindrical Equidistant Projection to
	 * Geographic (longitude/latitude).
	 * @param c - The Coordinate to be projected.
	 */
	
	public static final Coordinate ceqd2lonlat(Coordinate c){
		double[] coords = GeometryUtils.ceqd2lonlat(new double[]{c.x,c.y,c.z});
		return new Coordinate(coords[0],coords[1],coords[2]);
	}
	
	/**
	 * Returns the cross product of two coordinate positions/vectors
	 * 
	 * @param a - First coordinate/vector location
	 * @param b - Second coordinate/vector location
	 */
	
	public final static Coordinate cross(Coordinate a, Coordinate b) {
		return new Coordinate((a.y * b.z) - (b.y * a.z), (a.z * b.x)
				- (b.z * a.x), (a.x * b.y) - (b.x * a.y));
	}
	
	/**
	 * Returns the dilation/multiplication of a coordinate by a scale factor
	 * 
	 * @param c - Coordinate/vector location
	 * @param factor - Scale factor
	 * @return
	 */
	
	public final static Coordinate dilate(Coordinate c, double factor){
		return new Coordinate(c.x*factor, c.y*factor, c.z*factor);
	}
	
	/**
	 * Returns the dot product of two coordinate positions/vectors
	 * 
	 * @param a - First coordinate/vector location
	 * @param b - Second coordinate/vector location
	 */


	public final static double dot(Coordinate a, Coordinate b) {
		if (Double.isNaN(a.z) && Double.isNaN(b.z)) {
			return (a.x * b.x) + (a.y * b.y);
		} else {
			return (a.x * b.x) + (a.y * b.y) + (a.z * b.z);
		}
	}
	
	/**
	 * Returns the length between two Coordinates in 3D space.
	 * @param c1 - The first Coordinate
	 * @param c2 - The second Coordinate
	 */
	
	public static final double length3D(Coordinate c1, Coordinate c2){
		return magnitude(subtract(c2,c1));
	}
	
	/**
	 * Transforms a Coordinate from Geographic projection (longitude/latitude)
	 * to Cylindrical Equidistant Projection.
	 * @param c - The Coordinate to be projected.
	 */
	
	public static final Coordinate lonlat2ceqd(Coordinate c){
		double[] coords = GeometryUtils.lonlat2ceqd(new double[]{c.x,c.y,c.z});
		return new Coordinate(coords[0],coords[1],coords[2]);
	}
	
	/**
	 * Returns the distance from the origin/magnitude of the coordinate/vector.
	 * @param c - Coordinate/vector location.
	 */
	
	public final static double magnitude(Coordinate c){
		return Math.sqrt(dot(c,c));
	}
	
	/**
	 * Returns an array of midpoints from an array of Coordinate values.
	 * @param ca - an array of Coordinate values.
	 */
	
	public static final Coordinate[] midpoints(Coordinate[] ca){
		if(ca.length < 2){throw new IllegalArgumentException("Coordinate array must contain 2 more more points");}
		Coordinate[] out = new Coordinate[ca.length-1];
		for(int i = 0; i < out.length; i++){
			out[i]= average(ca[i],ca[i+1]);
		}
		return out;
	}
	
	/**
	 * Returns the normalized cross product of two coordinate positions/vectors
	 * 
	 * @param a - First coordinate/vector location.
	 * @param b - Second coordinate/vector location.
	 */
	
	public final static Coordinate ncross(Coordinate a, Coordinate b) {
		
		return normalize(cross(a,b));
	}
	
	/**
	 * Returns the negative of the coordinate position
	 * 
	 * @param a - First coordinate/vector location.
	 * @param b - Second coordinate/vector location.
	 */
	
	public final static Coordinate negative(Coordinate c){
		return new Coordinate(-c.x,-c.y,-c.z);
	}
	
	/**
	 * Reduces a LineSegment by a given distance from the start towards the end
	 * 
	 * @param ls - the LineSegment requiring reduction
	 * @param distance - the distance the LineSegment should be reduced
	 */
	
	public final static LineSegment nibble(LineSegment ls, double distance){
	    Coordinate coord = new Coordinate();
	    double segmentLengthFraction = distance/magnitude(subtract(ls.p0,ls.p1));
	    coord.x = ls.p0.x + segmentLengthFraction * (ls.p1.x - ls.p0.x);
	    coord.y = ls.p0.y + segmentLengthFraction * (ls.p1.y - ls.p0.y);
	    coord.z = ls.p0.z + segmentLengthFraction * (ls.p1.z - ls.p0.z);
	    ls.p0=coord;
	    return ls;
	}
	
	/**
	 * Returns the normal (3D perpendicular) to a set of vertices.
	 * @param vertices
	 */
	
	public final static Coordinate normal(Coordinate[] vertices){
		Coordinate v0 = vertices[0];
		Coordinate v1 = vertices[1];
		Coordinate v2 = vertices[vertices.length-1];
		Coordinate u = CoordinateMath.subtract(v1, v0);
		Coordinate v = CoordinateMath.subtract(v2, v0);
		return CoordinateMath.cross(u, v);
	}
	
	/**
	 * Returns the normal (3D perpendicular) to a set of vertices such that
	 * the result always faces upwards in the z-direction.
	 * @param vertices
	 */
	
	public final static Coordinate normal_zplus(Coordinate[] vertices){
		Coordinate tmp = normal(vertices);
		if(tmp.z<0){return negative(tmp);}
		return tmp;
	}
	
	/**
	 * Normalizes the coordinate values to have a unit magnitude (length equal to 1)
	 * 
	 * @param c
	 * @return
	 */
	
	public final static Coordinate normalize(Coordinate c){
		double n = magnitude(c);
		return new Coordinate(c.x/n,c.y/n,c.z/n);
	}
	
	/**
	 * Finds the intersection point of a ray with a plane
	 * 
	 * @param ls
	 * @param vertices
	 * @return
	 */
	
	public static final Coordinate planeIntersection(LineSegment ls,Coordinate[] vertices) {

		Coordinate v0 = vertices[0];
		Coordinate v1 = vertices[1];
		Coordinate v2 = vertices[vertices.length-1];
		Coordinate p0 = ls.p0;
		Coordinate p1 = ls.p1;
		Coordinate u = subtract(v1, v0);
		Coordinate v = subtract(v2, v0);
		Coordinate norm = cross(u, v);

		if (norm.equals3D(new Coordinate())) {
			throw new IllegalArgumentException("Intersection polygon is degenerate.");
		}

		Coordinate dir = subtract(p1, p0);
		Coordinate w0 = subtract(p0, v0);

		double a = -dot(norm, w0);
		double b = dot(norm, dir);

		// Coincident with the plane
		
		if (Math.abs(b) < 1E-6) {
			return NaN;
		}
		
		// Heading in the opposite direction

		double r = a / b;
		if (r < 0d) {
			return NaN;
		}

		return add(p0, dilate(dir, r));		
	}
	
	/**
	 * From: http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
	 * Copyright (c) 1970-2003, Wm. Randolph Franklin
	 * 
	 * @param point
	 * @param vertices
	 * @return
	 */
	
	public static final boolean pointInPoly2D(Coordinate point, Coordinate[] vertices)
	{
	  int nvert = vertices.length;
	  boolean c = false;
	  for (int i = 0, j = nvert-1; i < nvert; j = i++) {
	    if (((vertices[i].y>point.y) != (vertices[j].y>point.y)) && (point.x < (vertices[j].x-vertices[i].x) * (point.y-vertices[i].y) / (vertices[j].y-vertices[i].y) + vertices[i].x))
	       c = !c;
	  }
	  return c;
	}
	
	/**
	 * Modified from: http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
	 * Copyright (c) 1970-2003, Wm. Randolph Franklin
	 * 
	 * @param point
	 * @param vertices
	 * @return
	 */
	
	public static final boolean pointInPoly3D(Coordinate point, Coordinate[] vertices)
	{
	  int nvert = vertices.length;
	  boolean c1 = false,c2 = false,c3 = false;
	  for (int i = 0, j = nvert-1; i < nvert; j = i++) {
	    if (((vertices[i].y>point.y) != (vertices[j].y>point.y)) && (point.x < (vertices[j].x-vertices[i].x) * (point.y-vertices[i].y) / (vertices[j].y-vertices[i].y) + vertices[i].x)){
	       c1 = !c1;}
	    if (((vertices[i].z>point.z) != (vertices[j].z>point.z)) && (point.x < (vertices[j].x-vertices[i].x) * (point.z-vertices[i].z) / (vertices[j].z-vertices[i].z) + vertices[i].x))
		   c2 = !c2;
	    if (((vertices[i].y>point.y) != (vertices[j].y>point.y)) && (point.z < (vertices[j].z-vertices[i].z) * (point.y-vertices[i].y) / (vertices[j].y-vertices[i].y) + vertices[i].z))
		   c3 = !c3;
	  }
	  return c1||c2||c3;
	}
	
	public final static Coordinate reflect(Coordinate point, Coordinate fulcrum, Coordinate norm){
		Coordinate dir = subtract(point,fulcrum);
		Coordinate nnorm = CoordinateMath.normalize(norm);
		return add(subtract(dir, CoordinateMath.dilate(nnorm, 2 * CoordinateMath.dot(dir, nnorm))),fulcrum);	
	}
	
	//public final static Coordinate angleSplit(Coordinate p0, Coordinate vertex, Coordinate p1, Coordinate reference){
	//	double angle = angle3DSigned(p0,vertex,p1,reference);
	//	Coordinate norm = CoordinateMath.normal(new Coordinate[] { p1, vertex, p0 });
	//	
	//	Coordinate newaxis = CoordinateMath.add(norm, vertex);
	//	return CoordinateMath.rotate3D(p0, newaxis, vertex, angle/2);
	//}
	
	/**
	 * From http://inside.mines.edu/~gmurray/ArbitraryAxisRotation/
	 * 
	 * @param point
	 * @param axis
	 * @param theta
	 * @return
	 */
	
	public final static Coordinate rotate3D(Coordinate point, Coordinate axis, double theta){
		double x = point.x;
		double y = point.y;
		double z = point.z;
		double u = axis.x;
		double v = axis.y;
		double w = axis.z;
		double st = Math.sin(theta);
		double ct = Math.cos(theta);
		double u2v2w2 = u*u+v*v+w*w;
		double ru2v2w2 = Math.sqrt(u2v2w2);
		double out_x = (u*(u*x+v*y+w*z)*(1-ct)+u2v2w2*x*ct+ru2v2w2*(-w*y+v*z)*st)/u2v2w2;
		double out_y = (v*(u*x+v*y+w*z)*(1-ct)+u2v2w2*y*ct+ru2v2w2*(w*x-u*z)*st)/u2v2w2;
		double out_z = (w*(u*x+v*y+w*z)*(1-ct)+u2v2w2*z*ct+ru2v2w2*(-v*x+u*y)*st)/u2v2w2;
		return new Coordinate(out_x, out_y, out_z);
	}
	
	public final static Coordinate rotate3Dn(Coordinate point, Coordinate axis, double theta){
		double x = point.x;
		double y = point.y;
		double z = point.z;
		double u = axis.x;
		double v = axis.y;
		double w = axis.z;
		double st = Math.sin(theta);
		double ct = Math.cos(theta);
		double out_x = u*(u*x+v*y+w*z)*(1-ct)+x*ct+(-w*y+v*z)*st;
		double out_y = v*(u*x+v*y+w*z)*(1-ct)+y*ct+(w*x-u*z)*st;
		double out_z = w*(u*x+v*y+w*z)*(1-ct)+z*ct+(-v*x+u*y)*st;
		return new Coordinate(out_x, out_y, out_z);
	}
	
	public final static Coordinate rotate3D(Coordinate point, Coordinate axis, Coordinate reference, double theta){
		Coordinate t1 = subtract(point,reference);
		Coordinate t2 = subtract(axis,reference);
		Coordinate np = rotate3D(t1,t2,theta);
		return add(np,reference);
	}
	
	public final static Coordinate rotate3Dn(Coordinate point, Coordinate axis, Coordinate reference, double theta){
		Coordinate t1 = subtract(point,reference);
		Coordinate t2 = subtract(axis,reference);
		Coordinate np = rotate3Dn(t1,t2,theta);
		return add(np,reference);
	}
	
	public final static Coordinate signum(Coordinate c){
		return new Coordinate(Math.signum(c.x),Math.signum(c.y),Math.signum(c.z));
	}
	
	/**
	 * Subtracts two coordinate locations
	 * 
	 * @param a - First coordinate/vector location
	 * @param b - Second coordinate/vector location
	 * @return
	 */
	
	public final static Coordinate subtract(Coordinate a, Coordinate b) {
		return new Coordinate(a.x - b.x, a.y - b.y, a.z - b.z);
	}
}
