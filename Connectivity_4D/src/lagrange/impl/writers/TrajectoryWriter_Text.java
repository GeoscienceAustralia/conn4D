package lagrange.impl.writers;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lagrange.Particle;
import lagrange.output.ThreadWriter;
import lagrange.output.TrajectoryWriter;
import lagrange.utils.TimeConvert;

/**
 * Writes pertinent trajectory data to an output file in ASCII text format.
 * 
 * @author Johnathan Kool
 * 
 */

public class TrajectoryWriter_Text implements TrajectoryWriter {

	private ThreadWriter tw;
	private ThreadWriter stl;
	private String filename;
	private String timeUnits = "Date";
	private String durationUnits = "Days";
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private boolean negCoord = false;
	private boolean isEmpty = true;

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
			
			// The second ThreadWriter is for the .set files
			// We write both at once to avoid duplicate processing.

			stl = new ThreadWriter(outputFile.substring(0, outputFile
					.lastIndexOf("."))+ ".set");
			stl.open();
			
			// Write column headers

			tw
					.write("ID\tTIME\tDURATION\tDEPTH\tLON\tLAT\tDIST\tSTATUS\tNODATA\n");
			stl.write("ID\tTIME\tDURATION\tDEPTH\tLON\tLAT\tDIST\tLOCATION\n");

		} catch (IOException e) {
			System.out
					.println("\n\nCould not create/access trajectory output file: "
							+ outputFile + ".\n\n");
			System.out.println("Please ensure that the output directory given in the configuration file exists.");
		}

	}

	/**
	 * Actually writes the data parameters to the output file.
	 * 
	 * @param p -
	 *            The particle whose information will be persisted.
	 */

	@Override
	public synchronized void apply(Particle p) {

		StringBuffer sb = new StringBuffer();

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
				stl.write(sb.toString() + p.getDestination() + "\n");
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
		isEmpty = false;

		//this.notifyAll();
		}
	}


	/**
	 * Closes and cleans up the output file
	 */

	@Override
	public void close() {

		// Close and flush the trajectory file
		
		tw.close();

		// Close and flush the settlement file

		stl.close();
		
		if(isEmpty){
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
}
