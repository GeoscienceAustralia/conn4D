package au.gov.ga.conn4d;

/**
 * Reads boundary (e.g. bathymetry) values from a data source
 * 
 * @author Johnathan Kool
 * 
 */

public interface Boundary {

	/**
	 * Given x and y locations return a depth at that location
	 * 
	 * @param x - position in the x direction
	 * @param y - position in the y direction
	 */

	public double getBoundaryDepth(double x, double y);

	/**
	 * Clones the BathymetryReader object
	 */

	public Boundary clone();

	/**
	 * Given x and y locations, return the depth at that location taking into
	 * account intracell change.
	 * 
	 * @param x - position in the x direction
	 * @param y - position in the y direction
	 * @return - the boundary depth at the coordinate location taking into
	 *         account intracell depth change.
	 */

	public double getPreciseBoundaryDepth(double x, double y);

	/**
	 * Sets whether depth values are increasingly positive in a downwards
	 * direction (i.e. depth values are positive).
	 */

	public void setPositiveDown(boolean positiveDown);
}
