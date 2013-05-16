package au.gov.ga.conn4d;

/**
 * Interface for generating and managing Release threads using a Release
 * Factory class.
 * 
 * @author Johnathan Kool
 *
 */

public interface ReleaseRunner {

	/**
	 * Generates releases associated with a given set of Parameters
	 * 
	 * @param parameters
	 */
	
	public void run(Parameters parameters);
	
}
