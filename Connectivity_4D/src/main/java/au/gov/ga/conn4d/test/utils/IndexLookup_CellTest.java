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

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import au.gov.ga.conn4d.utils.IndexLookup_Cell;

public class IndexLookup_CellTest{
	
	NetcdfFile ncFile;
	IndexLookup_Cell loc;
	Variable var;
	
	@Test
	public void testLocate() {
		
		try {
			ncFile = NetcdfFile.open("./files/xmap.nc");
		} catch (IOException e) {
			e.printStackTrace();
		}
		var = ncFile.findVariable("Longitude");
		loc = new IndexLookup_Cell(var);
		loc.setMinval(-1.005);
		loc.setMaxval(1.005);
		Assert.assertEquals(-1,loc.lookup(-8));
		Assert.assertEquals(-1,loc.lookup(-1.005));
		Assert.assertEquals(0,loc.lookup(-1.004));
		Assert.assertEquals(0,loc.lookup(-1.003));
		Assert.assertEquals(0,loc.lookup(-1.0));
		Assert.assertEquals(0,loc.lookup(-0.999));
		Assert.assertEquals(0,loc.lookup(-0.996));
		Assert.assertEquals(1,loc.lookup(-0.995));
		Assert.assertEquals(1,loc.lookup(-0.994));
		Assert.assertEquals(1,loc.lookup(-0.99));
		Assert.assertEquals(100,loc.lookup(0));
		Assert.assertEquals(200,loc.lookup(0.999));
		Assert.assertEquals(200,loc.lookup(1));
		Assert.assertEquals(201,loc.lookup(1.05));
		Assert.assertEquals(201,loc.lookup(1.07));
		Assert.assertEquals(201,loc.lookup(18));
	}
}
