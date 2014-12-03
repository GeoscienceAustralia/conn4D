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

package au.gov.ga.conn4d.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

public class PrjTransform_WGS2CEQD implements PrjTransform{

	private final static double R_EARTH = 6371009; // IUGG recommended value

	@Override
	public double[] project(double[] coords) {
		return function(coords);
	}

	@Override
	public double[] project(double x, double y) {
		return function(new double[] { x, y });
	}

	@Override
	public Coordinate project(Coordinate c) {
		if (c==null){return null;}
		double[] tmp = function(new double[] { c.x, c.y });
		return new Coordinate(tmp[0], tmp[1], c.z);
	}
	
	@Override
	public Coordinate[] project(Coordinate[] ca){
		if(ca==null){return null;}
		Coordinate[] out = new Coordinate[ca.length];
		for(int i = 0; i < ca.length; i++){
			out[i] = project(ca[i]);
		}
		return out;
	}
	
	@Override
	public LineSegment project(LineSegment ls){
		if(ls==null){return null;}
		return new LineSegment(project(ls.p0),project(ls.p1));	
	}
	
	@Override
	public double[] inverse(double[] coords) {
		return function_inv(coords);
	}

	@Override
	public double[] inverse(double x, double y) {
		return function_inv(new double[] { x, y });
	}

	@Override
	public Coordinate inverse(Coordinate c) {
		if(c==null){return null;}
		double[] tmp = function_inv(new double[] { c.x, c.y });
		return new Coordinate(tmp[0], tmp[1], c.z);
	}
	
	@Override
	public Coordinate[] inverse(Coordinate[] ca){
		if(ca==null){return null;}
		Coordinate[] out = new Coordinate[ca.length];
		for(int i = 0; i < ca.length; i++){
			out[i] = inverse(ca[i]);
		}
		return out;
	}

	
	@Override
	public LineSegment inverse(LineSegment ls){
		if(ls==null){return null;}
		return new LineSegment(inverse(ls.p0),inverse(ls.p1));	
	}
	private double[] function(double[] coords) {
		if(coords==null){return null;}
		double latitude_origin = Math.toRadians(0);
		double central_meridian = Math.toRadians(0);
		double longitude = Math.toRadians(coords[0]);
		double latitude = Math.toRadians(coords[1]);
		double[] out = new double[coords.length];
		out[1] = R_EARTH * latitude;
		out[0] = R_EARTH * (longitude - central_meridian)
				* Math.cos(latitude_origin);
		if (coords.length == 3) {
			out[2] = coords[2];
		}
		return out;
	}

	// Cylindrical Equidistant Meters to longitude and latitude

	private double[] function_inv(double[] coords) {
		if(coords==null){return null;}
		double latitude_origin = Math.toRadians(0);
		double central_meridian = Math.toRadians(0);
		double[] out = new double[coords.length];
		out[1] = Math.toDegrees(coords[1] / R_EARTH);
		out[0] = Math.toDegrees(central_meridian
				+ (coords[0] / (R_EARTH * Math.cos(latitude_origin))));
		if (coords.length == 3) {
			out[2] = coords[2];
		}
		return out;
	}
}
