package lagrange.impl.movement;

import lagrange.Diffusion;
import lagrange.Particle;

public class Diffusion_None implements Diffusion, Cloneable {
	public void apply(Particle p){}
	public Diffusion_None clone(){return new Diffusion_None();}
}
