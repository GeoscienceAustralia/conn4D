package au.gov.ga.conn4d.utils;

import au.gov.ga.conn4d.Particle;

public class FluidPhysics {

	private final double g = 9.80665;
	// private static double fluidDensity = 1;
	private static double fluidDensity = 1.25;
	// private final double fluidDensity = 1.025;
	private static final double F_1_3 = 1d / 3d;
	private static final double F_1_5 = 1d / 5d;
	private static final double F_1_7 = 1d / 7d;
	private static final double F_1_9 = 1d / 9d;
	private static final double F_1_11 = 1d / 11d;
	private static final double F_1_13 = 1d / 13d;
	private static final double F_1_15 = 1d / 15d;
	private static final double F_1_17 = 1d / 17d;

	public static void main(String[] args) {
		FluidPhysics fp = new FluidPhysics();
		double v0 = 0;
		double h = 1;
		Particle p = new Particle();
		System.out.println(fp.calcTimeEquivalent(p, v0));
		System.out.println(fp.calcVelocity(p, fp.calcTimeEquivalent(p, v0)));
		System.out.println(fp.calcVelocityChange(p, v0, h));
	}

	public double calcTimeEquivalent(Particle p, double v0) {
		
		return -Math.sqrt(2
				* p.getMass()
				/ (Math.abs(p.getDensity() - fluidDensity) * g * p.getDragCoefficient()
						* p.getxArea() * fluidDensity))
				* atanh(Math
						.sqrt(p.getDragCoefficient()
								* p.getxArea()
								* fluidDensity
								/ (2 * p.getMass()
										* Math.abs(p.getDensity() - fluidDensity) * g))
						* v0);
	}

	public double calcVelocity(Particle p, double time) {
		
		if(p.getDensity()==fluidDensity){return 0;}
		
		double multiplier = Math.signum(time)*Math.signum(p.getDensity()-fluidDensity);
		
		return multiplier*(-Math.sqrt(2 * p.getMass() * Math.abs(p.getDensity() - fluidDensity) * g
				/ (p.getDragCoefficient() * p.getxArea() * fluidDensity))
				* Math.tanh(Math.sqrt(g * Math.abs(p.getDensity() - fluidDensity)
						* p.getDragCoefficient() * p.getxArea() * fluidDensity
						/ (2 * p.getMass()))
						* time));
	}

	public double calcVelocityChange(Particle p, double v0, double time) {
		double teq = calcTimeEquivalent(p,v0);
		return calcVelocity(p, teq + time) - v0;
	}
	
	public double calcVelocityChange(Particle p, double ambient, double v0, double time){
		double teq = calcTimeEquivalent(p,v0-ambient);
		return calcVelocity(p, teq + time) - (v0-ambient);
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
}
