package au.gov.ga.conn4d.test.impl.movement;

import au.gov.ga.conn4d.VelocityReader;

public class VelocityReader_TestPlug implements VelocityReader, Cloneable {

	private final double[] NODATA = { Double.NaN, Double.NaN, Double.NaN };

	@Override
	public double[] getNODATA() {
		return NODATA;
	}

	@Override
	public void close() {
	}

	@Override
	public double[][] getBounds() {
		return new double[][]{{0,1000},{-100000,100000},{-90,90},{0,360}};
	}

	@Override
	public int[][] getShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUnits() {
		return "milliseconds";
	}

	@Override
	public double[] getVelocities(long time, double z, double lon, double lat) {
		
		return new double[]{0,0,0-1-(z*z)};
	}

	@Override
	public boolean isNearNoData() {
		return false;
	}

	public VelocityReader_TestPlug clone() {
		return this;
	}
}
