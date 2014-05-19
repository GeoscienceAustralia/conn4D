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

package au.gov.ga.conn4d.impl.movement;

/**
 *  @author Johnathan Kool based on FORTRAN code developed by Claire B. Paris,
 * 		    Ashwanth Srinivasan, and Robert K. Cowen.
 */

import au.gov.ga.conn4d.Advector;
import au.gov.ga.conn4d.Movement;
import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.VelocityReader;
import au.gov.ga.conn4d.impl.readers.VelocityReader_NetCDF_4D;
import au.gov.ga.conn4d.utils.GeometryUtils;

/**
 * 3D Movement implementation using a Cash-Karp Runge-Kutte solver to perform
 * integration across horizontal velocity fields.
 * 
 * Cash-Karp Runge-Kutte solver:
 * http://en.wikipedia.org/wiki/Cash%E2%80%93Karp_method
 */

public class Advection_RK4_3D implements Advector, Movement, Cloneable {

	private float h;
	private VelocityReader vr = new VelocityReader_NetCDF_4D();

	// Cash-Karp Butcher tableau

	private final double

	B21 = 0.2f, B31 = 3.0f / 40.0f, B32 = 9.0f / 40.0f, B41 = 0.3f,
			B42 = -0.9f, B43 = 1.2f, B51 = -11.0f / 54.0f, B52 = 2.5f,
			B53 = -70.0f / 27.0f, B54 = 35.0f / 27.0f,
			B61 = 1631.0f / 55296.0f, B62 = 175.0f / 512.0f,
			B63 = 575.0f / 13824.0f, B64 = 44275.0f / 110592.0f,
			B65 = 253.0f / 4096.0f, C1 = 37.0f / 378.0f, C3 = 250.0f / 621.0f,
			C4 = 125.0f / 594.0f, C6 = 512.0f / 1771.0f;

	/**
	 * Moves a particle through advection, integrating along the velocity field
	 * through Runge-Kutta integration.
	 */

	@Override
	public synchronized void apply(Particle p) {

		// Runge-Kutta components

		double aku1, aku2, aku3, aku4, aku5, aku6;
		double akv1, akv2, akv3, akv4, akv5, akv6;
		double akw1, akw2, akw3, akw4, akw5, akw6;

		// Holding variables for displacement in x, y and z direction

		double dx, dy, dz;

		// Holding variable for paired velocities (u and v)

		double[] ctmp;

		// Holding variable for new co-ordinates (location)

		double[] tmpcoord;

		// Get variables from the particle

		long t = p.getT();
		double z = p.getZ();
		double x = p.getX();
		double y = p.getY();
		p.setNodata(false);
		p.setNearNoData(false);

		// Retrieve velocity values - check for bottom? if NaN... check
		// neighbors... then check bottom.

		try {
			ctmp = vr.getVelocities(t, z, x, y);

			if (vr.isNearNoData()) {
				p.setNearNoData(true);
			}

			// If the velocity values are null, we are outside the boundary domain

			if (ctmp == null) {
				p.setLost(true);
				return;
			}

			// If we have velocity values, but their values are NODATA, check to see
			// if the particle is on land. If not, then handle accordingly (decay
			// function)

			if (ctmp == vr.getNODATA()) {

				// This is to prevent calculating stationary particles over and
				// over.

				if (Math.abs(p.getU()) < 0.0001 && Math.abs(p.getV()) < 0.0001) {
					return;
				}

				aku1 = p.getU();
				akv1 = p.getV();
				akw1 = p.getW();
				p.setNodata(true);
				p.setNearNoData(true);

			} else {
				aku1 = ctmp[0];
				akv1 = ctmp[1];
				akw1 = ctmp[2];
				p.setNodata(false);
			}

			dx = B21 * h * aku1;
			dy = B21 * h * akv1;
			dz = B21 * h * akw1;

			// Automatic conversion of coordinate system

			tmpcoord = GeometryUtils.latLon(new double[] { y, x }, dy, dx);

			// Get the velocities at the updated position

			ctmp = vr.getVelocities(t, Math.min(
					Math.max(vr.getBounds()[1][0], vr.getBounds()[1][1]), z + dz),
					tmpcoord[1], tmpcoord[0]);

			if (vr.isNearNoData()) {
				p.setNearNoData(true);
			}

			if (ctmp == null) {
				p.setLost(true);
				return;
			}

			if (ctmp == vr.getNODATA()) {

				aku2 = aku1;
				akv2 = akv1;
				akw2 = akw1;
				p.setNodata(true);
				p.setNearNoData(true);

			} else {

				aku2 = ctmp[0];
				akv2 = ctmp[1];
				akw2 = ctmp[2];
				p.setNodata(false);
			}

			dx = h * (B31 * aku1 + B32 * aku2);
			dy = h * (B31 * akv1 + B32 * akv2);
			dz = h * (B31 * akw1 + B32 * akw2);

			tmpcoord = GeometryUtils.latLonZ(new double[] { y, x, z }, dy, dx, dz);
			ctmp = vr.getVelocities(t, Math.min(
					Math.max(vr.getBounds()[1][0], vr.getBounds()[1][1]), z + dz),
					tmpcoord[1], tmpcoord[0]);

			if (vr.isNearNoData()) {
				p.setNearNoData(true);
			}

			if (ctmp == null) {
				p.setLost(true);
				return;
			}

			if (ctmp == vr.getNODATA()) {

				aku3 = aku2;
				akv3 = akv2;
				akw3 = akw2;
				p.setNodata(true);
				p.setNearNoData(true);

			} else {

				aku3 = ctmp[0];
				akv3 = ctmp[1];
				akw3 = ctmp[2];
				p.setNodata(false);
			}

			dx = h * (B41 * aku1 + B42 * aku2 + B43 * aku3);
			dy = h * (B41 * akv1 + B42 * akv2 + B43 * akv3);
			dz = h * (B41 * akw1 + B42 * akw2 + B43 * akw3);

			tmpcoord = GeometryUtils.latLonZ(new double[] { y, x, z }, dy, dx, dz);
			ctmp = vr.getVelocities(t, Math.min(
					Math.max(vr.getBounds()[1][0], vr.getBounds()[1][1]), z + dz),
					tmpcoord[1], tmpcoord[0]);

			if (vr.isNearNoData()) {
				p.setNearNoData(true);
			}

			if (ctmp == null) {
				p.setLost(true);
				return;
			}

			if (ctmp == vr.getNODATA()) {

				aku4 = aku3;
				akv4 = akv3;
				akw4 = akw3;
				p.setNodata(true);
				p.setNearNoData(true);

			} else {

				aku4 = ctmp[0];
				akv4 = ctmp[1];
				akw4 = ctmp[2];
				p.setNodata(false);
			}
			dx = h * (B51 * aku1 + B52 * aku2 + B53 * aku3 + B54 * aku4);
			dy = h * (B51 * akv1 + B52 * akv2 + B53 * akv3 + B54 * akv4);
			dz = h * (B51 * akw1 + B52 * akw2 + B53 * akw3 + B54 * akw4);

			tmpcoord = GeometryUtils.latLonZ(new double[] { y, x, z }, dy, dx, dz);
			ctmp = vr.getVelocities(t, Math.min(
					Math.max(vr.getBounds()[1][0], vr.getBounds()[1][1]), z + dz),
					tmpcoord[1], tmpcoord[0]);

			if (vr.isNearNoData()) {
				p.setNearNoData(true);
			}

			if (ctmp == null) {
				p.setLost(true);
				return;
			}

			if (ctmp == vr.getNODATA()) {

				aku5 = aku4;
				akv5 = akv4;
				akw5 = akw4;
				p.setNodata(true);
				p.setNearNoData(true);

			} else {

				aku5 = ctmp[0];
				akv5 = ctmp[1];
				akw5 = ctmp[2];
				p.setNodata(false);
			}

			dx = h
					* (B61 * aku1 + B62 * aku2 + B63 * aku3 + B64 * aku4 + B65
							* aku5);
			dy = h
					* (B61 * akv1 + B62 * akv2 + B63 * akv3 + B64 * akv4 + B65
							* akv5);
			dz = h
					* (B61 * akw1 + B62 * akw2 + B63 * akw3 + B64 * akw4 + B65
							* akw5);

			tmpcoord = GeometryUtils.latLonZ(new double[] { y, x, z }, dy, dx, dz);
			ctmp = vr.getVelocities(t, Math.min(
					Math.max(vr.getBounds()[1][0], vr.getBounds()[1][1]), z + dz),
					tmpcoord[1], tmpcoord[0]);

			if (vr.isNearNoData()) {
				p.setNearNoData(true);
			}

			if (ctmp == null) {
				p.setLost(true);
				return;
			}

			if (ctmp == vr.getNODATA()) {

				aku6 = aku5;
				akv6 = akv5;
				akw6 = akw5;
				p.setNodata(true);
				p.setNearNoData(true);

			} else {

				aku6 = ctmp[0];
				akv6 = ctmp[1];
				akw6 = ctmp[2];
				p.setNodata(false);
			}

			dx = h * (C1 * aku1 + C3 * aku3 + C4 * aku4 + C6 * aku6);
			dy = h * (C1 * akv1 + C3 * akv3 + C4 * akv4 + C6 * akv6);
			dz = h * (C1 * akw1 + C3 * akw3 + C4 * akw4 + C6 * akw6);

			tmpcoord = GeometryUtils.latLon(new double[] { y, x }, dy, dx);
			ctmp = vr.getVelocities(t, Math.min(
					Math.max(vr.getBounds()[1][0], vr.getBounds()[1][1]), z + dz),
					tmpcoord[1], tmpcoord[0]);

			p.setPX(p.getX());
			p.setPY(p.getY());
			p.setPZ(p.getZ());
			p.setY(tmpcoord[0]);
			p.setX(tmpcoord[1]);
			p.setZ(Math.min(0, z + dz));
			p.setU(aku6);
			p.setV(akv6);
			p.setW(akw6);
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the time step duration - i.e. the interval over which the
	 * integration occurs.
	 * 
	 * @param h
	 *            - duration in milliseconds, stored as seconds, since the time
	 *            units must correspond with the velocity files.
	 */

	public void setH(float h) {
		// **IMPORTANT!!!** Accepts h in milliseconds, uses h in seconds because
		// that is how velocity is delivered in the oceanographic data files.
		// i.e. we avoid repeatedly converting time when reading velocity values.
		this.h = h / 1000;
	}

	/**
	 * Sets the VelocityReader
	 * 
	 * @param vr
	 */

	public void setVr(VelocityReader vr) {
		this.vr = vr;
	}

	/**
	 * Returns a clone of the object.
	 */

	@Override
	public Advection_RK4_3D clone() {
		Advection_RK4_3D rk4 = new Advection_RK4_3D();
		rk4.setH(h * 1000);// We multiply by 1000 because h is stored as seconds
		rk4.setVr(vr.clone());
		return rk4;
	}

	/**
	 * Releases resources associated with the class.
	 */

	@Override
	public void close() {
		vr.close();
	}
}
