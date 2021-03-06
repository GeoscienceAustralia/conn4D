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

package au.gov.ga.conn4d.impl.collision;

import au.gov.ga.conn4d.utils.CoordinateMath;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

/**
 * Performs intersection and reflection operations associated with a raster
 * surface.
 * 
 * @author Johnathan Kool
 * 
 */

public class Intersector_3D_Raster {

	private Intersector_3D_Poly i3p = new Intersector_3D_Poly();
	private double surfaceLevel = 0;

	/**
	 * Returns the intersection point of a line segment with the plane defined
	 * by the given vertices. The intersection point must lie within the
	 * vertices, otherwise a NaN Coordinate is returned.
	 * 
	 * @param ls
	 * @param vertices
	 */

	public Coordinate intersect(LineSegment ls, Coordinate[] vertices) {
		// Better criteria might be combined distance to the centroid?
		// double d1 = CoordinateMath.magnitude(CoordinateMath.subtract(
		// vertices[0], vertices[2]));
		// double d2 = CoordinateMath.magnitude(CoordinateMath.subtract(
		// vertices[1], vertices[3]));

		double d1 = 0;
		double d2 = 0;

		d1 = CoordinateMath.magnitude(CoordinateMath.subtract(vertices[0],
				vertices[2]));
		d2 = CoordinateMath.magnitude(CoordinateMath.subtract(vertices[1],
				vertices[3]));

		Coordinate[] t1, t2;

		if (d2 > d1) {
			t1 = new Coordinate[] { vertices[0], vertices[1], vertices[3] };
			t2 = new Coordinate[] { vertices[1], vertices[2], vertices[3] };
		} else {
			t1 = new Coordinate[] { vertices[0], vertices[1], vertices[2] };
			t2 = new Coordinate[] { vertices[0], vertices[2], vertices[3] };
		}

		Coordinate isect1 = i3p.intersection(ls, t1);
		Coordinate isect2 = i3p.intersection(ls, t2);

		if (isect1 == getNaN() && isect2 == getNaN()) {
			return isect1;
		}

		if (isect1 == getNaN()) {
			return isect2;
		}
		if (isect2 == getNaN()) {
			return isect1;
		}

		if (isect1.distance(ls.p0) < isect2.distance(ls.p0)) {
			return isect1;
		}
		return isect2;
	}

	/**
	 * Performs a reflection of the line segment off the surface given by the
	 * vertices. The vertices must contain the intersection point to generate a
	 * reflection. During model operation, reflection is currently handled by
	 * the CoordinateMath class.
	 * 
	 * @param ls
	 * @param vertices
	 */

	public LineSegment reflect(LineSegment ls, Coordinate[] vertices) {
		
		// Better criteria might be combined distance to the centroid?
		
		double d1 = CoordinateMath.magnitude(CoordinateMath.subtract(
				vertices[0], vertices[2]));
		double d2 = CoordinateMath.magnitude(CoordinateMath.subtract(
				vertices[1], vertices[3]));

		Coordinate[] t1, t2;

		if (d2 > d1) {
			t1 = new Coordinate[] { vertices[0], vertices[1], vertices[3] };
			t2 = new Coordinate[] { vertices[1], vertices[2], vertices[3] };
		} else {
			t1 = new Coordinate[] { vertices[0], vertices[1], vertices[2] };
			t2 = new Coordinate[] { vertices[0], vertices[2], vertices[3] };
		}

		LineSegment ls1 = i3p.reflect(ls, t1);
		LineSegment ls2 = i3p.reflect(ls, t2);

		boolean ls1_change = ls1.p1 != ls.p1;
		boolean ls2_change = ls2.p1 != ls.p1;

		// If a reflection occurred off of both triangles, then test to see
		// which intersection point is closer to the beginning of the line.

		if (ls1_change && ls2_change) {
			if (ls2.p0.distance(ls.p0) > ls1.p0.distance(ls.p0)) {
				return ls1;
			}
			return ls2;
		}
		// Otherwise pick one, the other, or neither
		else {
			if (ls1_change) {
				return ls1;
			}
			if (ls2_change) {
				return ls2;
			}
			return ls;
		}
	}

	/**
	 * Performs a reflection of the line segment off the surface given by the
	 * vertices. The vertices must contain the intersection point to generate a
	 * reflection. During model operation, reflection is currently handled by
	 * the CoordinateMath class. This method provides additional functionality
	 * by bounding using surfaceLevel. i.e. values will be constrained to be
	 * below surfaceLevel.
	 * 
	 * @param ls
	 *            - the LineSegment indicating the path of an object.
	 * @param vertices
	 *            - Coordinates indicating vertices of a flat polygon
	 */

	public LineSegment reflect_special(LineSegment ls, Coordinate[] vertices) {
		// Better criteria might be combined distance to the centroid?
		double d1 = CoordinateMath.magnitude(CoordinateMath.subtract(
				vertices[0], vertices[2]));
		double d2 = CoordinateMath.magnitude(CoordinateMath.subtract(
				vertices[1], vertices[3]));

		Coordinate[] t1, t2;

		if (d2 > d1) {
			t1 = new Coordinate[] { vertices[0], vertices[1], vertices[3] };
			t2 = new Coordinate[] { vertices[1], vertices[2], vertices[3] };
		} else {
			t1 = new Coordinate[] { vertices[0], vertices[1], vertices[2] };
			t2 = new Coordinate[] { vertices[0], vertices[2], vertices[3] };
		}

		// If z is greater, special reflection rotates the vector away
		// from the reflection surface until z = 0.

		LineSegment ls1 = i3p.reflect_special(ls, t1, surfaceLevel);
		LineSegment ls2 = i3p.reflect_special(ls, t2, surfaceLevel);

		boolean ls1_change = ls1.p1 != ls.p1;
		boolean ls2_change = ls2.p1 != ls.p1;

		// If a reflection occurred off of both triangles, then test to see
		// which intersection point is closer to the beginning of the line.

		if (ls1_change && ls2_change) {
			if (ls2.p0.distance(ls.p0) > ls1.p0.distance(ls.p0)) {
				return ls1;
			}
			return ls2;
		}
		// Otherwise pick one, the other, or neither
		else {
			if (ls1_change) {
				return ls1;
			}
			if (ls2_change) {
				return ls2;
			}
			return ls;
		}
	}

	/**
	 * Returns the NaN Coordinate value being used by this class.
	 * 
	 * @return - A Coordinate containing NaN values
	 */

	public Coordinate getNaN() {
		return i3p.getNaN();
	}

	/**
	 * Retrieves the surface level being used as a maximum value by the class
	 */

	public double getSurfaceLevel() {
		return surfaceLevel;
	}

	/**
	 * Sets the surface level being used as a maximum value by the class
	 * 
	 * @param surface
	 */

	public void setSurfaceLevel(double surface) {
		this.surfaceLevel = surface;
	}
}
