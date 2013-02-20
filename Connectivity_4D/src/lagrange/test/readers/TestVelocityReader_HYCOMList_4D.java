package lagrange.test.readers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import lagrange.impl.readers.VelocityReader_HYCOMList_4D;

import org.junit.Before;
import org.junit.Test;

public class TestVelocityReader_HYCOMList_4D {

	String dir = "V:/HYCOM";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		TestVelocityReader_HYCOMList_4D tvr = new TestVelocityReader_HYCOMList_4D();
		tvr.setUp();
		tvr.test();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void test() {
		double[][] bnds = ncl.getBounds();
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
		System.out.println(formatUTC.format(time));
		double[][] dims = ncl.getDims();
		double[] vels = ncl.getVelocities(time, -10, 100.0799560546875, -49.904899597);	
	}

}
