package lagrange.output;

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

public class ThreadWriter_Binary implements Runnable {
    private final Writer out;
    private final BlockingQueue<Item> queue = new LinkedBlockingQueue<Item>();
    private volatile boolean started = false;
    private volatile boolean stopped = false;


    /**
     * Opens a ThreadWriter using a File object for the output file
     * 
     * @param file
     * @throws IOException
     */
    
    public ThreadWriter_Binary(File file) throws IOException {
        this.out = new BufferedWriter(new java.io.FileWriter(file));
    }
    
    /**
     * Opens a ThreadWriter using a String representing the path of the
     * output file
     * 
     * @param filename
     * @throws IOException
     */
    
    public ThreadWriter_Binary(String filename) throws IOException {
        this.out = new BufferedWriter(new java.io.FileWriter(new File(filename)));
    }

    /**
     * Submits a String to the queue for writing
     * 
     * @param s
     * @return
     */
    
    public ThreadWriter_Binary write(String s) {
        if (!started) {
            throw new IllegalStateException("open() call expected before append()");
        }
        try {
            queue.put(new StringItem(s));
        } catch (InterruptedException ignored) {
        }
        return this;
    }

    /**
     * Starts the class running in its own Thread.
     */
    
    public void open() {
        this.started = true;
        new Thread(this).start();
    }

    /**
     * Keeps the class waiting to write submissions to the queue
     * as long as it is not stopped, and there is something left to write.
     */
    
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
        	out.close();
        } catch (IOException ignore) {
        }
    }

    /**
     * Prepares the class for shutdown.  The Thread will be removed
     * once the queue is empty.
     */
    
    public void close() {
        this.stopped = true;
    }

    /**
     * Generic interface for writing objects using a write method.
     * 
     * @author Johnathan Kool
     *
     */
    
    private static interface Item {
        void write(Writer out) throws IOException;
    }

    /**
     * Helper class for writing Strings.
     * 
     * @author Johnathan Kool
     *
     */
    
    private static class StringItem implements Item {
        private final String s;

        public StringItem(String s) {
            this.s = s;
        }

        public void write(Writer out) throws IOException {
            out.append(s);
        }
    }
}
