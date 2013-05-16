package au.gov.ga.conn4d.input;

/**
 * Local parameters used in the model.  Local in the sense that if the program is distributed,
 * these are resources that are found on the local machine.  They are not passed through the
 * network.  Examples include file and pathnames for velocity and GIS files.
 * 
 * @author Johnathan Kool
 */

public class ConfigurationOverride extends ParameterReader {

	public String uname = "uvel"; // Name of the u parameter in the velocity files (e.g. "UVEL")
	public String vname = "vvel"; // Name of the v parameter in the velocity files (e.g. "VVEL")
	public String wname = "wvel"; // Name of the w parameter in the velocity files (e.g. "VVEL")
	public String ufile = "uvel.nc"; // Path and name of the NetCDF file containing the uvel information
	public String vfile = "vvel.nc"; // Path and name of the NetCDF file containing the vvel information
	public String wfile = "wvel.nc"; // Path and name of the NetCDF file containing the wvel information
	public String veldir = ".\\";
	public String latName = "Latitude"; // Name of the latitude parameter in the velocity files
	public String lonName = "Longitude"; // Name of the longitude parameter in the velocity files
	public String kName = "Depth"; // Name of the Depth parameter in the velocity files
	public String tName = "Time"; // Name of the Time parameter in the velocity files
	public boolean negOceanCoord = false;// Does the oceanographic model have negative coordinate values?
	public String polyFileName = "reefpolys.shp";// Name of the settlement polygon file
	public String polyKey = "ID"; // Index field of the settlement polygon file
	public boolean negCoord = false; // Are negative coordinate values being used?
	public String landFileName = "landmask.shp"; // Name of the land mask file
	public String bathymetryFileName = "bathymetry.asc";
	public String trajOutputDir = ".\\";		// Output directory for trajectory files
	public String vertFile = "vertical.vrt";	// Path and name of the vertical migration matrix.
	public String velocityType = "IANN";		// Velocity type (IANN- Interannual or CLIM - climatological)
	public long timeOffset = -2177521200000l;   // Offset between velocity time (HYCOM-1900) and Java's base time (1970)
	public String timeOffsetUnits = "milliseconds";
	
	/**
	 * No argument constructor.
	 */
	
	public ConfigurationOverride(){}
	
	/**
	 * Constructor that accepts a file name.
	 * 
	 * @param str - name of the file to be read
	 */
	
	public ConfigurationOverride(String str) {
		readFile(str);
	}	
}