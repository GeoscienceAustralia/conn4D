package au.gov.ga.conn4d;

/**
 * Retrieves velocity values from a designated source
 * 
 * @author Johnathan Kool
 * 
 */

public interface VelocityReader {

	/**
	 * Generates a clone of the VelocityReader object
	 * 
	 * @return - a clone of the VelocityReader
	 */

	public abstract VelocityReader clone();

	/**
	 * Releases resources associated with the VelocityReader instance.
	 */

	public abstract void close();

	/**
	 * Retrieves a 4x2 array of minimum and maximum values for time, depth,
	 * vertical dimensions and horizontal dimensions in that order.
	 */

	public abstract double[][] getBounds();

	/**
	 * Retrieves the array object representing a NODATA value from the velocity
	 * files.
	 * 
	 * @return - array object representing NODATA
	 */

	public double[] getNODATA();

	/**
	 * Retrieves the shape/dimensions of the velocity field
	 */

	public abstract int[][] getShape();

	/**
	 * Retrieves the units of the velocity field (e.g. meters per second).
	 */

	public abstract String getUnits();

	/**
	 * Retrieves the velocity values at the given coordinates
	 * 
	 * @param time - Time
	 * @param z - Depth
	 * @param lon - Longitude
	 * @param lat - Latitude
	 * @return - Coordinate pair containing u and v velocity values
	 */

	public abstract double[] getVelocities(long time, double z, double lon,
			double lat);

	/**
	 * Identifies whether previously queried velocity values contained a NODATA
	 * value.
	 * 
	 * @return boolean indicating whether NODATA was in the vicinity of queried
	 *         velocity values
	 */

	public abstract boolean isNearNoData();

}