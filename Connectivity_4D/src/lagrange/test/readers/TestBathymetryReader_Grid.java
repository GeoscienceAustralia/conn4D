package lagrange.test.readers;

import java.io.IOException;
import java.util.Arrays;

import lagrange.impl.readers.Boundary_NetCDF_Grid;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestBathymetryReader_Grid {

	Boundary_NetCDF_Grid xbr,ybr,rbr;
	String xpath = "C:/Temp/bath_xmap.nc";
	String ypath = "C:/Temp/bath_ymap.nc";
	String realpath = "C:/Temp/aus_bath_lite2.nc";
	
	@Before
	public void setUp(){
		try {
			xbr = new Boundary_NetCDF_Grid(xpath,"Latitude","Longitude");
			ybr = new Boundary_NetCDF_Grid(ypath,"Latitude","Longitude");
			rbr = new Boundary_NetCDF_Grid(realpath,"Latitude","Longitude");
			xbr.setPositiveDown(false);
			ybr.setPositiveDown(false);
			rbr.setPositiveDown(false);		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void valueXTest() {
		// Basic test of a surface where the value = x.
		for(float j = -1; j < 1; j+=0.04){
			Assert.assertEquals(j, xbr.getBoundaryDepth(j, 0),1E-6);
		}
	}
	
	@Test
	public void valueYTest(){
		// Basic test of a surface where the value = y.
		for(float i = -1; i < 1; i+=0.04){
			Assert.assertEquals(i, ybr.getBoundaryDepth(0,i),1E-6);
		}		
	}
	
	@Test
	public void printTest(){
		System.out.println(Arrays.toString(rbr.getIndices(92, -60)));
		System.out.println(Arrays.toString(rbr.getIndices(92.007, -59.995)));
		int[] idx = rbr.getIndices(113.71699029648877, -26.870002872571874);
		idx = rbr.getIndices(113.71699029648877, -26.870002872571874);
		System.out.println(Arrays.toString(idx));
		System.out.println(rbr.getBoundaryDepth(113.71699029648877, -26.870002872571874));
	}
}
