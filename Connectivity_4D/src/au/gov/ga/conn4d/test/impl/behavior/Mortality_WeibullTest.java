package au.gov.ga.conn4d.test.impl.behavior;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.impl.behavior.Mortality_Weibull;
import au.gov.ga.conn4d.utils.TimeConvert;

public class Mortality_WeibullTest {

	private Mortality_Weibull mw;
	private final double reps = 1E6;

	@Before
	public void setUp() {
		mw = new Mortality_Weibull(0, 1);
	}

	/**
	 * Prints out values for the number of specified reps (1E6). Values should
	 * conform to an exponential distribution curve. Carrying out a unit test
	 * with guaranteed results is difficult due to the stochastic nature of the
	 * function.
	 * 
	 * @param args
	 */

	@Test
	public void testApplyParticle() {

		Particle p = new Particle();

		// Mortality is originally 0. No Particles should die on the first pass.
		for (int i = 0; i < reps; i++) {
			mw.apply(p);
			if (p.isDead()) {
				fail();
			}
		}

		mw.setDelta_t(TimeConvert.convertToMillis("Days", 1));
		mw.setK(1);
		mw.setLambda(1);
		mw.setTimeInterval(TimeConvert.convertToMillis("Days", 1));

		long sum = 0;

		for (int i = 0; i < reps; i++) {
			p = new Particle();
			mw.apply(p);
			if (!p.isDead()) {
				sum++;
			}
		}

		double p_surv = (double) sum / reps;
		assertEquals(p_surv,0.367879,1E-2);
	}

	@Test
	public void testClone() {
		Mortality_Weibull clone = mw.clone();
		assertFalse(clone == mw);
		assertTrue(clone.getK() == mw.getK());
		assertTrue(clone.getLambda() == mw.getLambda());
		assertTrue(clone.getTimeIntervalMillis() == mw.getTimeIntervalMillis());
	}
}
