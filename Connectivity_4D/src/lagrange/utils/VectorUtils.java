package lagrange.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import lagrange.test.utils.Filter;
import cern.jet.random.Normal;

public class VectorUtils {

	/**
	 * Creates a copy of a 2-dimensional matrix of doubles
	 * 
	 * @param mtx
	 * @return
	 */

	public static double[][] copy(double[][] mtx) {
		double[][] out = new double[mtx.length][mtx[0].length];
		for (int i = 0; i < mtx.length; i++) {
			System.arraycopy(mtx[i], 0, out[i], 0, mtx[i].length);
		}
		return out;
	}

	/**
	 * Retrieves a column vector of ints from an integer matrix.
	 * 
	 * @param mtx
	 *            int[][] - The integer matrix from which the value is to be
	 *            retrieved.
	 * @param col
	 *            int - The identifier of the column to be retrieved.
	 * @return int[] - The column vector.
	 */

	public static double[] getColumn(double[][] mtx, int col) {

		double[] out = new double[mtx.length];

		for (int i = 0; i < mtx.length; i++) {

			out[i] = mtx[i][col];

		}

		return out;
	}

	/**
	 * Retrieves a column vector of ints from an integer matrix.
	 * 
	 * @param mtx
	 *            int[][] - The integer matrix from which the value is to be
	 *            retrieved.
	 * @param col
	 *            int - The identifier of the column to be retrieved.
	 * @return int[] - The column vector.
	 */

	public static float[] getColumn(float[][] mtx, int col) {

		float[] out = new float[mtx.length];

		for (int i = 0; i < mtx.length; i++) {

			out[i] = mtx[i][col];

		}

		return out;
	}

	/**
	 * Retrieves a column vector of ints from an integer matrix.
	 * 
	 * @param mtx
	 *            int[][] - The integer matrix from which the value is to be
	 *            retrieved.
	 * @param col
	 *            int - The identifier of the column to be retrieved.
	 * @return int[] - The column vector.
	 */
	
	public static int[] getColumn(int[][] mtx, int col) {

		int[] out = new int[mtx.length];

		for (int i = 0; i < mtx.length; i++) {

			out[i] = mtx[i][col];

		}

		return out;
	}

	/**
	 * Retrieves a column vector of ints from an integer matrix.
	 * 
	 * @param mtx
	 *            int[][] - The integer matrix from which the value is to be
	 *            retrieved.
	 * @param col
	 *            int - The identifier of the column to be retrieved.
	 * @return int[] - The column vector.
	 */

	public static long[] getColumn(long[][] mtx, int col) {

		long[] out = new long[mtx.length];

		for (int i = 0; i < mtx.length; i++) {

			out[i] = mtx[i][col];

		}

		return out;
	}

	/**
	 * Retrieves a column vector of ints from an integer matrix.
	 * 
	 * @param mtx
	 *            int[][] - The integer matrix from which the value is to be
	 *            retrieved.
	 * @param col
	 *            int - The identifier of the column to be retrieved.
	 * @return int[] - The column vector.
	 */

	public static Number[] getColumn(Number[][] mtx, int col) {

		Number[] out = new Number[mtx.length];

		for (int i = 0; i < mtx.length; i++) {

			out[i] = mtx[i][col];

		}

		return out;
	}

	/**
	 * Loads a transition matrix into the class based on a stored text file.
	 * 
	 * @param file
	 *            File: The file containing the ASCII representation of the
	 *            transition matrix.
	 * 
	 * @return double[][]:The ASCII matrix converted to double[][] form.
	 */

	public static double[][] loadASCIIMatrix(File file) {

		double[][] matrix = null;

		FileReader fr = null;
		;
		BufferedReader b = null;

		try {
			fr = new FileReader(file);
			b = new BufferedReader(fr);

			String rl = b.readLine();

			if (rl == null) {

				throw new java.lang.NullPointerException(
						"Supplied matrix is empty");
			}

			StringTokenizer stk = new StringTokenizer(rl);
			int numTokens = stk.countTokens();
			matrix = new double[numTokens][numTokens];

			int i = 0;

			while (rl != null) {

				int j = 0;

				while (stk.hasMoreTokens()) {

					matrix[i][j] = Double.parseDouble(stk.nextToken());
					j++;

				}

				rl = b.readLine();
				if (rl != null) {
					stk = new StringTokenizer(rl);
				}
				i++;

			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			System.out.println();
			System.out.println("Incorrect Number Format.  Exiting.");
			System.exit(-1);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			System.out.println();
			System.out.println("File Not Found.  Exiting.");
			System.exit(-1);

		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println();
			System.out
					.println("Exception occurred when reading the file.  Exiting.");
			System.exit(-1);

		} finally {
			if (b != null) {
				try {
					b.close();
				} catch (IOException e) {
				}
			}
		}
		return matrix;
	}
	
	/**
	 * Returns Moran's I for the value/weight inputs.
	 * 
	 * @param vals
	 * @param weights
	 * @return
	 */
	
	public static double moransI(double[] vals, double[][] weights){
		int n = vals.length;
		if(vals == null || n <= 1){
			return Double.NaN;
		}
		
		int W = 0;
		
		double sum = 0;
		for(int i = 0; i < n; i++){
			sum += vals[i];
		}
		
		double mean = sum/n;
		double[] z = new double[vals.length];
		
		for(int i = 0; i < n; i++){
			z[i] = vals[i]-mean;
		}
		
		double num = 0;
		double denom = 0;
		
		for (int i = 0; i < n; i++){
			denom += z[i]*z[i];
			for(int j = 0; j < n; j++){
				if(weights[i][j]!=0){
					W+=weights[i][j];
					num += weights[i][j]*z[i]*z[j];
				}
			}
		}
		
		double scale = (double) n/(double) W;
		return scale * (num/denom);	
	}
	
	/**
	 * Returns the percentile position of the autocorrelation value within the standard distribution.
	 * This is NOT the p-value.
	 * 
	 * @param vals
	 * @param weights
	 * @param alpha
	 * @return
	 */
	
	public static double moransI_percentile(double[] vals, double[][] weights){
		int n = vals.length;
		if(vals == null || n <= 1){
			throw new IllegalArgumentException("Array is empty or contains only one value");
		}
		
		double sum = 0;
		for(int i = 0; i < n; i++){
			sum += vals[i];
		}
		
		double mean = sum/n;
		double[] z = new double[vals.length];
		double Dnum = 0;
		double Ddenom = 0;
		
		for(int i = 0; i < n; i++){
			z[i] = vals[i]-mean;
			Dnum+=Math.pow(z[i], 4);
			Ddenom+=z[i]*z[i];
		}
		Ddenom/=n;
		double k = (Dnum/n)/(Ddenom*Ddenom);
		
		double num = 0;
		double denom = 0;
		double S0 = 0;
		double S1 = 0;
		double S2 = 0;
		
		for (int i = 0; i < n; i++){
			denom += z[i]*z[i];
			double wjs1 = 0;
			double wjs2 = 0;
			for(int j = 0; j < n; j++){
				if(weights[i][j]!=0){
					S0+=weights[i][j];
					num += weights[i][j]*z[i]*z[j];
					S1+=(weights[i][j]+weights[j][i])*(weights[i][j]+weights[j][i]);
					wjs1+=weights[i][j];
					wjs2+=weights[j][i];
				}
			}
			S2+=(wjs1+wjs2)*(wjs1+wjs2);
		}
		
		S1/=2;
		double scale = n/S0;
		double I = scale * (num/denom);
		
		double EI = -1/(n-1);
		
		double n2 = n*n;
		double S02 = S0*S0; 
		double A = n*((n2-3*n+3)*S1-n*S2+3*S02);
		double B = k*((n2-n)*S1-2*n*S2+6*S02);
		double C = (n-1)*(n-2)*(n-3)*S02;
		
		double EI2 = (A-B)/C;
		double VI = EI2-(EI*EI);
		double zi = (I-EI)/Math.sqrt(VI);
		
		Normal nrm = new Normal(0d,1d, null);
		return nrm.cdf(zi);
	}
	
	/**
	 * Tests whether autocorrelation is significant for the value/weight combination.
	 * 
	 * @param vals
	 * @param weights
	 * @param alpha
	 * @return
	 */
	
	public static boolean moransI_test(double[] vals, double[][] weights, double alpha){
		double p = moransI_percentile(vals,weights);
		double a1 = (1-alpha)/2;
		double a2 = 1-a1;
		if(p<a1 || p>a2){
			return true;
		}
		return false;
	}
	
	/**
	 * Performs a logical selection on a matrix of double values
	 * 
	 * @param vals - The matrix of values to be selected from
	 * @param filter - Filter object determining which values meet given criteria
	 * @return - a matrix of doubles where 0 indicates false and 1 indicates true.
	 */
	
	public static double[][] select(double[][] vals, Filter filter){
		double[][] out = new double[vals.length][vals[0].length];
		for(int i = 0; i < vals.length; i++){
			for(int j = 0; j < vals[0].length; j++){
				if(filter.accept(vals[i][j])){
					out[i][j] = 1;
				}
			}
		}
		return out;
	}
	
	private static double findScale(double[] values, double[][] coordinates, double lagsize){
		return 0;//TODO NOT YET IMPLEMENTED
	}
	
	private static double findScale(double[] values, double[] coordinates, double lagsize){
		 return 0;//TODO NOT YET IMPLEMENTED
	}
	
	/**
	 * Generates a distance matrix from a vector of values
	 * 
	 * @param values
	 * @return
	 */
	
	public static double[][] pdist(double[] values){
		int n = values.length;
		double[][] d = new double[n][n];
		for(int j = 0; j < n; j++){
			for(int i = j+1; i < n; i++){
				d[i][j] = Math.abs(values[i]-values[j]);
				d[j][i] = d[i][j];
			}
		}
		return d;
	}
	
	/**
	 * Generates a Euclidean distance matrix from a list of coordinates
	 * 
	 * @param coordinates
	 * @return
	 */
	
	public static double[][] eucdist(double[][] coordinates){
		int n = coordinates.length;
		double[][] d = new double[n][n];
		for(int j = 0; j < n; j++){
			for(int i = j+1; i < n; i++){
				d[i][j] = euclidean(coordinates[i],coordinates[j]);
				d[j][i] = d[i][j];
			}
		}
		return d;
	}
	
	/**
	 * Calculates Euclidian distance for a pair of coordinates
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	
	public static double euclidean(double[] a, double[] b){
		if (a.length!=b.length){
			throw new IllegalArgumentException("Arrays must have the same length");
		}
		int dim = a.length;
		double d = 0;
		for(int i = 0; i < dim; i++){
			d+=(a[i]-b[i])*(a[i]-b[i]);
		}
		return Math.sqrt(d);
	}
}
