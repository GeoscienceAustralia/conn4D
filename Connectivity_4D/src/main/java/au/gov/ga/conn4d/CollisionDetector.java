package au.gov.ga.conn4d;

/**
 * Handles behaviour of Particles at intersections with Barriers
 * 
 * @author Johnathan Kool
 *
 */

public interface CollisionDetector {
	
	/**
	 * Adjusts Particle position and condition upon encountering a Barrier.
	 * 
	 * @param p - the Particle to be tested for collision activity.
	 */
	
	public void handleIntersection(Particle p);
	
	/**
	 * Generates a clone of the BoundaryHandler instance.
	 */
	
	public boolean isInBounds(long t, double z, double x, double y);
	
	/**
	 * Returns a copy of the class instance
	 */
	
	public CollisionDetector clone();
	
	/**
	 * Returns the boundary object being used by the CollisionDetector
	 */
	
	public Boundary getBoundary();
}
