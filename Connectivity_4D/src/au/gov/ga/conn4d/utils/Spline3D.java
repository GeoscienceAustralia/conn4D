package au.gov.ga.conn4d.utils;

public class Spline3D {

	private int nPoints = 0; // no. of x1 tabulated points
	private int mPoints = 0; // no. of x2 tabulated points
	private int lPoints = 0; // no. of x3 tabulated points
	private double[][][] y = null; // y=f(x1,x2) tabulated function
	private double[] x1 = null; // x1 in tabulated function f(x1,x2,x3)
	private double[] x2 = null; // x2 in tabulated function f(x1,x2,x3)
	private double[] x3 = null; // x3 in tabulated function f(x1,x2,x3)
	private double[] xMin = new double[3]; // minimum values of x1, x2 and x3
	private double[] xMax = new double[3]; // maximum values of x1, x2 and x3
	private Spline2D[] bcsn = null; // nPoints array of BiCubicSpline
											// instances
	private Spline csm = null; // CubicSpline instance
	private double[][][] d2ydx2inner = null; // inner matrix of second
												// derivatives

	/**
	 * Constructor
	 * 
	 * @param x1
	 *            - axis values for the first dimension.
	 * @param x2
	 *            - axis values for the second dimension.
	 * @param x3
	 *            - axis values for the third dimension.
	 * @param y
	 *            - 3-dimensional matrix of float data values.
	 */

	public Spline3D(double[] x1, double[] x2, double[] x3, float[][][] y) {
		this.nPoints = x1.length;
		this.mPoints = x2.length;
		this.lPoints = x3.length;

		this.csm = new Spline(this.nPoints);
		this.bcsn = Spline2D.oneDarray(this.nPoints, this.mPoints,
				this.lPoints);
		this.x1 = new double[this.nPoints];
		this.x2 = new double[this.mPoints];
		this.x3 = new double[this.lPoints];
		this.y = new double[this.nPoints][this.mPoints][this.lPoints];
		this.d2ydx2inner = new double[this.nPoints][this.mPoints][this.lPoints];
		for (int i = 0; i < this.nPoints; i++) {
			this.x1[i] = x1[i];
		}
		this.xMin[0] = VectorMath.minimum(this.x1);
		this.xMax[0] = VectorMath.maximum(this.x1);
		for (int j = 0; j < this.mPoints; j++) {
			this.x2[j] = x2[j];
		}
		this.xMin[1] = VectorMath.minimum(this.x2);
		this.xMax[1] = VectorMath.maximum(this.x2);
		for (int j = 0; j < this.lPoints; j++) {
			this.x3[j] = x3[j];
		}
		this.xMin[2] = VectorMath.minimum(this.x3);
		this.xMax[2] = VectorMath.maximum(this.x3);
		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < this.mPoints; j++) {
				for (int k = 0; k < this.lPoints; k++) {
					this.y[i][j][k] = y[i][j][k];
				}
			}
		}

		double[][] yTempml = new double[this.mPoints][this.lPoints];
		for (int i = 0; i < this.nPoints; i++) {

			for (int j = 0; j < this.mPoints; j++) {
				for (int k = 0; k < this.lPoints; k++) {
					yTempml[j][k] = y[i][j][k];
				}
			}
			this.bcsn[i].resetData(x2, x3, yTempml);
			this.d2ydx2inner[i] = this.bcsn[i].getDeriv();
		}
	}

	/**
	 * Resets the x1, x2, x3, y data arrays Primarily for use in
	 * QuadriCubicSpline
	 * 
	 * @param x1
	 *            - axis values for the first dimension.
	 * @param x2
	 *            - axis values for the second dimension.
	 * @param x3
	 *            - axis values for the third dimension.
	 * @param y
	 *            - 3-dimensional matrix of float data values.
	 */

	public void resetData(double[] x1, double[] x2, double[] x3, float[][][] y) {

		for (int i = 0; i < this.nPoints; i++) {
			this.x1[i] = x1[i];
		}
		this.xMin[0] = VectorMath.minimum(this.x1);
		this.xMax[0] = VectorMath.maximum(this.x1);

		for (int i = 0; i < this.mPoints; i++) {
			this.x2[i] = x2[i];
		}
		this.xMin[1] = VectorMath.minimum(this.x2);
		this.xMax[1] = VectorMath.maximum(this.x2);

		for (int i = 0; i < this.lPoints; i++) {
			this.x3[i] = x3[i];
		}
		this.xMin[2] = VectorMath.minimum(this.x3);
		this.xMax[2] = VectorMath.maximum(this.x3);

		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < this.mPoints; j++) {
				for (int k = 0; k < this.lPoints; k++) {
					this.y[i][j][k] = y[i][j][k];
				}
			}
		}

		this.csm = new Spline(this.nPoints);
		this.bcsn = Spline2D.oneDarray(this.nPoints, this.mPoints,
				this.lPoints);
		double[][] yTempml = new double[this.mPoints][this.lPoints];
		for (int i = 0; i < this.nPoints; i++) {

			for (int j = 0; j < this.mPoints; j++) {
				for (int k = 0; k < this.lPoints; k++) {
					yTempml[j][k] = y[i][j][k];
				}
			}
			this.bcsn[i].resetData(x2, x3, yTempml);
			this.d2ydx2inner[i] = this.bcsn[i].getDeriv();
		}
	}

	/**
	 * Returns an interpolated value of y for values of x1, x2 and x3 from a
	 * tabulated function y=f(x1,x2,x3)
	 * 
	 * @param xx1
	 * @param xx2
	 * @param xx3
	 */

	public double interpolate(double xx1, double xx2, double xx3) {

		double[] yTempm = new double[nPoints];

		for (int i = 0; i < nPoints; i++) {
			yTempm[i] = this.bcsn[i].interpolate(xx2, xx3);
		}

		this.csm.resetData(x1, yTempm);
		return this.csm.interpolate(xx1);
	}

	/**
	 * Sets the values from which the interpolations will be derived.
	 * 
	 * @param y
	 *            - a 3-dimensional matrix of float values.
	 */

	public void setValues(float[][][] y) {
		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < this.mPoints; j++) {
				for (int k = 0; k < this.lPoints; k++) {
					this.y[i][j][k] = y[i][j][k];
				}
			}
			bcsn[i].setValues(y[i]);
		}
	}

	/**
	 * Retrieves the dimension of the values being used for interpolation.
	 */

	public int[] getDim() {
		return new int[] { nPoints, mPoints, lPoints };
	}
}
