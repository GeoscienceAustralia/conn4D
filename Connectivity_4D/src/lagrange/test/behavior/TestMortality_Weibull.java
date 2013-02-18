package lagrange.test.behavior;

import lagrange.Particle;
import lagrange.impl.behavior.Mortality_Weibull;
import lagrange.utils.TimeConvert;

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
	 *  
	 * @param args
	 */
	
	public static void main(String[] args){
		TestMortality_Weibull tmw = new TestMortality_Weibull();
		tmw.go();
		System.out.println("Complete.");
	}
	
	public void go(){
		
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
