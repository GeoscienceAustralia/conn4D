package lagrange.test.utils;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import lagrange.utils.IndexLookup_Nearest;

import org.junit.Test;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

public class TestIndexLookup_Nearest {

	NetcdfFile ncFile;
	IndexLookup_Nearest loc;
	Variable var;

	@Test
	/**
	 * Tests the values that are retrieved nearest to the provided location.  Linear_x increases from 0 to 100 in the range -1 to 1, and ranges from 0 to 1 units in depth.
	 * The class will record whether the value was out of bounds, but will still return the closest
	 * end value.  Note, this is used to lookup *index* values from a 1-D vector - NOT to retrieve values from an array.
	 * The method is designed to work with references to the centroids of cells, not cell edges. 
	 */
	
	public void testLocate() {
		try {
			ncFile = NetcdfFile.open("C://Temp//Linear_x.nc");
		} catch (IOException e) {
			e.printStackTrace();
		}
		var = ncFile.findVariable("Longitude");
		loc = new IndexLookup_Nearest(var);
		
		// Half-way between -1 and 1 should be 50
		// InBounds should be 0
		assertEquals(50, loc.lookup(0));
		assertEquals(0, loc.isIn_Bounds());
		
		// -1 should be 0
		// InBounds should be 0
		assertEquals(0, loc.lookup(-1.0));
		assertEquals(0, loc.isIn_Bounds());
		
		// +1 should be 100
		// InBounds should be 0
		assertEquals(100, loc.lookup(1.0));
		assertEquals(0, loc.isIn_Bounds());
		
		// Beyond the left edge should return 0
		// but InBounds will return -1
		assertEquals(0, loc.lookup(-5));
		assertEquals(-1, loc.isIn_Bounds());

		// Beyond the left edge should return 100
		// but InBounds will return +1
		assertEquals(100, loc.lookup(5));
		assertEquals(1, loc.isIn_Bounds());
		
		// Switch variable to Latitude, and repeat the basic tests.
		
		var = ncFile.findVariable("Latitude");
		loc = new IndexLookup_Nearest(var);
		loc.setVariable(var);
		assertEquals(50, loc.lookup(0));
		assertEquals(0, loc.lookup(-1.0));
		assertEquals(100, loc.lookup(1.0));
		
		// Switch variable to Depth
		
		var = ncFile.findVariable("Depth");
		loc = new IndexLookup_Nearest(var);
		loc.setVariable(var);
		assertEquals(0, loc.lookup(0));
		assertEquals(10, loc.lookup(1));
		
		assertEquals(0, loc.lookup(-1));
		assertEquals(-1, loc.isIn_Bounds());
		
		assertEquals(10, loc.lookup(2));
		assertEquals(1, loc.isIn_Bounds());
		
		// Ensure rounding works
		assertEquals(5, loc.lookup(.475));
		assertEquals(6, loc.lookup(.612));
	}

	/**
	 * Tests values when variables (e.g. Latitude and Longitude) have more than
	 * one dimension.
	 */

	@Test
	public void testDimension() {
		try {
			ncFile = NetcdfFile.open("V:/Data/HYCOM/AUS_u_2009_1.nc");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		var = ncFile.findVariable("Longitude");
		loc = new IndexLookup_Nearest(var, 0);
		System.out.println(Arrays.toString(loc.getJavaArray()));
		loc.setVariable(var, 1);
		System.out.println(Arrays.toString(loc.getJavaArray()));
		var = ncFile.findVariable("Latitude");
		loc.setVariable(var, 0);
		System.out.println(Arrays.toString(loc.getJavaArray()));
		loc.setVariable(var, 1);
		System.out.println(Arrays.toString(loc.getJavaArray()));
	}
}
