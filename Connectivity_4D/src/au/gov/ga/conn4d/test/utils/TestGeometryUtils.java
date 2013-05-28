package au.gov.ga.conn4d.test.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import au.gov.ga.conn4d.utils.GeometryUtils;

public class TestGeometryUtils {

	@Test
	public void test() {
		assertEquals(GeometryUtils.lonlat2ceqd(new double[]{1,0})[0], 111319.5, 0.1);
		assertEquals(GeometryUtils.lonlat2ceqd(new double[]{0,1})[1], 111319.5, 0.1);
		assertEquals(GeometryUtils.ceqd2lonlat(new double[]{111319.5,0})[0], 1, 0.001);
		assertEquals(GeometryUtils.ceqd2lonlat(new double[]{0,111319.5})[1], 1, 0.001);
		double[] input = new double[]{113.40437496675112,-26.983227605170704};
		System.out.println(Arrays.toString(input));
		double[] output = GeometryUtils.lonlat2ceqd(input);
		System.out.println(Arrays.toString(output));
		double[] backagain = GeometryUtils.ceqd2lonlat(output);
		System.out.println(Arrays.toString(backagain));	
	}

}
