package au.gov.ga.conn4d.test.utils;


import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.utils.VectorUtils;

public class VectorUtilsTest {

	double[] vals = new double[] { 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1 };
	double[] vals2 = new double[] {1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1 };
	double eps = 1E-9;
	float epsf = 1E-9f;
	Filter filter;

	double[][] distances = new double[][] {
			{ 0, 1, 2, 3, 1, 2, 3, 4, 2, 3, 4, 5, 3, 4, 5, 6 },
			{ 1, 0, 1, 2, 2, 1, 2, 3, 3, 2, 3, 4, 4, 3, 4, 5 },
			{ 2, 1, 0, 1, 3, 2, 1, 2, 4, 3, 2, 3, 5, 4, 3, 2 },
			{ 3, 2, 1, 0, 4, 3, 2, 1, 5, 4, 3, 2, 6, 5, 4, 3 },
			{ 1, 2, 3, 4, 0, 1, 2, 3, 1, 2, 3, 4, 2, 3, 4, 5 },
			{ 2, 1, 2, 3, 1, 0, 1, 2, 2, 1, 2, 3, 3, 2, 3, 4 },
			{ 3, 2, 1, 2, 2, 1, 0, 1, 3, 2, 1, 2, 4, 3, 2, 3 },
			{ 4, 3, 2, 1, 3, 2, 1, 0, 4, 3, 2, 1, 5, 4, 3, 2 },
			{ 2, 3, 4, 5, 1, 2, 3, 4, 0, 1, 2, 3, 1, 2, 3, 4 },
			{ 3, 2, 3, 4, 2, 1, 2, 3, 1, 0, 1, 2, 2, 1, 2, 3 },
			{ 4, 3, 2, 3, 3, 2, 1, 2, 2, 1, 0, 1, 3, 2, 1, 2 },
			{ 5, 4, 3, 2, 4, 3, 2, 1, 3, 2, 1, 0, 4, 3, 2, 1 },
			{ 3, 4, 5, 6, 2, 3, 4, 5, 1, 2, 3, 4, 0, 1, 2, 3 },
			{ 4, 3, 4, 5, 3, 2, 3, 4, 2, 1, 2, 3, 1, 0, 1, 2 },
			{ 5, 4, 3, 4, 4, 3, 2, 3, 3, 2, 1, 2, 2, 1, 0, 1 },
			{ 6, 5, 2, 3, 5, 4, 3, 2, 4, 3, 2, 1, 3, 2, 1, 0 } };
	
	float[][] distances_float = new float[][] {
			{ 0, 1, 2, 3, 1, 2, 3, 4, 2, 3, 4, 5, 3, 4, 5, 6 },
			{ 1, 0, 1, 2, 2, 1, 2, 3, 3, 2, 3, 4, 4, 3, 4, 5 },
			{ 2, 1, 0, 1, 3, 2, 1, 2, 4, 3, 2, 3, 5, 4, 3, 2 },
			{ 3, 2, 1, 0, 4, 3, 2, 1, 5, 4, 3, 2, 6, 5, 4, 3 },
			{ 1, 2, 3, 4, 0, 1, 2, 3, 1, 2, 3, 4, 2, 3, 4, 5 },
			{ 2, 1, 2, 3, 1, 0, 1, 2, 2, 1, 2, 3, 3, 2, 3, 4 },
			{ 3, 2, 1, 2, 2, 1, 0, 1, 3, 2, 1, 2, 4, 3, 2, 3 },
			{ 4, 3, 2, 1, 3, 2, 1, 0, 4, 3, 2, 1, 5, 4, 3, 2 },
			{ 2, 3, 4, 5, 1, 2, 3, 4, 0, 1, 2, 3, 1, 2, 3, 4 },
			{ 3, 2, 3, 4, 2, 1, 2, 3, 1, 0, 1, 2, 2, 1, 2, 3 },
			{ 4, 3, 2, 3, 3, 2, 1, 2, 2, 1, 0, 1, 3, 2, 1, 2 },
			{ 5, 4, 3, 2, 4, 3, 2, 1, 3, 2, 1, 0, 4, 3, 2, 1 },
			{ 3, 4, 5, 6, 2, 3, 4, 5, 1, 2, 3, 4, 0, 1, 2, 3 },
			{ 4, 3, 4, 5, 3, 2, 3, 4, 2, 1, 2, 3, 1, 0, 1, 2 },
			{ 5, 4, 3, 4, 4, 3, 2, 3, 3, 2, 1, 2, 2, 1, 0, 1 },
			{ 6, 5, 2, 3, 5, 4, 3, 2, 4, 3, 2, 1, 3, 2, 1, 0 } };

	double[][] sel;

	@Before
	public void startUp() {
	}

	@Test
	public void test() {
		double[][] sel = VectorUtils.select(distances, new BandFilter(0, 1));
		double mi = VectorUtils.moransI(vals, sel);
		assertEquals(-1.0, mi,1E-16);
		assertTrue(VectorUtils.moransI_test(vals, sel,0.95));
		double[][] sel2 = VectorUtils.select(distances, new BandFilter(0, 3));
		assertFalse(VectorUtils.moransI_test(vals2, sel2,0.95));
	}
	
	@Test
	public void copyTest(){
		double[][] copy = VectorUtils.copy(distances);
		assertEquals(distances[0][0], copy[0][0],eps);
		assertEquals(distances[0][3], copy[0][3],eps);
		assertEquals(distances[3][0], copy[3][0],eps);
		copy[9][9] = 42;
		assertNotSame(distances, copy);
	}
	
	@Test
	public void getColumnDoubleTest(){
		assertArrayEquals(VectorUtils.getColumn(distances, 1),new double[]{1,0,1,2,2,1,2,3,3,2,3,4,4,3,4,5},eps);
	}
	
	@Test
	public void getColumnFloatTest(){
		assertArrayEquals(VectorUtils.getColumn(distances_float, 1),new float[]{1,0,1,2,2,1,2,3,3,2,3,4,4,3,4,5},epsf);
	}
}

class BandFilter implements Filter {
	final double min;
	final double max;

	BandFilter(double _min, double _max) {
		min = _min;
		max = _max;
	}

	@Override
	public boolean accept(Number n) {
		if (n.doubleValue() > min && n.doubleValue() <= max) {
			return true;
		}
		return false;
	}
}
