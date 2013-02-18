package lagrange.impl.readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

import lagrange.Parameters;
import lagrange.input.ReleaseFileReader;

/**
 * 
 * @author Johnathan Kool
 * 
 */

public class ReleaseFileReader_Shapefile implements ReleaseFileReader{

	private long relID; // Release ID
	private Geometry position;
	private float z; // Longitude, Latitude and Depth
	private int npart; // # of particles to be released
	private String locName; // Location name

	private boolean EOF = false; // Indicates whether the end of the file has
									// been reached

	private ShapefileDataStore dataStore;
	private FeatureSource<SimpleFeatureType,SimpleFeature> source;
	private FeatureCollection<SimpleFeatureType,SimpleFeature> collection;
	private FeatureIterator<SimpleFeature> iterator;
	private SimpleFeature feat;

	private String header_ID = "POLYNUM";
	private String header_Depth = "DEPTH";
	private String header_Npart = "NPART";
	private String header_locName = "FNAME";

	public ReleaseFileReader_Shapefile(String filename)
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
	 * Indicates whether there are remaining records to be read
	 */

	public boolean hasNext() {
		return !EOF;
	}

	/**
	 * Parses the next available record
	 */

	public void next() {

		parse();
		readNext();
	}

	/**
	 * Parses the information from the line
	 */

	private void parse() {

		position = (Geometry) feat.getDefaultGeometry();
		
		Object o = feat.getAttribute(header_ID);
		if(o instanceof java.lang.Integer){
			relID = ((Integer) feat.getAttribute(header_ID)).longValue();	
		}
		else{
			relID = (Long) feat.getAttribute(header_ID);
		}
		
		z = ((Double) feat.getAttribute(header_Depth)).floatValue();
		
		//Npart *must* be an integer, because the CountDownLatch controlling
		//the number of releases only accepts integers.
		
		npart = (Integer) feat.getAttribute(header_Npart);
		locName = (String) feat.getAttribute(header_locName);
	}

	/**
	 * Does the grunt work of reading the information from the line and flagging
	 * EOF if there is no more data.
	 */

	private void readNext() {

		try {
			if (!iterator.hasNext()) {
				EOF = true;
				return;
			}
			feat = iterator.next();
			parse();

		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Parameters setParameters(Parameters d) {
		d.setLocName(locName);
		d.setNPart(npart);
		d.setPosition(position);
		d.setDepth(z);
		return d;
	}

	// Getters and Setters

	public long getSourceID() {
		return relID;
	}

	public Geometry getPosition() {
		return position;
	}

	public float getDepth() {
		return z;
	}

	public long getNpart() {
		return npart;
	}

	public String getLocName() {
		return locName;
	}
}
