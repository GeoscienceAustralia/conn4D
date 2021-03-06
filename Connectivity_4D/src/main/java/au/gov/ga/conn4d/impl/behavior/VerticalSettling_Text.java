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

package au.gov.ga.conn4d.impl.behavior;

import java.io.File;
import java.io.IOException;

import au.gov.ga.conn4d.Boundary;
import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.VerticalMigration;
import au.gov.ga.conn4d.impl.readers.Boundary_Raster_NetCDF;
import au.gov.ga.conn4d.utils.VectorUtils;



public class VerticalSettling_Text implements VerticalMigration, Cloneable {

	private long timeInterval = 1;
	private String timeIntervalUnits = "Days";
	private double[][] vmtx;
	// private double[] bins = {5,15,25,40,62.5,87.5,112.5,137.5};
	private double[] bins = { 10, 30, 50, 70 };
	private Boundary bathym;
	private double sinkingPer6hrs = 0;

	/*
	 * Depth bins are: 0: 0-10m (5m) 1: 10-20m (15m) 2: 20-30m (25m) 3: 30-50m
	 * (40m) 4: 50-75m (62.5m) 5: 75-100m (87.5m) 6: 100-125m (112.5m) 7:
	 * 125m-150m (137.5m)
	 */

	public VerticalSettling_Text() {
	}

	public VerticalSettling_Text(String vertfile) {
		vmtx = VectorUtils.loadASCIIMatrix(new File(vertfile));
	}

	// *** What if we are in a shallower area than the depth range?

	@Override
	public void apply(Particle p) {

		double depth = p.getZ();
		depth = depth + sinkingPer6hrs;
		
		if(checkDepth(depth,p.getX(),p.getY())){
			p.setSettling(true);
		}
		else{p.setZ(depth);}
	}

	public long getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	public String getTimeIntervalUnits() {
		return timeIntervalUnits;
	}

	public void setTimeIntervalUnits(String timeIntervalUnits) {
		this.timeIntervalUnits = timeIntervalUnits;
	}

	public double[][] getVmtx() {
		return vmtx;
	}

	public void setVmtx(double[][] vmtx) {
		this.vmtx = vmtx;
	}

	public double[] getBins() {
		return bins;
	}

	public void setBins(double[] bins) {
		this.bins = bins;
	}

	public void setBathymetry(String filename) {
		try {
			Boundary_Raster_NetCDF ncb = new Boundary_Raster_NetCDF(filename);
			ncb.setLatName("Latitude");
			ncb.setLonName("Longitude");
			ncb.initialize();
			bathym = ncb;

		} catch (IOException e) {
			System.out.println("Bathymetry layer - " + filename
					+ " could not be read.");
			e.printStackTrace();
		}
	}

	/**
	 * Checks a given depth at the given position versus the bathymetry layer.
	 */

	private boolean checkDepth(double depth, double x, double y) {

		double bval = bathym.getBoundaryDepth(x, y);

		/*
		 * If the bathymetry says we're on land, use the shallowest depth bin.
		 * Land intercepts are handled using shapefiles since it requires an
		 * angle of reflection.
		 */

		if (depth >= Math.abs(bval)/100) {
			return true;
		}
		return false;

	}

	@Override
	public VerticalSettling_Text clone() {
		VerticalSettling_Text tvs = new VerticalSettling_Text();
		tvs.timeInterval = timeInterval;
		tvs.timeIntervalUnits = timeIntervalUnits;
		tvs.vmtx = VectorUtils.copy(vmtx);
		tvs.bathym = bathym.clone();
		return tvs;
	}
}
