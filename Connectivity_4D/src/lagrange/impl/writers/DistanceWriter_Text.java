package lagrange.impl.writers;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import lagrange.Particle;
import lagrange.output.DistanceWriter;
import lagrange.output.ThreadWriter;

public class DistanceWriter_Text implements DistanceWriter{

	private ThreadWriter bw;
	private String filename;
	private Map<String,Double> num = new TreeMap<String,Double>();
	private Map<String,Double> dist = new TreeMap<String,Double>();
	private boolean deleteEmpty = true;

	/**
	 * Constructor that uses a String to generate the output file.
	 * 
	 * @param outputFile -
	 *            The path and name of the output file
	 */

	public DistanceWriter_Text(String outputFile) {
		
		filename = outputFile;

		try {

			// Create the file and use a BufferedWriter for efficiency.

			bw = new ThreadWriter(outputFile);
			bw.open();

		} catch (IOException e) {
			System.out
					.println("Could not create/access matrix output file: "
							+ outputFile + ".\n\n");
			e.printStackTrace();
		}

	}
	
	@Override
	public synchronized void apply(Particle p){
		if(p.canSettle()){
			if(num.get(p.getDestination())!=null){
				num.put(p.getDestination(), num.get(p.getDestination())+1);
				dist.put(p.getDestination(), dist.get(p.getDestination())+p.getDistance());
			}
			else{num.put(p.getDestination(), 1d);
			dist.put(p.getDestination(), p.getDistance());}
		}
	}
	
	@Override
	public void close() {

		try {
			Iterator<String> it = num.keySet().iterator();
			while(it.hasNext()){
				String s = it.next();
				num.put(s, dist.get(s)/num.get(s));
			}
			bw.write(num.toString());
		}
		finally {
			if(bw!=null){bw.close();
			}
			if (deleteEmpty) {
				if (dist.isEmpty()) {
					File f = new File(filename);
					f.delete();
				}
			}
		}
	}
}