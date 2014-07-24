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
 
package au.gov.ga.conn4d.input;

/**
 * Local parameters used in the model.  Local in the sense that if the program is distributed,
 * these are resources that are found on the local machine.  They are not passed through the
 * network.  Examples include file and pathnames for velocity and GIS files.
 * 
 * @author Johnathan Kool
 */

public class EnvironmentParameters extends ParameterReader {

	public String uname = "uvel"; // Name of the u parameter in the velocity files (e.g. "UVEL")
	public String vname = "vvel"; // Name of the v parameter in the velocity files (e.g. "VVEL")
	public String wname = "wvel"; // Name of the w parameter in the velocity files (e.g. "VVEL")
	public String ufile = "uvel.nc"; // Path and name of the NetCDF file containing the uvel information
	public String vfile = "vvel.nc"; // Path and name of the NetCDF file containing the vvel information
	public String wfile = "wvel.nc"; // Path and name of the NetCDF file containing the wvel information
	public String veldir = ".\\";
	public String latName = "Latitude"; // Name of the latitude parameter in the velocity files
	public String lonName = "Longitude"; // Name of the longitude parameter in the velocity files
	public String kName = "Depth"; // Name of the Depth parameter in the velocity files
	public String tName = "Time"; // Name of the Time parameter in the velocity files
	public boolean negOceanCoord = false;// Does the oceanographic model have negative coordinate values?
	public String polyFileName = "reefpolys.shp";// Name of the settlement polygon file
	public String polyKey = "ID"; // Index field of the settlement polygon file
	public boolean negCoord = false; // Are negative coordinate values being used?
	public String landFileName = "landmask.shp"; // Name of the land mask file
	public String bathymetryFileName = "bathymetry.asc";
	public String trajOutputDir = ".\\";		// Output directory for trajectory files
	public String vertFile = "vertical.vrt";	// Path and name of the vertical migration matrix.
	public String velocityType = "IANN";		// Velocity type (IANN- Interannual or CLIM - climatological)
	public long timeOffset = -2177521200000l;   // Offset between velocity time (HYCOM-1900) and Java's base time (1970)
	public String timeOffsetUnits = "milliseconds";
	public int bufferSize = 32768;
	public int poolSize = 8;
	
	/**
	 * No argument constructor.
	 */
	
	public EnvironmentParameters(){}
	
	/**
	 * Constructor that accepts a file name.
	 * 
	 * @param str - name of the file to be read
	 */
	
	public EnvironmentParameters(String str) {
		readFile(str);
	}	
}
