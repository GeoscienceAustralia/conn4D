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

package au.gov.ga.conn4d.impl.readers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.input.ReleaseFileReader;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Reads release information from a text file. Very old class - may not be
 * functioning properly.
 * 
 * @author Johnathan Kool
 */

public class ReleaseFileReader_Text implements ReleaseFileReader {

	private int relID; // Release ID
	private float lon, lat, z; // Longitude, Latitude and Depth
	private int npart; // # of particles to be released
	private int ryyyy, rmm, rdd; // Year, month, date
	private String locName; // Location name

	private BufferedReader br; // Reader (for reading the file)
	private StringTokenizer stk;
	private boolean EOF = false; // Indicates whether the end of the file has
									// been reached
	private String s; // Holds the content of the next line.
	private int lineNumber = 0;
	private GeometryFactory gf = new GeometryFactory();

	public ReleaseFileReader_Text(String filename) throws FileNotFoundException {

		// Open the file and then buffer for efficiency

		FileReader fr = new FileReader(filename);
		br = new BufferedReader(fr);
		readNext();
	}

	/**
	 * Retrieves the depth of the initial release.
	 * 
	 * @return - the depth of the initial release.
	 */

	@Override
	public float getDepth() {
		return z;
	}

	/**
	 * Retrieves the latitude of the initial release.
	 * 
	 * @return - the latitude of the initial release.
	 */

	public float getLat() {
		return lat;
	}

	/**
	 * Retrieves the text name of the release location.
	 */

	@Override
	public String getLocName() {
		return locName;
	}

	/**
	 * Retrieves the longitude of the initial release.
	 * 
	 * @return - the longitude of the initial release.
	 */

	public float getLon() {
		return lon;
	}

	/**
	 * Retrieves the number of particles to be released.
	 */

	@Override
	public long getNpart() {
		return npart;
	}

	// Getters and Setters

	/**
	 * Retrieves a Geometry corresponding to the initial release.
	 * 
	 * @return - a Geometry corresponding to the initial release.
	 */

	@Override
	public Geometry getPosition() {
		return gf.createPoint(new Coordinate(lon, lat));
	}

	/**
	 * Retrieves the release day.
	 */

	public int getRdd() {
		return rdd;
	}

	/**
	 * Retrieves the release month.
	 */

	public int getRmm() {
		return rmm;
	}

	/**
	 * Retrieves the release year.
	 */

	public int getRyyyy() {
		return ryyyy;
	}

	/**
	 * Retrieves the source ID of the release
	 * 
	 * @return - the source ID of the release
	 */

	public long getSourceID() {
		return relID;
	}

	/**
	 * Indicates whether there are remaining records to be read
	 */

	@Override
	public boolean hasNext() {

		return !EOF;
	}

	/**
	 * Parses the next available record
	 */

	@Override
	public void next() {

		parse();
		lineNumber++;
		readNext();
	}

	/**
	 * Parses the information from the line
	 */

	private void parse() {

		stk = new StringTokenizer(s);

		if (stk.countTokens() != 9) {
			throw new IllegalArgumentException(
					"Invalid number of fields in the release file (Line "
							+ lineNumber
							+ ") - must equal 9.\n\nRelID, Lon, Lat, Layer, nParticles, Year, Month, Day, Name.\nEnsure there are no extra carriage returns in the file.\n\n");
		}

		relID = Integer.parseInt(stk.nextToken());
		lat = Float.parseFloat(stk.nextToken());
		lon = Float.parseFloat(stk.nextToken());
		z = Float.parseFloat(stk.nextToken());
		npart = Integer.parseInt(stk.nextToken());
		ryyyy = Integer.parseInt(stk.nextToken());
		rmm = Integer.parseInt(stk.nextToken());
		rdd = Integer.parseInt(stk.nextToken());
		locName = stk.nextToken();
	}

	/**
	 * Does the grunt work of reading the information from the line and flagging
	 * EOF if there is no more data.
	 */

	private void readNext() {

		try {
			s = br.readLine();
		} catch (IOException e) {

			System.out
					.println("WARNING: Could not read from the release file.");
			e.printStackTrace();
		}

		if (s == null) {
			EOF = true;
			return;
		}

		parse();
	}

	/**
	 * Sets the values of the passed-in Parameters object
	 */

	@Override
	public Parameters setParameters(Parameters prm) {
		prm.setLocName(locName);
		prm.setNPart(npart);
		prm.setPosition(gf.createPoint(new Coordinate(lon, lat)));
		prm.setDepth(z);
		return prm;
	}
}
