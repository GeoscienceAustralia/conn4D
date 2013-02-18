package lagrange.impl.readers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import lagrange.Parameters;
import lagrange.input.ReleaseFileReader;

/**
 * 
 * @author Johnathan Kool
 * 
 */

public class ReleaseFileReader_Text implements ReleaseFileReader{

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
	 * Indicates whether there are remaining records to be read
	 */

	public boolean hasNext() {

		return !EOF;
	}

	/**
	 * Parses the next available record
	 */

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
					"Invalid number of fields in the release file (Line " + lineNumber +") - must equal 9.\n\nRelID, Lon, Lat, Layer, nParticles, Year, Month, Day, Name.\nEnsure there are no extra carriage returns in the file.\n\n");
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
	
	public Parameters setParameters(Parameters prm){
		prm.setLocName(locName);
		prm.setNPart(npart);
		prm.setPosition(gf.createPoint(new Coordinate(lon,lat)));
		prm.setDepth(z);
		return prm;
	}

	// Getters and Setters

	public long getSourceID() {
		return relID;
	}

	public float getLon() {
		return lon;
	}

	public float getLat() {
		return lat;
	}
	
	public Geometry getPosition() {
		return gf.createPoint(new Coordinate(lon,lat));
	}

	public float getDepth() {
		return z;
	}

	public long getNpart() {
		return npart;
	}

	public int getRyyyy() {
		return ryyyy;
	}

	public int getRmm() {
		return rmm;
	}

	public int getRdd() {
		return rdd;
	}

	public String getLocName() {
		return locName;
	}
}
