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

/**
 * Additional binary search-type functions for searching arrays. Additions
 * include searching with prior positional expectation, and searching on
 * descending arrays without re-sorting.
 * 
 */

public class ArraySearch {

	int memory = 100;
	double vvar;

	/**
	 * Checks that {@code minVal} and {@code maxVal} are in the range and
	 * throws an appropriate exception, if they aren't.
	 */

	private static void rangeCheck(int length, int minVal, int maxVal) {
		if (minVal > maxVal) {
			throw new IllegalArgumentException("minVal(" + minVal
					+ ") > maxVal(" + maxVal + ")");
		}
		if (minVal < 0) {
			throw new ArrayIndexOutOfBoundsException(minVal);
		}
		if (maxVal > length) {
			throw new ArrayIndexOutOfBoundsException(maxVal);
		}
	}

	/**
	 * Searches a range of the specified array of longs for the specified value
	 * using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */

	public static int reverseSearch(long[] a, long key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	/**
	 * Searches a range of the specified array of longs for the specified value
	 * using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param minVal
	 *            the index of the first element (inclusive) to be searched
	 * @param maxVal
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */

	public static int reverseSearch(long[] a, int minVal, int maxVal,
			long key) {
		rangeCheck(a.length, minVal, maxVal);
		return reverseSearch0(a, minVal, maxVal, key);
	}

	// Like public version, but without range checks.
	
	private static int reverseSearch0(long[] a, int minVal, int maxVal,
			long key) {
		int low = minVal;
		int high = maxVal - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			long midVal = a[mid];

			if (midVal > key)
				low = mid + 1;
			else if (midVal < key)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}

	/**
	 * Searches a range of the specified array of integers for the specified
	 * value using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */

	public static int reverseSearch(int[] a, int key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	/**
	 * Searches a range of the specified array of integers for the specified
	 * value using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param minVal
	 *            the index of the first element (inclusive) to be searched
	 * @param maxVal
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */

	public static int reverseSearch(int[] a, int minVal, int maxVal, int key) {
		rangeCheck(a.length, minVal, maxVal);
		return reverseSearch0(a, minVal, maxVal, key);
	}

	// Like public version, but without range checks.
	
	private static int reverseSearch0(int[] a, int minVal, int maxVal,
			int key) {
		int low = minVal;
		int high = maxVal - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			int midVal = a[mid];

			if (midVal > key)
				low = mid + 1;
			else if (midVal < key)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}

	/**
	 * Searches a range of the specified array of integers for the specified
	 * value using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */
	
	public static int reverseSearch(float[] a, float key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	/**
	 * Searches a range of the specified array of integers for the specified
	 * value using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param minVal
	 *            the index of the first element (inclusive) to be searched
	 * @param maxVal
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */
	
	public static int reverseSearch(float[] a, int minVal, int maxVal,
			float key) {
		rangeCheck(a.length, minVal, maxVal);
		return reverseSearch0(a, minVal, maxVal, key);
	}

	// Like public version, but without range checks.
	
	private static int reverseSearch0(float[] a, int minVal, int maxVal,
			float key) {
		int low = minVal;
		int high = maxVal - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			float midVal = a[mid];

			if (midVal > key)
				low = mid + 1;
			else if (midVal < key)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}

	/**
	 * Searches a range of the specified array of integers for the specified
	 * value using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */
	
	public static int reverseSearch(double[] a, double key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	/**
	 * Searches a range of the specified array of longs for the specified value
	 * using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param minVal
	 *            the index of the first element (inclusive) to be searched
	 * @param maxVal
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */
	
	public static int reverseSearch(double[] a, int minVal, int maxVal,
			double key) {
		rangeCheck(a.length, minVal, maxVal);
		return reverseSearch0(a, minVal, maxVal, key);
	}

	// Like public version, but without range checks.
	
	private static int reverseSearch0(double[] a, int minVal, int maxVal,
			double key) {
		int low = minVal;
		int high = maxVal - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			double midVal = a[mid];

			if (midVal > key)
				low = mid + 1;
			else if (midVal < key)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}
	
	/**
	 * Searches a range of the specified array of integers for the specified
	 * value using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */

	public static int reverseSearch(short[] a, short key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	/**
	 * Searches a range of the specified array of longs for the specified value
	 * using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param minVal
	 *            the index of the first element (inclusive) to be searched
	 * @param maxVal
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */
	
	public static int reverseSearch(short[] a, int minVal, int maxVal,
			short key) {
		rangeCheck(a.length, minVal, maxVal);
		return reverseSearch0(a, minVal, maxVal, key);
	}

	// Like public version, but without range checks.
	
	private static int reverseSearch0(short[] a, int minVal, int maxVal,
			short key) {
		int low = minVal;
		int high = maxVal - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			short midVal = a[mid];

			if (midVal > key)
				low = mid + 1;
			else if (midVal < key)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}

	/**
	 * Searches a range of the specified array of integers for the specified
	 * value using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */
	
	public static int reverseSearch(byte[] a, byte key) {
		return reverseSearch0(a, 0, a.length, key);
	}
	
	/**
	 * Searches a range of the specified array of longs for the specified value
	 * using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param minVal
	 *            the index of the first element (inclusive) to be searched
	 * @param maxVal
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */

	public static int reverseSearch(byte[] a, int minVal, int maxVal,
			byte key) {
		rangeCheck(a.length, minVal, maxVal);
		return reverseSearch0(a, minVal, maxVal, key);
	}

	// Like public version, but without range checks.
	
	private static int reverseSearch0(byte[] a, int minVal, int maxVal,
			byte key) {
		int low = minVal;
		int high = maxVal - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			byte midVal = a[mid];

			if (midVal > key)
				low = mid + 1;
			else if (midVal < key)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}

	/**
	 * Searches a range of the specified array of integers for the specified
	 * value using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */
	
	public static int reverseSearch(char[] a, char key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	/**
	 * Searches a range of the specified array of longs for the specified value
	 * using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param minVal
	 *            the index of the first element (inclusive) to be searched
	 * @param maxVal
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */
	
	public static int reverseSearch(char[] a, int minVal, int maxVal,
			char key) {
		rangeCheck(a.length, minVal, maxVal);
		return reverseSearch0(a, minVal, maxVal, key);
	}

	// Like public version, but without range checks.
	
	private static int reverseSearch0(char[] a, int minVal, int maxVal,
			char key) {
		int low = minVal;
		int high = maxVal - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			double midVal = a[mid];

			if (midVal > key)
				low = mid + 1;
			else if (midVal < key)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}
	
	/**
	 * Searches a range of the specified array of integers for the specified
	 * value using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */

	public static int reverseSearch(Object[] a, Object key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	/**
	 * Searches a range of the specified array of longs for the specified value
	 * using the binary search algorithm. The range must be sorted in
	 * *descending* order prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements with the
	 * specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param minVal
	 *            the index of the first element (inclusive) to be searched
	 * @param maxVal
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>maxVal</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code minVal > maxVal}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code minVal < 0 or maxVal > a.length}
	 */
	
	public static int reverseSearch(Object[] a, int minVal, int maxVal,
			Object key) {
		rangeCheck(a.length, minVal, maxVal);
		return reverseSearch0(a, minVal, maxVal, key);
	}

	// Like public version, but without range checks.
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static int reverseSearch0(Object[] a, int minVal, int maxVal,
			Object key) {
		int low = minVal;
		int high = maxVal - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			Comparable midVal = (Comparable) a[mid];
			int cmp = midVal.compareTo(key);

			if (cmp > 0)
				low = mid + 1;
			else if (cmp < 0)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}
}
