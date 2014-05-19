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

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 * Abstract class for retrieving values from an oceanographic NetCDF File
 * 
 * @author Johnathan Kool
 */

public abstract class Reader_NetCDF {

	protected NetcdfFile netcdfFile;
	protected Variable bndVar;
	protected String varName = "variable needs to be set";
	protected String latName = "Latitude";
	protected String lonName = "Longitude";
	protected String timeName = "Time";
	protected boolean neglon = false;

	protected Reader_NetCDF(){}
	
	/**
	 * Constructor accepting a String containing the path of the resource.
	 * 
	 * @param fileName - the path of the resource
	 */

	public Reader_NetCDF(String fileName) {

		try {
			netcdfFile = NetcdfFile.open(fileName);
			bndVar = netcdfFile.findVariable(varName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a clone of the class instance
	 */
	
	@Override
	public abstract Reader_NetCDF clone();

	/**
	 * Retrieves the name of the variable being used to obtain latitude values
	 * 
	 * @return - the name of the variable being used to obtain latitude values
	 */
	
	public String getLatName() {
		return latName;
	}

	/**
	 * Retrieves the name of the variable being used to obtain longitude values
	 * 
	 * @return - the name of the variable being used to obtain longitude values
	 */
	
	public String getLonName() {
		return lonName;
	}

	/**
	 * Retrieves the name of the variable being used as the data content source.
	 */
	
	public String getVariableName() {
		return varName;
	}

	/**
	 * Initializes the class by generating index lookups.
	 * 
	 * @throws IOException
	 */
	
	public abstract void initialize() throws IOException;

	/**
	 * Identifies whether the data source uses negative longitude values.
	 * 
	 * @return - whether the data source uses negative longitude values
	 */
	
	public boolean hasNeglon() {
		return neglon;
	}
	
	/**
	 * Sets the name of the variable being used to retrieve latitude values.
	 * 
	 * @param latName
	 */

	public void setLatName(String latName) {
		this.latName = latName;
	}

	/**
	 * Sets the name of the variable being used to retrieve longitude values.
	 * 
	 * @param lonName
	 */
	
	public void setLonName(String lonName) {
		this.lonName = lonName;
	}

	/**
	 * Sets whether the data source uses negative longitude values.
	 * 
	 * @param neglon
	 */
	
	public void setNeglon(boolean neglon) {
		this.neglon = neglon;
	}

	/**
	 * Sets the name of the variable being used as the data content source.
	 * 
	 * @param variableName
	 */
	
	public void setVariableName(String variableName) {
		this.varName = variableName;
	}
	
	/**
	 * Returns the minimum and maximum values for each dimension.
	 */
	
	public abstract double[][] getBounds();
}
