package lagrange;
import java.util.ArrayList;


public class ProcessChain implements Process {

	private ArrayList<Process> processes = new ArrayList<Process>();
	
	public void apply(Particle p){
		for (int i = 0; i < processes.size(); i++){
			processes.get(i).apply(p);
		}
	}
	
	public void add(Process proc){
		processes.add(proc);
	}
	
	public void remove(Process proc){
		processes.remove(proc);
	}
	
	public void remove(int i){
		processes.remove(i);
	}
	
}
