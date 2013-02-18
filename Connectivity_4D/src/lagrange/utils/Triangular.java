package lagrange.utils;

import cern.jet.random.Uniform;

public class Triangular {

	private double a;
	private double b;
	private double c;
	private double midpoint;
	
	public Triangular(double min, double max, double mode){
		a = min;
		b = max;
		c = mode;
		calcMidpoint();
	}
	
	public double nextDouble(){
		double u = Uniform.staticNextDouble();
		if (u < midpoint){
			return a + Math.sqrt(u*(b-a)*(c-a));
		}
		else{
			return b - Math.sqrt((1-u)*(b-a)*(b-c));
		}
	}
	
	private void calcMidpoint(){
		midpoint = (c-a)/(b-a);
	}
}
