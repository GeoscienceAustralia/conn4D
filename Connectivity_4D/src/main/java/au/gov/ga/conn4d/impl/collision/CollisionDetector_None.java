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

package au.gov.ga.conn4d.impl.collision;

import au.gov.ga.conn4d.Boundary;
import au.gov.ga.conn4d.CollisionDetector;
import au.gov.ga.conn4d.Particle;

/**
 * Collision Detector implementation that performs no actions.  Primarily used
 * for debugging and testing.
 * 
 * @author Johnathan Kool
 *
 */

public class CollisionDetector_None implements CollisionDetector, Cloneable {

	/**
	 * Performs actions that relocate a Particle upon encountering a barrier.
	 */
	@Override
	public void handleIntersection(Particle p) {}
	
	/**
	 * Generates a clone of the CollisionDetection instance.
	 */
	@Override
	public CollisionDetector_None clone(){
		return this;
	}
	
	/**
	 * Identifies whether a 4-dimensional coordinate (x,y,z,t) is within bounds
	 * or not.
	 */
	@Override
	public boolean isInBounds(long t, double z, double x, double y){return true;}
	
	/**
	 * Returns the Boundary object associated with this Class.
	 */
	@Override
	public Boundary getBoundary(){return null;}
}
