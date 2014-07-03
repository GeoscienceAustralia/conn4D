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


import au.gov.ga.conn4d.Parameters;

import com.vividsolutions.jts.geom.Geometry;


/**
 * General interface for reading from Release Files
 * 
 * @author Johnathan Kool
 *
 */

public interface ReleaseFileReader {
	
	/**
	 * Closes resources associated with the class.
	 */
	
	public void close();
	
	/**
	 * Retrieves the depth value from a single line/source in the ReleaseFile
	 * 
	 * @return float - depth value
	 */
	
	public float getDepth();

	/**
	 * Retrieves the name of a source from a single line in the release file 
	 * 
	 * @return String - the name of the source of the release objects.
	 */
	
	public String getLocName();
	
	/**
	 * Retrieves the number of Particles to be released from a single source.
	 */

	public long getNpart();

	/**
	 * Retrieves the Geometry of the location from which Particles will be released.
	 * 
	 * @return Geometry - feature representing a release location
	 */
	
	public Geometry getPosition();

	/**
	 * Identifies whether there is another line following the current position in the file.
	 * 
	 * @return boolean - indicating whether there is another line beyond the current position
	 */
	
	public boolean hasNext();
	
	/**
	 * Increments to the next line in the release file
	 */

	public void next();
	
	/**
	 * Sets the Parameter values
	 * 
	 * @param prm - 
	 */

	public Parameters setParameters(Parameters prm);
}
