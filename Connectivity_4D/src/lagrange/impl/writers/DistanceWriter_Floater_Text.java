package lagrange.impl.writers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javolution.util.FastMap;

import lagrange.Particle;
import lagrange.output.DistanceWriter;

public class DistanceWriter_Floater_Text implements DistanceWriter{

	BufferedWriter bw;
	FastMap<String,Double> num = new FastMap<String,Double>();
	FastMap<String,Double> dist = new FastMap<String,Double>();

	/**
	 * Constructor that uses a String to generate the output file.
	 * 
	 * @param outputFile -
	 *            The path and name of the output file
	 */

	public DistanceWriter_Floater_Text(String outputFile) {

		try {

			// Create the file and use a BufferedWriter for efficiency.

			FileWriter fw = new FileWriter(outputFile);
			bw = new BufferedWriter(fw);

		} catch (IOException e) {
			System.out
					.println("Could not create/access matrix output file: "
							+ outputFile + ".\n\n");
			e.printStackTrace();
		}

	}
	
	@Override
	public synchronized void apply(Particle p){
		if(p.canSettle()&&!p.getVisited().contains(num.get(p.getDestination()))){
			if(num.get(p.getDestination())!=null){
				num.put(p.getDestination(), num.get(p.getDestination())+1);
				dist.put(p.getDestination(), dist.get(p.getDestination())+p.getDistance());
			}
			else{num.put(p.getDestination(), 1d);
			dist.put(p.getDestination(), p.getDistance());}
		}
	}
	
	public void flush(){
		try {
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			bw.flush();
			bw.close();
		} catch (IOException e) {
			System.out.println("Could not close distance output file: "
					+ bw.toString() + ".\n\n");
			e.printStackTrace();
		}

	}
}