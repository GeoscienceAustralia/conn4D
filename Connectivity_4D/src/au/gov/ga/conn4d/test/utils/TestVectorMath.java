package au.gov.ga.conn4d.test.utils;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.conn4d.utils.VectorMath;

/**
 * Tests for VectorMath
 * 
 * @author Johnathan Kool
 */

public class TestVectorMath {

	private double[] da = new double[] { 1d, 2d };
	private double[] db = new double[] { 3d, 4d };
	private double[] dx = new double[] { 1d, 0d, 0d };
	private double[] dy = new double[] { 0d, 1d, 0d };
	private float[] fa = new float[] { 1f, 2f };
	private float[] fb = new float[] { 3f, 4f };
	private float[] fx = new float[] { 1f, 0f, 0f };
	private float[] fy = new float[] { 0f, 1f, 0f };
	private int[] ia = new int[] { 1, 2 };
	private int[] ib = new int[] { 3, 4 };
	private long[] la = new long[] { 1l, 2l };
	private long[] lb = new long[] { 3l, 4l };
	private double eps = 1E-8;
	private float epsf = 1E-8f;

	@Test
	public void testAddDoubleArrayDoubleArray() {
		Assert.assertArrayEquals(new double[] { 4d, 6d },
				VectorMath.add(da, db), eps);
	}

	@Test
	public void testAddFloatArrayFloatArray() {
		Assert.assertArrayEquals(new float[] { 4f, 6f },
				VectorMath.add(fa, fb), epsf);
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
		VectorMath.cross(dx, dy, result);
		Assert.assertArrayEquals(new double[] { 0d, 0d, 1d }, result, eps);
	}

	@Test
	public void testCrossFloatArrayFloatArrayFloatArray() {
		float[] result = new float[3];
		VectorMath.cross(fx, fy, result);
		Assert.assertArrayEquals(new float[] { 0f, 0f, 1f }, result, epsf);
	}

	@Test
	public void testCumulative() {
		Assert.assertArrayEquals(new double[] { 0, 1, 2, 4, 7, 12, 20 },
				VectorMath.cumulative(new double[] { 0, 1, 1, 2, 3, 5, 8 }),
				eps);
	}

	@Test
	public void testDiv() {
		Assert.assertArrayEquals(new double[] { 3d, 2d },
				VectorMath.div(lb, la), eps);
		try {
			VectorMath.div(lb, new long[] { 1, 2, 3, 4, 5 });
			Assert.fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testDotDoubleArrayDoubleArray() {
		Assert.assertEquals(11d, VectorMath.dot(da, db), eps);
	}

	@Test
	public void testDotFloatArrayFloatArray() {
		Assert.assertEquals(11f, VectorMath.dot(fa, fb), eps);
	}

	@Test
	public void testDotIntArrayIntArray() {
		Assert.assertEquals(11, VectorMath.dot(ia, ib));
	}

	@Test
	public void testFlipDoubleArray() {
		double[] nl = null;
		VectorMath.flip(nl);
		Assert.assertNull(nl);
		double[] result = new double[] { 1, 2, 3, 4, 5 };
		VectorMath.flip(result);
		Assert.assertArrayEquals(new double[] { 5, 4, 3, 2, 1 }, result, eps);
	}

	@Test
	public void testFlipFloatArray() {
		float[] nl = null;
		VectorMath.flip(nl);
		Assert.assertNull(nl);
		float[] result = new float[] { 1, 2, 3, 4, 5 };
		VectorMath.flip(result);
		Assert.assertArrayEquals(new float[] { 5, 4, 3, 2, 1 }, result, epsf);
	}

	@Test
	public void testFlipIntArray() {
		int[] nl = null;
		VectorMath.flip(nl);
		Assert.assertNull(nl);
		int[] result = new int[] { 1, 2, 3, 4, 5 };
		VectorMath.flip(result);
		Assert.assertArrayEquals(new int[] { 5, 4, 3, 2, 1 }, result);
	}

	@Test
	public void testFlipLongArray() {
		long[] nl = null;
		VectorMath.flip(nl);
		Assert.assertNull(nl);
		long[] result = new long[] { 1, 2, 3, 4, 5 };
		VectorMath.flip(result);
		Assert.assertArrayEquals(new long[] { 5, 4, 3, 2, 1 }, result);
	}

	@Test
	public void testFlipShortArray() {
		short[] nl = null;
		VectorMath.flip(nl);
		Assert.assertNull(nl);
		short[] result = new short[] { 1, 2, 3, 4, 5 };
		VectorMath.flip(result);
		Assert.assertArrayEquals(new short[] { 5, 4, 3, 2, 1 }, result);
	}

	@Test
	public void testHistDoubleArrayDoubleArray() {
		double[] vals = new double[] { 0, 0, 1, 1, 1, 1.5, 1.7, 2, 8, 10 };
		double[] bins = new double[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		int[] hist = VectorMath.hist(vals, bins);
		Assert.assertArrayEquals(new int[] { 2, 3, 3, 0, 0, 0, 0, 0, 1, 0, 1 },
				hist);
	}

	@Test
	public void testHistIntArrayDoubleArray() {
		int[] vals = new int[] { 0, 0, 1, 1, 1, 2, 2, 2, 8, 10 };
		int[] bins = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		int[] hist = VectorMath.hist(vals, bins);
		Assert.assertArrayEquals(new int[] { 2, 3, 3, 0, 0, 0, 0, 0, 1, 0, 1 },
				hist);
	}

	@Test
	public void testHistLongArrayDoubleArray() {
		long[] vals = new long[] { 0, 0, 1, 1, 1, 2, 2, 2, 8, 10 };
		long[] bins = new long[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		long[] hist = VectorMath.hist(vals, bins);
		Assert.assertArrayEquals(
				new long[] { 2, 3, 3, 0, 0, 0, 0, 0, 1, 0, 1 }, hist);
	}

	@Test
	public void testInt2Long() {
		Assert.assertArrayEquals(new long[] { 1l, 2l, 3l, 4l, 5l },
				VectorMath.int2Long(new int[] { 1, 2, 3, 4, 5 }));
	}

	@Test
	public void testLinspaceDoubleDoubleInt() {
		Assert.assertArrayEquals(new double[] { 0d, 1d, 2d, 3d, 4d, 5d, 6d, 7d,
				8d, 9d, 10d }, VectorMath.linspace(0d, 10d, 11), eps);
	}

	@Test
	public void testLinspaceIntIntInt() {
		Assert.assertArrayEquals(
				new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
				VectorMath.linspace(0, 10, 11));
	}

	@Test
	public void testLong2Int() {
		Assert.assertArrayEquals(new int[] { 1, 2, 3, 4, 5 },
				VectorMath.long2Int(new long[] { 1l, 2l, 3l, 4l, 5l }));
	}

	@Test
	public void testMagnitudeFloatArray() {
		Assert.assertEquals(Math.sqrt(3),
				VectorMath.magnitude(new float[] { 1f, 1f, 1f }), 1E-7);
	}

	@Test
	public void testMagnitudeIntArray() {
		Assert.assertEquals(Math.sqrt(3),
				VectorMath.magnitude(new int[] { 1, 1, 1 }), 1E-7);
	}

	@Test
	public void testNegateDoubleArray() {
		Assert.assertArrayEquals(
				new double[] { Math.PI, 2d, 1d, 0d, -1d, -2d, -Math.PI },
				VectorMath.negate(new double[] { -Math.PI, -2d, -1d, 0d, 1d,
						2d, Math.PI }), eps);
	}

	@Test
	public void testNegateFloatArray() {
		Assert.assertArrayEquals(new float[] { 2f, 1f, 0f, -1f, -2f },
				VectorMath.negate(new float[] { -2f, -1f, 0f, 1f, 2f }), epsf);
	}

	@Test
	public void testNegateIntArray() {
		Assert.assertArrayEquals(new int[] { 2, 1, 0, -1, -2 },
				VectorMath.negate(new int[] { -2, -1, 0, 1, 2 }));
	}

	@Test
	public void testNegateLongArray() {
		Assert.assertArrayEquals(new long[] { 2l, 1l, 0l, -1l, -2l },
				VectorMath.negate(new long[] { -2l, -1l, 0l, 1l, 2l }));
	}

	@Test
	public void testNorm1DoubleArray() {
		Assert.assertArrayEquals(new double[] { 0d, 0.1d, 0.2d, 0.3d, 0.4d },
				VectorMath.norm1(new double[] { 0d, 1d, 2d, 3d, 4d }), eps);
	}

	@Test
	public void testNorm1FloatArray() {
		Assert.assertArrayEquals(new float[] { 0f, 0.1f, 0.2f, 0.3f, 0.4f },
				VectorMath.norm1(new float[] { 0f, 1f, 2f, 3f, 4f }), epsf);
	}

	@Test
	public void testSubtractDoubleArrayDoubleArray() {
		Assert.assertArrayEquals(new double[] { 2d, 2d },
				VectorMath.subtract(db, da), eps);
	}

	@Test
	public void testSubtractFloatArrayFloatArray() {
		Assert.assertArrayEquals(new float[] { 2f, 2f },
				VectorMath.subtract(fb, fa), epsf);
	}

	@Test
	public void testSubtractIntArrayIntArray() {
		Assert.assertArrayEquals(new int[] { 2, 2 },
				VectorMath.subtract(ib, ia));
	}

	@Test
	public void testSubtractLongArrayLongArray() {
		Assert.assertArrayEquals(new long[] { 2l, 2l },
				VectorMath.subtract(lb, la));
	}

	@Test
	public void testSumAsDoubleDoubleArray() {
		Assert.assertEquals(10d,
				VectorMath.sumAsDouble(new double[] { 0d, 1d, 2d, 3d, 4d }),
				eps);
	}

	@Test
	public void testSumAsDoubleLongArray() {
		Assert.assertEquals(10d,
				VectorMath.sumAsDouble(new long[] { 0l, 1l, 2l, 3l, 4l }), eps);
	}

	@Test
	public void testSumAsFloat() {
		Assert.assertEquals(10f,
				VectorMath.sumAsFloat(new float[] { 0f, 1f, 2f, 3f, 4f }), eps);
	}

	@Test
	public void testSumAsInt() {
		Assert.assertEquals(10,
				VectorMath.sumAsInt(new int[] { 0, 1, 2, 3, 4 }));
	}

	@Test
	public void testSumAsLong() {
		Assert.assertEquals(10l,
				VectorMath.sumAsLong(new long[] { 0l, 1l, 2l, 3l, 4l }));
	}

	@Test
	public void testVariance() {
		Assert.assertEquals(3.5,
				VectorMath.variance(new float[] { 1, 2, 3, 4, 5, 6 }), eps);
		Assert.assertEquals(Float.NaN, VectorMath.variance(new float[] {}),
				Float.MIN_VALUE);
		Assert.assertEquals(Float.NaN, VectorMath.variance(new float[] { 1 }),
				Float.MIN_VALUE);
		Assert.assertEquals(0.5, VectorMath.variance(new float[] { 1, 2 }), eps);
	}

	@Test
	public void testWeighted_variance() {
		Assert.assertEquals(1.8, VectorMath.weighted_variance(new float[] { 1, 2, 3, 4, 5, 6 },new float[]{1,2,5,5,2,1}), 1E-6);
	}

}
