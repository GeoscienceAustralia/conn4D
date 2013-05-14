package lagrange.test.utils;

import junit.framework.Assert;
import lagrange.utils.CoordinateMath;

import org.junit.Before;

import com.vividsolutions.jts.geom.Coordinate;

public class TestCoordinateMath {
	
	Coordinate origin = new Coordinate(0,0,0);
	Coordinate x1 = new Coordinate(1,0,0);
	Coordinate y1 = new Coordinate(0,1,0);
	Coordinate z1 = new Coordinate(0,0,1);
	Coordinate x2 = new Coordinate(-1,0,0);
	Coordinate y2 = new Coordinate(0,-1,0);
	Coordinate z2 = new Coordinate(0,0,-1);
	Coordinate xy1 = new Coordinate(1,1,0);
	Coordinate xz1 = new Coordinate(1,0,1);
	Coordinate yz1 = new Coordinate(0,1,1);
	Coordinate xyz1 = new Coordinate(1,1,1);
	Coordinate xyz2 = new Coordinate(1,-1,1);
	
	@Before
	public void setUp(){}

	//@Test
	public void testAngle3DSigned() {
		// 90° x and z direction
		Assert.assertEquals(90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,0,1), origin, new Coordinate(1,0,1),z1)),1E-16);
		Assert.assertEquals(90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,0,1), origin, new Coordinate(-1,0,1),z1)),1E-16);
		Assert.assertEquals(-90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,0,1), origin, new Coordinate(1,0,1),z2)),1E-16);
		Assert.assertEquals(-90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,0,1), origin, new Coordinate(-1,0,1),z2)),1E-16);
		Assert.assertEquals(-90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,0,-1), origin, new Coordinate(1,0,-1),z1)),1E-16);
		Assert.assertEquals(-90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,0,-1), origin, new Coordinate(-1,0,-1),z1)),1E-16);
		Assert.assertEquals(90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,0,-1), origin, new Coordinate(1,0,-1),z2)),1E-16);
		Assert.assertEquals(90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,0,-1), origin, new Coordinate(-1,0,-1),z2)),1E-16);
		
		
		// 90° y and z direction
		
		Assert.assertEquals(90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(0,-1,1), origin, new Coordinate(0,1,1),z1)),1E-16);
		Assert.assertEquals(90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(0,1,1), origin, new Coordinate(0,-1,1),z1)),1E-16);
		Assert.assertEquals(-90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(0,-1,1), origin, new Coordinate(0,1,1),z2)),1E-16);
		Assert.assertEquals(-90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(0,1,1), origin, new Coordinate(0,-1,1),z2)),1E-16);
		Assert.assertEquals(-90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(0,-1,-1), origin, new Coordinate(0,1,-1),z1)),1E-16);
		Assert.assertEquals(-90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(0,1,-1), origin, new Coordinate(0,-1,-1),z1)),1E-16);
		Assert.assertEquals(90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(0,-1,-1), origin, new Coordinate(0,1,-1),z2)),1E-16);
		Assert.assertEquals(90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(0,1,-1), origin, new Coordinate(0,-1,-1),z2)),1E-16);

		// 90° x and y direction
		
		Assert.assertEquals(90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,-1,0), origin, new Coordinate(1,1,0),x1)),1E-16);
		Assert.assertEquals(90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,1,0), origin, new Coordinate(1,-1,0),x1)),1E-16);
		Assert.assertEquals(-90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,-1,0), origin, new Coordinate(1,1,0),x2)),1E-16);
		Assert.assertEquals(-90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,1,0), origin, new Coordinate(1,-1,0),x2)),1E-16);
		Assert.assertEquals(-90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,-1,0), origin, new Coordinate(-1,1,0),y2)),1E-16);
		Assert.assertEquals(90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,1,0), origin, new Coordinate(-1,-1,0),y2)),1E-16);
		Assert.assertEquals(90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,-1,0), origin, new Coordinate(-1,1,0),y1)),1E-16);
		Assert.assertEquals(-90d,Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,1,0), origin, new Coordinate(-1,-1,0),y1)),1E-16);	
	}
	
	//@Test
	public void testNCross(){
		System.out.println(CoordinateMath.ncross(new Coordinate(0,1,0),new Coordinate(1,0,0)));
		System.out.println(CoordinateMath.ncross(new Coordinate(5,6,0),new Coordinate(6,5,0)));
	}
	
	//@Test
	public void testRotation(){
		
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(1,0,0), new Coordinate(0,0,1), Math.toRadians(90)));
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(1,0,0), new Coordinate(0,0,1), Math.toRadians(180)));
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(0,1,0), new Coordinate(0,0,1), Math.toRadians(90)));
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(0,-1,0), new Coordinate(0,0,1), Math.toRadians(90)));
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(0,-1,0), new Coordinate(0,0,1), Math.toRadians(-90)));
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(1,0,0), new Coordinate(0,1,0), Math.toRadians(90)));
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(1,0,0), new Coordinate(0,1,0), Math.toRadians(180)));
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(-1,0,0), new Coordinate(0,1,0), Math.toRadians(90)));
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(1,0,0), new Coordinate(0,1,0), Math.toRadians(-90)));
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(1,0,0), new Coordinate(0,1,0), Math.toRadians(180)));
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(0,0,1), new Coordinate(1,0,0), Math.toRadians(90)));
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(0,0,1), new Coordinate(0,1,0), Math.toRadians(90)));
		//System.out.println(CoordinateMath.rotate3D(new Coordinate(11,10,0), new Coordinate(10,10,15), new Coordinate(10,10,0), Math.toRadians(-90)));
		//System.out.println(CoordinateMath.rotate3Dn(new Coordinate(11,10,0), new Coordinate(10,10,15), new Coordinate(10,10,0), Math.toRadians(-90)));
		System.out.println(CoordinateMath.rotate3D(new Coordinate(0,0,-1), new Coordinate(0,1,0), new Coordinate(0,0,0), Math.toRadians(90)));
		System.out.println(CoordinateMath.rotate3D(new Coordinate(0,0.5,-1), new Coordinate(0,1.5,0), Math.toRadians(180)));
	}
	
	//@Test
	public void testReflection(){
		Coordinate c = CoordinateMath.reflect(xyz1, origin, z1);
		Assert.assertTrue(c.x == 1 && c.y == 1 && c.z == -1);
		c = CoordinateMath.reflect(xyz1, origin, x1);
		Assert.assertTrue(c.x == -1 && c.y == 1 && c.z == 1);
		c = CoordinateMath.reflect(xyz1, origin, y1);
		Assert.assertTrue(c.x == 1 && c.y == -1 && c.z == 1);
		c = CoordinateMath.reflect(xyz1, origin, xyz1);
		Assert.assertTrue(Math.abs(c.x-(-1))<1E-8 && Math.abs(c.y-(-1))<1E-8 && Math.abs(c.z-(-1))<1E-8);
		c = CoordinateMath.reflect(xyz1, new Coordinate(0,0,-1), z1);
		Assert.assertTrue(c.x == 1 && c.y == 1 && c.z == -3);
		c = CoordinateMath.reflect(x1, new Coordinate(5,0,0), x1);
		Assert.assertTrue(c.x == 9 && c.y == 0 && c.z == 0);		
	}
}
