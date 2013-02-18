package lagrange.test.utils;

import static org.junit.Assert.*;

import java.util.Arrays;

import lagrange.utils.Utils;

import org.junit.Test;

public class TestUtils {

	@Test
	public void test() {
		assertEquals(Utils.lonlat2ceqd(new double[]{1,0})[0], 111319.5, 0.1);
		assertEquals(Utils.lonlat2ceqd(new double[]{0,1})[1], 111319.5, 0.1);
		assertEquals(Utils.ceqd2lonlat(new double[]{111319.5,0})[0], 1, 0.001);
		assertEquals(Utils.ceqd2lonlat(new double[]{0,111319.5})[1], 1, 0.001);
		double[] input = new double[]{113.40437496675112,-26.983227605170704};
		System.out.println(Arrays.toString(input));
		double[] output = Utils.lonlat2ceqd(input);
		System.out.println(Arrays.toString(output));
		double[] backagain = Utils.ceqd2lonlat(output);
		System.out.println(Arrays.toString(backagain));	
	}

}
