package lagrange.test;

import lagrange.Mortality;
import lagrange.Particle;
import lagrange.output.DistanceWriter;
import lagrange.output.MatrixWriter;
import lagrange.output.TrajectoryWriter;

public class NullPlug implements MatrixWriter, TrajectoryWriter,
		DistanceWriter, Cloneable,Mortality {
	
	public void apply(Particle p){}
	public void apply(Particle p,double cycles){}
	public void close(){}
	public boolean isNearNoData(){return false;}
	public void checkReflect(Particle p){}
	public void flush(){}
	public void setTimeInterval(long d){}
	public void setTimeUnits(String s){}
	public void setDurationUnits(String s){}
	public NullPlug clone(){return this;}
	public void setNegCoord(boolean b){}
}
