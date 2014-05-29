/*******************************************************************************
 * Copyright 2014 Geoscience Australia (www.ga.gov.au)
 * @author - Johnathan Kool (Geoscience Australia)
 * 
 * Licensed under the BSD-3 License
 * 
 * http://opensource.org/licenses/BSD-3-Clause
 *  
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *  
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package au.gov.ga.conn4d.test.impl.readers;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.impl.readers.VelocityReader_NetCDF_4D;

public class VelocityReader_NetCDF_4DTest {

	VelocityReader_NetCDF_4D v3 = new VelocityReader_NetCDF_4D();

	String uFile = "./files/linear_x.nc";
	String vFile = "./files/linear_y.nc";
	String wFile = "./files/linear_z.nc";

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
		//Assert.assertArrayEquals(null, new double[] { 78.190, 89.245, 1.042 },
		//		v3.getVelocities(0, 0.1042, 0.5638, 0.7849), 1E-6);
		
		Assert.assertArrayEquals(null, new double[] { 78.094582, 95.764366, 1.9485152802 },
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
