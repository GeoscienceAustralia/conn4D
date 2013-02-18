package lagrange.test.readers;

import lagrange.impl.readers.Reader_NetCDF_3D;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestReader_NetCDF_3D {

	Reader_NetCDF_3D n3;
	String filename = "E:/HPC/Modeling/AUS/Input/NetCDF/AUS_mld_2009.nc";
	String varName = "mld";
	String timeName = "Time";
	String latName = "Latitude";
	String lonName = "Longitude";
	
	@Before
	public void setUp() throws Exception {
		n3 = new Reader_NetCDF_3D(filename, varName, timeName, latName, lonName);
	}
	
	@Test
	public void test() {
		Assert.assertEquals(23.5252,n3.getValue(39448, 100, -50),1E-4);
		Assert.assertEquals(24.2172,n3.getValue(39448, 100.08, -50),1E-4);
		Assert.assertEquals(24.2172,n3.getValue(39448, 100.04, -50),1E-4);
		Assert.assertEquals(23.5252,n3.getValue(39448.25, 100, -50),1E-4);
		Assert.assertEquals(62.4045,n3.getValue(39449, 100, -50),1E-4);
		Assert.assertEquals(62.4045,n3.getValue(39448.5, 100, -50),1E-4);
	}
}
