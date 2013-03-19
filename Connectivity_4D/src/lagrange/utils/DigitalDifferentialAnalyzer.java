package lagrange.utils;

import com.vividsolutions.jts.geom.LineSegment;

public class DigitalDifferentialAnalyzer {

	private LineSegment ls;
	private double x_offset;
	private double y_offset;
	private int[] adjacency = new int[2];
	private int[] displacement = new int[2];
	private final double PRECISION = 1E-6;
	//private boolean first = true;
	private double x_linedist = Double.MAX_VALUE;
	private double y_linedist = Double.MAX_VALUE;

	@SuppressWarnings("unused")
	private int x, y;
	private double dx, dy;
	private double dt_dx, dt_dy;
	private double snap_x, snap_y, cellsize;

	private int n_divisions = 0;
	private int x_inc, y_inc;
	private double t_next_vertical, t_next_horizontal;
	
	/**
	 * Constructor taking a snap point for adjusting grid intersection points
	 * 
	 * @param snap_x - x coordinate of the snap point
	 * @param snap_y - y coordinate of the snap point
	 */
	
	public DigitalDifferentialAnalyzer(double snap_x, double snap_y, double cellsize) {
		this.snap_x = snap_x;
		this.snap_y = snap_y;
		this.cellsize = cellsize;
	}
		
	/**
	 * Constructor taking both a LineSegment and a snap point.
	 */
		
	public DigitalDifferentialAnalyzer(LineSegment ls, double snap_x, double snap_y, double cellsize) {
		this.snap_x = snap_x;
		this.snap_y = snap_y;
		this.cellsize = cellsize;;
		setLine(ls);
	}
	
	/**
	 * Identifies the relative position of the next adjacent cell
	 * according to the direction of the LineString.  The horizontal 
	 * is provided first, followed by the vertical.
	 */

	public void increment() {
		/*if(first){
			first = false;
			if(edgeCheck()){
				if(x_linedist<y_linedist){
					adjacency[0] = x_inc;
					adjacency[1] = 0;
				}
				if(x_linedist>y_linedist){
					adjacency[0] = 0;
					adjacency[1] = y_inc;
				}
				if(x_linedist==y_linedist){
					adjacency[0] = x_inc;
					adjacency[1] = y_inc;
				}
				displacement[0]+=adjacency[0];
				displacement[1]+=displacement[1];
				return;
			}
			increment();
		}*/
		
		if (t_next_vertical < t_next_horizontal) {
			y += y_inc;
			t_next_vertical += dt_dy;
			adjacency[0] = 0;
			adjacency[1] = y_inc;
			displacement[0]+=adjacency[0];
			displacement[1]+=displacement[1];
			return;
		}

		if (t_next_vertical > t_next_horizontal) {
			x += x_inc;
			t_next_horizontal += dt_dx;
			adjacency[0] = x_inc;
			adjacency[1] = 0;
			displacement[0]+=adjacency[0];
			displacement[1]+=displacement[1];
			return;
		}

		if (t_next_vertical == t_next_horizontal) {
			x += x_inc;
			t_next_horizontal += dt_dx;
			y += y_inc;
			t_next_vertical += dt_dy;
			adjacency[0] = x_inc;
			adjacency[1] = y_inc;
			displacement[0]+=adjacency[0];
			displacement[1]+=displacement[1];
		}
	}
	
	/**
	 * Increments the cell position and retrieves the position of the
	 * next adjacent cell
	 * 
	 * @return an integer array containing the relative position of the
	 * next adjacent cell
	 */

	public int[] nextCell() {
		increment();
		return adjacency;
	}

	/**
	 * Sets the LineSegment to be intersected 
	 * @param ls
	 */

	public boolean edgeCheck(){
		if (Math.abs(x_linedist)<PRECISION||Math.abs(y_linedist)<PRECISION){
			return true;
		}
		return false;
	}
	
	public void setLine(LineSegment ls) {
		this.ls = ls;

		double p0x = ls.p0.x;
		double p0y = ls.p0.y;
		double p1x = ls.p1.x;
		double p1y = ls.p1.y;
		
		x_offset = (snap_x)%cellsize;
		y_offset = (snap_y)%cellsize;

		x = (int) (Math.floor(p0x));
		y = (int) (Math.floor(p0y));
		
		x_linedist = (p0x-snap_x)%cellsize-cellsize;
		y_linedist = (p0y-snap_y)%cellsize-cellsize;

		dx = Math.abs(p1x - p0x);
		dy = Math.abs(p1y - p0y);

		dt_dx = 1.0 / dx;
		dt_dy = 1.0 / dy;

		if (Math.abs(p1x - p0x) < PRECISION) {
			x_inc = 0;
			t_next_horizontal = Double.POSITIVE_INFINITY; // infinity
		} else if (p1x > p0x) {
			double pos_x = (p0x-x_offset)/cellsize;
			x_inc = 1;
			t_next_horizontal = (Math.floor(pos_x) + 1 - pos_x) * dt_dx;
		} else {
			double pos_x = (p0x-x_offset)/cellsize;
			x_inc = -1;         //distance to travel in x/difference from integer (x) * change in t wrt x (m).
			t_next_horizontal = (pos_x - Math.floor(pos_x)) * dt_dx;
		}

		if (Math.abs(p1y - p0y) == 0) {
			y_inc = 0;
			t_next_vertical = Double.POSITIVE_INFINITY; // infinity
		} else if (p1y > p0y) {
			y_inc = 1;
			double pos_y = (p0y-y_offset)/cellsize;
			t_next_vertical = (Math.floor(pos_y) + 1 - pos_y) * dt_dy;
		} else {
			y_inc = -1;
			double pos_y = (p0y-y_offset)/cellsize;
			t_next_vertical = (pos_y - Math.floor(pos_y)) * dt_dy;
		}
		
		if(t_next_horizontal==0d){t_next_horizontal = dt_dx;}
		if(t_next_vertical==0d){t_next_vertical = dt_dy;}
		
		displacement = new int[2];
	}
	
	/**
	 * Retrieves the current adjacent cell (without incrementing position)
	 * @return
	 */
	
	public int[] getAdjacency() {
		return adjacency;
	}
	
	/**
	 * Retrieves the cumulative positional change
	 * @return
	 */
	
	public int[] getDisplacement(){
		return displacement;
	}
	
	/**
	 * Retrieves the currently set LineSegment
	 * @return
	 */

	public LineSegment getLine() {
		return ls;
	}
	
	/**
	 * Returns the number of line divisions
	 * @return
	 */
	
	public int getN_divisions() {
		return n_divisions;
	}
	
	/**
	 * Retrieves the precision of the grid (for determining what
	 * constitutes horizontal and vertical)
	 * @return
	 */
	
	public double getPRECISION() {
		return PRECISION;
	}
	
	/**
	 * Retrieves the offset of the initial point from the grid in the x direction
	 * @return
	 */

	public double getX_offset() {
		return x_offset;
	}

	/**
	 * Retrieves the offset of the initial point from the grid in the y direction
	 * @return
	 */
	
	public double getY_offset() {
		return y_offset;
	}
}

