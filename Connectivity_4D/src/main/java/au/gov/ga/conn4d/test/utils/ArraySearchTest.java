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

package au.gov.ga.conn4d.test.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.utils.ArraySearch;

public class ArraySearchTest {

	private long[] la_fwd = new long[100];
	private long[] la_rev = new long[100];
	private int[] ia_fwd = new int[100];
	private int[] ia_rev = new int[100];
	private double[] da_fwd = new double[100];
	private double[] da_rev = new double[100];
	private float[] fa_fwd = new float[100];
	private float[] fa_rev = new float[100];
	private short[] sa_fwd = new short[100];
	private short[] sa_rev = new short[100];
	private char[] ca_fwd = new char[100];
	private char[] ca_rev = new char[100];
	
	@Before
	public void setUp() throws Exception {
		for(int i = 0; i < la_fwd.length; i++){
			la_fwd[i] = i;
			ia_fwd[i] = i;
			da_fwd[i] = i;
			fa_fwd[i] = i;
			sa_fwd[i] = (short) i;
			ca_fwd[i] = Character.toChars(i)[0];
			
		}
		for(int i = 0; i < la_rev.length; i++){
			la_rev[i] = -i;
			ia_rev[i] = -i;
			da_rev[i] = -i;
			fa_rev[i] = -i;
			sa_rev[i] = (short) -i;
			ca_rev[i] = Character.toChars(-i+100)[0];
		}
	}
	
	@Test
	public void testLong() {
		assertEquals(ArraySearch.reverseSearch(la_rev, -80),80);
		assertEquals(ArraySearch.reverseSearch(la_rev, -20),20);
	}
	
	@Test
	public void testInt() {;
		assertEquals(ArraySearch.reverseSearch(ia_rev, -80),80);
		assertEquals(ArraySearch.reverseSearch(ia_rev, -20),20);
	}
	
	@Test
	public void testDouble() {
		assertEquals(ArraySearch.reverseSearch(da_rev, -80),80);
		assertEquals(ArraySearch.reverseSearch(da_rev, -20),20);
	}
	
	@Test
	public void testFloat() {
		assertEquals(ArraySearch.reverseSearch(fa_rev, -80),80);
		assertEquals(ArraySearch.reverseSearch(fa_rev, -20),20);
	}
}
