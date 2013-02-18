package lagrange;

/**
 * Reads depth values from a data source
 * 
 * @author Johnathan Kool
 *
 */

public interface Boundary {
	
	/**
	 * Given x and y locations return a depth at that location
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	
	public double getBoundaryDepth(double x, double y);
	
	/**
	 * Clones the BathymetryReader object
	 * 
	 * @return
	 */
	
	public Boundary clone();
}
