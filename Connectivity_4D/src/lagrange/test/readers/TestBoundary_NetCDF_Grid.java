package lagrange.test.readers;

//import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import lagrange.impl.readers.Boundary_NetCDF_Grid;

import org.junit.Test;

public class TestBoundary_NetCDF_Grid {
	
	

	@Test
	public void test() {
		Boundary_NetCDF_Grid bng = null;
		try {
			bng = new Boundary_NetCDF_Grid("C:/Temp/bath_index.nc","Latitude","Longitude");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(Arrays.toString(bng.getVertices(new int[]{1,1})));
		System.out.println("Pause");
		
		
	}

}
