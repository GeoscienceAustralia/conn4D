package lagrange.test.readers;

//import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import lagrange.impl.readers.Boundary_NetCDF_Grid;

import org.junit.Before;
import org.junit.Test;

public class TestBoundary_NetCDF_Grid {

	Boundary_NetCDF_Grid bng = null;
	
	@Before
	public void setUp(){
		try {
			bng = new Boundary_NetCDF_Grid("C:/Temp/bath_index.nc","Latitude","Longitude");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
		System.out.println(Arrays.toString(bng.getVertices(new int[]{1,1})));
		System.out.println("Pause");	
	}
	
	@Test
	public void testRealDepth(){
		System.out.println(bng.getRealDepth(-1.959, -1.96));
	}

}
