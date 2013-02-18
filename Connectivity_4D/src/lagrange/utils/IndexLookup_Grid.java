package lagrange.utils;

public class IndexLookup_Grid {

		private final double minx;
		private final double miny;
		private final double cellsize_x;
		private final double cellsize_y;

		public IndexLookup_Grid(double minx, double miny, double maxx, double maxy,
				int nrows, int ncols,double cellsize) {
			this.minx = minx;
			this.miny = miny;
			this.cellsize_x = cellsize;
			this.cellsize_y = cellsize;
		}

		public int[] getIndices(double x, double y) {
			
			int[] out = new int[2];
			out[1] = (int) (Math.floor((x-minx)/cellsize_x)); 
			out[0] = (int) (Math.floor((y-miny)/cellsize_y));
			return out;
		}
}
