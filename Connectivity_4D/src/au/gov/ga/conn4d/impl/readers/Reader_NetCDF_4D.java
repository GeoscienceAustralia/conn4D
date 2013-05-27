package au.gov.ga.conn4d.impl.readers;

import java.io.IOException;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import au.gov.ga.conn4d.utils.IndexLookup_Nearest;

/**
 * Retrieves values from a 4D NetCDF File (time,z,y,x)
 * 
 * @author Johnathan Kool
 */

public class Reader_NetCDF_4D extends Reader_NetCDF {

	private String depthName = "Depth";
	private IndexLookup_Nearest lats, lons, time, depth;

	/**
	 * Constructor accepting a String containing the path of the resource.
	 * 
	 * @param fileName - the path of the resource
	 */

	public Reader_NetCDF_4D(String fileName) {
		super(fileName);
	}

	/**
	 * Constructor accepting a String containing the path of the resource, as
	 * well as Strings containing the variable names for latitude, longitude,
	 * time, and the data content.
	 * 
	 * @param fileName - the path of the resource
	 * @param varName - the name of the data content variable
	 * @param timeName - the name of the time variable
	 * @param depthName - the name of the depth variable
	 * @param latName - the name of the latitude variable
	 * @param lonName - the name of the longitude variable
	 * @throws IOException
	 */

	public Reader_NetCDF_4D(String fileName, String varName, String timeName,
			String depthName, String latName, String lonName)
			throws IOException {

		netcdfFile = NetcdfFile.open(fileName);
		bndVar = netcdfFile.findVariable(varName);
		this.latName = latName;
		this.lonName = lonName;
		this.timeName = timeName;
		this.depthName = depthName;
		initialize();
	}

	/**
	 * Returns a clone of the class instance
	 */
	
	@Override
	public Reader_NetCDF_4D clone() {
		Reader_NetCDF_4D ncb;
		ncb = new Reader_NetCDF_4D(netcdfFile.getLocation());
		// TODO TIDY THIS UP!!!!
		ncb.neglon = neglon;
		return ncb;
	}

	/**
	 * Retrieves the value from the NetCDF file corresponding to the variable
	 * name set in varName.
	 * 
	 * @param t
	 *            - time lookup value
	 * @param z
	 * 			  - z lookup value
	 * @param x
	 *            - x lookup value
	 * @param y
	 *            - y lookup value
	 */
	
	public double getValue(double t, double z, double x, double y) {

		if (neglon) {
			x = (x + 180) % 360 - 180;
		}

		int tval = time.lookup(t);
		int k = depth.lookup(z);
		int i = lats.lookup(y);
		int j = lons.lookup(x);

		if (time.isIn_Bounds() != 0 || lats.isIn_Bounds() != 0
				|| lons.isIn_Bounds() != 0) {
			return Double.NaN;
		}

		Array bnd = null;
		try {
			bnd = bndVar.read(new int[] { tval, k, i, j }, new int[] { 1, 1, 1,
					1 });
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidRangeException e) {
			e.printStackTrace();
		}
		return bnd.getDouble(0);
	}

	/**
	 * Initializes the class by generating index lookups.
	 * 
	 * @throws IOException
	 */
	
	public void initialize() throws IOException {
		lats = new IndexLookup_Nearest(netcdfFile.findVariable(latName));
		lons = new IndexLookup_Nearest(netcdfFile.findVariable(lonName));
		time = new IndexLookup_Nearest(netcdfFile.findVariable(timeName));
		depth = new IndexLookup_Nearest(netcdfFile.findVariable(depthName));
	}
}
