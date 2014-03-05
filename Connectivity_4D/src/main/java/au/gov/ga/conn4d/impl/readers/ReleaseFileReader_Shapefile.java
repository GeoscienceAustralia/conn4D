package au.gov.ga.conn4d.impl.readers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.input.ReleaseFileReader;

import com.vividsolutions.jts.geom.Geometry;

/**
 * ReleaseFileReader using a shapefile (.shp) to provide release parameters at
 * each location.
 * 
 * @author Johnathan Kool
 */

public class ReleaseFileReader_Shapefile implements ReleaseFileReader {

	protected long relID; // Release ID
	protected Geometry position;
	private float z; // Longitude, Latitude and Depth
	protected int npart; // # of particles to be released
	protected String locName; // Location name

	private boolean EOF = false; // Indicates whether the end of the file has
									// been reached

	protected ShapefileDataStore dataStore;
	protected FeatureSource<SimpleFeatureType, SimpleFeature> source;
	protected FeatureCollection<SimpleFeatureType, SimpleFeature> collection;
	protected FeatureIterator<SimpleFeature> iterator;
	protected SimpleFeature feat;

	protected String header_ID = "POLYNUM";
	private String header_Depth = "DEPTH";
	protected String header_Npart = "NPART";
	protected String header_locName = "FNAME";

	/**
	 * No-argument constructor
	 */

	protected ReleaseFileReader_Shapefile() {
	}

	/**
	 * Constructor accepting a String providing the location of the
	 * shapefile resource.
	 * 
	 * @param fileName
	 * @throws IOException
	 */

	public ReleaseFileReader_Shapefile(String fileName) throws IOException {

		File f = new File(fileName);
		URL shapeURL = f.toURI().toURL();

		dataStore = new ShapefileDataStore(shapeURL);

		String name = dataStore.getTypeNames()[0];
		source = dataStore.getFeatureSource(name);
		collection = source.getFeatures();
		iterator = collection.features();

		readNext();
	}

	/**
	 * Retrieves the depth of the initial release point as a double value.
	 */

	@Override
	public float getDepth() {
		return z;
	}

	/**
	 * Retrieves the text name of the release location
	 */

	@Override
	public String getLocName() {
		return locName;
	}

	/**
	 * Retrieves the number of Particles to be released.
	 */

	@Override
	public long getNpart() {
		return npart;
	}

	/**
	 * Retrieves the position of the initial release point as a Geometry.
	 */

	@Override
	public Geometry getPosition() {
		return position;
	}

	/**
	 * Retrieves the source ID of the release
	 * 
	 * @return - the source ID of the release
	 */

	public long getSourceID() {
		return relID;
	}

	// Getters and Setters

	/**
	 * Indicates whether there are remaining records to be read
	 */

	@Override
	public boolean hasNext() {
		return !EOF;
	}

	/**
	 * Parses the next available record
	 */

	@Override
	public void next() {

		parse();
		readNext();
	}

	/**
	 * Parses the information from the line
	 */

	protected void parse() {

		position = (Geometry) feat.getDefaultGeometry();

		Object o = feat.getAttribute(header_ID);
		if (o instanceof java.lang.Integer) {
			relID = ((Integer) feat.getAttribute(header_ID)).longValue();
		} else {
			relID = (Long) feat.getAttribute(header_ID);
		}

		z = ((Double) feat.getAttribute(header_Depth)).floatValue();

		// Npart *must* be an integer, because the CountDownLatch controlling
		// the number of releases only accepts integers.

		npart = (Integer) feat.getAttribute(header_Npart);
		locName = (String) feat.getAttribute(header_locName);
	}

	/**
	 * Does the grunt work of reading the information from the line and flagging
	 * EOF if there is no more data.
	 */

	protected void readNext() {

		try {
			if (!iterator.hasNext()) {
				EOF = true;
				return;
			}
			feat = iterator.next();
			parse();

		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the values of the passed-in Parameters object
	 */

	@Override
	public Parameters setParameters(Parameters d) {
		d.setLocName(locName);
		d.setNPart(npart);
		d.setPosition(position);
		d.setDepth(z);
		return d;
	}
}
