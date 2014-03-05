package au.gov.ga.conn4d.impl.writers;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.output.MatrixWriter;
import au.gov.ga.conn4d.output.ThreadWriter;


/**
 * Persists particle information to a text-based output file.
 * 
 * @author Johnathan Kool
 * 
 */

public class MatrixWriter_Text implements MatrixWriter {

	private ThreadWriter bw;
	private Map<String, Long> m = new TreeMap<String, Long>();
	private String filename;
	private boolean deleteEmpty = true;

	/**
	 * Constructor that uses a String to generate the output file.
	 * 
	 * @param outputFile
	 *            - The path and name of the output file
	 */

	public MatrixWriter_Text(String outputFile) {

		filename = outputFile;

		try {

			// Create the file and use a BufferedWriter for efficiency.

			bw = new ThreadWriter(outputFile);
			bw.open();

		} catch (IOException e) {
			System.out.println("Could not create/access matrix output file: "
					+ outputFile + ".\n\n");
			e.printStackTrace();
		}
	}

	/**
	 * Performs the action of persisting the Particle's relevant information.
	 * 
	 * @param p
	 */

	@Override
	public synchronized void apply(Particle p) {
		if (p.canSettle()) {
			if (m.get(p.getDestination()) != null) {
				m.put(p.getDestination(), m.get(p.getDestination()) + 1);
			} else {
				m.put(p.getDestination(), 1l);
			}
		}
	}

	@Override
	public void close() {

		try {
			bw.write(m.toString());

			if (deleteEmpty) {
				if (m.isEmpty()) {
					File f = new File(filename);
					f.delete();
				}
			}
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
	}
}
