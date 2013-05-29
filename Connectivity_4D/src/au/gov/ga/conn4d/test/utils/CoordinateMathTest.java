package au.gov.ga.conn4d.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
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
		Coordinate[] ca1 = new Coordinate[]{x,y,z};
		Coordinate[] ca2 = new Coordinate[]{nx,ny,nz};
		Coordinate[] ca3 = new Coordinate[]{x,y,z,nx,ny,nz};
		Coordinate[] ca4 = new Coordinate[]{x,xy,xyz};
		double val = 1.0d/3.0d;
		assertTrue(CoordinateMath.average(ca1).equals3D(new Coordinate(val,val,val)));
		assertTrue(CoordinateMath.average(ca2).equals3D(new Coordinate(-val,-val,-val)));
		assertTrue(CoordinateMath.average(ca3).equals3D(new Coordinate(0.0,0.0,0.0)));
		assertTrue(CoordinateMath.average(ca4).equals3D(new Coordinate(1.0,2.0*val,val)));
	}

	/*
	 * @Test public void testCeqd2lonlat() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testCross() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testDilate() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testDot() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testLength3D() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testLonlat2ceqd() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testMagnitude() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testMidpoints() { fail("Not yet implemented"); }
	 */
	@Test
	public void testNcross() {
		System.out.println(CoordinateMath.ncross(new Coordinate(0, 1, 0),
				new Coordinate(1, 0, 0)));
		System.out.println(CoordinateMath.ncross(new Coordinate(5, 6, 0),
				new Coordinate(6, 5, 0)));
	}

	/*
	 * @Test public void testNegative() { fail("Not yet implemented"); }
	 * 
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
		Assert.assertTrue(c.x == 1 && c.y == 1 && c.z == -1);
		c = CoordinateMath.reflect(xyz, origin, x);
		Assert.assertTrue(c.x == -1 && c.y == 1 && c.z == 1);
		c = CoordinateMath.reflect(xyz, origin, y);
		Assert.assertTrue(c.x == 1 && c.y == -1 && c.z == 1);
		c = CoordinateMath.reflect(xyz, origin, xyz);
		Assert.assertTrue(Math.abs(c.x - (-1)) < 1E-8
				&& Math.abs(c.y - (-1)) < 1E-8 && Math.abs(c.z - (-1)) < 1E-8);
		c = CoordinateMath.reflect(xyz, new Coordinate(0, 0, -1), z);
		Assert.assertTrue(c.x == 1 && c.y == 1 && c.z == -3);
		c = CoordinateMath.reflect(x, new Coordinate(5, 0, 0), x);
		Assert.assertTrue(c.x == 9 && c.y == 0 && c.z == 0);
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