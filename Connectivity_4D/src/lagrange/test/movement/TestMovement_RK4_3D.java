package lagrange.test.movement;

import junit.framework.Assert;

import lagrange.Particle;
import lagrange.impl.movement.Advection_RK4_3D;
import lagrange.impl.readers.VelocityReader_NetCDF_4D;

import org.junit.Before;
import org.junit.Test;

public class TestMovement_RK4_3D {

	Advection_RK4_3D rk3d = new Advection_RK4_3D();
	VelocityReader_NetCDF_4D v3 = new VelocityReader_NetCDF_4D();
	Particle p;
	
	@Before
	public void setUp() throws Exception {
		v3.setUFile("C:/Temp/Ones.nc", "Variable X");
		v3.setVFile("C:/Temp/Ones.nc", "Variable X");
		v3.setWFile("C:/Temp/Ones.nc", "Variable X");
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
	public void test() {
		for(int i = 0; i < 100; i++){
			rk3d.apply(p);
			Assert.assertEquals((double) (i+1)/1000, p.getZ(), 1E-4);
		}
	}
}