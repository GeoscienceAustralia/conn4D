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

package au.gov.ga.conn4d.impl.writers;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.output.ThreadWriter;
import au.gov.ga.conn4d.output.TrajectoryWriter;
import au.gov.ga.conn4d.utils.TimeConvert;


/**
 * Writes pertinent trajectory data to an output file in ASCII text format.
 * 
 * @author Johnathan Kool
 * 
 */

public class TrajectoryWriter_Text implements TrajectoryWriter {

	private ThreadWriter tw;
	private String filename;
	private String timeUnits = "Date";
	private String durationUnits = "Days";
	private boolean negCoord = false;
	private boolean emptyFile = true;

	/**
	 * Constructor that uses a String to generate the output file.
	 * 
	 * @param outputFile -
	 *            The path and name of the output file
	 */

	public TrajectoryWriter_Text(String outputFile) {
		
		filename = outputFile;

		try {

			// The first ThreadWriter is for the .trj files

			tw = new ThreadWriter(outputFile);
			tw.open();
					
			// Write column headers

			tw
					.write("ID\tTIME\tDURATION\tDEPTH\tLON\tLAT\tDIST\tSTATUS\tNODATA\n");

		} catch (IOException e) {
			throw new IllegalArgumentException("\n\nCould not create/access trajectory output file: "
					+ outputFile + ".\n\nPlease ensure that the output directory given in the configuration file exists.");
		}

	}

	/**
	 * Actually writes the data parameters to the output file.
	 * 
	 * @param p -
	 *            The particle whose information will be persisted.
	 */

	@Override
	public void apply(Particle p) {

		StringBuffer sb = new StringBuffer();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		/*
		 * Write ID, Time (as an actual Date/Time stamp), Duration (Days),
		 * Depth, Longitude, Latitude, Distance /* traveled and Status (S =
		 * settled, L = Lost, M = Dead, I = in transit)
		 */

		if(p.recording()){
		sb.append(p.getID() + "\t");
		sb.append(df.format(new Date(p.getT())) + "\t");
		sb.append(TimeConvert.convertFromMillis(durationUnits, p.getAge())
				+ "\t");
		sb.append(p.getZ() + "\t");
		if (negCoord) {
			sb.append(-(360d - p.getX()) + "\t");
		} else {
			sb.append(p.getX() + "\t");
		}
		sb.append(p.getY() + "\t");
		sb.append(p.getDistance() + "\t");
		if (p.canSettle() == true) {
			sb.append("\"S" + p.getDestination() + "\"\t");

		} else if (p.isLost() == true) {
			sb.append("L\t");
			p.setRecording(false);
		} else if (p.isDead() == true) {
			sb.append("M\t");
			p.setRecording(false);
		} else if (p.wasError()== true) {
			sb.append("X\t");
			p.setRecording(false);
		} else {
			sb.append("I\t");
		}
		
		sb.append(p.wasNoData());
		sb.append("\n");

		tw.write(sb.toString());
		emptyFile = false;
		}
	}

	public void flush(){
		tw.flush();
	}
	
	/**
	 * Closes and cleans up the output file
	 */

	@Override
	public void close() {

		// Close and flush the trajectory file
		
		tw.close();

		// Close and flush the settlement file
		
		if(emptyFile){
			File file = new File(filename);
			file.delete();
			File sfile = new File(filename.substring(0, filename.lastIndexOf("."))+ ".set");
			sfile.delete();
		}
	}

	public String getTimeUnits() {
		return timeUnits;
	}

	@Override
	public void setTimeUnits(String timeUnits) {
		this.timeUnits = timeUnits;
	}

	public String getDurationUnits() {
		return durationUnits;
	}

	@Override
	public void setDurationUnits(String durationUnits) {
		this.durationUnits = durationUnits;
	}
	
	@Override
	public void setNegCoord(boolean negCoord){
		this.negCoord = negCoord;
	}
	
	public boolean isEmptyFile(){
		return emptyFile;
	}
}
