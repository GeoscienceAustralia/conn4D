package lagrange;

/**
 * Advection interface.  Used to move a Particle through advection.
 * 
 * @author Johnathan Kool
 *
 */

public interface Advection {
	
	/**
	 * This method changes the properties of the particle object accordingly.
	 * 
	 * @param p - The particle to be acted upon
	 */
	
	public void apply(Particle p);
	
	/**
	 * Indicates whether the particle being moved is in proximity to NoData
	 * (used in efficiently applying the land detection routine).
	 * 
	 * @return
	 */
	
	public boolean isNearNoData(Particle p);
	
	/**
	 * Generates a clone of the Advection instance 
	 * @return
	 */
	
	public Advection clone();
	
	/**
	 * Releases resources associated with the object
	 */
	
	public void close();
}
