package au.gov.ga.conn4d.test.utils;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.conn4d.utils.VectorMath;

public class TestVectorMath {

	double[] da = new double[] { 1d, 2d };
	double[] db = new double[] { 3d, 4d };
	double[] dx = new double[] { 1d, 0d, 0d};
	double[] dy = new double[] { 0d, 1d, 0d};
	double[] dz = new double[] { 0d, 0d, 1d};
	float[] fa = new float[] { 1f, 2f };
	float[] fb = new float[] { 3f, 4f };
	float[] fx = new float[] { 1f, 0f, 0f};
	float[] fy = new float[] { 0f, 1f, 0f};
	float[] fz = new float[] { 0f, 0f, 1f};
	int[] ia = new int[] { 1, 2 };
	int[] ib = new int[] { 3, 4 };
	long[] la = new long[] { 1l, 2l };
	long[] lb = new long[] { 3l, 4l };
	double eps = 1E-8;

	@Test
	public void testAddDoubleArrayDoubleArray() {
		Assert.assertArrayEquals(new double[] { 4d, 6d },
				VectorMath.add(da, db), eps);
	}

	@Test
	public void testAddFloatArrayFloatArray() {
		Assert.assertArrayEquals(new float[] { 4f, 6f },
				VectorMath.add(fa, fb), (float) eps);
	}

	@Test
	public void testAddIntArrayIntArray() {
		Assert.assertArrayEquals(new int[] { 4, 6 }, VectorMath.add(ia, ib));
	}

	@Test
	public void testAddLongArrayLongArray() {
		Assert.assertArrayEquals(new long[] { 4l, 6l }, VectorMath.add(la, lb));
	}

	@Test
	public void testConstantIntDouble() {
		Assert.assertArrayEquals(new double[] { Math.E, Math.E, Math.E },
				VectorMath.constant(3, Math.E), eps);
	}

	@Test
	public void testConstantIntInt() {
		Assert.assertArrayEquals(new int[] { 42, 42, 42, 42, 42, 42, 42 },
				VectorMath.constant(7, 42));
	}

	@Test
	public void testCrossDoubleArrayDoubleArrayDoubleArray() {
		double[] result = new double[3];
		VectorMath.cross(dx, dy,result);
		Assert.assertArrayEquals(new double[]{0d,0d,1d},result,eps);
	}
/*
	@Test
	public void testCrossFloatArrayFloatArrayFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testCumulative() {
		fail("Not yet implemented");
	}

	@Test
	public void testDiv() {
		fail("Not yet implemented");
	}

	@Test
	public void testDotDoubleArrayDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testDotFloatArrayFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testDotIntArrayIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testFlipDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testFlipFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testFlipIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testFlipLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testFlipShortArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testHistDoubleArrayDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testHistIntArrayIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testHistLongArrayLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testInt2Long() {
		fail("Not yet implemented");
	}

	@Test
	public void testLinspaceDoubleDoubleInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testLinspaceIntIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testLong2Int() {
		fail("Not yet implemented");
	}

	@Test
	public void testMagnitudeFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMagnitudeIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testNegateDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testNegateFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testNegateIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testNegateLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testNorm1DoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testNorm1FloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubtractDoubleArrayDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubtractFloatArrayFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubtractIntArrayIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubtractLongArrayLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSumAsDoubleDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSumAsDoubleLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSumAsFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testSumAsInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testSumAsLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testVariance() {
		fail("Not yet implemented");
	}

	@Test
	public void testWeighted_variance() {
		fail("Not yet implemented");
	}*/

}
