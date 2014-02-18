package au.gov.ga.conn4d;

import au.gov.ga.conn4d.output.TrajectoryWriter;

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
	public void close();
	public void setWriter(TrajectoryWriter tw);
	
}
