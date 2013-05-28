package au.gov.ga.conn4d.impl.readers;

import java.io.IOException;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import au.gov.ga.conn4d.VelocityReader;
import au.gov.ga.conn4d.utils.IndexLookup_Nearest;
import au.gov.ga.conn4d.utils.TimeConvert;
//import au.gov.ga.conn4d.utils.TriCubicSpline;
import au.gov.ga.conn4d.utils.TricubicSplineInterpolatingFunction;
import au.gov.ga.conn4d.utils.TricubicSplineInterpolator;

/**
 * Reads 3D Velocity values from a collection of 3 NetCDF files (u,v,w)
 * 
 * @author Johnathan Kool
 */

public class VelocityReader_NetCDF_4D implements VelocityReader, Cloneable {

	private double u, v, w;
	private String freqUnits = "Days";
	private final int kernelSize = 5;
	private final int halfKernel = kernelSize / 2;
	private final int zKernelSize = 3;
	private final int zHalfKernel = zKernelSize / 2;
	private float cutoff = 1E3f;
	private NetcdfFile uFile, vFile, wFile;
	private Variable latVar, lonVar, zVar, tVar;
	private Variable uVar, vVar, wVar;
	private Array uArr, vArr, wArr;
	private IndexLookup_Nearest xloc, yloc, zloc, tloc;
//	private TriCubicSpline tcs = new TriCubicSpline(new double[zKernelSize],
//			new double[kernelSize], new double[kernelSize],
//			new float[zKernelSize][kernelSize][kernelSize]);
	private final double[] NODATA = { Double.NaN, Double.NaN, Double.NaN };
	private String latName = "Latitude";
	private String lonName = "Longitude";
	private String kName = "Depth";
	private String tName = "Time";
	private double[][] bounds = new double[4][2];
	// private boolean negOceanCoord = false;
	// private boolean negPolyCoord = true;
	private long timeOffset = -2177521200000l; // Difference between
												// HYCOM's base time
												// (1900) and Java's
												// base time (1970)
	private boolean nearNoData = false;

	private String uName;
	private String vName;
	private String wName;

	private double[] velocities;
	private double[] averages;
	private double[] variances;

	/**
	 * Releases resources associated with this instance
	 */

	@Override
	public void close() {
		try {
			uFile.close();
			vFile.close();
			wFile.close();
		} catch (IOException e) {
			System.out
					.println("WARNING:  Error while closing velocity files from VelocityReader.  Attempting to continue.");
			e.printStackTrace();

		} finally {
			uVar = null;
			vVar = null;
			wVar = null;
			uArr = null;
			vArr = null;
			wArr = null;
		}
		
		xloc.close();
		yloc.close();
		zloc.close();
		tloc.close();
		
		xloc = null;
		yloc = null;
		zloc = null;
		tloc = null;
}

	/**
	 * Retrieves velocities as a vector [u,v,w] based on given positions
	 * 
	 * @param time
	 *            - time coordinate in milliseconds
	 * @param z
	 *            - depth coordinate
	 * @param lon
	 *            - longitude (decimal degrees)
	 * @param lat
	 *            - latitude (decimal degrees)
	 */

	@Override
	public synchronized double[] getVelocities(long time, double z, double lon,
			double lat) {

		try {
			int js, is, ks, ts;

			float stime = (float) TimeConvert.convertFromMillis(freqUnits, time
					- timeOffset);

			// Searching for the cell indices nearest to the given location

			is = yloc.lookup(lat);
			js = xloc.lookup(lon);
			ks = zloc.lookup(z);
			ts = tloc.lookup(stime);

			// Completely outside the horizontal bounds - return null as opposed
			// to NODATA

			if (yloc.isIn_Bounds() != 0) {
				this.notifyAll();
				return null;
			}

			// Completely outside the vertical bounds

			if (xloc.isIn_Bounds() != 0) {
				this.notifyAll();
				return null;
			}

			// Completely outside the time bounds

			if (tloc.isIn_Bounds() != 0) {
				this.notifyAll();
				return null;
			}

			// Outside the depth bounds (downwards)

			if (zloc.isIn_Bounds() > 0) {
				this.notifyAll();
				return null;
			}
			
			// Beyond the vertical ocean surface
			
			if (zloc.isIn_Bounds() < 0) {
				ks = 0; // Assign the index to the surface for now.
				z = zloc.getMinVal();
			}

			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// ATTENTION!!!! THE ORDER IS VERY IMPORTANT HERE!!!! Latitude (i/y)
			// and then Longitude (j/x). That's the way it is set up in the
			// NetCDF File. The best way would be to have automatic order
			// detection
			// somehow....
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

			// Handling data edges

			int i_lhs = halfKernel;
			int i_rhs = halfKernel;
			int j_lhs = halfKernel;
			int j_rhs = halfKernel;
			int k_lhs = zHalfKernel;
			int k_rhs = zHalfKernel;

			if (is < halfKernel) {
				i_lhs = is;
			}
			if (is + halfKernel >= yloc.arraySize()) {
				i_rhs = yloc.arraySize() - is - 1;
			}
			if (js < halfKernel) {
				j_lhs = js;
			}
			if (js + halfKernel >= xloc.arraySize()) {
				j_rhs = xloc.arraySize() - js - 1;
			}
			if (ks < zHalfKernel) {
				k_lhs = ks;
			}
			if (ks + zHalfKernel >= zloc.arraySize()) {
				k_rhs = zloc.arraySize() - ks - 1;
			}

			int istart = is - i_lhs;
			int jstart = js - j_lhs;
			int kstart = ks - k_lhs;

			// LHS + RHS + middle

			int idim = i_lhs + i_rhs + 1;
			int jdim = j_lhs + j_rhs + 1;
			int kdim = k_lhs + k_rhs + 1;

			// Splines cannot be used with only 2 points. If we are using a
			// kernel size of 3 and it is reduced due to edge effects, then
			// slide the window.

			if (kdim == 2) {
				if (kstart != 0) {
					kstart -= (k_rhs + 1);
				}
				kdim = zKernelSize;
			}

			int blocksize = idim * jdim * kdim;
			int semiblock = (idim * (jdim + 1) * kdim)/2;

			int[] origin = new int[] { ts, kstart, istart, jstart };
			int[] shape = new int[] { 1, kdim, idim, jdim };

			Array latArr = null, lonArr = null, depthArr = null;

			try {
				latArr = latVar.read(new int[] { istart }, new int[] { idim });
				lonArr = lonVar.read(new int[] { jstart }, new int[] { jdim });
				depthArr = zVar.read(new int[] { kstart }, new int[] { kdim });

			} catch (InvalidRangeException e) {
				// Should not occur. Checking done above.
				e.printStackTrace();
			}

			try {
				uArr = uVar.read(origin, shape);
				uArr = uArr.reduce();
			} catch (InvalidRangeException e) {
				// Should not occur. Checking done above.
				e.printStackTrace();
			}

			try {
				vArr = vVar.read(origin, shape);
				vArr = vArr.reduce();
			} catch (InvalidRangeException e) {
				// Should not occur. Checking done above.
				e.printStackTrace();
			}

			if (zloc.isIn_Bounds() >= 0) {

				try {
					wArr = wVar.read(origin, shape);
					wArr = wArr.reduce();

				} catch (InvalidRangeException e) {
					// Should not occur. Checking done above.
					e.printStackTrace();
				}
			}

			float[][][] autmp = (float[][][]) uArr.copyToNDJavaArray();
			float[][][] avtmp = (float[][][]) vArr.copyToNDJavaArray();
			float[][][] awtmp = (float[][][]) wArr.copyToNDJavaArray();

			// Replace NODATA values with average values...

			int uct = 0;
			double usum = 0;
			double uavg = 0;
			double ussq = 0;
			double uvar = 0;

			int vct = 0;
			double vsum = 0;
			double vavg = 0;
			double vssq = 0;
			double vvar = 0;

			int wct = 0;
			double wsum = 0;
			double wavg = 0;
			double wssq = 0;
			double wvar = 0;

			for (int i = 0; i < idim; i++) {
				for (int j = 0; j < jdim; j++) {
					for (int k = 0; k < kdim; k++) {

						if (autmp[k][i][j] < cutoff
								&& !Double.isNaN(autmp[k][i][j])) {
							uct++;
							usum += autmp[k][i][j];
							uavg = usum / uct;
							ussq += autmp[k][i][j] * autmp[k][i][j];
							uvar = ussq / uct - uavg * uavg;

						}
					}
				}
			}

			for (int i = 0; i < idim; i++) {
				for (int j = 0; j < jdim; j++) {
					for (int k = 0; k < kdim; k++) {
						if (avtmp[k][i][j] < cutoff
								&& !Double.isNaN(avtmp[k][i][j])) {
							vct++;
							vsum += avtmp[k][i][j];
							vavg = vsum / vct;
							vssq += avtmp[k][i][j] * avtmp[k][i][j];
							vvar = vssq / vct - vavg * vavg;
						}
					}
				}
			}

			if (zloc.isIn_Bounds() >= 0) {

				for (int i = 0; i < idim; i++) {
					for (int j = 0; j < jdim; j++) {
						for (int k = 0; k < kdim; k++) {

							if (awtmp[k][i][j] < cutoff
									&& !Double.isNaN(awtmp[k][i][j])) {
								wct++;
								wsum += awtmp[k][i][j];
								wavg = wsum / wct;
								wssq += awtmp[k][i][j] * awtmp[k][i][j];
								wvar = wssq / wct - wavg * wavg;
							}
						}
					}
				}
			}

			nearNoData = false;

			// If there are null values...

			if (uct < blocksize) {

				nearNoData = true;

				// If there are more than (io-1)^2, return null

				if (uct < semiblock) {
					velocities = NODATA;
					averages = NODATA;
					variances = NODATA;
					return NODATA;
				}

				// Otherwise mitigate by replacing using the average value.

				for (int i = 0; i < idim; i++) {
					for (int j = 0; j < jdim; j++) {
						for (int k = 0; k < kdim; k++) {

							if (autmp[k][i][j] > cutoff
									|| Double.isNaN(autmp[k][i][j])) {

								autmp[k][i][j] = (float) uavg;
							}
						}
					}
				}
			}

			if (vct < blocksize) {

				nearNoData = true;

				// If there are more than halfKernel/2, return null

				if (vct < semiblock) {
					this.notifyAll();
					velocities = NODATA;
					averages = NODATA;
					variances = NODATA;
					return NODATA;
				}

				// Otherwise mitigate by replacing using the average value.

				for (int i = 0; i < idim; i++) {
					for (int j = 0; j < jdim; j++) {
						for (int k = 0; k < kdim; k++) {

							if (avtmp[k][i][j] > cutoff
									|| Double.isNaN(avtmp[k][i][j])) {

								avtmp[k][i][j] = (float) vavg;
							}
						}
					}
				}
			}
			if (zloc.isIn_Bounds() >= 0) {
				if (wct < blocksize) {

					nearNoData = true;

					// If there are more than (halfKernel-1)^2, return null

					if (wct < semiblock) {
						this.notifyAll();
						velocities = NODATA;
						averages = NODATA;
						variances = NODATA;
						return NODATA;
					}

					// Otherwise mitigate by replacing using the average value.

					for (int i = 0; i < kernelSize; i++) {
						for (int j = 0; j < kernelSize; j++) {
							for (int k = 0; k < zKernelSize; k++) {

								if (awtmp[k][i][j] > cutoff
										|| Double.isNaN(awtmp[k][i][j])) {

									awtmp[k][i][j] = (float) wavg;
								}
							}
						}
					}
				}
			}

			// Convert the Arrays into Java arrays

			double[] latja = (double[]) latArr.copyTo1DJavaArray();
			double[] lonja = (double[]) lonArr.copyTo1DJavaArray();
			double[] zja = (double[]) depthArr.copyTo1DJavaArray();

			// Obtain the interpolated values

			//int[] dim = tcs.getDim();

			//if (dim[0] != zja.length || dim[1] != latja.length
			//		|| dim[2] != lonja.length) {
			//	tcs = new TriCubicSpline(zja, latja, lonja, autmp);
			//} else {
			//	tcs.resetData(zja, latja, lonja, autmp);
			//}
			//try {
			
			TricubicSplineInterpolator tci = new TricubicSplineInterpolator();
			TricubicSplineInterpolatingFunction tsf = tci.interpolate(lonja, latja, zja, autmp);
			//	u = tcs.interpolate(z, lat, lon);
			u = tsf.value(lon,lat,z);
			//	tcs.setValues(avtmp);
			tsf = tci.interpolate(lonja, latja, zja, avtmp);
			//v = tcs.interpolate(z, lat, lon);
			v = tsf.value(lon,lat,z);

				if (zloc.isIn_Bounds() >= 0) {
					//tcs.setValues(awtmp);
					//w = tcs.interpolate(z, lat, lon);
					tsf = tci.interpolate(lonja, latja, zja, awtmp);
					w = tsf.value(lon,lat,z);
				} else {
					w = 0;
				}

			//} catch (Exception e) {
			//	e.printStackTrace();
			//}

			// If there is something strange with the values, return NODATA.

			if (Math.abs(u) > cutoff) {
				u = 0.0f;
				v = 0.0f;
				w = 0.0f;
				this.notifyAll();
				velocities = NODATA;
				averages = NODATA;
				variances = NODATA;
				return NODATA;
			} else if (Math.abs(v) > cutoff) {
				u = 0.0f;
				v = 0.0f;
				w = 0.0f;
				this.notifyAll();
				velocities = NODATA;
				averages = NODATA;
				variances = NODATA;
				return NODATA;
			} else if (Math.abs(w) > cutoff) {
				u = 0.0f;
				v = 0.0f;
				w = 0.0f;
				this.notifyAll();
				velocities = NODATA;
				averages = NODATA;
				variances = NODATA;
				return NODATA;
			}

			if (Double.isNaN(u) || Double.isNaN(v) || Double.isNaN(w)) {
				u = 0.0f;
				v = 0.0f;
				w = 0.0f;
				this.notifyAll();
				velocities = NODATA;
				averages = NODATA;
				variances = NODATA;
				return NODATA;
			}

			// Otherwise return the interpolated values.

			this.notifyAll();
			velocities = new double[] { u, v, w };
			averages = new double[] { uavg, vavg, wavg };

			// Correct running population variance to be sample variance.
			double ucorr = (double) uct / (double) (uct - 1);
			double vcorr = (double) vct / (double) (vct - 1);
			double wcorr = (double) wct / (double) (wct - 1);
			variances = new double[] { uvar * ucorr, vvar * vcorr, wvar * wcorr };

			return velocities;

			// If for some reason there was an error reading from the file,
			// return null.

		} catch (IOException e) {
			System.out
					.println("WARNING:  Error reading from velocity files.\n\n");
			e.printStackTrace();
		}
		this.notifyAll();
		return null;
	}

	/**
	 * Indicates if the position is near an element with NoData
	 */

	@Override
	public boolean isNearNoData() {
		return nearNoData;
	}

	/**
	 * Indicates whether negative latitude and longitude coordinates are being
	 * used. Used to back-transform coordinate values into the original
	 * reference frame if required.
	 */

	// public boolean isNegOceanCoord() {
	// return negOceanCoord;
	// }

	// public boolean isNegPolyCoord() {
	// return negPolyCoord;
	// }

	/**
	 * Retrieves average velocity values associated with the last queried
	 * position.
	 */

	public double[] getAverages() {
		return averages;
	}
	
	/**
	 * Retrieves the minimum and maximum values of the t, z, y and x dimensions
	 */

	@Override
	public double[][] getBounds() {
		return bounds;
	}

	/**
	 * Retrieves the name of the Depth variable
	 */

	public String getKName() {
		return kName;
	}

	/**
	 * Retrieves the name of the Latitude variable
	 */

	public String getLatName() {
		return latName;
	}

	/**
	 * Retrieves the name of the Longitude variable
	 */

	public String getLonName() {
		return lonName;
	}

	/**
	 * Retrieves the representation of the NODATA value
	 */

	@Override
	public double[] getNODATA() {
		return NODATA;
	}

	/**
	 * Retrieves the shapes of the velocity variables (u,v,w)
	 */

	@Override
	public int[][] getShape() {
		return new int[][] { uVar.getShape(), vVar.getShape(), wVar.getShape() };
	}

	/**
	 * Retrieves the name of the Time variable
	 */

	public String getTName() {
		return tName;
	}

	/**
	 * Retrieves the frequency units(?)
	 */

	@Override
	public String getUnits() {
		return freqUnits;
	}

	/**
	 * Retrieves variance values associated with the last queried position
	 */

	public double[] getVariances() {
		return variances;
	}

	/**
	 * Sets the average values for the velocities (u,v,w)
	 * 
	 * @param averages
	 */
	
	public void setAverages(double[] averages) {
		this.averages = averages;
	}
	
	/**
	 * Sets the name of the depth variable
	 * 
	 * @param kName
	 */	

	public void setKName(String kName) {
		this.kName = kName;
	}

	/**
	 * Sets the name of the latitude variable
	 * 
	 * @param latName
	 */
	
	public void setLatName(String latName) {
		this.latName = latName;
	}

	/**
	 * Sets the name of the longitude variable
	 * 
	 * @param lonName
	 */
	
	public void setLonName(String lonName) {
		this.lonName = lonName;
	}

	// public void setNegOceanCoord(boolean negOceanCoord) {
	// this.negOceanCoord = negOceanCoord;
	// }

	// public void setNegPolyCoord(boolean negPolyCoord) {
	// this.negPolyCoord = negPolyCoord;
	// }

	/**
	 * Sets the time offset value for the time reference of the velocity files
	 * from Java's base time
	 * 
	 * @param offset
	 */

	public void setTimeOffset(long offset) {
		this.timeOffset = offset;
	}

	/**
	 * Sets the name of the lookup variable for the Time position
	 * 
	 * @param name
	 */

	public void setTLookup(String name) {
		tVar = uFile.findVariable(tName);
		if (tVar == null) {
			System.out
					.println("Incorrect variable match:\n\nVariables in local config file: "
							+ tName + ".");
			System.out.println("Velocity file variables: "
					+ uFile.getVariables().toString() + "\n");
		}
		tloc = new IndexLookup_Nearest(tVar);
		bounds[0][0] = tloc.getMinVal();
		bounds[0][1] = tloc.getMaxVal();

	}

	/**
	 * Sets the name of the time variable
	 * 
	 * @param tName
	 */
	
	public void setTName(String tName) {
		this.tName = tName;
	}

	/**
	 * Sets the u-velocity field to be used by this class
	 * 
	 * @param _velocityFile
	 *            - the u-velocity NetCDF object
	 * @param uName
	 *            - the name of the u variable
	 * @throws IOException
	 */

	public void setUFile(String _velocityFile, String uName) throws IOException {

		uFile = NetcdfFile.open(_velocityFile);
		uVar = uFile.findVariable(uName);
		this.uName = uName;
	}

	public void setUnits(String units) {
		this.freqUnits = units;
	}

	/**
	 * Sets the variance values for the velocities (u,v,w)
	 * 
	 * @param variances
	 */

	public void setVariances(double[] variances) {
		this.variances = variances;
	}

	/**
	 * Sets the v-velocity field to be used by this class
	 * 
	 * @param _velocityFile
	 *            - the v-velocity NetCDF object
	 * @param vName
	 *            - the name of the v variable
	 * @throws IOException
	 */

	public void setVFile(String _velocityFile, String vName) throws IOException {
		vFile = NetcdfFile.open(_velocityFile);
		vVar = vFile.findVariable(vName);
		this.vName = vName;
	}

	/**
	 * Sets the w-velocity ffile to be used by this class
	 * 
	 * @param _velocityFile
	 *            - the w-velocity NetCDF object
	 * @param wName
	 *            - the name of the w variable
	 * @throws IOException
	 */

	public void setWFile(String _velocityFile, String wName) throws IOException {
		wFile = NetcdfFile.open(_velocityFile);
		wVar = wFile.findVariable(wName);
		this.wName = wName;
	}

	/**
	 * Sets the name of the lookup variable for the East-West position
	 * 
	 * @param name
	 */

	public void setXLookup(String name) {
		lonVar = uFile.findVariable(lonName);
		if (lonVar == null) {
			System.out
					.println("Incorrect variable match:\n\nVariables in local config file: "
							+ lonName + ".");
			System.out.println("Velocity file variables: "
					+ uFile.getVariables().toString() + "\n");
		}
		xloc = new IndexLookup_Nearest(lonVar);
		bounds[3][0] = xloc.getMinVal();
		bounds[3][1] = xloc.getMaxVal();
	}

	/**
	 * Sets the name of the lookup variable for the North-South position
	 * 
	 * @param name
	 */

	public void setYLookup(String name) {
		latVar = uFile.findVariable(latName);
		if (latVar == null) {
			System.out
					.println("Incorrect variable match:\n\nVariables in local config file: "
							+ latName + ".");
			System.out.println("Velocity file variables: "
					+ uFile.getVariables().toString() + "\n");
		}
		yloc = new IndexLookup_Nearest(latVar);
		bounds[2][0] = yloc.getMinVal();
		bounds[2][1] = yloc.getMaxVal();

	}

	/**
	 * Sets the name of the lookup variable for vertical position
	 * 
	 * @param name
	 */

	public void setZLookup(String name) {
		zVar = uFile.findVariable(kName);
		if (lonVar == null) {
			System.out
					.println("Incorrect variable match:\n\nVariables in local config file: "
							+ kName + ".");
			System.out.println("Velocity file variables: "
					+ uFile.getVariables().toString() + "\n");
		}
		zloc = new IndexLookup_Nearest(zVar);
		bounds[1][0] = zloc.getMinVal();
		bounds[1][1] = zloc.getMaxVal();
	}

	/**
	 * Clones the VelocityReader_NetCDF_3D object.
	 */

	@Override
	public VelocityReader_NetCDF_4D clone() {
		VelocityReader_NetCDF_4D ncv = new VelocityReader_NetCDF_4D();
		ncv.freqUnits = freqUnits;
		try {
			ncv.uFile = NetcdfFile.open(uFile.getLocation());
			ncv.vFile = NetcdfFile.open(vFile.getLocation());
			ncv.wFile = NetcdfFile.open(wFile.getLocation());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ncv.negOceanCoord = negOceanCoord;
		// ncv.negPolyCoord = negPolyCoord;
		ncv.kName = kName;
		ncv.latName = latName;
		ncv.lonName = lonName;
		ncv.tName = tName;
		ncv.uName = uName;
		ncv.vName = vName;
		ncv.wName = wName;
		ncv.uVar = ncv.uFile.findVariable(uName);
		ncv.vVar = ncv.vFile.findVariable(vName);
		ncv.wVar = ncv.wFile.findVariable(wName);
		ncv.setXLookup(lonName);
		ncv.setYLookup(latName);
		ncv.setZLookup(kName);
		ncv.setTLookup(tName);
		ncv.setTimeOffset(timeOffset);
		return ncv;
	}
}