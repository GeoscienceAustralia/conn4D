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

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.impl.readers.VelocityReader_HYCOMList_4D;

public class VelocityReader_HYCOMList_4DTest {

	private String dir = "G:/Blocks";
	private VelocityReader_HYCOMList_4D ncl;
	private double eps = 1E-2;
	
	@Before
	public void setUp(){
		try {
			ncl = new VelocityReader_HYCOMList_4D();
			ncl.setTName("Time");
			ncl.setZName("Depth");
			ncl.setLatName("Latitude");
			ncl.setLonName("Longitude");
			ncl.initialize(dir);
			ncl.setTLookup("Time");
			ncl.setZLookup("Depth");
			ncl.setXLookup("Longitude");
			ncl.setYLookup("Latitude");
			ncl.setUName("u");
			ncl.setVName("v");
			ncl.setWName("w");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetVelocities() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat formatUTC = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ssZ");
		formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		c.set(Calendar.YEAR, 2009);
		c.set(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		double[] vels = ncl.getVelocities(c.getTimeInMillis(), -10, 100.08, -27.090);
		assertArrayEquals(vels,new double[]{-0.09442, 0.01034, -2.60986E-05},eps);
		c.set(Calendar.DAY_OF_YEAR,91);
		vels = ncl.getVelocities(c.getTimeInMillis(), -100, 150, -47.8);
		assertArrayEquals(vels,new double[]{0.37643, -0.17394, -2.20267E-05},eps);
	}
	
	@After
	public void testClose(){
		ncl.close();
	}
}
