package lagrange.impl.collision;

import lagrange.Boundary;
import lagrange.CollisionDetector;
import lagrange.Particle;

public class CollisionDetector_None implements CollisionDetector {

	public void handleIntersection(Particle p) {}
	public CollisionDetector_None clone(){
		return this;
	}
	public boolean isInBounds(long t, double z, double x, double y){return true;}
	public Boundary getBoundary(){return null;}
}
