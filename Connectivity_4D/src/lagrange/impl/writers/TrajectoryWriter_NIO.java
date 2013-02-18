package lagrange.impl.writers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lagrange.Particle;
import lagrange.output.TrajectoryWriter;
import lagrange.utils.TimeConvert;

/**
 * Writes pertinent trajectory data to an output file in ASCII text format.
 * 
 * @author Johnathan Kool
 * 
 */

public class TrajectoryWriter_NIO implements TrajectoryWriter {

	private BufferedWriter bw;
	private BufferedWriter stl;
	private String timeUnits = "Date";
	private String durationUnits = "Days";
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private boolean negCoord;

	/**
	 * Constructor that uses a String to generate the output file.
	 * 
	 * @param outputFile -
	 *            The path and name of the output file
	 */

	public TrajectoryWriter_NIO(String outputFile) {

		try {

			// Create the file and use a BufferedWriter for efficiency.

			FileWriter fw = new FileWriter(outputFile);
			FileWriter fw2 = new FileWriter(outputFile.substring(0, outputFile
					.lastIndexOf("."))
					+ ".set");
			bw = new BufferedWriter(fw);
			
			stl = new BufferedWriter(fw2);

			// Write column headers

			bw
					.write("ID\tTIME\tDURATION\tDEPTH\tLON\tLAT\tDIST\tSTATUS\tNODATA\n");
			stl.write("ID\tTIME\tDURATION\tDEPTH\tLON\tLAT\tDIST\tLOCATION\n");

		} catch (IOException e) {
			System.out
					.println("Could not create/access trajectory output file: "
							+ outputFile + ".\n\n");
			e.printStackTrace();
		}

	}

	/**
	 * Actually writes the data parameters to the output file.
	 * 
	 * @param p -
	 *            The particle whose information will be persisted.
	 */

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
		if (p.getX() > 180) {
			sb.append(-(360d - p.getX()) + "\t");
		} else {
			sb.append(p.getX() + "\t");
		}
		sb.append(p.getY() + "\t");
		sb.append(p.getDistance() + "\t");
		if (p.canSettle() == true) {
			try {
				stl.write(sb.toString() + p.getDestination() + "\n");
			} catch (IOException e) {
				System.out.println("Could not access settlement output file: "
						+ stl.toString() + ".\n\n");
				e.printStackTrace();
			}
			sb.append("S" + p.getDestination() + "\t");

		} else if (p.isLost() == true) {
			sb.append("L\t");
			p.setRecording(false);
		} else if (p.isDead() == true) {
			sb.append("M\t");
			p.setRecording(false);
		} else {
			sb.append("I\t");
		}
		sb.append(p.wasNoData());
		sb.append("\n");
		try {
			bw.write(sb.toString());
			bw.flush();
		} catch (IOException e) {
			System.out.println("Could not access trajectory output file: "
					+ bw.toString() + ".\n\n");
			e.printStackTrace();
		}
		this.notifyAll();
		}
	}

	public void flush() {
		try {
			bw.flush();
			stl.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Closes and cleans up the output file
	 */

	public void close() {

		// Close and flush the trajectory file

		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			System.out.println("Could not close trajectory output file: "
					+ bw.toString() + ".\n\n");
			e.printStackTrace();
		}

		// Close and flush the settlement file

		try {
			stl.flush();
			stl.close();
		} catch (IOException e) {
			System.out.println("Could not close settlement output file: "
					+ bw.toString() + ".\n\n");
			e.printStackTrace();
		}
	}

	public String getTimeUnits() {
		return timeUnits;
	}

	public void setTimeUnits(String timeUnits) {
		this.timeUnits = timeUnits;
	}

	public String getDurationUnits() {
		return durationUnits;
	}

	public void setDurationUnits(String durationUnits) {
		this.durationUnits = durationUnits;
	}
	public void setNegCoord(boolean negCoord){
		this.negCoord = negCoord;
	}
}
