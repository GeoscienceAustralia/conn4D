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

import au.gov.ga.conn4d.Mortality;
import au.gov.ga.conn4d.Particle;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.engine.RandomSeedTable;

/**
 * Implements mortality using an exponential function. The mortality rate is
 * always set using milliseconds, even though the duration over which it is
 * applied (timeInterval - in milliseconds) may change. In other words,
 * time conversions to millisecond units must be performed externally to this
 * class.
 * 
 * @author Johnathan Kool
 */

public class Mortality_Exponential implements Mortality, Cloneable {

	private int seed = RandomSeedTable.getSeedAtRowColumn(
			Uniform.staticNextIntFromTo(0, Integer.MAX_VALUE),
			Uniform.staticNextIntFromTo(0, RandomSeedTable.COLUMNS));
	private RandomEngine re = new MersenneTwister64(seed);
	private Uniform uni = new Uniform(re);
	private long timeInterval;
	private double mrate;

	public Mortality_Exponential(double mrate) {
		this.mrate = mrate;
	}

	/**
	 * Applies probabilistic mortality the given particle
	 */

	@Override
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
	 * Retrieves the mortality rate per millisecond
	 * 
	 * @return - the mortality rate per millisecond
	 */

	public double getMrate() {
		return mrate;
	}

	/**
	 * Returns the time interval over which mortality occurs
	 * 
	 * @return - the time interval over which mortality occurs
	 */

	public long getTimeIntervalMillis() {
		return timeInterval;
	}

	/**
	 * Sets the mortality rate as a per millisecond rate
	 * 
	 * @param mrate
	 *            - the mortality rate as a per millisecond rate
	 */

	public void setMrate(double mrate) {
		this.mrate = mrate;
	}

	/**
	 * Sets the time interval over which mortality occurs
	 */

	@Override
	public void setTimeInterval(long millis) {
		this.timeInterval = millis;
	}

	/**
	 * Generates a copy of the class instance
	 */

	@Override
	public Mortality_Exponential clone() {
		Mortality_Exponential me = new Mortality_Exponential(mrate);
		me.setTimeInterval(timeInterval);
		return me;
	}
}
