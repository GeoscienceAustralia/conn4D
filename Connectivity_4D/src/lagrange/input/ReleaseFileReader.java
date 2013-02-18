package lagrange.input;

import lagrange.Parameters;
import com.vividsolutions.jts.geom.Geometry;

public interface ReleaseFileReader {
	
	/**
	 * Retrieves the depth value from a single line/source in the ReleaseFile
	 * 
	 * @return float - depth value
	 */
	
	public float getDepth();

	/**
	 * Retrieves the name of a source from a single line in the release file 
	 * 
	 * @return String - the name of the source of the release objects.
	 */
	
	public String getLocName();
	
	/**
	 * Retrieves the number of Particles to be released from a single source.
	 * 
	 * @return
	 */

	public long getNpart();

	/**
	 * Retrieves the Geometry of the location from which Particles will be released.
	 * 
	 * @return Geometry - feature representing a release location
	 */
	
	public Geometry getPosition();
	
	/**
	 * Retrieves the ID of the source location.
	 * 
	 * @return long - ID of the current source location
	 */

	//public long getSourceID();

	/**
	 * Identifies whether there is another line following the current position in the file.
	 * 
	 * @return boolean - indicating whether there is another line beyond the current position
	 */
	
	public boolean hasNext();
	
	/**
	 * Increments to the next line in the release file
	 */

	public void next();
	
	/**
	 * Sets the Parameter
	 * 
	 * @param d
	 * @return
	 */

	public Parameters setParameters(Parameters prm);
}
