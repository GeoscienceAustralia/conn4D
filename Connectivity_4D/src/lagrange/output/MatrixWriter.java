package lagrange.output;

import lagrange.Particle;

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
