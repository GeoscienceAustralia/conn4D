package lagrange;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Interface extension for the Boundary class to provide access to methods
 * associated with raster-based data sources.
 * 
 * @author Johnathan Kool
 */

public interface Boundary_Raster extends Boundary {

	/**
	 * Returns the cell size (assumed to be square) of the grid
	 * 
	 * @return - the cell size of the grid
	 */

	public double getCellSize();

	/**
	 * Retrieves the indices of the grid (lower left start) associated with the
	 * provided Coordinate
	 */

	public int[] getIndices(Coordinate c);

	/**
	 * Retrieves the indices of the grid associated with the provided x,y
	 * coordinate values.
	 */

	public int[] getIndices(double x, double y);

	/**
	 * Retrieves the minimum x value of the raster. This value corresponds to the
	 * left edge position of the left-most cell.
	 */
	
	public double getMinx();

	/**
	 * Retrieves the minimum y value of the raster. This value corresponds to the
	 * left edge position of the left-most cell.
	 */
	
	public double getMiny();

	/**
	 * Returns the depth of the cell at the provided coordinates, taking into
	 * account the slope of the cell.
	 */
	
	public double getRealDepth(double x, double y);

	/**
	 * Retrieves corner vertices associated with a cell intersecting the given
	 * Coordinate value
	 * 
	 * @param c
	 *            - a Coordinate containing positional information.
	 * @return
	 */

	public Coordinate[] getVertices(Coordinate c);

	/**
	 * Retrieves vertices associated with a cell intersecting a set of indices.
	 * 
	 * @param indices
	 *            - a set of indices indicating position within the grid.
	 * @return
	 */

	public Coordinate[] getVertices(int[] indices);

	/**
	 * Retrieves a List of vertices associated with cells intersecting a List of
	 * indices.
	 * 
	 * @param indices
	 * @return
	 */

	public List<Coordinate[]> getVertices(List<int[]> indices);
}
