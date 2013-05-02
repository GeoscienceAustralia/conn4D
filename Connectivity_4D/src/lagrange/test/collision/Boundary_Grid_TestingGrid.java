package lagrange.test.collision;

import com.vividsolutions.jts.geom.Coordinate;

import lagrange.Boundary_Grid;

public class Boundary_Grid_TestingGrid implements Boundary_Grid {

	public double minx = -1;
	public double miny = -1;
	public double cellsize = 1;
	public Coordinate midpoint = new Coordinate(0,0,0);
	
	public Coordinate[][] ca;

	public double getBoundaryDepth() {
		return 1.207107;
	}

	public double getBoundaryDepth(double a, double b) {
		return 1.207107;
	}

	public Boundary_Grid_TestingGrid clone() {
		return this;
	}

	public double getPreciseBoundaryDepth() {
		return 0;
	}

	public double getPreciseBoundaryDepth(double a, double b) {
		return 0;
	}

	public void setPositiveDown(boolean down) {
	}

	public double getMinx() {
		return minx;
	}

	public double getMiny() {
		return miny;
	}

	public double getCellSize() {
		return cellsize;
	}

	public Coordinate[] getVertices(Coordinate c) {
		if (c.x < midpoint.x && c.y < midpoint.x) {
			return ca[1];
		} 
		if (c.x < midpoint.x) {
			return ca[0];
		}
		if(c.y < midpoint.x){
			return ca[3];
		}
		return ca[2];
	}

	public Coordinate[] getVertices(int[] indices) {
		if(indices[0] < 0 && indices[1] < 0){
			return ca[1];
		}
		if (indices[1] < 0) {
			return ca[0];
		}
		if (indices[0] < 0){
			return ca[3];
		}
		return ca[2];
	}

	public int[] getIndices(double x, double y) {
		if (x < 0 && y < 0) {
			return new int[] { -1, -1 };
		}
		if (x < 0){return new int[] {0,-1};}
		if (y < 0){return new int[] {-1,0};}
		return new int[] {0,0};
	}

	public double getRealDepth(double x, double y) {
		return getPreciseBoundaryDepth();
	}
	
	public void setCells(Coordinate[][] ca){
		this.ca = ca;
	}
}
