package lagrange.test.utils;

import static org.junit.Assert.*;

import java.io.IOException;

import lagrange.utils.IndexLookup_Nearest;

import org.junit.Test;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

public class TestIndexLookup_Nearest{
	
	NetcdfFile ncFile;
	IndexLookup_Nearest loc;
	Variable var;
	
	@Test
	
	//Linear x increases from 0 to 100 in the range -1 to 1
	
	public void testLocate() {
		try {
			ncFile = NetcdfFile.open("C://Temp//Linear_x.nc");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		var = ncFile.findVariable("Longitude");
		loc = new IndexLookup_Nearest(var);
		assertEquals(50, loc.lookup(0));
		assertEquals(0, loc.lookup(-1.0));
		assertEquals(100, loc.lookup(1.0));
		assertEquals(-1, loc.lookup(-5));
		assertEquals(-102, loc.lookup(5));
		var = ncFile.findVariable("Latitude");
		loc.setVariable(var);
		assertEquals(50, loc.lookup(0));
		assertEquals(0, loc.lookup(-1.0));
		assertEquals(100, loc.lookup(1.0));
		var = ncFile.findVariable("Depth");
		loc = new IndexLookup_Nearest(var);
		loc.setVariable(var);
		assertEquals(0, loc.lookup(0));
		assertEquals(-1, loc.lookup(-1));
		assertEquals(10, loc.lookup(1));
		assertEquals(-12, loc.lookup(2));
		// Ensure rounding works
		assertEquals(5, loc.lookup(.475));
		assertEquals(6, loc.lookup(.612));
	}
	
	public static void main(String[] args){
		TestIndexLookup_Nearest tl = new TestIndexLookup_Nearest();
		tl.testLocate();
	}
}
