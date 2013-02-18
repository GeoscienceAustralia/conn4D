package lagrange.impl.readers;

import java.io.IOException;

import lagrange.utils.IndexLookup_Nearest;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

public class Reader_NetCDF_3D {

	private NetcdfFile netcdfFile;
	private Variable bndVar;
	private String varName = "mld";
	private String latName = "Latitude";
	private String lonName = "Longitude";
	private String timeName = "Time";
	private boolean neglon = false;
	private IndexLookup_Nearest lats, lons, time;

	public Reader_NetCDF_3D(String filename) {

		try {
			netcdfFile = NetcdfFile.open(filename);
			bndVar = netcdfFile.findVariable(varName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Reader_NetCDF_3D(String filename, String varname, String timeName, String latName, String lonName)
			throws IOException {

		netcdfFile = NetcdfFile.open(filename);
		bndVar = netcdfFile.findVariable(varName);
		this.latName = latName;
		this.lonName = lonName;
		this.timeName = timeName;
		initialize();
	}

	public void initialize() throws IOException {
		lats = new IndexLookup_Nearest(netcdfFile.findVariable(latName));
		lons = new IndexLookup_Nearest(netcdfFile.findVariable(lonName));
		time = new IndexLookup_Nearest(netcdfFile.findVariable(timeName));
	}

	public double getValue(double t, double x, double y) {

		if (neglon) {
			x = (x + 180) % 360 - 180;
		}

		int tm = time.lookup(t);
		int i = lats.lookup(y);
		int j = lons.lookup(x);

		if (time.isIn_Bounds() !=0 || lats.isIn_Bounds() != 0 || lons.isIn_Bounds() != 0) {
			return Double.NaN;
		}
		
		Array bnd = null;
		try {
			bnd = bndVar.read(new int[]{tm,i,j},new int[]{1,1,1});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bnd.getDouble(0);
	}

	public String getVariableName() {
		return varName;
	}

	public void setVariableName(String variableName) {
		this.varName = variableName;
	}

	public String getLatName() {
		return latName;
	}

	public void setLatName(String latName) {
		this.latName = latName;
	}

	public String getLonName() {
		return lonName;
	}

	public void setLonName(String lonName) {
		this.lonName = lonName;
	}

	public boolean isNeglon() {
		return neglon;
	}

	public void setNeglon(boolean neglon) {
		this.neglon = neglon;
	}

	public Reader_NetCDF_3D clone() {
		Reader_NetCDF_3D ncb;
		ncb = new Reader_NetCDF_3D(netcdfFile.getLocation());
		// TODO FIX THIS UP!!!!
		ncb.neglon = neglon;
		return ncb;
	}
}