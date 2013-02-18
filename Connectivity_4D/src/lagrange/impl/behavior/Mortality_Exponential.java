package lagrange.impl.behavior;

import lagrange.Mortality;
import lagrange.Particle;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.engine.RandomSeedTable;
import lagrange.Process;

/**
 * Implements mortality using an exponential function.
 * 
 * @author Johnathan Kool
 */

public class Mortality_Exponential implements Mortality, Process {

	private int seed = RandomSeedTable.getSeedAtRowColumn(
			Uniform.staticNextIntFromTo(0, Integer.MAX_VALUE),
			Uniform.staticNextIntFromTo(0, RandomSeedTable.COLUMNS));
	private RandomEngine re = new MersenneTwister64(seed);
	private Uniform uni = new Uniform(re);
	private long timeInterval;
	private double mrate;

	// private String units;

	public Mortality_Exponential(double mrate) {
		this.mrate = mrate;
	}

	/**
	 * Applies probabilistic mortality the given particle
	 */

	public void apply(Particle p) {

		if (uni.nextDouble() > Math.exp(-1.0 * mrate * timeInterval)) {
			p.setDead(true);
		}
	}

	/**
	 * Applies probabilistic mortality the given particle
	 */

	public void apply(Particle p, double cycles) {

		if (uni.nextDouble() > Math.exp(-1.0 * mrate * cycles * timeInterval)) {
			p.setDead(true);
		}
	}
	
	/**
	 * Retrieves the mortality rate
	 * 
	 * @return
	 */

	public double getMrate() {
		return mrate;
	}
	
	public double getTimeIntervalMillis(){
		return timeInterval;
	}

	/**
	 * Sets the mortality rate
	 * 
	 * @param mrate
	 */

	public void setMrate(double mrate) {
		this.mrate = mrate;
	}
	
	public void setTimeInterval(long millis){
		this.timeInterval = millis;
	}

	public Mortality_Exponential clone() {
		Mortality_Exponential me = new Mortality_Exponential(mrate);
		me.setTimeInterval(timeInterval);
		return me;
	}

}
