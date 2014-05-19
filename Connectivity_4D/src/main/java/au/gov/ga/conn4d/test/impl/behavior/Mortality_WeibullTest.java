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

package au.gov.ga.conn4d.test.impl.behavior;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.impl.behavior.Mortality_Weibull;
import au.gov.ga.conn4d.utils.TimeConvert;

/**
 * Performs testing for the Mortality_Weibull class
 */

public class Mortality_WeibullTest {

	private Mortality_Weibull mw;
	private final double reps = 1E6;

	/**
	 * Setup procedures
	 */
	
	@Before
	public void setUp() {
		mw = new Mortality_Weibull(0, 1);
	}

	/**
	 * Prints out values for the number of specified reps (1E6). Values should
	 * conform to an exponential distribution curve. Carrying out a unit test
	 * with guaranteed results is difficult due to the stochastic nature of the
	 * function.
	 */

	@Test
	public void testApplyParticle() {

		Particle p = new Particle();

		// Mortality is originally 0. No Particles should die on the first pass.
		for (int i = 0; i < reps; i++) {
			mw.apply(p);
			if (p.isDead()) {
				fail();
			}
		}

		mw.setDelta_t(TimeConvert.convertToMillis("Days", 1));
		mw.setK(1);
		mw.setLambda(1);
		mw.setTimeInterval(TimeConvert.convertToMillis("Days", 1));

		long sum = 0;

		for (int i = 0; i < reps; i++) {
			p = new Particle();
			mw.apply(p);
			if (!p.isDead()) {
				sum++;
			}
		}

		double p_surv = (double) sum / reps;
		assertEquals(p_surv,0.367879,1E-2);
	}

	/**
	 * Performs testing of the clone operation
	 */
	
	@Test
	public void testClone() {
		Mortality_Weibull clone = mw.clone();
		assertFalse(clone == mw);
		assertTrue(clone.getK() == mw.getK());
		assertTrue(clone.getLambda() == mw.getLambda());
		assertTrue(clone.getTimeIntervalMillis() == mw.getTimeIntervalMillis());
	}
}
