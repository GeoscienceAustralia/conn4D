package au.gov.ga.conn4d.test.impl.readers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.impl.readers.VelocityReader_HYCOMList_4D;

public class VelocityReader_HYCOMList_4DTest {

	String dir = "V:/Data/HYCOM";
	VelocityReader_HYCOMList_4D ncl;
	
	@Before
	public void setUp(){
		try {
			ncl = new VelocityReader_HYCOMList_4D(dir); 
			ncl.setXLookup("Longitude");
			ncl.setYLookup("Latitude");
			ncl.setTLookup("MT");
			ncl.setZLookup("Depth");
			ncl.setUName("u");
			ncl.setVName("v");
			ncl.setWName("w");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Test is currently incomplete.
	@Test
	public void test() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat formatUTC = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ssZ");
		formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		c.set(Calendar.YEAR, 2009);
		c.set(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long time = c.getTimeInMillis();
		double[] vels = ncl.getVelocities(time, -10, 100.0799560546875, -49.904899597);
		System.out.println(Arrays.toString(vels));
	}
}
