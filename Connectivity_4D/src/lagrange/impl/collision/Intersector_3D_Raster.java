package lagrange.impl.collision;

import lagrange.utils.CoordinateMath;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

public class Intersector_3D_Raster {
	
	Intersector_3D_Poly i3p = new Intersector_3D_Poly();
	
	public Coordinate intersect(LineSegment ls, Coordinate[] vertices) {
		// Better criteria might be combined distance to the centroid?
		double d1 = CoordinateMath.magnitude(CoordinateMath.subtract(vertices[0], vertices[2]));
		double d2 = CoordinateMath.magnitude(CoordinateMath.subtract(vertices[1], vertices[3]));

		Coordinate[] t1,t2;
		
		if(d2>d1){
			t1 = new Coordinate[]{vertices[0],vertices[1],vertices[3]};
			t2 = new Coordinate[]{vertices[1],vertices[2],vertices[3]};
		}
		else{
			t1 = new Coordinate[]{vertices[0],vertices[1],vertices[2]};
			t2 = new Coordinate[]{vertices[0],vertices[2],vertices[3]};
		}
		
		Coordinate isect1 = i3p.intersection(ls,t1);
		Coordinate isect2 = i3p.intersection(ls,t2);
		
		if(isect1.distance(ls.p0)<isect1.distance(ls.p0))
			{return isect1;}
		return isect2;
	}
	
	public LineSegment reflect(LineSegment ls, Coordinate[] vertices){
		// Better criteria might be combined distance to the centroid?
		double d1 = CoordinateMath.magnitude(CoordinateMath.subtract(vertices[0], vertices[2]));
		double d2 = CoordinateMath.magnitude(CoordinateMath.subtract(vertices[1], vertices[3]));

		Coordinate[] t1,t2;
		
		if(d2>d1){
			t1 = new Coordinate[]{vertices[0],vertices[1],vertices[3]};
			t2 = new Coordinate[]{vertices[1],vertices[2],vertices[3]};
		}
		else{
			t1 = new Coordinate[]{vertices[0],vertices[1],vertices[2]};
			t2 = new Coordinate[]{vertices[0],vertices[2],vertices[3]};
		}
		
		LineSegment ls1 = i3p.reflect(ls,t1);
		LineSegment ls2 = i3p.reflect(ls,t2);
		
		boolean ls1_change = ls1.p1!=ls.p1;
		boolean ls2_change = ls2.p1!=ls.p1;
		
		// If a reflection occurred off of both triangles, then test to see
		// which intersection point is closer to the beginning of the line.
		
		if(ls1_change&&ls2_change){
			if(ls2.p0.distance(ls.p0)>ls1.p0.distance(ls.p0)){
				return ls1;
			}
			return ls2;
		}
		// Otherwise pick one, the other, or neither
		else{
		if(ls1_change){return ls1;}
		if(ls2_change){return ls2;}
		return ls;}	
	}
	
	public LineSegment reflect_special(LineSegment ls, Coordinate[] vertices){
		// Better criteria might be combined distance to the centroid?
		double d1 = CoordinateMath.magnitude(CoordinateMath.subtract(vertices[0], vertices[2]));
		double d2 = CoordinateMath.magnitude(CoordinateMath.subtract(vertices[1], vertices[3]));

		Coordinate[] t1,t2;
		
		if(d2>d1){
			t1 = new Coordinate[]{vertices[0],vertices[1],vertices[3]};
			t2 = new Coordinate[]{vertices[1],vertices[2],vertices[3]};
		}
		else{
			t1 = new Coordinate[]{vertices[0],vertices[1],vertices[2]};
			t2 = new Coordinate[]{vertices[0],vertices[2],vertices[3]};
		}
		
		// If z is greater, special reflection rotates the vector away
		// from the reflection surface until z = 0.
		
		LineSegment ls1 = i3p.reflect_special(ls,t1);
		LineSegment ls2 = i3p.reflect_special(ls,t2);
		
		boolean ls1_change = ls1.p1!=ls.p1;
		boolean ls2_change = ls2.p1!=ls.p1;
		
		// If a reflection occurred off of both triangles, then test to see
		// which intersection point is closer to the beginning of the line.
		
		if(ls1_change&&ls2_change){
			if(ls2.p0.distance(ls.p0)>ls1.p0.distance(ls.p0)){
				return ls1;
			}
			return ls2;
		}
		// Otherwise pick one, the other, or neither
		else{
		if(ls1_change){return ls1;}
		if(ls2_change){return ls2;}
		return ls;}	
	}
	
	public LineSegment reflect_reverse(LineSegment ls, Coordinate[] vertices){
		// Better criteria might be combined distance to the centroid?
		double d1 = CoordinateMath.magnitude(CoordinateMath.subtract(vertices[0], vertices[2]));
		double d2 = CoordinateMath.magnitude(CoordinateMath.subtract(vertices[1], vertices[3]));

		Coordinate[] t1,t2;
		
		if(d2>d1){
			t1 = new Coordinate[]{vertices[0],vertices[1],vertices[3]};
			t2 = new Coordinate[]{vertices[1],vertices[2],vertices[3]};
		}
		else{
			t1 = new Coordinate[]{vertices[0],vertices[1],vertices[2]};
			t2 = new Coordinate[]{vertices[0],vertices[2],vertices[3]};
		}
		
		// If z is greater, special reflection rotates the vector away
		// from the reflection surface until z = 0.
		
		LineSegment ls1 = i3p.reflect_reverse(ls,t1);
		LineSegment ls2 = i3p.reflect_reverse(ls,t2);
		
		boolean ls1_change = ls1.p1!=ls.p1;
		boolean ls2_change = ls2.p1!=ls.p1;
		
		// If a reflection occurred off of both triangles, then test to see
		// which intersection point is closer to the beginning of the line.
		
		if(ls1_change&&ls2_change){
			if(ls2.p0.distance(ls.p0)>ls1.p0.distance(ls.p0)){
				return ls1;
			}
			return ls2;
		}
		// Otherwise pick one, the other, or neither
		else{
		if(ls1_change){return ls1;}
		if(ls2_change){return ls2;}
		return ls;}	
	}
}
