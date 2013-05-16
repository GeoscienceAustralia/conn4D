package au.gov.ga.conn4d.impl.readers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import au.gov.ga.conn4d.Habitat;
import au.gov.ga.conn4d.impl.readers.Habitat_Shapefile;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;


/**
 * Reads and provides various search functions for an ESRI shape file.
 * 
 * @author Johnathan Kool
 */

public class Habitat_Shapefile implements Habitat, Cloneable{

	public final long NO_INTERSECTION = Long.MIN_VALUE;

	private ShapefileDataStore store;
	private FeatureSource<SimpleFeatureType,SimpleFeature> source;
	private FeatureCollection<SimpleFeatureType,SimpleFeature> collection;
	protected GeometryFactory gf = new GeometryFactory();
	protected String luField = "FNAME";
	private int nPatches;
	private Geometry geom;
	protected boolean neglon;
	protected String fileName;
	protected SpatialIndex index = new STRtree();

	/**
	 * Creates a pyramid of bounding boxes in order to perform fast intersect
	 * searching.
	 * 
	 * @return
	 */

	private void buildSearchTree() {

		try {

			FeatureIterator<SimpleFeature> iterator = collection.features();

			while (iterator.hasNext()) {
				SimpleFeature feature = iterator.next();
				geom = (Geometry) feature.getAttribute(0);
				index.insert(geom.getEnvelopeInternal(), feature);
			}

			iterator.close();

		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a clone of the class instance.
	 */

	@Override
	public Habitat_Shapefile clone(){
		Habitat_Shapefile out = new Habitat_Shapefile();
		try {
			out.setDataSource(fileName);
			out.neglon=neglon;
			out.luField=luField;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	/**
	 * Converts from positive Longitude coordinates to negative Longitude
	 * coordinates
	 * 
	 * @param oldlon
	 * @return
	 */

	protected synchronized double cvt(double oldlon) {
		if (oldlon > 180) {
			return -(360d - oldlon);
		} else
			return oldlon;
	}

	/**
	 * Returns the number of patches (polygons) in the shapefile.
	 */

	public int getNPatches() {
		return nPatches;
	}

	/**
	 * Detects whether the provided coordinates intersect the shapefile
	 * 
	 * @param x1 -
	 *            The x coordinate to be tested
	 * @param y1 -
	 *            The y coordinate to be tested
	 */

	@SuppressWarnings("unchecked")
	public synchronized long intersect(double x1, double y1) {

		if (neglon) {
			x1 = cvt(x1);
		}
		Point p = gf.createPoint(new Coordinate(x1, y1));
		List<SimpleFeature> fl = index.query(p.getEnvelopeInternal());

		if (fl.size() == 0) {
			return NO_INTERSECTION;
		}

		SimpleFeature f;

		for (int i = 0; i < fl.size(); i++) {

			f = fl.get(i);
			Geometry g = (Geometry) f.getAttribute(0);
			if (p.intersects(g)) {
				return ((Number) f.getAttribute(luField)).longValue();
			}
		}
		
		return NO_INTERSECTION;
	}
	
	/**
	 * Detects whether the line between the two coordinate points intersect the
	 * shapefile
	 * 
	 * @param x1 -
	 *            the initial x coordinate
	 * @param y1 -
	 *            the initial y coordinate
	 * @param x2 -
	 *            the terminal x coordinate
	 * @param y2 -
	 *            the terminal y coordinate
	 */

	@SuppressWarnings("unchecked")
	public synchronized long intersect(double x1, double y1, double x2, double y2) {

		if (neglon) {
			x1 = cvt(x1);
			x2 = cvt(x2);
		}

		LineString ls = gf.createLineString(new Coordinate[] {
				new Coordinate(x1, y1), new Coordinate(x2, y2) });
		List<SimpleFeature> fl = index.query(ls.getEnvelopeInternal());

		if (fl.size() == 0) {
			return NO_INTERSECTION;
		}

		SimpleFeature f = fl.get(0);
		Geometry g = (Geometry) f.getAttribute(0);

		if (!ls.intersects(g)) {
			return NO_INTERSECTION;
		}

		return (Integer) f.getAttribute(luField);

	}
	
	/**
	 * Identifies whether the given x,y pair intersects the shapefile 
	 * @param x
	 * @param y
	 */

	@SuppressWarnings("unchecked")
	public synchronized boolean intersects(double x, double y){
		
		if(neglon){
			x = ((180+x)%360)-180;
		}
		Point p = gf.createPoint(new Coordinate(x,y));
		List<SimpleFeature> l = index.query(p.getEnvelopeInternal());
		Iterator<SimpleFeature> it = l.iterator();
		while(it.hasNext()){
			if(((Geometry) it.next().getAttribute(0)).intersects(p)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sets the shapefile data source used by the class and builds the
	 * search tree.
	 */

	@Override
	public void setDataSource(String filename) throws IOException {

		this.fileName = filename;
		File f = new File(filename);
		URL shapeURL = f.toURI().toURL();

		store = new ShapefileDataStore(shapeURL);
		String name = store.getTypeNames()[0];
		source = store.getFeatureSource(name);
		collection = source.getFeatures();
	
		SimpleFeatureType schema = source.getSchema();
		Query query = new Query( schema.getTypeName(), Filter.INCLUDE );
		nPatches = source.getCount(query);
		
		buildSearchTree();
	}

	/**
	 * Searches the feature set to see if the given coordinates lie within any
	 * of the polygons. If so, the polygon's unique identifier is returned. If
	 * not, NODATA is returned
	 * 
	 * @param field
	 * @throws IOException
	 * @throws IllegalAttributeException
	 */

	@Override
	public void setLookupField(String field) {
		
		luField = field;
	}
	
	/**
	 * Indicates whether the coordinates use negative Longitude values.
	 * 
	 * @param neglon
	 */

	@Override
	public void setNegLon(boolean neglon) {
		this.neglon = neglon;
	}
	
	
}
