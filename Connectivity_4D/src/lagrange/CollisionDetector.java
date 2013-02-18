package lagrange;

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
	 * @param p
	 */
	
	public void handleIntersection(Particle p);
	
	/**
	 * Generates a clone of the BoundaryHandler instance.
	 * 
	 * @return
	 */
	
	public boolean isInBounds(long t, double z, double x, double y);
	
	public CollisionDetector clone();
}
