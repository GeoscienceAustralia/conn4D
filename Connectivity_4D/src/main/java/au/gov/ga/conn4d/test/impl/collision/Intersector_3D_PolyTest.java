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


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.impl.collision.Intersector_3D_Poly;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineSegment;

/**
 * Tests polygon intersections in 3D
 * 
 * @author Johnathan Kool
 */

public class Intersector_3D_PolyTest {

	private Geometry rectangle, rectangle2, rectangle3, rectangle4;
	private LineSegment linex;
	private LineSegment liney;
	private LineSegment linez;
	//private double surfaceLevel = 0;
	private double e = 1E-12;
	Intersector_3D_Poly lsi = new Intersector_3D_Poly();
	GeometryFactory gf = new GeometryFactory();

	@Before
	public void setUp() throws Exception {

		Coordinate c0 = new Coordinate(-1, -1, 0);
		Coordinate c1 = new Coordinate(-1, 1, 0);
		Coordinate c2 = new Coordinate(1, 1, 0);
		Coordinate c3 = new Coordinate(1, -1, 0);

		// Horizontal square
		rectangle = gf.createPolygon(
				gf.createLinearRing(new Coordinate[] { c0, c1, c2, c3, c0 }),
				null);
		
		// Reverse-order vertices
		rectangle2 = gf.createPolygon(
				gf.createLinearRing(new Coordinate[] { c3, c2, c1, c0, c3 }),
				null);
		
		// Vertical square, aligned with y-axis
		rectangle3 = gf.createPolygon(
				gf.createLinearRing(new Coordinate[] {
						new Coordinate(0, -1, -1), new Coordinate(0, -1, 1),
						new Coordinate(0, 1, 1), new Coordinate(0, 1, -1),
						new Coordinate(0, -1, -1) }), null);
		
		// Vertical square, alignes with x-axis
		rectangle4 = gf.createPolygon(
				gf.createLinearRing(new Coordinate[] {
						new Coordinate(-1, 0, -1), new Coordinate(-1, 0, 1),
						new Coordinate(1, 0, 1), new Coordinate(1, 0, -1),
						new Coordinate(-1, 0, -1) }), null);

		linex = new LineSegment(new Coordinate(1, 0, 0), new Coordinate(-1, 0,
				0));
		liney = new LineSegment(new Coordinate(0, 1, 0), new Coordinate(0, -1,
				0));
		linez = new LineSegment(new Coordinate(0, 0, 1), new Coordinate(0, 0,
				-1));
	}
	
	/**
	 * Test rotation about the X axis using a line oriented in the Z direction (vertical).
	 */
	
	@Test
	public void testXAxisRotationLineZ() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateX(linez.p0, fraction),
					rotateX(linez.p1, fraction));
			double x = 0;
			double y = newLine.p1.y;
			double z = -newLine.p1.z;
			LineSegment rfLine = lsi.reflect(newLine, rectangle);
			Assert.assertArrayEquals(new double[] { x, y, z },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	/**
	 * Test rotation about the X axis using a line oriented in the Z direction (vertical)
	 * using vertices in reversed order.
	 */
	
	@Test
	public void testXAxisRotationLineZ_ReversedVertices() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateX(linez.p0, fraction),
					rotateX(linez.p1, fraction));
			double x = 0;
			double y = newLine.p1.y;
			double z = -newLine.p1.z;
			LineSegment rfLine = lsi.reflect(newLine, rectangle2);
			Assert.assertArrayEquals(new double[] { x, y, z },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	/**
	 * Test rotation about the X axis using a line oriented in the Y direction (horizontal).
	 */
	
	@Test
	public void testXAxisRotationLineY() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateX(liney.p0, fraction),
					rotateX(liney.p1, fraction));
			double x = 0;
			double y = -newLine.p1.y;
			double z = newLine.p1.z;
			LineSegment rfLine = lsi.reflect(newLine, rectangle4);
			Assert.assertArrayEquals(new double[] { x, y, z },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	/**
	 * Test rotation about the Y axis using a line oriented in the Z direction (vertical).
	 */
	
	@Test
	public void testYAxisRotationLineZ() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateY(linez.p0, fraction),
					rotateY(linez.p1, fraction));
			double x = newLine.p1.x;
			double y = 0;
			double z = -newLine.p1.z;
			LineSegment rfLine = lsi.reflect(newLine, rectangle);
			Assert.assertArrayEquals(new double[] { x, y, z },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	/**
	 * Test rotation about the Y axis using a line oriented in the Z direction (vertical)
	 * using vertices in reversed order.
	 */
	
	@Test
	public void testYAxisRotationLineZ_ReversedVertices() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateY(linez.p0, fraction),
					rotateY(linez.p1, fraction));
			double x = newLine.p1.x;
			double y = 0;
			double z = -newLine.p1.z;
			LineSegment rfLine = lsi.reflect(newLine, rectangle2);
			Assert.assertArrayEquals(new double[] { x, y, z },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	/**
	 * Test rotation about the Y axis using a line oriented in the X direction (horizontal).
	 */
	
	@Test
	public void testYAxisRotationLineX() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateY(linex.p0, fraction),
					rotateY(linex.p1, fraction));
			double x = -newLine.p1.x;
			double y = 0;
			double z = newLine.p1.z;
			LineSegment rfLine = lsi.reflect(newLine, rectangle3);
			Assert.assertArrayEquals(new double[] { x, y, z },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	/**
	 * Test rotation about the Z axis using a line oriented in the X direction (horizontal).
	 */
	
	@Test
	public void testZAxisRotationLineX() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateZ(liney.p0, fraction),
					rotateZ(liney.p1, fraction));
			double x = newLine.p1.x;
			double y = -newLine.p1.y;
			double z = 0;
			LineSegment rfLine = lsi.reflect(newLine, rectangle4);
			Assert.assertArrayEquals(new double[] { x, y, z },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	/**
	 * Test rotation about the Z axis using a line oriented in the Y direction (horizontal).
	 */
	
	@Test
	public void testZAxisRotationLineY() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateZ(linex.p0, fraction),
					rotateZ(linex.p1, fraction));
			double x = -newLine.p1.x;
			double y = newLine.p1.y;
			double z = 0;
			LineSegment rfLine = lsi.reflect(newLine, rectangle3);
			Assert.assertArrayEquals(new double[] { x, y, z },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	/**
	 * Test for the correct intersection point.
	 */
	
	@Test
	public void testIntersection(){
		Coordinate p0 = new Coordinate(-1,-1,-1);
		Coordinate p1 = new Coordinate(1,-1,-1);
		Coordinate p2 = new Coordinate(1,1,1);
		Coordinate p3 = new Coordinate(-1,1,1);
		Coordinate[] vertices = new Coordinate[] {p0,p1,p2,p3};
		LineSegment ls = new LineSegment(new Coordinate(0,0,1),new Coordinate(0,0,-1));
		Coordinate isect = lsi.intersection(ls, vertices);
		
		Assert.assertArrayEquals(new double[]{0,0,0},new double[]{isect.x,isect.y,isect.z},e);
		
		p0 = new Coordinate(-1,-1,-5);
		p1 = new Coordinate(+1,-1,-5);
		p2 = new Coordinate(+1,+1,-4);
		p3 = new Coordinate(-1,+1,-4);
		vertices = new Coordinate[] {p0,p1,p2,p3};
		ls = new LineSegment(new Coordinate(0,0,1E6),new Coordinate(0,0,-1E6));
		isect = lsi.intersection(ls, vertices);
		
		Assert.assertArrayEquals(new double[]{0,0,-4.5},new double[]{isect.x,isect.y,isect.z},e);
	}
	
	/**
	 * Test rotation about the X axis using a line oriented in the Z direction (vertical)
	 * handling breaching behaviour.
	 */
	
	@Test
	public void testXAxisRotationLineZ_special() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateX(linez.p0, fraction),
					rotateX(linez.p1, fraction));
			double x = 0;
			double y = newLine.p1.y;
			double z = -newLine.p1.z;
			LineSegment rfLine = lsi.reflect_special(newLine, rectangle);
			Assert.assertArrayEquals((new double[] { x, y, Math.min(z, 0) }),
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	/**
	 * Test rotation about the X axis using a line oriented in the Z direction (vertical)
	 * handling breaching behaviour, using reversed polygon vertices.
	 */
	
	@Test
	public void testXAxisRotationLineZ_ReversedVertices_special() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateX(linez.p0, fraction),
					rotateX(linez.p1, fraction));
			double x = 0;
			double y = newLine.p1.y;
			double z = -newLine.p1.z;
			LineSegment rfLine = lsi.reflect_special(newLine, rectangle2);
			Assert.assertArrayEquals(new double[] { x, y, Math.min(z, 0) },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	@Test
	public void testXAxisRotationLineY_special() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateX(liney.p0, fraction),
					rotateX(liney.p1, fraction));
			double x = 0;
			double y = -newLine.p1.y;
			double z = newLine.p1.z;
			LineSegment rfLine = lsi.reflect_special(newLine, rectangle4);
			if(rfLine.p1.z==1.0){
				System.out.println("??");
				LineSegment tmp = lsi.reflect_special(newLine, rectangle4);
				System.out.println(tmp);
			}
			Assert.assertArrayEquals(new double[] { x, y, Math.min(z, 0) },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	@Test
	public void testYAxisRotationLineZ_special() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateY(linez.p0, fraction),
					rotateY(linez.p1, fraction));
			double x = newLine.p1.x;
			double y = 0;
			double z = -newLine.p1.z;
			LineSegment rfLine = lsi.reflect_special(newLine, rectangle);
			Assert.assertArrayEquals(new double[] { x, y, Math.min(z, 0) },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	@Test
	public void testYAxisRotationLineZ_ReversedVertices_special() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateY(linez.p0, fraction),
					rotateY(linez.p1, fraction));
			double x = newLine.p1.x;
			double y = 0;
			double z = -newLine.p1.z;
			LineSegment rfLine = lsi.reflect_special(newLine, rectangle2);
			Assert.assertArrayEquals(new double[] { x, y, Math.min(z, 0) },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	@Test
	public void testYAxisRotationLineX_special() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateY(linex.p0, fraction),
					rotateY(linex.p1, fraction));
			double x = -newLine.p1.x;
			double y = 0;
			double z = newLine.p1.z;
			LineSegment rfLine = lsi.reflect_special(newLine, rectangle3);
			Assert.assertArrayEquals(new double[] { x, y, Math.min(z,0) },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	@Test
	public void testZAxisRotationLineX_special() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateZ(liney.p0, fraction),
					rotateZ(liney.p1, fraction));
			double x = newLine.p1.x;
			double y = -newLine.p1.y;
			double z = 0;
			LineSegment rfLine = lsi.reflect_special(newLine, rectangle4);
			Assert.assertArrayEquals(new double[] { x, y, Math.min(z, 0) },
					c2arr(rfLine.p1), 1E-6);
		}
	}
	
	@Test
	public void testZAxisRotationLineY_special() {
		int n = 100;
		for (int i = 0; i < n; i++) {
			double fraction = (double) i / (double) n;
			LineSegment newLine = new LineSegment(
					rotateZ(linex.p0, fraction),
					rotateZ(linex.p1, fraction));
			double x = -newLine.p1.x;
			double y = newLine.p1.y;
			double z = 0;
			LineSegment rfLine = lsi.reflect_special(newLine, rectangle3);
			Assert.assertArrayEquals(new double[] { x, y, Math.min(z, 0) },
					c2arr(rfLine.p1), 1E-6);
		}
	}
		
	@Test
	public void testIntersection_special(){
		Coordinate p0 = new Coordinate(-1,-1,-1);
		Coordinate p1 = new Coordinate(1,-1,-1);
		Coordinate p2 = new Coordinate(1,1,1);
		Coordinate p3 = new Coordinate(-1,1,1);
		Coordinate[] vertices = new Coordinate[] {p0,p1,p2,p3};
		LineSegment ls = new LineSegment(new Coordinate(0,0,1),new Coordinate(0,0,-1));
		Coordinate isect = lsi.intersection(ls, vertices);
		
		Assert.assertArrayEquals(new double[]{0,0,0},new double[]{isect.x,isect.y,isect.z},e);
		
		p0 = new Coordinate(-1,-1,-5);
		p1 = new Coordinate(+1,-1,-5);
		p2 = new Coordinate(+1,+1,-4);
		p3 = new Coordinate(-1,+1,-4);
		vertices = new Coordinate[] {p0,p1,p2,p3};
		ls = new LineSegment(new Coordinate(0,0,1E6),new Coordinate(0,0,-1E6));
		isect = lsi.intersection(ls, vertices);
		
		Assert.assertArrayEquals(new double[]{0,0,-4.5},new double[]{isect.x,isect.y,isect.z},e);
	}
	
	

	private double[] c2arr(Coordinate c) {
		return new double[] { c.x, c.y, c.z };
	}

	// Formula from ArcGIS
	public double getSlope(Coordinate[] c) {
		double dzdx = Math.abs(c[0].z - c[1].z) / Math.abs(c[0].x - c[1].x);
		double dzdy = Math.abs(c[1].z - c[2].z) / Math.abs(c[1].y - c[2].y);
		return (Math.atan(Math.sqrt((dzdx * dzdx) + (dzdy * dzdy))))
				* (180 / Math.PI);
	}

	// Formula from ArcGIS
	public double getAspect(Coordinate[] c) {
		double dzdx = Math.abs(c[0].z - c[1].z) / Math.abs(c[0].x - c[1].x);
		double dzdy = Math.abs(c[1].z - c[2].z) / Math.abs(c[1].y - c[2].y);
		double aspect = 180 / Math.PI * Math.atan2(dzdy, -dzdx);
		if (aspect < 0)
			return 90.0 - aspect;
		else if (aspect > 90.0)
			return 360.0 - aspect + 90.0;
		else
			return 90.0 - aspect;
	}

	private Coordinate rotateX(Coordinate c, double fraction) {
		double q = fraction * 2 * Math.PI;
		double y = c.y * Math.cos(q) - c.z * Math.sin(q);
		double z = c.y * Math.sin(q) + c.z * Math.cos(q);
		return new Coordinate(c.x, y, z);
	}

	private Coordinate rotateY(Coordinate c, double fraction) {
		double q = fraction * 2 * Math.PI;
		double z = c.z * Math.cos(q) - c.x * Math.sin(q);
		double x = c.z * Math.sin(q) + c.x * Math.cos(q);
		return new Coordinate(x, c.y, z);
	}

	private Coordinate rotateZ(Coordinate c, double fraction) {
		double q = fraction * 2 * Math.PI;
		double x = c.x * Math.cos(q) - c.y * Math.sin(q);
		double y = c.y * Math.sin(q) + c.y * Math.cos(q);
		return new Coordinate(x, y, c.z);
	}

/*	private Coordinate[] rotateX(Coordinate[] ca, double fraction) {
		Coordinate[] out = new Coordinate[ca.length];
		for (int i = 0; i < ca.length; i++) {
			out[i] = rotateX(ca[i], fraction);
		}
		return out;
	}

	private Coordinate[] rotateY(Coordinate[] ca, double fraction) {
		Coordinate[] out = new Coordinate[ca.length];
		for (int i = 0; i < ca.length; i++) {
			out[i] = rotateY(ca[i], fraction);
		}
		return out;
	}

	private Coordinate[] rotateZ(Coordinate[] ca, double fraction) {
		Coordinate[] out = new Coordinate[ca.length];
		for (int i = 0; i < ca.length; i++) {
			out[i] = rotateZ(ca[i], fraction);
		}
		return out;
	}*/
}
