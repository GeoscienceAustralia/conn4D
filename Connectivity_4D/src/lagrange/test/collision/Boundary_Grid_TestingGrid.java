package lagrange.test.collision;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import lagrange.Boundary_Grid;

public class Boundary_Grid_TestingGrid implements Boundary_Grid {

	private double minx = -1;
	private double miny = -1;
	private double cellsize = 1;
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
		return getVertices(getIndices(c));
	}

	public Coordinate[] getVertices(int[] indices) {
		int len = (int) Math.sqrt(indices.length);
		int index = len*indices[0]+ indices[1];
		return ca[index];
	}
	
	public List<Coordinate[]> getVertices(List<int[]> indices){
		List<Coordinate[]> list = new ArrayList<Coordinate[]>(indices.size());
		Iterator<int[]> it = indices.iterator();
		while(it.hasNext()){
			list.add(getVertices(it.next()));
		}
		return list;
	}

	public int[] getIndices(double x, double y) {
		return new int[] {(int) Math.floor((y-miny)/cellsize),(int) Math.floor((x-minx)/cellsize)};
	}
	
	public int[] getIndices(Coordinate c){
		return getIndices(c.x,c.y);
	}

	public double getRealDepth(double x, double y) {
		return getPreciseBoundaryDepth();
	}
	
	public void setCells(Coordinate[][] ca){
		this.ca = ca;
	}
	
	public void setMinX(double minx){
		this.minx = minx;
	}
	public void setMinY(double miny){
		this.miny = miny;
	}
	public void setCellSize(double cellsize){
		this.cellsize = cellsize;
	}
}
