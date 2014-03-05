package au.gov.ga.conn4d.output;

import au.gov.ga.conn4d.Particle;

/**
 * Interface for writing summarized connectivity matrix values.
 * 
 * @author Johnathan Kool
 *
 */

public interface MatrixWriter {
	
	/**
	 * This method performs persistence operations for the particle
	 * 
	 * @param p - The particle to be acted upon
	 */
	
	public void apply(Particle p);
	
	/**
	 * Release resources associated with the class
	 */
	
	public void close();
}
