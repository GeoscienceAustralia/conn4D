package lagrange.impl.behavior;

import lagrange.Intersector;
import lagrange.Particle;
import lagrange.Settlement;

public class Settlement_None implements Settlement {

	@Override
	public void apply(Particle p) {}

	@Override
	public void setIntersector(Intersector isect) {}
	
	public Settlement_None clone(){
		return this;
	}

}
