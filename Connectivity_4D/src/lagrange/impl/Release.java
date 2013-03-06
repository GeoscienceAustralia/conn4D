package lagrange.impl;

import java.util.concurrent.CountDownLatch;

import com.vividsolutions.jts.geom.Coordinate;

import lagrange.CollisionDetector;
import lagrange.Diffusion;
import lagrange.Mortality;
import lagrange.Movement;
import lagrange.Parameters;
import lagrange.Particle;
import lagrange.Settlement;
import lagrange.VerticalMigration;
import lagrange.output.DistanceWriter;
import lagrange.output.MatrixWriter;
import lagrange.output.TrajectoryWriter;
import lagrange.utils.GeometryUtils;

/**
 * 
 * Performs actions associated with releasing a single Particle object from a
 * Habitat element.
 * 
 * @author Johnathan Kool
 */

public class Release implements Runnable {

	private Parameters prm;
	private Mortality mort;
	private VerticalMigration vm;
	private Settlement sm;
	private Movement mv;
	private Diffusion df;
	private CollisionDetector collisionDetect;
	private CountDownLatch doneSignal;
	private TrajectoryWriter tw;
	private DistanceWriter dw;
	private MatrixWriter mw;
	private long time;
	private long id = -1;
	private long competencyStart = 0;
	private boolean kill = false;
	private boolean negCoord = false;
	private boolean negOceanCoord = false;

	public boolean preKill(){
		if(prm==null){return false;}
		competencyStart = prm.getCompetencyStart();
		Particle p = new Particle();
		for(int i = 0; i < competencyStart; i+=prm.getH()){
			mort.apply(p);
			if(p.isDead()){
				kill = true;
				break;
			}
		}
		return kill;
	}

	/**
	 * Release event of a single particle from a single polygon
	 */

	@Override
	public void run() {
		
		Particle p = new Particle();
		
		// Create the particle object.

		// Set the ID.

		p.setID(id);

		try {

			// Then set the coordinates.

			Coordinate c = prm.getCoordinates();

			if(!negOceanCoord&&negCoord){
				p.setX((c.x + 360) % 360);
			} else {p.setX(c.x);}
			p.setPX(p.getX());
			p.setY(c.y);
			p.setPY(p.getY());
			p.setZ(prm.getDepth());	
			p.setBirthday(time);
			p.setSource(prm.getLocName());
			p.setCompetencyStart(prm.getCompetencyStart());
			
			long rd = prm.getRelDuration();
			//int ct = 0;
			long writect = 0;
			
			// For each time step...

			for (long t = 0; t < rd; t += prm.getH()) {
				
				// Update the Particle's time reference

				p.setT(time + t);

				// If mortality was pre-processed, avoid double-processing
				
				if (prm.usesEffectiveMigration()) {
					if (p.getAge() >= p.getCompetencyStart()) {

						mort.apply(p);
						if (p.isDead()) {
							break;
						}
					}

				// Or, go about it the traditional way.

				} else {
					mort.apply(p);
					if (p.isDead()) {
						break;
					}
				}

				// Use the Runge-Kutta function to move the particle
				
				mv.apply(p);

				// Apply stochastic turbulent velocity

				df.apply(p);

				if (p.isLost()) {
					tw.apply(p);
					break;
				}

				//Positional check?
				
				// For now, if above surface, return to surface.
				// we should instead randomize in the mixed layer.

				if(p.getZ()>0){p.setZ(0);}
				
				// Apply vertical migration

				if (prm.usesVerticalMigration()) {
					vm.apply(p);
				}

				// Calculate the change in distance first, otherwise
				// reflection would truncate the distance traveled.

				p.setDistance(p.getDistance()
						+ GeometryUtils.distance_Sphere(p.getPX(), p.getPY(), p.getX(),
								p.getY()));

				// Check and see if the particle has bounced off land. (only
				// check if a NODATA cell has been encountered in the
				// interpolation range)
				
				if (p.isNearNoData()) {
					collisionDetect.handleIntersection(p);
				}

				// Can the particle settle?

				sm.apply(p);

				// If we have exceeded the output frequency threshold,
				// or if settling has occurred, then write.

				if (writect >= prm.getOutputFreq()
						
						// write immediately if it can settle, and is over
						// appropriate habitat, unless we're using FloatOver
						
						|| (p.canSettle() && 
								!(sm instanceof lagrange.impl.behavior.Settlement_FloatOver))) {
					tw.apply(p);
					mw.apply(p);
					dw.apply(p);
					writect = 0;
				}
				
				writect += prm.getH();
				
				//System.out.print("\n" + ct + "\t" + p.toString());
				//ct++;

				// If we're lost or dead, then there's no point in going on...
				
				if (p.isFinished()) {
					break;
				}
			} 
			p.setFinished(true);
			
			// end of time step
			
		} catch (StackOverflowError e){
			e.printStackTrace();
		} catch(Exception e) {			
			p.setError(true);
			System.out.println("ERROR: " + e.toString());
			e.printStackTrace();
			tw.apply(p);
		} finally {
			//Ensure the doneSignal is passed upon termination.
			if(doneSignal!=null){
				doneSignal.countDown();
			}
			mv.close();
			mv = null;
			df = null;
		}
	}
	
	/**
	 * Retrieves the BoundaryHandler associated with this instance
	 * 
	 * @return
	 */

	public CollisionDetector getBoundaryHandler() {
		return collisionDetect;
	}

	/**
	 * Retrieves the Diffusion object associated with this instance
	 * 
	 * @return
	 */
	
	public Diffusion getDiffusion() {
		return df;
	}

	/**
	 * Retrieves the DistanceWriter associated with this instance
	 * 
	 * @return
	 */
	
	public DistanceWriter getDistanceWriter() {
		return dw;
	}

	/**
	 * Retrieves the ID of the Particle
	 * 
	 * @return
	 */
	
	public long getId() {
		return id;
	}

	/**
	 * Retrieves the MatrixWriter associated with this instance
	 * 
	 * @return
	 */
	
	public MatrixWriter getMatrixWriter() {
		return mw;
	}

	/**
	 * Retrieves the Mortality object associated with this instance
	 * 
	 * @return
	 */
	
	public Mortality getMortality() {
		return mort;
	}

	/**
	 * Retrieves the Movement object associated with this instance
	 * 
	 * @return
	 */
	
	public Movement getMovement() {
		return mv;
	}

	/**
	 * Retrieves the Parameter object associated with this instance.
	 * 
	 * @return
	 */
	
	public Parameters getParameters() {
		return prm;
	}
	
	/**
	 * Retrieves the Settlement object associated with this instance.
	 * 
	 * @return
	 */

	public Settlement getSettlement() {
		return sm;
	}
	
	/**
	 * Retrieves the timestamp (in milliseconds) of this instance.
	 * 
	 * @return
	 */

	public long getTime() {
		return time;
	}

	/**
	 * Retrieves the TrajectoryWriter associated with this instance.
	 * 
	 * @return
	 */
	
	public TrajectoryWriter getTrajectoryWriter() {
		return tw;
	}

	/**
	 * Retrieves the VerticalMigration object associated with this instance.
	 * 
	 * @return
	 */
	
	public VerticalMigration getVerticalMigration() {
		return vm;
	}

	/**
	 * Sets the BoundaryHandler object for this instance.
	 * 
	 * @param bh - The BoundaryHandler object
	 */
	
	public void setBoundaryHandler(CollisionDetector bh) {
		this.collisionDetect = bh;
	}

	/**
	 * Sets the Diffusion object for this instance.
	 * 
	 * @param df - The Diffusion object
	 */
	
	public void setDiffusion(Diffusion df) {
		this.df = df;
	}

	/**
	 * Sets the DistanceWriter object for this instance.
	 * 
	 * @param dw - The DistanceWriter object
	 */
	
	public void setDistanceWriter(DistanceWriter dw) {
		this.dw = dw;
	}

	/**
	 * Sets the CountDownLatch object associated with this instance.
	 * 
	 * @param doneSignal - CountDownLatch object for coordinating 
	 * multiple simultaneous Releases.
	 */
	
	public void setDoneSignal(CountDownLatch doneSignal) {
		this.doneSignal = doneSignal;
	}
	
	/**
	 * Sets the identifier for a Release instance.
	 * 
	 * @param id - The identifier number
	 */

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Flag the instance for termination.
	 * 
	 * @param kill - boolean indicating whether the instance should be terminated
	 */
	
	public void setKill(boolean kill) {
		this.kill = kill;
	}

	/**
	 * Sets the MatrixWriter object for this instance.
	 * 
	 * @param mw - The MatrixWriter object
	 */
	
	public void setMatrixWriter(MatrixWriter mw) {
		this.mw = mw;
	}

	/**
	 * Sets the Mortality object for this instance.
	 * 
	 * @param mort - The Mortality object
	 */
	
	public void setMortality(Mortality mort) {
		this.mort = mort;
	}

	/**
	 * Sets the Movement object for this instance.
	 * 
	 * @param mv - The Movement object
	 */
	
	public void setMovement(Movement mv) {
		this.mv = mv;
	}

	
	public void setNegativeCoordinates(boolean negCoord){
		this.negCoord=negCoord;
	}
	
	public void setNegativeOceanCoordinates(boolean negOceanCoord){
		this.negOceanCoord=negOceanCoord;
	}
	
	/**
	 * Sets the Parameters object for this instance.
	 * 
	 * @param prm - The Parameters object
	 */
	
	public void setParameters(Parameters prm) {
		this.prm = prm;
	}

	/**
	 * Sets the Settlement object for this instance.
	 * 
	 * @param sm - The Settlement object
	 */
	
	public void setSettlement(Settlement sm) {
		this.sm = sm;
	}
	
	/**
	 * Sets the timestamp (in milliseconds) for this instance.
	 * 
	 * @param time - timestamp (in milliseconds)
	 */

	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Sets the TrajectoryWriter object for this instance.
	 * 
	 * @param tw - The TrajectoryWriter object
	 */
	
	public void setTrajectoryWriter(TrajectoryWriter tw) {
		this.tw = tw;
	}

	/**
	 * Sets the VerticalMigration object for this instance.
	 * 
	 * @param vm - The VerticalMigration object
	 */
	
	public void setVerticalMigration(VerticalMigration vm) {
		this.vm = vm;
	}
	
	/**
	 * Sets whether this instance should be terminated
	 * 
	 * @return
	 */

	public boolean toBeKilled() {
		return kill;
	}
	
	public boolean usesNegativeCoordinates(){
		return negCoord;
	}
	
	public boolean usesNegativeOceanCoordinates(){
		return negOceanCoord;
	}
}