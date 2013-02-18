package lagrange;

/**
 * Mortality interface.
 * 
 * @author Johnathan Kool
 *
 */

public interface Mortality {
	
	/**
	 * This method changes the properties of the particle object accordingly.
	 * 
	 * @param p - The particle to be acted upon
	 */
	
	public void apply(Particle p);
	
	/**
	 * This method changes the properties of the particle object accordingly.
	 * 
	 * @param p - The particle to be acted upon
	 */
	
	public void apply(Particle p, double cycles);
	
	/**
	 * Generates a clone of the Mortality instance
	 * 
	 * @return
	 */
	
	public Mortality clone();
	
	/**
	 * Sets the time interval (in milliseconds) over which the Mortality occurs.
	 * 
	 * @param millis
	 */
	
	public void setTimeInterval(long millis);

}
