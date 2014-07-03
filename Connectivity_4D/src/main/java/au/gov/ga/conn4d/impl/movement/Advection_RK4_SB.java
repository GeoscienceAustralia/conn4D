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
 * 		    Ashwanth Srinivasan, and Robert K. Cowen at the University of Miami.
 */

import au.gov.ga.conn4d.Advector;
import au.gov.ga.conn4d.Movement;
import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.VelocityReader;
import au.gov.ga.conn4d.impl.readers.VelocityReader_NetCDF_4D;
import au.gov.ga.conn4d.utils.FluidPhysics;
import au.gov.ga.conn4d.utils.GeometryUtils;

/**
 * 3D Movement implementation using a Cash-Karp Runge-Kutta solver to perform
 * integration across horizontal velocity fields.
 * 
 * Cash-Karp Runge-Kutta solver:
 * http://en.wikipedia.org/wiki/Cash%E2%80%93Karp_method
 */

public class Advection_RK4_SB implements Advector, Movement, Cloneable {

	private float h;
	private VelocityReader vr = new VelocityReader_NetCDF_4D();
	private FluidPhysics fp = new FluidPhysics();

	// Cash-Karp Butcher tableau

	private final double

	B21 = 0.2f, 				
	B31 = 3.0f / 40.0f, 		B32 = 9.0f / 40.0f, 
	B41 = 0.3f,					B42 = -0.9f, 			B43 = 1.2f, B51 = -11.0f / 54.0f, 
								B52 = 2.5f,				B53 = -70.0f / 27.0f, 		B54 = 35.0f / 27.0f,
	B61 = 1631.0f / 55296.0f, 	B62 = 175.0f / 512.0f,	B63 = 575.0f / 13824.0f, 	B64 = 44275.0f / 110592.0f,	B65 = 253.0f / 4096.0f, 	
	C1 = 37.0f / 378.0f, 								C3 = 250.0f / 621.0f,		C4 = 125.0f / 594.0f, 								C6 = 512.0f / 1771.0f;

	/**
	 * Moves a particle through advection, integrating along the velocity field
	 * through Runge-Kutta integration.
	 */

	@Override
	public synchronized void apply(Particle p) {

		// Runge-Kutta components

		double ambient_u1, ambient_u2, ambient_u3, ambient_u4, ambient_u5, ambient_u6;
		double ambient_v1, ambient_v2, ambient_v3, ambient_v4, ambient_v5, ambient_v6;
		double ambient_w1, ambient_w2, ambient_w3, ambient_w4, ambient_w5, ambient_w6;
		double nw1, nw2, nw3, nw4, nw5, nw6;

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

				ambient_u1 = p.getU();
				ambient_v1 = p.getV();
				ambient_w1 = p.getW();
				
				p.setNodata(true);
				p.setNearNoData(true);

			} else {
				p.setPU(p.getU());
				p.setPV(p.getV());
				p.setPW(p.getW());
				
				ambient_u1 = ctmp[0];
				ambient_v1 = ctmp[1];
				ambient_w1 = ctmp[2];
				
				p.setNodata(false);
			}

			dx = B21 * h * ambient_u1;
			dy = B21 * h * ambient_v1;
			
			nw1 = ambient_w1 + fp.calcVelocityChange(p, ambient_w1, p.getW(), h/6);
			dz = B21 * h * (nw1);

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

				ambient_u2 = ambient_u1;
				ambient_v2 = ambient_v1;
				ambient_w2 = ambient_w1;
				p.setNodata(true);
				p.setNearNoData(true);

			} else {

				ambient_u2 = ctmp[0];
				ambient_v2 = ctmp[1];
				ambient_w2 = ctmp[2];
				p.setNodata(false);
			}

			dx = h * (B31 * ambient_u1 + B32 * ambient_u2);
			dy = h * (B31 * ambient_v1 + B32 * ambient_v2);
			
			nw2 = ambient_w2 + fp.calcVelocityChange(p, ambient_w2, nw1, h/6);
			dz = h * (B31 * nw1 + B32 * nw2);

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

				ambient_u3 = ambient_u2;
				ambient_v3 = ambient_v2;
				ambient_w3 = ambient_w2;
				p.setNodata(true);
				p.setNearNoData(true);

			} else {

				ambient_u3 = ctmp[0];
				ambient_v3 = ctmp[1];
				ambient_w3 = ctmp[2];
				p.setNodata(false);
			}

			dx = h * (B41 * ambient_u1 + B42 * ambient_u2 + B43 * ambient_u3);
			dy = h * (B41 * ambient_v1 + B42 * ambient_v2 + B43 * ambient_v3);
			nw3 = ambient_w3 + fp.calcVelocityChange(p, ambient_w3, nw2, h/6);
			dz = h * (B41 * nw1 + B42 * nw2 + B43 * nw3);

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

				ambient_u4 = ambient_u3;
				ambient_v4 = ambient_v3;
				ambient_w4 = ambient_w3;
				p.setNodata(true);
				p.setNearNoData(true);

			} else {

				ambient_u4 = ctmp[0];
				ambient_v4 = ctmp[1];
				ambient_w4 = ctmp[2];
				p.setNodata(false);
			}
			
			dx = h * (B51 * ambient_u1 + B52 * ambient_u2 + B53 * ambient_u3 + B54 * ambient_u4);
			dy = h * (B51 * ambient_v1 + B52 * ambient_v2 + B53 * ambient_v3 + B54 * ambient_v4);
			nw4 = ambient_w4 + fp.calcVelocityChange(p, ambient_w4, nw3, h/6); 
			dz = h * (B51 * nw1 + B52 * nw2 + B53 * nw3 + B54 * nw4);

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

				ambient_u5 = ambient_u4;
				ambient_v5 = ambient_v4;
				ambient_w5 = ambient_w4;
				p.setNodata(true);
				p.setNearNoData(true);

			} else {

				ambient_u5 = ctmp[0];
				ambient_v5 = ctmp[1];
				ambient_w5 = ctmp[2];
				p.setNodata(false);
			}

			dx = h
					* (B61 * ambient_u1 + B62 * ambient_u2 + B63 * ambient_u3 + B64 * ambient_u4 + B65
							* ambient_u5);
			dy = h
					* (B61 * ambient_v1 + B62 * ambient_v2 + B63 * ambient_v3 + B64 * ambient_v4 + B65
							* ambient_v5);
			nw5 = ambient_w5 + fp.calcVelocityChange(p, ambient_w5, nw4, h/6);
			dz = h
					* (B61 * nw1 + B62 * nw2 + B63 * nw3 + B64 * nw4 + B65
							* nw5);

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

				ambient_u6 = ambient_u5;
				ambient_v6 = ambient_v5;
				ambient_w6 = ambient_w5;
				p.setNodata(true);
				p.setNearNoData(true);

			} else {

				ambient_u6 = ctmp[0];
				ambient_v6 = ctmp[1];
				ambient_w6 = ctmp[2];
				p.setNodata(false);
			}

			dx = h * (C1 * ambient_u1 + C3 * ambient_u3 + C4 * ambient_u4 + C6 * ambient_u6);
			dy = h * (C1 * ambient_v1 + C3 * ambient_v3 + C4 * ambient_v4 + C6 * ambient_v6);
			nw6 = ambient_w6 + fp.calcVelocityChange(p, ambient_w6, nw5, h/6);
			dz = h * (C1 * nw1 + C3 * nw3 + C4 * nw4 + C6 * nw6);

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
			p.setU(ambient_u6);
			p.setV(ambient_v6);
			p.setW(nw6);
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
	public Advection_RK4_SB clone() {
		Advection_RK4_SB rk4 = new Advection_RK4_SB();
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
	
	public FluidPhysics getFluidPhysics(){
		return fp;
	}
}
