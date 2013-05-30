package au.gov.ga.conn4d.utils;

public class Spline2D {

	private int nPoints = 0; 
	private int mPoints = 0; 
	private int nPointsT = 0;
	private int mPointsT = 0; 
	private double[][] y = null; 
	private double[][] yT = null; 
	private double[] x1 = null; 
	private double[] x2 = null;
	private double[][] d2ydx2inner = null;											
	private double[][] d2ydx2innerT = null;
	private Spline csn[] = null; 
	private Spline csm = null;
	private Spline csnT[] = null; 										
	private Spline csmT = null; 
	private double interpolatedValue = Double.NaN; 												
	private double interpolatedValueTranspose = Double.NaN; 															
	private double interpolatedValueMean = Double.NaN;
	
	// Constructor with data arrays initialized to zero

	public Spline2D(int nP, int mP) {
		this.nPoints = nP;
		this.mPoints = mP;
		this.nPointsT = mP;
		this.mPointsT = nP;
		this.csm = new Spline(this.nPoints);
		this.csmT = new Spline(this.nPointsT);

		this.csn = Spline.oneDarray(this.nPoints, this.mPoints);
		this.csnT = Spline.oneDarray(this.nPointsT, this.mPointsT);

		this.x1 = new double[this.nPoints];
		this.x2 = new double[this.mPoints];
		this.y = new double[this.nPoints][this.mPoints];
		this.yT = new double[this.nPointsT][this.mPointsT];
		this.d2ydx2inner = new double[this.nPoints][this.mPoints];
		this.d2ydx2innerT = new double[this.nPointsT][this.mPointsT];

	}

	// Resets the x1, x2, y data arrays
	// Primarily for use in TiCubicSpline
	public void resetData(double[] x1, double[] x2, double[][] y) {

		for (int i = 0; i < this.nPoints; i++) {
			this.x1[i] = x1[i];
		}

		for (int i = 0; i < this.mPoints; i++) {
			this.x2[i] = x2[i];
		}

		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < this.mPoints; j++) {
				this.y[i][j] = y[i][j];
				this.yT[j][i] = y[i][j];
			}
		}

		this.csm = new Spline(this.nPoints);
		this.csn = Spline.oneDarray(this.nPoints, this.mPoints);
		double[] yTempn = new double[mPoints];

		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < mPoints; j++)
				yTempn[j] = y[i][j];
			this.csn[i].resetData(x2, yTempn);
			this.csn[i].calcDeriv();
			this.d2ydx2inner[i] = this.csn[i].getDeriv();
		}

		this.csmT = new Spline(this.nPointsT);
		this.csnT = Spline.oneDarray(this.nPointsT, this.mPointsT);
		double[] yTempnT = new double[mPointsT];

		for (int i = 0; i < this.nPointsT; i++) {
			for (int j = 0; j < mPointsT; j++)
				yTempnT[j] = yT[i][j];
			this.csnT[i].resetData(x1, yTempnT);
			this.csnT[i].calcDeriv();
			this.d2ydx2innerT[i] = this.csnT[i].getDeriv();
		}
	}

	// Returns a new BiCubicSpline setting internal array size to nP x mP and
	// all array values to zero with natural spline default
	// Primarily for use in this.oneDarray for TiCubicSpline
	public static Spline2D zero(int nP, int mP) {
		Spline2D aa = new Spline2D(nP, mP);
		return aa;
	}

	// Create a one dimensional array of BiCubicSpline objects of length nP each
	// of internal array size mP x lP
	// Primarily for use in TriCubicSpline
	public static Spline2D[] oneDarray(int nP, int mP, int lP) {
		Spline2D[] a = new Spline2D[nP];
		for (int i = 0; i < nP; i++) {
			a[i] = Spline2D.zero(mP, lP);
		}
		return a;
	}

	// Get inner matrix of derivatives
	// Primarily used by TriCubicSpline
	public double[][] getDeriv() {
		return this.d2ydx2inner;
	}

	/**
	 * Returns an interpolated value of y for a value of x
	 * from a tabulated function y=f(x1,x2)
	 * @param xx1
	 * @param xx2
	 */
	
	public double interpolate(double xx1, double xx2) {

		double[] yTempm = new double[this.nPoints];

		for (int i = 0; i < this.nPoints; i++) {
			yTempm[i] = this.csn[i].interpolate(xx2);
		}
		this.csm.resetData(x1, yTempm);
		this.interpolatedValue = this.csm.interpolate(xx1);

		double[] yTempmT = new double[this.nPointsT];

		for (int i = 0; i < this.nPointsT; i++) {
			yTempmT[i] = this.csnT[i].interpolate(xx1);
		}
		this.csmT.resetData(x2, yTempmT);
		this.interpolatedValueTranspose = this.csmT.interpolate(xx2);

		this.interpolatedValueMean = (this.interpolatedValue + this.interpolatedValueTranspose) / 2.0;
		return this.interpolatedValueMean;
	}

	/**
	 * Sets dependent values using a 2D float array
	 * 
	 * @param y
	 */
	
	public void setValues(float[][] y) {

		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < this.mPoints; j++) {
				this.y[i][j] = y[i][j];
			}
		}
		for (int i = 0; i < this.nPointsT; i++) {
			for (int j = 0; j < this.mPointsT; j++) {
				this.yT[i][j] = y[j][i];
			}
		}

		double[] yTempn = new double[mPoints];
		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < mPoints; j++)
				yTempn[j] = y[i][j];
			this.csn[i].resetData(x2, yTempn);
			this.csn[i].calcDeriv();
			this.d2ydx2inner[i] = this.csn[i].getDeriv();
		}

		double[] yTempnT = new double[mPointsT];
		for (int i = 0; i < this.nPointsT; i++) {
			for (int j = 0; j < mPointsT; j++)
				yTempnT[j] = yT[i][j];
			this.csnT[i].resetData(x1, yTempnT);
			this.csnT[i].calcDeriv();
			this.d2ydx2innerT[i] = this.csnT[i].getDeriv();
		}
	}
}