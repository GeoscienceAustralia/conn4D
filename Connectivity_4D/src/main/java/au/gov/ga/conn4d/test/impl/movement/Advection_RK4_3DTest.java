package au.gov.ga.conn4d.test.impl.movement;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.impl.movement.Advection_RK4_3D;
import au.gov.ga.conn4d.impl.readers.VelocityReader_NetCDF_4D;

/**
 * Tests Advection_RK4_3D - 3D Runge-Kutta integrated advection.
 */

public class Advection_RK4_3DTest {

	Advection_RK4_3D rk3d = new Advection_RK4_3D();
	VelocityReader_NetCDF_4D v3 = new VelocityReader_NetCDF_4D();
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
		rk3d.setH(1); // Integration interval of one millisecond
		p = new Particle();
		p.setT(0);
		p.setX(-1);
		p.setY(-1);
		p.setZ(0);
	}

	@Test
	
	/**
	 * The position of the particle should change in a linear manner of
	 * one unit per thousand time steps.  i.e. at t=1000, the particle
	 * should be at position 1 etc.  Note, the code automatically converts
	 * X and Y to Latitude and Longitude, therefore this needs to be
	 * accounted for.
	 */
	
	public void test() {
		for(int i = 0; i < 100; i++){
			rk3d.apply(p);
			Assert.assertEquals((double) -(i+1)/1000, p.getZ(), 1E-4);
		}
	}
}
