package lagrange.impl.behavior;

import lagrange.Intersector;
import lagrange.Particle;
import lagrange.Settlement;
import lagrange.impl.readers.Shapefile;

public class Settlement_FloatOver implements Settlement, Cloneable{
	
	private Shapefile settlementPolys;
	private Intersector isect;
	//String destination;
	
	public synchronized void apply(Particle p){
		
		if (p.getCompetencyStart() >=0 && p.getAge() >= p.getCompetencyStart()) {

			long ivalue = isect.intersect(p.getX(), p.getY());
			if (ivalue != Intersector.NO_INTERSECTION) {
				
				// We have encountered a suitable polygon.  What now?			
				
				p.setDestination(Long.toString(ivalue));
				p.setSettling(true);
			}
			else{
				p.setSettling(false);
			}
		}
		this.notifyAll();
	}

	public void setSettlementPolys(Shapefile settlementPolys) {
		this.settlementPolys = settlementPolys;
	}

	public void setIntersector(Intersector isect){
		this.isect = isect;
	}
	
	public Settlement_FloatOver clone(){
		Settlement_FloatOver sf = new Settlement_FloatOver();
		sf.setSettlementPolys(settlementPolys);
		return sf;
	}
}
