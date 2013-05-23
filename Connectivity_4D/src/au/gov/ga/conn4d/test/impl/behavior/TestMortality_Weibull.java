package au.gov.ga.conn4d.test.impl.behavior;

import org.junit.Test;

import au.gov.ga.conn4d.Particle;

import au.gov.ga.conn4d.impl.behavior.Mortality_Weibull;
import au.gov.ga.conn4d.utils.TimeConvert;


public class TestMortality_Weibull {
	
	private final double reps = 1E6;
	private final double lambda = 1/0.635;
	private final double k = .7559;
	private final double t = 90;
	private String t_units = "Days";
	
	/**
	 * Prints out values for the number of specified reps (1E6).
	 * Values should conform to a Weibull distribution curve.  Carrying out a 
	 * unit test with guaranteed results is difficult due to the stochastic nature 
	 * of the function.
	 */
	
	@Test
	public void testDistribution(){
		
		double ct = 0;
		Mortality_Weibull mw = new Mortality_Weibull(lambda,k);
		long time = TimeConvert.convertToMillis(t_units, t);
		mw.setDelta_t(time);
		
		for(int i = 0; i < reps; i++){
			Particle p = new Particle();
			p.setT(time);
			mw.apply(p);
			if(!p.isDead()){
				ct++;
			}
		}	
		System.out.println(ct/reps);	
	}
	
}
