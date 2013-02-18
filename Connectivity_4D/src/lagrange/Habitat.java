package lagrange;

import java.io.IOException;

/**
 * A generic interface for spatial layers representing habitat features.
 * 
 * @author Johnathan Kool
 *
 */

public interface Habitat {
	
	/**
	 * Sets the location of the data source using a String representing
	 * the path of the source file(s).
	 * 
	 * @param polyFileName
	 * @throws IOException
	 */
	
	public void setDataSource(String polyFileName) throws IOException;
	
	
	
	public void setLookupField(String polyKey);
	public void setNegLon(boolean negPolyCoord);
}