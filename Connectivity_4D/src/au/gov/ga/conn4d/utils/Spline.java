package au.gov.ga.conn4d.utils;

public class Spline {

	// Create a one dimensional array of cubic spline objects of length n each
	// of array length m

	public static Spline[] oneDarray(int n, int m) {
		Spline[] a = new Spline[n];
		for (int i = 0; i < n; i++) {
			a[i] = Spline.zero(m);
		}
		return a;
	}

	// Returns a new CubicSpline setting array lengths to n and all array values
	// to zero with natural spline default

	public static Spline zero(int n) {
		Spline aa = new Spline(n);
		return aa;
	}

	private double[] x = null;
	private double[] y = null;
	private double[] d2ydx2 = null;
	private int[] newAndOldIndices;
	private int nPoints = 0;
	private int nPointsOriginal = 0;
	private double yp1 = Double.NaN;
	private double ypn = Double.NaN;
	private double yy = Double.NaN;
	private boolean derivCalculated = false;

	public Spline(int nPoints) {
		this.nPoints = nPoints;
		this.nPointsOriginal = this.nPoints;
		this.x = new double[nPoints];
		this.y = new double[nPoints];
		this.d2ydx2 = new double[nPoints];
	}

	/**
	 * Calculates the second derivatives of the tabulated function for use by
	 * the cubic spline interpolation method (.interpolate). This method follows
	 * the procedure in Numerical Methods C language procedure for calculating
	 * second derivatives
	 */

	public void calcDeriv() {
		double p = 0.0D, qn = 0.0D, sig = 0.0D, un = 0.0D;
		double[] u = new double[nPoints];

		if (Double.isNaN(this.yp1)) {
			d2ydx2[0] = u[0] = 0.0;
		} else {
			this.d2ydx2[0] = -0.5;
			u[0] = (3.0 / (this.x[1] - this.x[0]))
					* ((this.y[1] - this.y[0]) / (this.x[1] - this.x[0]) - this.yp1);
		}

		for (int i = 1; i <= this.nPoints - 2; i++) {
			sig = (this.x[i] - this.x[i - 1]) / (this.x[i + 1] - this.x[i - 1]);
			p = sig * this.d2ydx2[i - 1] + 2.0;
			this.d2ydx2[i] = (sig - 1.0) / p;
			u[i] = (this.y[i + 1] - this.y[i]) / (this.x[i + 1] - this.x[i])
					- (this.y[i] - this.y[i - 1]) / (this.x[i] - this.x[i - 1]);
			u[i] = (6.0 * u[i] / (this.x[i + 1] - this.x[i - 1]) - sig
					* u[i - 1])
					/ p;
		}

		if (Double.isNaN(this.ypn)) {
			qn = un = 0.0;
		} else {
			qn = 0.5;
			un = (3.0 / (this.x[nPoints - 1] - this.x[this.nPoints - 2]))
					* (this.ypn - (this.y[this.nPoints - 1] - this.y[this.nPoints - 2])
							/ (this.x[this.nPoints - 1] - x[this.nPoints - 2]));
		}

		this.d2ydx2[this.nPoints - 1] = (un - qn * u[this.nPoints - 2])
				/ (qn * this.d2ydx2[this.nPoints - 2] + 1.0);
		for (int k = this.nPoints - 2; k >= 0; k--) {
			this.d2ydx2[k] = this.d2ydx2[k] * this.d2ydx2[k + 1] + u[k];
		}
	}

	// Returns the internal array of second derivatives
	public double[] getDeriv() {
		if (!this.derivCalculated)
			this.calcDeriv();
		return this.d2ydx2;
	}

	// INTERPOLATE
	// Returns an interpolated value of y for a value of x from a tabulated
	// function y=f(x)
	// after the data has been entered via a constructor.
	// The derivatives are calculated on the first call to this
	// method ands are
	// then stored for use on all subsequent calls

	public double interpolate(double xx) {

		double h = 0.0D, b = 0.0D, a = 0.0D;
		int k = 0;
		int klo = 0;
		int khi = this.nPoints - 1;
		while (khi - klo > 1) {
			k = (khi + klo) >> 1;
			if (this.x[k] > xx) {
				khi = k;
			} else {
				klo = k;
			}
		}
		h = this.x[khi] - this.x[klo];

		a = (this.x[khi] - xx) / h;
		b = (xx - this.x[klo]) / h;
		this.yy = a
				* this.y[klo]
				+ b
				* this.y[khi]
				+ ((a * a * a - a) * this.d2ydx2[klo] + (b * b * b - b)
						* this.d2ydx2[khi]) * (h * h) / 6.0;
		return this.yy;
	}

	/**
	 * Sort points into an ascending abscissa order
	 */

	public void orderPoints() {
		double[] dummy = new double[nPoints];
		this.newAndOldIndices = new int[nPoints];
		// Sort x into ascending order storing indices changes
		VectorMath.selectionSort(this.x, dummy, this.newAndOldIndices);
		// Sort x into ascending order and make y match the new order storing
		// both new x and new y
		VectorMath.selectionSort(this.x, this.y, this.x, this.y);
	}

	/**
	 * Resets the x y data arrays
	 */
	
	public void resetData(double[] x, double[] y) {
		this.nPoints = this.nPointsOriginal;

		for (int i = 0; i < this.nPoints; i++) {
			this.x[i] = x[i];
			this.y[i] = y[i];
		}
		this.orderPoints();
	}
}
