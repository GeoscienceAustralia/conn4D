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

package au.gov.ga.conn4d;

/**
 * Retrieves velocity values from a designated source
 * 
 * @author Johnathan Kool
 * 
 */

public interface VelocityReader {

	/**
	 * Generates a clone of the VelocityReader object
	 * 
	 * @return - a clone of the VelocityReader
	 */

	public abstract VelocityReader clone();

	/**
	 * Releases resources associated with the VelocityReader instance.
	 */

	public abstract void close();

	/**
	 * Retrieves a 4x2 array of minimum and maximum values for time, depth,
	 * vertical dimensions and horizontal dimensions in that order.
	 */

	public abstract double[][] getBounds();

	/**
	 * Retrieves the array object representing a NODATA value from the velocity
	 * files.
	 * 
	 * @return - array object representing NODATA
	 */

	public double[] getNODATA();

	/**
	 * Retrieves the shape/dimensions of the velocity field
	 */

	public abstract int[][] getShape();

	/**
	 * Retrieves the units of the velocity field (e.g. meters per second).
	 */

	public abstract String getUnits();

	/**
	 * Retrieves the velocity values at the given coordinates
	 * 
	 * @param time - Time
	 * @param z - Depth
	 * @param lon - Longitude
	 * @param lat - Latitude
	 * @return - Coordinate pair containing u and v velocity values
	 */

	public abstract double[] getVelocities(long time, double z, double lon,
			double lat);

	/**
	 * Identifies whether previously queried velocity values contained a NODATA
	 * value.
	 * 
	 * @return boolean indicating whether NODATA was in the vicinity of queried
	 *         velocity values
	 */

	public abstract boolean isNearNoData();

}
