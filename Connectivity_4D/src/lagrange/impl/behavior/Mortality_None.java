package lagrange.impl.behavior;

import lagrange.Mortality;
import lagrange.Particle;
import lagrange.Process;

public class Mortality_None implements Mortality, Process, Cloneable{
	public void apply(Particle p){}
	public void apply(Particle p, double cycles){}
	public Mortality clone(){return new Mortality_None();}
	public void setTimeInterval(long millis){}
}
