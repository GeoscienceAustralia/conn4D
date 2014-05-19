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
