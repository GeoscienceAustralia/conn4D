package au.gov.ga.conn4d.test.impl.behavior;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.impl.behavior.Mortality_Exponential;
import au.gov.ga.conn4d.utils.TimeConvert;

public class Mortality_ExponentialTest {

	private final double reps = 1E6;
	private Mortality_Exponential me;
	
	@Before
	public void setUp(){
		me = new Mortality_Exponential(0);
	}
	
	/**
	 * Prints out values for the number of specified reps (1E6).
	 * Values should conform to an exponential distribution curve.  Carrying out a 
	 * unit test with guaranteed results is difficult due to the stochastic nature 
	 * of the function.
	 */

	@Test
	public void testApplyParticle() {
		
		Particle p = new Particle();
		
		// Mortality is originally 0.  No Particles should die on the first pass.
		for(int i = 0; i < reps; i++){
			me.apply(p);
			if(p.isDead()){fail();}
		}
		
		me.setMrate(TimeConvert.convertFromMillis("Days", 0.1));
		me.setTimeInterval(TimeConvert.convertToMillis("Days", 1));
		
		long sum = 0;
		
		for(int i = 0; i < reps; i++){
			p = new Particle();
			me.apply(p);
			if(!p.isDead()){sum++;}
		}
		
		double p_surv = (double) sum / reps;
		assertEquals(p_surv,Math.exp(-0.1*1),1E-3);
		
		me.setMrate(TimeConvert.convertFromMillis("Days", 0.07));
		me.setTimeInterval(TimeConvert.convertToMillis("Days", 4));
		
		sum = 0;
		
		for(int i = 0; i < reps; i++){
			p = new Particle();
			me.apply(p);
			if(!p.isDead()){sum++;}
		}
		
		p_surv = (double) sum / reps;
		assertEquals(p_surv,Math.exp(-0.07*4),1E-3);
	}

	@Test
	public void testApplyParticleDouble() {
		me.setMrate(TimeConvert.convertFromMillis("Days", 0.1));
		me.setTimeInterval(TimeConvert.convertToMillis("Days", 1));
		
		long sum = 0;
		Particle p;
		
		for(int i = 0; i < reps; i++){
			p = new Particle();
			me.apply(p,3);
			if(!p.isDead()){sum++;}
		}
		
		double p_surv = (double) sum / reps;
		assertEquals(p_surv,Math.exp(-0.1*1*3),1E-3);
		
	}

	@Test
	public void testClone() {
		Mortality_Exponential clone = me.clone();
		assertFalse(clone==me);
		assertTrue(clone.getMrate()==me.getMrate());
		assertTrue(clone.getTimeIntervalMillis()==me.getTimeIntervalMillis());
	}
}
