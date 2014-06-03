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

package au.gov.ga.conn4d.test.impl.movement;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

import au.gov.ga.conn4d.Particle;

import au.gov.ga.conn4d.impl.movement.Diffusion_Simple_3D;

public class Diffusion_Simple_3DTest {

	Diffusion_Simple_3D ds3 = new Diffusion_Simple_3D();
	int n = 10000;

	@Test
	public void test() {

		Coordinate[] ca = new Coordinate[n];
		double[] xa = new double[n];
		double[] ya = new double[n];
		double[] za = new double[n];

		for (int i = 0; i < n; i++) {
			Particle p = new Particle();

			for (int j = 0; j < 10000; j++) {
				ds3.apply(p);
			}
			ca[i] = new Coordinate(p.getX(), p.getY(), p.getZ());
			xa[i] = p.getX();
			ya[i] = p.getY();
			za[i] = p.getZ();
		}
		
		//for(int i = 0; i < n; i++){
		//	System.out.println(xa[i]);
		//}
		
		//KolmogorovSmirnovTest kst = new KolmogorovSmirnovTest();
		//System.out.println(Arrays.toString(xa));
		//System.out.println(kst.kolmogorovSmirnovTest(new NormalDistribution(0,?),xa,0.05));
		
	}
}
