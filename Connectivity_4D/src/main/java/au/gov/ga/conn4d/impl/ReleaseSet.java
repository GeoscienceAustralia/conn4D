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

package au.gov.ga.conn4d.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.ReleaseRunner;
import au.gov.ga.conn4d.impl.readers.ReleaseFileReader_Shapefile_4D;
import au.gov.ga.conn4d.impl.readers.ReleaseFileReader_Text;
import au.gov.ga.conn4d.impl.writers.TrajectoryWriter_Binary;
import au.gov.ga.conn4d.input.EnvironmentParameters;
import au.gov.ga.conn4d.input.ModelParameters;
import au.gov.ga.conn4d.input.ReleaseFileReader;
import au.gov.ga.conn4d.parameters.Parameters_Zonal_4D;
import au.gov.ga.conn4d.utils.TimeConvert;

/**
 * Handles running sets of releases (individual track lines)
 * 
 * @author Johnathan Kool
 */

public class ReleaseSet {

	private ReleaseFileReader rf_reader;
	private ReleaseRunner rr;
	private SimpleDateFormat innerFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat outerFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss zzz");
	private SimpleDateFormat fullFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss zzz");
	private NumberFormat deltaformat = new DecimalFormat("#.000");
	private ModelParameters modelParameters;
	private EnvironmentParameters environmentParameters;
	private long time;
	private String restartAt;
	private TrajectoryWriter_Binary tb = null;

	/**
	 * Two-argument constructor accepting ModelParameters and EnvironmentParameters.
	 * The Constructor is set up this way so that the code can operate in a client-server
	 * type relationship with the client submitting a job to a machine with a local configuration.
	 * 
	 * @param modelParameters
	 * @param environmentParameters
	 */
	
	public ReleaseSet(ModelParameters modelParameters, EnvironmentParameters environmentParameters){
		this.modelParameters = modelParameters;
		this.environmentParameters = environmentParameters;
		this.rr = new ReleaseRunner_4D(environmentParameters);
		this.outerFormat.setTimeZone(TimeZone.getTimeZone(System.getProperty("user.timezone")));
	}
	
	/**
	 * Runs the set of releases
	 */
	
	public void runSet(){
		
		long reltimer = System.currentTimeMillis();
		
		if (modelParameters.minTimeUnits.equalsIgnoreCase("Date")) {
			System.out.println("Release date "
					+ fullFormat.format(time) + ":");
		} else {
			System.out.println("Release " + (time + 1) + ":");
		}
		
		// Set the output path

		String outputFolder = environmentParameters.trajOutputDir;
		int bufferSize = environmentParameters.bufferSize;
		
		if(outputFolder.equalsIgnoreCase("jobfs")){
			outputFolder = System.getenv("PBS_JOBFS");
		}

		File outputDir = new File(outputFolder + File.separator + modelParameters.outputFolder);
		
		if(!outputDir.exists()){
			outputDir.mkdir();
		}
		
		String outputPath = outputFolder + File.separator + modelParameters.outputFolder+File.separator+innerFormat.format(new Date(time))+".dat";
		
		System.out.println("Writing to " + outputPath + "...");
		
		// Writing to binary files is currently hard-coded.
		
		try {
			tb = new TrajectoryWriter_Binary(outputPath, bufferSize);
			tb.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		rr.setWriter(tb);
		
		// Try reading the release file (if it can't be found, then
		// quit).

		try {
			if (modelParameters.relFileName.endsWith(".shp")) {
				rf_reader = new ReleaseFileReader_Shapefile_4D(modelParameters.relFileName);
			} else {
				rf_reader = new ReleaseFileReader_Text(modelParameters.relFileName);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Iterate through each release point
		
		while (rf_reader.hasNext()) {

			Parameters prm = new Parameters_Zonal_4D();
			long timer = System.currentTimeMillis();

			// Set parameters using the current line of the release file
			// as well as the 'global' parameters

			rf_reader.setParameters(prm);
			modelParameters.setParameters(prm);

			// Produce a single run.
			// if time units are dates, name folders by date
			
			String folder = "";
			if (modelParameters.minTimeUnits.equalsIgnoreCase("Date")) {
				folder = innerFormat.format(new Date(time));
			}

			// otherwise name according to the time value
			
			else {
				folder = "T_"
						+ TimeConvert.convertToMillis(modelParameters.minTimeUnits,
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
					+ outerFormat.format(new Date(System
							.currentTimeMillis())));
			rf_reader.next();
			System.gc();
		}
		
		if (modelParameters.minTimeUnits.equalsIgnoreCase("Date")) {
			System.out.println("Release date "
					+ fullFormat.format(time)
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
		
		tb.flush();
		tb.close();
		rf_reader.close();
	}

	/**
	 * Closes resources when finished (runs ReleaseRunner close)
	 */
	
	public void close(){
		
		rr.close();
	}
	
	// Getters and setters

	public ModelParameters getModelParameters() {
		return modelParameters;
	}

	public void setsetModelParameters(ModelParameters modelParameters) {
		this.modelParameters = modelParameters;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getRestartAt() {
		return restartAt;
	}

	public void setRestartAt(String restartAt) {
		this.restartAt = restartAt;
	}
}
