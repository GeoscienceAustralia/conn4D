package au.gov.ga.conn4d.utils;

import cern.jet.random.Uniform;

/**
 * Generates random numbers according to a triangular distribution.
 * (http://en.wikipedia.org/wiki/Triangular_distribution)
 * 
 * @author Johnathan Kool
 *
 */

public class Triangular {

	private double a;
	private double b;
	private double c;
	private double midpoint;
	
	/**
	 * Triangular distribution three-parameter constructor
	 * 
	 * @param min - the minimum value of the distribution
	 * @param max - the maximum value of the distribution
	 * @param mode - the mode of the distribution
	 */
	
	public Triangular(double min, double max, double mode){
		a = min;
		b = max;
		c = mode;
		calcMidpoint();
	}
	
	/**
	 * Returns the next random value
	 * 
	 * @return
	 */
	
	public double nextDouble(){
		double u = Uniform.staticNextDouble();
		if (u < midpoint){
			return a + Math.sqrt(u*(b-a)*(c-a));
		}
		else{
			return b - Math.sqrt((1-u)*(b-a)*(b-c));
		}
	}
	
	/**
	 * Calculates the midpoint of the distribution
	 */
	
	private void calcMidpoint(){
		midpoint = (c-a)/(b-a);
	}
}
