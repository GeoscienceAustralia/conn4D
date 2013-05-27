package au.gov.ga.conn4d.test.utils;

import junit.framework.Assert;

import org.junit.Test;

import au.gov.ga.conn4d.utils.Fmath;

public class TestFMath {

	private final double eps = 1E-7;
	private final float epsf = 1E-7f;

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
	public void testAntilog10Double() {
		Assert.assertEquals(100, Fmath.antilog10(2d),eps);
		Assert.assertEquals(1000, Fmath.antilog10(3d),eps);
		Assert.assertEquals(Math.pow(10.0d, Math.PI), Fmath.antilog10(Math.PI),eps);
	}

	@Test
	public void testAntilog10Float() {
		Assert.assertEquals(100, Fmath.antilog10(2f),epsf);
		Assert.assertEquals(1000, Fmath.antilog10(3f),epsf);
		Assert.assertEquals(Math.pow(10.0f, (float) Math.E), Fmath.antilog10((float) Math.E),1E-4);
	}

	@Test
	public void testLogDouble() {
		Assert.assertEquals(2, Fmath.log(Math.exp(2d)),eps);
		Assert.assertEquals(3, Fmath.log(Math.exp(3d)),eps);
		Assert.assertEquals(Math.PI, Fmath.log(Math.exp(Math.PI)),eps);
	}

	@Test
	public void testLogFloat() {
		Assert.assertEquals(2, Fmath.log(Math.exp(2d)),eps);
		Assert.assertEquals(3, Fmath.log(Math.exp(3d)),eps);
		Assert.assertEquals(Math.PI, Fmath.log(Math.exp(Math.PI)),eps);
	}

	/*@Test
	public void testAntilogDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testAntilogFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testLog2Double() {
		fail("Not yet implemented");
	}

	@Test
	public void testLog2Float() {
		fail("Not yet implemented");
	}

	@Test
	public void testAntilog2Double() {
		fail("Not yet implemented");
	}

	@Test
	public void testAntilog2Float() {
		fail("Not yet implemented");
	}

	@Test
	public void testLog10DoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testLog10DoubleInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testLog10FloatFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testLog10FloatInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testSquareDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testSquareFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testSquareBigDecimal() {
		fail("Not yet implemented");
	}

	@Test
	public void testSquareInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testSquareLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testSquareBigInteger() {
		fail("Not yet implemented");
	}

	@Test
	public void testFactorialInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testFactorialLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testFactorialBigInteger() {
		fail("Not yet implemented");
	}

	@Test
	public void testFactorialDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testFactorialBigDecimal() {
		fail("Not yet implemented");
	}

	@Test
	public void testLogFactorialInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testLogFactorialLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testLogFactorialDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testSignDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testSignFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testSignInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testSignLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testHypotDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testHypotFloatFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testAngleDoubleDoubleDoubleDoubleDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testAngleDoubleDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testSinDoubleDoubleDoubleDoubleDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testSinDoubleDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testSinDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testAsin() {
		fail("Not yet implemented");
	}

	@Test
	public void testCosDoubleDoubleDoubleDoubleDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testCosDoubleDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testCosDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testAcos() {
		fail("Not yet implemented");
	}

	@Test
	public void testTanDoubleDoubleDoubleDoubleDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testTanDoubleDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testTanDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testAtan() {
		fail("Not yet implemented");
	}

	@Test
	public void testAtan2() {
		fail("Not yet implemented");
	}

	@Test
	public void testCot() {
		fail("Not yet implemented");
	}

	@Test
	public void testAcot() {
		fail("Not yet implemented");
	}

	@Test
	public void testAcot2() {
		fail("Not yet implemented");
	}

	@Test
	public void testSec() {
		fail("Not yet implemented");
	}

	@Test
	public void testAsec() {
		fail("Not yet implemented");
	}

	@Test
	public void testCsc() {
		fail("Not yet implemented");
	}

	@Test
	public void testAcsc() {
		fail("Not yet implemented");
	}

	@Test
	public void testExsec() {
		fail("Not yet implemented");
	}

	@Test
	public void testAexsec() {
		fail("Not yet implemented");
	}

	@Test
	public void testVers() {
		fail("Not yet implemented");
	}

	@Test
	public void testAvers() {
		fail("Not yet implemented");
	}

	@Test
	public void testCovers() {
		fail("Not yet implemented");
	}

	@Test
	public void testAcovers() {
		fail("Not yet implemented");
	}

	@Test
	public void testHav() {
		fail("Not yet implemented");
	}

	@Test
	public void testAhav() {
		fail("Not yet implemented");
	}

	@Test
	public void testSinc() {
		fail("Not yet implemented");
	}

	@Test
	public void testNsinc() {
		fail("Not yet implemented");
	}

	@Test
	public void testSinh() {
		fail("Not yet implemented");
	}

	@Test
	public void testAsinh() {
		fail("Not yet implemented");
	}

	@Test
	public void testCosh() {
		fail("Not yet implemented");
	}

	@Test
	public void testAcosh() {
		fail("Not yet implemented");
	}

	@Test
	public void testTanh() {
		fail("Not yet implemented");
	}

	@Test
	public void testAtanh() {
		fail("Not yet implemented");
	}

	@Test
	public void testCoth() {
		fail("Not yet implemented");
	}

	@Test
	public void testAcoth() {
		fail("Not yet implemented");
	}

	@Test
	public void testSech() {
		fail("Not yet implemented");
	}

	@Test
	public void testAsech() {
		fail("Not yet implemented");
	}

	@Test
	public void testCsch() {
		fail("Not yet implemented");
	}

	@Test
	public void testAcsch() {
		fail("Not yet implemented");
	}

	@Test
	public void testTruncateDoubleInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testTruncateFloatInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsInfinityDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsInfinityFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsPlusInfinityDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsPlusInfinityFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsMinusInfinityDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsMinusInfinityFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsNaNDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsNaNFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualFloatFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualCharChar() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinLimitsDoubleDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinLimitsFloatFloatFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinLimitsLongLongLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinLimitsIntIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinLimitsBigDecimalBigDecimalBigDecimal() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinLimitsBigIntegerBigIntegerBigInteger() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinPerCentDoubleDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinPerCentFloatFloatFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinPerCentLongLongDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinPerCentLongLongLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinPerCentIntIntDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinPerCentIntIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinPerCentBigDecimalBigDecimalBigDecimal() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinPerCentBigIntegerBigIntegerBigDecimal() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEqualWithinPerCentBigIntegerBigIntegerBigInteger() {
		fail("Not yet implemented");
	}

	@Test
	public void testCompareDoubleDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testCompareIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testCompareLongLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testCompareFloatFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testCompareByteByte() {
		fail("Not yet implemented");
	}

	@Test
	public void testCompareShortShort() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsIntegerDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsIntegerDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsIntegerFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsIntegerFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsIntegerNumber() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsIntegerNumberArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEvenInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEvenFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEvenDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsOddInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsOddFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsOddDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testLeapYear() {
		fail("Not yet implemented");
	}

	@Test
	public void testDateToJavaMilliS() {
		fail("Not yet implemented");
	}

	@Test
	public void testMaximumDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMaximumFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMaximumIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMaximumLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMinimumDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMinimumFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMinimumIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMinimumLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMaximumDifferenceDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMaximumDifferenceFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMaximumDifferenceLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMaximumDifferenceIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMinimumDifferenceDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMinimumDifferenceFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMinimumDifferenceLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMinimumDifferenceIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testReverseArrayDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testReverseArrayFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testReverseArrayIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testReverseArrayLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testReverseArrayCharArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayAbsDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayAbsFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayAbsLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayAbsIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayMultByConstantDoubleArrayDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayMultByConstantIntArrayDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayMultByConstantDoubleArrayInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayMultByConstantIntArrayInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testLog10ElementsDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testLog10ElementsFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testLnElementsDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testLnElementsFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSquareRootElementsDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSquareRootElementsFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testRaiseElementsToPowerDoubleArrayDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testRaiseElementsToPowerDoubleArrayInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testRaiseElementsToPowerFloatArrayFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testRaiseElementsToPowerFloatArrayInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testInvertElementsDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testInvertElementsFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndicesOfDoubleArrayDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndicesOfFloatArrayFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndicesOfLongArrayLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndicesOfIntArrayInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndicesOfShortArrayShort() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndicesOfByteArrayByte() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndicesOfCharArrayChar() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndicesOfStringArrayString() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndicesOfObjectArrayObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfDoubleArrayDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfFloatArrayFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfLongArrayLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfIntArrayInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfByteArrayByte() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfShortArrayShort() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfCharArrayChar() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfStringArrayString() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfObjectArrayObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testNearestElementValueDoubleArrayDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testNearestElementIndexDoubleArrayDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testNearestLowerElementValueDoubleArrayDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testNearestLowerElementIndexDoubleArrayDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testNearestHigherElementValueDoubleArrayDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testNearestHigherElementIndexDoubleArrayDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testNearestElementValueIntArrayInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testNearestElementIndexIntArrayInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testNearestLowerElementValueIntArrayInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testNearestLowerElementIndexIntArrayInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testNearestHigherElementValueIntArrayInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testNearestHigherElementIndexIntArrayInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testArraySumDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testArraySumFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testArraySumIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testArraySumLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayPositiveElementsSum() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayProductDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayProductFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayProductIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testArrayProductLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testConcatenateDoubleArrayDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testConcatenateFloatArrayFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testConcatenateIntArrayIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testConcatenateLongArrayLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testConcatenateShortArrayShortArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testConcatenateByteArrayByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testConcatenateCharArrayCharArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testConcatenateStringArrayStringArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testConcatenateObjectArrayObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testFloatTOdouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIntTOdouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testIntTOfloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testIntTOlong() {
		fail("Not yet implemented");
	}

	@Test
	public void testLongTOdouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testLongTOfloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testShortTOdouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testShortTOfloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testShortTOlong() {
		fail("Not yet implemented");
	}

	@Test
	public void testShortTOint() {
		fail("Not yet implemented");
	}

	@Test
	public void testByteTOdouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testByteTOfloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testByteTOlong() {
		fail("Not yet implemented");
	}

	@Test
	public void testByteTOint() {
		fail("Not yet implemented");
	}

	@Test
	public void testByteTOshort() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoubleTOint() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintlnDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintlnFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintlnIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintlnLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintCharArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintlnCharArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintStringArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintlnStringArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintShortArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintlnShortArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintlnByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintDoubleArrayArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectSortVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectSortArrayList() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortDoubleArrayDoubleArrayIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortDoubleArrayDoubleArrayDoubleArrayDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortFloatArrayFloatArrayFloatArrayFloatArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortLongArrayLongArrayLongArrayLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortIntArrayIntArrayIntArrayIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortDoubleArrayLongArrayDoubleArrayLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortLongArrayDoubleArrayLongArrayDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortDoubleArrayIntArrayDoubleArrayIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortIntArrayDoubleArrayIntArrayDoubleArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortLongArrayIntArrayLongArrayIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectionSortIntArrayLongArrayIntArrayLongArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectSort() {
		fail("Not yet implemented");
	}*/

}
