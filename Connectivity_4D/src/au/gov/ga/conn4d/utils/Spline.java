package au.gov.ga.conn4d.utils;

public class Spline {

	private double[] d2ydx2 = null; // double array of second derivatives
	private boolean derivCalculated = false; // logical indicating whether
												// derivative
												// values should be
												// recalculated
	private int[] newAndOldIndices; // Stores the indices mapping the order of
									// the independent values to sorted order
	private int nPoints = 0; // Stores the number of points used by the Spline
	private int nPointsOriginal = 0; // Stores the original number of points
										// used by the Spline
	private double[] x = null; // array of independent axis values
	private double[] y = null; // array of dependent axis values

	private double yy = Double.NaN; // class-level storage for the interpolated
									// value

	/**
	 * Constructor initializing the Spline using a defined number of points
	 * 
	 * @param nPoints
	 */

	public Spline(int nPoints) {
		// Store the number of points at the class level
		this.nPoints = nPoints;

		// Store the number of points as being the original number of points
		this.nPointsOriginal = this.nPoints;

		// Initialize the array that will contain the first axis (independent)
		// values to contain nPoints
		this.x = new double[nPoints];

		// Initialize the array that will contain the dependent values to
		// contain nPoints
		this.y = new double[nPoints];

		// Initialize the array that will contain the second derivative values
		// to contain nPoints
		this.d2ydx2 = new double[nPoints];
	}

	/**
	 * Calculates the second derivatives of the tabulated function for use by
	 * the cubic spline interpolation method. This method follows
	 * the procedure in Numerical Recipes Numerical Recipes 3rd ed. 
	 * by Press et al. 2007 p.123
	 */

	public void calcDeriv() {
		// initialize 4 double variables, p,qn,sig and un.
		double p = 0.0D, qn = 0.0D, sig = 0.0D, un = 0.0D;

		// create a double array of length nPoints
		double[] u = new double[nPoints];

		// set the first derivative element to be 0.
		d2ydx2[0] = u[0] = 0.0;

		// Loop: starting at the second element, and up to (including) the next
		// to last element - use the code contained in the Numerical Recipes
		// 3rd edition tridiagonal decomposition for loop on page 123.  The
		// code can be used verbatim.
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

		// Set qn and un to be 0 (likely redundant/unnecessary)
		qn = un = 0.0;

		// Use the code contained in the Numerical Recipes
		// 3rd edition - the last 3 lines of the Spline_interp method
		// in the middle of page 123.  y2 in our case is d2ydx2 and n is
		// nPoints.
		
		this.d2ydx2[this.nPoints - 1] = (un - qn * u[this.nPoints - 2])
				/ (qn * this.d2ydx2[this.nPoints - 2] + 1.0);
		for (int k = this.nPoints - 2; k >= 0; k--) {
			this.d2ydx2[k] = this.d2ydx2[k] * this.d2ydx2[k + 1] + u[k];
		}
	}

	/**
	 * Returns the internal array of second derivatives
	 * 
	 * @return a double array containing the second derivatives
	 */
	public double[] getDeriv() {
		// If the derivatives need to be re-calculated, perform the method
		if (!this.derivCalculated)
			this.calcDeriv();

		// return the array of second derivatives
		return this.d2ydx2;
	}

	/**
	 * Returns an interpolated value of y for a value of x from a tabulated
	 * function y=f(x) after the data has been entered via a constructor. The
	 * derivatives are calculated on the first call to this method and are then
	 * stored for use in subsequent calls
	 */

	public double interpolate(double xx) {

		// Initialize three doubles, h, b and a.
		double h = 0.0D, b = 0.0D, a = 0.0D;
		int k = 0; // Create an index position as k at 0
		int klo = 0; // Store the lower index position (start at 0)
		int khi = this.nPoints - 1; // Store the upper index position
									// (nPoints-1)

		// Loop: while the difference between the lower and upper index position
		// is greater than one
		while (khi - klo > 1) {
			// Add the high and low indices and use a signed right shift by one
			// to
			// get the integer floor of the mean value (e.g. (10+7)>>1 = 8).
			k = (khi + klo) >> 1;

			// if the value at k is greater than the search value, then
			// set the upper index to be k.

			if (this.x[k] > xx) {
				khi = k;

				// otherwise, set the lower index to be k.

			} else {
				klo = k;
			}
		}

		// Let h be the difference between the independent axis value at the
		// upper index and the independent axis value at the lower index
		h = this.x[khi] - this.x[klo];

		// Let a be the difference between the axis value at the upper index and
		// the search value, and divide by h
		// to get the proportional distance from the upper axis value.

		a = (this.x[khi] - xx) / h;

		// Let a be the difference between the axis value at the lower index and
		// the search value, and divide by h
		// to get the proportional distance from the lower axis value.

		b = (xx - this.x[klo]) / h;

		// Calculate the interpolated value using the following equation from
		// Numerical Recipes 3rd ed. by Press et al. 2007 p.124, and store at the
		// class level: 
		// y = a*y[klo]+b*y[khi]+((a*a*a-a)*this.d2ydx2[klo]+(b*b*b-b)*this.d2ydx2[khi])*(h*h)/6.0;

		this.yy = a
				* this.y[klo]
				+ b
				* this.y[khi]
				+ ((a * a * a - a) * this.d2ydx2[klo] + (b * b * b - b)
						* this.d2ydx2[khi]) * (h * h) / 6.0;

		// Return the interpolated value
		return this.yy;
	}

	/**
	 * Create a one dimensional array of n (empty) Spline objects containing m
	 * elements each.
	 */

	public static Spline[] oneDarray(int n, int m) {

		// Create a new array of length n to hold the Spline objects

		Spline[] a = new Spline[n];

		// Loop to fill the array with Spline objects containing m (empty)
		// elements each

		for (int i = 0; i < n; i++) {
			a[i] = Spline.zero(m);
		}

		// return the array
		return a;
	}

	/**
	 * Sort points into an ascending abscissa order
	 */

	public void orderPoints() {

		// Create a new dummy array of doubles having length nPoints
		double[] dummy = new double[nPoints];

		// Also set newAndOldIndices as a zero array of length nPoints
		this.newAndOldIndices = new int[nPoints];
		// Use VectorMath.selectionSort(double[], double[], double[]) to sort x
		// into ascending order storing index changes (pass in the dummy
		// array and the indices stored at the class level)
		VectorMath.selectionSort(this.x, dummy, this.newAndOldIndices);
		// Use VectorMath.selectionSort(double[],double[],double[],double[]) to
		// sort x into ascending order and make y match the new order storing
		// both new x and new y
		VectorMath.selectionSort(this.x, this.y, this.x, this.y);
	}

	/**
	 * Resets the x y data arrays
	 */

	public void resetData(double[] x, double[] y) {
		// Reset the number of points to be the original number of points
		this.nPoints = this.nPointsOriginal;

		// Loop over the passed arrays and store in the class-level variables
		for (int i = 0; i < this.nPoints; i++) {
			this.x[i] = x[i];
			this.y[i] = y[i];
		}

		// Order/sort the points using the orderPoints() method.
		this.orderPoints();
	}

	/**
	 * Create a Spline object containing n empty (zero-value) elements
	 * 
	 * @param n
	 *            - the number of zero-value elements in the Spline
	 * @return
	 */

	public static Spline zero(int n) {
		// Use the constructor to create a Spline containing n elements
		Spline aa = new Spline(n);
		// return the Spline
		return aa;
	}
}