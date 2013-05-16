package au.gov.ga.conn4d.test.utils;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.conn4d.utils.Fmath;

public class TestFMath {
	
	private final double eps = 1E-7;

	@Test
	public void testLog10Double() {
		Assert.assertEquals(2, Fmath.log10(100d),eps);
		Assert.assertEquals(3, Fmath.log10(1000d),eps);
		Assert.assertEquals(Math.log10(1234567d), Fmath.log10(1234567d),eps);
	}
	
	@Test
	public void testLog10Float() {
		Assert.assertEquals(2, Fmath.log10(100f),eps);
		Assert.assertEquals(3, Fmath.log10(1000f),eps);
		Assert.assertEquals(Math.log10(1234567f), Fmath.log10(1234567f),eps);
	}
	
	@Test
	public void testAntiLogDouble() {
		Assert.assertEquals(100, Fmath.antilog10(2d),eps);
		Assert.assertEquals(1000, Fmath.antilog10(3d),eps);
		Assert.assertEquals(Math.pow(10.0d, Math.PI), Fmath.antilog10(Math.PI),eps);
	}

}
