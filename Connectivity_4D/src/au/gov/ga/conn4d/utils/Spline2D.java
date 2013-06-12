package au.gov.ga.conn4d.utils;

public class Spline2D {

	private int nPoints = 0; // number of points on first axis (=x1.length)
	private int mPoints = 0; // number of points on second axis (=x2.length)
	private int nPointsT = 0; // number of points on first transposed axis
								// (=x2.length)
	private int mPointsT = 0; // number of points on second transposed axis
								// (=x1.length)
	private double[][] y = null; // 2D array of dependent values
	private double[][] yT = null;// 2D array of dependent values transposed
	private double[] x1 = null; // stored first axis values
	private double[] x2 = null; // stored second axis values
	private double[][] d2ydx2inner = null; // 2D Array of second derivative
											// values
	private double[][] d2ydx2innerT = null; // // Array of second derivative
											// values on the transpose
	private Spline[] csn = null; // 1D array of Spline objects along first axis
	private Spline csm = null; // Spline object along first axis
	private Spline[] csnT = null; // 1D array of Spline objects along second
									// axis (transpose)
	private Spline csmT = null; // Spline object along the second axis
								// (transpose)
	private double interpolatedValue = Double.NaN; // Stored interpolated value
	private double interpolatedValueTranspose = Double.NaN; // Stored
															// interpolated
															// value on
															// transposed data
	private double interpolatedValueMean = Double.NaN; // Stored mean
														// interpolated value
														// (average of original
														// and transpose)

	/**
	 * Constructor with initial data arrays
	 */

	public Spline2D(double[] x1, double[] x2, double[][] vals) {
		this.nPoints = x1.length; // set nPoints to equal the length of x1
		this.mPoints = x2.length; // set mPoints to equal the length of x2
		this.nPointsT = x1.length; // set nPointsT to equal the length of x1 (?)
		this.mPointsT = x2.length; // set mPoints to equal the length of x2 (?)
		this.csm = new Spline(this.nPoints); // set the first axis Spline object
												// as a new Spline containing
												// nPoints elements
		this.csmT = new Spline(this.nPointsT); // set the second axis Spline
												// object as a new Spline
												// containing nPointsT elements
		this.resetData(x1, x2, vals); // Reset the data for this class using
										// x1,x2 and vals.
	}

	/**
	 * Constructor with data arrays initialized to zero
	 * 
	 * @param nP
	 * @param mP
	 */

	public Spline2D(int nP, int mP) {
		// Store the lengths of the axes
		this.nPoints = nP; // set nPoints as nP
		this.mPoints = mP; // set mPoints as mP
		this.nPointsT = mP; // set nPointsT (transpose) as mP
		this.mPointsT = nP; // set mPointsT (transpose) as nP

		// Initialize an empty Spline for the primary axis (nPoints elements)
		this.csm = new Spline(this.nPoints);

		// Initialize an empty Spline for the transposed axis (nPointsT
		// elements)
		this.csmT = new Spline(this.nPointsT);

		// Initialize a 1-D array of empty Splines for the primary axis,
		// an nPoints array of mPoints.
		this.csn = Spline.oneDarray(this.nPoints, this.mPoints);

		// Initialize a 1-D array of empty Splines for the transpose axis,
		// an nPointsT array of mPointsT.
		this.csnT = Spline.oneDarray(this.nPointsT, this.mPointsT);

		// initializes a new double array of length nPoints to hold first axis
		// values
		this.x1 = new double[this.nPoints];

		// initializes a new double array of length nPoints to hold second axis
		// values
		this.x2 = new double[this.mPoints];

		// initialize a 2D double array (nPoints by mPoints) to store the grid
		// values.
		this.y = new double[this.nPoints][this.mPoints];

		// initialize a 2D double array (nPointsT by mPointsT) to store the
		// transposed grid values.
		this.yT = new double[this.nPointsT][this.mPointsT];

		// initialize a 2D double array (nPoints by mPoints) to store the second
		// derivative values.
		this.d2ydx2inner = new double[this.nPoints][this.mPoints];

		// initialize a 2D double array (nPoints by mPoints) to store the second
		// derivative values for the transposed system.
		this.d2ydx2innerT = new double[this.nPointsT][this.mPointsT];
	}

	/**
	 * Resets the data arrays
	 * 
	 * @param x1
	 * @param x2
	 * @param vals
	 */

	public void resetData(double[] x1, double[] x2, double[][] vals) {

		// Loop through the passed values for the first axis and
		// store the values in the class-level first axis variable.

		for (int i = 0; i < this.nPoints; i++) {
			this.x1[i] = x1[i];
		}

		// Loop through the passed values for the second axis and
		// store the values in the class-level second axis variable.

		for (int i = 0; i < this.mPoints; i++) {
			this.x2[i] = x2[i];
		}

		// 2D Loop: Loop through the passed values and
		// store their values [i][j]->[i][j] in the class-level variable, as well
		// as writing their transpose at the class level [i][j]->[j][i]

		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < this.mPoints; j++) {
				this.y[i][j] = vals[i][j];
				this.yT[j][i] = vals[i][j];
			}
		}

		// Create a new Spline object containing nPoints elements
		this.csm = new Spline(this.nPoints);

		// Create a 1-D array of Spline objects (nPoints objects of length
		// mPoints).
		this.csn = Spline.oneDarray(this.nPoints, this.mPoints);

		// Generate a temporary array of length mPoints
		double[] yTempn = new double[mPoints];

		// 2D loop: set the values of the temporary array by slicing
		// across the first axis.
		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < mPoints; j++)

				// Set the value of the temporary array at the given index pair
				yTempn[j] = vals[i][j];

			// In the process of looping along the Spline array, reset the data
			// for each Spline object using the second axis, and temporary
			// (sliced) values.
			this.csn[i].resetData(x2, yTempn);

			// In the process of looping along the Spline array, calculate the
			// second derivative values.
			this.csn[i].calcDeriv();

			// In the process of looping along the Spline array, retrieve the
			// second derivative values, and store them at the class level
			this.d2ydx2inner[i] = this.csn[i].getDeriv();
		}

		// Generate a new Spline for the transpose containing nPointsT elements,
		// and store at the class level

		this.csmT = new Spline(this.nPointsT);

		// Create a 1-D array of Spline objects (nPointsT objects of length
		// mPointsT).
		this.csnT = Spline.oneDarray(this.nPointsT, this.mPointsT);

		// Generate a temporary array of length mPointsT
		double[] yTempnT = new double[mPointsT];

		// 2D loop: set the values of the temporary transposed array by slicing
		// across the first axis.
		for (int i = 0; i < this.nPointsT; i++) {
			for (int j = 0; j < mPointsT; j++)
				// Set the value of the temporary array at the given index pair
				yTempnT[j] = yT[i][j];

			// In the process of looping along the Spline array, reset the data
			// for each Spline object using the second axis, and temporary
			// transposed (sliced) values.
			this.csnT[i].resetData(x1, yTempnT);

			// In the process of looping along the Spline array, calculate the
			// second derivative values for the transposed values
			this.csnT[i].calcDeriv();

			// In the process of looping along the Spline array, retrieve the
			// second derivative values for the transposed values, and store
			// them at the class level
			this.d2ydx2innerT[i] = this.csnT[i].getDeriv();
		}
	}

	/**
	 * Returns a new Spline2D setting internal array size to nP x mP and
	 * 
	 * @param nP
	 * @param mP
	 */

	public static Spline2D zero(int nP, int mP) {
		// Create a new Spline2D object of dimension nP x mP using the
		// constructor
		Spline2D aa = new Spline2D(nP, mP);

		// Return the Spline2D object
		return aa;
	}

	/**
	 * Create a one dimensional array of Spline2D objects of length nP each
	 * 
	 * @param nP
	 *            - the size of the 1D array
	 * @param mP
	 *            - the size of the first dimension of the Spline2D objects
	 * @param lP
	 *            - the size of the second dimension of the Spline2D objects
	 * @return - an array of nP Spline2D objects of dimension mP x lP.
	 */

	public static Spline2D[] oneDarray(int nP, int mP, int lP) {

		// Initialize an (empty) array of size nP of Spline2D objects.
		Spline2D[] a = new Spline2D[nP];

		// Loop to fill the array with Spline2D objects containing zero values.
		for (int i = 0; i < nP; i++) {
			a[i] = Spline2D.zero(mP, lP);
		}

		// Return the Spline2D object
		return a;
	}

	/**
	 * (GET) Retrieves the inner matrix of derivatives for this object
	 */

	public double[][] getDeriv() {
		return this.d2ydx2inner;
	}

	/**
	 * Returns an interpolated value of y for a value of x from a tabulated
	 * function y=f(x1,x2)
	 * 
	 * @param xx1
	 * @param xx2
	 */

	public double interpolate(double xx1, double xx2) {

		// Create a temporary array to hold the 1D interpolated values for the
		// first axis

		double[] yTempm = new double[this.nPoints];

		// For all points along the first axis, generate interpolated values for
		// the second positional value

		for (int i = 0; i < this.nPoints; i++) {
			yTempm[i] = this.csn[i].interpolate(xx2);
		}

		// Save the interpolated data values for the first axis to a class-level
		// variable
		// and reset the associated parameters.

		this.csm.resetData(x1, yTempm);

		// Store the interpolated value for the first axis to a class-level
		// variable

		this.interpolatedValue = this.csm.interpolate(xx1);

		// Create a temporary array to hold the 1D interpolated values for the
		// second (transpose) axis

		double[] yTempmT = new double[this.nPointsT];

		// Generate the interpolated values for the second (transpose) axis

		for (int i = 0; i < this.nPointsT; i++) {
			yTempmT[i] = this.csnT[i].interpolate(xx1);
		}

		// Store the interpolated value for the first axis to a class-level
		// variable
		// and reset the associated parameters.

		this.csmT.resetData(x2, yTempmT);

		// Store the interpolated value for the first axis to a class-level
		// variable

		this.interpolatedValueTranspose = this.csmT.interpolate(xx2);

		// Take the mean of the interpolated value along both axes.

		this.interpolatedValueMean = (this.interpolatedValue + this.interpolatedValueTranspose) / 2.0;

		// Return the mean interpolated value.

		return this.interpolatedValueMean;
	}

	/**
	 * Sets dependent values using a 2D float array
	 * 
	 * @param y
	 */

	public void setValues(float[][] y) {

		// 2D Loop: overwrite the values stored at the class
		// level

		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < this.mPoints; j++) {
				this.y[i][j] = y[i][j];
			}
		}

		// 2D Loop: Overwrite the transposed values stored at the
		// class level

		for (int i = 0; i < this.nPointsT; i++) {
			for (int j = 0; j < this.mPointsT; j++) {
				this.yT[i][j] = y[j][i];
			}
		}

		// Generate a temporary array of length mPoints
		double[] yTempn = new double[mPoints];

		// 2D loop: set the values of the temporary array by slicing
		// across the first axis.
		for (int i = 0; i < this.nPoints; i++) {
			for (int j = 0; j < mPoints; j++)

				// Set the value of the temporary array at the given index pair
				yTempn[j] = y[i][j];

			// In the process of looping along the Spline array, reset the data
			// for each Spline object using the second axis, and temporary
			// (sliced) values.
			this.csn[i].resetData(x2, yTempn);

			// In the process of looping along the Spline array, calculate the
			// second derivative values.
			this.csn[i].calcDeriv();

			// In the process of looping along the Spline array, retrieve the
			// second derivative values, and store them at the class level
			this.d2ydx2inner[i] = this.csn[i].getDeriv();
		}

		// Generate a new Spline for the transpose containing nPointsT elements,
		// and store at the class level

		this.csmT = new Spline(this.nPointsT);

		// Create a 1-D array of Spline objects (nPointsT objects of length
		// mPointsT).
		this.csnT = Spline.oneDarray(this.nPointsT, this.mPointsT);

		// Generate a temporary array of length mPointsT
		double[] yTempnT = new double[mPointsT];

		// 2D loop: set the values of the temporary transposed array by slicing
		// across the first axis.
		for (int i = 0; i < this.nPointsT; i++) {
			for (int j = 0; j < mPointsT; j++)
				// Set the value of the temporary array at the given index pair
				yTempnT[j] = yT[i][j];

			// In the process of looping along the Spline array, reset the data
			// for each Spline object using the second axis, and temporary
			// transposed (sliced) values.
			this.csnT[i].resetData(x1, yTempnT);

			// In the process of looping along the Spline array, calculate the
			// second derivative values for the transposed values
			this.csnT[i].calcDeriv();

			// In the process of looping along the Spline array, retrieve the
			// second derivative values for the transposed values, and store
			// them at the class level
			this.d2ydx2innerT[i] = this.csnT[i].getDeriv();
		}
	}
}