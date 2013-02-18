package lagrange.impl.collision;

import lagrange.Boundary;
import lagrange.utils.CoordinateMath;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineSegment;

/**
 * 
 * @author Johnathan Kool
 * 
 */

public class Intersector_3D_Poly{
	
	/**
	 * Reflects a line segment relative to a flat polygon in 3D space.
	 * 
	 * @param ls
	 * @param vertices
	 * @return
	 */
	private double tolerance = 1E-8;
	public Intersector_3D_Poly(){}
	public Intersector_3D_Poly(Boundary bth){}
	
	public Coordinate intersection(LineSegment ls, Coordinate[] vertices) {

		Coordinate v0 = vertices[0];
		Coordinate v1 = vertices[1];
		Coordinate v2 = vertices[vertices.length-1];
		Coordinate p0 = ls.p0;
		Coordinate p1 = ls.p1;
		Coordinate u = CoordinateMath.subtract(v1, v0);
		Coordinate v = CoordinateMath.subtract(v2, v0);
		Coordinate norm = CoordinateMath.cross(u, v);
		//Coordinate norm = CoordinateMath.cross(v, u);

		if (norm.equals3D(new Coordinate())) {
			throw new IllegalArgumentException("Intersection polygon is degenerate.");
		}

		Coordinate dir = CoordinateMath.subtract(p1, p0);
		Coordinate w0 = CoordinateMath.subtract(p0, v0);

		double a = -CoordinateMath.dot(norm, w0);
		double b = CoordinateMath.dot(norm, dir);
		
		// Coincident with the plane
		
		if (Math.abs(b) < tolerance) {
			return new Coordinate(Double.NaN, Double.NaN, Double.NaN);
		}
		
		// Heading in the opposite direction

		double r = a / b;
		if (r < 0d) {
			return new Coordinate(Double.NaN, Double.NaN, Double.NaN);
		}
		
		return CoordinateMath.add(p0, CoordinateMath.dilate(dir, r));
	}
	
	public LineSegment reflect(LineSegment ls, Coordinate[] vertices) {

		Coordinate v0 = vertices[0];
		Coordinate v1 = vertices[1];
		Coordinate v2 = vertices[vertices.length-1];
		Coordinate p0 = ls.p0;
		Coordinate p1 = ls.p1;
		Coordinate u = CoordinateMath.subtract(v1, v0);
		Coordinate v = CoordinateMath.subtract(v2, v0);
		Coordinate norm = CoordinateMath.cross(u, v);
		//Coordinate norm = CoordinateMath.cross(v, u);

		if (norm.equals3D(new Coordinate())) {
			throw new IllegalArgumentException("Intersection polygon is degenerate.");
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
		
		Coordinate isect = CoordinateMath.add(p0, CoordinateMath.dilate(dir, r));
		
		// If the intersection is not within the polygon, then return the line
		
		if(!CoordinateMath.pointInPoly3D(isect, vertices)){
			return ls;
		}
		
		// If the intersection is not on the line, then return the line
		
		if(ls.distance(isect)>tolerance){
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

	public LineSegment reflect_special(LineSegment ls, Coordinate[] vertices) {

		Coordinate v0 = vertices[0];
		Coordinate v1 = vertices[1];
		Coordinate v2 = vertices[vertices.length-1];
		Coordinate p0 = ls.p0;
		Coordinate p1 = ls.p1;
		Coordinate u = CoordinateMath.subtract(v1, v0);
		Coordinate v = CoordinateMath.subtract(v2, v0);
		Coordinate norm = CoordinateMath.cross(u, v);
		//Coordinate norm = CoordinateMath.cross(v, u);

		if (norm.equals3D(new Coordinate())) {
			throw new IllegalArgumentException("Intersection polygon is degenerate.");
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
		
		Coordinate isect = CoordinateMath.add(p0, CoordinateMath.dilate(dir, r));
		
		// If the intersection is not within the polygon, then return the line
		
		if(!CoordinateMath.pointInPoly3D(isect, vertices)){
			return ls;
		}
		
		// If the intersection is not on the line, then return the line
		
		if(ls.distance(isect)>tolerance){
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
		LineSegment r_line = new LineSegment(isect, CoordinateMath.add(isect, reflection));
		
		if(r_line.p1.z>0){
			double xdir = Math.signum(r_line.p1.x-r_line.p0.x);
			double ydir = -Math.signum(r_line.p1.y-r_line.p0.y);
			double x_adj = xdir*r_line.p1.z*Math.sin(r_line.angle());
			double y_adj = ydir*r_line.p1.z*Math.cos(r_line.angle());
			r_line.p1.x+=x_adj;
			r_line.p1.y+=y_adj;
			r_line.p1.z=0;
		}
		return r_line;
	}
	
	public LineSegment reflect(LineSegment ls, Geometry polygon) {

		Coordinate[] vertices = new Coordinate[polygon.getNumPoints()-1];
		System.arraycopy(polygon.getCoordinates(), 0, vertices, 0, vertices.length);
		return reflect(ls,vertices);
	}
}