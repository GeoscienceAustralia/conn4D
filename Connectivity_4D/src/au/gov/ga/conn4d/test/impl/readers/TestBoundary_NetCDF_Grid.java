package au.gov.ga.conn4d.test.impl.readers;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.impl.readers.Boundary_Raster_NetCDF;

public class TestBoundary_NetCDF_Grid {

	Boundary_Raster_NetCDF bng = null;
	
	@Before
	public void setUp(){
		try {
			bng = new Boundary_Raster_NetCDF("C:/Temp/bath_index.nc","Latitude","Longitude");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testVertices() {
		Assert.assertNull(bng.getVertices(new int[]{0,0}));
		System.out.println(Arrays.toString(bng.getVertices(new int[]{1,1})));
	}
	
	@Test
	public void testIndices() {
		Assert.assertArrayEquals(new int[]{50, 50},bng.getIndices(0, 0));
		Assert.assertArrayEquals(new int[]{0, 0},bng.getIndices(-2, -2));
		Assert.assertArrayEquals(new int[]{0, 0},bng.getIndices(-1.96, -2));
		Assert.assertArrayEquals(new int[]{0, 1},bng.getIndices(-1.96+1E-12, -2));
		Assert.assertArrayEquals(new int[]{0, 1},bng.getIndices(-1.95, -2));
	}
	
	//@Test
	public void testRealDepth(){
		System.out.println(bng.getRealDepth(-1.959, -1.96));
	}

}
