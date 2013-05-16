package au.gov.ga.conn4d;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import au.gov.ga.conn4d.impl.readers.ReleaseFileReader_Shapefile_4D;
import au.gov.ga.conn4d.impl.readers.ReleaseFileReader_Text;
import au.gov.ga.conn4d.parameters.Parameters_Zonal_4D;
import au.gov.ga.conn4d.utils.TimeConvert;

import au.gov.ga.conn4d.impl.ReleaseRunner_4D;
import au.gov.ga.conn4d.input.ParameterOverride;
import au.gov.ga.conn4d.input.ReleaseFileReader;


/**
 * Lagrangian particle tracking program.
 */

public class Connect_4D {
	private static ParameterOverride gp = new ParameterOverride();
	private static ReleaseFileReader rf;
	private static ReleaseRunner_4D rr;
	private static DateFormat outerformat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss zzz");
	private static NumberFormat deltaformat = new DecimalFormat("#.000");
	private static String prmfile = "default.prm";
	private static String cfgfile = "default.cfg";
	private static String restartAt = "#";
	private static boolean pass = true;

	/**
	 * 2-argument constructor
	 * 
	 * @param prmfile
	 *            - String representing the path to the 'global' model
	 *            parameters
	 * @param cfgfile
	 *            - String representing the path to the 'local'
	 *            machine-dependent parameters
	 */

	public Connect_4D(String prmfile, String cfgfile) {

		gp.readFile(prmfile);
		TimeZone.setDefault(TimeZone.getTimeZone(gp.timezone));

		// Initialize the release runner using the local configuration.

		rr = new ReleaseRunner_4D(cfgfile);
	}

	/**
	 * Executes the simulation
	 */

	public void run() {

		long outertimer = System.currentTimeMillis();
		System.out.println("\nSimulation started "
				+ outerformat.format(new Date(outertimer)) + "\n");
		SimpleDateFormat innerformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat innerformat_full = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss zzz");

		try {

			long start = TimeConvert.convertToMillis(gp.minTimeUnits,
					gp.minTime);
			long end = TimeConvert.convertToMillis(gp.maxTimeUnits, gp.maxTime);
			long relsp = gp.relSp.equalsIgnoreCase("-1") ? Long.MAX_VALUE
					: TimeConvert.convertToMillis(gp.relSpUnits, gp.relSp);

			// 'Hard' end (simulation terminates such that releases stop
			// in advance of end date

			// for (long time = start; time < end-relDuration; time += relsp) {

			// 'Soft' end (simulation carries past end date until release
			// duration is complete)

			for (long time = start; time < end; time += relsp) {

				long reltimer = System.currentTimeMillis();
				if (gp.minTimeUnits.equalsIgnoreCase("Date")) {
					System.out.println("Release date "
							+ innerformat_full.format(time) + ":");
				} else {
					System.out.println("Release " + (time + 1) + ":");
				}

				// Try reading the release file (if it can't be found, then
				// quit).

				if (gp.relFileName.endsWith(".shp")) {
					rf = new ReleaseFileReader_Shapefile_4D(gp.relFileName);
				} else {
					rf = new ReleaseFileReader_Text(gp.relFileName);
				}

				// Allows for re-starting the code

				while (rf.hasNext()) {

					if (pass && !restartAt.equalsIgnoreCase("#")) {
						if (rf.getLocName().equalsIgnoreCase(restartAt)) {
							pass = false;
						} else {
							rf.next();
							continue;
						}
					}

					Parameters prm = new Parameters_Zonal_4D();
					long timer = System.currentTimeMillis();

					// Set parameters using the current line of the release file
					// as well as the 'global' parameters

					rf.setParameters(prm);
					gp.setParameters(prm);

					// Produce a single run.
					// if time units are dates, name folders by date
					String folder = "";
					if (gp.minTimeUnits.equalsIgnoreCase("Date")) {
						folder = innerformat.format(new Date(time));
					}

					// otherwise name according to the time value
					else {
						folder = "T_"
								+ TimeConvert.convertToMillis(gp.minTimeUnits,
										time);
					}

					prm.setTime(time);
					prm.setWriteFolder(prm.getOutputFolder() + "/" + folder);
					System.out.print("\t" + prm.getLocName());

					rr.run(prm);

					System.out.println("\tComplete\t("
							+ TimeConvert.millisToString(System
									.currentTimeMillis() - timer)
							+ ")\t"
							+ outerformat.format(new Date(System
									.currentTimeMillis())));
					rf.next();
					System.gc();
				}

				if (pass && !restartAt.equalsIgnoreCase("#")) {
					System.out
							.println("\n"
									+ restartAt
									+ " was set as the restart target, but was not found (must be an exact match).  Exiting.");
					break;
				}
				
				if (gp.minTimeUnits.equalsIgnoreCase("Date")) {
					System.out.println("Release date "
							+ innerformat_full.format(time)
							+ " complete. ("
							+ TimeConvert.millisToString(System
									.currentTimeMillis() - reltimer) + ")\n");
				} else {
					System.out
							.println("\nRelease "
									+ (time + 1)
									+ " complete. ("
									+ deltaformat.format(((double) System
											.currentTimeMillis() - (double) reltimer) / 1000d)
									+ "s)\n");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		rr.close();

		System.out.println("\nTime finished: "
				+ new Date(System.currentTimeMillis())
				+ " ("
				+ TimeConvert.millisToString(System.currentTimeMillis()
						- outertimer) + ")");

		System.exit(0);
	}

	/**
	 * Main class
	 * 
	 * @param args
	 * @throws Exception
	 */

	public static void main(String args[]) throws Exception {
		if (args.length > 0) {
			prmfile = args[0];
		} else {
			if (prmfile == null) {
				System.out
						.println("Usage: java -jar JB4.jar <parameter file> <configuration file>");
				System.exit(-1);
			}
		}

		if (args.length > 1) {
			cfgfile = args[1];
		} else {
			System.out
					.println("Configuration file not provided.  Using default configuration.");
		}

		if (args.length > 2) {
			restartAt = args[2];
		}

		Connect_4D connect = new Connect_4D(prmfile, cfgfile);
		connect.run();
	}

	/**
	 * Retrieves the ReleaseRunner object being used by the class.
	 */
	
	public ReleaseRunner getReleaseRunner(){
		return rr;
	}
	
	/**
	 * Retrieves the ReleaseFileReader object being used by the class.
	 */
	
	public ReleaseFileReader getReleaseFileReader(){
		return rf;
	}
	
	/**
	 * Sets the restart location for resuming a prematurely terminated run.
	 * @param val - a String representing the release area where last processing
	 * occurred.
	 */
	
	public void setRestart(String val){
		restartAt = val;
	}
}
