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
 * Performs Tricubic spline interpolation.
 * 
 * @author Johnathan Kool
 */

public class Spline3D {

	private int nPoints = 0; // number of points on first axis
	private int mPoints = 0; // number of points on second axis
	private int lPoints = 0; // number of points on third axis
	private double[][][] y = null; // double[][][] of grid values
	private double[] x1 = null; // double array of first axis values
	private double[] x2 = null; // double array of second axis values
	private double[] x3 = null; // double array of third axis values
	private double[] xMin = new double[3]; // minimum values of x1, x2 and x3
	private double[] xMax = new double[3]; // maximum values of x1, x2 and x3
	private Spline2D[] sp2da = null; // nPoints array of Spline2D instances
	private Spline spln = null; // Spline instance
	private double[][][] d2ydx2inner = null; // inner (3D) matrix of second
												// derivatives

	/**
	 * Constructor
	 * 
	 * @param x1
	 *            - axis values for the first dimension.
	 * @param x2
	 *            - axis values for the second dimension.
	 * @param x3
	 *            - axis values for the third dimension.
	 * @param y
	 *            - 3-dimensional matrix of float data values.
	 */

	public Spline3D(double[] x1, double[] x2, double[] x3, float[][][] y) {

		// Store the length of the first axis at the class level as nPoints.
		this.nPoints = x1.length;

		// Store the length of the second axis at the class level as mPoints.
		this.mPoints = x2.length;

		// Store the length of the first axis at the class level as lPoints.
		this.lPoints = x3.length;

		// Create a new Spline object containing n elements.
		this.spln = new Spline(this.nPoints);

		// Create an array of n Spline2D objects with dimensions m and l
		this.sp2da = Spline2D.oneDarray(this.nPoints, this.mPoints,
				this.lPoints);

		// Set x1 at the class level to be a new double array of size nPoints
		this.x1 = new double[this.nPoints];

		// Set x2 at the class level to be a new double array of size mPoints
		this.x2 = new double[this.mPoints];

		// Set x3 at the class level to be a new double array of size lPoints
		this.x3 = new double[this.lPoints];

		// Set y at the class level to be a new 3D double array (nPoints x
		// mPoints x lPoints)
		this.y = new double[this.nPoints][this.mPoints][this.lPoints];

		// Set d2ydx2inner at the class level to be a new 3D double array
		// (nPoints x mPoints x lPoints)
		this.d2ydx2inner = new double[this.nPoints][this.mPoints][this.lPoints];

		// Loop across the passed values of the first axis and store them at the
		// class level.
		for (int i = 0; i < this.nPoints; i++) {
			this.x1[i] = x1[i];
		}

		// Store the minimum and maximum values of the first axis at the class
		// level.
		this.xMin[0] = VectorMath.minimum(this.x1);
		this.xMax[0] = VectorMath.maximum(this.x1);

		// Loop across the passed values of the second axis and store them at
		// the class level.
		for (int j = 0; j < this.mPoints; j++) {
			this.x2[j] = x2[j];
		}
		// Store the minimum and maximum values of the second axis at the class
		// level.
		this.xMin[1] = VectorMath.minimum(this.x2);
		this.xMax[1] = VectorMath.maximum(this.x2);

		// Loop across the passed values of the third axis and store them at the
		// class level.
		for (int j = 0; j < this.lPoints; j++) {
			this.x3[j] = x3[j];
		}
		// Store the minimum and maximum values of the third axis at the class
		// level.
		this.xMin[2] = VectorMath.minimum(this.x3);
		this.xMax[2] = VectorMath.maximum(this.x3);

		// Loop across the passed grid values and store them at the class level

		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < this.mPoints; j++) {
				for (int k = 0; k < this.lPoints; k++) {
					this.y[i][j][k] = y[i][j][k];
				}
			}
		}

		// Create a mPoints x lPoints array of doubles to temporarily hold
		// 2D values for writing into the class-level array of Spline2D objects.

		double[][] yTempml = new double[this.mPoints][this.lPoints];

		// Loop across the array of Spline2D objects (nPoints length)

		for (int i = 0; i < this.nPoints; i++) {

			// 2D loop: Fill the temporary 2D array by slicing the passed y
			// array along the first axis.

			for (int j = 0; j < this.mPoints; j++) {
				for (int k = 0; k < this.lPoints; k++) {
					yTempml[j][k] = y[i][j][k];
				}
			}

			// In the process of looping along the Spline2D array, reset the
			// class level data for each Spline2D object using the second axis, 
			// third axis and temporary (sliced) values.
			this.sp2da[i].resetData(x2, x3, yTempml);

			// Retrieve the second derivative values from the Spline2D object
			// and store in the class level array.
			this.d2ydx2inner[i] = this.sp2da[i].getDeriv();
		}
	}

	/**
	 * Resets the x1, x2, x3, y data arrays
	 * 
	 * @param x1
	 *            - axis values for the first dimension.
	 * @param x2
	 *            - axis values for the second dimension.
	 * @param x3
	 *            - axis values for the third dimension.
	 * @param y
	 *            - 3-dimensional matrix of float data values.
	 */

	public void resetData(double[] x1, double[] x2, double[] x3, float[][][] y) {

		// Loop through the passed values for the first axis and
		// store the values in the class-level first axis variable.

		for (int i = 0; i < this.nPoints; i++) {
			this.x1[i] = x1[i];
		}

		// Store the minimum and maximum values of the first axis at the class
		// level.
		this.xMin[0] = VectorMath.minimum(this.x1);
		this.xMax[0] = VectorMath.maximum(this.x1);

		// Loop across the passed values of the second axis and store them at
		// the class level.
		for (int i = 0; i < this.mPoints; i++) {
			this.x2[i] = x2[i];
		}

		// Store the minimum and maximum values of the second axis at the class
		// level.
		this.xMin[1] = VectorMath.minimum(this.x2);
		this.xMax[1] = VectorMath.maximum(this.x2);

		// Loop across the passed values of the third axis and store them at
		// the class level.
		for (int i = 0; i < this.lPoints; i++) {
			this.x3[i] = x3[i];
		}

		// Store the minimum and maximum values of the third axis at the class
		// level.
		this.xMin[2] = VectorMath.minimum(this.x3);
		this.xMax[2] = VectorMath.maximum(this.x3);

		// Loop across the passed grid values and store them at the class level
		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < this.mPoints; j++) {
				for (int k = 0; k < this.lPoints; k++) {
					this.y[i][j][k] = y[i][j][k];
				}
			}
		}
		// The following starred comments are why we cannot simply use resetData
		// in the constructor.

		// *Create a new Spline object of length nPoints, and store at the class
		// level.
		this.spln = new Spline(this.nPoints);

		// *Create a nPoints 1D array of Spline2D objects (mPoints x lPoints),
		// and store at the class level
		this.sp2da = Spline2D.oneDarray(this.nPoints, this.mPoints,
				this.lPoints);

		// Create a temporary 2D double array of size mPoints x lPoints
		double[][] yTempml = new double[this.mPoints][this.lPoints];

		// Loop across the array of Spline2D objects (nPoints length)
		for (int i = 0; i < this.nPoints; i++) {

			// 2D loop: Fill the temporary 2D array by slicing the passed y
			// array along the first axis.
			for (int j = 0; j < this.mPoints; j++) {
				for (int k = 0; k < this.lPoints; k++) {
					yTempml[j][k] = y[i][j][k];
				}
			}
			// In the process of looping along the Spline2D array, reset the
			// class level data for each Spline2D object using the second axis,
			// third axis and temporary (sliced) values.
			this.sp2da[i].resetData(x2, x3, yTempml);

			// Retrieve the second derivative values from the Spline2D object
			// and store in the class level array.
			this.d2ydx2inner[i] = this.sp2da[i].getDeriv();
		}
	}

	/**
	 * Returns an interpolated value of y for values of x1, x2 and x3 from a
	 * tabulated function y=f(x1,x2,x3)
	 * 
	 * @param xx1
	 * @param xx2
	 * @param xx3
	 */

	public double interpolate(double xx1, double xx2, double xx3) {

		// Create a temporary array of doubles of length n

		double[] yTempm = new double[nPoints];

		// Loop across the temporary array and fill it with interpolated
		// values generated by the ith BicubicSpline stored in the
		// class-level array using the passed values/positions on the second and
		// third axes.

		for (int i = 0; i < nPoints; i++) {
			yTempm[i] = this.sp2da[i].interpolate(xx2, xx3);
		}

		// Store the array interpolated values stored in a class-level variable

		this.spln.resetData(x1, yTempm);

		// Return the interpolated value at the passed value/position on the
		// first axis.
		return this.spln.interpolate(xx1);
	}

	/**
	 * Sets the values from which the interpolations will be derived.
	 * 
	 * @param y
	 *            - a 3-dimensional matrix of float values.
	 */

	public void setValues(float[][][] y) {

		// 3D Loop: Overwrite class-level grid values
		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < this.mPoints; j++) {
				for (int k = 0; k < this.lPoints; k++) {
					this.y[i][j][k] = y[i][j][k];
				}
			}
			// Within the first level of the loop, also set the values
			// for each Spline2D object
			sp2da[i].setValues(y[i]);
		}
	}

	/**
	 * GET: Retrieves the dimension of the values being used for interpolation.
	 */

	public int[] getDim() {
		return new int[] { nPoints, mPoints, lPoints };
	}
}
