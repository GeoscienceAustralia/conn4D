/*******************************************************************************
 * Copyright 2014 Geoscience Australia (www.ga.gov.au)
 * @author - Johnathan Kool (Geoscience Australia)
 * 
 * Licensed under the BSD-3 License
 * 
 * http://opensource.org/licenses/BSD-3-Clause
 *  
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *  
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package au.gov.ga.conn4d.utils;

import java.util.Arrays;

/**
 * <p>
 * Title: VectorMath
 * </p>
 * <p>
 * Description: A collection of methods for creating and manipulating matrices
 * of primitives.
 * </p>
 * 
 * @author Johnathan Kool
 * @version 0.1
 */

public class VectorMath {

	/**
	 * Adds two vectors of equal dimension together.
	 * 
	 * @param a
	 *            int[][] - The first matrix
	 * @param b
	 *            int[][] - The second matrix
	 * @return int[][] - The result of adding the two matrices.
	 */

	public static double[] add(double[] a, double[] b) {

		double[] sum = new double[a.length];

		for (int i = 0; i < a.length; i++) {

			sum[i] = a[i] + b[i];
		}

		return sum;
	}

	/**
	 * Adds two vectors of equal dimension together.
	 * 
	 * @param a
	 *            int[][] - The first matrix
	 * @param b
	 *            int[][] - The second matrix
	 * @return int[][] - The result of adding the two matrices.
	 */

	public static float[] add(float[] a, float[] b) {

		float[] sum = new float[a.length];

		for (int i = 0; i < a.length; i++) {

			sum[i] = a[i] + b[i];
		}

		return sum;
	}

	/**
	 * Adds two vectors of equal dimension together.
	 * 
	 * @param a
	 *            int[][] - The first matrix
	 * @param b
	 *            int[][] - The second matrix
	 * @return int[][] - The result of adding the two matrices.
	 */

	public static int[] add(int[] a, int[] b) {

		int[] sum = new int[a.length];

		for (int i = 0; i < a.length; i++) {

			sum[i] = a[i] + b[i];
		}

		return sum;
	}

	/**
	 * Adds two vectors of equal dimension together.
	 * 
	 * @param a
	 *            int[][] - The first matrix
	 * @param b
	 *            int[][] - The second matrix
	 * @return int[][] - The result of adding the two matrices.
	 */

	public static long[] add(long[] a, long[] b) {

		long[] sum = new long[a.length];

		for (int i = 0; i < a.length; i++) {

			sum[i] = a[i] + b[i];
		}

		return sum;
	}

	/**
	 * Returns a vector containing a constant value
	 * 
	 * @param length
	 *            int - the length of the desired vector
	 * @param value
	 *            double - an int value for the vector
	 * @return double[] - The constant value vector
	 */

	public static double[] constant(int length, double value) {

		double[] out = new double[length];
		for (int i = 0; i < out.length; i++) {

			out[i] = value;
		}

		return out;
	}

	/**
	 * Returns a vector containing a constant value
	 * 
	 * @param length
	 *            int - the length of the desired vector
	 * @param value
	 *            int - an int value for the vector
	 * @return int[] - The constant value vector
	 */

	public static int[] constant(int length, int value) {

		int[] out = new int[length];
		for (int i = 0; i < out.length; i++) {

			out[i] = value;
		}

		return out;

	}

	/**
	 * Compute the cross product of two vectors
	 * 
	 * @param p1
	 *            The first vector
	 * @param p2
	 *            The second vector
	 * @param result
	 *            Where to store the cross product
	 **/

	public static void cross(double[] p1, double[] p2, double[] result) {
		result[0] = p1[1] * p2[2] - p2[1] * p1[2];
		result[1] = p1[2] * p2[0] - p2[2] * p1[0];
		result[2] = p1[0] * p2[1] - p2[0] * p1[1];
	}

	/**
	 * Compute the cross product of two vectors
	 * 
	 * @param p1
	 *            The first vector
	 * @param p2
	 *            The second vector
	 * @param result
	 *            Where to store the cross product
	 **/

	public static void cross(float[] p1, float[] p2, float[] result) {
		result[0] = p1[1] * p2[2] - p2[1] * p1[2];
		result[1] = p1[2] * p2[0] - p2[2] * p1[0];
		result[2] = p1[0] * p2[1] - p2[0] * p1[1];
	}

	/**
	 * Returns the cumulative form of the supplied array,
	 * 
	 * @param da
	 *            double[]: The array to be accumulated
	 * @return double[]: The cumulative form of the supplied array.
	 */

	public static double[] cumulative(double[] da) {

		double[] cm = new double[da.length];
		double sum = 0;

		for (int i = 0; i < da.length; i++) {

			sum += da[i];
			cm[i] = sum;

		}

		return cm;
	}

	/**
	 * Performs element-wise division of two arrays (vectors)
	 * 
	 * @param num
	 *            - the numerator vector
	 * @param denom
	 *            - the denominator vector
	 * @return - resulting vector
	 */

	public static double[] div(long[] num, long[] denom) {

		int len = num.length;
		if (len != denom.length) {
			throw new IllegalArgumentException(
					"Arrays must be of equal size for division");
		}

		double[] output = new double[len];

		for (int i = 0; i < len; i++) {

			output[i] = (double) num[i] / (double) denom[i];
		}

		return output;
	}

	/**
	 * Compute the dot product of two vectors
	 * 
	 * @param v1
	 *            The first vector
	 * @param v2
	 *            The second vector
	 * @return v1 dot v2
	 **/

	public static double dot(double[] v1, double[] v2) {
		double res = 0;
		for (int i = 0; i < v1.length; i++)
			res += v1[i] * v2[i];
		return res;
	}

	/**
	 * Compute the dot product of two vectors
	 * 
	 * @param v1
	 *            The first vector
	 * @param v2
	 *            The second vector
	 * @return v1 dot v2
	 **/

	public static float dot(float[] v1, float[] v2) {
		float res = 0;
		for (int i = 0; i < v1.length; i++)
			res += v1[i] * v2[i];
		return res;
	}

	/**
	 * Compute the dot product of two vectors
	 * 
	 * @param v1
	 *            The first vector
	 * @param v2
	 *            The second vector
	 * @return v1 dot v2
	 **/

	public static int dot(int[] v1, int[] v2) {
		int res = 0;
		for (int i = 0; i < v1.length; i++)
			res += v1[i] * v2[i];
		return res;
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * 
	 * <p>
	 * This method does nothing for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array
	 *            the array to reverse, may be <code>null</code>
	 */

	public static void flip(double[] array) {
		if (array == null) {
			return;
		}
		int i = 0;
		int j = array.length - 1;
		double tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * 
	 * <p>
	 * This method does nothing for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array
	 *            the array to reverse, may be <code>null</code>
	 */

	public static void flip(float[] array) {
		if (array == null) {
			return;
		}
		int i = 0;
		int j = array.length - 1;
		float tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * 
	 * <p>
	 * This method does nothing for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array
	 *            the array to reverse, may be <code>null</code>
	 */
	public static void flip(int[] array) {
		if (array == null) {
			return;
		}
		int i = 0;
		int j = array.length - 1;
		int tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * 
	 * <p>
	 * This method does nothing for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array
	 *            the array to reverse, may be <code>null</code>
	 */

	public static void flip(long[] array) {
		if (array == null) {
			return;
		}
		int i = 0;
		int j = array.length - 1;
		long tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * 
	 * <p>
	 * This method does nothing for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array
	 *            the array to reverse, may be <code>null</code>
	 */

	public static short[] flip(short[] array) {
		if (array == null) {
			return null;
		}
		int i = 0;
		int j = array.length - 1;
		short tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}

	/**
	 * Guaranteed upper bound for a binary search of an array. If values are
	 * equivalent, default binarySearch can return any value randomly.
	 * 
	 * @param da
	 *            - The array of values to be searched
	 * @param target
	 *            - the target being searched for
	 * @return
	 */

	private static int gBinary_ub(double[] da, double target) {

		int idx = Arrays.binarySearch(da, target);

		if (idx == -1) {
			return -1;
		}

		// If the value is negative...

		if (idx < -1) {
			return Math.abs(idx) - 2;
		}

		while (Math.abs(idx) != da.length - 1 && da[idx] == da[idx + 1]) {
			idx++;
		}

		return idx;

	}

	/**
	 * Guaranteed upper bound for a binary search of an array. If values are
	 * equivalent, default binarySearch can return any value randomly.
	 * 
	 * @param ia
	 *            - The array of values to be searched
	 * @param target
	 *            - The target being searched for
	 * @return
	 */

	private static int gBinary_ub(int[] ia, int target) {

		int idx = Arrays.binarySearch(ia, target);

		// If the value is negative...

		if (idx == -1) {
			return -1;
		}

		if (idx < -1) {
			return Math.abs(idx) - 2;
		}

		while (Math.abs(idx) != ia.length - 1 && ia[idx] == ia[idx + 1]) {
			idx++;
		}

		return idx;

	}

	/**
	 * Guaranteed upper bound for a binary search of an array. If values are
	 * equivalent, default binarySearch can return any value randomly.
	 * 
	 * @param la
	 *            - The array of values to be searched
	 * @param target
	 *            - The target being searched for
	 * @return
	 */

	private static int gBinary_ub(long[] la, long target) {

		int idx = Arrays.binarySearch(la, target);

		// If the value is negative...

		if (idx == -1) {
			return -1;
		}

		if (idx < -1) {
			return Math.abs(idx) - 2;
		}

		while (Math.abs(idx) != la.length - 1 && la[idx] == la[idx + 1]) {
			idx++;
		}

		return idx;

	}

	/**
	 * Converts a list of values into frequencies based on threshold
	 * values,similar to constructing a histogram
	 * 
	 * @param da
	 *            - the array of values
	 * @param th
	 *            - threshold values
	 * @return - the number of elements falling within each bounded class.
	 */

	public static int[] hist(double[] da, double[] th) {

		Arrays.sort(da);
		Arrays.sort(th);
		int[] out = new int[th.length];
		int acc = 0;

		// This will return the cumulative. Subtract by the previous entry

		for (int i = 0; i < th.length; i++) {

			out[i] = gBinary_ub(da, th[i]) + 1 - acc;
			acc = acc + out[i];
			System.out.print("");
		}

		return out;

	}

	/**
	 * Converts a list of values into frequencies based on threshold
	 * values,similar to constructing a histogram
	 * 
	 * @param ia
	 *            - the array of values
	 * @param th
	 *            - threshold values
	 * @return - the number of elements falling within each bounded class.
	 */

	public static int[] hist(int[] ia, int[] th) {

		Arrays.sort(ia);
		Arrays.sort(th);
		int[] out = new int[th.length];
		int acc = 0;

		// This will return the cumulative. Subtract by the previous entry

		for (int i = 0; i < th.length; i++) {

			out[i] = Math.abs(gBinary_ub(ia, th[i])) + 1 - acc;
			acc = acc + out[i];
			System.out.print("");
		}

		return out;

	}

	/**
	 * Converts a list of values into frequencies based on threshold
	 * values,similar to constructing a histogram
	 * 
	 * @param la
	 *            - the array of values
	 * @param th
	 *            - threshold values
	 * @return - the number of elements falling within each bounded class.
	 */

	public static long[] hist(long[] la, long[] th) {

		Arrays.sort(la);
		Arrays.sort(th);
		long[] out = new long[th.length];
		long acc = 0;

		// This will return the cumulative. Subtract by the previous entry

		for (int i = 0; i < th.length; i++) {

			out[i] = Math.abs(gBinary_ub(la, th[i])) + 1 - acc;
			acc = acc + out[i];
			System.out.print("");
		}

		return out;

	}

	/**
	 * Converts a matrix of integers into longs.
	 * 
	 * @param ia
	 *            - The matrix of integer values
	 * @return - The corresponding matrix as long values.
	 */

	public static long[] int2Long(int[] ia) {

		long[] out = new long[ia.length];
		for (int i = 0; i < ia.length; i++) {

			out[i] = ia[i];
		}

		return out;
	}

	/**
	 * Creates a vector of linearly spaced values
	 * 
	 * @param start
	 *            - Starting element value
	 * @param end
	 *            - Ending element value
	 * @param intervals
	 *            - Number of elements
	 * @return - The vector of linearly spaced values
	 */

	public static double[] linspace(double start, double end, int intervals) {

		double range = end - start;
		double spacing = range / (intervals - 1);
		double[] out = new double[intervals];

		out[0] = start;

		for (int i = 1; i < intervals; i++) {

			out[i] = out[i - 1] + spacing;

		}

		return out;

	}

	/**
	 * Creates a vector of linearly spaced values
	 * 
	 * @param start
	 *            - Starting element value
	 * @param end
	 *            - Ending element value
	 * @param intervals
	 *            - Number of elements
	 * @return - The vector of linearly spaced values
	 */

	public static int[] linspace(int start, int end, int intervals) {

		double range = end - start;
		double spacing = range / (intervals - 1);
		int[] out = new int[intervals];

		out[0] = start;

		for (int i = 1; i < intervals; i++) {

			out[i] = (int) (Math.round(out[i - 1] + spacing));

		}

		return out;

	}

	/**
	 * Casts a matrix of long values into integers
	 * 
	 * @param la
	 *            - The matrix of long values
	 * @return - The matrix as corresponding int values
	 */

	public static int[] long2Int(long[] la) {

		int[] out = new int[la.length];
		for (int i = 0; i < la.length; i++) {

			out[i] = (int) la[i];
		}

		return out;
	}

	/**
	 * Compute the magnitude (length) of a vector
	 * 
	 * @param vector
	 *            The vector
	 * @return The magnitude of the vector
	 **/

	public static double magnitude(float[] vector) {
		return Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]
				+ vector[2] * vector[2]);
	}

	/**
	 * Compute the magnitude (length) of a vector
	 * 
	 * @param vector
	 *            The vector
	 * @return The magnitude of the vector
	 **/

	public static double magnitude(int[] vector) {
		return Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]
				+ vector[2] * vector[2]);
	}

	/**
	 * Returns the maximum value of an array of doubles.
	 * 
	 * @param da
	 *            - the input array of doubles
	 * @return - the maximum value contained in the array
	 */

	public static double maximum(double[] da) {
		int n = da.length;
		double max = da[0];
		for (int i = 1; i < n; i++) {
			if (da[i] > max)
				max = da[i];
		}
		return max;
	}

	/**
	 * Returns the maximum value of an array of floats.
	 * 
	 * @param fa
	 *            - the input array of floats
	 * @return - the maximum value contained in the array
	 */

	public static float maximum(float[] fa) {
		int n = fa.length;
		float max = fa[0];
		for (int i = 1; i < n; i++) {
			if (fa[i] > max)
				max = fa[i];
		}
		return max;
	}

	/**
	 * Returns the maximum value of an array of integers.
	 * 
	 * @param ia
	 *            - the input array of integers
	 * @return - the maximum value contained in the array
	 */

	public static int maximum(int[] ia) {
		int n = ia.length;
		int max = ia[0];
		for (int i = 1; i < n; i++) {
			if (ia[i] > max)
				max = ia[i];
		}
		return max;
	}

	/**
	 * Returns the maximum value of an array of longs.
	 * 
	 * @param la
	 *            - the input array of longs
	 * @return - the maximum value contained in the array
	 */

	public static long maximum(long[] la) {
		int n = la.length;
		long max = la[0];
		for (int i = 1; i < n; i++) {
			if (la[i] > max)
				max = la[i];
		}
		return max;
	}
	
	/**
	 * Returns the index of the maximum value of an array of doubles.
	 * 
	 * @param da
	 *            - the input array of doubles
	 * @return - the index of the maximum value contained in the array
	 */

	public static int maxi(double[] da) {
		int n = da.length;
		int max = 0;
		for (int i = 1; i < n; i++) {
			if (da[i] > max)
				max = i;
		}
		return max;
	}
	
	/**
	 * Returns the index of the maximum value of an array of floats.
	 * 
	 * @param fa
	 *            - the input array of doubles
	 * @return - the index of the maximum value contained in the array
	 */

	public static int maxi(float[] fa) {
		int n = fa.length;
		int max = 0;
		for (int i = 1; i < n; i++) {
			if (fa[i] > max)
				max = i;
		}
		return max;
	}
	
	/**
	 * Returns the index of the maximum value of an array of ints.
	 * 
	 * @param ia
	 *            - the input array of doubles
	 * @return - the index of the maximum value contained in the array
	 */

	public static int maxi(int[] ia) {
		int n = ia.length;
		int max = 0;
		for (int i = 1; i < n; i++) {
			if (ia[i] > max)
				max = i;
		}
		return max;
	}
	
	/**
	 * Returns the index of the maximum value of an array of doubles.
	 * 
	 * @param la
	 *            - the input array of longs
	 * @return - the index of the maximum value contained in the array
	 */

	public static int maxi(long[] la) {
		int n = la.length;
		int max = 0;
		for (int i = 1; i < n; i++) {
			if (la[i] > max)
				max = i;
		}
		return max;
	}

	/**
	 * Returns the minimum value of an array of doubles.
	 * 
	 * @param da
	 *            - the input array of doubles
	 * @return - the minimum value contained in the array
	 */

	public static double minimum(double[] da) {
		int n = da.length;
		double min = da[0];
		for (int i = 1; i < n; i++) {
			if (da[i] < min)
				min = da[i];
		}
		return min;
	}

	/**
	 * Returns the minimum value of an array of floats.
	 * 
	 * @param da
	 *            - the input array of doubles
	 * @return - the minimum value contained in the array
	 */

	public static float minimum(float[] da) {
		int n = da.length;
		float min = da[0];
		for (int i = 1; i < n; i++) {
			if (da[i] < min)
				min = da[i];
		}
		return min;
	}

	/**
	 * Returns the minimum value of an array of integers.
	 * 
	 * @param ia
	 *            - the input array of integers
	 * @return - the minimum value contained in the array
	 */

	public static int minimum(int[] ia) {
		int n = ia.length;
		int min = ia[0];
		for (int i = 1; i < n; i++) {
			if (ia[i] < min)
				min = ia[i];
		}
		return min;
	}

	/**
	 * Returns the minimum value of an array of longs.
	 * 
	 * @param la
	 *            - the input array of longs
	 * @return - the minimum value contained in the array
	 */

	public static long minimum(long[] la) {
		int n = la.length;
		long min = la[0];
		for (int i = 1; i < n; i++) {
			if (la[i] < min)
				min = la[i];
		}
		return min;
	}

	/**
	 * Returns a vector with negative values relative to the original
	 * 
	 * @param vector
	 */

	public static double[] negate(double[] vector) {
		for (int i = 0; i < vector.length; i++) {
			vector[i] = -vector[i];
		}
		return vector;
	}

	/**
	 * Returns a vector with negative values relative to the original
	 * 
	 * @param vector
	 */

	public static float[] negate(float[] vector) {
		for (int i = 0; i < vector.length; i++) {
			vector[i] = -vector[i];
		}
		return vector;
	}

	/**
	 * Returns a vector with negative values relative to the original
	 * 
	 * @param vector
	 */

	public static int[] negate(int[] vector) {
		for (int i = 0; i < vector.length; i++) {
			vector[i] = -vector[i];
		}
		return vector;
	}

	/**
	 * Returns a vector with negative values relative to the original
	 * 
	 * @param vector
	 */

	public static long[] negate(long[] vector) {
		for (int i = 0; i < vector.length; i++) {
			vector[i] = -vector[i];
		}
		return vector;
	}

	/**
	 * Divides the vector by its sum to return a 1-norm of the vector
	 * 
	 * @param vector
	 */

	public static double[] norm1(double[] vector) {
		double sum = sumAsDouble(vector);
		double[] out = new double[vector.length];
		for (int i = 0; i < vector.length; i++) {
			out[i] = vector[i] / sum;
		}
		return out;
	}

	/**
	 * Divides the vector by its sum to return a 1-norm of the vector
	 * 
	 * @param vector
	 */

	public static float[] norm1(float[] vector) {
		float sum = sumAsFloat(vector);
		float[] out = new float[vector.length];
		for (int i = 0; i < vector.length; i++) {
			out[i] = vector[i] / sum;
		}
		return out;
	}

	/**
	 * Sort an array of doubles into ascending order
	 */

	public static double[] selectionSort(double[] da) {
		int index = 0;
		int lastIndex = -1;
		int n = da.length;
		double temp = 0.0D;
		double[] sorted = new double[n];
		for (int i = 0; i < n; i++) {
			sorted[i] = da[i];
		}

		while (lastIndex != n - 1) {
			index = lastIndex + 1;
			for (int i = lastIndex + 2; i < n; i++) {
				if (sorted[i] < sorted[index]) {
					index = i;
				}
			}
			lastIndex++;
			temp = sorted[index];
			sorted[index] = sorted[lastIndex];
			sorted[lastIndex] = temp;
		}
		return sorted;
	}

	/**
	 * Sort an array of floats into ascending order
	 */

	public static float[] selectionSort(float[] fa) {
		int index = 0;
		int lastIndex = -1;
		int n = fa.length;
		float temp = 0.0f;
		float[] sorted = new float[n];
		for (int i = 0; i < n; i++) {
			sorted[i] = fa[i];
		}

		while (lastIndex != n - 1) {
			index = lastIndex + 1;
			for (int i = lastIndex + 2; i < n; i++) {
				if (sorted[i] < sorted[index]) {
					index = i;
				}
			}
			lastIndex++;
			temp = sorted[index];
			sorted[index] = sorted[lastIndex];
			sorted[lastIndex] = temp;
		}
		return sorted;
	}

	/**
	 * Sort an array of integers into ascending order 
	 */

	public static int[] selectionSort(int[] ia) {
		int index = 0;
		int lastIndex = -1;
		int n = ia.length;
		int temp = 0;
		int[] sorted = new int[n];
		for (int i = 0; i < n; i++) {
			sorted[i] = ia[i];
		}

		while (lastIndex != n - 1) {
			index = lastIndex + 1;
			for (int i = lastIndex + 2; i < n; i++) {
				if (sorted[i] < sorted[index]) {
					index = i;
				}
			}
			lastIndex++;
			temp = sorted[index];
			sorted[index] = sorted[lastIndex];
			sorted[lastIndex] = temp;
		}
		return sorted;
	}

	/**
	 * Sort an array of longs into ascending order
	 */

	public static long[] selectionSort(long[] da) {
		int index = 0;
		int lastIndex = -1;
		int n = da.length;
		long temp = 0L;
		long[] sorted = new long[n];
		for (int i = 0; i < n; i++) {
			sorted[i] = da[i];
		}

		while (lastIndex != n - 1) {
			index = lastIndex + 1;
			for (int i = lastIndex + 2; i < n; i++) {
				if (sorted[i] < sorted[index]) {
					index = i;
				}
			}
			lastIndex++;
			temp = sorted[index];
			sorted[index] = sorted[lastIndex];
			sorted[lastIndex] = temp;
		}
		return sorted;
	}

	/**
	 * Sorts an array of doubles into ascending order
	 */

	public static void selectionSort(double[] original, double[] sorted,
			int[] indices) {
		int index = 0;
		int lastIndex = -1;
		int n = original.length;
		double temp = 0.0D;
		int temp_idx = 0;
		
		for (int i = 0; i < n; i++) {
			sorted[i] = original[i];
			indices[i] = i;
		}

		while (lastIndex != n - 1) {
			index = lastIndex + 1;
			for (int i = lastIndex + 2; i < n; i++) {
				if (sorted[i] < sorted[index]) {
					index = i;
				}
			}
			lastIndex++;
			temp = sorted[index];
			sorted[index] = sorted[lastIndex];
			sorted[lastIndex] = temp;
			temp_idx = indices[index];
			indices[index] = indices[lastIndex];
			indices[lastIndex] = temp_idx;
		}
	}
	
	// sort the elements of an array of doubles into ascending order with
	// matching switches in an array of the length
	// using selection sort method
	// array determining the order is the first argument
	// matching array is the second argument
	// sorted arrays returned as third and fourth arguments respectively
	public static void selectionSort(double[] aa, double[] bb, double[] cc,
			double[] dd) {
		int index = 0;
		int lastIndex = -1;
		int n = aa.length;
		int m = bb.length;
		if (n != m)
			throw new IllegalArgumentException(
					"First argument array, aa, (length = " + n
							+ ") and the second argument array, bb, (length = "
							+ m + ") should be the same length");
		int nn = cc.length;
		if (nn < n)
			throw new IllegalArgumentException(
					"The third argument array, cc, (length = "
							+ nn
							+ ") should be at least as long as the first argument array, aa, (length = "
							+ n + ")");
		int mm = dd.length;
		if (mm < m)
			throw new IllegalArgumentException(
					"The fourth argument array, dd, (length = "
							+ mm
							+ ") should be at least as long as the second argument array, bb, (length = "
							+ m + ")");

		double holdx = 0.0D;
		double holdy = 0.0D;

		for (int i = 0; i < n; i++) {
			cc[i] = aa[i];
			dd[i] = bb[i];
		}

		while (lastIndex != n - 1) {
			index = lastIndex + 1;
			for (int i = lastIndex + 2; i < n; i++) {
				if (cc[i] < cc[index]) {
					index = i;
				}
			}
			lastIndex++;
			holdx = cc[index];
			cc[index] = cc[lastIndex];
			cc[lastIndex] = holdx;
			holdy = dd[index];
			dd[index] = dd[lastIndex];
			dd[lastIndex] = holdy;
		}
	}

	/**
	 * Subtracts two vectors of equal dimension.
	 * 
	 * @param a
	 *            int[] - The first matrix
	 * @param b
	 *            int[] - The second matrix
	 * @return int[] - The result of adding the two matrices.
	 */

	public static double[] subtract(double[] a, double[] b) {

		double[] sum = new double[a.length];

		for (int i = 0; i < a.length; i++) {

			sum[i] = a[i] - b[i];
		}

		return sum;
	}

	/**
	 * Subtracts two vectors of equal dimension.
	 * 
	 * @param a
	 *            int[] - The first matrix
	 * @param b
	 *            int[] - The second matrix
	 * @return int[] - The result of adding the two matrices.
	 */

	public static float[] subtract(float[] a, float[] b) {

		float[] sum = new float[a.length];

		for (int i = 0; i < a.length; i++) {

			sum[i] = a[i] - b[i];
		}

		return sum;
	}

	/**
	 * Subtracts two vectors of equal dimension.
	 * 
	 * @param a
	 *            int[] - The first matrix
	 * @param b
	 *            int[] - The second matrix
	 * @return int[] - The result of adding the two matrices.
	 */

	public static int[] subtract(int[] a, int[] b) {

		int[] sum = new int[a.length];

		for (int i = 0; i < a.length; i++) {

			sum[i] = a[i] - b[i];
		}

		return sum;
	}

	/**
	 * Subtracts two vectors of equal dimension.
	 * 
	 * @param a
	 *            long[] - The first matrix
	 * @param b
	 *            long[] - The second matrix
	 * @return long[] - The result of adding the two matrices.
	 */

	public static long[] subtract(long[] a, long[] b) {

		long[] sum = new long[a.length];

		for (int i = 0; i < a.length; i++) {

			sum[i] = a[i] - b[i];
		}

		return sum;
	}

	/**
	 * Returns the sum of the supplied vector as a double
	 * 
	 * @param doubleArray
	 *            long[] - A vector of longs
	 * @return double - The sum of the vector.
	 */

	public static double sumAsDouble(double[] doubleArray) {
		double sum = 0;

		for (double d : doubleArray) {
			sum += d;
		}

		return sum;

	}

	/**
	 * Returns the sum of the supplied vector as a double
	 * 
	 * @param longArray
	 *            long[] - A vector of longs
	 * @return double - The sum of the vector.
	 */

	public static double sumAsDouble(long[] longArray) {
		double sum = 0;

		for (double d : longArray) {
			sum += d;
		}

		return sum;

	}

	/**
	 * Returns the sum of the supplied vector as a float
	 * 
	 * @param floatArray
	 *            long[] - A vector of longs
	 * @return float - The sum of the vector.
	 */

	public static float sumAsFloat(float[] floatArray) {
		float sum = 0;

		for (float f : floatArray) {
			sum += f;
		}

		return sum;

	}

	/**
	 * Returns the sum of the supplied vector as an int
	 * 
	 * @param intArray
	 *            long[] - A vector of ints
	 * @return int - The sum of the vector.
	 */

	public static int sumAsInt(int[] intArray) {
		int sum = 0;

		for (int i : intArray) {
			sum += i;
		}

		return sum;

	}

	/**
	 * Returns the sum of the supplied vector as a long
	 * 
	 * @param longArray
	 *            long[] - A vector of longs
	 * @return long - The sum of the vector.
	 */

	public static long sumAsLong(long[] longArray) {
		long sum = 0;

		for (long l : longArray) {
			sum += l;
		}

		return sum;
	}

	/**
	 * Returns the sample variance of the array values using a one-pass
	 * algorithm.
	 * 
	 * @param fa
	 *            - A vector of floats
	 * @return - The variance of the vector
	 */

	public static float variance(float[] fa) {
		int n = 0;
		float mean = 0;
		float M2 = 0;

		if (fa == null || fa.length == 0) {
			return Float.NaN;
		}

		for (int i = 0; i < fa.length; i++) {
			n = n + 1;
			float delta = fa[i] - mean;
			mean = mean + delta / n;
			M2 = M2 + delta * (fa[i] - mean);
		}
		float variance = M2 / (n - 1);
		return variance;
	}

	/**
	 * Retrieves the weighted variance given an array of float values and their
	 * corresponding weight.
	 * 
	 * @param fa
	 *            - an array of float values
	 * @param weights
	 *            - the relative weight of the value in fa at the corresponding
	 *            position.
	 */

	public static float weighted_variance(float[] fa, float[] weights) {

		float sumweight = 0;
		float mean = 0;
		float M2 = 0;
		int n = fa.length;

		for (int i = 0; i < n; i++) {
			float temp = weights[i] + sumweight;
			float delta = fa[i] - mean;
			float R = delta * weights[i] / temp;
			mean = mean + R;
			M2 = M2 + sumweight * delta * R;
			sumweight = temp;
		}

		float variance_n = M2 / sumweight;
		return variance_n * n / (n - 1);
	}
}
