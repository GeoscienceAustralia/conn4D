package lagrange;

import com.vividsolutions.jts.geom.Coordinate;
import java.util.List;

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
