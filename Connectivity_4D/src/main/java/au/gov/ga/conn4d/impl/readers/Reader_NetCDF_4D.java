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

package au.gov.ga.conn4d.impl.readers;

import java.io.IOException;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import au.gov.ga.conn4d.utils.IndexLookup_Nearest;

/**
 * Retrieves values from a 4D NetCDF File (time,z,y,x)
 * 
 * @author Johnathan Kool
 */

public class Reader_NetCDF_4D extends Reader_NetCDF {

	private String depthName = "Depth";
	private IndexLookup_Nearest lats, lons, time, depth;

	/**
	 * Constructor accepting a String containing the path of the resource.
	 * 
	 * @param fileName - the path of the resource
	 */

	public Reader_NetCDF_4D(String fileName) {
		super(fileName);
	}

	/**
	 * Constructor accepting a String containing the path of the resource, as
	 * well as Strings containing the variable names for latitude, longitude,
	 * time, and the data content.
	 * 
	 * @param fileName - the path of the resource
	 * @param varName - the name of the data content variable
	 * @param timeName - the name of the time variable
	 * @param depthName - the name of the depth variable
	 * @param latName - the name of the latitude variable
	 * @param lonName - the name of the longitude variable
	 * @throws IOException
	 */

	public Reader_NetCDF_4D(String fileName, String varName, String timeName,
			String depthName, String latName, String lonName)
			throws IOException {

		netcdfFile = NetcdfFile.open(fileName);
		bndVar = netcdfFile.findVariable(varName);
		this.latName = latName;
		this.lonName = lonName;
		this.timeName = timeName;
		this.depthName = depthName;
	}

	/**
	 * Returns a clone of the class instance
	 */
	
	@Override
	public Reader_NetCDF_4D clone() {
		Reader_NetCDF_4D ncb;
		ncb = new Reader_NetCDF_4D(netcdfFile.getLocation());
		ncb.neglon = neglon;
		return ncb;
	}

	/**
	 * Retrieves the value from the NetCDF file corresponding to the variable
	 * name set in varName.
	 * 
	 * @param t
	 *            - time lookup value
	 * @param z
	 * 			  - z lookup value
	 * @param x
	 *            - x lookup value
	 * @param y
	 *            - y lookup value
	 */
	
	public double getValue(double t, double z, double x, double y) {

		if (neglon) {
			x = (x + 180) % 360 - 180;
		}

		int tval = time.lookup(t);
		int k = depth.lookup(z);
		int i = lats.lookup(y);
		int j = lons.lookup(x);

		if (time.isIn_Bounds() != 0 || lats.isIn_Bounds() != 0
				|| lons.isIn_Bounds() != 0) {
			return Double.NaN;
		}

		Array bnd = null;
		try {
			bnd = bndVar.read(new int[] { tval, k, i, j }, new int[] { 1, 1, 1,
					1 });
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidRangeException e) {
			e.printStackTrace();
		}
		return bnd.getDouble(0);
	}

	/**
	 * Initializes the class by generating index lookups.
	 * 
	 * @throws IOException
	 */
	
	public void initialize() throws IOException {
		lats = new IndexLookup_Nearest(netcdfFile.findVariable(latName));
		lons = new IndexLookup_Nearest(netcdfFile.findVariable(lonName));
		time = new IndexLookup_Nearest(netcdfFile.findVariable(timeName));
		depth = new IndexLookup_Nearest(netcdfFile.findVariable(depthName));
	}
	
	/**
	 * Returns the minimum and maximum values for each dimension.
	 */
	
	public double[][] getBounds(){
		double[][] bounds = new double[4][2];
		bounds[0][0] = time.getMinVal();
		bounds[0][1] = time.getMaxVal();
		bounds[1][0] = depth.getMinVal();
		bounds[1][1] = depth.getMaxVal();
		bounds[2][0] = lons.getMinVal();
		bounds[2][1] = lons.getMaxVal();
		bounds[3][0] = lats.getMinVal();
		bounds[3][1] = lats.getMaxVal();
		return bounds;
	}
}
