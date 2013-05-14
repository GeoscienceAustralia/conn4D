package lagrange.impl.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lagrange.Boundary_Grid;
import lagrange.impl.collision.Intersector_3D_Poly;
import lagrange.utils.CoordinateMath;
import lagrange.utils.IndexLookup_Nearest;
import ucar.ma2.Array;
import ucar.ma2.Index;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

public class Boundary_Grid_NetCDF implements Boundary_Grid, Cloneable {

	private NetcdfFile boundary;
	private Variable boundaryVar;
	private String boundaryName = "bathymetry";
	private String latName = "Latitude";
	private String lonName = "Longitude";
	private boolean neglon = true;
	private boolean positiveDown = false;
	private boolean centroid_reference = false;
	private float pd=1f;
	private Array bnd;
	private Index bndInd;
	private IndexLookup_Nearest lats, lons;
	private double cellsize;
	private double minx;
	private double miny;
	private double maxx;
	private double maxy;
	private int nrows;
	private Intersector_3D_Poly i3d = new Intersector_3D_Poly();

	private int ncols;

	public Boundary_Grid_NetCDF(String filename) {

		try {
			boundary = NetcdfFile.open(filename);
			boundaryVar = boundary.findVariable(boundaryName);
			bnd = boundaryVar.read();
			bndInd = bnd.getIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Boundary_Grid_NetCDF(String bathname, String latName,
			String lonName) throws IOException {

		boundary = NetcdfFile.open(bathname);
		boundaryVar = boundary.findVariable(boundaryName);
		bnd = boundaryVar.read();
		bndInd = bnd.getIndex();
		this.latName = latName;
		this.lonName = lonName;
		initialize();
	}

	@Override
	public Boundary_Grid_NetCDF clone() {
		Boundary_Grid_NetCDF ncb;
		ncb = new Boundary_Grid_NetCDF(boundary.getLocation());
		// TODO FIX THIS UP!!!!
		ncb.neglon = neglon;
		return ncb;
	}

	@Override
	public double getCellSize() {
		return cellsize;
	}
	
	@Override
	public double getBoundaryDepth(double x, double y) {

		if (!inBounds(x, y)) {
			return Double.NaN;
		}
		int[] indices = getIndices(x, y);
		bndInd.set(indices[0], indices[1]);
		return bnd.getDouble(bndInd)*pd; // Corrects if bathymetry is in positive units.
	}
	
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
		
		double i0 = ((rx-x)/(rx-lx)*tlz) + (((x-lx)/(rx-lx)*trz));
		double i1 = ((rx-x)/(rx-lx)*blz) + (((x-lx)/(rx-lx)*brz));
		return (((by-y)/(by-ty)*i0) + (((y-ty)/(by-ty)*i1)))*pd;		
	}
	
	public double getBoundaryDepth(int[] indices) {
		bndInd.set(indices[0], indices[1]);
		return bnd.getDouble(bndInd)*pd; // Corrects if bathymetry is in positive units.
	}
	
	@Override
	public double getRealDepth(double x, double y){
		LineSegment ls = new LineSegment(new Coordinate(x,y,1E6),new Coordinate(x,y,-1E6));
		return (i3d.intersection(ls, getVertices(getIndices(x,y)))).z;
	}
	
	@Override
	public int[] getIndices(double x, double y){
		if (neglon) {
			x = (x + 180) % 360 - 180;
		}
		
		if(x<minx || x > maxx || y<miny || y > maxy){
			return null;
		}
		
		return new int[] {(int) Math.floor((y-miny)/cellsize),(int) Math.floor((x-minx)/cellsize)};
	}

	@Override
	public int[] getIndices(Coordinate c){
		return getIndices(c.x,c.y);
	}
	
	public String getLatName() {
		return latName;
	}

	public double[] getLats() {
		return lats.getJavaArray();
	}

	public String getLonName() {
		return lonName;
	}

	public double[] getLons() {
		return lons.getJavaArray();
	}

	public boolean getPositiveDown(){
		return positiveDown;
	}
	
	public double getSlope(Coordinate p){
		return getSlope(p.x,p.y);
	}
	
	//Formula from ArcGIS
	public double getSlope(double x, double y){
		Coordinate[] c = getVertices(getIndices(x,y));
		double dzdx = Math.abs(c[0].z-c[1].z)/Math.abs(c[0].x-c[1].x);
		double dzdy = Math.abs(c[1].z-c[2].z)/Math.abs(c[1].y-c[2].y);
		return (Math.atan(Math.sqrt((dzdx*dzdx)+(dzdy*dzdy))))*(180/Math.PI);
	}
	
	public double getAspect(Coordinate p){
		return getAspect(p.x,p.y);
	}
	
	// Formula from ArcGIS
	public double getAspect(double x, double y){
		Coordinate[] c = getVertices(getIndices(x,y));
		double dzdx = Math.abs(c[0].z-c[1].z)/Math.abs(c[0].x-c[1].x);
		double dzdy = Math.abs(c[1].z-c[2].z)/Math.abs(c[1].y-c[2].y);
		double aspect = 180/Math.PI*Math.atan2(dzdy, -dzdx);
		  if (aspect < 0)
		    return 90.0 - aspect;
		  else if (aspect > 90.0)
		    return 360.0 - aspect + 90.0;
		  else
		    return 90.0 - aspect;
	}
	
	public double getSlopeC(Coordinate p){
		return getSlopeC(p.x,p.y);
	}
	
	//Formula from ArcGIS
	public double getSlopeC(double x, double y){
		Coordinate[] c = getCeqdVertices(getIndices(x,y));
		double dzdx = Math.abs(c[0].z-c[1].z)/Math.abs(c[0].x-c[1].x);
		double dzdy = Math.abs(c[1].z-c[2].z)/Math.abs(c[1].y-c[2].y);
		return (Math.atan(Math.sqrt((dzdx*dzdx)+(dzdy*dzdy))))*(180/Math.PI);
	}
	
	public double getAspectC(Coordinate p){
		return getAspectC(p.x,p.y);
	}
	
	// Formula from ArcGIS
	public double getAspectC(double x, double y){
		Coordinate[] c = getCeqdVertices(getIndices(x,y));
		double dzdx = Math.abs(c[0].z-c[1].z)/Math.abs(c[0].x-c[1].x);
		double dzdy = Math.abs(c[1].z-c[2].z)/Math.abs(c[1].y-c[2].y);
		double aspect = 180/Math.PI*Math.atan2(dzdy, -dzdx);
		  if (aspect < 0)
		    return 90.0 - aspect;
		  else if (aspect > 90.0)
		    return 360.0 - aspect + 90.0;
		  else
		    return 90.0 - aspect;
	}
	
	public String getVariableName() {
		return boundaryName;
	}

	@Override
	public Coordinate[] getVertices(Coordinate c) {
		return getVertices(getIndices(c.x,c.y));
	}
	
	public Coordinate[] getCeqdVertices(Coordinate c) {
		return getCeqdVertices(getIndices(c.x,c.y));
	}

	@Override
	public Coordinate[] getVertices(int[] indices){
		int i = indices[0];
		int j = indices[1];
		
		if (i<1||i>nrows-2||j<1||j>ncols-2){return null;}
		
		float z = bnd.getFloat(bndInd.set(i, j))*pd;

		float llz = bnd.getFloat(bndInd.set(i - 1, j - 1))*pd;
		float lrz = bnd.getFloat(bndInd.set(i - 1, j + 1))*pd;
		float urz = bnd.getFloat(bndInd.set(i + 1, j + 1))*pd;
		float ulz = bnd.getFloat(bndInd.set(i + 1, j - 1))*pd;
		float top = bnd.getFloat(bndInd.set(i + 1, j))*pd;
		float right = bnd.getFloat(bndInd.set(i, j + 1))*pd;
		float bottom = bnd.getFloat(bndInd.set(i - 1, j))*pd;
		float left = bnd.getFloat(bndInd.set(i, j - 1))*pd;
		
		double x1 = j*cellsize+minx;
		double x2 = (j+1)*cellsize+minx;
		double y1 = i*cellsize+miny;
		double y2 = (i+1)*cellsize+miny;
		
		Coordinate ll = new Coordinate(x1, y1,
				(z + left + bottom + llz) / 4f);
		Coordinate lr = new Coordinate(x2, y1,
				(z + right + bottom + lrz) / 4f);
		Coordinate ur = new Coordinate(x2, y2,
				(z + right + top + urz) / 4f);
		Coordinate ul = new Coordinate(x1, y2,
				(z + left + top + ulz) / 4f);
			
		return new Coordinate[] { ll, lr, ur, ul };
	}
	
	public Coordinate[] getCeqdVertices(int[] indices){
		int i = indices[0];
		int j = indices[1];
		
		if (i<1||i>nrows-2||j<1||j>ncols-2){return null;}
		
		float z = bnd.getFloat(bndInd.set(i, j))*pd;

		float llz = bnd.getFloat(bndInd.set(i - 1, j - 1))*pd;
		float lrz = bnd.getFloat(bndInd.set(i - 1, j + 1))*pd;
		float urz = bnd.getFloat(bndInd.set(i + 1, j + 1))*pd;
		float ulz = bnd.getFloat(bndInd.set(i + 1, j - 1))*pd;
		float top = bnd.getFloat(bndInd.set(i + 1, j))*pd;
		float right = bnd.getFloat(bndInd.set(i, j + 1))*pd;
		float bottom = bnd.getFloat(bndInd.set(i - 1, j))*pd;
		float left = bnd.getFloat(bndInd.set(i, j - 1))*pd;
		
		double x1 = j*cellsize+minx;
		double x2 = (j+1)*cellsize+minx;
		double y1 = i*cellsize+miny;
		double y2 = (i+1)*cellsize+miny;
		
		
		Coordinate ll = CoordinateMath.lonlat2ceqd(new Coordinate(x1, y1,
				(z + left + bottom + llz) / 4f));
		Coordinate lr = CoordinateMath.lonlat2ceqd(new Coordinate(x2, y1,
				(z + right + bottom + lrz) / 4f));
		Coordinate ur = CoordinateMath.lonlat2ceqd(new Coordinate(x2, y2,
				(z + right + top + urz) / 4f));
		Coordinate ul = CoordinateMath.lonlat2ceqd(new Coordinate(x1, y2,
				(z + left + top + ulz) / 4f));
			
		return new Coordinate[] { ll, lr, ur, ul };
	}
	
	@Override
	public List<Coordinate[]> getVertices(List<int[]> indices){
		List<Coordinate[]> list = new ArrayList<Coordinate[]>(indices.size());
		Iterator<int[]> it = indices.iterator();
		while(it.hasNext()){
			list.add(getVertices(it.next()));
		}
		return list;
	}


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

	public boolean inCollarBounds(double x, double y) {
		if (x < minx+cellsize) {
			return false;
		}
		if (x > maxx-cellsize) {
			return false;
		}
		if (y < miny+cellsize) {
			return false;
		}
		if (y > maxy-cellsize) {
			return false;
		}
		
		return true;
	}

	public void initialize() throws IOException {
		lats = new IndexLookup_Nearest(boundary.findVariable(latName));
		lons = new IndexLookup_Nearest(boundary.findVariable(lonName));
		nrows = lats.arraySize();
		ncols = lons.arraySize();
		cellsize = (lats.getMaxVal()-lats.getMinVal())/(nrows-1);
		if(centroid_reference){
			minx = lons.getMinVal()-(cellsize/2);
			miny = lats.getMinVal()-(cellsize/2);
			maxx = lons.getMaxVal()+(cellsize/2);
			maxy = lats.getMaxVal()+(cellsize/2);
		}
		else{
			minx = lons.getMinVal();
			miny = lats.getMinVal();
			maxx = lons.getMaxVal()+cellsize;
			maxy = lats.getMaxVal()+cellsize;
		}
	}
	
	public boolean isNeglon() {
		return neglon;
	}
	
	public void setLatName(String latName) {
		this.latName = latName;
	}

	public void setLonName(String lonName) {
		this.lonName = lonName;
	}
	
	public void setNeglon(boolean neglon) {
		this.neglon = neglon;
	}
	
	public void setVariableName(String variableName) {
		this.boundaryName = variableName;
	}

	public double getCellsize() {
		return cellsize;
	}

	public void setCellsize(float cellsize) {
		this.cellsize = cellsize;
	}

	@Override
	public double getMinx() {
		return minx;
	}

	public void setMinx(double minx) {
		this.minx = minx;
	}

	@Override
	public double getMiny() {
		return miny;
	}

	public void setMiny(double miny) {
		this.miny = miny;
	}

	public double getMaxx() {
		return maxx;
	}

	public void setMaxx(double maxx) {
		this.maxx = maxx;
	}

	public double getMaxy() {
		return maxy;
	}

	public void setMaxy(double maxy) {
		this.maxy = maxy;
	}

	public int getNrows() {
		return nrows;
	}

	public void setNrows(int nrows) {
		this.nrows = nrows;
	}

	public int getNcols() {
		return ncols;
	}

	public void setNcols(int ncols) {
		this.ncols = ncols;
	}
	
	@Override
	public void setPositiveDown(boolean positiveDown){
		this.positiveDown = positiveDown;
		pd=positiveDown?-1f:1f;
	}

	public boolean usesCentroidReference() {
		return centroid_reference;
	}

	public void setReferenceByCentroid(boolean centroid_reference) {
		this.centroid_reference = centroid_reference;
	}
}
