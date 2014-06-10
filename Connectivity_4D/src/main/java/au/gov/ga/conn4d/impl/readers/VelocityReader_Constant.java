package au.gov.ga.conn4d.impl.readers;

import au.gov.ga.conn4d.VelocityReader;

public class VelocityReader_Constant implements VelocityReader {
	private final double[] NODATA = { Double.NaN, Double.NaN, Double.NaN };
	private double[] velocities = new double[]{0,0,0};

	@Override
	public double[] getNODATA() {
		return NODATA;
	}

	@Override
	public void close() {
	}

	@Override
	public double[][] getBounds() {
		return new double[][]{{Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY},{Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY},{Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY},{Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY}};
	}

	@Override
	public int[][] getShape() {
		return null;
	}

	@Override
	public String getUnits() {
		return "milliseconds";
	}

	/**
	 * Returns -1-z^2 for the w velocity value for testing [analytic solution is -tan(z)].  
	 * Velocities for u and v are 0 since x and y values are autoconverted to latitude and 
	 * longitude, but the calculations are otherwise identical.
	 */
	@Override
	public double[] getVelocities(long time, double z, double lon, double lat) {
		
		return velocities;
	}

	@Override
	public boolean isNearNoData() {
		return false;
	}

	public VelocityReader_Constant clone() {
		return this;
	}
	
	public void setVelocities(double [] velocities){
		this.velocities=velocities;
	}
}
