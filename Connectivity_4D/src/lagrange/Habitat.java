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
	 * Sets the location of the data source using a String representing the path
	 * of the source file(s).
	 * 
	 * @param polyFileName
	 * @throws IOException
	 */

	public void setDataSource(String polyFileName) throws IOException;

	/**
	 * Sets the path String of the resource containing the polygon information
	 * 
	 * @param polyKey
	 */

	public void setLookupField(String polyKey);

	/**
	 * Sets whether polygon coordinates have negative values. Used to ensure
	 * that model output is back-transformed into the same coordinate system as
	 * the settlement polygons if necessary.
	 * 
	 * @param negPolyCoord
	 */

	public void setNegLon(boolean negPolyCoord);
}