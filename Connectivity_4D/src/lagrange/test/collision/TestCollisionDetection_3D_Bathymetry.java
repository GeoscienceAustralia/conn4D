package lagrange.test.collision;

import java.io.IOException;
import java.util.Arrays;

import lagrange.Particle;
import lagrange.impl.collision.CollisionDetector_3D_Bathymetry;
import lagrange.impl.readers.Boundary_NetCDF_Grid;
import lagrange.utils.Utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//import com.vividsolutions.jts.geom.Coordinate;

public class TestCollisionDetection_3D_Bathymetry {
	
	CollisionDetector_3D_Bathymetry cdb_x,cdb_y, cdbreal;
	Boundary_NetCDF_Grid gx, gy, greal;

	@Before
	public void setUp(){
		
		try {
			gx = new Boundary_NetCDF_Grid("C:/Temp/bath_x_m.nc","Latitude","Longitude");
			gy = new Boundary_NetCDF_Grid("C:/Temp/bath_y_m.nc","Latitude","Longitude");
			greal = new Boundary_NetCDF_Grid("C:/Temp/aus_bath_lite.nc","Latitude","Longitude");
			greal.setPositiveDown(false);

			cdb_x = new CollisionDetector_3D_Bathymetry(gx);
			cdb_y = new CollisionDetector_3D_Bathymetry(gy);
			cdbreal = new CollisionDetector_3D_Bathymetry(greal);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testXSlopeZLine() {
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0);	p1.setPZ(1); p1.setX(0); p1.setY(0); p1.setZ(-1); 
		cdb_x.handleIntersection(p1);
		double[] da = Utils.lonlat2ceqd(new double[]{p1.getX(),p1.getY(),p1.getZ()});
	    Assert.assertArrayEquals(new double[]{-1,0,0},da ,1E-1);// precision decrease due to use of meters
	}
	
	/*@Test
	public void testXSlopeOffsetZLine(){
		Particle p1 = new Particle();
		cdb_x.getBoundary().getBoundaryDepth(1, 1);
		p1.setPX(1); p1.setPY(1);	p1.setPZ(111319.4921875+1); p1.setX(1); p1.setY(1); p1.setZ(111319.4921875-1); 
		cdb_x.handleIntersection(p1);
		double[] da = Utils.lonlat2ceqd(new double[]{p1.getX(),p1.getY(),p1.getZ()});
		double[] da2 = Utils.lonlat2ceqd(new double[]{1,1,111319.4921875});
	    Assert.assertArrayEquals(new double[]{da2[0]-1,da2[1],da[2]},da ,1E-1);// precision decrease to to use of meters
	}*/
	
	@Test
	public void testXReverseSlopeZLine() {
		gx.setPositiveDown(true);
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0);	p1.setPZ(1); p1.setX(0); p1.setY(0); p1.setZ(-1); 
		cdb_x.handleIntersection(p1);
		double[] da = Utils.lonlat2ceqd(new double[]{p1.getX(),p1.getY(),p1.getZ()});
	    Assert.assertArrayEquals(new double[]{1,0,0},da ,1E-1);// precision decrease due to use of meters
	}
	
	@Test
	public void testYSlopeZLine() {
		Particle p1 = new Particle();
		p1.setPX(0); p1.setPY(0);	p1.setPZ(1); p1.setX(0); p1.setY(0); p1.setZ(-1); 
		cdb_y.handleIntersection(p1);
		double[] da = Utils.lonlat2ceqd(new double[]{p1.getX(),p1.getY(),p1.getZ()});
	    Assert.assertArrayEquals(new double[]{0,-1,0},da ,1E-1);// precision decrease due to use of meters
	}
	
	@Test
	public void testRealSlopeRealLine(){
		Particle p1 = new Particle();
		p1.setPX(113.46163392338082); p1.setPY(-27.67861062425476); p1.setPZ(-5.0); p1.setX(113.4442410384298); p1.setY(-27.667486017407406); p1.setZ(-24417.7189078367); 
		int[] indices = greal.getIndices(p1.getPX(), p1.getPY()); 
		System.out.println(Arrays.toString(indices));
		//Coordinate [] ca = greal.getVertices(indices);
		System.out.println(greal.getSlope(p1.getPX(),p1.getPY()));
		System.out.println(greal.getAspect(p1.getPX(),p1.getPY()));
		cdbreal.handleIntersection(p1);
		//double[] da = new double[]{p1.getX(),p1.getY(),p1.getZ()};
	}
}
