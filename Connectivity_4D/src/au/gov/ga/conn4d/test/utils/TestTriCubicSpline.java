package au.gov.ga.conn4d.test.utils;

import junit.framework.Assert;

import org.junit.Test;

import au.gov.ga.conn4d.utils.BiCubicSpline;
import au.gov.ga.conn4d.utils.TriCubicSpline;

public class TestTriCubicSpline {

	double[] a1 = new double[]{1,2,3};
	double[] b1 = new double[] {1,2,3,4,5};
	double[] c1 = new double[] {1,2,3,4,5};
	float[][] d1 = new float[][]{{1,2,3,4,5},
									{6,7,8,9,10},
									{11,12,13,14,15},
									{16,17,18,19,20},
									{21,22,23,24,25}};
									
	float[][] d2 = new float[][]{{26,27,28,29,30},
								  {31,32,33,34,35},
								  {36,37,38,39,40},
								  {41,42,43,44,45},
								  {46,47,48,49,50}};
								   
	float[][] d3 = new float[][]{{51,52,53,54,55},
									{56,57,58,59,60},
									{61,62,63,64,65},
									{66,67,68,69,70},
									{71,72,73,74,75}};
	
	float[][][]e1 = new float[][][]{d1,d2,d3};
	
	BiCubicSpline bcs = new BiCubicSpline(b1,c1,d1);
	TriCubicSpline tcs = new TriCubicSpline(a1,b1,c1,e1);
	
	@Test
	public void test() {
		
		Assert.assertTrue(bcs.interpolate(1, 1)==1.0);
		Assert.assertTrue(bcs.interpolate(1, 2)==2.0);
		Assert.assertTrue(bcs.interpolate(2, 1)==6.0);
		Assert.assertTrue(bcs.interpolate(1, 1.5)==1.5);
		Assert.assertTrue(bcs.interpolate(1.5, 1)==3.5);
		
		Assert.assertTrue(tcs.interpolate(1, 1, 1)==1.0);
		Assert.assertTrue(tcs.interpolate(1, 1, 2)==2.0);
		Assert.assertTrue(tcs.interpolate(1, 2, 1)==6.0);
		Assert.assertTrue(tcs.interpolate(2, 1, 1)==26.0);
		Assert.assertTrue(tcs.interpolate(1, 1, 1.5)==1.5);
		Assert.assertTrue(tcs.interpolate(1, 1.5, 1)==3.5);
		Assert.assertTrue(tcs.interpolate(1.5, 1, 1)==13.5);
		Assert.assertTrue(tcs.interpolate(1, 1, 1.25)==1.25);
		Assert.assertTrue(tcs.interpolate(1, 1.25, 1)==2.25);
		Assert.assertTrue(tcs.interpolate(1.25, 1, 1)==7.25);
		Assert.assertTrue(tcs.interpolate(2.5, 2.5, 2.5)==47.5);
	}
}
