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

package au.gov.ga.conn4d.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import au.gov.ga.conn4d.utils.GeometryUtils;

public class GeometryUtilsTest {
	
	private double eps = 1E-6;

	@Test
	public void testProjectionConversions() {
		assertEquals(GeometryUtils.lonlat2ceqd(new double[]{1,0})[0], 111195.08, 0.1);
		assertEquals(GeometryUtils.lonlat2ceqd(new double[]{0,1})[1], 111195.08, 0.1);
		assertEquals(GeometryUtils.ceqd2lonlat(new double[]{111195.08,0})[0], 1, 0.001);
		assertEquals(GeometryUtils.ceqd2lonlat(new double[]{0,111195.08})[1], 1, 0.001);
		double[] input = new double[]{113.40437496675112,-26.983227605170704};
		double[] output = GeometryUtils.lonlat2ceqd(input);
		assertArrayEquals(new double[]{1.2610008969117487E7, -3000402.2527058693},output,eps);
		double[] backagain = GeometryUtils.ceqd2lonlat(output);
		assertArrayEquals(new double[]{113.40437496675112,-26.983227605170704},backagain,eps);
	}
	
	@Test
	public void testDistanceSphere(){
		double rln1 = 0;
		double rlt1 = 0; 
		double rln2 = 1;
		double rlt2 = 0;
		
		assertEquals(GeometryUtils.distance_Sphere(rln1, rlt1, rln2, rlt2),111195.08372419141,1E-9);
		assertEquals(GeometryUtils.distance_Sphere(0, 0, 0, 1),111195.08372419141,1E-9);
		assertEquals(GeometryUtils.distance_Sphere(0, 0, 1, 0),111195.08372419141,1E-9);
		assertEquals(GeometryUtils.distance_Sphere(0, 60, 1, 60),55597.0126102095,1E-9);
		assertEquals(GeometryUtils.distance_Sphere(-20, -30, -21, -31),146775.8885698756,1E-9);
		
	}
}
