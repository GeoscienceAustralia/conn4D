package lagrange;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

public interface Boundary_Grid extends Boundary {
	public Coordinate[] getVertices(Coordinate c);

	public Coordinate[] getVertices(int[] indices);
	
	public List<Coordinate[]> getVertices(List<int[]> indices);

	public int[] getIndices(double x, double y);
	
	public int[] getIndices(Coordinate c);

	public double getMinx();

	public double getMiny();

	public double getCellSize();

	public double getRealDepth(double x, double y);
}
