package au.gov.ga.conn4d.test.impl.readers;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.impl.readers.Reader_NetCDF_4D;

public class TestReader_NetCDF_4D {

	Reader_NetCDF_4D n4;
	String filename = "Z:/NetCDF/AUS_u_2005.nc";
	String varName = "u";
	String timeName = "Time";
	String depthName = "Depth";
	String latName = "Latitude";
	String lonName = "Longitude";
	
	@Before
	public void setUp() throws Exception {
		n4 = new Reader_NetCDF_4D(filename, varName, timeName, depthName, latName, lonName);
		n4.initialize();
	}
	
	@Test
	public void test() {
		assertEquals(0.5307697057723999,n4.getValue(37985, 0, 100, -50),1E-4);
		assertEquals(0.4407572150230407,n4.getValue(37985, 0, 100.08, -50),1E-4);
		assertEquals(0.4407572150230407,n4.getValue(37985, 0, 100.04, -50),1E-4);
		assertEquals(0.5307697057723999,n4.getValue(37985.25, 0, 100, -50),1E-4);
		assertEquals(0.5922527313232422,n4.getValue(37986, 0, 100, -50),1E-4);
		assertEquals(0.5922527313232422,n4.getValue(37985.5, 0, 100, -50),1E-4);
	}
}
