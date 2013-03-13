package lagrange.impl.movement;

import lagrange.Diffusion;
import lagrange.Particle;

public class Diffusion_None implements Diffusion, Cloneable {
	@Override
	public void apply(Particle p){}
	@Override
	public Diffusion_None clone(){return new Diffusion_None();}
}
