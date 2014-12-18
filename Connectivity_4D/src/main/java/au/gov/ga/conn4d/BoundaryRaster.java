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

package au.gov.ga.conn4d;

import java.util.List;
import com.vividsolutions.jts.geom.Coordinate;

/**
 *  Boundary_Raster: Interface extension for the Boundary class to provide access 
 *                   to methods associated with raster-based data sources.
 */

public interface BoundaryRaster extends Boundary {

	/**
	 * Returns the cell size (assumed to be square) of the grid
	 * 
	 * @return - the cell size of the grid
	 */

	public double getCellSize();

	/**
	 * Retrieves the indices of the grid (lower left start) associated with the
	 * provided Coordinate
	 */

	public int[] getIndices(Coordinate c);

	/**
	 * Retrieves the indices of the grid associated with the provided x,y
	 * coordinate values.
	 */

	public int[] getIndices(double x, double y);

	/**
	 * Retrieves the minimum x value of the raster. This value corresponds to the
	 * left edge position of the left-most cell.
	 */
	
	public double getMinx();

	/**
	 * Retrieves the minimum y value of the raster. This value corresponds to the
	 * left edge position of the left-most cell.
	 */
	
	public double getMiny();

	/**
	 * Returns the depth of the cell at the provided coordinates, taking into
	 * account the slope of the cell.
	 */
	
	public double getRealDepth(double x, double y);

	/**
	 * Retrieves corner vertices associated with a cell intersecting the given
	 * Coordinate value
	 * 
	 * @param c
	 *            - a Coordinate containing positional information.
	 */

	public Coordinate[] getVertices(Coordinate c);

	/**
	 * Retrieves vertices associated with a cell intersecting a set of indices.
	 * 
	 * @param indices
	 *            - a set of indices indicating position within the grid.
	 */

	public Coordinate[] getVertices(int[] indices);

	/**
	 * Retrieves a List of vertices associated with cells intersecting a List of
	 * indices.
	 * 
	 * @param indices
	 */

	public List<Coordinate[]> getVertices(List<int[]> indices);
}
