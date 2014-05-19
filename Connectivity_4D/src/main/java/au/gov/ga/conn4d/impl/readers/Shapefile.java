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
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;

import au.gov.ga.conn4d.Habitat;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;

/**
 * Provides basic capability for reading Shapefiles given
 * a path location.  A STRtree is built for the data
 * upon construction.
 * 
 * @author Johnathan Kool
 */

public class Shapefile implements Habitat{

	private ShapefileDataStore dataStore;
	private FeatureSource<SimpleFeatureType,SimpleFeature> source;
	private Geometry geom;
	private boolean negLon;
	private String filename;
	private int nPatches;
	private String luField = "POLYNUM";
	private double minx = Double.MAX_VALUE;
	private double miny = Double.MAX_VALUE;
	private double maxx = -Double.MAX_VALUE;
	private double maxy = -Double.MAX_VALUE;
	
	private SpatialIndex index = new STRtree();

	public Shapefile(){}
	
	public Shapefile(String filename){
		try {
			setDataSource(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.filename = filename;
	}
	
	/**
	 * Sets the data source of the Shapefile using a String
	 * containing the path of the resource.
	 */
	
	@Override
	public void setDataSource(String filename) throws IOException {
		
		  this.filename = filename;
		  File f = new File(filename);
		  URL shapeURL = f.toURI().toURL();

		  dataStore = new ShapefileDataStore(shapeURL);

		  String name = dataStore.getTypeNames()[0];
		  source = dataStore.getFeatureSource(name);
		  
		  SimpleFeatureType schema = source.getSchema();
		  Query query = new Query( schema.getTypeName(), Filter.INCLUDE );
		  nPatches = source.getCount(query);

		buildSearchTree();
	}
	
	/**
	 * Builds a spatial search index for the data
	 */
	
	private void buildSearchTree() {

		FeatureIterator<SimpleFeature> iterator = null;
		
		try {
			FeatureCollection<SimpleFeatureType,SimpleFeature> collection = source.getFeatures();
			iterator = collection.features();
			while (iterator.hasNext()) {
				SimpleFeature feature = iterator.next();
				geom = (Geometry) feature.getDefaultGeometry();
				minx = Math.min(minx, geom.getEnvelopeInternal().getMinX());
				miny = Math.min(miny, geom.getEnvelopeInternal().getMinY());
				maxx = Math.max(maxx, geom.getEnvelopeInternal().getMaxX());
				maxy = Math.max(maxy, geom.getEnvelopeInternal().getMaxY());
				index.insert(geom.getEnvelopeInternal(), feature);
			}

		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(iterator != null){
				iterator.close();
			}
		}
	}
	
	/**
	 * Retrieves a FeatureIterator for the data set
	 */
	
	public FeatureIterator<SimpleFeature> getIterator(){
		try {
			FeatureCollection<SimpleFeatureType,SimpleFeature> collection = source.getFeatures();
			return collection.features();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Retrieves the spatial index of the data set
	 */
	
	public SpatialIndex getSpatialIndex(){
		return index;
	}
	
	/**
	 * Identifies whether the data set uses negative longitude values.
	 */
	
	public boolean hasField(String field){
		Iterator<PropertyDescriptor> it = source.getSchema().getDescriptors().iterator();
		while(it.hasNext()){
			PropertyDescriptor pd = it.next();
			if(pd.getName().toString().equals(field)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isNegLon(){
		return negLon;
	}
	
	/**
	 * Sets whether the data set uses negative longitude values.
	 */
	
	@Override
	public void setNegLon(boolean negLon){
		this.negLon = negLon;
	}
	
	/**
	 * Retrieves the number of patches/polygons in the data set
	 */
	
	public int getNPatches() {
		return nPatches;
	}
	
	/**
	 * Retrieves the name of the source data file being used.
	 */
	
	public String getFilename(){
		return filename;
	}
	
	/**
	 * Sets the name of the lookup (index) field to be used
	 * when retrieving data.
	 */
	
	@Override
	public void setLookupField(String lookupField) {
		luField = lookupField;
	}
	
	/**
	 * Gets the name of the lookup (index) field to be used
	 * when retrieving data.
	 */
	
	public String getLookupField() {
		return luField;
	}
	
	/**
	 * Closes resources associated with this instance
	 */
	
	public void close(){
		geom = null;
		dataStore.dispose();
	}

	/**
	 * Retrieves the minimum east-west value of the data set
	 */
	
	public double getMinx() {
		return minx;
	}

	/**
	 * Retrieves the minimum north-south value of the data set
	 */
	
	public double getMiny() {
		return miny;
	}

	/**
	 * Retrieves the maximum east-west value of the data set
	 */
	
	public double getMaxx() {
		return maxx;
	}

	/**
	 * Retrieves the maximum north-south value of the data set
	 */
	
	public double getMaxy() {
		return maxy;
	}
}
