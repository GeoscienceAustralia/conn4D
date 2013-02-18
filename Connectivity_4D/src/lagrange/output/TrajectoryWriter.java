package lagrange.output;

import lagrange.Particle;

/**
 * Interface for writing trajectory output.
 * 
 * @author Johnathan Kool
 *
 */

public interface TrajectoryWriter {
	
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
	
	/**
	 * Sets the time units for the output (e.g. Days, Date)
	 * 
	 * @param units
	 */
	
	public void setTimeUnits(String units);
	
	/**
	 * Sets the duration units for the output (e.g. Days, Date)
	 * 
	 * @param units
	 */
	
	public void setDurationUnits(String units);
	
	public void setNegCoord(boolean negCoord);
}
