package lagrange;

/**
 * Settlement interface.
 * 
 * @author Johnathan Kool
 *
 */

public interface Settlement {
	
	/**
	 * This method changes the properties of the particle object accordingly.
	 * 
	 * @param p - The particle to be acted upon
	 */
	
	public void apply(Particle p);
	
	/**
	 * Sets the Intersector object for this instance.  Used to determine if Particles
	 * intersect Habitat.
	 * 
	 * @param isect - the Intersector object
	 */
	
	public void setIntersector(Intersector isect);
	
	/**
	 * Generates a clone of the Settlement object.
	 * 
	 * @return - clone of the Settlement object
	 */
	
	public abstract Settlement clone();

}
