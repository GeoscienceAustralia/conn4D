package au.gov.ga.conn4d.test.impl.readers;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

import au.gov.ga.conn4d.impl.readers.Boundary_Raster_NetCDF;

public class Boundary_NetCDF_GridTest {

	Boundary_Raster_NetCDF bng = null;
	
	@Before
	public void setUp(){
		try {
			bng = new Boundary_Raster_NetCDF("./files/bath_index.nc","Latitude","Longitude");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testVertices() {
		Assert.assertNull(bng.getVertices(new int[]{0,0}));
		Coordinate[] ca = bng.getVertices(new int[]{1,1});
		assertEquals(ca[0], new Coordinate(-1.96,-1.96,51));
		assertEquals(ca[1], new Coordinate(-1.92,-1.96,52));
		assertEquals(ca[2], new Coordinate(-1.92,-1.92,153));
		assertEquals(ca[3], new Coordinate(-1.96,-1.92,152));
	}
	
	@Test
	public void testIndices() {
		Assert.assertArrayEquals(new int[]{50, 50},bng.getIndices(0, 0));
		Assert.assertArrayEquals(new int[]{0, 0},bng.getIndices(-2, -2));
		Assert.assertArrayEquals(new int[]{0, 0},bng.getIndices(-1.96, -2));
		Assert.assertArrayEquals(new int[]{0, 1},bng.getIndices(-1.96+1E-12, -2));
		Assert.assertArrayEquals(new int[]{0, 1},bng.getIndices(-1.95, -2));
	}
}
