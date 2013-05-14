package lagrange.impl.behavior;

import lagrange.Mortality;
import lagrange.Particle;

public class Mortality_None implements Mortality, Cloneable {

	/**
	 * Applies the mortality function (in this case, nothing occurs)
	 */

	@Override
	public void apply(Particle p) {
	}

	/**
	 * Applies the mortality function over a given number of cycles (in this
	 * case, nothing occurs)
	 */

	@Override
	public void apply(Particle p, double cycles) {
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
