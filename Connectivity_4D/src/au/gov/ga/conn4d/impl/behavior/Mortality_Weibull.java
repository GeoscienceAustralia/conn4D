package au.gov.ga.conn4d.impl.behavior;

import au.gov.ga.conn4d.Mortality;
import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.utils.TimeConvert;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.engine.RandomSeedTable;

/**
 * Implements mortality using a Weibull distribution. See:
 * http://en.wikipedia.org/wiki/Weibull_distribution
 * 
 * @author Johnathan Kool
 */

public class Mortality_Weibull implements Mortality {

	private double lambda;
	private double k;
	private long delta_t = 7200000;
	private long timeInterval;
	private String units = "Days";

	private int seed = RandomSeedTable.getSeedAtRowColumn(
			Uniform.staticNextIntFromTo(0, Integer.MAX_VALUE),
			Uniform.staticNextIntFromTo(0, RandomSeedTable.COLUMNS));
	private RandomEngine re = new MersenneTwister64(seed);
	private Uniform uni = new Uniform(re);

	/**
	 * Constructor requiring shape parameters as input.
	 * 
	 * @param lambda
	 *            - lambda shape parameter
	 * @param k
	 *            - k shape parameter
	 */

	public Mortality_Weibull(double lambda, double k) {
		this.lambda = lambda;
		this.k = k;
	}

	/**
	 * Applies probabilistic mortality to the given particle.
	 */

	@Override
	public synchronized void apply(Particle p) {

		// We are working with a discrete time window, therefore we don't want
		// to use the straight pdf. Instead we subtract cdfs. The difference is
		// the change from the previous checkpoint to the current one.

		double t1 = TimeConvert.convertFromMillis(units, p.getAge());
		double t0 = t1 - TimeConvert.convertFromMillis(units, delta_t);

		double p1 = Math.exp(-Math.pow((t1 / lambda), k));
		double p0 = Math.exp(-Math.pow((t0 / lambda), k));

		// Because we're working with survivorship and cdfs, the earlier time
		// step will always be greater than the later - hence p0 - p1.

		// We subtract to get the probability over the time interval, and then
		// divide by 1-p0 to scale appropriately. e.g. If it hasn't happened
		// yet, it *must* at some point (unless the p is in the tail, which is
		// handled through scaling using the remainder).

		double value = (p0 - p1) / (p0);

		if (uni.nextDouble() < value) {
			p.setDead(true);
		}
		this.notifyAll();
	}

	/**
	 * Applies mortality over the given number of cycles
	 */

	@Override
	public synchronized void apply(Particle p, double cycles) {

		// We are working with a discrete time window, therefore we don't want
		// to use the straight pdf. Instead we subtract cdfs. The difference is
		// the change from the previous checkpoint to the current one.

		double t1 = TimeConvert.convertFromMillis(units, p.getAge());
		double t0 = t1 - TimeConvert.convertFromMillis(units, delta_t);

		double p1 = Math.exp(-Math.pow((t1 / lambda), k));
		double p0 = Math.exp(-Math.pow((t0 / lambda), k));

		// Because we're working with survivorship and cdfs, the earlier time
		// step will always be greater than the later - hence p0 - p1.

		// We subtract to get the probability over the time interval, and then
		// divide by 1-p0 to scale appropriately. e.g. If it hasn't happened
		// yet, it *must* at some point (unless the p is in the tail, which is
		// handled through scaling using the remainder).

		double value = (p0 - p1) / (p0);

		if (uni.nextDouble() < value) {
			p.setDead(true);
		}
		this.notifyAll();
	}

	/**
	 * Returns a copy of the class instance
	 */

	@Override
	public Mortality_Weibull clone() {
		Mortality_Weibull mw = new Mortality_Weibull(lambda, k);
		mw.setTimeInterval(timeInterval);
		return mw;
	}

	/**
	 * Retrieves the differential time over which mortality is being checked
	 * 
	 * @return - the differential time over which mortality is being checked
	 */

	public double getDelta_t() {
		return delta_t;
	}

	/**
	 * Returns the k parameter being used by the function
	 * 
	 * @return - the k parameter being used by the function
	 */

	public double getK() {
		return k;
	}

	/**
	 * Returns the lambda parameter being used by the function
	 * 
	 * @return - the lambda parameter being used by the function
	 */

	public double getLambda() {
		return lambda;
	}

	/**
	 * Returns the total time interval over which the mortality is being applied
	 * 
	 * @return - the total time interval over which the mortality is being
	 *         applied
	 */

	public long getTimeIntervalMillis() {
		return timeInterval;
	}

	/**
	 * Sets the time interval since last checking for mortality
	 * 
	 * @param delta_t
	 *            - the time interval since last checking for mortality
	 */

	public void setDelta_t(long delta_t) {
		this.delta_t = delta_t;
	}

	/**
	 * Sets the k parameter being used by the function.
	 * 
	 * @param k
	 *            - the k parameter to be used by the function.
	 */

	public void setK(double k) {
		this.k = k;
	}

	/**
	 * Sets the value of the lambda parameter being used by the function.
	 * 
	 * @param lambda
	 *            - the value of the lambda parameter being used by the
	 *            function.
	 */

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	/**
	 * Sets the time interval over which mortality occurs.
	 * 
	 * @param millis
	 *            - the time interval over which mortality occurs.
	 */

	@Override
	public void setTimeInterval(long millis) {
		this.timeInterval = millis;
	}
}
