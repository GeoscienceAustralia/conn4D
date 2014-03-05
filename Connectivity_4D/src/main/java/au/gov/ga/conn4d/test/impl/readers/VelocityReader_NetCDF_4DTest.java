package au.gov.ga.conn4d.test.impl.readers;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.impl.readers.VelocityReader_NetCDF_4D;

public class VelocityReader_NetCDF_4DTest {

	VelocityReader_NetCDF_4D v3 = new VelocityReader_NetCDF_4D();

	String uFile = "C:/Temp/Linear_x.nc";
	String vFile = "C:/Temp/Linear_y.nc";
	String wFile = "C:/Temp/Linear_z.nc";

	@Before
	public void setUp() throws Exception {
		v3.setUFile(uFile, "Variable_X");
		v3.setVFile(vFile, "Variable_Y");
		v3.setWFile(wFile, "Variable_Z");
		v3.setXLookup("Longitude");
		v3.setYLookup("Latitude");
		v3.setZLookup("Depth");
		v3.setTLookup("Time");
		v3.setTimeOffset(0);
	}

	@After
	public void tearDown() throws Exception {
		v3.close();
	}

	@Test
	public void testOrigin() {
		// At the origin
		Assert.assertArrayEquals(null, new double[] { 50.0, 50.0, 0.0 },
				v3.getVelocities(0, 0, 0, 0), 1E-6);
	}

	@Test
	public void testOriginAtDepth() {
		// Increment z by a half unit
		Assert.assertArrayEquals(null, new double[] { 50.0, 50.0, 5.0 },
				v3.getVelocities(0, 0.5, 0, 0), 1E-6);
	}

	@Test
	public void testBottomLeftCorner() {
		// Value at x=-1, y=-1, z = 0
		Assert.assertArrayEquals(null, new double[] { 0.0, 0.0, 0.0 },
				v3.getVelocities(0, 0, -1, -1), 1E-6);
	}

	@Test
	public void testUpperRightCorner() {
		// Value at x=1, y=1, z = 0
		Assert.assertArrayEquals(null, new double[] { 100.0, 100.0, 10.0 },
				v3.getVelocities(0, 1, 1, 1), 1E-6);
	}

	@Test
	public void testOutOfBoundsSouth() {
		// Value at x=1, y=-5, z = 0. Expect null
		Assert.assertNull(v3.getVelocities(0, 1, -5, 1));
	}

	@Test
	public void testOutOfBoundsNorth() {
		// Value at x=1, y=5, z = 0. Expect null
		Assert.assertNull(v3.getVelocities(0, 1, 5, 1));
	}

	@Test
	public void testOutOfBoundsWest() {
		// Value at x=1, y=-5, z = 0. Expect null
		Assert.assertNull(v3.getVelocities(0, 1, 1, -5));
	}

	@Test
	public void testOutOfBoundsEast() {
		// Value at x=1, y=5, z = 0. Expect null
		Assert.assertNull(v3.getVelocities(0, 1, 1, 5));
	}

	//@Test
	public void testOutOfBoundsUp() {
		// Value at x=0, y=0, z = 5 (beyond surface)
		Assert.assertArrayEquals(null, new double[] { 100.0, 100.0, 0.0 },
				v3.getVelocities(0, -5, 1, 1), 1E-6);
	}

	@Test
	public void testOutOfBoundsDown() {
		// Value at x=0, y=0, z = -5
		Assert.assertNull(v3.getVelocities(0, 5, 1, 1));
	}

	@Test
	public void testInterpolation() {
		Assert.assertArrayEquals(null, new double[] { 78.190, 89.245, 1.042 },
				v3.getVelocities(0, 0.1042, 0.5638, 0.7849), 1E-6);
	}

	@Test
	public void testBounds() {
		Assert.assertArrayEquals(new double[] { 0, 100 }, v3.getBounds()[0],
				1E-6);
		Assert.assertArrayEquals(new double[] { 0, 1 }, v3.getBounds()[1], 1E-6);
		Assert.assertArrayEquals(new double[] { -1, 1 }, v3.getBounds()[2],
				1E-6);
		Assert.assertArrayEquals(new double[] { -1, 1 }, v3.getBounds()[3],
				1E-6);
	}
}
