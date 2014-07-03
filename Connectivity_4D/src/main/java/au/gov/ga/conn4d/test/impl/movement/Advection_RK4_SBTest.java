package au.gov.ga.conn4d.test.impl.movement;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.impl.movement.Advection_RK4_SB;
import au.gov.ga.conn4d.impl.readers.VelocityReader_Constant;
import au.gov.ga.conn4d.impl.readers.VelocityReader_NetCDF_4D;

public class Advection_RK4_SBTest {

	Advection_RK4_SB rk3d_net = new Advection_RK4_SB();
	Advection_RK4_SB rk3d_inc = new Advection_RK4_SB();
	Advection_RK4_SB rk3d_none = new Advection_RK4_SB();
	VelocityReader_NetCDF_4D v3_net = new VelocityReader_NetCDF_4D();
	VelocityReader_TestPlug v3_tan = new VelocityReader_TestPlug();
	VelocityReader_Constant v3_11n1 = new VelocityReader_Constant();
	VelocityReader_Constant v3_00n1 = new VelocityReader_Constant();
	VelocityReader_Constant v3_none = new VelocityReader_Constant();
	Particle p;

	@Before
	public void setUp() throws Exception {
		v3_net.setUFile("./files/ones.nc", "Variable X");
		v3_net.setVFile("./files/ones.nc", "Variable X");
		v3_net.setWFile("./files/zeros.nc", "Variable X");
		v3_net.setXLookup("Longitude");
		v3_net.setYLookup("Latitude");
		v3_net.setZLookup("Depth");
		v3_net.setTLookup("Time");
		v3_net.setTimeOffset(0);
		v3_none.setVelocities(new double[]{0,0,0});
		v3_11n1.setVelocities(new double[]{1,1,-1});
		rk3d_net.setVr(v3_net);
		rk3d_inc.setVr(v3_tan);
		rk3d_none.setVr(v3_none);
		rk3d_net.setH(1000);
		rk3d_inc.setH(200f);
		rk3d_none.setH(1000);
		rk3d_none.getFluidPhysics().setFluidDensity(1.25);
		p = new Particle();
		p.setT(0);
		p.setX(0);
		p.setY(0);
		p.setZ(0);
		p.setDensity(6);
	}

	/**
	 * The position of the particle should change in a linear manner of one unit
	 * per thousand time steps. i.e. at t=1000, the particle should be at
	 * position 1 etc. Note, the code automatically converts X and Y to Latitude
	 * and Longitude, therefore this needs to be accounted for.
	 */

	@Test
	public void testConstantVelocity() {
		for (int i = 0; i < 10; i++) {
			rk3d_none.apply(p);
			System.out.println(p.getZ());
		}
	}

	/**
	 * Test is based on using the function -t^2-1 which gives an analytical
	 * result of -tan(t), verified using Wolfram Alpha www.wolframalpha.com
	 */
	// @Test
	public void testIncreasingVelocity() {
		Particle p = new Particle();
		p.setX(0);
		p.setY(0);
		p.setZ(0);
		rk3d_inc.apply(p);
		assertEquals(p.getZ(), -Math.tan(0.2), 1E-2);
		rk3d_inc.apply(p);
		assertEquals(p.getZ(), -Math.tan(0.4), 1E-2);
		rk3d_inc.apply(p);
		assertEquals(p.getZ(), -Math.tan(0.6), 1E-2);
		rk3d_inc.apply(p);
		assertEquals(p.getZ(), -Math.tan(0.8), 1E-2);
		rk3d_inc.apply(p);
		assertEquals(p.getZ(), -Math.tan(1.0), 1E-2);
		rk3d_inc.apply(p);
		assertEquals(p.getZ(), -Math.tan(1.2), 1E-2);
		rk3d_inc.apply(p);
	}
}
