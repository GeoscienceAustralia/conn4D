package lagrange.test.behavior;

import lagrange.Particle;
import lagrange.impl.behavior.Mortality_Exponential;
import lagrange.utils.TimeConvert;

public class TestMortality_Exponential {
	
	private final double reps = 1E6;
	private final double mrate = 0.06;
	private final double t = 1;
	private String t_units = "Days";
	
	/**
	 * Prints out values for the number of specified reps (1E6).
	 * Values should conform to an exponential distribution curve.  Carrying out a 
	 * unit test with guaranteed results is difficult due to the stochastic nature 
	 * of the function.
	 *  
	 * @param args
	 */
	
	public static void main(String[] args){
		TestMortality_Exponential tmw = new TestMortality_Exponential();
		tmw.go();
		System.out.println("Complete.");
	}
	
	public void go(){
		
		double ct = 0;
		long time = TimeConvert.convertToMillis(t_units, t);
		Mortality_Exponential me = new Mortality_Exponential(mrate/time);
		me.setTimeInterval(time*5);
		
		for(int i = 0; i < reps; i++){
			Particle p = new Particle();
			p.setT(time);
			me.apply(p);
			if(!p.isDead()){
				ct++;
			}
		}
		System.out.println(ct/reps);	
	}	
}