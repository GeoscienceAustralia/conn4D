package lagrange.impl.readers;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;

import lagrange.Habitat;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;


public class Shapefile implements Habitat{

	private ShapefileDataStore dataStore;
	private FeatureSource<SimpleFeatureType,SimpleFeature> source;
	private Geometry geom;
	boolean negLon;
	private String filename;
	private int nPatches;
	private String luField = "POLYNUM";
	

	protected SpatialIndex index = new STRtree();

	public Shapefile(){}
	
	public Shapefile(String filename){
		try {
			setDataSource(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.filename = filename;
	}
	
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
	
	private void buildSearchTree() {

		FeatureIterator<SimpleFeature> iterator = null;
		
		try {
			FeatureCollection<SimpleFeatureType,SimpleFeature> collection = source.getFeatures();
			iterator = collection.features();

			while (iterator.hasNext()) {
				SimpleFeature feature = iterator.next();
				geom = (Geometry) feature.getDefaultGeometry();
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
	
	public FeatureIterator<SimpleFeature> getIterator(){
		try {
			FeatureCollection<SimpleFeatureType,SimpleFeature> collection = source.getFeatures();
			return collection.features();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public SpatialIndex getSpatialIndex(){
		return index;
	}
	
	public boolean isNegLon(){
		return negLon;
	}
	
	@Override
	public void setNegLon(boolean negLon){
		this.negLon = negLon;
	}
	
	public int getNPatches() {
		return nPatches;
	}
	
	public String getFilename(){
		return filename;
	}
	
	@Override
	public void setLookupField(String lookupField) {
		luField = lookupField;
	}
	
	public String getLookupField() {
		return luField;
	}
	
	public void close(){
		geom = null;
		dataStore.dispose();
	}
}
