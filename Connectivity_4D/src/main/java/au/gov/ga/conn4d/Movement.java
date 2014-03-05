package au.gov.ga.conn4d;

/**
 * Movement interface.
 * 
 * @author Johnathan Kool
 *
 */

public interface Movement {
	
	/**
	 * This method changes the properties of the particle object accordingly.
	 * 
	 * @param p - The particle to be acted upon
	 */
	
	public void apply(Particle p);
	
	/**
	 * Generates a clone of the Movement instance.
	 */
	
	public Movement clone();
	
	/**
	 * Releases resources associated with the Movement instance.
	 */
	
	public void close();
}
