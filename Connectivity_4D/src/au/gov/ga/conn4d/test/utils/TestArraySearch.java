package au.gov.ga.conn4d.test.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.utils.ArraySearch;

public class TestArraySearch {

	private long[] la_fwd = new long[100];
	private long[] la_rev = new long[100];
	private int[] ia_fwd = new int[100];
	private int[] ia_rev = new int[100];
	private double[] da_fwd = new double[100];
	private double[] da_rev = new double[100];
	private float[] fa_fwd = new float[100];
	private float[] fa_rev = new float[100];

	
	@Before
	public void setUp() throws Exception {
		for(int i = 0; i < la_fwd.length; i++){
			la_fwd[i] = i;
			ia_fwd[i] = i;
			da_fwd[i] = i;
			fa_fwd[i] = i;
			
		}
		for(int i = 0; i < la_rev.length; i++){
			la_rev[i] = -i;
			ia_rev[i] = -i;
			da_rev[i] = -i;
			fa_rev[i] = -i;
		}
	}
	
	@Test
	public void testLong() {
		assertTrue(ArraySearch.binarySearch(la_fwd, 80, 80)==80);
		assertTrue(ArraySearch.binarySearch(la_fwd, 80, 60)==80);
		assertTrue(ArraySearch.binarySearch(la_fwd, 80, 90)==80);
		assertTrue(ArraySearch.reverseSearch(la_rev, -80)==80);
		assertTrue(ArraySearch.binarySearch(la_fwd, 20, 20)==20);
		assertTrue(ArraySearch.binarySearch(la_fwd, 20, 10)==20);
		assertTrue(ArraySearch.binarySearch(la_fwd, 20, 30)==20);
		assertTrue(ArraySearch.reverseSearch(la_rev, -20)==20);
	}
	
	@Test
	public void testInt() {
		assertTrue(ArraySearch.binarySearch(ia_fwd, 80, 80)==80);
		assertTrue(ArraySearch.binarySearch(ia_fwd, 80, 60)==80);
		assertTrue(ArraySearch.binarySearch(ia_fwd, 80, 90)==80);
		assertTrue(ArraySearch.reverseSearch(ia_rev, -80)==80);
		assertTrue(ArraySearch.binarySearch(ia_fwd, 20, 20)==20);
		assertTrue(ArraySearch.binarySearch(ia_fwd, 20, 10)==20);
		assertTrue(ArraySearch.binarySearch(ia_fwd, 20, 30)==20);
		assertTrue(ArraySearch.reverseSearch(ia_rev, -20)==20);
	}
	
	@Test
	public void testDouble() {
		assertTrue(ArraySearch.binarySearch(da_fwd, 80, 80)==80);
		assertTrue(ArraySearch.binarySearch(da_fwd, 80, 60)==80);
		assertTrue(ArraySearch.binarySearch(da_fwd, 80, 90)==80);
		assertTrue(ArraySearch.reverseSearch(da_rev, -80)==80);
		assertTrue(ArraySearch.binarySearch(da_fwd, 20, 20)==20);
		assertTrue(ArraySearch.binarySearch(da_fwd, 20, 10)==20);
		assertTrue(ArraySearch.binarySearch(da_fwd, 20, 30)==20);
		assertTrue(ArraySearch.reverseSearch(da_rev, -20)==20);
	}
	
	@Test
	public void testFloat() {
		assertTrue(ArraySearch.binarySearch(fa_fwd, 80, 80)==80);
		assertTrue(ArraySearch.binarySearch(fa_fwd, 80, 60)==80);
		assertTrue(ArraySearch.binarySearch(fa_fwd, 80, 90)==80);
		assertTrue(ArraySearch.reverseSearch(fa_rev, -80)==80);
		assertTrue(ArraySearch.binarySearch(fa_fwd, 20, 20)==20);
		assertTrue(ArraySearch.binarySearch(fa_fwd, 20, 10)==20);
		assertTrue(ArraySearch.binarySearch(fa_fwd, 20, 30)==20);
		assertTrue(ArraySearch.reverseSearch(fa_rev, -20)==20);
	}
}
