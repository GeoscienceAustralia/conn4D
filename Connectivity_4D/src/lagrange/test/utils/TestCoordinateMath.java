package lagrange.test.utils;

import junit.framework.Assert;
import lagrange.utils.CoordinateMath;

import org.junit.Before;
import org.junit.Test;

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

	@Test
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
		
		System.out.println(Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,0,0), origin, new Coordinate(-1,0,0),z1)));
		System.out.println(Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,0,0), origin, new Coordinate(1,0,0),z1)));
		System.out.println(Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,0,0), origin, new Coordinate(-1,0,0),z2)));
		System.out.println(Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,0,0), origin, new Coordinate(1,0,0),z2)));
		
	}
}
