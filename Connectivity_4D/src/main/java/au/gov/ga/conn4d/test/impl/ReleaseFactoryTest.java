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

package au.gov.ga.conn4d.test.impl;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.impl.Release;
import au.gov.ga.conn4d.impl.ReleaseFactory_4D;
import au.gov.ga.conn4d.impl.behavior.Mortality_None;
import au.gov.ga.conn4d.impl.behavior.Settlement_None;
import au.gov.ga.conn4d.impl.collision.CollisionDetector_None;
import au.gov.ga.conn4d.impl.movement.Advection_RK4_3D;
import au.gov.ga.conn4d.impl.movement.Diffusion_None;
import au.gov.ga.conn4d.impl.readers.VelocityReader_NetCDF_4D;
import au.gov.ga.conn4d.impl.writers.TrajectoryWriter_Text;
import au.gov.ga.conn4d.input.EnvironmentParameters;
import au.gov.ga.conn4d.parameters.Parameters_Test;
//import au.gov.ga.conn4d.test.NullPlug;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class ReleaseFactoryTest {
	
	ReleaseFactory_4D rf = new ReleaseFactory_4D();
	GeometryFactory gf = new GeometryFactory();
	
	@Before
	public void setup() throws Exception{
				
		// Set parameter values
		
		// Why set Release parameters using the Parameter class instead of directly?  
		// The thinking was that parameters could be sent out to client machines.  
		// Rather than bottlenecking at the source (the requester), we create copies
		// of the values that can then be distributed to the client machines.
		
		EnvironmentParameters lp = new EnvironmentParameters();
		lp.ufile = "C:/Temp/Ones.nc";
		lp.uname = "Variable X";
		lp.vfile = "C:/Temp/Ones.nc";
		lp.vname = "Variable X";
		lp.wfile = "C:/Temp/Ones.nc";
		lp.wname = "Variable X";
		
		// Construct the VelocityReader
		VelocityReader_NetCDF_4D nvr = new VelocityReader_NetCDF_4D();
		nvr.setUFile(lp.ufile, lp.uname);
		nvr.setVFile(lp.vfile, lp.vname);
		nvr.setWFile(lp.wfile, lp.wname);		
		nvr.setXLookup("Longitude");
		nvr.setYLookup("Latitude");
		nvr.setZLookup("Depth");
		nvr.setTLookup("Time");
		nvr.setTimeOffset(0);
		
		rf.setVelocityReader(nvr); //TODO Setting in two places? (1)
		
		// Construct the Movement function
		Advection_RK4_3D rk4 = new Advection_RK4_3D();
		rk4.setVr(nvr);            //TODO Setting in two places? (2)
		rk4.setH(10);
		rf.setMovement(rk4);	
		rf.setDiffusion(new Diffusion_None());
		rf.setMortality(new Mortality_None());
		rf.setCollisionDetection(new CollisionDetector_None());
		//rf.setDistanceWriter(new NullPlug());
		//rf.setMatrixWriter(new NullPlug());
		rf.setSettlement(new Settlement_None());
		rf.setTrajectoryWriter(new TrajectoryWriter_Text("C:/Temp/testme.txt"));
		rf.setLocalParameters(new EnvironmentParameters());
	}
	
	@Test
	public void test(){
		Parameters prm = new Parameters_Test();
		prm.setDepth(0);
		prm.setLocName("Test");
		prm.setRelDuration(100);
		prm.setPosition(gf.createPoint(new Coordinate(0,0,0)));
		prm.setH(10);
		int n = 5;
		CountDownLatch doneSignal = new CountDownLatch(n);
		
		for (int i = 0; i < n; i++){
			Release r = rf.generate();
			r.setParameters(prm);
			r.setDoneSignal(doneSignal);
			Thread th = new Thread(r);
			th.start();
		}
		
		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		rf.shutdown();
	}
}
