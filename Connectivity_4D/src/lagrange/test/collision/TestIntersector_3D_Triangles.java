package lagrange.test.collision;
import lagrange.impl.collision.Intersector_3D_Triangles;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineSegment;


public class TestIntersector_3D_Triangles {
	
	Geometry triangle,triangle2,triangle3,triangle4,triangle5,triangle6;
	LineSegment linex1,linex2,linex3,linex4,linex5,linex6,linex7,linex8,linex9,linex10,linex11,linex12,linex14;
	LineSegment liney1,liney2,liney3,liney4,liney5,liney6,liney7,liney8,liney9,liney10,liney11,liney12,liney14;
	LineSegment linez1,linez2,linez3,linez4,linez5,linez6,linez7,linez8,linez9,linez10,linez11,linez12,linez14;
	Intersector_3D_Triangles lsi = new Intersector_3D_Triangles();
	
	@Before
	public void setUp() throws Exception {
		GeometryFactory gf = new GeometryFactory();
		Coordinate c0 = new Coordinate( 0, 1, 0);
		Coordinate c1 = new Coordinate(-1,-1, 0);
		Coordinate c2 = new Coordinate( 1,-1, 0);
		
		Coordinate p0 = new Coordinate( 0, 0, 1);
		Coordinate p1 = new Coordinate( 0, 0,-1);
		
		triangle = gf.createPolygon(gf.createLinearRing(new Coordinate[]{c0,c1,c2,c0}), null);
		triangle2 = gf.createPolygon(gf.createLinearRing(new Coordinate[]{c2,c1,c0,c2}), null);
		triangle3 = gf.createPolygon(gf.createLinearRing(new Coordinate[]{
				new Coordinate(0,0,1),
				new Coordinate(0,-1,-1),
				new Coordinate(0,1,-1),
				new Coordinate(0,0,1)}), null);
		triangle4 = gf.createPolygon(gf.createLinearRing(new Coordinate[]{
				new Coordinate(0,0,1),
				new Coordinate(-1,0,-1),
				new Coordinate(1,0,-1),
				new Coordinate(0,0,1)}), null);
		triangle5 = gf.createPolygon(gf.createLinearRing(new Coordinate[]{
				new Coordinate(-1,1,1),
				new Coordinate(0,-1,-1),
				new Coordinate(1,1,1),
				new Coordinate(-1,1,1)}), null);
		triangle6 = gf.createPolygon(gf.createLinearRing(new Coordinate[]{
				new Coordinate(-0.1,0.1,0.1),
				new Coordinate(0,-0.1,-0.1),
				new Coordinate(0.1,0.1,0.1),
				new Coordinate(-0.1,0.1,0.1)}), null);

		linex1 = new LineSegment(new Coordinate(-1,0,0),new Coordinate(1,0,0));
		linex2 = new LineSegment(new Coordinate(0,0,0),new Coordinate(0,0,0));
		linex3 = new LineSegment(new Coordinate(-1,0,1), new Coordinate(1,0,-1));
		linex4 = new LineSegment(new Coordinate(-1,1,0), new Coordinate(-1,-1,0));
		linex5 = new LineSegment(new Coordinate(-1,0.25,0.25), new Coordinate(1,0.25,0.25));
		linex6 = new LineSegment(new Coordinate(-1,-0.25,-0.25), new Coordinate(1,-0.25,-0.25));
		linex7 = new LineSegment(new Coordinate(-1,0,0), new Coordinate(-2,0,0));
		linex8 = new LineSegment(new Coordinate(1,0,0), new Coordinate(-1,0,0));
		linex9 = new LineSegment(new Coordinate(-1,-1,-1), new Coordinate(1,-1,-1));
		linex10 = new LineSegment(new Coordinate(-1,-1-1E-5,-1-1E-5), new Coordinate(1,-1-1E-5,-1-1E-5));
		linex11 = new LineSegment(new Coordinate(1,0,0), new Coordinate(0,0,0));
		linex12 = new LineSegment(new Coordinate(0,0,0), new Coordinate(1,0,0));
		
		liney1 = new LineSegment(new Coordinate(0,1,0),new Coordinate(0,-1,0));
		liney2 = new LineSegment(new Coordinate(0,0,0),new Coordinate(0,0,0));
		liney3 = new LineSegment(new Coordinate(0,1,1), new Coordinate(0,-1,-1));
		liney4 = new LineSegment(new Coordinate(1,-1,0), new Coordinate(-1,-1,0));
		liney5 = new LineSegment(new Coordinate(0.25,-1,0.25), new Coordinate(0.25,1,0.25));
		liney6 = new LineSegment(new Coordinate(-0.25,-1,-0.25), new Coordinate(-0.25,1,-0.25));
		liney7 = new LineSegment(new Coordinate(0,-1,0), new Coordinate(0,-2,0));
		liney8 = new LineSegment(new Coordinate(0,1,0), new Coordinate(0,-1,0));
		liney9 = new LineSegment(new Coordinate(-1,-1,-1), new Coordinate(-1,1,-1));
		liney10 = new LineSegment(new Coordinate(-1-1E-5,-1,-1-1E-5), new Coordinate(-1-1E-5,1,-1-1E-5));
		liney11 = new LineSegment(new Coordinate(0,1,0), new Coordinate(0,0,0));
		liney12 = new LineSegment(new Coordinate(0,0,0), new Coordinate(0,1,0));
		
		linez1 = new LineSegment(p0,p1);
		linez2 = new LineSegment(new Coordinate(0,0,0),new Coordinate(0,0,0));
		linez3 = new LineSegment(new Coordinate(-1,0,1), new Coordinate(1,0,-1));
		linez4 = new LineSegment(new Coordinate(0,1,1), new Coordinate(0,-1,-1));
		linez5 = new LineSegment(new Coordinate(0.25,0.25,1), new Coordinate(0.25,0.25,-1));
		linez6 = new LineSegment(new Coordinate(-0.25,-0.25,1), new Coordinate(-0.25,-0.25,-1));
		linez7 = new LineSegment(new Coordinate(0,0,1), new Coordinate(0,0,2));
		linez8 = new LineSegment(new Coordinate(0,0,-1), new Coordinate(0,0,1));
		linez9 = new LineSegment(new Coordinate(-1,-1,1), new Coordinate(-1,-1,-1));
		linez10 = new LineSegment(new Coordinate(-1-1E-5,-1-1E-5,1), new Coordinate(-1-1E-5,-1-1E-5,-1));
		linez11 = new LineSegment(new Coordinate(0,0,1), new Coordinate(0,0,0));
		linez12 = new LineSegment(new Coordinate(0,0,0), new Coordinate(0,0,1));
		linez14 = new LineSegment(new Coordinate(-0.5,0.5,0), new Coordinate(0.5,0.5,0));
	}

	@Test
	public void testIntersect() {
		
		Assert.assertArrayEquals(new double[]{-1,0,0}, c2arr(lsi.reflect(linex1, triangle3).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{0,0,0}, c2arr(lsi.reflect(linex2, triangle3).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{-1,0,-1}, c2arr(lsi.reflect(linex3, triangle3).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{-1,-1,0}, c2arr(lsi.reflect(linex4, triangle3).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{-1,0.25,0.25}, c2arr(lsi.reflect(linex5, triangle3).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{-1,-0.25,-0.25}, c2arr(lsi.reflect(linex6, triangle3).p1), 1E-6);
		Assert.assertEquals(linex7.p1, lsi.reflect(linex7, triangle3).p1);
		Assert.assertArrayEquals(new double[]{1,0,0}, c2arr(lsi.reflect(linex8, triangle3).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{-1,-1,-1}, c2arr(lsi.reflect(linex9, triangle3).p1), 1E-6);
		Assert.assertEquals(linex10.p1, lsi.reflect(linex10, triangle3).p1);
		Assert.assertArrayEquals(new double[]{0,0,0}, c2arr(lsi.reflect(linex11, triangle3).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{-1,0,0}, c2arr(lsi.reflect(linex12, triangle3).p1), 1E-6);
		
		Assert.assertArrayEquals(new double[]{0,1,0}, c2arr(lsi.reflect(liney1, triangle4).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{0,0,0}, c2arr(lsi.reflect(liney2, triangle4).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{0,1,-1}, c2arr(lsi.reflect(liney3, triangle4).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{-1,-1,0}, c2arr(lsi.reflect(liney4, triangle4).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{0.25,-1,0.25}, c2arr(lsi.reflect(liney5, triangle4).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{-0.25,-1,-0.25}, c2arr(lsi.reflect(liney6, triangle4).p1), 1E-6);
		Assert.assertEquals(liney7.p1, lsi.reflect(liney7, triangle4).p1);
		Assert.assertArrayEquals(new double[]{0,1,0}, c2arr(lsi.reflect(liney8, triangle4).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{-1,-1,-1}, c2arr(lsi.reflect(liney9, triangle4).p1), 1E-6);
		Assert.assertEquals(liney10.p1, lsi.reflect(liney10, triangle4).p1);
		Assert.assertArrayEquals(new double[]{0,0,0}, c2arr(lsi.reflect(liney11, triangle4).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{0,-1,0}, c2arr(lsi.reflect(liney12, triangle4).p1), 1E-6);
		
		Assert.assertArrayEquals(new double[]{0,0,1}, c2arr(lsi.reflect(linez1, triangle).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{0,0,0}, c2arr(lsi.reflect(linez2, triangle).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{1,0,1}, c2arr(lsi.reflect(linez3, triangle).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{0,-1,1}, c2arr(lsi.reflect(linez4, triangle).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{0.25,0.25,1}, c2arr(lsi.reflect(linez5, triangle).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{-0.25,-0.25,1}, c2arr(lsi.reflect(linez6, triangle).p1), 1E-6);
		Assert.assertEquals(linez7.p1, lsi.reflect(linez7, triangle).p1);
		Assert.assertArrayEquals(new double[]{0,0,-1}, c2arr(lsi.reflect(linez8, triangle).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{-1,-1,1}, c2arr(lsi.reflect(linez9, triangle).p1), 1E-6);
		Assert.assertEquals(linez10.p1, lsi.reflect(linez10, triangle).p1);
		Assert.assertArrayEquals(new double[]{0,0,0}, c2arr(lsi.reflect(linez11, triangle).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{0,0,-1}, c2arr(lsi.reflect(linez12, triangle).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{0,-1,0}, c2arr(lsi.reflect(linez1, triangle5).p1), 1E-6);
		Assert.assertArrayEquals(new double[]{0,-1,0}, c2arr(lsi.reflect(linez1, triangle6).p1), 1E-6);
	}
	
	private double[] c2arr(Coordinate c){
		return new double[]{c.x,c.y,c.z};
	}
	
	public static void main(String[] args){
		TestIntersector_3D_Triangles lsit = new TestIntersector_3D_Triangles();
		try {
			lsit.setUp();
			lsit.testIntersect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
