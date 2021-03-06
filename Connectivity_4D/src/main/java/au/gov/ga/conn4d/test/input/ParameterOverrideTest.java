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

package au.gov.ga.conn4d.test.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.input.ModelParameters;
import au.gov.ga.conn4d.parameters.Parameters_Test;

public class ParameterOverrideTest {
	
	ModelParameters po = new ModelParameters(); 
	
	@Test
	public void testReadFile() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		po.readFile("./files/test.prm");
		//assertEquals(baos.toString(),"Invalid parameter value provided for variable centroid, setting to false");
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
		assertFalse(po.centroid);
		assertEquals(po.competencyStart,"31");
		assertEquals(po.competencyStartUnits,"Seconds");
		assertEquals(po.diffusionType,"Test");
		assertFalse(po.effectiveMigration);
		assertEquals(po.h,"1");
		assertEquals(po.hUnits,"Minute");
		assertEquals(po.initialPositionType,"Test");
		assertEquals(po.minTime,"01/01/1999");
		assertEquals(po.minTimeUnits,"Date");
		assertEquals(po.maxTime,"12/31/2002");
		assertEquals(po.minTimeUnits,"Date");
		
		Parameters prm = new Parameters_Test();
		po.setParameters(prm);
	}
}
