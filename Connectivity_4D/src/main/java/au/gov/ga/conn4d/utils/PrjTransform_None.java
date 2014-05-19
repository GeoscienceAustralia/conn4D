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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

/**
 * PrjTransform implementation that performs no transformation.
 *  
 * @author Johnathan Kool
 */

public class PrjTransform_None implements PrjTransform{
	
	/**
	 * Projects an array of doubles from one coordinate system into another.
	 * In this case, no transformation occurs. 
	 */
	@Override
	public double[] project(double[] coords){return coords;}

	/**
	 * Projects an x,y pair from one coordinate system into another.
	 * In this case, no transformation occurs. 
	 */
	@Override
	public double[] project(double x, double y){return new double[]{x,y};}

	/**
	 * Projects a Coordinate from one coordinate system into another.
	 * In this case, no transformation occurs. 
	 */
	@Override
	public Coordinate project(Coordinate c){return c;}

	/**
	 * Projects an array of Coordinates from one coordinate system into another.
	 * In this case, no transformation occurs. 
	 */
	@Override
	public Coordinate[] project(Coordinate[] ca){return ca;}
	
	/**
	 * Projects a LineSegment from one coordinate system into another.
	 * In this case, no transformation occurs. 
	 */
	@Override
	public LineSegment project(LineSegment ls){return ls;}
	
	/**
	 * Reverses the transformation from one coordinate system into another
	 * for an array of doubles.  In this case, no transformation occurs. 
	 */
	@Override
	public double[] inverse(double[] coords){return coords;}

	/**
	 * Reverses the transformation from one coordinate system into another
	 * for an x,y pair.  In this case, no transformation occurs. 
	 */
	@Override
	public double[] inverse(double x, double y){return new double[]{x,y};}

	/**
	 * Reverses the transformation from one coordinate system into another
	 * for a Coordinate.  In this case, no transformation occurs. 
	 */
	@Override
	public Coordinate inverse(Coordinate c){return c;}
	
	/**
	 * Reverses the transformation from one coordinate system into another
	 * for an array of Coordinates.  In this case, no transformation occurs. 
	 */
	@Override
	public Coordinate[] inverse(Coordinate[] ca){return ca;}
	
	/**
	 * Reverses the transformation from one coordinate system into another
	 * for a LineSegment.  In this case, no transformation occurs. 
	 */
	@Override
	public LineSegment inverse(LineSegment ls){return ls;}
}
