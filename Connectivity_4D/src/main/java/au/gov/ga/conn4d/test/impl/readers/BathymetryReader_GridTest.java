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

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.impl.readers.Boundary_Raster_NetCDF;

public class BathymetryReader_GridTest {

	Boundary_Raster_NetCDF xbr,ybr,rbr;
	String xpath = "./files/bath_xmap.nc";
	String ypath = "./files/bath_ymap.nc";
	
	@Before
	public void setUp(){
		try {
			xbr = new Boundary_Raster_NetCDF(xpath,"Latitude","Longitude");
			ybr = new Boundary_Raster_NetCDF(ypath,"Latitude","Longitude");
			xbr.setPositiveDown(false);
			ybr.setPositiveDown(false);	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void valueXTest() {
		// Basic test of a surface where the value = x.
		for(float j = -1; j < 1; j+=0.04){
			Assert.assertEquals(j, xbr.getBoundaryDepth(j, 0),1E-6);
		}
	}
	
	@Test
	public void valueYTest(){
		// Basic test of a surface where the value = y.
		for(float i = -1; i < 1; i+=0.04){
			Assert.assertEquals(i, ybr.getBoundaryDepth(0,i),1E-6);
		}		
	}
}
