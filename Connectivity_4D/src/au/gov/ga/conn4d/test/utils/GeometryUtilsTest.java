package au.gov.ga.conn4d.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import au.gov.ga.conn4d.utils.GeometryUtils;

public class GeometryUtilsTest {
	
	private double eps = 1E-8;

	@Test
	public void test() {
		assertEquals(GeometryUtils.lonlat2ceqd(new double[]{1,0})[0], 111319.5, 0.1);
		assertEquals(GeometryUtils.lonlat2ceqd(new double[]{0,1})[1], 111319.5, 0.1);
		assertEquals(GeometryUtils.ceqd2lonlat(new double[]{111319.5,0})[0], 1, 0.001);
		assertEquals(GeometryUtils.ceqd2lonlat(new double[]{0,111319.5})[1], 1, 0.001);
		double[] input = new double[]{113.40437496675112,-26.983227605170704};
		double[] output = GeometryUtils.lonlat2ceqd(input);
		assertArrayEquals(new double[]{1.2624117275028195E7, -3003759.156966605},output,eps);
		double[] backagain = GeometryUtils.ceqd2lonlat(output);
		assertArrayEquals(new double[]{113.40437496675112,-26.983227605170704},backagain,eps);
	}

}
