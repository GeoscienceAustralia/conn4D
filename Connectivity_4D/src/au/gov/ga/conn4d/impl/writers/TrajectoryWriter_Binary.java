package au.gov.ga.conn4d.impl.writers;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.output.TrajectoryWriter;

/**
 * Writes String items to an output file using a BlockingQueue
 * 
 * @author Johnathan Kool
 * 
 */

public class TrajectoryWriter_Binary implements Runnable,TrajectoryWriter {
	
	private static boolean negCoord = false;
	@SuppressWarnings("unused")
	private String timeUnits = "Date";
	@SuppressWarnings("unused")
	private String durationUnits = "Days";

	/**
	 * Generic interface for writing objects using a write method. 
	 */

	private final DataOutputStream out;
	private final BlockingQueue<Item> queue = new LinkedBlockingQueue<Item>();
	private volatile boolean started = false;
	private volatile boolean stopped = false;
	
	/**
	 * Generic Item interface for writing objects.
	 */
	
	private static interface Item {
		void write(DataOutputStream out) throws IOException;
	}
	
	/**
	 * Helper class for writing Strings.
	 */

	private static class DataItem implements Item {
		private final String SOURCE;
		private final long ID;
		private final long TIME;
		private final long DURATION;
		private final double Z;
		private final double X;
		private final double Y;
		private final double DISTANCE;
		private final String STATUS;
		private final boolean NODATA;

		public DataItem(String source, long id, long time, long duration, double z, double x, double y, double distance, String status, boolean nodata) {
			SOURCE = source;
			ID = id;
			TIME = time;
			DURATION = duration;
			Z = z;
			X = x;
			Y = y;
			DISTANCE = distance;
			STATUS = status;
			NODATA = nodata;
		}

		@Override
		public void write(DataOutputStream out) throws IOException {
			try {
				out.writeUTF(SOURCE);
				out.writeChar(31);
				out.writeLong(ID);
				out.writeChar(31);
				out.writeLong(TIME);
				out.writeChar(31);
				out.writeLong(DURATION);
				out.writeChar(31);
				out.writeDouble(Z);
				out.writeChar(31);
				if (negCoord) {
					out.writeDouble(-(360d - X));
				} else {
					out.writeDouble(X);
				}
				out.writeChar(31);
				out.writeDouble(Y);
				out.writeChar(31);
				out.writeDouble(DISTANCE);
				out.writeChar(31);
				out.writeUTF(STATUS);
				out.writeChar(31);
				out.writeBoolean(NODATA);
				out.writeChar(30);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void apply(Particle p){
		
		String status;
		if (p.canSettle() == true) {
			status = "S" + p.getDestination();
		} else if (p.isLost() == true) {
			status = "L";
			p.setRecording(false);
		} else if (p.isDead() == true) {
			status = "M";
			p.setRecording(false);
		} else if (p.wasError() == true) {
			status = "X";
			p.setRecording(false);
		} else {
			status = "I";
		}
		
		DataItem item = new DataItem(p.getSource(),p.getID(),p.getT(),p.getAge(),p.getZ(),p.getX(),p.getY(),p.getDistance(),status,p.wasNoData());
		write(item);
	}

	/**
	 * Opens a ThreadWriter using a File object for the output file
	 * 
	 * @param outputFile - The File object to be written to
	 * @throws IOException
	 */

	public TrajectoryWriter_Binary(File outputFile) throws IOException {
		this.out = 	new DataOutputStream(new BufferedOutputStream(
				new GZIPOutputStream(new FileOutputStream(outputFile))));
	}

	/**
	 * Opens a ThreadWriter using a String representing the path of the output
	 * file
	 * 
	 * @param filename - The path of the File object to be written to
	 * @throws IOException
	 */

	public TrajectoryWriter_Binary(String filename) throws IOException {
		this.out = new DataOutputStream(new BufferedOutputStream(
				new GZIPOutputStream(new FileOutputStream(new File(filename)))));
	}
	
	/**
	 * Opens a ThreadWriter using a File object for the output file
	 * 
	 * @param outputFile - The File object to be written to
	 * @throws IOException
	 */

	public TrajectoryWriter_Binary(File outputFile, int buffersize) throws IOException {
		this.out = 	new DataOutputStream(new BufferedOutputStream(
				new GZIPOutputStream(new FileOutputStream(outputFile)),buffersize));
	}

	/**
	 * Opens a ThreadWriter using a String representing the path of the output
	 * file
	 * 
	 * @param filename
	 * @throws IOException
	 */

	public TrajectoryWriter_Binary(String filename, int buffersize) throws IOException {
		this.out = new DataOutputStream(new BufferedOutputStream(
				new GZIPOutputStream(new FileOutputStream(new File(filename))),buffersize));
	}

	/**
	 * Prepares the class for shutdown. The Thread will be removed once the
	 * queue is empty.
	 */

	public void close() {
		
		this.stopped = true;
	}

	/**
	 * Starts the class running in its own Thread.
	 */

	public void open() {
		this.started = true;
		new Thread(this).start();
	}

	/**
	 * Keeps the class waiting to write submissions to the queue as long as it
	 * is not stopped, and there is something left to write.
	 */

	@Override
	public void run() {
		while (!stopped || queue.size() > 0) {
			try {
				Item item = queue.poll(100, TimeUnit.MICROSECONDS);
				if (item != null) {
					try {
						item.write(out);
					} catch (IOException e) {
						System.out.println("Could not access output file: "
								+ out.toString() + ".\n\n");
						e.printStackTrace();
					}
				}
			} catch (InterruptedException e) {
				try {
					out.flush();
				} catch (IOException ignore) {
				}
			}
		}
		try {
			//out.writeChar(28);
			out.flush();
			out.close();
		} catch (IOException ignore) {
		}
	}

	/**
	 * Submits a String to the queue for writing.
	 * 
	 * @param item
	 *            - the DataItem to be written to to the output file.
	 */

	public TrajectoryWriter_Binary write(DataItem item) {
		if (!started) {
			throw new IllegalStateException(
					"open() call expected before append()");
		}
		try {
			queue.put(item);
		} catch (InterruptedException ignored) {
		}
		return this;
	}
	
	@Override
	public void setTimeUnits(String timeUnits) {
		this.timeUnits = timeUnits;
	}
	
	@Override
	public void setDurationUnits(String durationUnits) {
		this.durationUnits = durationUnits;
	}
	@Override
	public void setNegCoord(boolean negCoord){
		TrajectoryWriter_Binary.negCoord = negCoord;
	}
}
