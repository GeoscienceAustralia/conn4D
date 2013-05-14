package lagrange.impl.collision;

import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

import lagrange.Boundary;
import lagrange.Boundary_Grid;
import lagrange.CollisionDetector;
import lagrange.Particle;
import lagrange.utils.CoordinateMath;
import lagrange.utils.ReferenceGrid;
import lagrange.utils.PrjTransform;
import lagrange.utils.TimeConvert;
import lagrange.utils.VectorMath;
import lagrange.utils.PrjTransform_WGS2CEQD;

/**
 * Adjusts Particle position and condition upon encountering a Barrier.
 * 
 * @param p
 */

public class CollisionDetector_3D_Bathymetry implements CollisionDetector {

	private Boundary_Grid bnd;
	private Intersector_3D_Raster i3d = new Intersector_3D_Raster();
	private double surfaceLevel = 0;
	private PrjTransform pt = new PrjTransform_WGS2CEQD();
	private final int bounceLimit = 50;

	public CollisionDetector_3D_Bathymetry(Boundary bathym) {
		this.bnd = (Boundary_Grid) bathym;
	}

	/**
	 * Performs actions that relocate a Particle upon encountering a barrier.
	 */

	@Override
	public void handleIntersection(Particle p) {

			ReferenceGrid rg = new ReferenceGrid(bnd.getMinx(), bnd.getMiny(),
					bnd.getCellSize());

			Coordinate start = new Coordinate(p.getPX(), p.getPY(), p.getPZ());
			Coordinate end = new Coordinate(p.getX(), p.getY(), p.getZ());

			// check if start/end/position are out of bounds...

			LineSegment ln = new LineSegment(start, end);
			rg.setLine(ln);

			int[] startcell = bnd.getIndices(ln.p0.x, ln.p0.y);
			int[] currentcell = new int[] { startcell[0], startcell[1] };
			int[] endcell = bnd.getIndices(ln.p1.x, ln.p1.y);

			int internal_reflections = 0;

			while (true) {
				// Error checking
				if (Double.isNaN(ln.p0.x) && Double.isNaN(ln.p0.y)) {
					System.out
							.println("\nWarning: Reflection start is a NaN value.  Aborting particle "
									+ p.getID()
									+ " at time= "
									+ TimeConvert.millisToDays(p.getAge())
									+ ", track " + start + " " + end);
					p.setError(true);
					p.setLost(true);
					return;
				}

				// Preventing infinite loops
				if (internal_reflections > bounceLimit) {
					System.out
							.println("\nWarning:  Repetition break.  Aborting particle "
									+ p.getID()
									+ " at time="
									+ TimeConvert.millisToDays(p.getAge())
									+ ", track " + start + " " + end);
					p.setError(true);
					return;
				}

				// Check whether an intersection has occurred

				Coordinate isect = i3d.intersect(ln, bnd.getVertices(currentcell));

				// If there was no intersection...
				
				if (isNaN(isect)) {
					
					// If we're at the last cell then break out of the loop.
					
					if (Arrays.equals(currentcell, endcell)) {
						break;
					}
					
					// Otherwise advance to the next cell.
					
					int[] nc = rg.nextCell();
					VectorMath.flip(nc);
					currentcell = VectorMath.add(currentcell, nc);
					
				// Otherwise reflect about the collective norm.
					
				} else {		

					List<int[]> cells = rg.getCellList(isect);
					Coordinate cnorm;
					
					// If there is only one cell, use its norm
					
					if (cells.size() == 1) {
						cnorm = CoordinateMath.normal_zplus(pt.project(bnd
								.getVertices(cells.get(0))));
					}
					
					// Otherwise determine the average of the norms

					else {
						Coordinate[] norms = new Coordinate[cells.size()];
						for (int i = 0; i < cells.size(); i++) {
							
							//z-plus ensures norms are facing up.
							
							Coordinate[] v = bnd.getVertices(cells.get(i));						
							norms[i] = CoordinateMath.normal_zplus(pt.project(v));
						}
						cnorm = CoordinateMath.average(norms);
					}
					
					// Project into meters to ensure properly scaled rotation
					// and then invert the projection to return to gcs.

					Coordinate update = pt.inverse(CoordinateMath.reflect(
							pt.project(ln.p1), pt.project(isect), cnorm));
					
					// If the length of the reflection is below the tolerance
					// threshold, then set as lost.  This could probably be
					// handled better, but only seems to be an issue in very
					// shallow areas where results are questionable anyways.
					
					double rd = bnd.getRealDepth(update.x, update.y);
					
					if(CoordinateMath.length3D(isect, update)<=1E-8){
						p.setLost(true);
						p.setX(isect.x);
						p.setY(isect.y);
						p.setZ(rd);
						return;
					}

					ln = new LineSegment(isect, update);
					
					// Prevent surface breaching
					if(ln.p1.z > surfaceLevel){
						ln.p1.z = surfaceLevel;
					}


					
					if(ln.p1.z < rd){
						p.setLost(true);
						p.setX(ln.p1.x);
						p.setY(ln.p1.y);
						p.setZ(rd);
						return;
					}
					
					// Remove a small section from the beginning of the line
					// to prevent re-reflection.  THIS IS IMPORTANT!				
					
					CoordinateMath.nibble(ln, 1E-08);
					
					rg.setLine(new LineSegment(ln));
					currentcell = bnd.getIndices(isect);
					endcell = bnd.getIndices(update);
					internal_reflections++;
				}
			}

			p.setX(ln.p1.x);
			p.setY(ln.p1.y);
			p.setZ(ln.p1.z);
	}

	/**
	 * Identifies whether a 4-dimensional coordinate (x,y,z,t) is within bounds
	 * or not. t is not actually used in this case.
	 */

	@Override
	public boolean isInBounds(long t, double z, double x, double y) {
		if (z < bnd.getBoundaryDepth(x, y)) {
			return false;
		}
		return true;
	}

	/**
	 * Generates a clone of the CollisionDetection instance.
	 * 
	 * @return
	 */

	@Override
	public CollisionDetector_3D_Bathymetry clone() {
		CollisionDetector_3D_Bathymetry clone = new CollisionDetector_3D_Bathymetry(
				bnd);
		clone.setProjectionTransform(this.pt);
		return clone;
	}

	/**
	 * Returns the Boundary object associated with this Class.
	 * 
	 * @return
	 */

	@Override
	public Boundary getBoundary() {
		return bnd;
	}

	/**
	 * Sets the Boundary object associated with this Class.
	 * 
	 * @return
	 */

	public void setBoundary(Boundary bnd) {
		this.bnd = (Boundary_Grid) bnd;
	}

	/**
	 * Retrieves the surface level being used (e.g. 0)
	 * 
	 * @return
	 */

	public double getSurfaceLevel() {
		return surfaceLevel;
	}

	/**
	 * Sets the surface level being used (e.g. 0)
	 * 
	 * @return
	 */

	public void setSurfaceLevel(double surfaceLevel) {
		this.surfaceLevel = surfaceLevel;
	}

	/**
	 * Retrieves the projection transformation being used to convert geographic
	 * horizontal coordiantes into metres.
	 * 
	 * @return
	 */

	public PrjTransform getProjectionTransform() {
		return pt;
	}

	/**
	 * Sets the projection transformation being used to convert geographic
	 * horizontal coordiantes into metres.
	 * 
	 * @return
	 */

	public void setProjectionTransform(PrjTransform pt) {
		this.pt = pt;
	}
	
	/**
	 * Used to perform NaN comparisons with coordinates since the
	 * default method does not handle NaN values well.
	 * 
	 * @param c
	 * @return
	 */

	private boolean isNaN(Coordinate c) {
		if (Double.isNaN(c.x) && Double.isNaN(c.y) && Double.isNaN(c.z)) {
			return true;
		}
		return false;
	}
}