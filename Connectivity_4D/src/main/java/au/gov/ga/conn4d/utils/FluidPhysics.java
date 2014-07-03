package au.gov.ga.conn4d.utils;

import au.gov.ga.conn4d.Particle;

public class FluidPhysics {

	private final double g = 9.80665;
	// private static double fluidDensity = 1;
	private double fluidDensity = 1.25;
	// private final double fluidDensity = 1.025;
	private final double F_1_3 = 1d / 3d;
	private final double F_1_5 = 1d / 5d;
	private final double F_1_7 = 1d / 7d;
	private final double F_1_9 = 1d / 9d;
	private final double F_1_11 = 1d / 11d;
	private final double F_1_13 = 1d / 13d;
	private final double F_1_15 = 1d / 15d;
	private final double F_1_17 = 1d / 17d;
	
	/**
	 * Given an initial velocity, calculates the time equivalent
	 * required for a particle to reach that velocity.  If the particle
	 * is at or beyond terminal velocity (e.g. through a change in fluid
	 * density), then a value of positive infinity is returned.
	 * 
	 * @param p
	 * @param v0
	 * @return
	 */

	public double calcTimeEquivalent(Particle p, double v0) {
		
		double t1 = -Math.sqrt(2
				* p.getMass()
				/ (Math.abs(p.getDensity() - fluidDensity) * g * p.getDragCoefficient()
						* p.getxArea() * fluidDensity));
		
		double inner = Math
				.sqrt(p.getDragCoefficient()
						* p.getxArea()
						* fluidDensity
						/ (2 * p.getMass()
								* Math.abs(p.getDensity() - fluidDensity) * g))
				* v0;

		if(Math.abs(inner)>1){
			return Double.POSITIVE_INFINITY;
		}
		
		double t2 = atanh(inner);
		
		return t1*t2;
	}
	
	/**
	 * Calculates the sinking/buoyant velocity of a particle 
	 * in a fluid over time.
	 * 
	 * @param p
	 * @param time
	 * @return
	 */

	public double calcVelocity(Particle p, double time) {
		
		if(p.getDensity()==fluidDensity){return 0;}
		
		double multiplier = Math.signum(time)*Math.signum(p.getDensity()-fluidDensity);
		double t1_1 = 2 * p.getMass() * Math.abs(p.getDensity() - fluidDensity) * g;
		double t1_2 = p.getDragCoefficient() * p.getxArea() * fluidDensity;
		double t2_1 = g * Math.abs(p.getDensity() - fluidDensity)* p.getDragCoefficient() * p.getxArea() * fluidDensity;
		double t2_2 = 2 * p.getMass();
		
		return multiplier*(-Math.sqrt(t1_1/t1_2)*Math.tanh(Math.sqrt(t2_1/t2_2)*time));
	}
	
	

	public double calcVelocityChange(Particle p, double v0, double time) {
		double teq = calcTimeEquivalent(p,v0);
		return calcVelocity(p, teq + time) - v0;
	}
	
	public double calcVelocityChange(Particle p, double ambient, double v0, double time){
		
		if(fluidDensity==0){return (-g * time) + ambient +v0;}
		
		double teq = calcTimeEquivalent(p,v0-ambient);
		if(teq==Double.POSITIVE_INFINITY){
			return terminalVelocity(p) + ambient -v0;
		}
		
		return calcVelocity(p, teq + time) + ambient;
	}

	/**
	 * Returns the terminal velocity of the particle in fluid.
	 * 
	 * @param p - The particle traveling through the fluid medium.
	 * @return
	 */
	
	public double terminalVelocity(Particle p){
		return -Math.sqrt(2*p.getMass()*g/(p.getDragCoefficient()*p.getxArea()*(p.getDensity() - fluidDensity)));
	}

	// Copied from Apache Commons Math v3.0
	
	private double atanh(double a) {
		boolean negative = false;
		if (a < 0) {
			negative = true;
			a = -a;
		}

		double absAtanh;
		if (a > 0.15) {
			absAtanh = 0.5 * Math.log((1 + a) / (1 - a));
		} else {
			final double a2 = a * a;
			if (a > 0.087) {
				absAtanh = a
						* (1 + a2
								* (F_1_3 + a2
										* (F_1_5 + a2
												* (F_1_7 + a2
														* (F_1_9 + a2
																* (F_1_11 + a2
																		* (F_1_13 + a2
																				* (F_1_15 + a2
																						* F_1_17))))))));
			} else if (a > 0.031) {
				absAtanh = a
						* (1 + a2
								* (F_1_3 + a2
										* (F_1_5 + a2
												* (F_1_7 + a2
														* (F_1_9 + a2
																* (F_1_11 + a2
																		* F_1_13))))));
			} else if (a > 0.003) {
				absAtanh = a
						* (1 + a2
								* (F_1_3 + a2
										* (F_1_5 + a2 * (F_1_7 + a2 * F_1_9))));
			} else {
				absAtanh = a * (1 + a2 * (F_1_3 + a2 * F_1_5));
			}
		}

		return negative ? -absAtanh : absAtanh;
	}
	
	/**
	 * Sets the density of the fluid medium.
	 * 
	 * @param fluidDensity
	 */
	
	public void setFluidDensity(double fluidDensity){
		this.fluidDensity = fluidDensity;
	}
	
	/**
	 * Retrieves the density of the fluid medium.
	 * 
	 * @return
	 */
	
	public double getFluidDensity(){
		return fluidDensity;
	}
}
