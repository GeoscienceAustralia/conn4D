package lagrange.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class VectorUtils {

	/**
	 * Creates a copy of a 2-dimensional matrix of doubles
	 * 
	 * @param mtx
	 * @return
	 */
	
	public static double[][] copy(double[][] mtx){
		double[][] out = new double[mtx.length][mtx[0].length];
		for(int i = 0; i < mtx.length; i++){
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

		FileReader fr = null;;
		BufferedReader b=null;
		
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
			if(b!=null){
				try {
					b.close();
				} catch (IOException e) {}
			}
		}
		return matrix;
	}
}
