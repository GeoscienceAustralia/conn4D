package lagrange.test.readers;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import lagrange.impl.readers.VelocityReader_NetCDFList_4D;
import lagrange.utils.TimeConvert;

import org.junit.Before;
import org.junit.Test;

public class TestVelocityReader_NetCDFList {

	String dir = "V:/HYCOM";
	VelocityReader_NetCDFList_4D ncl;
	
	@Before
	public void setUp(){
		try {
			ncl = new VelocityReader_NetCDFList_4D(dir);
			ncl.setXLookup("Longitude");
			ncl.setYLookup("Latitude");
			ncl.setTLookup("MT");
			ncl.setZLookup("Depth");
			
			ncl.setUName("u");
			ncl.setVName("v");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		TestVelocityReader_NetCDFList tvr = new TestVelocityReader_NetCDFList();
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
		double[] vels = ncl.getVelocities(time, -5, 100, -50);
	}

}
