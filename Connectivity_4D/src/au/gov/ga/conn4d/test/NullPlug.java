package au.gov.ga.conn4d.test;

import au.gov.ga.conn4d.Mortality;
import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.output.DistanceWriter;
import au.gov.ga.conn4d.output.MatrixWriter;
import au.gov.ga.conn4d.output.TrajectoryWriter;

public class NullPlug implements MatrixWriter, TrajectoryWriter,
		DistanceWriter, Cloneable,Mortality {
	
	@Override
	public void apply(Particle p){}
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
