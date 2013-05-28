package au.gov.ga.conn4d.impl.readers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import au.gov.ga.conn4d.VelocityReader;
import au.gov.ga.conn4d.utils.FilenamePatternFilter;
import au.gov.ga.conn4d.utils.IndexLookup_Nearest;
import au.gov.ga.conn4d.utils.TimeConvert;
import au.gov.ga.conn4d.utils.TricubicSplineInterpolatingFunction;
import au.gov.ga.conn4d.utils.TricubicSplineInterpolator;
//import au.gov.ga.conn4d.utils.TriCubicSpline;
import au.gov.ga.conn4d.utils.VectorMath;

/**
 * Reads 3D Velocity values from a collection of 3 NetCDF files (u,v,w)
 * 
 * @author Johnathan Kool
 */

public class VelocityReader_NetCDFDir_4D implements VelocityReader, Cloneable {

	private double u, v, w;
	private Map<Long, NetcdfFile> uFiles = new TreeMap<Long, NetcdfFile>();
	private Map<Long, NetcdfFile> vFiles = new TreeMap<Long, NetcdfFile>();
	private Map<Long, NetcdfFile> wFiles = new TreeMap<Long, NetcdfFile>();
	private ArrayList<Long> uKeys = new ArrayList<Long>();
	private ArrayList<Long> vKeys = new ArrayList<Long>();
	private ArrayList<Long> wKeys = new ArrayList<Long>();
	private String freqUnits = "Days";
	private final int kernelSize = 5;
	private final int halfKernel = kernelSize / 2;
	private final int zKernelSize = 3;
	private final int zHalfKernel = zKernelSize / 2;
	private int pidx;
	private float cutoff = 1E3f;
	private boolean positiveDown = true;
	private NetcdfFile uFile, vFile, wFile;
	private Variable latVar, lonVar, zVar, tVar;
	private Variable uVar, vVar, wVar;
	private Array uArr, vArr, wArr, latArr, zArr;
	private IndexLookup_Nearest xloc, yloc, zloc, tloc;
	//private TriCubicSpline tcs = new TriCubicSpline(new double[zKernelSize],
	//		new double[kernelSize], new double[kernelSize],
	//		new float[zKernelSize][kernelSize][kernelSize]);
	private final double[] NODATA = { Double.NaN, Double.NaN, Double.NaN };
	private String latName = "Latitude";
	private String lonName = "Longitude";
	private String zName = "Depth";
	private String tName = "Time";
	private String dir;
	private double[] latvec, zvec;
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
	 * No-argument constructor
	 */

	public VelocityReader_NetCDFDir_4D() {
	}

	/**
	 * Constructor accepting a String indicating the path of the directory
	 * containing a set of NetCDF files.
	 * @param dir
	 * @throws IOException
	 */
	
	public VelocityReader_NetCDFDir_4D(String dir) throws IOException {
		initialize(dir);
	}

	public void initialize(String dir) throws IOException {
		this.dir = dir;
		File f = new File(dir);
		if (!f.isDirectory()) {
			throw new IllegalArgumentException(f.getName()
					+ " is not a directory.");
		}

		// Filter the list of files

		File[] fa = f.listFiles(new FilenamePatternFilter(".*_[uvw]_.*\\.nc"));

		for (File fil : fa) {

			String name = fil.getName();

			NetcdfFile ncf = NetcdfFile.open(fil.getPath());
			tVar = ncf.findVariable(tName);

			Array arr = tVar.read();

			// Convert into a java array

			double[] ja = (double[]) arr.copyTo1DJavaArray();

			// Determine the minimum and maximum times

			long[] minmax = new long[2];

			minmax[0] = TimeConvert.HYCOMToMillis((long) ja[0]);
			minmax[1] = TimeConvert.HYCOMToMillis((long) ja[ja.length - 1]);

			// Put into an index linking start time with the associated file

			if (name.substring(name.indexOf("_") + 1, name.indexOf("_") + 2)
					.equalsIgnoreCase("u")) {
				uFiles.put(minmax[0], ncf);
			}

			if (name.substring(name.indexOf("_") + 1, name.indexOf("_") + 2)
					.equalsIgnoreCase("v")) {
				vFiles.put(minmax[0], ncf);
			}
			if (name.substring(name.indexOf("_") + 1, name.indexOf("_") + 2)
					.equalsIgnoreCase("w")) {
				wFiles.put(minmax[0], ncf);
			}
		}

		// If there are no files in one of the index collections, then exit.

		if (uFiles.size() == 0 || vFiles.size() == 0) {
			System.out
					.println("Velocity directory is missing a file set, or files are not named properly."
							+ "Files  must be named as *_u_*, *_v_*, and *_w_*.");

			System.exit(0);
		}

		uKeys = new ArrayList<Long>(uFiles.keySet());
		vKeys = new ArrayList<Long>(vFiles.keySet());
		wKeys = new ArrayList<Long>(wFiles.keySet());

		uFile = uFiles.get(uKeys.get(0));
		vFile = vFiles.get(vKeys.get(0));
		wFile = wFiles.get(wKeys.get(0));

		uVar = uFile.findVariable(uName);
		vVar = vFile.findVariable(vName);
		wVar = wFile.findVariable(wName);

		// Latitude and depth are read here because they should not change
		// and therefore can be input once only.

		latVar = uFile.findVariable(latName);
		zVar = uFile.findVariable(zName);
		latArr = latVar.read();
		zArr = zVar.read();
		latvec = (double[]) latArr.copyTo1DJavaArray();
		zvec = (double[]) zArr.copyTo1DJavaArray();
		if (positiveDown) {
			VectorMath.negate(zvec);
		}

		setYLookup(latName);
		setZLookup(zName);

		bounds[0][0] = uKeys.get(0);
	}

	private void checkTime(long time) {

		// Right now, the lookups are solely based on the dimensions (lats,
		// lons, depth, time)
		// of the u velocity file, assuming that u v w and w share common
		// Dimensions. Otherwise
		// we'd need to set up independent XYZ and T lookups for UV and W
		// increasing computational overhead, when this really shouldn't be
		// necessary.
		// BUT, it might be a good idea to ensure that the velocity files are
		// consistent.

		int uidx = Collections.binarySearch(uKeys, time);
		// int vidx = Collections.binarySearch(vKeys, time);
		// int widx = Collections.binarySearch(wKeys, time);

		if (uidx < 0) {
			uidx = -(uidx + 2);
		}

		// if (vidx < 0) {
		// vidx = -(vidx + 2);
		// }

		// if (widx < 0) {
		// widx = -(widx + 2);
		// }

		if (pidx != uidx) {

			uFile = uFiles.get(uKeys.get(uidx));
			vFile = vFiles.get(vKeys.get(uidx));
			wFile = wFiles.get(wKeys.get(uidx));

			uVar = uFile.findVariable(uName);
			vVar = vFile.findVariable(vName);
			wVar = wFile.findVariable(wName);

			setTLookup(tName);
			setXLookup(lonName);
			// setYLookup(latName);
			// setZLookup(zName);

			pidx = uidx;
		}
	}

	/**
	 * Clones the VelocityReader_NetCDF_3D object.
	 */

	@Override
	public VelocityReader_NetCDFDir_4D clone() {
		try {
			VelocityReader_NetCDFDir_4D ndr = new VelocityReader_NetCDFDir_4D();
			ndr.dir = dir;
			ndr.freqUnits = freqUnits;
			ndr.latName = latName;
			ndr.lonName = lonName;
			ndr.uName = uName;
			ndr.vName = vName;
			ndr.wName = wName;
			ndr.zName = zName;
			ndr.tName = tName;

			ndr.initialize(dir);

			ndr.setXLookup(ndr.latName);
			ndr.setYLookup(ndr.lonName);
			ndr.setZLookup(ndr.zName);
			ndr.setTLookup(ndr.tName);

			return ndr;
		} catch (IOException e) {
			System.out
					.println("WARNING:  Unexpected error.  NetCDFDirReader could not be cloned.\n");
			return null;
		}
	}

	@Override
	public void close() {
		try {
			if (uFile != null) {
				uFile.close();
			}
			if (vFile != null) {
				vFile.close();
			}
			if (wFile != null) {
				wFile.close();
			}
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

		if (xloc != null) {
			xloc.close();
			xloc = null;
		}
		if (yloc != null) {
			yloc.close();
			yloc = null;
		}
		if (zloc != null) {
			zloc.close();
			zloc = null;
		}
		if (tloc != null) {
			tloc.close();
			tloc = null;
		}
	}

	/**
	 * Retrieves average velocity values associated with the last queried
	 * position.
	 */

	public double[] getAverages() {
		return averages;
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
	 * Retrieves the minimum and maximum values of the t, z, y and x dimensions
	 */

	@Override
	public double[][] getBounds() {
		return bounds;
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

		if (Double.isNaN(lon) || Double.isNaN(lat)) {
			throw new IllegalArgumentException(
					"Latitude or Longitude value is NaN");
		}

		checkTime(time);

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

			if (zloc.isIn_Bounds() < 0) {
				this.notifyAll();
				return null;
			}

			// Beyond the vertical ocean surface

			if (zloc.isIn_Bounds() > 0) {
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
			int semiblock = (idim * (jdim + 1) * kdim) / 2;

			int[] origin = new int[] { ts, kstart, istart, jstart };
			int[] shape = new int[] { 1, kdim, idim, jdim };

			// Array latArr = null, lonArr = null, zArr = null;
			Array lonArr = null;

			// Here we are subsetting the dimensions. Reading an Array from the
			// Variable
			// seems to be inefficient, probably because they are large and are
			// not in memory.
			// For the latitude and z arrays, which should not change structure
			// -
			// we can instead read the arrays built during initialization.

			try {
				// latArr = latVar.read(new int[] { istart }, new int[] { idim
				// });
				lonArr = lonVar.read(new int[] { jstart }, new int[] { jdim });
				// zArr = zVar.read(new int[] { kstart }, new int[] { kdim });

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

			// Convert the Arrays into Java arrays - because latitude and z
			// should
			// be consistent among files, we subset from a constant array.

			double[] latja = Arrays.copyOfRange(latvec, istart, istart + idim);
			// double[] latja = (double[]) latArr.copyTo1DJavaArray();
			double[] lonja = (double[]) lonArr.copyTo1DJavaArray();
			// double[] zja = (double[]) zArr.copyTo1DJavaArray();
			double[] zja = Arrays.copyOfRange(zvec, kstart, kstart + kdim);

			// Obtain the interpolated values

			//int[] dim = tcs.getDim();

			//if (dim[0] != zja.length || dim[1] != latja.length
			//		|| dim[2] != lonja.length) {
			//	tcs = new TriCubicSpline(zja, latja, lonja, autmp);
			//} else {
			//	tcs.resetData(zja, latja, lonja, autmp);
			//}
			
			TricubicSplineInterpolator tci = new TricubicSplineInterpolator();
			TricubicSplineInterpolatingFunction tsf = tci.interpolate(lonja, latja, zja, autmp);

			//try {
				//u = tcs.interpolate(z, lat, lon);
			u = tsf.value(lon,lat,z);
				//tcs.setValues(avtmp);
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
	 * Retrieves the name of the Depth variable
	 */

	public String getZName() {
		return zName;
	}

	/**
	 * Indicates if the position is near an element with NoData
	 */

	@Override
	public boolean isNearNoData() {
		return nearNoData;
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
		bounds[0][0] = tloc.getMinVal() + timeOffset;
		bounds[0][1] = tloc.getMaxVal() + timeOffset;

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
	 * Sets the u-velocity file to be used by this class
	 * 
	 * @param _velocityFile
	 *            - the v-velocity NetCDF object
	 * @param uName
	 *            - the name of the v variable
	 * @throws IOException
	 */

	public void setUFile(String _velocityFile, String uName) throws IOException {
		uFile = NetcdfFile.open(_velocityFile);
		uVar = uFile.findVariable(uName);
		this.uName = uName;
	}

	/**
	 * Sets the name of the velocity field to be used by this class
	 * 
	 * @param uName
	 *            - the name of the u variable
	 * @throws IOException
	 */

	public void setUName(String uName) throws IOException {
		this.uName = uName;
		if (uFile != null) {
			uVar = uFile.findVariable(uName);
		}
	}

	public void setUnits(String units) {
		this.freqUnits = units;
	}

	/**
	 * Sets the v-velocity file to be used by this class
	 * 
	 * @param _velocityFile
	 *            - the v-velocity NetCDF object
	 * @param vName
	 *            - the name of the v variable
	 * @throws IOException
	 */

	public void setVFile(String _velocityFile, String vName) throws IOException {
		this.vName = vName;
		vFile = NetcdfFile.open(_velocityFile);
		if (vFile != null) {
			vVar = vFile.findVariable(vName);
		}
	}

	/**
	 * Sets the name of the velocity field to be used by this class
	 * 
	 * @param vName
	 *            - the name of the v variable
	 * @throws IOException
	 */

	public void setVName(String vName) throws IOException {
		this.vName = vName;
		if (vFile != null) {
			vVar = vFile.findVariable(vName);
		}
	}

	/**
	 * Sets the w-velocity file to be used by this class
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
	 * Sets the name of the velocity field to be used by this class
	 * 
	 * @param wName
	 *            - the name of the w variable
	 * @throws IOException
	 */

	public void setWName(String wName) throws IOException {
		this.wName = wName;
		if (wFile != null) {
			wVar = wFile.findVariable(wName);
		}
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
		zVar = uFile.findVariable(zName);
		if (zVar == null) {
			System.out
					.println("Incorrect variable match:\n\nVariables in local config file: "
							+ zName + ".");
			System.out.println("Velocity file variables: "
					+ uFile.getVariables().toString() + "\n");
		}
		zloc = new IndexLookup_Nearest(zVar);
		zloc.setNegate(true);
		bounds[1][0] = zloc.getMinVal();
		bounds[1][1] = zloc.getMaxVal();
	}

	/**
	 * Sets the name of the depth variable
	 * 
	 * @param zName
	 */

	public void setZName(String zName) {
		this.zName = zName;
	}
}
