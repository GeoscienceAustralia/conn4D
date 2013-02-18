package lagrange.utils;

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
	 * Subtracts two coordinate locations
	 * 
	 * @param a - First coordinate/vector location
	 * @param b - Second coordinate/vector location
	 * @return
	 */
	
	
	public final static Coordinate subtract(Coordinate a, Coordinate b) {
		return new Coordinate(a.x - b.x, a.y - b.y, a.z - b.z);
	}
	
	/**
	 * Returns the dot product of two coordinate positions/vectors
	 * 
	 * @param a - First coordinate/vector location
	 * @param b - Second coordinate/vector location
	 * @return
	 */


	public final static double dot(Coordinate a, Coordinate b) {
		if (Double.isNaN(a.z) && Double.isNaN(b.z)) {
			return (a.x * b.x) + (a.y * b.y);
		} else {
			return (a.x * b.x) + (a.y * b.y) + (a.z * b.z);
		}
	}

	/**
	 * Returns the cross product of two coordinate positions/vectors
	 * 
	 * @param a - First coordinate/vector location
	 * @param b - Second coordinate/vector location
	 * @return
	 */
	
	public final static Coordinate cross(Coordinate a, Coordinate b) {
		return new Coordinate((a.y * b.z) - (b.y * a.z), (a.z * b.x)
				- (b.z * a.x), (a.x * b.y) - (b.x * a.y));
	}
	
	/**
	 * Returns the normalized cross product of two coordinate positions/vectors
	 * 
	 * @param a - First coordinate/vector location
	 * @param b - Second coordinate/vector location
	 * @return
	 */
	
	public final static Coordinate ncross(Coordinate a, Coordinate b) {
		
		return normalize(new Coordinate((a.y * b.z) - (b.y * a.z), (a.z * b.x)
				- (b.z * a.x), (a.x * b.y) - (b.x * a.y)));
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
	 * Returns the negative of the coordinate position
	 * 
	 * @param a - First coordinate/vector location
	 * @param b - Second coordinate/vector location
	 * @return
	 */
	
	public final static Coordinate negative(Coordinate c){
		return new Coordinate(-c.x,-c.y,-c.z);
	}
	
	/**
	 * Returns the distance from the origin/magnitude of the coordinate/vector 
	 * 
	 * @param c - Coordinate/vector location
	 * @return
	 */
	
	public final static double magnitude(Coordinate c){
		return Math.sqrt(dot(c,c));
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
	 * Reduces a LineSegment by a given distance from the back to the front
	 * 
	 * @param ls
	 * @param distance
	 * @return
	 */
	
	public final static LineSegment nibble(LineSegment ls, double distance){
	    Coordinate coord = new Coordinate();
	    double segmentLengthFraction = distance/ls.getLength();
	    coord.x = ls.p0.x + segmentLengthFraction * (ls.p1.x - ls.p0.x);
	    coord.y = ls.p0.y + segmentLengthFraction * (ls.p1.y - ls.p0.y);
	    coord.z = ls.p0.z + segmentLengthFraction * (ls.p1.z - ls.p0.z);
	    ls.p0=coord;
	    return ls;
	}
	
	/**
	 * From: http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
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
	
	public static final Coordinate average(Coordinate a, Coordinate b){
		return dilate(add(a,b),1d/2d);
	}
	
	public static final Coordinate[] midpoints(Coordinate[] ca){
		if(ca.length < 2){throw new IllegalArgumentException("Coordinate array must contain 2 more more points");}
		Coordinate[] out = new Coordinate[ca.length-1];
		for(int i = 0; i < out.length; i++){
			out[i]= average(ca[i],ca[i+1]);
		}
		return out;
	}
	
	public static Coordinate lonlat2ceqd(Coordinate c){
		double[] coords = Utils.lonlat2ceqd(new double[]{c.x,c.y,c.z});
		return new Coordinate(coords[0],coords[1],coords[2]);
	}
	
	public static Coordinate ceqd2lonlat(Coordinate c){
		double[] coords = Utils.ceqd2lonlat(new double[]{c.x,c.y,c.z});
		return new Coordinate(coords[0],coords[1],coords[2]);
	}
}
