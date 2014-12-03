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

/**
 * Utilities for determining change in position and distance along a sphere.
 * 
 * @author Johnathan Kool - modified from code developed by Ashwanth Srinivasan.
 * 
 */

public class GeometryUtils {

	//private final static double R_EARTH = 6378137d; //WGS84 radius (max value)
	private final static double R_EARTH = 6371009; // IUGG recommended value
	private final static double REINV = 1d / R_EARTH;// Inverse Radius of the Earth

	/**
	 * Calculates the distance traveled along a sphere (great circle distance)
	 * 
	 * @param rlon1 -
	 *            The longitude of origin
	 * @param rlat1 -
	 *            The latitude of origin
	 * @param rlon2 -
	 *            The destination longitude
	 * @param rlat2 -
	 *            The destination latitude.
	 * @return - Distance traveled in meters.
	 */

	public static double distance_Sphere(double rlon1, double rlat1,
			double rlon2, double rlat2) {

		double rln1, rlt1, rln2, rlt2;
		double dist;

		rln1 = Math.toRadians(rlon1);
		rlt1 = Math.toRadians(rlat1);
		rln2 = Math.toRadians(rlon2);
		rlt2 = Math.toRadians(rlat2);

		double d_lambda = Math.abs(rln1 - rln2);

		// Simple great circle distance

		// dist = Math.acos(Math.cos(rlt1) * Math.cos(rlt2) * Math.cos(rln2 -
		// rln1)
		// + Math.sin(rlt1) * Math.sin(rlt2));

		// More complex great circle distance formula to reduce error due to
		// rounding - from Wikipedia http://en.wikipedia.org/wiki/Great-circle_distance

		double n1 = Math.pow(Math.cos(rlt2) * Math.sin(d_lambda), 2);
		double n2 = Math.pow(Math.cos(rlt1) * Math.sin(rlt2) - (Math.sin(rlt1)
				* Math.cos(rlt2) * Math.cos(d_lambda)), 2);
		double numerator = Math.sqrt(n1 + n2);
		double denominator = Math.sin(rlt1) * Math.sin(rlt2) + Math.cos(rlt1)
				* Math.cos(rlt2) * Math.cos(d_lambda);
		dist = Math.atan2(numerator, denominator);

		return 2*Math.PI*R_EARTH * Math.toDegrees(dist) / 360;
	}
	
	/**
	 * Calculates the distance traveled along a sphere (great circle distance)
	 * 
	 * @param rlon1 -
	 *            The longitude of origin
	 * @param rlat1 -
	 *            The latitude of origin
	 * @param rlon2 -
	 *            The destination longitude
	 * @param rlat2 -
	 *            The destination latitude.
	 * @return - Distance traveled in meters.
	 */

	public static double distance_Sphere(float rlon1, float rlat1, float rlon2,
			float rlat2) {

		return distance_Sphere((double) rlon1, (double) rlat1,(double) rlon2, (double) rlat2);
	}

	/**
	 * Executes a change in position within a spherical coordinate system.
	 * 
	 * @param coords -
	 *            Coordinates, latitude first, then longitude
	 * @param dy -
	 *            Change in the y direction (latitude) in meters
	 * @param dx -
	 *            Change in the x direction (longitude) in meters
	 * @return - The new position, latitude then longitude.
	 */

	public static double[] latLon(double[] coords, double dy, double dx) {

		double rlat2, rlon2;
		double dlon, rln1, rlt1;

		rln1 = Math.toRadians(coords[1]); // Convert longitude to radians
		rlt1 = Math.toRadians(coords[0]); // Convert latitude to radians
		rlat2 = rlt1 + dy * REINV; // Convert distance to radians
		rlat2 = Math.asin(Math.sin(rlat2) * Math.cos(dx * REINV)); // Trigonometry
		// magic!
		dlon = Math.atan2(Math.sin(dx * REINV) * Math.cos(rlt1), (Math.cos(dx
				* REINV) - Math.sin(rlt1) * Math.sin(rlat2)));
		rlon2 = Math.toDegrees(rln1 + dlon); // Convert back
		rlat2 = Math.toDegrees(rlat2); // same

		return new double[] { rlat2, rlon2 };

	}
	
	/**
	 * Executes a change in position within a spherical coordinate system.
	 * 
	 * @param coords -
	 *            Coordinates, latitude first, then longitude
	 * @param dy -
	 *            Change in the y direction (latitude) in meters
	 * @param dx -
	 *            Change in the x direction (longitude) in meters
	 * @return - The new position, latitude then longitude.
	 */

	public static float[] latLon(float[] coords, float dy, float dx) {

		double rlat2, rlon2;
		double dlon, rln1, rlt1;

		rln1 = Math.toRadians(coords[1]); // Convert longitude to radians
		rlt1 = Math.toRadians(coords[0]); // Convert latitude to radians
		rlat2 = rlt1 + dy * REINV; // Convert distance to radians
		rlat2 = Math.asin(Math.sin(rlat2) * Math.cos(dx * REINV)); // Trigonometry
		// magic!
		dlon = Math.atan2(Math.sin(dx * REINV) * Math.cos(rlt1), (Math.cos(dx
				* REINV) - Math.sin(rlt1) * Math.sin(rlat2)));
		rlon2 = Math.toDegrees(rln1 + dlon); // Convert back
		rlat2 = Math.toDegrees(rlat2); // same

		return new float[] { (float) rlat2, (float) rlon2 };

	}

	/**
	 * Executes a change in position within a spherical coordinate system.
	 * 
	 * @param coords -
	 *            Coordinates, latitude first, then longitude
	 * @param dy -
	 *            Change in the y direction (latitude) in meters
	 * @param dx -
	 *            Change in the x direction (longitude) in meters
	 * @param dz -
	 *            Change in the z direction (depth) in meters
	 * @return - The new position, latitude then longitude.
	 */
	
	public static double[] latLonZ(double[] coords, double dy, double dx, double dz) {

		double rlat2, rlon2;
		double dlon, rln1, rlt1;

		rln1 = Math.toRadians(coords[1]); // Convert longitude to radians
		rlt1 = Math.toRadians(coords[0]); // Convert latitude to radians
		rlat2 = rlt1 + dy * REINV; // Convert distance to radians
		rlat2 = Math.asin(Math.sin(rlat2) * Math.cos(dx * REINV)); // Trigonometry
		// magic!
		dlon = Math.atan2(Math.sin(dx * REINV) * Math.cos(rlt1), (Math.cos(dx
				* REINV) - Math.sin(rlt1) * Math.sin(rlat2)));
		rlon2 = Math.toDegrees(rln1 + dlon); // Convert back
		rlat2 = Math.toDegrees(rlat2); // same

		return new double[] { rlat2, rlon2, coords[2]+dz };

	}

	/**
	 * Executes a change in position within a spherical coordinate system.
	 * 
	 * @param coords -
	 *            Coordinates, latitude first, then longitude
	 * @param dy -
	 *            Change in the y direction (latitude) in meters
	 * @param dx -
	 *            Change in the x direction (longitude) in meters
	 * @param dz -
	 *            Change in the z direction (depth) in meters
	 * @return - The new position, latitude then longitude.
	 */
	
	public static float[] latLonZ(float[] coords, float dy, float dx, float dz) {

		double rlat2, rlon2;
		double dlon, rln1, rlt1;

		rln1 = Math.toRadians(coords[1]); // Convert longitude to radians
		rlt1 = Math.toRadians(coords[0]); // Convert latitude to radians
		rlat2 = rlt1 + dy * REINV; // Convert distance to radians
		rlat2 = Math.asin(Math.sin(rlat2) * Math.cos(dx * REINV)); // Trigonometry magic!
		dlon = Math.atan2(Math.sin(dx * REINV) * Math.cos(rlt1), (Math.cos(dx
				* REINV) - Math.sin(rlt1) * Math.sin(rlat2)));
		rlon2 = Math.toDegrees(rln1 + dlon); // Convert back
		rlat2 = Math.toDegrees(rlat2); // same

		return new float[] { (float) rlat2, (float) rlon2, coords[2]+dz};
	}
	
	/**
	 * Converts coordinates from decimal degrees to Mercator projection
	 * 
	 * @param coords
	 * @return
	 */
	public static double[] dd2mercator(double[] coords){
		double rad = Math.toRadians(coords[1]);
		double fsin = Math.sin(rad);
		double y = (R_EARTH/2.0)*Math.log((1.0+fsin)/(1.0-fsin));
		double x = Math.toDegrees(coords[0]) * R_EARTH;
		return new double[]{x,y};
	}
	
	// Longitude and latitude values to Cylindrical Equidistant Meters
	
	public static double[] lonlat2ceqd(double[] coords){
		double latitude_origin = Math.toRadians(0);
		double central_meridian = Math.toRadians(0);
		double longitude = Math.toRadians(coords[0]);
		double latitude = Math.toRadians(coords[1]);
		double [] out = new double[coords.length];
		out[1] = R_EARTH*latitude;
		out[0] = R_EARTH*(longitude-central_meridian)*Math.cos(latitude_origin);
		if(coords.length==3){out[2]=coords[2];}
		return out;
	}

	// Cylindrical Equidistant Meters to longitude and latitude
	
	public static double[] ceqd2lonlat(double[] coords){
		double latitude_origin = Math.toRadians(0);
		double central_meridian = Math.toRadians(0);
		double [] out = new double[coords.length];
		out[1] = Math.toDegrees(coords[1]/R_EARTH);
		out[0] = Math.toDegrees(central_meridian+(coords[0]/(R_EARTH*Math.cos(latitude_origin))));
		if(coords.length==3){out[2]=coords[2];}
		return out;
	}
	
	public static boolean checkDD(double[] coords){
		return (coords[0]<-90)||(coords[0]>90)||(coords[1]<-180)||(coords[1]>360);
	}
}
