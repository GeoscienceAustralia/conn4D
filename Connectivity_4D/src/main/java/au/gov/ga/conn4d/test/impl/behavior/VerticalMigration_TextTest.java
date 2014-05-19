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

import junit.framework.TestCase;

import org.junit.Before;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.impl.behavior.VerticalMigration_Text;

/**
 * Tests vertical migration behaviour based on an input text file.
 */

public class VerticalMigration_TextTest extends TestCase {

	VerticalMigration_Text vm;
	Particle p;
	double [][] m1 = {	{ 0, .05}, 
						{.2, .1}, 
						{.2, .2}, 
						{.2, .3}, 
						{.2, .2}, 
						{.2, .1}, 
						{.0, .05}};
	double[] bins = {5,15,25,40,62.5,87.5,112.5,137.5};
	
	/**
	 * Sets up the test class
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		p = new Particle();
		p.setZ(5);
		vm = new VerticalMigration_Text();
		vm.setTimeInterval(Long.MAX_VALUE);
		vm.setTimeIntervalUnits("Days");
		vm.setVmtx(m1);
	}
	
	/**
	 * Incomplete
	 */
	
	//@Test
	public void testMovement() {}
}
