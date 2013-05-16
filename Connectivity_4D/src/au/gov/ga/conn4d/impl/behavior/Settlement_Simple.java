package au.gov.ga.conn4d.impl.behavior;

import au.gov.ga.conn4d.Intersector;
import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.Settlement;
import au.gov.ga.conn4d.impl.readers.Shapefile;

/**
 * 
 * Provides behavior that relates to settling (e.g. writing position), and
 * terminates the progress of Particles.
 * 
 * @author Johnathan Kool
 * 
 */

public class Settlement_Simple implements Settlement, Cloneable {

	private Shapefile settlementPolys;
	private Intersector isect;

	/**
	 * Performs actions associated with settling, terminating the Particle's
	 * progress.
	 */

	@Override
	public synchronized void apply(Particle p) {

		if (p.getAge() >= p.getCompetencyStart()) {

			long ivalue = isect.intersect(p.getX(), p.getY());
			if (ivalue != Intersector.NO_INTERSECTION) {

				// We have encountered a suitable polygon.
				// Set the destination value, and halt the
				// Particle's progress.

				p.setDestination(Long.toString(ivalue));
				p.setSettling(true);
				p.setFinished(true);
			}
		}
		this.notifyAll();
	}

	/**
	 * Sets the shapefile describing the settlement areas.
	 * 
	 * @param settlementPolys
	 */

	public void setSettlementPolys(Shapefile settlementPolys) {
		this.settlementPolys = settlementPolys;
	}

	/**
	 * Sets the type of Intersector being used to identify intersections between
	 * Particles/Particle Paths and the settlement polygons
	 */

	@Override
	public void setIntersector(Intersector isect) {
		this.isect = isect;
	}

	/**
	 * Returns a copy of the class instance
	 */

	@Override
	public Settlement_Simple clone() {
		Settlement_Simple sf = new Settlement_Simple();
		sf.setSettlementPolys(settlementPolys);
		return sf;
	}
}
