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

/**
* Intersector: Performs intersection operations for coordinates against a 
*              spatial layer.
*/

public interface Intersector {
	public final long NO_INTERSECTION = Long.MIN_VALUE;

	/**
	 * Retrieves a long value representing the feature intersecting the point
	 * given by the x and y coordinate values. If the coordinates do not
	 * intersect an object, NO_INTERSECTION is returned as a value.
	 * 
	 * @param x - x position
	 * @param y - y position
	 */

	public long intersect(double x, double y);

	/**
	 * Retrieves a long value representing the feature intersecting the line
	 * given by the x and y coordinate values. If the coordinates do not
	 * intersect an object, NO_INTERSECTION is returned as a value.
	 * 
	 * @param x1 - initial x position
	 * @param y1 - initial y position
	 * @param x2 - end x position
	 * @param y2 - end y position
	 */

	public long intersect(double x1, double y1, double x2, double y2);

	/**
	 * Identifies whether the given coordinates intersect any features in the
	 * spatial layer.
	 * 
	 * @param x - x position
	 * @param y - y position
	 */

	public boolean intersects(double x, double y);
}
