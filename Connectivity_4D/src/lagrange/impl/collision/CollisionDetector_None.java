package lagrange.impl.collision;

import lagrange.Boundary;
import lagrange.CollisionDetector;
import lagrange.Particle;

public class CollisionDetector_None implements CollisionDetector {

	@Override
	public void handleIntersection(Particle p) {}
	@Override
	public CollisionDetector_None clone(){
		return this;
	}
	@Override
	public boolean isInBounds(long t, double z, double x, double y){return true;}
	@Override
	public Boundary getBoundary(){return null;}
}
