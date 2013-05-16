package lagrange.impl.behavior;

import java.io.File;
import java.util.Arrays;

import lagrange.Particle;
import lagrange.VerticalMigration;
import lagrange.utils.TimeConvert;
import lagrange.utils.VectorMath;
import lagrange.utils.VectorUtils;
import cern.jet.random.Empirical;
import cern.jet.random.EmpiricalWalker;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.engine.RandomSeedTable;

/**
 * Uses a text-based file to obtain probability values of being at a certain
 * depth level (out-of-date class).
 * 
 * @author Johnathan Kool
 */

public class VerticalMigration_Text implements VerticalMigration, Cloneable {

	private long timeInterval = 5;
	private String timeIntervalUnits = "Days";
	private double[][] vmtx;
	// private double[] bins = {5,15,25,40,62.5,87.5,112.5,137.5};
	private double[] bins = { 10, 30, 50, 70 };
	private int seed = RandomSeedTable.getSeedAtRowColumn(
			Uniform.staticNextIntFromTo(0, Integer.MAX_VALUE),
			Uniform.staticNextIntFromTo(0, RandomSeedTable.COLUMNS));
	private RandomEngine re = new MersenneTwister64(seed);
	private EmpiricalWalker ew;

	/*
	 * Depth bins are: 0: 0-10m (5m) 1: 10-20m (15m) 2: 20-30m (25m) 3: 30-50m
	 * (40m) 4: 50-75m (62.5m) 5: 75-100m (87.5m) 6: 100-125m (112.5m) 7:
	 * 125m-150m (137.5m)
	 */

	/**
	 * No-argument constructor
	 */

	public VerticalMigration_Text() {
	}

	/**
	 * Constructor accepting a String containing the path location of an ASCII
	 * file containing a probability matrix for vertical position over time.
	 * 
	 * @param vertfile
	 */

	public VerticalMigration_Text(String vertfile) {
		vmtx = VectorUtils.loadASCIIMatrix(new File(vertfile));
	}

	/**
	 * Applies behavioral-based vertical movement to a Particle object.
	 */
	// *** What if we are in a shallower area than the depth range?

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

		double[] probabilities = VectorMath.norm1(VectorUtils.getColumn(vmtx,
				column));
		ew = new EmpiricalWalker(probabilities, Empirical.NO_INTERPOLATION, re);
		int select = ew.nextInt();
		int val;

		if (p.getZ() > bins[select]) {

			val = Arrays.binarySearch(bins, p.getZ());
			if (val < 0) {
				val = -(val + 1);
			}
			p.setZ(bins[Math.max(0, val - 1)]);
		}

		else if (p.getZ() < bins[select]) {
			val = Arrays.binarySearch(bins, p.getZ());
			if (val < 0) {
				val = -(val + 1);
			}
			p.setZ(bins[Math.min(bins.length - 1, val + 1)]);
		}
	}

	/**
	 * Returns a clone of the class instance
	 */

	@Override
	public VerticalMigration_Text clone() {
		VerticalMigration_Text tvm = new VerticalMigration_Text();
		tvm.timeInterval = timeInterval;
		tvm.timeIntervalUnits = timeIntervalUnits;
		tvm.vmtx = VectorUtils.copy(vmtx);
		return tvm;
	}

	/**
	 * Retrieves the depth bins used by the class.
	 * 
	 * @return - depth bins as an array of doubles
	 */

	public double[] getBins() {
		return bins;
	}

	/**
	 * Retrieves the time interval over which vertical transitioning occurs.
	 * 
	 * @return - the time interval over which vertical transitioning occurs.
	 */

	public long getTimeInterval() {
		return timeInterval;
	}

	/**
	 * Retrieves the units of the time interval over which vertical
	 * transitioning occurs.
	 * 
	 * @return - the units of the time interval over which vertical
	 *         transitioning occurs.
	 */

	public String getTimeIntervalUnits() {
		return timeIntervalUnits;
	}

	/**
	 * Retrieves the transition matrix for vertical movement
	 * 
	 * @return vmtx
	 *            - the transition matrix for vertical movement
	 */

	public double[][] getVmtx() {
		return vmtx;
	}

	/**
	 * Sets the depth bins
	 * 
	 * @return bins
	 *            - depth bins as an array of doubles
	 */

	public void setBins(double[] bins) {
		this.bins = bins;
	}

	/**
	 * Sets the time interval over which vertical transitioning occurs.
	 * 
	 * @param timeInterval
	 *            - the time interval over which vertical transitioning occurs.
	 */

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * Sets the units of the time interval over which vertical transitioning
	 * occurs.
	 * 
	 * @param timeIntervalUnits
	 *            - the units of the time interval over which vertical
	 *            transitioning occurs.
	 */

	public void setTimeIntervalUnits(String timeIntervalUnits) {
		this.timeIntervalUnits = timeIntervalUnits;
	}

	/**
	 * Sets the transition matrix for vertical movement
	 * 
	 * @param vmtx
	 *            - the transition matrix for vertical movement
	 */

	public void setVmtx(double[][] vmtx) {
		this.vmtx = vmtx;
	}
}
