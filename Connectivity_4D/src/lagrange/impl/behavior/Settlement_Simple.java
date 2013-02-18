package lagrange.impl.behavior;

import lagrange.Intersector;
import lagrange.Particle;
import lagrange.Settlement;
import lagrange.impl.readers.Shapefile;

public class Settlement_Simple implements Settlement, Cloneable{
	
	private Shapefile settlementPolys;
	private Intersector isect;
	
	public synchronized void apply(Particle p){
		
		if (p.getAge() >= p.getCompetencyStart()) {

			long ivalue = isect.intersect(p.getX(), p.getY());
			if (ivalue != Intersector.NO_INTERSECTION) {
				
				// We have encountered a suitable polygon.  What now?			
				
				p.setDestination(Long.toString(ivalue));
				p.setSettling(true);
				p.setFinished(true);
			}
		}
		this.notifyAll();
	}

	public void setSettlementPolys(Shapefile settlementPolys) {
		this.settlementPolys = settlementPolys;
	}

	public void setPolyCheckStart(double polyCheckStart) {
	}
	
	public void setIntersector(Intersector isect){
		this.isect = isect;
	}
	
	public Settlement_Simple clone(){
		Settlement_Simple sf = new Settlement_Simple();
		sf.setSettlementPolys(settlementPolys);
		return sf;
	}
}
