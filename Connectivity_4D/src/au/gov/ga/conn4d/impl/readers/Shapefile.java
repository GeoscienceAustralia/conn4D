package au.gov.ga.conn4d.impl.readers;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;


import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
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
	 * 
	 * @return
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
	 * 
	 * @return
	 */
	
	public SpatialIndex getSpatialIndex(){
		return index;
	}
	
	/**
	 * Identifies whether the data set uses negative longitude values.
	 * 
	 * @return
	 */
	
	public boolean isNegLon(){
		return negLon;
	}
	
	/**
	 * Sets whether the data set uses negative longitude values.
	 * 
	 * @return
	 */
	
	@Override
	public void setNegLon(boolean negLon){
		this.negLon = negLon;
	}
	
	/**
	 * Retrieves the number of patches/polygons in the data set
	 * 
	 * @return
	 */
	
	public int getNPatches() {
		return nPatches;
	}
	
	/**
	 * Retrieves the name of the source data file being used.
	 * 
	 * @return
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
	 * @return
	 */
	
	public double getMinx() {
		return minx;
	}

	/**
	 * Retrieves the minimum north-south value of the data set
	 * @return
	 */
	
	public double getMiny() {
		return miny;
	}

	/**
	 * Retrieves the maximum east-west value of the data set
	 * @return
	 */
	
	public double getMaxx() {
		return maxx;
	}

	/**
	 * Retrieves the maximum north-south value of the data set
	 * @return
	 */
	
	public double getMaxy() {
		return maxy;
	}
}
