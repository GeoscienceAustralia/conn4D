package au.gov.ga.conn4d.impl.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ucar.ma2.Array;
import ucar.ma2.Index;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import au.gov.ga.conn4d.Boundary_Raster;
import au.gov.ga.conn4d.impl.collision.Intersector_3D_Poly;
import au.gov.ga.conn4d.utils.CoordinateMath;
import au.gov.ga.conn4d.utils.IndexLookup_Nearest;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;


/**
 * Reads 2-dimensional surface values from a NetCDF Grid source.
 * 
 * @author Johnathan Kool
 */

public class Boundary_Raster_NetCDF implements Boundary_Raster, Cloneable {

	private NetcdfFile boundary;
	private Variable boundaryVar;
	private String boundaryName = "bathymetry";
	private String latName = "Latitude";
	private String lonName = "Longitude";
	private boolean neglon = true;
	private boolean positiveDown = false;
	private boolean centroid_reference = false;
	private float pd = 1f;
	private Array bnd;
	private Index bndInd;
	private IndexLookup_Nearest lats, lons;
	private double cellsize;
	private double minx;
	private double miny;
	private double maxx;
	private double maxy;
	private int nrows;
	private int ncols;
	private Intersector_3D_Poly i3d = new Intersector_3D_Poly();

	/**
	 * Constructor accepting a path name provided as a String.
	 * 
	 * @param fileName
	 *            - path name of the raster resource as a String
	 */

	public Boundary_Raster_NetCDF(String fileName) {

		try {
			boundary = NetcdfFile.open(fileName);
			boundaryVar = boundary.findVariable(boundaryName);
			bnd = boundaryVar.read();
			bndInd = bnd.getIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor accepting a path name provided as a String, and also
	 * accepting Strings defining the variable names for the latitude and
	 * longitude dimensions.
	 * 
	 * @param fileName
	 */

	public Boundary_Raster_NetCDF(String fileName, String latName,
			String lonName) throws IOException {

		boundary = NetcdfFile.open(fileName);
		boundaryVar = boundary.findVariable(boundaryName);
		bnd = boundaryVar.read();
		bndInd = bnd.getIndex();
		this.latName = latName;
		this.lonName = lonName;
		initialize();
	}

	/**
	 * Returns a clone of the class instance.
	 */

	@Override
	public Boundary_Raster_NetCDF clone() {
		Boundary_Raster_NetCDF ncb;
		ncb = new Boundary_Raster_NetCDF(boundary.getLocation());
		// TODO FIX THIS UP!!!!
		ncb.neglon = neglon;
		return ncb;
	}

	/**
	 * Retrieves the aspect of the cell at the given Coordinate
	 * 
	 * @param c
	 */

	public double getAspect(Coordinate c) {
		return getAspect(c.x, c.y);
	}

	/**
	 * Retrieves the aspect of the cell at the given x,y pair. Formula obtained
	 * from ArcGIS.
	 * 
	 * @param x - position in the x direction
	 * @param y - position in the y direction
	 */

	public double getAspect(double x, double y) {
		Coordinate[] c = getVertices(getIndices(x, y));
		double dzdx = Math.abs(c[0].z - c[1].z) / Math.abs(c[0].x - c[1].x);
		double dzdy = Math.abs(c[1].z - c[2].z) / Math.abs(c[1].y - c[2].y);
		double aspect = 180 / Math.PI * Math.atan2(dzdy, -dzdx);
		if (aspect < 0)
			return 90.0 - aspect;
		else if (aspect > 90.0)
			return 360.0 - aspect + 90.0;
		else
			return 90.0 - aspect;
	}

	/**
	 * Retrieves the slope of the cell at the given Coordinate, projecting into
	 * Cylindrical Equidistant projection. Formula obtained from ArcGIS.
	 * 
	 * @param c
	 */

	public double getAspectC(Coordinate c) {
		return getAspectC(c.x, c.y);
	}

	/**
	 * Retrieves the slope of the cell at the given Coordinate, projecting into
	 * Cylindrical Equidistant projection. Formula obtained from ArcGIS.
	 * 
	 * @param x - position in the x direction
	 * @param y - position in the y direction
	 */

	public double getAspectC(double x, double y) {
		Coordinate[] c = getCeqdVertices(getIndices(x, y));
		double dzdx = Math.abs(c[0].z - c[1].z) / Math.abs(c[0].x - c[1].x);
		double dzdy = Math.abs(c[1].z - c[2].z) / Math.abs(c[1].y - c[2].y);
		double aspect = 180 / Math.PI * Math.atan2(dzdy, -dzdx);
		if (aspect < 0)
			return 90.0 - aspect;
		else if (aspect > 90.0)
			return 360.0 - aspect + 90.0;
		else
			return 90.0 - aspect;
	}

	/**
	 * Retrieves the depth of the cell at the provided coordinates
	 */

	@Override
	public double getBoundaryDepth(double x, double y) {

		if (!inBounds(x, y)) {
			return Double.NaN;
		}
		int[] indices = getIndices(x, y);
		bndInd.set(indices[0], indices[1]);
		return bnd.getDouble(bndInd) * pd; // Corrects if bathymetry is in
											// positive units.
	}

	/**
	 * Retrieves the depth of the cell at the provided coordinates
	 */

	public double getBoundaryDepth(int[] indices) {
		bndInd.set(indices[0], indices[1]);
		return bnd.getDouble(bndInd) * pd; // Corrects if bathymetry is in
											// positive units.
	}

	/**
	 * Returns the cell size (assumed to be square) of the grid
	 */

	@Override
	public double getCellSize() {
		return cellsize;
	}

	/**
	 * Retrieves the vertices of the corners of the cell intersecting the given
	 * Coordinate
	 */

	public Coordinate[] getCeqdVertices(Coordinate c) {
		return getCeqdVertices(getIndices(c.x, c.y));
	}

	/**
	 * Retrieves the vertices of the corners of the cell corresponding to the
	 * provided indices (lower left corner start)
	 */

	public Coordinate[] getCeqdVertices(int[] indices) {
		int i = indices[0];
		int j = indices[1];

		if (i < 1 || i > nrows - 2 || j < 1 || j > ncols - 2) {
			return null;
		}

		float z = bnd.getFloat(bndInd.set(i, j)) * pd;

		float llz = bnd.getFloat(bndInd.set(i - 1, j - 1)) * pd;
		float lrz = bnd.getFloat(bndInd.set(i - 1, j + 1)) * pd;
		float urz = bnd.getFloat(bndInd.set(i + 1, j + 1)) * pd;
		float ulz = bnd.getFloat(bndInd.set(i + 1, j - 1)) * pd;
		float top = bnd.getFloat(bndInd.set(i + 1, j)) * pd;
		float right = bnd.getFloat(bndInd.set(i, j + 1)) * pd;
		float bottom = bnd.getFloat(bndInd.set(i - 1, j)) * pd;
		float left = bnd.getFloat(bndInd.set(i, j - 1)) * pd;

		double x1 = j * cellsize + minx;
		double x2 = (j + 1) * cellsize + minx;
		double y1 = i * cellsize + miny;
		double y2 = (i + 1) * cellsize + miny;

		Coordinate ll = CoordinateMath.lonlat2ceqd(new Coordinate(x1, y1, (z
				+ left + bottom + llz) / 4f));
		Coordinate lr = CoordinateMath.lonlat2ceqd(new Coordinate(x2, y1, (z
				+ right + bottom + lrz) / 4f));
		Coordinate ur = CoordinateMath.lonlat2ceqd(new Coordinate(x2, y2, (z
				+ right + top + urz) / 4f));
		Coordinate ul = CoordinateMath.lonlat2ceqd(new Coordinate(x1, y2, (z
				+ left + top + ulz) / 4f));

		return new Coordinate[] { ll, lr, ur, ul };
	}

	/**
	 * Retrieves the indices of the grid (lower left start) associated with the
	 * provided Coordinate
	 */

	@Override
	public int[] getIndices(Coordinate c) {
		return getIndices(c.x, c.y);
	}

	/**
	 * Retrieves the indices of the grid (lower left start) associated with the
	 * provided x,y coordinate values. Values on the line are associated with
	 * forward cell.
	 */

	@Override
	public int[] getIndices(double x, double y) {
		if (neglon) {
			x = (x + 180) % 360 - 180;
		}

		if (x < minx || x > maxx || y < miny || y > maxy) {
			return null;
		}

		return new int[] { (int) Math.floor((y - miny) / cellsize),
				(int) Math.floor((x - minx) / cellsize) };
	}

	/**
	 * Retrieves the name of the Latitude variable used by this class
	 */

	public String getLatName() {
		return latName;
	}

	/**
	 * Retrieves an array of values corresponding to the latitude values
	 * associated with each row of the grid.
	 */

	public double[] getLats() {
		return lats.getJavaArray();
	}

	/**
	 * Retrieves the name of the Longitude variable used by this class
	 */

	public String getLonName() {
		return lonName;
	}

	/**
	 * Retrieves an array of values corresponding to the longitude values
	 * associated with each column of the grid.
	 */

	public double[] getLons() {
		return lons.getJavaArray();
	}

	/**
	 * Retrieves the maximum x value of the raster. This value corresponds to
	 * the right edge position of the right-most cell.
	 */

	public double getMaxx() {
		return maxx;
	}

	/**
	 * Retrieves the maximum y value of the raster. This value corresponds to
	 * the upper edge position of the upper-most cell.
	 */

	public double getMaxy() {
		return maxy;
	}

	/**
	 * Retrieves the minimum x value of the raster. This value corresponds to
	 * the left edge position of the left-most cell.
	 */

	@Override
	public double getMinx() {
		return minx;
	}

	/**
	 * Retrieves the minimum y value of the raster. This value corresponds to
	 * the bottom edge position of the bottom-most cell.
	 */

	@Override
	public double getMiny() {
		return miny;
	}

	/**
	 * Retrieves the number of columns in the raster
	 */

	public int getNcols() {
		return ncols;
	}

	/**
	 * Retrieves the number of rows in the raster
	 */

	public int getNrows() {
		return nrows;
	}

	/**
	 * Indicates whether the grid is oriented such that positive values are
	 * increasing in a downward direction (e.g. depth values are positive)
	 */

	public boolean getPositiveDown() {
		return positiveDown;
	}

	/**
	 * Returns the depth of the cell at the provided coordinates, taking into
	 * account the slope of the cell.
	 */

	@Override
	public double getPreciseBoundaryDepth(double x, double y) {

		if (!inBounds(x, y)) {
			return Double.NaN;
		}
		int[] indices = getIndices(x, y);
		Coordinate[] verts = getVertices(indices);

		double lx = verts[3].x;
		double rx = verts[2].x;

		double tlz = verts[3].z;
		double trz = verts[2].z;
		double brz = verts[1].z;
		double blz = verts[0].z;

		double ty = verts[3].y;
		double by = verts[0].y;

		double i0 = ((rx - x) / (rx - lx) * tlz)
				+ (((x - lx) / (rx - lx) * trz));
		double i1 = ((rx - x) / (rx - lx) * blz)
				+ (((x - lx) / (rx - lx) * brz));
		return (((by - y) / (by - ty) * i0) + (((y - ty) / (by - ty) * i1)))
				* pd;
	}

	/**
	 * Possible duplicate function to PreciseBoundaryDepth...
	 */

	@Override
	public double getRealDepth(double x, double y) {
		LineSegment ls = new LineSegment(new Coordinate(x, y, 1E6),
				new Coordinate(x, y, -1E6));
		return (i3d.intersection(ls, getVertices(getIndices(x, y)))).z;
	}

	/**
	 * Retrieves the slope of the cell at the given Coordinate
	 * 
	 * @param c - the Coordinate
	 */

	public double getSlope(Coordinate c) {
		return getSlope(c.x, c.y);
	}

	/**
	 * Retrieves the slope of the cell at the given x,y pair Formula obtained
	 * from ArcGIS.
	 * 
	 * @param x - the x position
	 * @param y - the y position
	 */

	public double getSlope(double x, double y) {
		Coordinate[] c = getVertices(getIndices(x, y));
		double dzdx = Math.abs(c[0].z - c[1].z) / Math.abs(c[0].x - c[1].x);
		double dzdy = Math.abs(c[1].z - c[2].z) / Math.abs(c[1].y - c[2].y);
		return (Math.atan(Math.sqrt((dzdx * dzdx) + (dzdy * dzdy))))
				* (180 / Math.PI);
	}

	/**
	 * Retrieves the slope of the cell at the given Coordinate, projecting into
	 * Cylindrical Equidistant projection.
	 * 
	 * @param c - the Coordinate
	 */

	public double getSlopeC(Coordinate c) {
		return getSlopeC(c.x, c.y);
	}

	/**
	 * Retrieves the slope of the cell at the given x,y pair, projecting into
	 * Cylindrical Equidistant projection.
	 * 
	 * @param x - the x position
	 * @param y - the y position
	 */

	// Formula from ArcGIS
	public double getSlopeC(double x, double y) {
		Coordinate[] c = getCeqdVertices(getIndices(x, y));
		double dzdx = Math.abs(c[0].z - c[1].z) / Math.abs(c[0].x - c[1].x);
		double dzdy = Math.abs(c[1].z - c[2].z) / Math.abs(c[1].y - c[2].y);
		return (Math.atan(Math.sqrt((dzdx * dzdx) + (dzdy * dzdy))))
				* (180 / Math.PI);
	}

	/**
	 * Retrieves the name of the variable being used to look up values.
	 */

	public String getVariableName() {
		return boundaryName;
	}

	/**
	 * Retrieves the vertices of the corners of the cell at the current
	 * position.
	 */

	@Override
	public Coordinate[] getVertices(Coordinate c) {
		return getVertices(getIndices(c.x, c.y));
	}

	/**
	 * Retrieves the vertices of the corners of the cell at the index pair.
	 */

	@Override
	public Coordinate[] getVertices(int[] indices) {
		int i = indices[0];
		int j = indices[1];

		if (i < 1 || i > nrows - 2 || j < 1 || j > ncols - 2) {
			return null;
		}

		float z = bnd.getFloat(bndInd.set(i, j)) * pd;

		float llz = bnd.getFloat(bndInd.set(i - 1, j - 1)) * pd;
		float lrz = bnd.getFloat(bndInd.set(i - 1, j + 1)) * pd;
		float urz = bnd.getFloat(bndInd.set(i + 1, j + 1)) * pd;
		float ulz = bnd.getFloat(bndInd.set(i + 1, j - 1)) * pd;
		float top = bnd.getFloat(bndInd.set(i + 1, j)) * pd;
		float right = bnd.getFloat(bndInd.set(i, j + 1)) * pd;
		float bottom = bnd.getFloat(bndInd.set(i - 1, j)) * pd;
		float left = bnd.getFloat(bndInd.set(i, j - 1)) * pd;

		double x1 = j * cellsize + minx;
		double x2 = (j + 1) * cellsize + minx;
		double y1 = i * cellsize + miny;
		double y2 = (i + 1) * cellsize + miny;

		Coordinate ll = new Coordinate(x1, y1, (z + left + bottom + llz) / 4f);
		Coordinate lr = new Coordinate(x2, y1, (z + right + bottom + lrz) / 4f);
		Coordinate ur = new Coordinate(x2, y2, (z + right + top + urz) / 4f);
		Coordinate ul = new Coordinate(x1, y2, (z + left + top + ulz) / 4f);

		return new Coordinate[] { ll, lr, ur, ul };
	}

	/**
	 * Retrieves a List of Coordinate arrays corresponding to the provided List
	 * of index pairs.
	 */

	@Override
	public List<Coordinate[]> getVertices(List<int[]> indices) {
		List<Coordinate[]> list = new ArrayList<Coordinate[]>(indices.size());
		Iterator<int[]> it = indices.iterator();
		while (it.hasNext()) {
			list.add(getVertices(it.next()));
		}
		return list;
	}

	/**
	 * Identifies whether a given x,y pair falls within the full bounds of the
	 * raster data set.
	 * 
	 * @param x
	 *            - x position
	 * @param y
	 *            - y position
	 */

	public boolean inBounds(double x, double y) {
		if (x < minx) {
			return false;
		}
		if (x > maxx) {
			return false;
		}
		if (y < miny) {
			return false;
		}
		if (y > maxy) {
			return false;
		}

		return true;
	}

	/**
	 * Identifies whether a given x,y pair falls inside of a one-cell wide
	 * margin of the raster data set (i.e. not outside or on the edge).
	 * 
	 * @param x
	 * @param y
	 */

	public boolean inCollarBounds(double x, double y) {
		if (x < minx + cellsize) {
			return false;
		}
		if (x > maxx - cellsize) {
			return false;
		}
		if (y < miny + cellsize) {
			return false;
		}
		if (y > maxy - cellsize) {
			return false;
		}

		return true;
	}

	/**
	 * Initializes the class by generating the latitude and longitude arrays,
	 * determining the minimum and maximum extents and calculating the cell size
	 * of the raster.
	 * 
	 * @throws IOException
	 */

	public void initialize() throws IOException {
		lats = new IndexLookup_Nearest(boundary.findVariable(latName));
		lons = new IndexLookup_Nearest(boundary.findVariable(lonName));
		nrows = lats.arraySize();
		ncols = lons.arraySize();
		cellsize = (lats.getMaxVal() - lats.getMinVal()) / (nrows - 1);
		if (centroid_reference) {
			minx = lons.getMinVal() - (cellsize / 2);
			miny = lats.getMinVal() - (cellsize / 2);
			maxx = lons.getMaxVal() + (cellsize / 2);
			maxy = lats.getMaxVal() + (cellsize / 2);
		} else {
			minx = lons.getMinVal();
			miny = lats.getMinVal();
			maxx = lons.getMaxVal() + cellsize;
			maxy = lats.getMaxVal() + cellsize;
		}
	}

	/**
	 * Retrieves whether the longitude values used by the raster are intended to
	 * include negative values.
	 */

	public boolean isNeglon() {
		return neglon;
	}

	/**
	 * Sets the cell size of the raster.
	 * 
	 * @param cellsize
	 */

	public void setCellsize(float cellsize) {
		this.cellsize = cellsize;
	}

	/**
	 * Sets the name of the variable used to provide latitude values.
	 * 
	 * @param latName
	 */

	public void setLatName(String latName) {
		this.latName = latName;
	}

	/**
	 * Sets the name of the variable used to provide longitude values.
	 * 
	 * @param lonName
	 */

	public void setLonName(String lonName) {
		this.lonName = lonName;
	}

	/**
	 * Sets whether the class uses negative longitude values.
	 * 
	 * @param neglon
	 */

	public void setNeglon(boolean neglon) {
		this.neglon = neglon;
	}

	/**
	 * Sets whether depth values are increasingly positive in a downwards
	 * direction (i.e. depth values are positive).
	 * 
	 * @param positiveDown
	 */

	@Override
	public void setPositiveDown(boolean positiveDown) {
		this.positiveDown = positiveDown;
		pd = positiveDown ? -1f : 1f;
	}

	/**
	 * Sets whether the coordinate system is referenced to the center of the
	 * centroid or the lower left edge.
	 * 
	 * @param centroid_reference
	 */

	public void setReferenceByCentroid(boolean centroid_reference) {
		this.centroid_reference = centroid_reference;
	}
	
	/**
	 * Sets the name of the variable being used as the data content source.
	 * 
	 * @param variableName
	 */

	public void setVariableName(String variableName) {
		this.boundaryName = variableName;
	}

	/**
	 * Retrieves whether the coordinate system is referenced to the center of the
	 * centroid or the lower left edge.
	 */
	
	public boolean usesCentroidReference() {
		return centroid_reference;
	}
}
