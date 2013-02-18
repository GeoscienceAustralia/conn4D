package lagrange;

/**
 * Turbulent velocity interface.
 * 
 * @author Johnathan Kool
 *
 */

public interface Diffusion {

	/**
	 * This method changes the properties of the particle object accordingly.
	 * 
	 * @param p - The particle to be acted upon
	 */
	
	public abstract void apply(Particle p);
	
	/**
	 * Returns a clone of the Diffusion object
	 * 
	 * @return
	 */
	
	public abstract Diffusion clone();

}