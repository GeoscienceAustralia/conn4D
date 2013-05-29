package au.gov.ga.conn4d.test.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.utils.ArraySearch;

public class ArraySearchTest {

	private long[] la_fwd = new long[100];
	private long[] la_rev = new long[100];
	private int[] ia_fwd = new int[100];
	private int[] ia_rev = new int[100];
	private double[] da_fwd = new double[100];
	private double[] da_rev = new double[100];
	private float[] fa_fwd = new float[100];
	private float[] fa_rev = new float[100];
	private short[] sa_fwd = new short[100];
	private short[] sa_rev = new short[100];
	private char[] ca_fwd = new char[100];
	private char[] ca_rev = new char[100];
	
	@Before
	public void setUp() throws Exception {
		for(int i = 0; i < la_fwd.length; i++){
			la_fwd[i] = i;
			ia_fwd[i] = i;
			da_fwd[i] = i;
			fa_fwd[i] = i;
			sa_fwd[i] = (short) i;
			ca_fwd[i] = Character.toChars(i)[0];
			
		}
		for(int i = 0; i < la_rev.length; i++){
			la_rev[i] = -i;
			ia_rev[i] = -i;
			da_rev[i] = -i;
			fa_rev[i] = -i;
			sa_rev[i] = (short) -i;
			ca_rev[i] = Character.toChars(-i+100)[0];
		}
	}
	
	@Test
	public void testLong() {
		assertEquals(ArraySearch.binarySearch(la_fwd, 80, 80),80);
		assertEquals(ArraySearch.binarySearch(la_fwd, 80, 60),80);
		assertEquals(ArraySearch.binarySearch(la_fwd, 80, 90),80);
		assertEquals(ArraySearch.reverseSearch(la_rev, -80),80);
		assertEquals(ArraySearch.binarySearch(la_fwd, 20, 20),20);
		assertEquals(ArraySearch.binarySearch(la_fwd, 20, 10),20);
		assertEquals(ArraySearch.binarySearch(la_fwd, 20, 30),20);
		assertEquals(ArraySearch.reverseSearch(la_rev, -20),20);
		assertEquals(ArraySearch.binarySearch(la_fwd, 20, 80, 30, 40),30);
	}
	
	@Test
	public void testInt() {
		assertEquals(ArraySearch.binarySearch(ia_fwd, 80, 80),80);
		assertEquals(ArraySearch.binarySearch(ia_fwd, 80, 60),80);
		assertEquals(ArraySearch.binarySearch(ia_fwd, 80, 90),80);
		assertEquals(ArraySearch.reverseSearch(ia_rev, -80),80);
		assertEquals(ArraySearch.binarySearch(ia_fwd, 20, 20),20);
		assertEquals(ArraySearch.binarySearch(ia_fwd, 20, 10),20);
		assertEquals(ArraySearch.binarySearch(ia_fwd, 20, 30),20);
		assertEquals(ArraySearch.reverseSearch(ia_rev, -20),20);
		assertEquals(ArraySearch.binarySearch(ia_fwd, 20, 80, 30, 40),30);
	}
	
	@Test
	public void testDouble() {
		assertEquals(ArraySearch.binarySearch(da_fwd, 80, 80),80);
		assertEquals(ArraySearch.binarySearch(da_fwd, 80, 60),80);
		assertEquals(ArraySearch.binarySearch(da_fwd, 80, 90),80);
		assertEquals(ArraySearch.reverseSearch(da_rev, -80),80);
		assertEquals(ArraySearch.binarySearch(da_fwd, 20, 20),20);
		assertEquals(ArraySearch.binarySearch(da_fwd, 20, 10),20);
		assertEquals(ArraySearch.binarySearch(da_fwd, 20, 30),20);
		assertEquals(ArraySearch.reverseSearch(da_rev, -20),20);
		assertEquals(ArraySearch.binarySearch(da_fwd, 20, 80, 30, 40),30);
	}
	
	@Test
	public void testFloat() {
		assertEquals(ArraySearch.binarySearch(fa_fwd, 80, 80),80);
		assertEquals(ArraySearch.binarySearch(fa_fwd, 80, 60),80);
		assertEquals(ArraySearch.binarySearch(fa_fwd, 80, 90),80);
		assertEquals(ArraySearch.reverseSearch(fa_rev, -80),80);
		assertEquals(ArraySearch.binarySearch(fa_fwd, 20, 20),20);
		assertEquals(ArraySearch.binarySearch(fa_fwd, 20, 10),20);
		assertEquals(ArraySearch.binarySearch(fa_fwd, 20, 30),20);
		assertEquals(ArraySearch.reverseSearch(fa_rev, -20),20);
		assertEquals(ArraySearch.binarySearch(fa_fwd, 20, 80, 30, 40),30);
	}
}
