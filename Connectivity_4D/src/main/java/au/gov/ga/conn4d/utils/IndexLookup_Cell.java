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
import ucar.nc2.Variable;

/**
 * 
 * 
 * @author Johnathan Kool
 *
 */

public class IndexLookup_Cell implements Cloneable {

	private Array array;
	private Variable variable;
	private double[] java_array;
	private int index;
	private int in_bounds = 0;
	private double minval;
	private double maxval;

	/**
	 * No argument constructor
	 */
	
	public IndexLookup_Cell(){}
	
	/**
	 * Constructor accepting a Variable object
	 * 
	 * @param variable
	 */
	
	public IndexLookup_Cell(Variable variable){
		setVariable(variable);
		readArray();
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
		minval = java_array[0];
		maxval = java_array[java_array.length-1];
	}
	
	/**
	 * Retrieves the nearest index to the supplied value within the Variable
	 * associated with the class instance.  Returns -1 if index is out of bounds on
	 * the minimum side.  Returns array length if index is out of bounds on the
	 * maximum side.
	 * 
	 * @param val
	 */

	public synchronized int lookup(double val) {

		in_bounds = 0;

		if(java_array[0]>java_array[java_array.length-1]){
			index = ArraySearch.reverseSearch(java_array, val);
		}
		
		else {index = Arrays.binarySearch(java_array, val);}


		if(val<minval){in_bounds = -1; return -1;}
		if(val>maxval){in_bounds = 1; return java_array.length;}
		
		if(index <0){
			int out = -(index+2);
			if(out==-1){in_bounds = -1;}
			if(out==java_array.length-1){in_bounds=1;}
			return out;
		}
		
		return index;
	}
	
	/**
	 * Sets the Variable associated with the class instance.
	 * 
	 * @param variable
	 */
	
	public void setVariable(Variable variable){
		this.variable=variable;
		readArray();
	}
	
	/**
	 * Releases resources associated with the class.
	 */
	
	public void close(){
		array = null;
		java_array = null;
		variable = null;
	}
	
	/**
	 * Retrieves variable values as a Java array
	 */
	
	public double[] getJavaArray(){return java_array;}
	
	/**
	 * Retrieves the size of the variable
	 */
	
	public int arraySize(){
		return java_array.length;
	}
	
	/**
	 * Identifies whether the last queried value was within the bounds
	 * of the variable.
	 * 
	 * 			-1 means the searched value was below the minimum variable value
	 * 			0 means the searched value was within variable bounds
	 * 			+1 means the searched value was above the maximum variable value
	 */
	
	public int isIn_Bounds(){
		return in_bounds;
	}
	
	/**
	 * Returns the minimum value of the Variable
	 */
	
	public double getMinVal(){
		return java_array[0];
	}

	/**
	 * Returns the maximum value of the Variable
	 */
	
	public double getMaxVal(){
		return java_array[java_array.length-1];
	}
	
	@Override
	public IndexLookup_Cell clone(){
		return new IndexLookup_Cell(variable);
	}

	public double getMinval() {
		return minval;
	}

	public void setMinval(double minval) {
		this.minval = minval;
	}

	public double getMaxval() {
		return maxval;
	}

	public void setMaxval(double maxval) {
		this.maxval = maxval;
	}
}
