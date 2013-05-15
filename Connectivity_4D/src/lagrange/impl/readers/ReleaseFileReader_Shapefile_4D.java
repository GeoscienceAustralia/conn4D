package lagrange.impl.readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import lagrange.Parameters;

import org.geotools.data.shapefile.ShapefileDataStore;

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
	 * @return
	 */
	
	public double getMinDepth(){
		return minDepth;
	}
	
	/**
	 * Retrieves the maximum release depth
	 * @return
	 */
	
	public double getMaxDepth(){
		return maxDepth;
	}
}
