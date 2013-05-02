package lagrange.test.collision;

import java.io.IOException;
import java.util.Arrays;

import lagrange.Boundary;
import lagrange.Particle;
import lagrange.impl.collision.CollisionDetector_3D_Bathymetry;
import lagrange.impl.readers.Boundary_Grid_NetCDF;
import lagrange.utils.PrjTransform_None;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

public class TestCollisionDetection_3D_Bathymetry {
	
	CollisionDetector_3D_Bathymetry cdb_x, cdb_xr, cdb_y, cdbreal;
	CollisionDetector_3D_Bathymetry cdb_e1, cdb_e2, cdb_ex, cdb_eo;
	Boundary gx, gy, gxr, greal;
	Boundary_Grid_TestingGrid f1,f2,fx, fo;
	
	Coordinate a1 = new Coordinate(-1,1,1);
	Coordinate a2 = new Coordinate(-1,0,1);
	Coordinate a3 = new Coordinate(-1,-1,1);
	Coordinate a4 = new Coordinate(0,1,0);
	Coordinate a5 = new Coordinate(0,0,0);
	Coordinate a6 = new Coordinate(0,-1,0);
	Coordinate a7 = new Coordinate(1,1,1);
	Coordinate a8 = new Coordinate(1,0,1);
	Coordinate a9 = new Coordinate(1,-1,1);
	Coordinate[] sq1_1 = new Coordinate[]{a1,a2,a5,a4};
	Coordinate[] sq1_2 = new Coordinate[]{a2,a3,a6,a5};
	Coordinate[] sq1_3 = new Coordinate[]{a4,a5,a8,a7};
	Coordinate[] sq1_4 = new Coordinate[]{a5,a6,a9,a8};
	
	Coordinate b1 = new Coordinate(-1,1,1);
	Coordinate b2 = new Coordinate(-1,0,0);
	Coordinate b3 = new Coordinate(-1,-1,1);
	Coordinate b4 = new Coordinate(0,1,1);
	Coordinate b5 = new Coordinate(0,0,0);
	Coordinate b6 = new Coordinate(0,-1,1);
	Coordinate b7 = new Coordinate(1,1,1);
	Coordinate b8 = new Coordinate(1,0,0);
	Coordinate b9 = new Coordinate(1,-1,1);
	Coordinate[] sq2_1 = new Coordinate[]{b1,b2,b5,b4};
	Coordinate[] sq2_2 = new Coordinate[]{b2,b3,b6,b5};
	Coordinate[] sq2_3 = new Coordinate[]{b4,b5,b8,b7};
	Coordinate[] sq2_4 = new Coordinate[]{b5,b6,b9,b8};
	
	Coordinate c1 = new Coordinate(-1,1,-1);
	Coordinate c2 = new Coordinate(-1,0,-1);
	Coordinate c3 = new Coordinate(-1,-1,-1);
	Coordinate c4 = new Coordinate(0,1,0);
	Coordinate c5 = new Coordinate(0,0,0);
	Coordinate c6 = new Coordinate(0,-1,0);
	Coordinate c7 = new Coordinate(1,1,-1);
	Coordinate c8 = new Coordinate(1,0,-1);
	Coordinate c9 = new Coordinate(1,-1,-1);
	Coordinate[] sq3_1 = new Coordinate[]{c1,c2,c5,c4};
	Coordinate[] sq3_2 = new Coordinate[]{c2,c3,c6,c5};
	Coordinate[] sq3_3 = new Coordinate[]{c4,c5,c8,c7};
	Coordinate[] sq3_4 = new Coordinate[]{c5,c6,c9,c8};
	
	Coordinate d1 = new Coordinate(-1,1,1E6);
	Coordinate d2 = new Coordinate(-1,0,1E6);
	Coordinate d3 = new Coordinate(-1,-1,1E6);
	Coordinate d4 = new Coordinate(0,1,1E3);
	Coordinate d5 = new Coordinate(0,0,0);
	Coordinate d6 = new Coordinate(0,-1,1E3);
	Coordinate d7 = new Coordinate(1,1,0);
	Coordinate d8 = new Coordinate(1,0,0);
	Coordinate d9 = new Coordinate(1,-1,0);
	
	Coordinate[] sq4_1 = new Coordinate[]{d1,d2,d5,d4};
	Coordinate[] sq4_2 = new Coordinate[]{d2,d3,d6,d5};
	Coordinate[] sq4_3 = new Coordinate[]{d4,d5,d8,d7};
	Coordinate[] sq4_4 = new Coordinate[]{d5,d6,d9,d8};	
	
	Coordinate[][] ca = new Coordinate[][]{sq1_1,sq1_2,sq1_3,sq1_4};
	Coordinate[][] cb = new Coordinate[][]{sq2_1,sq2_2,sq2_3,sq2_4};
	Coordinate[][] cc = new Coordinate[][]{sq3_1,sq3_2,sq3_3,sq3_4};
	Coordinate[][] cd = new Coordinate[][]{sq4_1,sq4_2,sq4_3,sq4_4};

	@Before
	public void setUp(){
		
		try {
			gx = new Boundary_Grid_NetCDF("C:/Temp/bathym_increasing_x.nc","Latitude","Longitude");
			gxr = new Boundary_Grid_NetCDF("C:/Temp/bathym_decreasing_x.nc","Latitude","Longitude");
			gy = new Boundary_Grid_NetCDF("C:/Temp/bathym_increasing_y.nc","Latitude","Longitude");
			greal = new Boundary_Grid_NetCDF("C:/Temp/aus_bath_lite.nc","Latitude","Longitude");
			greal.setPositiveDown(false);
			
			f1 = new Boundary_Grid_TestingGrid();
			f1.setCells(ca); // 4-cell negative fold y-axis aligned
			
			f2 = new Boundary_Grid_TestingGrid();
			f2.setCells(cb); // 4-cell negative fold x-axis aligned
			
			fx = new Boundary_Grid_TestingGrid();
			fx.setCells(cc);
			
			fo = new Boundary_Grid_TestingGrid();
			fo.setCells(cd);

			cdb_x = new CollisionDetector_3D_Bathymetry(gx);
			cdb_x.setProjectionTransform(new PrjTransform_None());
			
			cdb_xr = new CollisionDetector_3D_Bathymetry(gxr);
			cdb_xr.setProjectionTransform(new PrjTransform_None());
			
			cdb_y = new CollisionDetector_3D_Bathymetry(gy);
			cdb_y.setProjectionTransform(new PrjTransform_None());

			cdbreal = new CollisionDetector_3D_Bathymetry(greal);
			
			cdb_e1 = new CollisionDetector_3D_Bathymetry(f1);
			cdb_e1.setProjectionTransform(new PrjTransform_None());
			
			cdb_e2 = new CollisionDetector_3D_Bathymetry(f2);
			cdb_e2.setProjectionTransform(new PrjTransform_None());
			
			cdb_ex = new CollisionDetector_3D_Bathymetry(fx);
			cdb_ex.setProjectionTransform(new PrjTransform_None());
			
			cdb_eo = new CollisionDetector_3D_Bathymetry(fo);
			cdb_eo.setProjectionTransform(new PrjTransform_None());
			
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
	public void testEdgeHit(){
		Particle p1 = new Particle();
		p1.setPX(-0.5); p1.setPY(0.5);	p1.setPZ(1); p1.setX(0.5); p1.setY(0.5); p1.setZ(-1);
		cdb_e1.setSurfaceLevel(100);
		cdb_e1.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
		System.out.println(Arrays.toString(da));
		//Assert.assertArrayEquals(new double[]{0,0,1},da ,1E-9);
	}
	
	//@Test
	public void testRealProblem(){
		Particle p1 = new Particle();
		
		//p1.setPX(113.1314548954153); p1.setPY(-25.15381371021109); p1.setPZ(0.0); p1.setX(113.1206995353216); p1.setY(-25.1427262565772); p1.setZ(0.0);
		//p1.setPX(119.2955883059722); p1.setPY(-19.967516812656964); p1.setPZ(0.0); p1.setX(119.28825081656038); p1.setY(-19.973046751359398); p1.setZ(0.0);
		p1.setPX(118.90337209729493); p1.setPY(-19.924273157502153); p1.setPZ(0.0); p1.setX(118.92530884302202); p1.setY(-19.938343466933144); p1.setZ(-0.724323105532676);
		//p1.setPX(119.6114368822359); p1.setPY(-20.0045209399733885); p1.setPZ(0.0); p1.setX(113.1206995353216); p1.setY(-25.1427262565772); p1.setZ(0.0);
		
		
		//int[] indices = greal.getIndices(p1.getPX(), p1.getPY()); 
		//System.out.println(Arrays.toString(indices));
		cdbreal.handleIntersection(p1);
	}
	
	public static void main(String[] args){
		TestCollisionDetection_3D_Bathymetry t = new TestCollisionDetection_3D_Bathymetry();
		t.setUp();
		t.testEdgeHit();
		//t.testRealProblem();
	}
}
