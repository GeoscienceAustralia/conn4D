package au.gov.ga.conn4d.impl.behavior;

import au.gov.ga.conn4d.Mortality;
import au.gov.ga.conn4d.Particle;

public class Mortality_None implements Mortality, Cloneable {

	/**
	 * Applies the mortality function (in this case, nothing occurs)
	 */

	@Override
	public void apply(Particle p) {
	}

	/**
	 * Generates a copy of this class instance
	 */
	@Override
	public Mortality clone() {
		return new Mortality_None();
	}

	/**
	 * Sets the time interval over which mortality occurs (in this case, nothing
	 * is changed)
	 */
	@Override
	public void setTimeInterval(long millis) {
	}
}
