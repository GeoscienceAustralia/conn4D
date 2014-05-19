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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.conn4d.utils.ReferenceGrid;

import com.vividsolutions.jts.geom.Coordinate;

public class ReferenceGridTest {
	ReferenceGrid rg = new ReferenceGrid(0,0,1);
	
	@Test
	public void testIsOnVerticalEdge(){
		Assert.assertTrue(rg.isOnVerticalEdge(new Coordinate(0,0)));
		Assert.assertTrue(rg.isOnVerticalEdge(new Coordinate(0,0.5)));
		Assert.assertTrue(rg.isOnVerticalEdge(new Coordinate(1,0.5)));
		Assert.assertFalse(rg.isOnVerticalEdge(new Coordinate(0.5,0)));
		Assert.assertFalse(rg.isOnVerticalEdge(new Coordinate(0.2,0.2)));
	}
	
	@Test
	public void testIsOnHorizontalEdge(){
		Assert.assertTrue(rg.isOnHorizontalEdge(new Coordinate(0,0)));
		Assert.assertFalse(rg.isOnHorizontalEdge(new Coordinate(0,0.5)));
		Assert.assertTrue(rg.isOnHorizontalEdge(new Coordinate(0.5,1)));
		Assert.assertTrue(rg.isOnHorizontalEdge(new Coordinate(0.5,0)));
		Assert.assertFalse(rg.isOnHorizontalEdge(new Coordinate(0.2,0.2)));
	}
	
	@Test
	public void testIsOnCorner(){
		Assert.assertTrue(rg.isOnCorner(new Coordinate(0,0)));
		Assert.assertFalse(rg.isOnCorner(new Coordinate(0,0.5)));
		Assert.assertFalse(rg.isOnCorner(new Coordinate(0.5,1)));
		Assert.assertTrue(rg.isOnCorner(new Coordinate(5,7)));
		Assert.assertFalse(rg.isOnCorner(new Coordinate(0.2,0.2)));
	}
	
	@Test
	public void testGetCellList(){
		Coordinate c = new Coordinate(1.5,0);
		List<int[]> l = rg.getCellList(c);
		Assert.assertEquals(l.size(), 2);
		Assert.assertArrayEquals(l.get(0),new int[]{1,0});
		Assert.assertArrayEquals(l.get(1),new int[]{1,-1});
		
		c = new Coordinate(0,1.5);
		l = rg.getCellList(c);
		Assert.assertEquals(l.size(), 2);
		Assert.assertArrayEquals(l.get(0),new int[]{0,1});
		Assert.assertArrayEquals(l.get(1),new int[]{-1,1});
		
		c = new Coordinate(1,1);
		l = rg.getCellList(c);
		Assert.assertEquals(l.size(), 4);
		Assert.assertArrayEquals(l.get(0),new int[]{1,1});
		Assert.assertArrayEquals(l.get(1),new int[]{1,0});
		Assert.assertArrayEquals(l.get(2),new int[]{0,1});
		Assert.assertArrayEquals(l.get(3),new int[]{0,0});
	}
}
