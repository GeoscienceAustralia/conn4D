package lagrange.impl.collision;

import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

import lagrange.Boundary;
import lagrange.CollisionDetector;
import lagrange.Particle;
import lagrange.impl.readers.Boundary_NetCDF_Grid;
import lagrange.utils.CoordinateMath;
import lagrange.utils.DigitalDifferentialAnalyzer;
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

	private Boundary_NetCDF_Grid bnd;
	private Intersector_3D_Raster i3d = new Intersector_3D_Raster();
	private double surfaceLevel = 0;
	private PrjTransform pt = new PrjTransform_WGS2CEQD();
	private final int bounceLimit = 50;

	public CollisionDetector_3D_Bathymetry(Boundary bathym) {
		this.bnd = (Boundary_NetCDF_Grid) bathym;
	}

	@Override
	public void handleIntersection(Particle p) {

		// In addition to reflecting, we may also want to consider triggering
		// settling based on shear stress values

		Coordinate start = new Coordinate(p.getPX(), p.getPY(), p.getPZ());
		Coordinate end = new Coordinate(p.getX(), p.getY(), p.getZ());
		Coordinate start_prj = pt.project(start);
		Coordinate end_prj = pt.project(end);

		LineSegment tmpln = new LineSegment(start_prj,end_prj);

		// Get the vertices (x,y,z) of the four corners of the cell beneath the
		// Particle's initial position

		Coordinate[] box = pt.project(bnd.getVertices(start));

		if (box == null) {
			p.setLost(true);
			return;
		}

		LineSegment trans = new LineSegment(start_prj, end_prj);

		// Test for reflection at least once

		trans = i3d.reflect_special(trans, box);

		// Because we are using lats and lons for our horizontal reference
		// system, we must convert on the fly to meters to ensure proper
		// reflection in the vertical, and then back-convert.

		LineSegment backtrans = new LineSegment(pt.inverse(trans.p0),
				pt.inverse(trans.p1));

		// Get the indices of the current starting point (
		// (after checking for reflection, in case it was
		// reflected) as well as its end point

		int[] startCell = bnd.getIndices(backtrans.p0.x, backtrans.p0.y);
		int[] endCell = bnd.getIndices(backtrans.p1.x, backtrans.p1.y);

		// If, after reflection the start end end cells match and there
		// was no reflection (i.e. p0 hasn't changed) then halt

		if (Arrays.equals(startCell, endCell) && trans.p0.equals2D(tmpln.p0)) {
			p.setX(backtrans.p1.x);
			p.setY(backtrans.p1.y);
			p.setZ(backtrans.p1.z);
			return;
		}

		// Otherwise set up the grid path
		// The above section ensures we don't create the DDA
		// unless it is necessary to do so.

		DigitalDifferentialAnalyzer dda = new DigitalDifferentialAnalyzer(
				bnd.getMinx(), bnd.getMiny(), bnd.getCellSize());
		dda.setLine(backtrans);
		int[] currentCell = new int[] { startCell[0], startCell[1] };

		// While we haven't reached the end cell,
		// or if a reflection took place(i.e. the reflected
		// start is not the same as the previous start value)
		// then continue loop

		int internal_bounces = 0;

		while (!Arrays.equals(currentCell, endCell)
				|| !trans.p0.equals2D(tmpln.p0)) {

			// Compensate for a null start

			if (Double.isNaN(trans.p0.x) && Double.isNaN(trans.p0.y)) {
				System.out.println("\nWarning: Reflection start is a NaN value.  Aborting particle "
						+ p.getID() + " at time= "
						+ TimeConvert.millisToDays(p.getAge()) + ", track "
						+ start + " " + end);
				p.setError(true);
				p.setLost(true);
				break;
			}
			
			if (internal_bounces > bounceLimit) {
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

			if (!trans.p0.equals2D(tmpln.p0)) {
				
				// Handle edge case

				//if (dda.isOnEdge(backtrans.p0)) {
					
					//LineSegment opposite_reflection;
					
					// Correct by averaging with the reflection off next box.
					
					//Coordinate[] peekbox = pt
					//		.project(bnd.getVertices(VectorMath.add(
					//				currentCell, dda.peek())));
					
					//Coordinate centroid_1 = CoordinateMath.average(box);
					//Coordinate vertex = trans.p0;
					//Coordinate centroid_2 = CoordinateMath.average(peekbox);
					//double angle = CoordinateMath.angle3DSigned(centroid_1, vertex, centroid_2,tmpln.p0);
					//System.out.print(angle);
					//if(angle<0){opposite_reflection = i3d.reflect_special(tmpln, peekbox);}
					//else{opposite_reflection = i3d.reflect_special(tmpln, peekbox);}			
					
					// LineSegment opposite = i3d.reflect_special(ls, peekbox);
				//}
				
				tmpln.p1 = trans.p1;

				// Nibble to prevent re-reflection
				CoordinateMath.nibble(trans, Double.MIN_VALUE);
				backtrans = new LineSegment(pt.inverse(trans.p0),
						pt.inverse(trans.p1));
				dda.setLine(backtrans);
				tmpln.p0 = trans.p0;
				trans = i3d.reflect_special(trans, box);
				internal_bounces++;
				continue;
			}

			// Identify the direction of the next cell, then flip indices
			// because we are working with ij, but the DDA returns
			// horizontal first, then vertical

			int[] dir = dda.nextCell();
			VectorMath.flip(dir);

			// Add the result to our current cell index to increment

			currentCell = VectorMath.add(currentCell, dir);

			// Sanity check

			if (internal_bounces > bounceLimit) {
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

			internal_bounces++;
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
		this.bnd = (Boundary_NetCDF_Grid) bnd;
	}

	public double getSurfaceLevel() {
		return surfaceLevel;
	}

	public void setSurfaceLevel(double surfaceLevel) {
		this.surfaceLevel = surfaceLevel;
	}

	public PrjTransform getProjectionTransform() {
		return pt;
	}

	public void setProjectionTransform(PrjTransform pt) {
		this.pt = pt;
	}
}
