package lagrange.test.collision;

import java.io.IOException;
import java.util.Arrays;

import lagrange.Particle;
import lagrange.impl.collision.CollisionDetector_3D_Bathymetry;
import lagrange.impl.readers.Boundary_NetCDF_Grid;
import lagrange.utils.PrjTransform_None;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestCollisionDetection_3D_Bathymetry {
	
	CollisionDetector_3D_Bathymetry cdb_x, cdb_xr, cdb_y, cdbreal;
	Boundary_NetCDF_Grid gx, gy, gxr, greal;

	@Before
	public void setUp(){
		
		try {
			gx = new Boundary_NetCDF_Grid("C:/Temp/bathym_increasing_x.nc","Latitude","Longitude");
			gxr = new Boundary_NetCDF_Grid("C:/Temp/bathym_decreasing_x.nc","Latitude","Longitude");
			gy = new Boundary_NetCDF_Grid("C:/Temp/bathym_increasing_y.nc","Latitude","Longitude");
			greal = new Boundary_NetCDF_Grid("C:/Temp/aus_bath_lite.nc","Latitude","Longitude");
			greal.setPositiveDown(false);

			cdb_x = new CollisionDetector_3D_Bathymetry(gx);
			cdb_x.setProjectionTransform(new PrjTransform_None());
			
			cdb_xr = new CollisionDetector_3D_Bathymetry(gxr);
			cdb_xr.setProjectionTransform(new PrjTransform_None());
			
			cdb_y = new CollisionDetector_3D_Bathymetry(gy);
			cdb_y.setProjectionTransform(new PrjTransform_None());

			cdbreal = new CollisionDetector_3D_Bathymetry(greal);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testXSlopeZLine() {
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0);	p1.setPZ(-9); p1.setX(0); p1.setY(0); p1.setZ(-11); 
		cdb_x.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	    Assert.assertArrayEquals(new double[]{-1,0,-10},da ,1E-1);// precision decrease due to use of meters
	}
	
	//@Test
	public void testXSlopeOffsetZLine(){
		Particle p1 = new Particle();
		p1.setPX(5); p1.setPY(5);	p1.setPZ(-4); p1.setX(5); p1.setY(5); p1.setZ(-6); 
		cdb_x.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	    Assert.assertArrayEquals(new double[]{4,5,-5},da ,1E-1);// precision decrease due to use of meters
	}
	
	//@Test
	public void testXReverseSlopeZLine() {
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0);	p1.setPZ(-9); p1.setX(0); p1.setY(0); p1.setZ(-11); 
		cdb_xr.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	    Assert.assertArrayEquals(new double[]{1,0,-10},da ,1E-1);// precision decrease due to use of meters
	}
	
	//@Test
	public void testYSlopeZLine() {
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0);	p1.setPZ(-9); p1.setX(0); p1.setY(0); p1.setZ(-11); 
		cdb_y.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	    Assert.assertArrayEquals(new double[]{0,-1,-10},da ,1E-1);// precision decrease due to use of meters
	}
	
	@Test
	public void testRealProblem(){
		Particle p1 = new Particle();
		//p1.setPX(113.1314548954153); p1.setPY(-25.15381371021109); p1.setPZ(0.0); p1.setX(113.1206995353216); p1.setY(-25.1427262565772); p1.setZ(0.0);
		//p1.setPX(119.2955883059722); p1.setPY(-19.967516812656964); p1.setPZ(0.0); p1.setX(119.28825081656038); p1.setY(-19.973046751359398); p1.setZ(0.0);
		p1.setPX(118.90337209729493); p1.setPY(-19.924273157502153); p1.setPZ(0.0); p1.setX(118.92530884302202); p1.setY(-19.938343466933144); p1.setZ(-0.724323105532676);
		//p1.setPX(119.6114368822359); p1.setPY(-20.0045209399733885); p1.setPZ(0.0); p1.setX(113.1206995353216); p1.setY(-25.1427262565772); p1.setZ(0.0);
		
		
		int[] indices = greal.getIndices(p1.getPX(), p1.getPY()); 
		System.out.println(Arrays.toString(indices));
		cdbreal.handleIntersection(p1);
	}
	
	public static void main(String[] args){
		TestCollisionDetection_3D_Bathymetry t = new TestCollisionDetection_3D_Bathymetry();
		t.setUp();
		t.testRealProblem();
	}
}
