package au.gov.ga.conn4d.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import au.gov.ga.conn4d.utils.CoordinateMath;

import com.vividsolutions.jts.geom.Coordinate;

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
		assertEquals(CoordinateMath.dot(new Coordinate(1,2,Double.NaN), new Coordinate(3,4,Double.NaN)),11,eps);
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
		try{
			CoordinateMath.midpoints(new Coordinate[]{origin});
			fail("IllegalArgumentException should have been thrown.");
		} catch (IllegalArgumentException e){
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

	/*
	 * @Test public void testNibble() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testNormal() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testNormal_zplus() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testNormalize() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testPlaneIntersection() { fail("Not yet implemented");
	 * }
	 * 
	 * @Test public void testPointInPoly2D() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testPointInPoly3D() { fail("Not yet implemented"); }
	 */
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

	/*
	 * @Test public void testRotate3DCoordinateCoordinateDouble() {
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(1,0,0), new
	 * Coordinate(0,0,1), Math.toRadians(90)));
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(1,0,0), new
	 * Coordinate(0,0,1), Math.toRadians(180)));
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(0,1,0), new
	 * Coordinate(0,0,1), Math.toRadians(90)));
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(0,-1,0), new
	 * Coordinate(0,0,1), Math.toRadians(90)));
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(0,-1,0), new
	 * Coordinate(0,0,1), Math.toRadians(-90)));
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(1,0,0), new
	 * Coordinate(0,1,0), Math.toRadians(90)));
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(1,0,0), new
	 * Coordinate(0,1,0), Math.toRadians(180)));
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(-1,0,0), new
	 * Coordinate(0,1,0), Math.toRadians(90)));
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(1,0,0), new
	 * Coordinate(0,1,0), Math.toRadians(-90)));
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(1,0,0), new
	 * Coordinate(0,1,0), Math.toRadians(180)));
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(0,0,1), new
	 * Coordinate(1,0,0), Math.toRadians(90)));
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(0,0,1), new
	 * Coordinate(0,1,0), Math.toRadians(90)));
	 * //System.out.println(CoordinateMath.rotate3D(new Coordinate(11,10,0), new
	 * Coordinate(10,10,15), new Coordinate(10,10,0), Math.toRadians(-90)));
	 * //System.out.println(CoordinateMath.rotate3Dn(new Coordinate(11,10,0),
	 * new Coordinate(10,10,15), new Coordinate(10,10,0), Math.toRadians(-90)));
	 * System.out.println(CoordinateMath.rotate3D(new Coordinate(0,0,-1), new
	 * Coordinate(0,1,0), new Coordinate(0,0,0), Math.toRadians(90)));
	 * System.out.println(CoordinateMath.rotate3D(new Coordinate(0,0.5,-1), new
	 * Coordinate(0,1.5,0), Math.toRadians(180)));
	 * 
	 * @Test public void testRotate3DnCoordinateCoordinateDouble() {
	 * fail("Not yet implemented"); }
	 * 
	 * @Test public void testRotate3DCoordinateCoordinateCoordinateDouble() {
	 * fail("Not yet implemented"); }
	 * 
	 * @Test public void testRotate3DnCoordinateCoordinateCoordinateDouble() {
	 * fail("Not yet implemented"); }
	 * 
	 * @Test public void testSignum() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testSubtract() { fail("Not yet implemented"); }
	 */
}
