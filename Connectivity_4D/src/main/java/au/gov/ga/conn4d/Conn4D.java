/*******************************************************************************
 * Copyright 2014 Geoscience Australia (www.ga.gov.au)
 * @author - Johnathan Kool (Geoscience Australia)
 * 
 * Licensed under the BSD-3 License
 * 
 * http://opensource.org/licenses/BSD-3-Clause
 *  
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *  
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package au.gov.ga.conn4d;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import au.gov.ga.conn4d.impl.ReleaseSet;
import au.gov.ga.conn4d.input.EnvironmentParameters;
import au.gov.ga.conn4d.input.ModelParameters;
import au.gov.ga.conn4d.input.ReleaseFileReader;
import au.gov.ga.conn4d.utils.TimeConvert;

/**
 * Conn4D: Lagrangian particle tracking program
 */

public class Conn4D {

	private static ModelParameters modelParameters = new ModelParameters();
	private static EnvironmentParameters environmentParameters = new EnvironmentParameters();
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

	public Conn4D(String prmfile, String cfgfile) {

		modelParameters.readFile(prmfile);
		environmentParameters.readFile(cfgfile);
		TimeZone.setDefault(TimeZone.getTimeZone(modelParameters.timezone));
	}

	/**
	 * Executes the simulation
	 */

	public void run() {

		long outertimer = System.currentTimeMillis();
		System.out.println("\nSimulation started "
				+ outerformat.format(new Date(outertimer)) + "\n");

		// Convert the model start, end and release spacing to milliseconds.
		// If the release spacing is -1 then the release spacing is the maximum
		// value of a Long.

		long start = TimeConvert.convertToMillis(modelParameters.minTimeUnits,
				modelParameters.minTime);
		long end = TimeConvert.convertToMillis(modelParameters.maxTimeUnits,
				modelParameters.maxTime);
		long relsp = modelParameters.relSp.equalsIgnoreCase("-1") ? Long.MAX_VALUE
				: TimeConvert.convertToMillis(modelParameters.relSpUnits,
						modelParameters.relSp);

		// 'Hard' end (simulation terminates such that releases stop
		// in advance of end date

		// for (long time = start; time < end-relDuration; time += relsp) {

		// 'Soft' end (simulation carries past end date until release
		// duration is complete)

		ReleaseSet rs = new ReleaseSet(modelParameters, environmentParameters);

		for (long time = start; time < end; time += relsp) {
			rs.setTime(time);
			rs.runSet();
			if (pass && !restartAt.equalsIgnoreCase("#")) {
				System.out
						.println("\n"
								+ restartAt
								+ " was set as the restart target, but was not found"
								+ " (must be an exact match).  Exiting.");
				break;
			}
		}
		
		// Perform cleanup operations

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

		Conn4D connect = new Conn4D(prmfile, cfgfile);
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
