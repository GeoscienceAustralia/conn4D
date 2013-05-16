package au.gov.ga.conn4d.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Writes String items to an output file using a BlockingQueue
 * 
 * @author Johnathan Kool
 * 
 */

public class ThreadWriter implements Runnable {

	/**
	 * Generic interface for writing objects using a write method. 
	 */

	private final Writer out;
	private final BlockingQueue<Item> queue = new LinkedBlockingQueue<Item>();
	private volatile boolean started = false;
	private volatile boolean stopped = false;
	
	/**
	 * Generic Item interface for writing objects.
	 */
	
	private static interface Item {
		void write(Writer out) throws IOException;
	}
	
	/**
	 * Helper class for writing Strings.
	 */

	private static class StringItem implements Item {
		private final String s;

		public StringItem(String s) {
			this.s = s;
		}

		@Override
		public void write(Writer out) throws IOException {
			out.append(s);
		}
	}

	/**
	 * Opens a ThreadWriter using a File object for the output file
	 * 
	 * @param file
	 * @throws IOException
	 */

	public ThreadWriter(File file) throws IOException {
		this.out = new BufferedWriter(new java.io.FileWriter(file));
	}

	/**
	 * Opens a ThreadWriter using a String representing the path of the output
	 * file
	 * 
	 * @param filename
	 * @throws IOException
	 */

	public ThreadWriter(String filename) throws IOException {
		this.out = new BufferedWriter(
				new java.io.FileWriter(new File(filename)));
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
			}
		}
		try {
			out.flush();
			out.close();
		} catch (IOException ignore) {
		}
	}

	/**
	 * Submits a String to the queue for writing.
	 * 
	 * @param s
	 *            - a String to be written to to the output file.
	 */

	public ThreadWriter write(String s) {
		if (!started) {
			throw new IllegalStateException(
					"open() call expected before append()");
		}
		try {
			queue.put(new StringItem(s));
		} catch (InterruptedException ignored) {
		}
		return this;
	}
}
