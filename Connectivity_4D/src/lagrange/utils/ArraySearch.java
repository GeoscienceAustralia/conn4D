package lagrange.utils;

import java.util.Comparator;

/**
 * Additional binary search-type functions for searching arrays.  Additions
 * include searching with prior positional expectation, and searching on
 * descending arrays without re-sorting.
 * 
 * @author Johnathan Kool
 *
 */

public class ArraySearch {
	
	int memory = 100;
	double vvar;

	/**
	 * Checks that {@code fromIndex} and {@code toIndex} are in the range and
	 * throws an appropriate exception, if they aren't.
	 */

	private static void rangeCheck(int length, int fromIndex, int toIndex) {
		if (fromIndex > toIndex) {
			throw new IllegalArgumentException("fromIndex(" + fromIndex
					+ ") > toIndex(" + toIndex + ")");
		}
		if (fromIndex < 0) {
			throw new ArrayIndexOutOfBoundsException(fromIndex);
		}
		if (toIndex > length) {
			throw new ArrayIndexOutOfBoundsException(toIndex);
		}
	}

	// Searching

	/**
	 * Searches the specified array of longs for the specified value using the
	 * binary search algorithm. The array must be sorted (as by the
	 * {@link #sort(long[])} method) prior to making this call. If it is not
	 * sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
	 *         <i>insertion point</i> is defined as the point at which the key
	 *         would be inserted into the array: the index of the first element
	 *         greater than the key, or <tt>a.length</tt> if all elements in the
	 *         array are less than the specified key. Note that this guarantees
	 *         that the return value will be &gt;= 0 if and only if the key is
	 *         found.
	 */
	public static int binarySearch(long[] a, long key, int init) {
		return binarySearch0(a, 0, a.length, key, init);
	}

	/**
	 * Searches a range of the specified array of longs for the specified value
	 * using the binary search algorithm. The range must be sorted (as by the
	 * {@link #sort(long[], int, int)} method) prior to making this call. If it
	 * is not sorted, the results are undefined. If the range contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param fromIndex
	 *            the index of the first element (inclusive) to be searched
	 * @param toIndex
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>toIndex</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code fromIndex < 0 or toIndex > a.length}
	 * @since 1.6
	 */
	public static int binarySearch(long[] a, int fromIndex, int toIndex,
			long key, int init) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init);
	}

	/**
	 * Searches a range of the specified array of longs for the specified value
	 * using the binary search algorithm. The range must be sorted (as by the
	 * {@link #sort(long[], int, int)} method) prior to making this call. If it
	 * is not sorted, the results are undefined. If the range contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param fromIndex
	 *            the index of the first element (inclusive) to be searched
	 * @param toIndex
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @param var
	 *            confidence interval for the initial guess of the search key index
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>toIndex</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code fromIndex < 0 or toIndex > a.length}
	 * @since 1.6
	 */
	
	public static int binarySearch(long[] a, int fromIndex, int toIndex,
			long key, int init, double var) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init, var);
	}

	// Like public version, but without range checks.
	private static int binarySearch0(long[] a, int fromIndex, int toIndex,
			long key, int init) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			long midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else
				return mid;
		}

		return -(low + 1); // key not found.
	}

	// Like public version, but without range checks.
	private static int binarySearch0(long[] a, int fromIndex, int toIndex,
			long key, int init, double var) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;
		boolean first = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			if (first) {
				mid = init;
				first = false;
			}

			else {
				mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?
			}

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			long midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else
				return mid;
		}

		return -(low + 1); // key not found.
	}

	/**
	 * Searches the specified array of ints for the specified value using the
	 * binary search algorithm. The array must be sorted (as by the
	 * {@link #sort(int[])} method) prior to making this call. If it is not
	 * sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for	 
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
	 *         <i>insertion point</i> is defined as the point at which the key
	 *         would be inserted into the array: the index of the first element
	 *         greater than the key, or <tt>a.length</tt> if all elements in the
	 *         array are less than the specified key. Note that this guarantees
	 *         that the return value will be &gt;= 0 if and only if the key is
	 *         found.
	 */

	public static int binarySearch(int[] a, int key, int init) {
		return binarySearch0(a, 0, a.length, key, init);
	}

	public static int binarySearch(int[] a, int key, int init, double var) {
		return binarySearch0(a, 0, a.length, key, init, var);
	}

	/**
	 * Searches a range of the specified array of ints for the specified value
	 * using the binary search algorithm. The range must be sorted (as by the
	 * {@link #sort(int[], int, int)} method) prior to making this call. If it
	 * is not sorted, the results are undefined. If the range contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param fromIndex
	 *            the index of the first element (inclusive) to be searched
	 * @param toIndex
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>toIndex</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code fromIndex < 0 or toIndex > a.length}
	 * @since 1.6
	 */
	public static int binarySearch(int[] a, int fromIndex, int toIndex,
			int key, int init) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init);
	}

	// Like public version, but without range checks.
	private static int binarySearch0(int[] a, int fromIndex, int toIndex,
			int key, int init) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			long midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else
				return mid;
		}

		return -(low + 1); // key not found.
	}

	// Like public version, but without range checks.
	private static int binarySearch0(int[] a, int fromIndex, int toIndex,
			int key, int init, double var) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;
		boolean first = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			if (first) {
				mid = init;
				first = false;
			} else {
				mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?
			}

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			long midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else
				return mid;
		}

		return -(low + 1); // key not found.
	}

	/**
	 * Searches the specified array of shorts for the specified value using the
	 * binary search algorithm. The array must be sorted (as by the
	 * {@link #sort(short[])} method) prior to making this call. If it is not
	 * sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
	 *         <i>insertion point</i> is defined as the point at which the key
	 *         would be inserted into the array: the index of the first element
	 *         greater than the key, or <tt>a.length</tt> if all elements in the
	 *         array are less than the specified key. Note that this guarantees
	 *         that the return value will be &gt;= 0 if and only if the key is
	 *         found.
	 */

	public static int binarySearch(short[] a, short key, int init) {
		return binarySearch0(a, 0, a.length, key, init);
	}

	public static int binarySearch(short[] a, short key, int init, double var) {
		return binarySearch0(a, 0, a.length, key, init, var);
	}

	/**
	 * Searches a range of the specified array of shorts for the specified value
	 * using the binary search algorithm. The range must be sorted (as by the
	 * {@link #sort(short[], int, int)} method) prior to making this call. If it
	 * is not sorted, the results are undefined. If the range contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param fromIndex
	 *            the index of the first element (inclusive) to be searched
	 * @param toIndex
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>toIndex</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code fromIndex < 0 or toIndex > a.length}
	 * @since 1.6
	 */

	public static int binarySearch(short[] a, int fromIndex, int toIndex,
			short key, int init) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init);
	}

	public static int binarySearch(short[] a, int fromIndex, int toIndex,
			short key, int init, double var) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init, var);
	}

	// Like public version, but without range checks.
	private static int binarySearch0(short[] a, int fromIndex, int toIndex,
			short key, int init) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			long midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else
				return mid;
		}

		return -(low + 1); // key not found.
	}

	// Like public version, but without range checks.
	private static int binarySearch0(short[] a, int fromIndex, int toIndex,
			short key, int init, double var) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;
		boolean first = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			if (first) {
				mid = init;
				first = false;
			} else {
				mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?
			}

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			long midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else
				return mid;
		}

		return -(low + 1); // key not found.
	}

	/**
	 * Searches the specified array of chars for the specified value using the
	 * binary search algorithm. The array must be sorted (as by the
	 * {@link #sort(char[])} method) prior to making this call. If it is not
	 * sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
	 *         <i>insertion point</i> is defined as the point at which the key
	 *         would be inserted into the array: the index of the first element
	 *         greater than the key, or <tt>a.length</tt> if all elements in the
	 *         array are less than the specified key. Note that this guarantees
	 *         that the return value will be &gt;= 0 if and only if the key is
	 *         found.
	 */

	public static int binarySearch(char[] a, char key, int init) {
		return binarySearch0(a, 0, a.length, key, init);
	}

	public static int binarySearch(char[] a, char key, int init, double var) {
		return binarySearch0(a, 0, a.length, key, init, var);
	}

	/**
	 * Searches a range of the specified array of chars for the specified value
	 * using the binary search algorithm. The range must be sorted (as by the
	 * {@link #sort(char[], int, int)} method) prior to making this call. If it
	 * is not sorted, the results are undefined. If the range contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param fromIndex
	 *            the index of the first element (inclusive) to be searched
	 * @param toIndex
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>toIndex</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code fromIndex < 0 or toIndex > a.length}
	 * @since 1.6
	 */

	public static int binarySearch(char[] a, int fromIndex, int toIndex,
			char key, int init) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init);
	}

	public static int binarySearch(char[] a, int fromIndex, int toIndex,
			char key, int init, double var) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init, var);
	}

	// Like public version, but without range checks.
	private static int binarySearch0(char[] a, int fromIndex, int toIndex,
			char key, int init) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			char midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else
				return mid;
		}

		return -(low + 1); // key not found.
	}

	// Like public version, but without range checks.
	private static int binarySearch0(char[] a, int fromIndex, int toIndex,
			char key, int init, double var) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;
		boolean first = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			if (first) {
				mid = init;
				first = false;
			} else {
				mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?
			}

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			long midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else
				return mid;
		}

		return -(low + 1); // key not found.
	}

	/**
	 * Searches the specified array of bytes for the specified value using the
	 * binary search algorithm. The array must be sorted (as by the
	 * {@link #sort(byte[])} method) prior to making this call. If it is not
	 * sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
	 *         <i>insertion point</i> is defined as the point at which the key
	 *         would be inserted into the array: the index of the first element
	 *         greater than the key, or <tt>a.length</tt> if all elements in the
	 *         array are less than the specified key. Note that this guarantees
	 *         that the return value will be &gt;= 0 if and only if the key is
	 *         found.
	 */
	public static int binarySearch(byte[] a, byte key, int init) {
		return binarySearch0(a, 0, a.length, key, init);
	}

	public static int binarySearch(byte[] a, byte key, int init, double var) {
		return binarySearch0(a, 0, a.length, key, init);
	}

	/**
	 * Searches a range of the specified array of bytes for the specified value
	 * using the binary search algorithm. The range must be sorted (as by the
	 * {@link #sort(byte[], int, int)} method) prior to making this call. If it
	 * is not sorted, the results are undefined. If the range contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param fromIndex
	 *            the index of the first element (inclusive) to be searched
	 * @param toIndex
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>toIndex</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code fromIndex < 0 or toIndex > a.length}
	 * @since 1.6
	 */
	public static int binarySearch(byte[] a, int fromIndex, int toIndex,
			byte key, int init) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init);
	}

	public static int binarySearch(byte[] a, int fromIndex, int toIndex,
			byte key, int init, double var) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init, var);
	}

	// Like public version, but without range checks.
	private static int binarySearch0(byte[] a, int fromIndex, int toIndex,
			byte key, int init) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			long midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else
				return mid;
		}

		return -(low + 1); // key not found.
	}

	// Like public version, but without range checks.
	private static int binarySearch0(byte[] a, int fromIndex, int toIndex,
			byte key, int init, double var) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;
		boolean first = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			if (first) {
				mid = init;
				first = false;
			} else {
				mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?
			}

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			long midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else
				return mid;
		}

		return -(low + 1); // key not found.
	}

	/**
	 * Searches the specified array of doubles for the specified value using the
	 * binary search algorithm. The array must be sorted (as by the
	 * {@link #sort(double[])} method) prior to making this call. If it is not
	 * sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found. This method considers all NaN values to be equivalent and
	 * equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
	 *         <i>insertion point</i> is defined as the point at which the key
	 *         would be inserted into the array: the index of the first element
	 *         greater than the key, or <tt>a.length</tt> if all elements in the
	 *         array are less than the specified key. Note that this guarantees
	 *         that the return value will be &gt;= 0 if and only if the key is
	 *         found.
	 */
	public static int binarySearch(double[] a, double key, int init) {
		return binarySearch0(a, 0, a.length, key, init);
	}

	public static int binarySearch(double[] a, double key, int init, double var) {
		return binarySearch0(a, 0, a.length, key, init, var);
	}

	/**
	 * Searches a range of the specified array of doubles for the specified
	 * value using the binary search algorithm. The range must be sorted (as by
	 * the {@link #sort(double[], int, int)} method) prior to making this call.
	 * If it is not sorted, the results are undefined. If the range contains
	 * multiple elements with the specified value, there is no guarantee which
	 * one will be found. This method considers all NaN values to be equivalent
	 * and equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param fromIndex
	 *            the index of the first element (inclusive) to be searched
	 * @param toIndex
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>toIndex</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code fromIndex < 0 or toIndex > a.length}
	 * @since 1.6
	 */
	public static int binarySearch(double[] a, int fromIndex, int toIndex,
			double key, int init) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init);
	}

	public static int binarySearch(double[] a, int fromIndex, int toIndex,
			double key, int init, double var) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init, var);
	}

	// Like public version, but without range checks.
	private static int binarySearch0(double[] a, int fromIndex, int toIndex,
			double key, int init) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			double midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else {
				long midBits = Double.doubleToLongBits(midVal);
				long keyBits = Double.doubleToLongBits(key);
				if (midBits == keyBits) // Values are equal
					return mid; // Key found
				else if (midBits < keyBits) // (-0.0, 0.0) or (!NaN, NaN)
					low = mid + 1;
				else
					// (0.0, -0.0) or (NaN, !NaN)
					high = mid - 1;
			}
		}

		return -(low + 1); // key not found.
	}

	// Like public version, but without range checks.
	private static int binarySearch0(double[] a, int fromIndex, int toIndex,
			double key, int init, double var) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;
		boolean first = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			if (first) {
				mid = init;
				first = false;
			} else {
				mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?
			}

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			double midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else {
				long midBits = Double.doubleToLongBits(midVal);
				long keyBits = Double.doubleToLongBits(key);
				if (midBits == keyBits) // Values are equal
					return mid; // Key found
				else if (midBits < keyBits) // (-0.0, 0.0) or (!NaN, NaN)
					low = mid + 1;
				else
					// (0.0, -0.0) or (NaN, !NaN)
					high = mid - 1;
			}
		}

		return -(low + 1); // key not found.
	}

	/**
	 * Searches the specified array of floats for the specified value using the
	 * binary search algorithm. The array must be sorted (as by the
	 * {@link #sort(float[])} method) prior to making this call. If it is not
	 * sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found. This method considers all NaN values to be equivalent and
	 * equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
	 *         <i>insertion point</i> is defined as the point at which the key
	 *         would be inserted into the array: the index of the first element
	 *         greater than the key, or <tt>a.length</tt> if all elements in the
	 *         array are less than the specified key. Note that this guarantees
	 *         that the return value will be &gt;= 0 if and only if the key is
	 *         found.
	 */
	public static int binarySearch(float[] a, float key, int init) {
		return binarySearch0(a, 0, a.length, key, init);
	}

	public static int binarySearch(float[] a, float key, int init, double var) {
		return binarySearch0(a, 0, a.length, key, init, var);
	}

	/**
	 * Searches a range of the specified array of floats for the specified value
	 * using the binary search algorithm. The range must be sorted (as by the
	 * {@link #sort(float[], int, int)} method) prior to making this call. If it
	 * is not sorted, the results are undefined. If the range contains multiple
	 * elements with the specified value, there is no guarantee which one will
	 * be found. This method considers all NaN values to be equivalent and
	 * equal.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param fromIndex
	 *            the index of the first element (inclusive) to be searched
	 * @param toIndex
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>toIndex</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws IllegalArgumentException
	 *             if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code fromIndex < 0 or toIndex > a.length}
	 * @since 1.6
	 */
	public static int binarySearch(float[] a, int fromIndex, int toIndex,
			float key, int init) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init);
	}

	public static int binarySearch(float[] a, int fromIndex, int toIndex,
			float key, int init, double var) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init, var);
	}

	// Like public version, but without range checks.
	private static int binarySearch0(float[] a, int fromIndex, int toIndex,
			float key, int init) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			float midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else {
				long midBits = Double.doubleToLongBits(midVal);
				long keyBits = Double.doubleToLongBits(key);
				if (midBits == keyBits) // Values are equal
					return mid; // Key found
				else if (midBits < keyBits) // (-0.0, 0.0) or (!NaN, NaN)
					low = mid + 1;
				else
					// (0.0, -0.0) or (NaN, !NaN)
					high = mid - 1;
			}
		}

		return -(low + 1); // key not found.
	}

	// Like public version, but without range checks.
	private static int binarySearch0(float[] a, int fromIndex, int toIndex,
			float key, int init, double var) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) var / (double) a.length;
		boolean reflect = true;
		boolean first = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			if (first) {
				mid = init;
				first = false;
			} else {
				mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?
			}

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			float midVal = a[mid];

			if (midVal < key)
				low = mid + 1;

			else if (midVal > key)
				high = mid - 1;

			else {
				long midBits = Double.doubleToLongBits(midVal);
				long keyBits = Double.doubleToLongBits(key);
				if (midBits == keyBits) // Values are equal
					return mid; // Key found
				else if (midBits < keyBits) // (-0.0, 0.0) or (!NaN, NaN)
					low = mid + 1;
				else
					// (0.0, -0.0) or (NaN, !NaN)
					high = mid - 1;
			}
		}

		return -(low + 1); // key not found.
	}

	/**
	 * Searches the specified array for the specified object using the binary
	 * search algorithm. The array must be sorted into ascending order according
	 * to the {@linkplain Comparable natural ordering} of its elements (as by
	 * the {@link #sort(Object[])} method) prior to making this call. If it is
	 * not sorted, the results are undefined. (If the array contains elements
	 * that are not mutually comparable (for example, strings and integers), it
	 * <i>cannot</i> be sorted according to the natural ordering of its
	 * elements, hence results are undefined.) If the array contains multiple
	 * elements equal to the specified object, there is no guarantee which one
	 * will be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
	 *         <i>insertion point</i> is defined as the point at which the key
	 *         would be inserted into the array: the index of the first element
	 *         greater than the key, or <tt>a.length</tt> if all elements in the
	 *         array are less than the specified key. Note that this guarantees
	 *         that the return value will be &gt;= 0 if and only if the key is
	 *         found.
	 * @throws ClassCastException
	 *             if the search key is not comparable to the elements of the
	 *             array.
	 */
	public static int binarySearch(Object[] a, Object key, int init) {
		return binarySearch0(a, 0, a.length, key, init);
	}

	public static int binarySearch(Object[] a, Object key, int init, double var) {
		return binarySearch0(a, 0, a.length, key, init, var);
	}

	/**
	 * Searches a range of the specified array for the specified object using
	 * the binary search algorithm. The range must be sorted into ascending
	 * order according to the {@linkplain Comparable natural ordering} of its
	 * elements (as by the {@link #sort(Object[], int, int)} method) prior to
	 * making this call. If it is not sorted, the results are undefined. (If the
	 * range contains elements that are not mutually comparable (for example,
	 * strings and integers), it <i>cannot</i> be sorted according to the
	 * natural ordering of its elements, hence results are undefined.) If the
	 * range contains multiple elements equal to the specified object, there is
	 * no guarantee which one will be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param fromIndex
	 *            the index of the first element (inclusive) to be searched
	 * @param toIndex
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>toIndex</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws ClassCastException
	 *             if the search key is not comparable to the elements of the
	 *             array within the specified range.
	 * @throws IllegalArgumentException
	 *             if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code fromIndex < 0 or toIndex > a.length}
	 * @since 1.6
	 */
	public static int binarySearch(Object[] a, int fromIndex, int toIndex,
			Object key, int init) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init);
	}

	public static int binarySearch(Object[] a, int fromIndex, int toIndex,
			Object key, int init, double var) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, init, var);
	}

	// Like public version, but without range checks.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static int binarySearch0(Object[] a, int fromIndex, int toIndex,
			Object key, int init) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			Comparable midVal = (Comparable) a[mid];
			int cmp = midVal.compareTo(key);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}

		return -(low + 1); // key not found.
	}

	// Like public version, but without range checks.
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static int binarySearch0(Object[] a, int fromIndex, int toIndex,
			Object key, int init, double var) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) var / (double) a.length;
		boolean reflect = true;
		boolean first = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			if (first) {
				mid = init;
				first = false;
			} else {
				mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?
			}

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			Comparable midVal = (Comparable) a[mid];
			int cmp = midVal.compareTo(key);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}

		return -(low + 1); // key not found.
	}

	/**
	 * Searches the specified array for the specified object using the binary
	 * search algorithm. The array must be sorted into ascending order according
	 * to the specified comparator (as by the
	 * {@link #sort(Object[], Comparator) sort(T[], Comparator)} method) prior
	 * to making this call. If it is not sorted, the results are undefined. If
	 * the array contains multiple elements equal to the specified object, there
	 * is no guarantee which one will be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param key
	 *            the value to be searched for
	 * @param c
	 *            the comparator by which the array is ordered. A <tt>null</tt>
	 *            value indicates that the elements' {@linkplain Comparable
	 *            natural ordering} should be used.
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
	 *         <i>insertion point</i> is defined as the point at which the key
	 *         would be inserted into the array: the index of the first element
	 *         greater than the key, or <tt>a.length</tt> if all elements in the
	 *         array are less than the specified key. Note that this guarantees
	 *         that the return value will be &gt;= 0 if and only if the key is
	 *         found.
	 * @throws ClassCastException
	 *             if the array contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator, or the search
	 *             key is not comparable to the elements of the array using this
	 *             comparator.
	 */
	public static <T> int binarySearch(T[] a, T key, Comparator<? super T> c,
			int init) {
		return binarySearch0(a, 0, a.length, key, c, init);
	}

	public static <T> int binarySearch(T[] a, T key, Comparator<? super T> c,
			int init, double var) {
		return binarySearch0(a, 0, a.length, key, c, init, var);
	}

	/**
	 * Searches a range of the specified array for the specified object using
	 * the binary search algorithm. The range must be sorted into ascending
	 * order according to the specified comparator (as by the
	 * {@link #sort(Object[], int, int, Comparator) sort(T[], int, int,
	 * Comparator)} method) prior to making this call. If it is not sorted, the
	 * results are undefined. If the range contains multiple elements equal to
	 * the specified object, there is no guarantee which one will be found.
	 * 
	 * @param a
	 *            the array to be searched
	 * @param fromIndex
	 *            the index of the first element (inclusive) to be searched
	 * @param toIndex
	 *            the index of the last element (exclusive) to be searched
	 * @param key
	 *            the value to be searched for
	 * @param c
	 *            the comparator by which the array is ordered. A <tt>null</tt>
	 *            value indicates that the elements' {@linkplain Comparable
	 *            natural ordering} should be used.
	 * @param init
	 * 			  initial guess of the index of the search key
	 * @return index of the search key, if it is contained in the array within
	 *         the specified range; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
	 *         point</i> is defined as the point at which the key would be
	 *         inserted into the array: the index of the first element in the
	 *         range greater than the key, or <tt>toIndex</tt> if all elements
	 *         in the range are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws ClassCastException
	 *             if the range contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator, or the search
	 *             key is not comparable to the elements in the range using this
	 *             comparator.
	 * @throws IllegalArgumentException
	 *             if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code fromIndex < 0 or toIndex > a.length}
	 * @since 1.6
	 */
	public static <T> int binarySearch(T[] a, int fromIndex, int toIndex,
			T key, Comparator<? super T> c, int init) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, c, init);
	}

	public static <T> int binarySearch(T[] a, int fromIndex, int toIndex,
			T key, Comparator<? super T> c, int init, double var) {
		rangeCheck(a.length, fromIndex, toIndex);
		return binarySearch0(a, fromIndex, toIndex, key, c, init, var);
	}

	// Like public version, but without range checks.
	private static <T> int binarySearch0(T[] a, int fromIndex, int toIndex,
			T key, Comparator<? super T> c, int init) {

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) init / (double) a.length;
		boolean reflect = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			T midVal = a[mid];
			int cmp = c.compare(midVal, key);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}

		return -(low + 1); // key not found.
	}

	// Like public version, but without range checks.
	private static <T> int binarySearch0(T[] a, int fromIndex, int toIndex,
			T key, Comparator<? super T> c, int init, double var) {

		if (c == null) {
			return binarySearch0(a, fromIndex, toIndex, key, init, var);
		}

		int low = fromIndex;
		int high = toIndex - 1;
		double prior = (double) var / (double) a.length;
		boolean reflect = true;
		boolean first = true;

		while (low <= high) {
			int mid;
			int subsize = high - low;

			if (reflect) {
				prior = 1 - prior;
			}

			if (first) {
				mid = init;
				first = false;
			} else {
				mid = (int) Math.ceil((double) low + prior * (double) subsize); // Ceiling?
			}

			if (mid - low > subsize / 2)
				reflect = true;
			else
				reflect = false;

			T midVal = a[mid];
			int cmp = c.compare(midVal, key);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}

		return -(low + 1); // key not found.
	}

	public static int reverseSearch(long[] a, long key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	public static int reverseSearch(long[] a, int fromIndex, int toIndex,
			long key) {
		rangeCheck(a.length, fromIndex, toIndex);
		return reverseSearch0(a, fromIndex, toIndex, key);
	}

	private static int reverseSearch0(long[] a, int fromIndex, int toIndex,
			long key) {
		int low = fromIndex;
		int high = toIndex - 1;

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
	
	public static int reverseSearch(int[] a, int key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	public static int reverseSearch(int[] a, int fromIndex, int toIndex,
			int key) {
		rangeCheck(a.length, fromIndex, toIndex);
		return reverseSearch0(a, fromIndex, toIndex, key);
	}

	private static int reverseSearch0(int[] a, int fromIndex, int toIndex,
			int key) {
		int low = fromIndex;
		int high = toIndex - 1;

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
	public static int reverseSearch(float[] a, float key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	public static int reverseSearch(float[] a, int fromIndex, int toIndex,
			float key) {
		rangeCheck(a.length, fromIndex, toIndex);
		return reverseSearch0(a, fromIndex, toIndex, key);
	}

	private static int reverseSearch0(float[] a, int fromIndex, int toIndex,
			float key) {
		int low = fromIndex;
		int high = toIndex - 1;

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
	
	public static int reverseSearch(double[] a, double key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	public static int reverseSearch(double[] a, int fromIndex, int toIndex,
			double key) {
		rangeCheck(a.length, fromIndex, toIndex);
		return reverseSearch0(a, fromIndex, toIndex, key);
	}
	
	private static int reverseSearch0(double[] a, int fromIndex, int toIndex,
			double key) {
		int low = fromIndex;
		int high = toIndex - 1;

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
	public static int reverseSearch(short[] a, short key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	public static int reverseSearch(short[] a, int fromIndex, int toIndex,
			short key) {
		rangeCheck(a.length, fromIndex, toIndex);
		return reverseSearch0(a, fromIndex, toIndex, key);
	}
	
	private static int reverseSearch0(short[] a, int fromIndex, int toIndex,
			short key) {
		int low = fromIndex;
		int high = toIndex - 1;

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
	
	public static int reverseSearch(byte[] a, byte key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	public static int reverseSearch(byte[] a, int fromIndex, int toIndex,
			byte key) {
		rangeCheck(a.length, fromIndex, toIndex);
		return reverseSearch0(a, fromIndex, toIndex, key);
	}
	
	private static int reverseSearch0(byte[] a, int fromIndex, int toIndex,
			byte key) {
		int low = fromIndex;
		int high = toIndex - 1;

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
	
	public static int reverseSearch(char[] a, char key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	public static int reverseSearch(char[] a, int fromIndex, int toIndex,
			char key) {
		rangeCheck(a.length, fromIndex, toIndex);
		return reverseSearch0(a, fromIndex, toIndex, key);
	}
	
	private static int reverseSearch0(char[] a, int fromIndex, int toIndex,
			char key) {
		int low = fromIndex;
		int high = toIndex - 1;

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
	
	public static int reverseSearch(Object[] a, Object key) {
		return reverseSearch0(a, 0, a.length, key);
	}

	public static int reverseSearch(Object[] a, int fromIndex, int toIndex,
			Object key) {
		rangeCheck(a.length, fromIndex, toIndex);
		return reverseSearch0(a, fromIndex, toIndex, key);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static int reverseSearch0(Object[] a, int fromIndex, int toIndex,
			Object key) {
		int low = fromIndex;
		int high = toIndex - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			Comparable midVal = (Comparable)a[mid];
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
