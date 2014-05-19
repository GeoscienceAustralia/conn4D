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

package au.gov.ga.conn4d.test.impl.readers;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.impl.readers.Reader_NetCDF_3D;

public class Reader_NetCDF_3DTest {

	Reader_NetCDF_3D n3;
	String filename = "Z:/NetCDF/AUS_u_2005.nc";
	String varName = "mld";
	String timeName = "Time";
	String latName = "Latitude";
	String lonName = "Longitude";
	
	@Before
	public void setUp() throws Exception {
		n3 = new Reader_NetCDF_3D(filename, varName, timeName, latName, lonName);
	}
	
	@Test
	public void test() {
		assertEquals(23.5252,n3.getValue(39448, 100, -50),1E-4);
		assertEquals(24.2172,n3.getValue(39448, 100.08, -50),1E-4);
		assertEquals(24.2172,n3.getValue(39448, 100.04, -50),1E-4);
		assertEquals(23.5252,n3.getValue(39448.25, 100, -50),1E-4);
		assertEquals(62.4045,n3.getValue(39449, 100, -50),1E-4);
		assertEquals(62.4045,n3.getValue(39448.5, 100, -50),1E-4);
	}
}
