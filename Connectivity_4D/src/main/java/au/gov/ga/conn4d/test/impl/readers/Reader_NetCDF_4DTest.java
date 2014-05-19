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

import au.gov.ga.conn4d.impl.readers.Reader_NetCDF_4D;

public class Reader_NetCDF_4DTest {

	Reader_NetCDF_4D n4;
	String filename = "Z:/NetCDF/AUS_u_2005.nc";
	String varName = "u";
	String timeName = "Time";
	String depthName = "Depth";
	String latName = "Latitude";
	String lonName = "Longitude";
	
	@Before
	public void setUp() throws Exception {
		n4 = new Reader_NetCDF_4D(filename, varName, timeName, depthName, latName, lonName);
		n4.initialize();
	}
	
	@Test
	public void test() {
		assertEquals(0.5307697057723999,n4.getValue(37985, 0, 100, -50),1E-4);
		assertEquals(0.4407572150230407,n4.getValue(37985, 0, 100.08, -50),1E-4);
		assertEquals(0.4407572150230407,n4.getValue(37985, 0, 100.04, -50),1E-4);
		assertEquals(0.5307697057723999,n4.getValue(37985.25, 0, 100, -50),1E-4);
		assertEquals(0.5922527313232422,n4.getValue(37986, 0, 100, -50),1E-4);
		assertEquals(0.5922527313232422,n4.getValue(37985.5, 0, 100, -50),1E-4);
	}
}
