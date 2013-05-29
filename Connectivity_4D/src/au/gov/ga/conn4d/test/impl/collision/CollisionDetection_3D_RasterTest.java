package au.gov.ga.conn4d.test.impl.collision;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.Boundary;
import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.impl.collision.CollisionDetector_3D_Raster;
import au.gov.ga.conn4d.impl.readers.Boundary_Raster_NetCDF;
import au.gov.ga.conn4d.utils.PrjTransform_None;

import com.vividsolutions.jts.geom.Coordinate;

public class CollisionDetection_3D_RasterTest {
	
	CollisionDetector_3D_Raster cdb_x, cdb_xr, cdb_y, cdb_real;
	CollisionDetector_3D_Raster cdb_e1, cdb_e2, cdb_ex, cdb_eo;
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
	
	/**
	 * Sets up the TestingGrids and CollisionDetectors used in the test.
	 */
	
	@Before
	public void setUp(){
		
		try {
			gx = new Boundary_Raster_NetCDF("C:/Temp/bathym_increasing_x.nc","Latitude","Longitude");
			gxr = new Boundary_Raster_NetCDF("C:/Temp/bathym_decreasing_x.nc","Latitude","Longitude");
			gy = new Boundary_Raster_NetCDF("C:/Temp/bathym_increasing_y.nc","Latitude","Longitude");
			greal = new Boundary_Raster_NetCDF("C:/Temp/aus_bath_lite.nc","Latitude","Longitude");
			greal.setPositiveDown(false);
			
			f1 = new Boundary_Grid_TestingGrid();
			f1.setCells(ca); // 4-cell negative fold y-axis aligned
			
			f2 = new Boundary_Grid_TestingGrid();
			f2.setCells(cb); // 4-cell negative fold x-axis aligned
			
			fx = new Boundary_Grid_TestingGrid();
			fx.setCells(cc);
			
			fo = new Boundary_Grid_TestingGrid();
			fo.setCells(cd);

			cdb_x = new CollisionDetector_3D_Raster(gx);
			cdb_x.setProjectionTransform(new PrjTransform_None());
			
			cdb_xr = new CollisionDetector_3D_Raster(gxr);
			cdb_xr.setProjectionTransform(new PrjTransform_None());
			
			cdb_y = new CollisionDetector_3D_Raster(gy);
			cdb_y.setProjectionTransform(new PrjTransform_None());

			cdb_real = new CollisionDetector_3D_Raster(greal);
			
			cdb_e1 = new CollisionDetector_3D_Raster(f1);
			cdb_e1.setProjectionTransform(new PrjTransform_None());
			
			cdb_e2 = new CollisionDetector_3D_Raster(f2);
			cdb_e2.setProjectionTransform(new PrjTransform_None());
			
			cdb_ex = new CollisionDetector_3D_Raster(fx);
			cdb_ex.setProjectionTransform(new PrjTransform_None());
			
			cdb_eo = new CollisionDetector_3D_Raster(fo);
			cdb_eo.setProjectionTransform(new PrjTransform_None());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests breaching using positive slope in the X direction and a
	 * horizontal line.
	 */
	
	@Test
	public void testBreaching(){
		Particle p1 = new Particle();
		p1.setPX(-1.5); p1.setPY(0); p1.setPZ(-0.5); p1.setX(0.5); p1.setY(0); p1.setZ(-0.5);
		cdb_ex.setSurfaceLevel(0);
		cdb_ex.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
		Assert.assertArrayEquals(new double[]{-0.5,0,0},da ,1E-9);
	}	
	
	/**
	 * Tests reflection off a concave edge (parallel to Y-axis) using a
	 * 45° line
	 */
	
	@Test
	public void testConcaveEdgeLine(){
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0.5);	p1.setPZ(1); p1.setX(0); p1.setY(0.5); p1.setZ(-1);
		cdb_ex.setSurfaceLevel(100);
		cdb_ex.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
		Assert.assertArrayEquals(new double[]{0,0.5,1},da ,1E-9);
	}
	
	/**
	 * Tests reflection off a concave edge (parallel to Y-axis) using a
	 * vertical line
	 */
	
	@Test
	public void testConcaveYEdgeZLine(){
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0.5);	p1.setPZ(1); p1.setX(0); p1.setY(0.5); p1.setZ(-1);
		cdb_e1.setSurfaceLevel(100);
		cdb_e1.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
		Assert.assertArrayEquals(new double[]{0,0.5,1},da ,1E-9);
	}
	
	/**
	 * Tests reflection off a convex edge (parallel to Y-axis) using a
	 * vertical line
	 */
	
	@Test
	public void testConvexYEdgeZLine(){
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0.5);	p1.setPZ(1); p1.setX(0); p1.setY(0.5); p1.setZ(-1);
		cdb_e1.setSurfaceLevel(100);
		cdb_e1.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
		Assert.assertArrayEquals(new double[]{0,0.5,1},da ,1E-9);
	}
	
	/**
	 * Tests reflection behavior off a corner when there is a simple
	 * positive slope in the X-direction.
	 */
	
	@Test
	public void testEasyCorner(){
		Particle p1 = new Particle();
		p1.setPX(0.5); p1.setPY(0.5);	p1.setPZ(-8.5); p1.setX(0.5); p1.setY(0.5); p1.setZ(-10.5); 
		cdb_x.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	    Assert.assertArrayEquals(new double[]{-0.5,0.5,-9.5},da ,1E-1);// precision decrease due to use of meters
	}
	
	/**
	 * Tests reflection behavior off a horizontal edge when there is a simple
	 * positive slope in the X-direction.
	 */
	
	@Test
	public void testEasyXEdgeZLine(){
		Particle p1 = new Particle();
		p1.setPX(0.5); p1.setPY(0);	p1.setPZ(-8.5); p1.setX(0.5); p1.setY(0); p1.setZ(-10.5); 
		cdb_x.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	    Assert.assertArrayEquals(new double[]{-0.5,0,-9.5},da ,1E-1);// precision decrease due to use of meters
	}
	
	/**
	 * Tests reflection behavior off a vertical edge when there is a simple
	 * positive slope in the X-direction.
	 */
	
	@Test
	public void testEasyYEdgeZLine(){
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0.5);	p1.setPZ(-9); p1.setX(0); p1.setY(0.5); p1.setZ(-11); 
		cdb_x.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	    Assert.assertArrayEquals(new double[]{-1,0.5,-10},da ,1E-1);// precision decrease due to use of meters
	}	
	
	
	//@Test
	public void testReal(){
		Particle p1 = new Particle();
		p1.setPX(113.19207965621727); p1.setPY(-26.23487697950828); p1.setPZ(-3.2944260095246136); 
		p1.setX(113.34096133503076); p1.setY(-26.196148714500566); p1.setZ(-3.3461520598461485); 
		cdb_real.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
		System.out.println(Arrays.toString(da));
		//Assert.assertTrue(p1.isLost());
	}	
	
	/**
	 * Tests shoaling behavior using a positive slope in the X-direction.
	 */
	
	@Test
	public void testShoaling(){
		Particle p1 = new Particle();
		p1.setPX(-1); p1.setPY(0);	p1.setPZ(-10); p1.setX(1); p1.setY(0); p1.setZ(-10);
		cdb_x.setSurfaceLevel(-10);
		cdb_x.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	    Assert.assertArrayEquals(new double[]{0,0,-10},da ,1E-1);// precision decrease due to use of meters
	}	
	
	/**
	 * Tests a vertical line against a negative slope in the X-direction
	 */
	
	@Test
	public void testXReverseSlopeZLine() {
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0);	p1.setPZ(-9); p1.setX(0); p1.setY(0); p1.setZ(-11); 
		cdb_xr.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	    Assert.assertArrayEquals(new double[]{1,0,-10},da ,1E-1);// precision decrease due to use of meters
	}
	
	/**
	 * Tests a vertical line against a positive slope in the X-direction,
	 * offset from 0 (at 5,5)
	 */
	
	@Test
	public void testXSlopeOffsetZLine(){
		Particle p1 = new Particle();
		p1.setPX(5); p1.setPY(5);	p1.setPZ(-4); p1.setX(5); p1.setY(5); p1.setZ(-6); 
		cdb_x.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	    Assert.assertArrayEquals(new double[]{4,5,-5},da ,1E-1);// precision decrease due to use of meters
	}
	
	/**
	 * Tests a vertical line against a positive slope in the X-direction
	 */
	
	@Test
	public void testXSlopeZLine() {
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0);	p1.setPZ(-9); p1.setX(0); p1.setY(0); p1.setZ(-11); 
		cdb_x.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	    Assert.assertArrayEquals(new double[]{-1,0,-10},da ,1E-1);// precision decrease due to use of meters
	}
	
	/**
	 * Tests a vertical line against a positive slope in the Y-direction
	 */
	
	@Test
	public void testYSlopeZLine() {
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0);	p1.setPZ(-9); p1.setX(0); p1.setY(0); p1.setZ(-11); 
		cdb_y.handleIntersection(p1);
		double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	    Assert.assertArrayEquals(new double[]{0,-1,-10},da ,1E-1);// precision decrease due to use of meters
	}
}