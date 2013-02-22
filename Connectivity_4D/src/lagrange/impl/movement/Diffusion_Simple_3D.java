package lagrange.impl.movement;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.engine.RandomSeedTable;
import lagrange.Particle;
import lagrange.Diffusion;
import lagrange.utils.GeometryUtils;

/**
 * @author Johnathan Kool based on FORTRAN code developed by Robert K. Cowen,
 *         Claire Paris and Ashwanth Srinivasan.
 */

public class Diffusion_Simple_3D implements Diffusion, Cloneable {

	private float uK = 0.03f / 21600f; //0; The magic number in distance/seconds
	private float vK = 0.03f / 21600f; //0;
	private float wK = 1E-8f / 21600f; //0; Vertical coefficient of diffusivity
	// private float TL = 21600f; // 6 hours in seconds
	private float h; // Minimum integration time step (default=2hrs)

	private int seed = RandomSeedTable.getSeedAtRowColumn(
			Uniform.staticNextIntFromTo(0, Integer.MAX_VALUE),
			Uniform.staticNextIntFromTo(0, RandomSeedTable.COLUMNS));
	private RandomEngine re = new MersenneTwister64(seed);
	private Normal norm = new Normal(0, 1, re);

	public Diffusion_Simple_3D(float h) {
		this.h = h / 1000; // convert to seconds
	}

	/**
	 * Applies turbulent velocity to a particle.
	 * 
	 * The equations are based on equations 8 and 9 of Dimou, K.N. and Adams,
	 * E.E. 1993 - Estuarine and Coastal Shelf Science 37:99-110. A Random-walk,
	 * Particle Tracking Model for Well-Mixed Estuaries and Coastal Waters
	 * 
	 * @param p
	 *            - The particle to be acted upon.
	 */

	public void apply(Particle p) {

		float usc = (float) Math.sqrt(2f * uK * h);
		float vsc = (float) Math.sqrt(2f * vK * h);
		float wsc = (float) Math.sqrt(2f * wK * h);

		double u = usc * (float) norm.nextDouble();
		double v = vsc * (float) norm.nextDouble();
		double w = wsc * (float) norm.nextDouble();

		// Displacement equals velocity over time

		double dx = u * h;
		double dy = v * h;
		double dz = w * h;

		// Determine the new coordinates

		double[] coords = GeometryUtils.latLon(new double[] { p.getY(), p.getX() }, dy,
				dx);

		// Update the particle's coordinates.

		p.setX(coords[1]);
		p.setY(coords[0]);
		p.setZ(p.getZ()+dz);
	}

	/**
	 * Gets the minimum integration time step currently being used (in seconds)
	 * 
	 * @return
	 */

	public float getH() {
		return h * 1000;
	}

	/**
	 * Sets the minimum integration time step (in seconds)
	 * 
	 * @param h
	 */

	public void setH(float h) {
		this.h = h / 1000;
	}

	public Diffusion_Simple_3D clone() {
		return new Diffusion_Simple_3D(h * 1000);
	}
}
