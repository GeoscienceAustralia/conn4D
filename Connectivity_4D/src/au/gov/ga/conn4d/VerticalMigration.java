package au.gov.ga.conn4d;

/**
 * Vertical migration interface.
 * 
 * @author Johnathan Kool
 *
 */

public interface VerticalMigration {

	/**
	 * This method changes the properties of the particle object accordingly.
	 * 
	 * @param p - The particle to be acted upon
	 */
	
	public void apply(Particle p);
	
	/**
	 * Generates a clone of the VerticalMigration object
	 */
	
	public VerticalMigration clone();
}
