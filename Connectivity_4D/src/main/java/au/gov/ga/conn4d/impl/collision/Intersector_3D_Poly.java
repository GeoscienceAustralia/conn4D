package au.gov.ga.conn4d.impl.collision;


import au.gov.ga.conn4d.utils.CoordinateMath;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineSegment;


/**
 * 
 * Performs simple intersection and reflection operations associated with
 * an arbitrary planar polygon.
 * 
 * @author Johnathan Kool
 * 
 */

public class Intersector_3D_Poly {
	
	private double tolerance = 1E-8;
	private double surfaceLevel = 0;
	public static final Coordinate NaN = new Coordinate(Double.NaN, Double.NaN,
			Double.NaN);

	/**
	 * Returns the intersection point of a line segment with the plane defined
	 * by the given vertices. The intersection point must lie within the vertices,
	 * otherwise a NaN Coordinate is returned.
	 * 
	 * @param ls
	 * @param vertices
	 */
	
	public Coordinate intersection(LineSegment ls, Coordinate[] vertices) {
		Coordinate p_isect = plane_intersection(ls, vertices);
		if (p_isect == NaN) {
			return p_isect;
		}

		// If the intersection is not within the polygon, then return the line

		if (!CoordinateMath.pointInPoly3D(p_isect, vertices)) {
			return NaN;
		}

		// If the intersection is not on the line, then return the line

		if (ls.distance(p_isect) > tolerance) {
			return NaN;
		}

		return p_isect;
	}

	/**
	 * Returns the intersection point of a line segment with the plane defined
	 * by the given vertices. The vertices do not necessarily contain the
	 * intersection point.
	 * 
	 * @param ls
	 * @param vertices
	 */

	public Coordinate plane_intersection(LineSegment ls, Coordinate[] vertices) {

		Coordinate v0 = vertices[0];
		Coordinate v1 = vertices[1];
		Coordinate v2 = vertices[vertices.length - 1];
		Coordinate p0 = ls.p0;
		Coordinate p1 = ls.p1;
		Coordinate u = CoordinateMath.subtract(v1, v0);
		Coordinate v = CoordinateMath.subtract(v2, v0);
		Coordinate norm = CoordinateMath.cross(u, v);

		if (norm.equals3D(new Coordinate())) {
			throw new IllegalArgumentException(
					"Intersection polygon is degenerate.");
		}

		Coordinate dir = CoordinateMath.subtract(p1, p0);
		Coordinate w0 = CoordinateMath.subtract(p0, v0);

		double a = -CoordinateMath.dot(norm, w0);
		double b = CoordinateMath.dot(norm, dir);

		// Coincident with the plane

		if (Math.abs(b) < tolerance) {
			return NaN;
		}

		// Heading in the opposite direction

		double r = a / b;
		if (r < 0d) {
			return NaN;
		}

		return CoordinateMath.add(p0, CoordinateMath.dilate(dir, r));
	}

	/**
	 * Performs a reflection of the line segment off the surface given by the
	 * vertices. The vertices must contain the intersection point to generate a
	 * reflection. During model operation, reflection is currently handled by
	 * the CoordinateMath class.
	 * 
	 * @param ls
	 * @param vertices
	 */

	public LineSegment reflect(LineSegment ls, Coordinate[] vertices) {

		Coordinate v0 = vertices[0];
		Coordinate v1 = vertices[1];
		Coordinate v2 = vertices[vertices.length - 1];
		Coordinate p0 = ls.p0;
		Coordinate p1 = ls.p1;
		Coordinate u = CoordinateMath.subtract(v1, v0);
		Coordinate v = CoordinateMath.subtract(v2, v0);
		Coordinate norm = CoordinateMath.cross(u, v);

		if (norm.equals3D(new Coordinate())) {
			throw new IllegalArgumentException(
					"Intersection polygon is degenerate.");
		}

		Coordinate dir = CoordinateMath.subtract(p1, p0);
		Coordinate w0 = CoordinateMath.subtract(p0, v0);

		double a = -CoordinateMath.dot(norm, w0);
		double b = CoordinateMath.dot(norm, dir);

		// Coincident with the plane

		if (Math.abs(b) < tolerance) {
			return ls;
		}

		// Heading in the opposite direction

		double r = a / b;
		if (r < 0d) {
			return ls;
		}

		Coordinate isect = CoordinateMath
				.add(p0, CoordinateMath.dilate(dir, r));

		// If the intersection is not within the polygon, then return the line

		if (!CoordinateMath.pointInPoly3D(isect, vertices)) {
			return ls;
		}

		// If the intersection is not on the line, then return the line

		if (ls.distance(isect) > tolerance) {
			return ls;
		}

		Coordinate nnorm = CoordinateMath.normalize(norm);
		double middleterm = CoordinateMath.dot(dir, nnorm);
		Coordinate middleterm2 = CoordinateMath.dilate(nnorm, 2 * middleterm);
		Coordinate result = CoordinateMath.subtract(dir, middleterm2);
		Coordinate nresult = CoordinateMath.normalize(result);
		double remainder = CoordinateMath.magnitude(CoordinateMath.subtract(
				isect, p1));
		Coordinate reflection = CoordinateMath.dilate(nresult, remainder);
		return new LineSegment(isect, CoordinateMath.add(isect, reflection));
	}

	/**
	 * Performs a reflection of the line segment off the surface given by the
	 * vertices. The vertices must contain the intersection point to generate a
	 * reflection. During model operation, reflection is currently handled by
	 * the CoordinateMath class.  This method takes a Geometry as an argument
	 * rather than a Coordinate array of vertices.
	 * 
	 * @param ls - the LineSegment indicating the path of an object.
	 * @param polygon - a Geometry describing the reflection surface
	 */

	public LineSegment reflect(LineSegment ls, Geometry polygon) {

		Coordinate[] vertices = new Coordinate[polygon.getNumPoints() - 1];
		System.arraycopy(polygon.getCoordinates(), 0, vertices, 0,
				vertices.length);
		return reflect(ls, vertices);
	}
	
	public LineSegment reflect_special(LineSegment ls, Coordinate[] vertices) {
		return reflect_special(ls, vertices, this.surfaceLevel);
	}
	
	/**
	 * Performs a reflection of the line segment off the surface given by the
	 * vertices. The vertices must contain the intersection point to generate a
	 * reflection. During model operation, reflection is currently handled by
	 * the CoordinateMath class. This method provides additional functionality
	 * by bounding using surfaceLevel. i.e. values will be constrained to be
	 * below surfaceLevel.
	 * 
	 * @param ls
	 *            - the LineSegment indicating the path of an object.
	 * @param vertices
	 *            - Coordinates indicating vertices of a flat polygon
	 * @param surfaceLevel
	 *            - a double value indicating maximum possible vertical values.
	 */

	public LineSegment reflect_special(LineSegment ls, Coordinate[] vertices,
			double surfaceLevel) {

		Coordinate v0 = vertices[0];
		Coordinate v1 = vertices[1];
		Coordinate v2 = vertices[vertices.length - 1];
		Coordinate p0 = ls.p0;
		Coordinate p1 = ls.p1;
		Coordinate u = CoordinateMath.subtract(v1, v0);
		Coordinate v = CoordinateMath.subtract(v2, v0);
		Coordinate norm = CoordinateMath.cross(u, v);

		if (norm.equals3D(new Coordinate())) {
			throw new IllegalArgumentException(
					"Intersection polygon is degenerate.");
		}

		Coordinate dir = CoordinateMath.subtract(p1, p0);
		Coordinate w0 = CoordinateMath.subtract(p0, v0);

		double a = -CoordinateMath.dot(norm, w0);
		double b = CoordinateMath.dot(norm, dir);

		// Coincident with the plane

		if (Math.abs(b) < tolerance) {
			if (ls.p1.z > 0) {
				ls.p1.z = 0;
			}
			return ls;
		}

		// Heading in the opposite direction

		double r = a / b;
		if (r < 0d) {
			if (ls.p1.z > 0) {
				ls.p1.z = 0;
			}
			return ls;
		}

		Coordinate isect = CoordinateMath
				.add(p0, CoordinateMath.dilate(dir, r));

		// If the intersection is not within the polygon, then return the line

		if (!CoordinateMath.pointInPoly3D(isect, vertices)) {
			return ls;
		}

		// If the intersection is not on the line, then return the line

		if (ls.distance(isect) > tolerance) {
			return ls;
		}

		Coordinate nnorm = CoordinateMath.normalize(norm);
		double middleterm = CoordinateMath.dot(dir, nnorm);
		Coordinate middleterm2 = CoordinateMath.dilate(nnorm, 2 * middleterm);
		Coordinate result = CoordinateMath.subtract(dir, middleterm2);
		Coordinate nresult = CoordinateMath.normalize(result);
		double remainder = CoordinateMath.magnitude(CoordinateMath.subtract(
				isect, p1));
		Coordinate reflection = CoordinateMath.dilate(nresult, remainder);
		LineSegment r_line = new LineSegment(isect, CoordinateMath.add(isect,
				reflection));

		if (r_line.p1.z > 0) {
			r_line.p1.z = 0;
		}

		return r_line;
	}
	
	/**
	 * 
	 * Performs a reflection of the line segment off the surface given by the
	 * vertices. The vertices must contain the intersection point to generate a
	 * reflection. During model operation, reflection is currently handled by
	 * the CoordinateMath class. This method provides additional functionality
	 * by bounding using surfaceLevel. i.e. values will be constrained to be
	 * below surfaceLevel.
	 * 
	 * @param ls - the LineSegment indicating the path of an object.
	 * @param polygon - a Geometry describing the reflection surface
	 */

	public LineSegment reflect_special(LineSegment ls, Geometry polygon) {
		return reflect_special(ls, polygon, this.surfaceLevel);
	}
	
	/**
	 * 
	 * Performs a reflection of the line segment off the surface given by the
	 * vertices. The vertices must contain the intersection point to generate a
	 * reflection. During model operation, reflection is currently handled by
	 * the CoordinateMath class. This method provides additional functionality
	 * by bounding using surfaceLevel. i.e. values will be constrained to be
	 * below surfaceLevel.
	 * 
	 * @param ls - the LineSegment indicating the path of an object.
	 * @param polygon - a Geometry describing the reflection surface
	 * @param surfaceLevel - a double value indicating maximum possible vertical values.
	 */

	public LineSegment reflect_special(LineSegment ls, Geometry polygon,
			double surfaceLevel) {
		Coordinate[] vertices = new Coordinate[polygon.getNumPoints() - 1];
		System.arraycopy(polygon.getCoordinates(), 0, vertices, 0,
				vertices.length);
		LineSegment tmp = reflect_special(ls, vertices, surfaceLevel);
		return tmp;
	}
	
	/**
	 * Returns the NaN Coordinate value being used by this class.
	 * 
	 * @return - A Coordinate containing NaN values
	 */

	public Coordinate getNaN() {
		return NaN;
	}

	/**
	 * Retrieves the surface level being used as a maximum value by the class
	 */
	
	public double getSurfaceLevel(){
		return surfaceLevel;
	}
	
	/**
	 * Sets the surface level being used as a maximum value by the class
	 * @param surface
	 */
	
	public void setSurfaceLevel(double surface){
		this.surfaceLevel = surface;
	}
}