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

package au.gov.ga.conn4d.test.impl.collision;


import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.impl.collision.Intersector_3D_Raster;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

public class Intersector_3D_RasterTest {

	Intersector_3D_Raster i3d = new Intersector_3D_Raster();
	
	@Before
	public void setup(){
		
	}
	
	@Test
	public void testIntersect() {
		Coordinate a = new Coordinate(113.25900917753951, -23.12858311295413, -51.019166911993054);
		Coordinate b = new Coordinate(113.25463365242233, -23.1601362814392, -51.02637400036136);
		
		LineSegment ls = new LineSegment(a,b);
		Coordinate[] vertices = new Coordinate[]
		{new Coordinate(113.25261561937987, -23.137438714125906, -318.65625), 
		 new Coordinate(113.26261561878555, -23.137438714125906, -302.859375), 
		 new Coordinate(113.26261561878555, -23.127438714720235, -309.171875), 
		 new Coordinate(113.25261561937987, -23.127438714720235, -327.875)
		};
		
		Coordinate isect = i3d.intersect(ls, vertices);
	}
}
