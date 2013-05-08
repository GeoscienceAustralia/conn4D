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
	 * Performs actions that relocate a Particle upon encountering a
	 * barrier.
	 */
	
	@Override
	public void handleIntersection(Particle p) {

		// In addition to reflecting, we may also want to consider triggering
		// settling based on shear stress values

		Coordinate start = new Coordinate(p.getPX(), p.getPY(), p.getPZ());
		Coordinate end = new Coordinate(p.getX(), p.getY(), p.getZ());
		Coordinate start_prj = pt.project(start);
		Coordinate end_prj = pt.project(end);

		LineSegment tmpln = new LineSegment(start_prj, end_prj);

		// Get the vertices (x,y,z) of the four corners of the cell beneath the
		// Particle's initial position

		Coordinate[] box = pt.project(bnd.getVertices(start));

		// If we can't find coordinates for the starting position then
		// we are most likely outside the data domain. Return Lost.

		if (box == null) {
			p.setLost(true);
			return;
		}

		// Because we are using lats and lons for our horizontal reference
		// system, we must convert on the fly to meters to ensure proper
		// reflection in the vertical

		LineSegment trans = new LineSegment(start_prj, end_prj);

		// Test for reflection at least once

		trans = i3d.reflect_special(trans, box);

		// Back-converting the reflection

		LineSegment backtrans = new LineSegment(pt.inverse(trans.p0),
				pt.inverse(trans.p1));

		// Get the indices of the starting point and end point
		// after checking for reflection

		int[] startCell = bnd.getIndices(backtrans.p0.x, backtrans.p0.y);
		int[] endCell = bnd.getIndices(backtrans.p1.x, backtrans.p1.y);

		// If, the start and end cells match and p0 hasn't changed
		// (i.e. there was no reflection) then halt

		if (Arrays.equals(startCell, endCell) && trans.p0.equals2D(tmpln.p0)) {
			p.setX(backtrans.p1.x);
			p.setY(backtrans.p1.y);
			p.setZ(backtrans.p1.z);
			return;
		}

		// Otherwise, set up a RefrenceGrid to identify the grid path. We create
		// a new one because it is very lightweight and threading issues can be
		// avoided.

		ReferenceGrid refgrid = new ReferenceGrid(bnd.getMinx(), bnd.getMiny(),
				bnd.getCellSize());
		refgrid.setLine(backtrans);
		int[] currentCell = new int[] { startCell[0], startCell[1] };

		// Start keeping track of the number of internal reflections

		int internal_reflections = 0;

		// While the current cell is not the last cell in the path,
		// or if the reflection line has been updated, keep looping

		while (!Arrays.equals(currentCell, endCell)
				|| !trans.p0.equals2D(tmpln.p0)) {

			// As a safety measure, flag and kill particles with null coordinate
			// values.

			if (Double.isNaN(trans.p0.x) && Double.isNaN(trans.p0.y)) {
				System.out
						.println("\nWarning: Reflection start is a NaN value.  Aborting particle "
								+ p.getID()
								+ " at time= "
								+ TimeConvert.millisToDays(p.getAge())
								+ ", track " + start + " " + end);
				p.setError(true);
				p.setLost(true);
				break;
			}

			// Safety valve to prevent endless looping

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

			// If there was a reflection

			if (!trans.p0.equals3D(tmpln.p0)) {

				// Check if the point of reflection is on the edge of a cell

				if (refgrid.isOnEdge(backtrans.p0)) {

					Coordinate vertex = trans.p0;

					// Look at the adjacent cell(s)
					List<int[]> cells = refgrid.getAdjacencies(vertex);
					Coordinate principal_centroid = CoordinateMath.average(box);

					// For each adjacent cell, compensate for joint reflection

					for (int i = 0; i < cells.size(); i++) {

						VectorMath.flip(cells.get(i)); // convert from Cartesian
														// to ij
						
						Coordinate[] vertices = bnd.getVertices(cells.get(i));
						Coordinate[] vert_prj = pt.project(vertices);
						Coordinate adj_centroid = CoordinateMath
								.average(vert_prj);

						// This is the angle between the principal cell and
						// the adjacent cell divided by two. The division
						// by two yields the middle angle between the two.
						// Division does not affect the sign of the angle.

						double adj_angle = CoordinateMath.angle3DSigned(
								principal_centroid, vertex, adj_centroid,
								tmpln.p0) / 2;

						// If the angle is convex (negative), then ignore (edge
						// skim)

						if (adj_angle < 0) {
							continue;

						} else {

							// Calculate the difference between the angle of
							// incidence and the bisecting line

							double adj_o_angle = CoordinateMath.angle3DSigned(
									tmpln.p0, vertex, principal_centroid,
									tmpln.p0);

							// Calculate the rotation angle by reversing
							// direction (pi radians/180) and subtracting twice
							// the angle from the bisector. Once to get to the
							// bisector and again to get to the reflection
							// point.

							double r_angle = Math.PI - 2
									* (adj_angle - adj_o_angle);

							// Determine the axis of rotation by taking the
							// normal to the plane formed by the origin line
							// segment, the intersection point and the original
							// destination point

							Coordinate adj_norm = CoordinateMath
									.normal(new Coordinate[] { tmpln.p0,
											vertex, trans.p1 });
							Coordinate newaxis = CoordinateMath.add(adj_norm,
									vertex);

							// Perform the rotation

							Coordinate new_loc = CoordinateMath.rotate3D(
									tmpln.p1, newaxis, vertex, r_angle);

							if (new_loc.z > surfaceLevel) {
								new_loc.z = surfaceLevel;
							}

							tmpln.p1 = new_loc;
						} // End of else condition
					} // End of edge loop
					trans.p1 = tmpln.p1;
				} // End of edge condition

				// Nibble to prevent re-reflection. THIS IS IMPORTANT,
				// and has been the subject of a lot of debugging.
				// Using Double.MIN_VALUE or 1E-16 still creates problems,
				// so a larger value is needed, i.e. 1E-8.

				CoordinateMath.nibble(trans, 1E-8);
				backtrans = new LineSegment(pt.inverse(trans.p0),
						pt.inverse(trans.p1));
				refgrid.setLine(backtrans);
				tmpln.p0 = trans.p0;
				trans = i3d.reflect_special(trans, box);
				internal_reflections++;
				continue;
			}

			// Identify the direction of the next cell, then flip indices
			// because we are working with ij, but the DDA returns
			// horizontal first, then vertical

			int[] dir = refgrid.nextCell();
			VectorMath.flip(dir); // convert from Cartesian to ij
									// order

			// Add the result to our current cell index to increment

			currentCell = VectorMath.add(currentCell, dir);

			// Sanity check

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

			if (currentCell[0] < Math.min(startCell[0], endCell[0])
					|| currentCell[0] > Math.max(startCell[0], endCell[0])
					|| currentCell[1] < Math.min(startCell[1], endCell[1])
					|| currentCell[1] > Math.max(startCell[1], endCell[1])) {
				System.out
						.println("\nWarning:  Collision error.  Aborting particle "
								+ p.getID()
								+ " at time="
								+ TimeConvert.millisToDays(p.getAge())
								+ ", track " + start + " " + end);
				p.setError(true);
				return;
			}

			// Retrieve the vertices of the current cell

			box = pt.project(bnd.getVertices(currentCell));

			// If the vertices are out of bounds, continue - this should be
			// handled elsewhere

			if (box == null) {
				continue;
			}

			trans = i3d.reflect_special(trans, box);
			backtrans = new LineSegment(pt.inverse(trans.p0),
					pt.inverse(trans.p1));

			internal_reflections++;
		}

		// Temporary check to make sure we're not below the benthic layer

		double realDepth = bnd.getRealDepth(backtrans.p1.x, backtrans.p1.y);

		if (backtrans.p1.z < realDepth) {
			trans.p1.z = realDepth + 1;
			if (realDepth + 1 > surfaceLevel) {
				trans.p1.z = surfaceLevel;
				backtrans.p1.z = surfaceLevel;
				p.setLost(true);
			} else {
				trans.p1.z = realDepth + 1;
				backtrans.p1.z = realDepth + 1;
			}
		}

		p.setX(backtrans.p1.x);
		p.setY(backtrans.p1.y);
		p.setZ(backtrans.p1.z);
	}
	
	/**
	 * Identifies whether a 4-dimensional coordinate (x,y,z,t) is within bounds
	 * or not.  t is not actually used in this case.
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
}