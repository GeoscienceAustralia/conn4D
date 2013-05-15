package lagrange.impl.readers;

import java.io.IOException;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 * Abstract class for retrieving values from an oceanographic NetCDF File
 * 
 * @author Johnathan Kool
 */

public abstract class Reader_NetCDF {

	protected NetcdfFile netcdfFile;
	protected Variable bndVar;
	protected String varName = "variable needs to be set";
	protected String latName = "Latitude";
	protected String lonName = "Longitude";
	protected String timeName = "Time";
	protected boolean neglon = false;

	protected Reader_NetCDF(){}
	
	/**
	 * Constructor accepting a String containing the path of the resource.
	 * 
	 * @param filename - the path of the resource
	 */

	public Reader_NetCDF(String fileName) {

		try {
			netcdfFile = NetcdfFile.open(fileName);
			bndVar = netcdfFile.findVariable(varName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a clone of the class instance
	 */
	
	@Override
	public abstract Reader_NetCDF clone();

	/**
	 * Retrieves the name of the variable being used to obtain latitude values
	 * 
	 * @return - the name of the variable being used to obtain latitude values
	 */
	
	public String getLatName() {
		return latName;
	}

	/**
	 * Retrieves the name of the variable being used to obtain longitude values
	 * 
	 * @return - the name of the variable being used to obtain longitude values
	 */
	
	public String getLonName() {
		return lonName;
	}

	/**
	 * Retrieves the name of the variable being used as the data content source.
	 * 
	 * @return
	 */
	
	public String getVariableName() {
		return varName;
	}

	/**
	 * Initializes the class by generating index lookups.
	 * 
	 * @throws IOException
	 */
	
	public abstract void initialize() throws IOException;

	/**
	 * Identifies whether the data source uses negative longitude values.
	 * 
	 * @return - whether the data source uses negative longitude values
	 */
	
	public boolean hasNeglon() {
		return neglon;
	}
	
	/**
	 * Sets the name of the variable being used to retrieve latitude values.
	 * 
	 * @param latName
	 */

	public void setLatName(String latName) {
		this.latName = latName;
	}

	/**
	 * Sets the name of the variable being used to retrieve longitude values.
	 * 
	 * @param lonName
	 */
	
	public void setLonName(String lonName) {
		this.lonName = lonName;
	}

	/**
	 * Sets whether the data source uses negative longitude values.
	 * 
	 * @param neglon
	 */
	
	public void setNeglon(boolean neglon) {
		this.neglon = neglon;
	}

	/**
	 * Sets the name of the variable being used as the data content source.
	 * 
	 * @param variableName
	 */
	
	public void setVariableName(String variableName) {
		this.varName = variableName;
	}
}