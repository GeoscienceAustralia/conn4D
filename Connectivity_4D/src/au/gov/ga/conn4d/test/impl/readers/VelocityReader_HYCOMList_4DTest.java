package au.gov.ga.conn4d.test.impl.readers;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.impl.readers.VelocityReader_HYCOMList_4D;

public class VelocityReader_HYCOMList_4DTest {

	private String dir = "V:/Data/HYCOM/Blocks";
	private VelocityReader_HYCOMList_4D ncl;
	private double eps = 1E-2;
	
	@Before
	public void setUp(){
		try {
			ncl = new VelocityReader_HYCOMList_4D();
			ncl.setTName("Time");
			ncl.setZName("Depth");
			ncl.setLatName("Latitude");
			ncl.setLonName("Longitude");
			ncl.initialize(dir);
			ncl.setTLookup("Time");
			ncl.setZLookup("Depth");
			ncl.setXLookup("Longitude");
			ncl.setYLookup("Latitude");
			ncl.setUName("u");
			ncl.setVName("v");
			ncl.setWName("w");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetVelocities() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat formatUTC = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ssZ");
		formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		c.set(Calendar.YEAR, 2009);
		c.set(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		double[] vels = ncl.getVelocities(c.getTimeInMillis(), -10, 100.08, -27.090);
		assertArrayEquals(vels,new double[]{-0.09442, 0.01034, -2.60986E-05},eps);
		c.set(Calendar.DAY_OF_YEAR,91);
		vels = ncl.getVelocities(c.getTimeInMillis(), -100, 150, -47.8);
		assertArrayEquals(vels,new double[]{0.37643, -0.17394, -2.20267E-05},eps);
	}
	
	@After
	public void testClose(){
		ncl.close();
	}
}
