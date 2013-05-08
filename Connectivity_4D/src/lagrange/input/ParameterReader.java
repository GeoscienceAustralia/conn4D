package lagrange.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * This is a class for reading variables into the system at runtime from a text
 * file. The class defines how information is parsed from text into the
 * appropriate class type.
 * 
 * @author Johnathan Kool
 * 
 */

public class ParameterReader {

	private static BufferedReader br;
	private static String parse, tk;


	/**
	 * Sets and reads a Global parameter file.
	 * 
	 * @param filename -
	 *            The name of the file to be read.
	 */

	public void readFile(String filename) {

		// First read in the file and set up the StringTokenizer.

		try {
			FileReader fr = new FileReader(new File(filename));
			br = new BufferedReader(fr);

			parse = br.readLine();

			// As long as there are more lines...

			while (parse != null) {

				try {
					read(parse);
				} catch (SecurityException ex) {
					System.out.println("WARNING: Security violation.  Field "
							+ tk + "cannot be altered.");
					parse = br.readLine();
					continue;
				} catch (NoSuchFieldException ex) {
					System.out.println("WARNING: Field " + tk
							+ " was provided in the text file, but is not a Parameter class variable.  Continuing...");
					parse = br.readLine();
					continue;
				} catch (IllegalAccessException ex) {
					System.out.println("WARNING: Field + " + tk
							+ "could not be modified (Possibly inappropriate "
							+ "input form).  Continuing...");
					parse = br.readLine();
					continue;
				}

			}
		}

		catch (FileNotFoundException ex) {
			System.out.println("Parameter file " + filename
					+ " cannot be found.  Exiting program.");
			ex.printStackTrace();
			System.exit(-1);

		} catch (IOException ex) {
			System.out.println("Error reading line from input file.  Exiting.");
			ex.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Parses a text string to set variables dynamically at run time.
	 * 
	 * @param _parse
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	
	private void read(String _parse) throws SecurityException,
			NoSuchFieldException, IllegalAccessException, IOException {

		StringTokenizer stk = new StringTokenizer(_parse);
		tk = stk.nextToken();

		// Try to retrieve a Field by the name provided as a token. If it is
		// unavailable then return back to the while.

		Field f = this.getClass().getField(tk);
		Type t = f.getType();

		// If the identified field is of type int:

		if (t.toString().equalsIgnoreCase("int")) {

			Integer i = Integer.parseInt(stk.nextToken());
			f.setInt(this, i);
			parse = br.readLine();

		}

		// If the identified field is of type long:

		else if (t.toString().equalsIgnoreCase("long")) {

			Long l = Long.parseLong(stk.nextToken());
			f.setLong(this, l);
			parse = br.readLine();

		}

		// If the identified field is of type double:

		else if (t.toString().equalsIgnoreCase("double")) {

			f.setDouble(this, Double.parseDouble(stk.nextToken()));
			parse = br.readLine();

		}

		// If the identified field is of type boolean:

		else if (t.toString().equalsIgnoreCase("boolean")) {

			String ntk = stk.nextToken();
			if (!ntk.equalsIgnoreCase("true") && !ntk.equalsIgnoreCase("false")) {
				System.out
						.println("Invalid parameter value provided for variable "+f.getName()+", setting to false");
			}

			f.setBoolean(this, Boolean.valueOf(ntk));
			parse = br.readLine();

		}

		// If the identified field is of type int[]:

		else if (t.toString().equalsIgnoreCase("class [I")) {

			int[] ia = new int[stk.countTokens()];
			int i = 0;

			while (stk.hasMoreTokens()) {

				ia[i] = Integer.parseInt(stk.nextToken());
				i++;

			}

			f.set(this, ia);
			parse = br.readLine();

		}
		
		// If the identified field is of type double[]:
		
		else if (t.toString().equalsIgnoreCase("class [D")) {

			double[] da = new double[stk.countTokens()];
			int i = 0;

			while (stk.hasMoreTokens()) {

				da[i] = Integer.parseInt(stk.nextToken());
				i++;

			}

			f.set(this, da);
			parse = br.readLine();

		}

		// If the identified field is of type String:

		else if (t.toString().equalsIgnoreCase("class java.lang.String")) {

			f.set(this, stk.nextToken());
			parse = br.readLine();

		}

		// If the identified field is of type Date:

		else if (t.toString().equalsIgnoreCase("class java.util.Date")) {

			// Appropriate filtering needs to be added here;

			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"MM-dd-yyyy_HH:mm:ss", Locale.US);

				sdf.setCalendar(Calendar.getInstance());
				Date d = sdf.parse(stk.nextToken());
				f.set(this, d);
			} catch (IllegalAccessException ex2) {
				ex2.printStackTrace();
			} catch (IllegalArgumentException ex2) {
				ex2.printStackTrace();
			} catch (ParseException ex2) {
				ex2.printStackTrace();
			}

			parse = br.readLine();

		}

		else if (t.toString().equalsIgnoreCase("class java.io.File")) {

			File iFile = new File(stk.nextToken());
			f.set(this, iFile);
			parse = br.readLine();
		}

		// If the type is none of the above, conversion hasn't been
		// implemented therefore ignore and continue.

		else {
			System.out.println("Type " + t.toString()
					+ " cannot be converted.  Continuing...");
			parse = br.readLine();
		}
	}
}
