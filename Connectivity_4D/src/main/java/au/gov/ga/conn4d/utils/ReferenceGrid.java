/*******************************************************************************
 * Copyright 2014 Geoscience Australia (www.ga.gov.au)
 * @author - Johnathan Kool (Geoscience Australia)
 * 
 * Licensed under the BSD-3 License
 * 
 * http://opensource.org/licenses/BSD-3-Clause
 *  
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *  
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package au.gov.ga.conn4d.utils;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

public class ReferenceGrid {

	private LineSegment ls;
	private double x_offset;
	private double y_offset;
	private int[] nextDirection = new int[2];
	private int[] displacement = new int[2];
	private final double PRECISION = 1E-6;
	private double x_linedist = Double.MAX_VALUE;
	private double y_linedist = Double.MAX_VALUE;

	// private int x, y;
	private double dx, dy;
	private double dt_dx, dt_dy;
	private double snap_x, snap_y, cellsize;

	private int n_divisions = 0;
	private int x_inc, y_inc;
	private double t_next_vertical, t_next_horizontal;

	/**
	 * Constructor taking a snap point for adjusting grid intersection points
	 * 
	 * @param snap_x
	 *            - x coordinate of the snap point
	 * @param snap_y
	 *            - y coordinate of the snap point
	 */

	public ReferenceGrid(double snap_x, double snap_y, double cellsize) {
		this.snap_x = snap_x;
		this.snap_y = snap_y;
		this.cellsize = cellsize;
	}

	/**
	 * Constructor taking both a LineSegment and a snap point.
	 */

	public ReferenceGrid(LineSegment ls, double snap_x, double snap_y,
			double cellsize) {
		this.snap_x = snap_x;
		this.snap_y = snap_y;
		this.cellsize = cellsize;
		;
		setLine(ls);
	}

	/**
	 * Returns the next cell without incrementing
	 */

	public int[] peek() {
		if (t_next_vertical < t_next_horizontal) {
			return new int[] { 0, y_inc };
		}

		if (t_next_vertical > t_next_horizontal) {
			nextDirection[0] = x_inc;
			nextDirection[1] = 0;
			return new int[] { x_inc, 0 };
		}

		return new int[] { x_inc, y_inc };
	}

	/**
	 * Identifies the relative position of the next adjacent cell according to
	 * the direction of the LineString. The horizontal is provided first,
	 * followed by the vertical.
	 */

	public void increment() {

		if (t_next_vertical < t_next_horizontal) {
			// y += y_inc;
			t_next_vertical += dt_dy;
			nextDirection[0] = 0;
			nextDirection[1] = y_inc;
			displacement[0] += nextDirection[0];
			displacement[1] += displacement[1];
			return;
		}

		if (t_next_vertical > t_next_horizontal) {
			// x += x_inc;
			t_next_horizontal += dt_dx;
			nextDirection[0] = x_inc;
			nextDirection[1] = 0;
			displacement[0] += nextDirection[0];
			displacement[1] += displacement[1];
			return;
		}

		if (t_next_vertical == t_next_horizontal) {
			// x += x_inc;
			t_next_horizontal += dt_dx;
			// y += y_inc;
			t_next_vertical += dt_dy;
			nextDirection[0] = x_inc;
			nextDirection[1] = y_inc;
			displacement[0] += nextDirection[0];
			displacement[1] += displacement[1];
		}
	}

	/**
	 * Increments the cell position and retrieves the position of the next
	 * adjacent cell
	 * 
	 * @return an integer array containing the relative position of the next
	 *         adjacent cell
	 */

	public int[] nextCell() {
		increment();
		return nextDirection;
	}

	/**
	 * Sets the LineSegment to be intersected
	 */

	public boolean edgeCheck() {
		if (Math.abs(x_linedist) < PRECISION
				|| Math.abs(y_linedist) < PRECISION) {
			return true;
		}
		return false;
	}

	/**
	 * Identifies whether the coordinate is on a horizontal edge within
	 * precision
	 * 
	 * @param c
	 */

	public boolean isOnHorizontalEdge(Coordinate c) {
		if ((0.5 * cellsize)
				- Math.abs((c.y - snap_y) % cellsize - (0.5 * cellsize)) < PRECISION) {
			return true;
		}
		return false;
	}

	/**
	 * Identifies whether the LineString is on a horizontal seam within
	 * precision
	 * 
	 * @param ls
	 */

	public boolean isOnHorizontalSeam(LineSegment ls) {
		if (Math.abs(ls.p1.y - ls.p0.y) < PRECISION
				&& (0.5 * cellsize)
						- Math.abs(((ls.p0.y + ls.p1.y) / 2 - snap_y)
								% cellsize - (0.5 * cellsize)) < PRECISION) {
			return true;
		}
		return false;
	}

	/**
	 * Identifies whether the provided Coordinate is on a vertical edge within
	 * precision.
	 */

	public boolean isOnVerticalEdge(Coordinate c) {
		if ((0.5 * cellsize)
				- Math.abs((c.x - snap_x) % cellsize - (0.5 * cellsize)) < PRECISION) {
			return true;
		}
		return false;
	}

	/**
	 * Identifies whether the provided Coordinate is on a vertical seam within
	 * precision
	 */

	public boolean isOnVerticalSeam(LineSegment ls) {
		if (Math.abs(ls.p1.x - ls.p0.x) < PRECISION
				&& (0.5 * cellsize)
						- Math.abs(((ls.p0.x + ls.p1.y) / 2 - snap_x)
								% cellsize - (0.5 * cellsize)) < PRECISION) {
			return true;
		}
		return false;
	}

	/**
	 * Identifies whether the coordinate is on an edge within precision
	 * 
	 * @param c
	 */

	public boolean isOnEdge(Coordinate c) {
		return isOnVerticalEdge(c) || isOnHorizontalEdge(c);
	}

	/**
	 * Identifies whether the coordinate is on an edge within precision
	 * 
	 * @param ls
	 */

	public boolean isOnSeam(LineSegment ls) {
		return isOnVerticalSeam(ls) || isOnHorizontalSeam(ls);
	}

	/**
	 * Identifies whether the coordinate is on a corner within precision
	 * 
	 * @param c
	 */

	public boolean isOnCorner(Coordinate c) {
		return isOnVerticalEdge(c) && isOnHorizontalEdge(c);
	}

	/**
	 * Sets the LineSegment defining the path of the digital differential
	 * analyzer - i.e. the nextCell() pattern.
	 * 
	 * @param ls
	 *            - the LineSegment indicating a vector path through the grid.
	 */

	public void setLine(LineSegment ls) {
		this.ls = ls;

		double p0x = ls.p0.x;
		double p0y = ls.p0.y;
		double p1x = ls.p1.x;
		double p1y = ls.p1.y;

		x_offset = (snap_x) % cellsize;
		y_offset = (snap_y) % cellsize;

		// x = (int) (Math.floor(p0x));
		// y = (int) (Math.floor(p0y));

		x_linedist = (p0x - snap_x) % cellsize - cellsize;
		y_linedist = (p0y - snap_y) % cellsize - cellsize;

		dx = Math.abs(p1x - p0x);
		dy = Math.abs(p1y - p0y);

		dt_dx = 1.0 / dx;
		dt_dy = 1.0 / dy;

		if (Math.abs(p1x - p0x) < PRECISION) {
			x_inc = 0;
			t_next_horizontal = Double.POSITIVE_INFINITY; // infinity
		} else if (p1x > p0x) {
			double pos_x = (p0x - x_offset) / cellsize;
			x_inc = 1;
			t_next_horizontal = (Math.floor(pos_x) + 1 - pos_x) * dt_dx;
		} else {
			double pos_x = (p0x - x_offset) / cellsize;
			x_inc = -1; // distance to travel in x/difference from integer (x) *
						// change in t wrt x (m).
			t_next_horizontal = (pos_x - Math.floor(pos_x)) * dt_dx;
		}

		if (Math.abs(p1y - p0y) == 0) {
			y_inc = 0;
			t_next_vertical = Double.POSITIVE_INFINITY; // infinity
		} else if (p1y > p0y) {
			y_inc = 1;
			double pos_y = (p0y - y_offset) / cellsize;
			t_next_vertical = (Math.floor(pos_y) + 1 - pos_y) * dt_dy;
		} else {
			y_inc = -1;
			double pos_y = (p0y - y_offset) / cellsize;
			t_next_vertical = (pos_y - Math.floor(pos_y)) * dt_dy;
		}

		if (t_next_horizontal == 0d) {
			t_next_horizontal = dt_dx;
		}
		if (t_next_vertical == 0d) {
			t_next_vertical = dt_dy;
		}

		displacement = new int[2];
	}

	/**
	 * Returns a list of cell indices that are adjacent to the provided
	 * Coordinate
	 * 
	 * @param c
	 */

	public List<int[]> getCellList(Coordinate c) {
		List<int[]> list = new ArrayList<int[]>();
		int[] principal = new int[] {
				(int) Math.floor((c.x - snap_x) / cellsize),
				(int) Math.floor((c.y - snap_y) / cellsize) };
		list.add(principal);
		if (!isOnEdge(c)) {
			return list;
		}

		int x_neighbor = 0, y_neighbor = 0;

		if (isOnHorizontalEdge(c)) {
			y_neighbor = (int) (Math.signum(cellsize / 2 - c.y % cellsize)
					* (0.5 * cellsize) - Math.abs((c.y - snap_y) % cellsize
					- (0.5 * cellsize)));
			if (y_neighbor == 0) {
				int[] pos = new int[] {
						(int) Math.floor((c.x - snap_x) / cellsize),
						(int) Math.floor((c.y - snap_y - .25 * cellsize)
								/ cellsize) };
				if (pos[1] == principal[1]) {
					y_neighbor = 1;
				} else {
					y_neighbor = -1;
				}
			}
			list.add(new int[] { principal[0], principal[1] + y_neighbor });
		}

		if (isOnVerticalEdge(c)) {
			x_neighbor = (int) (Math.signum(cellsize / 2 - c.x % cellsize)
					* (0.5 * cellsize) - Math.abs((c.x - snap_x) % cellsize
					- (0.5 * cellsize)));
			if (x_neighbor == 0) {
				int[] pos = new int[] {
						(int) Math.floor((c.x - snap_x - .25 * cellsize)
								/ cellsize),
						(int) Math.floor((c.y - snap_y) / cellsize) };
				if (pos[0] == principal[0]) {
					x_neighbor = 1;
				} else {
					x_neighbor = -1;
				}
			}
			list.add(new int[] { principal[0] + x_neighbor, principal[1] });
		}

		if (isOnCorner(c)) {
			list.add(new int[] { principal[0] + x_neighbor,
					principal[1] + y_neighbor });
		}

		return list;
	}

	public List<int[]> getAdjacencies(Coordinate c) {
		List<int[]> adjacencies = getCellList(c);
		adjacencies.remove(0);
		return adjacencies;
	}

	/**
	 * Retrieves the current adjacent cell (without incrementing position)
	 */

	public int[] getNextDirection() {
		return nextDirection;
	}

	/**
	 * Retrieves the cumulative positional change
	 */

	public int[] getDisplacement() {
		return displacement;
	}

	/**
	 * Retrieves the currently set LineSegment
	 */

	public LineSegment getLine() {
		return ls;
	}

	/**
	 * Returns the number of line divisions
	 */

	public int getN_divisions() {
		return n_divisions;
	}

	/**
	 * Retrieves the precision of the grid (for determining what constitutes
	 * horizontal and vertical)
	 */

	public double getPRECISION() {
		return PRECISION;
	}

	/**
	 * Retrieves the offset of the initial point from the grid in the x
	 * direction
	 */

	public double getX_offset() {
		return x_offset;
	}

	/**
	 * Retrieves the offset of the initial point from the grid in the y
	 * direction
	 */

	public double getY_offset() {
		return y_offset;
	}
}
