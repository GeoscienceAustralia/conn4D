package lagrange.utils;

/**
 * Given an x and y position, this class returns the corresponding indices on a regular
 * grid.
 *  
 * @author Johnathan Kool
 *
 */

public class IndexLookup_Grid {

		private final double minx;
		private final double miny;
		private final double cellsize_x;
		private final double cellsize_y;

		/**
		 * Seven-parameter grid constructor
		 * 
		 * @param minx - the minimum x value of the grid (left coordinate)
		 * @param miny - the minimum y value of the grid (bottom coordinate)
		 * @param maxx - the maximum x value of the grid (right coordinate)
		 * @param maxy - the maximum y value of the grid (top coordinate)
		 * @param nrows - the number of grid rows
		 * @param ncols - the number of grid columns
		 * @param cellsize - the size of an individual cell (assumed to be square)
		 */
		
		public IndexLookup_Grid(double minx, double miny, double maxx, double maxy,
				int nrows, int ncols,double cellsize) {
			this.minx = minx;
			this.miny = miny;
			this.cellsize_x = cellsize;
			this.cellsize_y = cellsize;
		}

		/**
		 * Retrieves the indices at the designated coordinates.
		 * 
		 * @param x
		 * @param y
		 * @return
		 */
		
		public int[] getIndices(double x, double y) {
			
			int[] out = new int[2];
			out[1] = (int) (Math.floor((x-minx)/cellsize_x)); 
			out[0] = (int) (Math.floor((y-miny)/cellsize_y));
			return out;
		}
}
