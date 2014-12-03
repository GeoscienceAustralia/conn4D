package au.gov.ga.conn4d.utils;

import au.gov.ga.conn4d.Particle;

public class FluidPhysics {

	private final double g = 9.80665;
	private double fluidDensity = 1029;
	private final double F_1_3 = 1d / 3d;
	private final double F_1_5 = 1d / 5d;
	private final double F_1_7 = 1d / 7d;
	private final double F_1_9 = 1d / 9d;
	private final double F_1_11 = 1d / 11d;
	private final double F_1_13 = 1d / 13d;
	private final double F_1_15 = 1d / 15d;
	private final double F_1_17 = 1d / 17d;
	
	/**
	 * Given an initial velocity, calculates the time equivalent required for a
	 * particle to reach that velocity. If the particle is at or beyond terminal
	 * velocity (e.g. through a change in fluid density), then a value of
	 * positive infinity is returned.
	 * 
	 * @param p
	 * @param v0
	 * @return
	 */

	public double calcTimeEquivalent(Particle p, double v0) {

		double r1 = p.getDensity();
		double r2 = fluidDensity;
		double m = p.getMass();
		double C = p.getDragCoefficient();
		double A = p.getXArea();

		double t1 = -Math.signum(r1 - r2)
				* Math.sqrt((2 * Math.abs(r1 - r2) * m) / (g * C * A * r1 * r2));
		double inner = v0
				* -Math.signum(r1 - r2)
				* Math.sqrt((C * A * r1 * r2) / (2 * Math.abs(r1 - r2) * g * m));

		if (Math.abs(inner) > 1) {
			return Double.POSITIVE_INFINITY;
		}

		double t2 = atanh(inner);

		return t1 * t2;
	}

	/**
	 * Calculates the sinking/buoyant velocity of a particle in a fluid over
	 * time.
	 * 
	 * @param p
	 * @param time
	 * @return
	 */

	public double calcVelocity(Particle p, double time) {

		double r1 = p.getDensity();
		double r2 = fluidDensity;
		double m = p.getMass();
		double C = p.getDragCoefficient();
		double A = p.getXArea();

		if (p.getDensity() == fluidDensity) {
			return 0;
		}
		return -Math.signum(r1 - r2)
				* Math.sqrt((2 * Math.abs(r1 - r2) * g * m) / (C * A * r1 * r2))
				* Math.tanh(Math.sqrt((g * C * A * r1 * r2)
						/ (2 * Math.abs(r1 - r2) * m))
						* time);
	}

	/**
	 * Calculates the change in particle velocity within a moving reference frame.
	 * 
	 * @param p - the Particle object
	 * @param v0 - the initial velocity of the Particle
	 * @param time - time duration of motion
	 * @return
	 */
	
	public double calcVelocityChange(Particle p, double v0, double time) {
		return calcVelocityChange(p, 0, v0, time);
	}

	/**
	 * Calculates the change in particle velocity within a moving reference frame.
	 * 
	 * @param p - the Particle object
	 * @param ambient - the velocity of the enclosing reference frame
	 * @param v0 - the initial velocity of the Particle
	 * @param time - time duration of motion
	 * @return
	 */
	
	public double calcVelocityChange(Particle p, double ambient, double v0,
			double time) {

		if (fluidDensity == 0) {
			return (-g * time) + ambient + v0;
		}

		double teq = calcTimeEquivalent(p, v0 - ambient);
		if (teq == Double.POSITIVE_INFINITY) {
			return terminalVelocity(p) + ambient - v0;
		}

		return calcVelocity(p, time + teq) + ambient -v0;
	}

	/**
	 * Returns the terminal velocity of the particle in fluid.
	 * 
	 * @param p
	 *            - The particle traveling through the fluid medium.
	 * @return
	 */

	public double terminalVelocity(Particle p) {
		double r1 = p.getDensity();
		double r2 = fluidDensity;
		double m = p.getMass();
		double C = p.getDragCoefficient();
		double A = p.getXArea();
		return -Math.signum(r1 - r2)
				* Math.sqrt((2 * Math.abs(r1 - r2) * g * m) / (C * A * r1 * r2));
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

	public void setFluidDensity(double fluidDensity) {
		this.fluidDensity = fluidDensity;
	}

	/**
	 * Retrieves the density of the fluid medium.
	 * 
	 * @return
	 */

	public double getFluidDensity() {
		return fluidDensity;
	}
}
