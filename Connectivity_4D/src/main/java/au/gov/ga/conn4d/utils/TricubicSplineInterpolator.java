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

/*
 * TricubicSplineInterpolator modified from apache.commons.math3
 * to accommodate float value inputs, and to handle descending z values.
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.math3.analysis.interpolation.TrivariateGridInterpolator;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.util.MathArrays;

/**
 * Generates a tricubic interpolating function.
 * 
 * MODIFIED from the original on 19/05/2014 by Johnathan Kool to accept
 * float arrays and descending z values.
 * 
 * @since 2.2
 * @version $Id: TricubicSplineInterpolator.java 1379904 2012-09-01 23:54:52Z
 *          erans $
 */
public class TricubicSplineInterpolator implements TrivariateGridInterpolator {
	
	/**
	 * {@inheritDoc}
	 */
	public TricubicSplineInterpolatingFunction interpolate(final double[] zval,
			final double[] yval, final double[] xval, final double[][][] fval)
			throws NoDataException, DimensionMismatchException,
			NonMonotonicSequenceException {
		if (zval.length == 0 || yval.length == 0 || xval.length == 0
				|| fval.length == 0) {
			throw new NoDataException();
		}
		if (zval.length != fval.length) {
			throw new DimensionMismatchException(zval.length, fval.length);
		}
		
		MathArrays.checkOrder(zval);
		MathArrays.checkOrder(yval);
		
		final int zLen = zval.length;
		final int yLen = yval.length;
		final int xLen = xval.length;
		
		final double[] zzval;
		final double[][][] ffval = new double[zLen][][];
		
		if(zval[0]>zval[zLen-1]){
			zzval = VectorMath.selectionSort(zval);
			for(int i = 0; i<zLen; i++){
				ffval[i]=fval[zLen-1-i];
			}
		}
		else{
			zzval = zval;
		}

		// Samples, re-ordered as (z, x, y) and (y, z, x) tuplets
		// fvalXY[k][i][j] = f(xval[i], yval[j], zval[k])
		// fvalZX[j][k][i] = f(xval[i], yval[j], zval[k])
		final double[][][] fvalXY = new double[xLen][zLen][yLen];
		final double[][][] fvalZX = new double[yLen][xLen][zLen];
		for (int i = 0; i < zLen; i++) {
			if (ffval[i].length != yLen) {
				throw new DimensionMismatchException(ffval[i].length, yLen);
			}

			for (int j = 0; j < yLen; j++) {
				if (ffval[i][j].length != xLen) {
					throw new DimensionMismatchException(ffval[i][j].length,
							xLen);
				}

				for (int k = 0; k < xLen; k++) {
					final double v = ffval[i][j][k];
					fvalXY[k][i][j] = v;
					fvalZX[j][k][i] = v;
				}
			}
		}

		final BicubicSplineInterpolator bsi = new BicubicSplineInterpolator();

		// For each line x[i] (0 <= i < xLen), construct a 2D spline in y and z
		final BicubicSplineInterpolatingFunction[] xSplineYZ = new BicubicSplineInterpolatingFunction[zLen];
		for (int i = 0; i < zLen; i++) {
			xSplineYZ[i] = bsi.interpolate(yval, xval, ffval[i]);
		}

		// For each line y[j] (0 <= j < yLen), construct a 2D spline in z and x
		final BicubicSplineInterpolatingFunction[] ySplineZX = new BicubicSplineInterpolatingFunction[yLen];
		for (int j = 0; j < yLen; j++) {
			ySplineZX[j] = bsi.interpolate(xval, zzval, fvalZX[j]);
		}

		// For each line z[k] (0 <= k < zLen), construct a 2D spline in x and y
		final BicubicSplineInterpolatingFunction[] zSplineXY = new BicubicSplineInterpolatingFunction[xLen];
		for (int k = 0; k < xLen; k++) {
			zSplineXY[k] = bsi.interpolate(zzval, yval, fvalXY[k]);
		}

		// Partial derivatives wrt x and wrt y
		final double[][][] dFdX = new double[zLen][yLen][xLen];
		final double[][][] dFdY = new double[zLen][yLen][xLen];
		final double[][][] d2FdXdY = new double[zLen][yLen][xLen];
		for (int k = 0; k < xLen; k++) {
			final BicubicSplineInterpolatingFunction f = zSplineXY[k];
			for (int i = 0; i < zLen; i++) {
				final double z = zzval[i];
				for (int j = 0; j < yLen; j++) {
					final double y = yval[j];
					dFdX[i][j][k] = f.partialDerivativeX(z, y);
					dFdY[i][j][k] = f.partialDerivativeY(z, y);
					d2FdXdY[i][j][k] = f.partialDerivativeXY(z, y);
				}
			}
		}

		// Partial derivatives wrt y and wrt z
		final double[][][] dFdZ = new double[zLen][yLen][xLen];
		final double[][][] d2FdYdZ = new double[zLen][yLen][xLen];
		for (int i = 0; i < zLen; i++) {
			final BicubicSplineInterpolatingFunction f = xSplineYZ[i];
			for (int j = 0; j < yLen; j++) {
				final double y = yval[j];
				for (int k = 0; k < xLen; k++) {
					final double x = xval[k];
					dFdZ[i][j][k] = f.partialDerivativeY(y, x);
					d2FdYdZ[i][j][k] = f.partialDerivativeXY(y, x);
				}
			}
		}

		// Partial derivatives wrt x and wrt z
		final double[][][] d2FdZdX = new double[zLen][yLen][xLen];
		for (int j = 0; j < yLen; j++) {
			final BicubicSplineInterpolatingFunction f = ySplineZX[j];
			for (int k = 0; k < xLen; k++) {
				final double x = xval[k];
				for (int i = 0; i < zLen; i++) {
					final double z = zzval[i];
					d2FdZdX[i][j][k] = f.partialDerivativeXY(x, z);
				}
			}
		}

		// Third partial cross-derivatives
		final double[][][] d3FdXdYdZ = new double[zLen][yLen][xLen];
		for (int i = 0; i < zLen; i++) {
			final int nI = nextIndex(i, zLen);
			final int pI = previousIndex(i);
			for (int j = 0; j < yLen; j++) {
				final int nJ = nextIndex(j, yLen);
				final int pJ = previousIndex(j);
				for (int k = 0; k < xLen; k++) {
					final int nK = nextIndex(k, xLen);
					final int pK = previousIndex(k);

					// XXX Not sure about this formula
					d3FdXdYdZ[i][j][k] = (ffval[nI][nJ][nK] - ffval[nI][pJ][nK]
							- ffval[pI][nJ][nK] + ffval[pI][pJ][nK]
							- ffval[nI][nJ][pK] + ffval[nI][pJ][pK]
							+ ffval[pI][nJ][pK] - ffval[pI][pJ][pK])
							/ ((zzval[nI] - zzval[pI]) * (yval[nJ] - yval[pJ]) * (xval[nK] - xval[pK]));
				}
			}
		}

		// Create the interpolating splines
		return new TricubicSplineInterpolatingFunction(zzval, yval, xval, ffval,
				dFdX, dFdY, dFdZ, d2FdXdY, d2FdZdX, d2FdYdZ, d3FdXdYdZ);
	}

	public TricubicSplineInterpolatingFunction interpolate(final double[] zval,
			final double[] yval, final double[] xval, final float[][][] fval)
			throws NoDataException, DimensionMismatchException,
			NonMonotonicSequenceException {
		if (zval.length == 0 || yval.length == 0 || xval.length == 0
				|| fval.length == 0) {
			throw new NoDataException();
		}
		if (zval.length != fval.length) {
			throw new DimensionMismatchException(zval.length, fval.length);
		}

		MathArrays.checkOrder(yval);
		MathArrays.checkOrder(xval);

		final int zLen = zval.length;
		final int yLen = yval.length;
		final int xLen = xval.length;
		
		final double[] zzval;
		float[][][] ffval = new float[zLen][][];
		
		if(zval[0]>zval[zLen-1]){
			zzval = VectorMath.selectionSort(zval);
			for(int i = 0; i<zLen; i++){
				ffval[i]=fval[zLen-1-i];
			}
		}
		else{
			zzval = zval;
			ffval=fval;
		}

		// Samples, re-ordered as (z, x, y) and (y, z, x) tuplets
		// fvalXY[k][i][j] = f(xval[i], yval[j], zval[k])
		// fvalZX[j][k][i] = f(xval[i], yval[j], zval[k])
		final double[][][] fvalXY = new double[xLen][zLen][yLen];
		final double[][][] fvalZX = new double[yLen][xLen][zLen];
		for (int i = 0; i < zLen; i++) {
			if (ffval[i].length != yLen) {
				throw new DimensionMismatchException(ffval[i].length, yLen);
			}

			for (int j = 0; j < yLen; j++) {
				if (ffval[i][j].length != xLen) {
					throw new DimensionMismatchException(ffval[i][j].length,
							xLen);
				}

				for (int k = 0; k < xLen; k++) {
					final double v = ffval[i][j][k];
					fvalXY[k][i][j] = v;
					fvalZX[j][k][i] = v;
				}
			}
		}

		final BicubicSplineInterpolator bsi = new BicubicSplineInterpolator();

		// For each line x[i] (0 <= i < xLen), construct a 2D spline in y and z
		final BicubicSplineInterpolatingFunction[] xSplineYZ = new BicubicSplineInterpolatingFunction[zLen];
		for (int i = 0; i < zLen; i++) {
			xSplineYZ[i] = bsi.interpolate(yval, xval, ffval[i]);
		}

		// For each line y[j] (0 <= j < yLen), construct a 2D spline in z and x
		final BicubicSplineInterpolatingFunction[] ySplineZX = new BicubicSplineInterpolatingFunction[yLen];
		for (int j = 0; j < yLen; j++) {
			ySplineZX[j] = bsi.interpolate(xval, zzval, fvalZX[j]);
		}

		// For each line z[k] (0 <= k < zLen), construct a 2D spline in x and y
		final BicubicSplineInterpolatingFunction[] zSplineXY = new BicubicSplineInterpolatingFunction[xLen];
		for (int k = 0; k < xLen; k++) {
			zSplineXY[k] = bsi.interpolate(zzval, yval, fvalXY[k]);
		}

		// Partial derivatives wrt x and wrt y
		final double[][][] dFdX = new double[zLen][yLen][xLen];
		final double[][][] dFdY = new double[zLen][yLen][xLen];
		final double[][][] d2FdXdY = new double[zLen][yLen][xLen];
		for (int k = 0; k < xLen; k++) {
			final BicubicSplineInterpolatingFunction f = zSplineXY[k];
			for (int i = 0; i < zLen; i++) {
				final double x = zzval[i];
				for (int j = 0; j < yLen; j++) {
					final double y = yval[j];
					dFdX[i][j][k] = f.partialDerivativeX(x, y);
					dFdY[i][j][k] = f.partialDerivativeY(x, y);
					d2FdXdY[i][j][k] = f.partialDerivativeXY(x, y);
				}
			}
		}

		// Partial derivatives wrt y and wrt z
		final double[][][] dFdZ = new double[zLen][yLen][xLen];
		final double[][][] d2FdYdZ = new double[zLen][yLen][xLen];
		for (int i = 0; i < zLen; i++) {
			final BicubicSplineInterpolatingFunction f = xSplineYZ[i];
			for (int j = 0; j < yLen; j++) {
				final double y = yval[j];
				for (int k = 0; k < xLen; k++) {
					final double z = xval[k];
					dFdZ[i][j][k] = f.partialDerivativeY(y, z);
					d2FdYdZ[i][j][k] = f.partialDerivativeXY(y, z);
				}
			}
		}

		// Partial derivatives wrt x and wrt z
		final double[][][] d2FdZdX = new double[zLen][yLen][xLen];
		for (int j = 0; j < yLen; j++) {
			final BicubicSplineInterpolatingFunction f = ySplineZX[j];
			for (int k = 0; k < xLen; k++) {
				final double z = xval[k];
				for (int i = 0; i < zLen; i++) {
					final double x = zzval[i];
					d2FdZdX[i][j][k] = f.partialDerivativeXY(z, x);
				}
			}
		}

		// Third partial cross-derivatives
		final double[][][] d3FdXdYdZ = new double[zLen][yLen][xLen];
		for (int i = 0; i < zLen; i++) {
			final int nI = nextIndex(i, zLen);
			final int pI = previousIndex(i);
			for (int j = 0; j < yLen; j++) {
				final int nJ = nextIndex(j, yLen);
				final int pJ = previousIndex(j);
				for (int k = 0; k < xLen; k++) {
					final int nK = nextIndex(k, xLen);
					final int pK = previousIndex(k);

					// XXX Not sure about this formula
					d3FdXdYdZ[i][j][k] = (ffval[nI][nJ][nK] - ffval[nI][pJ][nK]
							- ffval[pI][nJ][nK] + ffval[pI][pJ][nK]
							- ffval[nI][nJ][pK] + ffval[nI][pJ][pK]
							+ ffval[pI][nJ][pK] - ffval[pI][pJ][pK])
							/ ((zzval[nI] - zzval[pI]) * (yval[nJ] - yval[pJ]) * (xval[nK] - xval[pK]));
				}
			}
		}

		// Create the interpolating splines
		return new TricubicSplineInterpolatingFunction(zzval, yval, xval, ffval,
				dFdX, dFdY, dFdZ, d2FdXdY, d2FdZdX, d2FdYdZ, d3FdXdYdZ);
	}

	/**
	 * Compute the next index of an array, clipping if necessary. It is assumed
	 * (but not checked) that {@code i} is larger than or equal to 0}.
	 * 
	 * @param i
	 *            Index
	 * @param max
	 *            Upper limit of the array
	 * @return the next index
	 */
	private int nextIndex(int i, int max) {
		final int index = i + 1;
		return index < max ? index : index - 1;
	}

	/**
	 * Compute the previous index of an array, clipping if necessary. It is
	 * assumed (but not checked) that {@code i} is smaller than the size of the
	 * array.
	 * 
	 * @param i
	 *            Index
	 * @return the previous index
	 */
	private int previousIndex(int i) {
		final int index = i - 1;
		return index >= 0 ? index : 0;
	}
}
