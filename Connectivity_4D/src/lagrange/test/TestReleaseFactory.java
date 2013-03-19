package lagrange.test;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import lagrange.Parameters;
import lagrange.impl.Release;
import lagrange.impl.ReleaseFactory_4D;
import lagrange.impl.behavior.Mortality_None;
import lagrange.impl.behavior.Settlement_None;
import lagrange.impl.collision.CollisionDetector_None;
import lagrange.impl.movement.Diffusion_None;
import lagrange.impl.movement.Advection_RK4_3D;
import lagrange.impl.readers.VelocityReader_NetCDF_4D;
import lagrange.impl.writers.TrajectoryWriter_Text;
import lagrange.input.LocalParameters;
import lagrange.parameters.Parameters_Test;

public class TestReleaseFactory {
	
	ReleaseFactory_4D rf = new ReleaseFactory_4D();
	GeometryFactory gf = new GeometryFactory();
	
	public static void main(String[] args){
		TestReleaseFactory trf = new TestReleaseFactory();
		try {
			trf.setup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		trf.test();
		
	}
	
	@Before
	public void setup() throws Exception{
				
		// Set parameter values
		
		// Why set Release parameters using the Parameter class instead of directly?  
		// The thinking was that parameters could be sent out to client machines.  
		// Rather than bottlenecking at the source (the requester), we create copies
		// of the values that can then be distributed to the client machines.
		
		LocalParameters lp = new LocalParameters();
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
		rf.setDistanceWriter(new NullPlug());
		rf.setMatrixWriter(new NullPlug());
		rf.setSettlement(new Settlement_None());
		rf.setTrajectoryWriter(new TrajectoryWriter_Text("C:/Temp/testme.txt"));
		rf.setLocalParameters(new LocalParameters());
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rf.shutdown();
	}
}