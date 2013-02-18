package lagrange.impl.collision;

import lagrange.utils.CoordinateMath;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineSegment;

/**
 * 
 * @author Johnathan Kool
 * 
 */

public class Intersector_3D_Triangles {
	
	public Coordinate intersection(Geometry triangle, LineSegment ls){
		return intersection(triangle.getCoordinates(), ls);
	}

	public Coordinate intersection(Coordinate[] triangle, LineSegment ls) {
		Coordinate origin = new Coordinate();
		Coordinate NaN = new Coordinate(Double.NaN, Double.NaN, Double.NaN);
		Coordinate v0 = triangle[0];
		Coordinate v1 = triangle[1];
		Coordinate v2 = triangle[2];
		Coordinate u = CoordinateMath.subtract(v1, v0);
		Coordinate v = CoordinateMath.subtract(v2, v0);
		Coordinate n = CoordinateMath.cross(u, v);
		
		if (n.equals3D(origin)) {
			throw new IllegalArgumentException("Triangle is degenerate.");
		}

		Coordinate dir = CoordinateMath.subtract(ls.p1, ls.p0);
		Coordinate w0 = CoordinateMath.subtract(ls.p0, v0);

		double a = -CoordinateMath.dot(n, w0);
		double b = CoordinateMath.dot(n, dir);

		if (Math.abs(b) < 1E-6) {
			return ls.p1;
		}

		double r = a / b;
		if (r < 0d) {
			return NaN;
		}

		Coordinate isect = CoordinateMath.add(ls.p0,
				CoordinateMath.dilate(dir, r));

		// Check whether isect is interior to the triangle

		double uu = CoordinateMath.dot(u, u);
		double uv = CoordinateMath.dot(u, v);
		double vv = CoordinateMath.dot(v, v);

		Coordinate w = CoordinateMath.subtract(isect, v0);
		double wu = CoordinateMath.dot(w, u);
		double wv = CoordinateMath.dot(w, v);

		double D = uv * uv - uu * vv;

		double s = (uv * wv - vv * wu) / D;
		if (s < 0d || s > 1d) {
			return NaN;
		}
		double t = (uv * wu - uu * wv) / D;
		if (t < 0d || t > 1d) {
			return NaN;
		}
		return isect;
	}
	
	public LineSegment reflect(LineSegment ls, Coordinate[] triangle){
		Coordinate v0 = triangle[0];
		Coordinate v1 = triangle[1];
		Coordinate v2 = triangle[2];
		Coordinate p0 = ls.p0;
		Coordinate p1 = ls.p1;
		Coordinate u = CoordinateMath.subtract(v1, v0);
		Coordinate v = CoordinateMath.subtract(v2, v0);
		Coordinate norm = CoordinateMath.cross(u, v);

		if (norm.equals3D(new Coordinate())) {
			throw new IllegalArgumentException("Triangle is degenerate.");
		}

		Coordinate dir = CoordinateMath.subtract(p1, p0);
		Coordinate w0 = CoordinateMath.subtract(p0, v0);

		double a = -CoordinateMath.dot(norm, w0);
		double b = CoordinateMath.dot(norm, dir);

		// Coincident with the triangle
		
		if (Math.abs(b) < 1E-6) {
			return ls;
		}
		
		// Moving in the opposite direction

		double r = a / b;
		if (r < 0d) {
			return ls;
		}

		Coordinate isect = CoordinateMath
				.add(p0, CoordinateMath.dilate(dir, r));

		// Check whether isect is interior to the triangle

		double uu = CoordinateMath.dot(u, u);
		double uv = CoordinateMath.dot(u, v);
		double vv = CoordinateMath.dot(v, v);

		Coordinate w = CoordinateMath.subtract(isect, v0);
		double wu = CoordinateMath.dot(w, u);
		double wv = CoordinateMath.dot(w, v);

		double D = uv * uv - uu * vv;

		double s = (uv * wv - vv * wu) / D;
		if (s < 0d || s > 1d) {
			return ls;
		}
		double t = (uv * wu - uu * wv) / D;
		if (t < 0d || t > 1d) {
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
	
	public LineSegment reflect_special(LineSegment ls, Coordinate[] triangle){
		Coordinate v0 = triangle[0];
		Coordinate v1 = triangle[1];
		Coordinate v2 = triangle[2];
		Coordinate p0 = ls.p0;
		Coordinate p1 = ls.p1;
		Coordinate u = CoordinateMath.subtract(v1, v0);
		Coordinate v = CoordinateMath.subtract(v2, v0);
		Coordinate norm = CoordinateMath.cross(u, v);

		if (norm.equals3D(new Coordinate())) {
			throw new IllegalArgumentException("Triangle is degenerate.");
		}

		Coordinate dir = CoordinateMath.subtract(p1, p0);
		Coordinate w0 = CoordinateMath.subtract(p0, v0);

		double a = -CoordinateMath.dot(norm, w0);
		double b = CoordinateMath.dot(norm, dir);

		// Coincident with the triangle
		
		if (Math.abs(b) < 1E-6) {
			return ls;
		}
		
		// Moving in the opposite direction

		double r = a / b;
		if (r < 0d) {
			return ls;
		}

		Coordinate isect = CoordinateMath
				.add(p0, CoordinateMath.dilate(dir, r));

		// Check whether isect is interior to the triangle

		double uu = CoordinateMath.dot(u, u);
		double uv = CoordinateMath.dot(u, v);
		double vv = CoordinateMath.dot(v, v);

		Coordinate w = CoordinateMath.subtract(isect, v0);
		double wu = CoordinateMath.dot(w, u);
		double wv = CoordinateMath.dot(w, v);

		double D = uv * uv - uu * vv;

		double s = (uv * wv - vv * wu) / D;
		if (s < 0d || s > 1d) {
			return ls;
		}
		double t = (uv * wu - uu * wv) / D;
		if (t < 0d || t > 1d) {
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
		
		// If z is greater than 0, rotate around the line to the surface
		// until z becomes 0.  Select the direction of rotation that lies
		// away from the point of origin (p0)
		
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

	public LineSegment reflect(LineSegment ls, Geometry triangle) {
		return reflect(ls, triangle.getCoordinates());
	}
}