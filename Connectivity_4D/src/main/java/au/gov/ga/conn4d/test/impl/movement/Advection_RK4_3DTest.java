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

package au.gov.ga.conn4d.test.impl.movement;

import static org.junit.Assert.*;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.impl.movement.Advection_RK4_3D;
import au.gov.ga.conn4d.impl.readers.VelocityReader_Constant;
//import au.gov.ga.conn4d.impl.movement.Advection_RK4_3Dv2;
import au.gov.ga.conn4d.impl.readers.VelocityReader_NetCDF_4D;

/**
 * Tests Advection_RK4_3D - 3D Runge-Kutta integrated advection.
 */

public class Advection_RK4_3DTest {

	Advection_RK4_3D rk3d = new Advection_RK4_3D();
	Advection_RK4_3D rk3d_inc = new Advection_RK4_3D();
	Advection_RK4_3D rk_con = new Advection_RK4_3D();
	VelocityReader_NetCDF_4D v3 = new VelocityReader_NetCDF_4D();
	VelocityReader_TestPlug v3t = new VelocityReader_TestPlug();
	VelocityReader_Constant vcon = new VelocityReader_Constant();
	Particle p;
	
	@Before
	public void setUp() throws Exception {
		v3.setUFile("./files/ones.nc", "Variable X");
		v3.setVFile("./files/ones.nc", "Variable X");
		v3.setWFile("./files/negative_ones.nc", "Variable_X");
		v3.setXLookup("Longitude");
		v3.setYLookup("Latitude");
		v3.setZLookup("Depth");
		v3.setTLookup("Time");
		v3.setTimeOffset(0);
		rk3d.setVr(v3);
		rk_con.setVr(vcon);
		rk3d_inc.setVr(v3t);
		rk3d.setH(1); // Integration interval of one millisecond
		rk_con.setH(1000);
		rk3d_inc.setH(200f);
		p = new Particle();
		p.setT(0);
		p.setX(-1);
		p.setY(-1);
		p.setZ(0);
	}
	
	/**
	 * The position of the particle should change in a linear manner of
	 * one unit per thousand time steps.  i.e. at t=1000, the particle
	 * should be at position 1 etc.  Note, the code automatically converts
	 * X and Y to Latitude and Longitude, therefore this needs to be
	 * accounted for.
	 */
	
	@Test
	public void testConstantVelocity() {
		for(int i = 0; i < 100; i++){
			rk3d.apply(p);
			Assert.assertEquals((double) -(i+1)/1000, p.getZ(), 1E-4);
		}
	}
	
	/**
	 * Test distance travelled using 1m/s velocity at different lons and lats.
	 */
	
	@Test
	public void testDistance() {

		Particle p = new Particle();
		p.setX(0);
		p.setY(0);
		p.setZ(0);

		vcon.setVelocities(new double[]{1,0,0});
		rk_con.apply(p);
		Assert.assertEquals(8.99280575E-6, p.getX(), 1E-9);

		p.setX(0);
		p.setY(0);
		p.setZ(0);
		
		vcon.setVelocities(new double[]{0,1,0});
		rk_con.apply(p);
		Assert.assertEquals(8.99280575E-6, p.getY(), 1E-9);
		
		p.setX(0);
		p.setY(0);
		p.setZ(0);
		
		vcon.setVelocities(new double[]{1,1,0});
		rk_con.apply(p);
		Assert.assertEquals(8.99280575E-6, p.getX(), 1E-9);
		Assert.assertEquals(8.99280575E-6, p.getY(), 1E-9);
			
		p.setX(0);
		p.setY(60);
		p.setZ(0);
		vcon.setVelocities(new double[]{1,0,0});
		rk_con.apply(p);
		Assert.assertEquals(1.7986406E-5, p.getX(), 1E-9);	
	}
	
	/**
	 * Test is based on using the function -t^2-1 which gives an analytical result of -tan(t), verified using
	 * Wolfram Alpha www.wolframalpha.com
	 */
	@Test 
	public void testIncreasingVelocity(){
		Particle p = new Particle();
		p.setX(0);
		p.setY(0);
		p.setZ(0);
		rk3d_inc.apply(p);
		assertEquals(p.getZ(),-Math.tan(0.2),1E-2);
		rk3d_inc.apply(p);
		assertEquals(p.getZ(),-Math.tan(0.4),1E-2);		
		rk3d_inc.apply(p);
		assertEquals(p.getZ(),-Math.tan(0.6),1E-2);
		rk3d_inc.apply(p);
		assertEquals(p.getZ(),-Math.tan(0.8),1E-2);
		rk3d_inc.apply(p);
		assertEquals(p.getZ(),-Math.tan(1.0),1E-2);
		rk3d_inc.apply(p);
		assertEquals(p.getZ(),-Math.tan(1.2),1E-2);
		rk3d_inc.apply(p);
	}
}
