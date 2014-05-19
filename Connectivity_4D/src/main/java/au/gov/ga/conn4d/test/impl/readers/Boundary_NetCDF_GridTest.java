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

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

import au.gov.ga.conn4d.impl.readers.Boundary_Raster_NetCDF;

public class Boundary_NetCDF_GridTest {

	Boundary_Raster_NetCDF bng = null;
	
	@Before
	public void setUp(){
		try {
			bng = new Boundary_Raster_NetCDF("./files/bath_index.nc","Latitude","Longitude");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testVertices() {
		Assert.assertNull(bng.getVertices(new int[]{0,0}));
		Coordinate[] ca = bng.getVertices(new int[]{1,1});
		assertEquals(ca[0], new Coordinate(-1.96,-1.96,51));
		assertEquals(ca[1], new Coordinate(-1.92,-1.96,52));
		assertEquals(ca[2], new Coordinate(-1.92,-1.92,153));
		assertEquals(ca[3], new Coordinate(-1.96,-1.92,152));
	}
	
	@Test
	public void testIndices() {
		Assert.assertArrayEquals(new int[]{50, 50},bng.getIndices(0, 0));
		Assert.assertArrayEquals(new int[]{0, 0},bng.getIndices(-2, -2));
		Assert.assertArrayEquals(new int[]{0, 0},bng.getIndices(-1.96, -2));
		Assert.assertArrayEquals(new int[]{0, 1},bng.getIndices(-1.96+1E-12, -2));
		Assert.assertArrayEquals(new int[]{0, 1},bng.getIndices(-1.95, -2));
	}
}
