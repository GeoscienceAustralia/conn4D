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

package au.gov.ga.conn4d.impl.behavior;

import au.gov.ga.conn4d.Intersector;
import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.Settlement;
import au.gov.ga.conn4d.impl.readers.Shapefile;

/**
 * 
 * Provides behavior that relates to settling (e.g. writing position) without
 * terminating the progress of Particles.
 * 
 * @author Johnathan Kool
 * 
 */

public class Settlement_FloatOver implements Settlement, Cloneable {

	private Shapefile settlementPolys;
	private Intersector isect;

	/**
	 * Performs actions associated with settling without terminating
	 * the pilgrim's progress.
	 */
	
	@Override
	public synchronized void apply(Particle p) {

		if (p.getCompetencyStart() >= 0 && p.getAge() >= p.getCompetencyStart()) {

			long ivalue = isect.intersect(p.getX(), p.getY());
			if (ivalue != Intersector.NO_INTERSECTION) {

				// We have encountered a suitable polygon.
				// Set the destination value
				p.setDestination(Long.toString(ivalue));
				p.setSettling(true);
			} else {
				p.setSettling(false);
			}
		}
		this.notifyAll();
	}

	/**
	 * Returns a copy of the class instance
	 */

	@Override
	public Settlement_FloatOver clone() {
		Settlement_FloatOver sf = new Settlement_FloatOver();
		sf.setSettlementPolys(settlementPolys);
		return sf;
	}

	/**
	 * Sets the type of Intersector being used to identify intersections between
	 * Particles/Particle Paths and the settlement polygons
	 */

	@Override
	public void setIntersector(Intersector isect) {
		this.isect = isect;
	}

	/**
	 * Sets the shapefile describing the settlement areas.
	 * 
	 * @param settlementPolys
	 */

	public void setSettlementPolys(Shapefile settlementPolys) {
		this.settlementPolys = settlementPolys;
	}
}
