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

package au.gov.ga.conn4d.test.impl.collision;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import au.gov.ga.conn4d.BoundaryRaster;

import com.vividsolutions.jts.geom.Coordinate;


/**
 * Class for creating small 4-cell based boundaries for testing collision
 * detections and reflections
 * 
 * @author Johnathan Kool
 */

public class Boundary_Grid_TestingGrid implements BoundaryRaster, Cloneable {

	private double minx = -1;
	private double miny = -1;
	private double cellsize = 1;
	public Coordinate midpoint = new Coordinate(0, 0, 0);

	public Coordinate[][] ca;

	/**
	 * Normally used to retrieve a clone of the class instance. Unneeded for
	 * testing, therefore the same object is returned.
	 */

	@Override
	public Boundary_Grid_TestingGrid clone() {
		return this;
	}

	/**
	 * Retrieves a boundary depth. Set as a constant.
	 */

	public double getBoundaryDepth() {
		return 1.207107;
	}

	/**
	 * Retrieves a boundary depth given an x,y pair. Set as a constant.
	 */

	@Override
	public double getBoundaryDepth(double a, double b) {
		return 1.207107;
	}

	/**
	 * Retrieves the cell size of the testing grid (1).
	 */
	
	@Override
	public double getCellSize() {
		return cellsize;
	}
	
	/**
	 * Retrieves the grid indices associated with the provided Coordinate.
	 */

	@Override
	public int[] getIndices(Coordinate c) {
		return getIndices(c.x, c.y);
	}
	
	/**
	 * Retrieves the grid indices associated with the given x,y pair.
	 */

	@Override
	public int[] getIndices(double x, double y) {
		return new int[] { (int) Math.floor((y - miny) / cellsize),
				(int) Math.floor((x - minx) / cellsize) };
	}

	/**
	 * Retrieves the minimum x value associated with the testing grid (-1).
	 */
	
	@Override
	public double getMinx() {
		return minx;
	}

	/**
	 * Retrieves the minimum y value associated with the testing grid (-1).
	 */
	
	@Override
	public double getMiny() {
		return miny;
	}

	/**
	 * Retrieves the precise boundary depth. Set as a constant 0.
	 */

	public double getPreciseBoundaryDepth() {
		return 0;
	}

	/**
	 * Retrieves the precise boundary depth given an x,y pair. Set as a constant
	 * 0.
	 */

	@Override
	public double getPreciseBoundaryDepth(double a, double b) {
		return 0;
	}

	/**
	 * Retrieves the precise boundary depth given an x,y pair. Set as a constant
	 * 0. Duplicate?
	 */
	
	@Override
	public double getRealDepth(double x, double y) {
		return getPreciseBoundaryDepth();
	}

	/**
	 * Retrieves the vertices of the cell intersecting the provided Coordinate.
	 */
	
	@Override
	public Coordinate[] getVertices(Coordinate c) {
		return getVertices(getIndices(c));
	}

	/**
	 * Retrieves the vertices of the cell intersecting the provided index values.
	 */
	
	@Override
	public Coordinate[] getVertices(int[] indices) {
		int len = (int) Math.sqrt(indices.length);
		int index = len * indices[0] + indices[1];
		return ca[index];
	}
	
	/**
	 * Retrieves a List of vertices of the cells intersecting the provided List of index values.
	 */

	@Override
	public List<Coordinate[]> getVertices(List<int[]> indices) {
		List<Coordinate[]> list = new ArrayList<Coordinate[]>(indices.size());
		Iterator<int[]> it = indices.iterator();
		while (it.hasNext()) {
			list.add(getVertices(it.next()));
		}
		return list;
	}
	
	/**
	 * Explicitly sets the vertices of the test cells
	 * 
	 * @param ca - An array of Coordinate arrays representing the vertices of the
	 * test cells.
	 */

	public void setCells(Coordinate[][] ca) {
		this.ca = ca;
	}

	/**
	 * Sets the cell size of the test cells.
	 */
	
	public void setCellSize(double cellsize) {
		this.cellsize = cellsize;
	}

	/**
	 * Sets the minimum
	 * @param minx
	 */
	
	public void setMinX(double minx) {
		this.minx = minx;
	}

	public void setMinY(double miny) {
		this.miny = miny;
	}

	@Override
	public void setPositiveDown(boolean down) {
	}
}
