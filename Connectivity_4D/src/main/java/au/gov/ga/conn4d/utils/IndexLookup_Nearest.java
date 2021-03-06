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

import java.io.IOException;
import java.util.Arrays;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Variable;

/**
 * @author Johnathan Kool
 */

public class IndexLookup_Nearest implements Cloneable {

	private Array array;
	private Variable variable;
	private double[] java_array;
	private int index;
	private int in_bounds = 0;
	private boolean negate = false;
	private boolean reverse_order = false;

	/**
	 * No argument constructor
	 */

	public IndexLookup_Nearest() {
	}

	/**
	 * Constructor accepting a Variable object
	 * 
	 * @param variable
	 */

	public IndexLookup_Nearest(Variable variable) {
		setVariable(variable);
	}

	/**
	 * Constructor accepting a Variable object
	 * 
	 * @param variable
	 */

	public IndexLookup_Nearest(Variable variable, int dim) {
		setVariable(variable, dim);
	}

	/**
	 * Reads the variable values into An array object
	 */

	private void readArray() {
		try {
			array = variable.read();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Convert into a java array

		if (array.getElementType() == Float.TYPE) {

			float[] fa = (float[]) array.copyTo1DJavaArray();
			java_array = new double[fa.length];
			for (int i = 0; i < java_array.length; i++) {
				java_array[i] = fa[i];
			}
		} else {
			java_array = (double[]) array.copyTo1DJavaArray();
		}

		reverse_order = isReversed();
	}

	/**
	 * Reads the variable values into An array object
	 */

	public Array readArray(int dim) {

		int len = variable.getShape(dim);
		int[] shape = new int[variable.getRank()];
		for (int i = 0; i < shape.length; i++) {
			shape[i] = 1;
		}
		shape[dim] = len;
		try {
			array = variable.read(new int[variable.getRank()], shape);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidRangeException e) {
			e.printStackTrace();
		}

		// Convert into a java array

		if (array.getElementType() == Float.TYPE) {

			float[] fa = (float[]) array.copyTo1DJavaArray();
			java_array = new double[fa.length];
			for (int i = 0; i < java_array.length; i++) {
				java_array[i] = fa[i];
			}
		} else {
			java_array = (double[]) array.copyTo1DJavaArray();
		}

		reverse_order = isReversed();
		return array;
	}

	/**
	 * Retrieves the nearest index to the supplied value within the Variable
	 * associated with the class instance.
	 * 
	 * @param val
	 */

	public synchronized int lookup(double val) {

		// Switch negative values to positive if needed
		val = negate ? val == 0 ? 0 : -val : val;

		in_bounds = 0;

		// Use binary search to look for the value. // splay tree
		// search???

		if (reverse_order) {
			index = ArraySearch.reverseSearch(java_array, val);
		}

		else {
			index = Arrays.binarySearch(java_array, val);
		}

		if (index < 0) {

			// Error check

			if (index == -1) {
				in_bounds = -1;
				return 0;
			}

			if (-(index) > java_array.length) {
				in_bounds = 1;
				return -(index + 2);
			}

			// If not an exact match - determine which value we're closer to

			double spval = -9999; // -9999 as opposed to 0 for troubleshooting,
									// because 0 can be a proper value.
			int iindex = -(index + 1) - 1;

			try {
				spval = (java_array[iindex] + java_array[iindex + 1]) / 2d;
			} catch (ArrayIndexOutOfBoundsException e) { // Should not reach
															// here.
				System.out.println("val: " + val + ", max array val: "
						+ java_array[java_array.length - 1] + ", idx: " + index
						+ ", ja.length: " + java_array.length + ", comp: "
						+ -(index + 2));
				System.exit(-1);
			}
			if (val < spval) {

				return iindex;

			} else {

				return iindex + 1;
			}
		}

		// Otherwise it's an exact match.

		return index;
	}

	/**
	 * Sets the Variable associated with the class instance.
	 * 
	 * @param variable
	 */

	public void setVariable(Variable variable) {
		this.variable = variable;
		readArray();
	}

	/**
	 * Sets the Variable associated with the class instance.
	 * 
	 * @param variable
	 */

	public void setVariable(Variable variable, int dim) {
		this.variable = variable;
		readArray(dim);
	}

	/**
	 * Releases resources associated with the class.
	 */

	public void close() {
		array = null;
		java_array = null;
		variable = null;
	}

	/**
	 * Retrieves variable values as a Java array
	 */

	public double[] getJavaArray() {
		if (negate) {
			double[] tmp = new double[java_array.length];
			for (int i = 0; i < java_array.length; i++) {
				tmp[i] = -java_array[i];
			}
			return tmp;
		}
		return java_array;
	}

	/**
	 * Retrieves the size of the variable
	 */

	public int arraySize() {
		return java_array.length;
	}

	/**
	 * Identifies whether the last queried value was within the bounds of the
	 * variable.
	 * 
	 * @return -1 means the searched value was below the minimum variable value
	 *         0 means the searched value was within variable bounds +1 means
	 *         the searched value was above the maximum variable value
	 */

	public int isIn_Bounds() {
		return in_bounds;
	}

	/**
	 * Returns the minimum value of the Variable
	 */

	public double getMinVal() {
		double val = negate ? -1 : 1;
		if (reverse_order) {
			return val * java_array[java_array.length - 1];
		}
		return val * java_array[0];
	}

	/**
	 * Returns the maximum value of the Variable
	 */

	public double getMaxVal() {
		double val = negate ? -1 : 1;
		if (reverse_order) {
			return val * java_array[0];
		}
		return val * java_array[java_array.length - 1];
	}

	@Override
	public IndexLookup_Nearest clone() {
		return new IndexLookup_Nearest(variable);
	}

	/**
	 * Sets whether the values need to be negated or not (e.g. negating
	 * bathymetry values)
	 */

	public void setNegate(boolean negate) {
		this.negate = negate;
	}

	/**
	 * Indicates whether the values are being negated or not (e.g. negating
	 * bathymetry values)
	 */

	public boolean getNegate() {
		return negate;
	}

	/**
	 * Indicates whether the array elements are sorted in reverse (e.g.
	 * descending) order
	 * 
	 * @return
	 */

	private boolean isReversed() {
		if (java_array[0] > java_array[java_array.length - 1]) {
			return true;
		}
		return false;
	}
}
