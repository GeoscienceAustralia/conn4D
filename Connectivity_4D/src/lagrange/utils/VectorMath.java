package lagrange.utils;
import java.util.*;

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
	 * @param v1
	 *            The first vector
	 * @param v2
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
	 * @param v1
	 *            The first vector
	 * @param v2
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
		assert len == denom.length;
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
     * <p>Reverses the order of the given array.</p>
     *
     * <p>This method does nothing for a <code>null</code> input array.</p>
     *
     * @param array  the array to reverse, may be <code>null</code>
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
     * <p>Reverses the order of the given array.</p>
     *
     * <p>This method does nothing for a <code>null</code> input array.</p>
     *
     * @param array  the array to reverse, may be <code>null</code>
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
     * <p>Reverses the order of the given array.</p>
     *
     * <p>This method does nothing for a <code>null</code> input array.</p>
     *
     * @param array  the array to reverse, may be <code>null</code>
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
     * <p>Reverses the order of the given array.</p>
     *
     * <p>This method does nothing for a <code>null</code> input array.</p>
     *
     * @param array  the array to reverse, may be <code>null</code>
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
     * <p>Reverses the order of the given array.</p>
     *
     * <p>This method does nothing for a <code>null</code> input array.</p>
     *
     * @param array  the array to reverse, may be <code>null</code>
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
		double spacing = range / intervals + 1;
		int[] out = new int[intervals + 1];

		out[0] = start;

		for (int i = 1; i < intervals + 1; i++) {

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

	public static float magnitude(float[] vector) {
		return (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]
				+ vector[2] * vector[2]);
	}

    /**
	 * Compute the magnitude (length) of a vector
	 * 
	 * @param vector
	 *            The vector
	 * @return The magnitude of the vector
	 **/
	
	public static int magnitude(int[] vector) {
		return FixedPointUtilities.sqrt(FixedPointUtilities.multiply(vector[0],
				vector[0])
				+ FixedPointUtilities.multiply(vector[1], vector[1])
				+ FixedPointUtilities.multiply(vector[2], vector[2]));
	}
    
    /**
	 * Returns a vector with negative values relative to the original
	 * 
	 * @param vector
	 * @return
	 */
	
	public static double[] negate(double[] vector){
		for (int i = 0; i < vector.length; i++){
			vector[i]=-vector[i];
		}
		return vector;
	}
    
    /**
	 * Returns a vector with negative values relative to the original
	 * 
	 * @param vector
	 * @return
	 */

	
	public static float[] negate(float[] vector){
		for (int i = 0; i < vector.length; i++){
			vector[i]=-vector[i];
		}
		return vector;
	}
    
    /**
	 * Returns a vector with negative values relative to the original
	 * 
	 * @param vector
	 * @return
	 */

	
	public static int[] negate(int[] vector){
		for (int i = 0; i < vector.length; i++){
			vector[i]=-vector[i];
		}
		return vector;
	}

    /**
	 * Returns a vector with negative values relative to the original
	 * 
	 * @param vector
	 * @return
	 */
	
	public static long[] negate(long[] vector){
		for (int i = 0; i < vector.length; i++){
			vector[i]=-vector[i];
		}
		return vector;
	}
	
	/**
	 * Divides the vector by its sum to return a 1-norm of the
	 * vector
	 * 
	 * @param vector
	 * @return
	 */
	
	public static double[] norm1(double[] vector){
		double sum = sumAsDouble(vector);
		double[] out = new double[vector.length];
		for(int i = 0; i < vector.length; i++){
			out[i] = vector[i]/sum;
		}
		return out;
	}
	
	/**
	 * Divides the vector by its sum to return a 1-norm of the
	 * vector
	 * 
	 * @param vector
	 * @return
	 */
	
	public static float[] norm1(float[] vector){
		float sum = sumAsFloat(vector);
		float[] out = new float[vector.length];
		for(int i = 0; i < vector.length; i++){
			out[i] = vector[i]/sum;
		}
		return out;
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
	 * @param intArray
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
	 * Returns the sample variance of the array values using
	 * a one-pass algorithm.
	 * 
	 * @param fa - A vector of floats
	 * @return - The variance of the vector
	 */
	
	public static float variance(float[] fa) {
		int n = 0;
		float mean = 0;
		float M2 = 0;

		for (int i = 0; i < fa.length; i++) {
			n = n + 1;
			float delta = fa[i] - mean;
			mean = mean + delta / (float) n;
			M2 = M2 + delta * (fa[i] - mean);
		}
		float variance = M2 / (float) (n - 1);
		return variance;
	}
	
	public static float weighted_variance(float[] fa, float[] weights){

	    float sumweight = 0;
	    float mean = 0;
	    float M2 = 0;
	    int n = fa.length;
	 
	    for (int i = 0; i < n; i++){
	        float temp = weights[i] + sumweight;
	        float delta = fa[i] - mean;
	        float R = delta * weights[i] / temp;
	        mean = mean + R;
	        M2 = M2 + sumweight * delta * R;
	        sumweight = temp;

	    }
	 
	    float variance_n = M2/sumweight;
	    return variance_n * (float) n/((float)(n-1));
	}
}