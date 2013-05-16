package au.gov.ga.conn4d.test.utils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.utils.ArraySearch;

public class TestArraySearch {

	private long[] la_fwd = new long[100];
	private long[] la_rev = new long[100];
	
	@Before
	public void setUp() throws Exception {
		for(int i = 0; i < la_fwd.length; i++){
			la_fwd[i] = i;
		}
		for(int i = 0; i < la_rev.length; i++){
			la_rev[i] = -i;
		}
	}
	
	@Test
	public void test() {
		Assert.assertTrue(ArraySearch.binarySearch(la_fwd, 80, 80)==80);
		Assert.assertTrue(ArraySearch.binarySearch(la_fwd, 80, 60)==80);
		Assert.assertTrue(ArraySearch.binarySearch(la_fwd, 80, 90)==80);
		Assert.assertTrue(ArraySearch.reverseSearch(la_rev, -80)==80);
		Assert.assertTrue(ArraySearch.binarySearch(la_fwd, 20, 20)==20);
		Assert.assertTrue(ArraySearch.binarySearch(la_fwd, 20, 10)==20);
		Assert.assertTrue(ArraySearch.binarySearch(la_fwd, 20, 30)==20);
		Assert.assertTrue(ArraySearch.reverseSearch(la_rev, -20)==20);
	}
}
