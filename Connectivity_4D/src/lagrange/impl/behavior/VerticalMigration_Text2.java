package lagrange.impl.behavior;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import lagrange.Boundary;
import lagrange.Particle;
import lagrange.VerticalMigration;
import lagrange.impl.readers.Boundary_Grid_NetCDF;
import lagrange.utils.TimeConvert;
import lagrange.utils.VectorMath;
import lagrange.utils.VectorUtils;
import cern.jet.random.Empirical;
import cern.jet.random.EmpiricalWalker;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;

public class VerticalMigration_Text2 implements VerticalMigration, Cloneable {

	private long timeInterval = 5;
	private String timeIntervalUnits = "Days";
	private double[][] vmtx;
	private double[] binbnd = { 0, 20, 40, 60, 80 };
	private RandomEngine re = new MersenneTwister64(new Date(System
			.currentTimeMillis()));
	private EmpiricalWalker ew;
	private Boundary bathym;
	
	/*
	 * Depth bins are: 0: 0-10m (5m) 1: 10-20m (15m) 2: 20-30m (25m) 3: 30-50m
	 * (40m) 4: 50-75m (62.5m) 5: 75-100m (87.5m) 6: 100-125m (112.5m) 7:
	 * 125m-150m (137.5m)
	 */

	/**
	 * No argument constructor
	 */

	public VerticalMigration_Text2() {
	}

	/**
	 * Constructor that accepts path and filename input as a String.
	 * 
	 * @param vertfile
	 */

	public VerticalMigration_Text2(String vertfile) {
		vmtx = VectorUtils.loadASCIIMatrix(new File(vertfile));
	}

	/**
	 * Transitions a particle vertically within the water column according to a
	 * text-based transition matrix. This method also uniformly distributes
	 * particles within strata, and performs a check using bathymetry
	 * information to ensure that particles do not intercept the seafloor.
	 */

	@Override
	public void apply(Particle p) {

		float blocktime = TimeConvert.convertToMillis(timeIntervalUnits,
				timeInterval);

		// Truncation is OK

		int column = (int) (p.getAge() / blocktime);

		// If age extends beyond the matrix dimensions, use the values
		// of the last column.

		if (column >= vmtx[0].length) {
			column = vmtx[0].length - 1;
		}

		// Retrieve the probabilities from the appropriate matrix column

		double[] probabilities = VectorMath.norm1(VectorUtils.getColumn(vmtx, column));

		// Create the Empirical Walker to generate random values

		ew = new EmpiricalWalker(probabilities, Empirical.NO_INTERPOLATION, re);

		// Get the random value

		int select = ew.nextInt();
		double val;

		// What is the current vertical position?

		int position = getPosition(p.getZ());

		/*
		 * If the position is greater than the bin that was selected, move it
		 * higher in the water column (smaller index value). Determine position
		 * within stratum using a Uniform distribution.
		 */

		if (position > select) {

			//val = Uniform.staticNextDoubleFromTo(binbnd[position - 1],
			//		binbnd[position]);
			val = (binbnd[position - 1]+binbnd[position])/2d;
		}

		/*
		 * If the position is less than the bin that was selected, move it
		 * deeper in the water column (larger index value). Determine position
		 * within stratum using a Uniform distribution.
		 */

		else if (position < select) {
			//val = Uniform.staticNextDoubleFromTo(binbnd[position + 1],
			//		binbnd[position + 2]);
			val = (binbnd[position + 1]+binbnd[position+2])/2d;
		}

		/*
		 * Otherwise we stay in the same bin and choose a new position using the
		 * Uniform distribution.
		 */

		else {
			//val = Uniform.staticNextDoubleFromTo(binbnd[position],
			//		binbnd[position + 1]);
			val = p.getZ();
		}

		/*
		 * The possibility exists that the vertical migration matrix
		 * will run the particle into the ground.  This corrects this
		 * problem using bathymetry information.
		 */

		val = checkDepth(val, p.getX(), p.getY());

		// Set the new Z value.

		p.setZ(val);
	}

	/**
	 * Retrieves the time interval - e.g. 5 (units of time)
	 * 
	 * @return
	 */

	public long getTimeInterval() {
		return timeInterval;
	}

	/**
	 * Sets the time interval - e.g. 5 (units of time)
	 * 
	 * @param timeInterval
	 */

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * Retrieves the time interval units (e.g. Hours, Days)
	 * 
	 * @return
	 */

	public String getTimeIntervalUnits() {
		return timeIntervalUnits;
	}

	/**
	 * Sets the time interval units (e.g. Hours, Days)
	 * 
	 * @param timeIntervalUnits
	 */

	public void setTimeIntervalUnits(String timeIntervalUnits) {
		this.timeIntervalUnits = timeIntervalUnits;
	}

	/**
	 * Retrieves the vertical transition matrix as an array of doubles.
	 * 
	 * @return
	 */

	public double[][] getVmtx() {
		return vmtx;
	}

	/**
	 * Sets the vertical transition matrix (using an array of doubles)
	 * 
	 * @param vmtx
	 */

	public void setVmtx(double[][] vmtx) {
		this.vmtx = vmtx;
	}

	/**
	 * Get the boundaries of the depth bins.
	 * 
	 * @return
	 */

	public double[] getBinBnds() {
		return binbnd;
	}

	/**
	 * Sets the boundaries of the depth bins.
	 * 
	 * @param binbnd
	 */

	public void setBinBnds(double[] binbnd) {
		this.binbnd = binbnd;
		Arrays.sort(binbnd);
	}

	/**
	 * Retrieves the bin that the particle is currently situated in.
	 * 
	 * @param depth
	 * @return
	 */

	private int getPosition(double depth) {
		int pos = Arrays.binarySearch(binbnd, depth);
		if (pos < 0) {
			pos = -(pos + 2);
		}

		return pos;
	}

	/**
	 * Sets the bathymetry layer being used to detect seafloor location.
	 * 
	 * @param filename
	 */

	public void setBathymetry(String filename) {
		try {
			Boundary_Grid_NetCDF ncb = new Boundary_Grid_NetCDF(filename);
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

	private double checkDepth(double depth, double x, double y) {

		double bval = -bathym.getBoundaryDepth(x, y);

		/*
		 * If the bathymetry says we're on land, use the shallowest depth bin.
		 * Land intercepts are handled using shapefiles since it requires an
		 * angle of reflection.
		 */

		if (bval < 0) {
			return Uniform.staticNextDoubleFromTo(0d, 1d);
		}
		
		if (bval < depth){
			return Uniform.staticNextDoubleFromTo(binbnd[getPosition(depth)],bval);
		}

		return depth;
	}
	
	@Override
	public VerticalMigration_Text2 clone(){
		VerticalMigration_Text2 adv = new VerticalMigration_Text2();
		adv.timeInterval=timeInterval;
		adv.timeIntervalUnits=timeIntervalUnits;
		adv.vmtx=VectorUtils.copy(vmtx);
		adv.binbnd = binbnd;
		adv.bathym = bathym.clone();
		return adv;
	}
}
