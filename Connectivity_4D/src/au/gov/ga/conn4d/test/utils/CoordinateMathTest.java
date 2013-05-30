package au.gov.ga.conn4d.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import au.gov.ga.conn4d.utils.CoordinateMath;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

public class CoordinateMathTest {

	private Coordinate origin = new Coordinate(0, 0, 0);
	private Coordinate x = new Coordinate(1, 0, 0);
	private Coordinate y = new Coordinate(0, 1, 0);
	private Coordinate z = new Coordinate(0, 0, 1);
	private Coordinate nx = new Coordinate(-1, 0, 0);
	private Coordinate ny = new Coordinate(0, -1, 0);
	private Coordinate nz = new Coordinate(0, 0, -1);
	private Coordinate xy = new Coordinate(1, 1, 0);
	private Coordinate xyz = new Coordinate(1, 1, 1);
	private Coordinate x2 = new Coordinate(2, 0, 0);
	private Coordinate y2 = new Coordinate(0, 2, 0);
	private Coordinate z2 = new Coordinate(0, 0, 2);
	private Coordinate[] xp = new Coordinate[] { y, z, origin };
	private Coordinate[] yp = new Coordinate[] { z, x, origin };
	private Coordinate[] zp = new Coordinate[] { x, y, origin };
	private Coordinate[] xpr = new Coordinate[] { origin, z, y };
	private Coordinate[] ypr = new Coordinate[] { origin, x, z };
	private Coordinate[] zpr = new Coordinate[] { origin, y, x };
	private double eps = 1E-8;

	@Test
	public void testAdd() {
		assertTrue(CoordinateMath.add(x, y).equals3D(new Coordinate(1, 1, 0)));
		assertTrue(CoordinateMath.add(x, z).equals3D(new Coordinate(1, 0, 1)));
		assertTrue(CoordinateMath.add(y, z).equals3D(new Coordinate(0, 1, 1)));
	}

	@Test
	public void testAngle3DSignedCoordinateCoordinateCoordinate() {
		// 90° x and z direction
		assertEquals(90d, Math.toDegrees(CoordinateMath.angle3DSigned(
				new Coordinate(-1, 0, 1), origin, new Coordinate(1, 0, 1), z)),
				1E-16);
		assertEquals(90d, Math.toDegrees(CoordinateMath.angle3DSigned(
				new Coordinate(1, 0, 1), origin, new Coordinate(-1, 0, 1), z)),
				1E-16);
		assertEquals(-90d,
				Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,
						0, 1), origin, new Coordinate(1, 0, 1), nz)), 1E-16);
		assertEquals(-90d,
				Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,
						0, 1), origin, new Coordinate(-1, 0, 1), nz)), 1E-16);
		assertEquals(-90d,
				Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,
						0, -1), origin, new Coordinate(1, 0, -1), z)), 1E-16);
		assertEquals(-90d,
				Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,
						0, -1), origin, new Coordinate(-1, 0, -1), z)), 1E-16);
		assertEquals(90d, Math.toDegrees(CoordinateMath
				.angle3DSigned(new Coordinate(-1, 0, -1), origin,
						new Coordinate(1, 0, -1), nz)), 1E-16);
		assertEquals(90d, Math.toDegrees(CoordinateMath
				.angle3DSigned(new Coordinate(1, 0, -1), origin,
						new Coordinate(-1, 0, -1), nz)), 1E-16);

		// 90° y and z direction

		assertEquals(90d, Math.toDegrees(CoordinateMath.angle3DSigned(
				new Coordinate(0, -1, 1), origin, new Coordinate(0, 1, 1), z)),
				1E-16);
		assertEquals(90d, Math.toDegrees(CoordinateMath.angle3DSigned(
				new Coordinate(0, 1, 1), origin, new Coordinate(0, -1, 1), z)),
				1E-16);
		assertEquals(-90d,
				Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(0,
						-1, 1), origin, new Coordinate(0, 1, 1), nz)), 1E-16);
		assertEquals(-90d,
				Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(0,
						1, 1), origin, new Coordinate(0, -1, 1), nz)), 1E-16);
		assertEquals(-90d,
				Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(0,
						-1, -1), origin, new Coordinate(0, 1, -1), z)), 1E-16);
		assertEquals(-90d,
				Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(0,
						1, -1), origin, new Coordinate(0, -1, -1), z)), 1E-16);
		assertEquals(90d, Math.toDegrees(CoordinateMath
				.angle3DSigned(new Coordinate(0, -1, -1), origin,
						new Coordinate(0, 1, -1), nz)), 1E-16);
		assertEquals(90d, Math.toDegrees(CoordinateMath
				.angle3DSigned(new Coordinate(0, 1, -1), origin,
						new Coordinate(0, -1, -1), nz)), 1E-16);

		// 90° x and y direction

		assertEquals(90d, Math.toDegrees(CoordinateMath.angle3DSigned(
				new Coordinate(1, -1, 0), origin, new Coordinate(1, 1, 0), x)),
				1E-16);
		assertEquals(90d, Math.toDegrees(CoordinateMath.angle3DSigned(
				new Coordinate(1, 1, 0), origin, new Coordinate(1, -1, 0), x)),
				1E-16);
		assertEquals(-90d,
				Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,
						-1, 0), origin, new Coordinate(1, 1, 0), nx)), 1E-16);
		assertEquals(-90d,
				Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(1,
						1, 0), origin, new Coordinate(1, -1, 0), nx)), 1E-16);
		assertEquals(-90d, Math.toDegrees(CoordinateMath
				.angle3DSigned(new Coordinate(-1, -1, 0), origin,
						new Coordinate(-1, 1, 0), ny)), 1E-16);
		assertEquals(90d, Math.toDegrees(CoordinateMath
				.angle3DSigned(new Coordinate(-1, 1, 0), origin,
						new Coordinate(-1, -1, 0), ny)), 1E-16);
		assertEquals(90d,
				Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,
						-1, 0), origin, new Coordinate(-1, 1, 0), y)), 1E-16);
		assertEquals(-90d,
				Math.toDegrees(CoordinateMath.angle3DSigned(new Coordinate(-1,
						1, 0), origin, new Coordinate(-1, -1, 0), y)), 1E-16);
	}

	// @Test public void
	// testAngle3DSignedCoordinateCoordinateCoordinateCoordinate() {
	// fail("Not yet implemented"); }

	@Test
	public void testAverageCoordinateCoordinate() {
		assertTrue(CoordinateMath.average(x, y).equals3D(
				new Coordinate(0.5, 0.5, 0)));
		assertTrue(CoordinateMath.average(x, z).equals3D(
				new Coordinate(0.5, 0, 0.5)));
		assertTrue(CoordinateMath.average(y, z).equals3D(
				new Coordinate(0, 0.5, 0.5)));
	}

	@Test
	public void testAverageCoordinateArray() {
		Coordinate[] ca1 = new Coordinate[] { x, y, z };
		Coordinate[] ca2 = new Coordinate[] { nx, ny, nz };
		Coordinate[] ca3 = new Coordinate[] { x, y, z, nx, ny, nz };
		Coordinate[] ca4 = new Coordinate[] { x, xy, xyz };
		double val = 1.0d / 3.0d;
		assertTrue(CoordinateMath.average(ca1).equals3D(
				new Coordinate(val, val, val)));
		assertTrue(CoordinateMath.average(ca2).equals3D(
				new Coordinate(-val, -val, -val)));
		assertTrue(CoordinateMath.average(ca3).equals3D(
				new Coordinate(0.0, 0.0, 0.0)));
		assertTrue(CoordinateMath.average(ca4).equals3D(
				new Coordinate(1.0, 2.0 * val, val)));
	}

	// @Test public void testCeqd2lonlat() { fail("Not yet implemented"); }

	@Test
	public void testCross() {
		assertTrue(CoordinateMath.cross(x2, y2).equals3D(
				new Coordinate(0, 0, 4)));
	}

	@Test
	public void testDilate() {
		assertTrue(CoordinateMath.dilate(xyz, Math.PI).equals3D(
				new Coordinate(Math.PI, Math.PI, Math.PI)));
		assertTrue(CoordinateMath.dilate(x2, Math.E).equals3D(
				new Coordinate(Math.E * 2, 0, 0)));
	}

	@Test
	public void testDot() {
		assertEquals(CoordinateMath.dot(new Coordinate(1, 2, Double.NaN),
				new Coordinate(3, 4, Double.NaN)), 11, eps);
		assertEquals(CoordinateMath.dot(x, y), 0, eps);
		assertEquals(CoordinateMath.dot(xy, xyz), 2, eps);
		Coordinate c1 = new Coordinate(1, 2, 3);
		Coordinate c2 = new Coordinate(4, 5, 6);
		assertEquals(CoordinateMath.dot(c1, c2), 32, eps);
	}

	@Test
	public void testLength3D() {
		assertEquals(CoordinateMath.length3D(origin, x), 1, eps);
		assertEquals(CoordinateMath.length3D(origin, y), 1, eps);
		assertEquals(CoordinateMath.length3D(origin, z), 1, eps);
		assertEquals(CoordinateMath.length3D(origin, x2), 2, eps);
		assertEquals(CoordinateMath.length3D(nx, x), 2, eps);
		assertEquals(CoordinateMath.length3D(xyz, xy), 1, eps);
	}

	// @Test public void testLonlat2ceqd() { fail("Not yet implemented"); }

	@Test
	public void testMagnitude() {
		assertEquals(CoordinateMath.magnitude(x), 1, eps);
		assertEquals(CoordinateMath.magnitude(y), 1, eps);
		assertEquals(CoordinateMath.magnitude(z), 1, eps);
		assertEquals(CoordinateMath.magnitude(xyz), Math.sqrt(3), eps);
	}

	@Test
	public void testMidpoints() {
		Coordinate c1 = new Coordinate(-2, -2, -2);
		Coordinate c2 = new Coordinate(-1, -1, -1);
		Coordinate c3 = origin;
		Coordinate c4 = xyz;
		Coordinate c5 = new Coordinate(2, 2, 2);
		Coordinate[] ca = new Coordinate[] { c1, c2, c3, c4, c5 };
		Coordinate m1 = new Coordinate(-1.5, -1.5, -1.5);
		Coordinate m2 = new Coordinate(-0.5, -0.5, -0.5);
		Coordinate m3 = new Coordinate(0.5, 0.5, 0.5);
		Coordinate m4 = new Coordinate(1.5, 1.5, 1.5);
		Coordinate[] cb = new Coordinate[] { m1, m2, m3, m4 };
		assertArrayEquals(CoordinateMath.midpoints(ca), cb);
		try {
			CoordinateMath.midpoints(new Coordinate[] { origin });
			fail("IllegalArgumentException should have been thrown.");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testNcross() {
		assertTrue(CoordinateMath.ncross(x2, y2).equals3D(
				new Coordinate(0, 0, 1)));
	}

	@Test
	public void testNegative() {
		assertEquals(CoordinateMath.negative(x), nx);
		assertEquals(CoordinateMath.negative(y), ny);
		assertEquals(CoordinateMath.negative(z), nz);
		assertEquals(CoordinateMath.negative(nx), x);
		assertEquals(CoordinateMath.negative(ny), y);
		assertEquals(CoordinateMath.negative(nz), z);
		assertEquals(CoordinateMath.negative(xyz), new Coordinate(-1, -1, -1));
	}

	@Test
	public void testNibble() {
		assertEquals(CoordinateMath.nibble(new LineSegment(nx, x), 1),
				new LineSegment(origin, x));
		assertEquals(CoordinateMath.nibble(new LineSegment(ny, y), 1),
				new LineSegment(origin, y));
		assertEquals(CoordinateMath.nibble(new LineSegment(nz, z), 1),
				new LineSegment(origin, z));
	}

	@Test
	public void testNormal() {
		assertEquals(CoordinateMath.normal(xp), new Coordinate(1, 0, 0));
		assertEquals(CoordinateMath.normal(yp), new Coordinate(0, 1, 0));
		assertEquals(CoordinateMath.normal(zp), new Coordinate(0, 0, 1));
		assertEquals(CoordinateMath.normal(xpr), new Coordinate(-1, 0, 0));
		assertEquals(CoordinateMath.normal(ypr), new Coordinate(0, -1, 0));
		assertEquals(CoordinateMath.normal(zpr), new Coordinate(0, 0, -1));
	}

	@Test
	public void testNormal_zplus() {
		Coordinate[] xp = new Coordinate[] { y, z, origin };
		Coordinate[] yp = new Coordinate[] { z, x, origin };
		Coordinate[] zp = new Coordinate[] { x, y, origin };
		Coordinate[] xpr = new Coordinate[] { origin, z, y };
		Coordinate[] ypr = new Coordinate[] { origin, x, z };
		Coordinate[] zpr = new Coordinate[] { origin, y, x };
		assertEquals(CoordinateMath.normal(xp), new Coordinate(1, 0, 0));
		assertEquals(CoordinateMath.normal(yp), new Coordinate(0, 1, 0));
		assertEquals(CoordinateMath.normal(zp), new Coordinate(0, 0, 1));
		assertEquals(CoordinateMath.normal(xpr), new Coordinate(-1, 0, 0));
		assertEquals(CoordinateMath.normal(ypr), new Coordinate(0, -1, 0));
		assertEquals(CoordinateMath.normal(zpr), new Coordinate(0, 0, 1));
	}

	@Test
	public void testNormalize() {
		assertEquals(CoordinateMath.normalize(x2), x);
		assertEquals(CoordinateMath.normalize(y2), y);
		assertEquals(CoordinateMath.normalize(z2), z);
		double sq3 = 1 / Math.sqrt(3);
		assertEquals(CoordinateMath.normalize(xyz), new Coordinate(sq3, sq3,
				sq3));
	}

	@Test
	public void testPlaneIntersection() {
		LineSegment xls = new LineSegment(xyz, new Coordinate(-1, 1, 1));
		assertEquals(CoordinateMath.planeIntersection(xls, xp), new Coordinate(
				0, 1, 1));
		LineSegment yls = new LineSegment(xyz, new Coordinate(1, -1, 1));
		assertEquals(CoordinateMath.planeIntersection(yls, yp), new Coordinate(
				1, 0, 1));
		LineSegment zls = new LineSegment(xyz, new Coordinate(1, 1, -1));
		assertEquals(CoordinateMath.planeIntersection(zls, zp), new Coordinate(
				1, 1, 0));
	}

	@Test
	public void testPointInPoly2D() {
		assertTrue(CoordinateMath.pointInPoly2D(new Coordinate(0.25, 0.25, 0),
				zp));
		assertFalse(CoordinateMath.pointInPoly2D(new Coordinate(1, 1, 0), zp));
	}

	@Test
	public void testPointInPoly3D() {
		assertTrue(CoordinateMath.pointInPoly3D(new Coordinate(0.25, 0.25, 0),
				zp));
		assertFalse(CoordinateMath.pointInPoly3D(new Coordinate(1, 1, 0), zp));
		assertTrue(CoordinateMath.pointInPoly3D(new Coordinate(0, 0.25, 0.25),
				xp));
		assertFalse(CoordinateMath.pointInPoly3D(new Coordinate(0, 1, 1), xp));
		assertTrue(CoordinateMath.pointInPoly3D(new Coordinate(0.25, 0, 0.25),
				yp));
		assertFalse(CoordinateMath.pointInPoly3D(new Coordinate(1, 0, 1), yp));
	}

	@Test
	public void testReflect() {
		Coordinate c = CoordinateMath.reflect(xyz, origin, z);
		assertTrue(c.x == 1 && c.y == 1 && c.z == -1);
		c = CoordinateMath.reflect(xyz, origin, x);
		assertTrue(c.x == -1 && c.y == 1 && c.z == 1);
		c = CoordinateMath.reflect(xyz, origin, y);
		assertTrue(c.x == 1 && c.y == -1 && c.z == 1);
		c = CoordinateMath.reflect(xyz, origin, xyz);
		assertTrue(Math.abs(c.x - (-1)) < 1E-8 && Math.abs(c.y - (-1)) < 1E-8
				&& Math.abs(c.z - (-1)) < 1E-8);
		c = CoordinateMath.reflect(xyz, new Coordinate(0, 0, -1), z);
		assertTrue(c.x == 1 && c.y == 1 && c.z == -3);
		c = CoordinateMath.reflect(x, new Coordinate(5, 0, 0), x);
		assertTrue(c.x == 9 && c.y == 0 && c.z == 0);
	}

	@Test
	public void testRotate3DCoordinateCoordinateDouble() {
		double d90 = Math.PI/2;
		double d180 = Math.PI;
		assertTrue(compare(CoordinateMath.rotate3D(x, z, d90),y,eps));
		assertTrue(compare(CoordinateMath.rotate3D(x, z, d180),nx,eps));
		assertTrue(compare(CoordinateMath.rotate3D(y, z, d90),nx,eps));
		assertTrue(compare(CoordinateMath.rotate3D(ny, z, d90),x,eps));
		assertTrue(compare(CoordinateMath.rotate3D(ny, z, -d90),nx,eps));
		assertTrue(compare(CoordinateMath.rotate3D(x, y, d90),nz,eps));
		assertTrue(compare(CoordinateMath.rotate3D(x, y, d180),nx,eps));
		assertTrue(compare(CoordinateMath.rotate3D(nx, y, d90),z,eps));
		assertTrue(compare(CoordinateMath.rotate3D(x, y, -d90),z,eps));
		assertTrue(compare(CoordinateMath.rotate3D(z, x, d90),ny,eps));
		assertTrue(compare(CoordinateMath.rotate3D(z, y, d90),x,eps));
	
/*
		System.out.println(CoordinateMath.rotate3D(new Coordinate(11, 10, 0),
				new Coordinate(10, 10, 15), new Coordinate(10, 10, 0),
				Math.toRadians(-90)));
		System.out.println(CoordinateMath.rotate3Dn(new Coordinate(11, 10, 0),
				new Coordinate(10, 10, 15), new Coordinate(10, 10, 0),
				Math.toRadians(-90)));
		System.out.println(CoordinateMath.rotate3D(new Coordinate(0, 0, -1),
				new Coordinate(0, 1, 0), new Coordinate(0, 0, 0),
				Math.toRadians(90)));
		System.out.println(CoordinateMath.rotate3D(new Coordinate(0, 0.5, -1),
				new Coordinate(0, 1.5, 0), Math.toRadians(180)));*/
	}

	/*
	 * @Test public void testRotate3DnCoordinateCoordinateDouble() {
	 * fail("Not yet implemented"); }
	 * 
	 * @Test public void testRotate3DCoordinateCoordinateCoordinateDouble() {
	 * fail("Not yet implemented"); }
	 * 
	 * @Test public void testRotate3DnCoordinateCoordinateCoordinateDouble() {
	 * fail("Not yet implemented"); }
	 */

	@Test
	public void testSignum() {
		assertEquals(CoordinateMath.signum(new Coordinate(-32, 0, 5)),
				new Coordinate(-1, 0, 1));
		assertEquals(CoordinateMath.signum(new Coordinate(0, -88, -Math.PI)),
				new Coordinate(0, -1, -1));
		assertEquals(CoordinateMath.signum(new Coordinate(Math.E, 42, -0)),
				new Coordinate(1, 1, 0));
	}

	@Test
	public void testSubtract() {
		assertEquals(CoordinateMath.subtract(origin, x), nx);
		assertEquals(CoordinateMath.subtract(origin, y), ny);
		assertEquals(CoordinateMath.subtract(origin, z), nz);
		assertEquals(CoordinateMath.subtract(xyz, x), new Coordinate(0, 1, 1));
	}
	
	private boolean compare(Coordinate a, Coordinate b, double eps){
		if(Math.abs(a.x-b.x)>eps){return false;}
		if(Math.abs(a.y-b.y)>eps){return false;}
		if(Math.abs(a.z-b.z)>eps){return false;}
		return true;
	}

}
