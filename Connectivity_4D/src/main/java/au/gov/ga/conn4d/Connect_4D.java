package au.gov.ga.conn4d;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import au.gov.ga.conn4d.impl.ReleaseSet;
import au.gov.ga.conn4d.input.ConfigurationOverride;
import au.gov.ga.conn4d.input.ParameterOverride;
import au.gov.ga.conn4d.input.ReleaseFileReader;
import au.gov.ga.conn4d.utils.TimeConvert;

/**
 * Lagrangian particle tracking program.
 * 
 * @author - Johnathan Kool, Geoscience Australia
 */

public class Connect_4D {
	private static ParameterOverride prm_override = new ParameterOverride();
	private static ConfigurationOverride cfg_override = new ConfigurationOverride();
	private static ReleaseFileReader rf_reader;
	private DateFormat outerformat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss zzz");

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

		prm_override.readFile(prmfile);
		cfg_override.readFile(cfgfile);
		TimeZone.setDefault(TimeZone.getTimeZone(prm_override.timezone));
	}

	/**
	 * Executes the simulation
	 */

	public void run() {

		long outertimer = System.currentTimeMillis();
		System.out.println("\nSimulation started "
				+ outerformat.format(new Date(outertimer)) + "\n");

		long start = TimeConvert.convertToMillis(prm_override.minTimeUnits,
				prm_override.minTime);
		long end = TimeConvert.convertToMillis(prm_override.maxTimeUnits,
				prm_override.maxTime);
		long relsp = prm_override.relSp.equalsIgnoreCase("-1") ? Long.MAX_VALUE
				: TimeConvert.convertToMillis(prm_override.relSpUnits,
						prm_override.relSp);

		// 'Hard' end (simulation terminates such that releases stop
		// in advance of end date

		// for (long time = start; time < end-relDuration; time += relsp) {

		// 'Soft' end (simulation carries past end date until release
		// duration is complete)

		ReleaseSet rs = new ReleaseSet(prm_override, cfg_override);

		for (long time = start; time < end; time += relsp) {
			rs.setTime(time);
			rs.runSet();
			if (pass && !restartAt.equalsIgnoreCase("#")) {
				System.out
						.println("\n"
								+ restartAt
								+ " was set as the restart target, but was not found (must be an exact match).  Exiting.");
				break;
			}
		}

		rs.close();

		System.out.println("\nTime finished: "
				+ outerformat.format(new Date(System.currentTimeMillis()))
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
	 * Retrieves the ReleaseFileReader object being used by the class.
	 */

	public ReleaseFileReader getReleaseFileReader() {
		return rf_reader;
	}

	/**
	 * Sets the restart location for resuming a prematurely terminated run.
	 * 
	 * @param val
	 *            - a String representing the release area where last processing
	 *            occurred.
	 */

	public void setRestart(String val) {
		restartAt = val;
	}
}
