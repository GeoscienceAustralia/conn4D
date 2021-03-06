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

import java.util.Iterator;
import java.util.List;

import org.opengis.feature.simple.SimpleFeature;

import au.gov.ga.conn4d.Intersector;
import au.gov.ga.conn4d.impl.readers.Shapefile;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Performs basic intersection operations associated with a shapefile.
 * 
 * @author Johnathan Kool
 */

public class Intersector_2D_Shapefile implements Intersector {

	private final long NO_INTERSECTION = Long.MIN_VALUE;
	private Shapefile sh;
	private GeometryFactory gf = new GeometryFactory();

	public Intersector_2D_Shapefile() {
	}

	public Intersector_2D_Shapefile(Shapefile sh) {
		this.sh = sh;
	}

	/**
	 * Intersects the position given by the x,y pair with the shapefile and
	 * returns the long value corresponding to the ID of the intersected
	 * polygon.  If no intersection was detected, the value of NO_INTERSECTION
	 * is returned, currently implemented as Long.MIN_VALUE.
	 */
	
	@Override
	@SuppressWarnings("unchecked")
	public long intersect(double x, double y) {

		if (sh.hasNegLon()) {
			x = cvt(x);
		}

		Point p = gf.createPoint(new Coordinate(x, y));

		List<SimpleFeature> fl = sh.getSpatialIndex().query(
				p.getEnvelopeInternal());

		if (fl.size() == 0) {
			return NO_INTERSECTION;
		}

		SimpleFeature f;

		for (int i = 0; i < fl.size(); i++) {

			f = fl.get(i);
			Geometry g = (Geometry) f.getAttribute(0);
			if (p.intersects(g)) {
				return ((Number) f.getAttribute(sh.getLookupField()))
						.longValue();
			}
		}

		return NO_INTERSECTION;
	}

	/**
	 * Detects whether the line between the two x,y pairs intersects the
	 * shapefile
	 * 
	 * @param x1
	 *            - the initial x coordinate
	 * @param y1
	 *            - the initial y coordinate
	 * @param x2
	 *            - the terminal x coordinate
	 * @param y2
	 *            - the terminal y coordinate
	 */

	@Override
	@SuppressWarnings("unchecked")
	public long intersect(double x1, double y1, double x2, double y2) {

		if (sh.hasNegLon()) {
			x1 = cvt(x1);
			x2 = cvt(x2);
		}

		LineString ls = gf.createLineString(new Coordinate[] {
				new Coordinate(x1, y1), new Coordinate(x2, y2) });
		List<SimpleFeature> fl = sh.getSpatialIndex().query(
				ls.getEnvelopeInternal());

		if (fl.size() == 0) {
			return NO_INTERSECTION;
		}

		SimpleFeature f = fl.get(0);
		Geometry g = (Geometry) f.getAttribute(0);

		if (!ls.intersects(g)) {
			return NO_INTERSECTION;
		}

		return (Integer) f.getAttribute(sh.getLookupField());

	}

	/**
	 * Identifies whether a given x,y position intersects the provided
	 * shapefile.
	 */
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean intersects(double x, double y) {

		if (sh.hasNegLon()) {
			x = ((180 + x) % 360) - 180;
		}
		Point p = gf.createPoint(new Coordinate(x, y));
		List<SimpleFeature> l = sh.getSpatialIndex().query(
				p.getEnvelopeInternal());
		Iterator<SimpleFeature> it = l.iterator();
		while (it.hasNext()) {
			if (((Geometry) it.next().getAttribute(0)).intersects(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Converts longitude values greater than 180 into negative values
	 * 
	 * @param oldlon
	 */
	
	private double cvt(double oldlon) {
		if (oldlon > 180) {
			return -(360d - oldlon);
		} else
			return oldlon;
	}
}
