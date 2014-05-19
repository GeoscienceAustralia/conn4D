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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.impl.behavior.Mortality_None;

/**
 * Performs testing for the Mortality_None class (no mortality)
 */

public class Mortality_NoneTest {

	Mortality_None mn = new Mortality_None();
	Particle p1;
	Particle p2;
	
	/**
	 * Setup procedures
	 */
	
	@Before
	public void setUp(){
		p1 = new Particle();
		p1.setBirthday(12345);
		p1.setComments("ALL UR BASE R BLONG 2 US");
		p1.setCompetencyStart(54321);
		p1.setDead(false);
		p1.setError(false);
		p1.setDistance(Math.PI);
		p1.setFinished(true);
		p1.setI(0);
		p1.setJ(1);
		p1.setK(2);
		p1.setLost(false);
		p1.setNearNoData(false);
		p1.setNodata(true);
		p1.setT(88);
		p1.setX(12397.123);
		p1.setY(2149234.123);
		p1.setZ(1E-32);
		p1.setPX(2342.23);
		p1.setPY(Math.E);
		p1.setZ(Double.POSITIVE_INFINITY);
		p1.setRecording(true);
		p1.setSettling(true);
		p1.setU(1.0);
		p1.setV(-1.0);
		p1.setW(0);
		p2 = p1.clone();
	}
	
	/**
	 * Ensures that all individuals are retained following mortality
	 */
	
	@Test
	public void test() {
		mn.apply(p2);
		Assert.assertTrue(p1.deepEquals(p2));
		mn.setTimeInterval(1000);
		mn.apply(p2);
		Assert.assertTrue(p1.deepEquals(p2));
	}
}
