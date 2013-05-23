package au.gov.ga.conn4d;

/**
 * Advection interface.  Used to move a Particle through advection.
 * 
 * @author Johnathan Kool
 *
 */

public interface Advector {
	
	/**
	 * This method changes the properties of the particle object accordingly.
	 * 
	 * @param p - The particle to be acted upon
	 */
	
	public void apply(Particle p);
	
	/**
	 * Generates a clone of the Advection instance 
	 */
	
	public Advector clone();
	
	/**
	 * Releases resources associated with the object
	 */
	
	public void close();
}
