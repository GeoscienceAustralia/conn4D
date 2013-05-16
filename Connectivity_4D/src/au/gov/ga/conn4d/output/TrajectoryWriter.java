package au.gov.ga.conn4d.output;

import au.gov.ga.conn4d.Particle;

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
	
	/**
	 * Sets whether the output coordinate system should use
	 * negative longitude values.
	 * 
	 * @param negCoord
	 */
	
	public void setNegCoord(boolean negCoord);
}
