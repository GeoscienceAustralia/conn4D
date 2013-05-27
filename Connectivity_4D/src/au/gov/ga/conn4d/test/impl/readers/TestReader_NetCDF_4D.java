package au.gov.ga.conn4d.test.impl.readers;


import org.junit.Assert;
import org.junit.Before;

import au.gov.ga.conn4d.impl.readers.Reader_NetCDF_3D;

public class TestReader_NetCDF_4D {

	Reader_NetCDF_3D n3;
	String filename = "Z:/NetCDF/AUS_u_2005.nc";
	String varName = "mld";
	String timeName = "Time";
	String latName = "Latitude";
	String lonName = "Longitude";
	
	@Before
	public void setUp() throws Exception {
		n3 = new Reader_NetCDF_3D(filename, varName, timeName, latName, lonName);
	}
	
	//@Test
	public void test() {
		Assert.assertEquals(23.5252,n3.getValue(39448, 100, -50),1E-4);
		Assert.assertEquals(24.2172,n3.getValue(39448, 100.08, -50),1E-4);
		Assert.assertEquals(24.2172,n3.getValue(39448, 100.04, -50),1E-4);
		Assert.assertEquals(23.5252,n3.getValue(39448.25, 100, -50),1E-4);
		Assert.assertEquals(62.4045,n3.getValue(39449, 100, -50),1E-4);
		Assert.assertEquals(62.4045,n3.getValue(39448.5, 100, -50),1E-4);
	}
}
