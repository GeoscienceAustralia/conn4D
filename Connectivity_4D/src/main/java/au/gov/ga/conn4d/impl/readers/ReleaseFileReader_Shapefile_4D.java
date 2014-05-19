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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.geotools.data.shapefile.ShapefileDataStore;

import au.gov.ga.conn4d.Parameters;

import com.vividsolutions.jts.geom.Geometry;


/**
 * ReleaseFileReader using a shapefile (.shp) to provide release parameters at
 * each location.  Extended for 4D releases where initial depth range
 * is important.
 *  
 * @author Johnathan Kool
 */

public class ReleaseFileReader_Shapefile_4D extends ReleaseFileReader_Shapefile{
	
	private String header_minDepth = "MINDEPTH";
	private String header_maxDepth = "MAXDEPTH";
	private double minDepth, maxDepth;

	public ReleaseFileReader_Shapefile_4D(String filename)
			throws FileNotFoundException, MalformedURLException, IOException {
		  
		File f = new File(filename);
		  URL shapeURL = f.toURI().toURL();

		  dataStore = new ShapefileDataStore(shapeURL);

		  String name = dataStore.getTypeNames()[0];
		  source = dataStore.getFeatureSource(name);
		  collection = source.getFeatures();
		  iterator = collection.features();
		  
		  readNext();	  
	}
	
	/**
	 * Parses the data contained by a single record
	 */
	
	@Override
	protected void parse() {

		position = (Geometry) super.feat.getDefaultGeometry();
		
		Object o = feat.getAttribute(header_ID);
		
		if(o instanceof java.lang.Integer){
			relID = ((Integer) feat.getAttribute(header_ID)).longValue();	
		}
		else{
			relID = (Long) feat.getAttribute(header_ID);
		}
		
		minDepth = ((Double) feat.getAttribute(this.header_minDepth)).floatValue();
		maxDepth = ((Double) feat.getAttribute(this.header_maxDepth)).floatValue();
		
		//Npart *must* be an integer, because the CountDownLatch controlling
		//the number of releases only accepts integers.
		
		npart = (Integer) feat.getAttribute(header_Npart);
		locName = (String) feat.getAttribute(header_locName);
	}
	
	/**
	 * Updates the values in the provided Parameters file.
	 */

	@Override
	public Parameters setParameters(Parameters d) {
		d.setLocName(super.locName);
		d.setNPart(super.npart);
		d.setPosition(super.position);
		d.setDepthRange(minDepth, maxDepth);
		return d;
	}
	
	/**
	 * Normally used to retrieve a depth from the class, however, here depth
	 * is provided as a range, therefore an IllegalArgumentException is thrown.
	 */

	@Override
	public float getDepth() {
		throw new IllegalArgumentException("Depth is provided as a range by this class");
	}
	
	/**
	 * Retrieves the minimum release depth
	 */
	
	public double getMinDepth(){
		return minDepth;
	}
	
	/**
	 * Retrieves the maximum release depth
	 */
	
	public double getMaxDepth(){
		return maxDepth;
	}
}
