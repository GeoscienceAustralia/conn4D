package au.gov.ga.conn4d.test.impl;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.impl.Release;
import au.gov.ga.conn4d.impl.behavior.Settlement_None;
import au.gov.ga.conn4d.impl.collision.CollisionDetector_None;
import au.gov.ga.conn4d.impl.movement.Advection_RK4_3D;
import au.gov.ga.conn4d.impl.movement.Diffusion_None;
import au.gov.ga.conn4d.impl.readers.VelocityReader_NetCDF_4D;
import au.gov.ga.conn4d.parameters.Parameters_Test;
import au.gov.ga.conn4d.test.NullPlug;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class ReleaseTest {

	Release r = new Release();
	Advection_RK4_3D rk3d = new Advection_RK4_3D();
	VelocityReader_NetCDF_4D v3 = new VelocityReader_NetCDF_4D();
	String uFile = "C:/Temp/Ones.nc";//"C:/Temp/im_y.nc";
	String vFile = "C:/Temp/Ones.nc";//"C:/Temp/im_x.nc";
	String wFile = "C:/Temp/Ones.nc";//"C:/Temp/im_z.nc";
	Parameters prm = new Parameters_Test();
	GeometryFactory gf = new GeometryFactory();
	
	@Before
	public void setUp() throws Exception {
		// Build the velocity readerr
		v3.setUFile(uFile, "Variable X");
		v3.setVFile(vFile, "Variable X");
		v3.setWFile(wFile, "Variable X");
		v3.setXLookup("Longitude");
		v3.setYLookup("Latitude");
		v3.setZLookup("Depth");
		v3.setTLookup("Time");
		v3.setTimeOffset(0);
		
		// Attach the velocity reader to the Movement class, and set
		// h, the minimum integration time step.
		
		rk3d.setVr(v3);
		rk3d.setH(10);
		
		// Set parameter values
		
		// Why set Release parameters using the Parameter class instead of directly?  
		// The thinking was that parameters could be sent out to client machines.  
		// Rather than bottlenecking at the source (the requester), we create copies
		// of the values that can then be distributed to the client machines.
		
		prm.setDepth(0);
		prm.setLocName("Test");
		prm.setRelDuration(100);
		prm.setPosition(gf.createPoint(new Coordinate(0,0.5)));
		prm.setH(10); // TODO Reconcile how H gets set.  i.e. only set it in one place.
		
		// Set the more complex release components - Null here for testing.
		
		r.setParameters(prm);
		r.setCollisionDetector(new CollisionDetector_None());
		r.setDiffusion(new Diffusion_None());
		r.setMortality(new NullPlug());
		r.setDistanceWriter(new NullPlug());
		r.setMatrixWriter(new NullPlug());
		r.setMovement(rk3d);
		r.setSettlement(new Settlement_None());
		r.setTrajectoryWriter(new NullPlug());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		r.run();
	}
}
