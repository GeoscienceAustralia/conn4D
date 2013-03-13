package lagrange.test;

import lagrange.Mortality;
import lagrange.Particle;
import lagrange.output.DistanceWriter;
import lagrange.output.MatrixWriter;
import lagrange.output.TrajectoryWriter;

public class NullPlug implements MatrixWriter, TrajectoryWriter,
		DistanceWriter, Cloneable,Mortality {
	
	@Override
	public void apply(Particle p){}
	@Override
	public void apply(Particle p,double cycles){}
	@Override
	public void close(){}
	public boolean isNearNoData(){return false;}
	public void checkReflect(Particle p){}
	public void flush(){}
	@Override
	public void setTimeInterval(long d){}
	@Override
	public void setTimeUnits(String s){}
	@Override
	public void setDurationUnits(String s){}
	@Override
	public NullPlug clone(){return this;}
	@Override
	public void setNegCoord(boolean b){}
}
