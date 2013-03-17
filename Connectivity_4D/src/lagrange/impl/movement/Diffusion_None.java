package lagrange.impl.movement;

import lagrange.Diffusion;
import lagrange.Particle;

/**
 * Diffusion implementation that performs no action.
 * 
 * @author Johnathan Kool
 *
 */

public class Diffusion_None implements Diffusion, Cloneable {
	@Override
	
	/**
	 * Applies turbulent diffusion velocity to a particle.  In this case,
	 * no action is performed.
	 */
	
	public void apply(Particle p){}
	
	/**
	 * Returns a copy of the class instance
	 */
	
	@Override
	public Diffusion_None clone(){return new Diffusion_None();}
}
