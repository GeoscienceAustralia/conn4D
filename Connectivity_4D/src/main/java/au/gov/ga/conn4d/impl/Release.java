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

package au.gov.ga.conn4d.impl;

import java.util.concurrent.CountDownLatch;

import au.gov.ga.conn4d.CollisionDetector;
import au.gov.ga.conn4d.Diffuser;
import au.gov.ga.conn4d.Mortality;
import au.gov.ga.conn4d.Movement;
import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.Settlement;
import au.gov.ga.conn4d.VerticalMigration;
import au.gov.ga.conn4d.impl.collision.CollisionDetector_None;
import au.gov.ga.conn4d.output.TrajectoryWriter;
import au.gov.ga.conn4d.utils.GeometryUtils;

import com.vividsolutions.jts.geom.Coordinate;

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
	private VerticalMigration vm;// = new Vertical_BET();
	private Settlement sm;
	private Movement mv;
	private Diffuser df;
	private CollisionDetector collisionDetector;
	private CountDownLatch doneSignal;
	private TrajectoryWriter tw;
	private long time;
	private long id = -1;
	private long competencyStart = 0;
	private boolean kill = false;
	private boolean negCoord = false;
	private boolean negOceanCoord = false;
	private boolean writeInitial = true;

	/**
	 * Retrieves the CollisionDetector associated with this instance
	 */

	public CollisionDetector getCollisionDetector() {
		return collisionDetector;
	}

	/**
	 * Retrieves the Diffusion object associated with this instance
	 */

	public Diffuser getDiffusion() {
		return df;
	}

	/**
	 * Retrieves the ID of the Particle
	 */

	public long getId() {
		return id;
	}

	/**
	 * Retrieves the Mortality object associated with this instance
	 */

	public Mortality getMortality() {
		return mort;
	}

	/**
	 * Retrieves the Movement object associated with this instance
	 */

	public Movement getMovement() {
		return mv;
	}

	/**
	 * Retrieves the Parameter object associated with this instance.
	 */

	public Parameters getParameters() {
		return prm;
	}

	/**
	 * Retrieves the Settlement object associated with this instance.
	 */

	public Settlement getSettlement() {
		return sm;
	}

	/**
	 * Retrieves the timestamp (in milliseconds) of this instance.
	 */

	public long getTime() {
		return time;
	}

	/**
	 * Retrieves the TrajectoryWriter associated with this instance.
	 */

	public TrajectoryWriter getTrajectoryWriter() {
		return tw;
	}

	/**
	 * Retrieves the VerticalMigration object associated with this instance.
	 */

	public VerticalMigration getVerticalMigration() {
		return vm;
	}

	/**
	 * Flags whether the release will be killed before effective activity
	 * (pre-processing mortality to save on computation)
	 */

	public boolean preKill() {
		if (prm == null) {
			return false;
		}
		competencyStart = prm.getCompetencyStart();
		Particle p = new Particle();
		for (int i = 0; i < competencyStart; i += prm.getH()) {
			mort.apply(p);
			if (p.isDead()) {
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

			if (!negOceanCoord && negCoord) {
				p.setX((c.x + 360) % 360);
			} else {
				p.setX(c.x);
			}
			p.setPX(p.getX());
			p.setY(c.y);
			p.setPY(p.getY());
			p.setZ(prm.getReleaseDepth());
			p.setPZ(p.getZ());
			p.setX0(p.getX());
			p.setY0(p.getY());
			p.setZ0(p.getZ());

			// Ensuring initial position is above the seafloor and in the
			// water column.

			if (!(collisionDetector instanceof CollisionDetector_None)) {
				double floor = collisionDetector.getBoundary()
						.getPreciseBoundaryDepth(p.getX(), p.getY());

				if (p.getZ() < floor) {
					if (floor + 1 > 0) {
						p.setLost(true);
						p.setError(true);
					} else {
						p.setZ(floor + 1);
					}
				}
			}

			p.setBirthday(time);
			p.setSource(prm.getLocName());
			p.setCompetencyStart(prm.getCompetencyStart());
			p.setT(time);

			if (writeInitial) {
				tw.apply(p);
			}

			long rd = prm.getRelDuration();
			// int ct = 0;
			long writect = prm.getH();

			// For each time step...

			for (long t = 0; t < rd; t += prm.getH()) {

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

				// Positional check?

				// For now, if above surface, return to surface.
				// we should instead randomize in the mixed layer.

				if (p.getZ() > 0) {
					p.setZ(0);
				}

				// Apply vertical migration

				if (prm.usesVerticalMigration()) {
					vm.apply(p);
				}

				// Calculate the change in distance first, otherwise
				// reflection would truncate the distance traveled.

				p.setDistance(p.getDistance()
						+ GeometryUtils.distance_Sphere(p.getPX(), p.getPY(),
								p.getX(), p.getY()));

				// Check and see if the particle has bounced off land. (only
				// check if a NODATA cell has been encountered in the
				// interpolation range)

				if (p.isNearNoData()) {
					collisionDetector.handleIntersection(p);
				}

				// Can the particle settle?

				sm.apply(p);

				// Update the Particle's time reference after processes are
				// complete so that the time reflects state upon completion of
				// the
				// processes.

				p.setT(p.getT() + prm.getH());

				// If we have exceeded the output frequency threshold,
				// or if settling has occurred, then write.

				if (writect >= prm.getOutputFreq()

				// write immediately if it can settle, and is over
				// appropriate habitat, unless we're using FloatOver

						|| (p.canSettle() && !(sm instanceof au.gov.ga.conn4d.impl.behavior.Settlement_FloatOver))) {
					tw.apply(p);
					writect = 0;
				}

				writect += prm.getH();

				// System.out.print("\n" + ct + "\t" + p.toString());
				// ct++;

				// If we're lost or dead, then there's no point in going on...

				if (p.isFinished()) {
					break;
				}
			}
			p.setFinished(true);

			// end of time step

		} catch (NullPointerException npe) {
			System.out.println("Null Pointer Error - \n" + "mort: " + mort
					+ "\n" + "sm: " + sm + "\n" + "mv: " + mv + "\n" + "df: "
					+ df + "\n" + "collisionDetect: " + collisionDetector
					+ "\n" + "tw: " + tw + "\n" + p.toString() + "\n");
			npe.printStackTrace();
		} catch (Exception e) {
			p.setError(true);
			System.out.println("ERROR: " + e.toString());
			e.printStackTrace();
			tw.apply(p);
		} finally {
			// Ensure the doneSignal is passed upon termination.
			if (doneSignal != null) {
				doneSignal.countDown();
			}
			mv.close();
			mv = null;
			df = null;
		}
	}

	/**
	 * Sets the BoundaryHandler object for this instance.
	 * 
	 * @param collisionDetector
	 *            - The CollisionDetector object
	 */

	public void setCollisionDetector(CollisionDetector collisionDetector) {
		this.collisionDetector = collisionDetector;
	}

	/**
	 * Sets the Diffusion object for this instance.
	 * 
	 * @param df
	 *            - The Diffusion object
	 */

	public void setDiffusion(Diffuser df) {
		this.df = df;
	}

	/**
	 * Sets the CountDownLatch object associated with this instance.
	 * 
	 * @param doneSignal
	 *            - CountDownLatch object for coordinating multiple simultaneous
	 *            Releases.
	 */

	public void setDoneSignal(CountDownLatch doneSignal) {
		this.doneSignal = doneSignal;
	}

	/**
	 * Sets the identifier for a Release instance.
	 * 
	 * @param id
	 *            - The identifier number
	 */

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Flag the instance for termination.
	 * 
	 * @param kill
	 *            - boolean indicating whether the instance should be terminated
	 */

	public void setKill(boolean kill) {
		this.kill = kill;
	}

	/**
	 * Sets the Mortality object for this instance.
	 * 
	 * @param mort
	 *            - The Mortality object
	 */

	public void setMortality(Mortality mort) {
		this.mort = mort;
	}

	/**
	 * Sets the Movement object for this instance.
	 * 
	 * @param mv
	 *            - The Movement object
	 */

	public void setMovement(Movement mv) {
		this.mv = mv;
	}

	/**
	 * Sets whether negative coordinates are being used by the geographic frame
	 * of reference
	 * 
	 * @param negCoord
	 */

	public void setNegativeCoordinates(boolean negCoord) {
		this.negCoord = negCoord;
	}

	/**
	 * Sets whether negative coordinates are being used by the oceanographic
	 * data
	 * 
	 * @param negOceanCoord
	 */

	public void setNegativeOceanCoordinates(boolean negOceanCoord) {
		this.negOceanCoord = negOceanCoord;
	}

	/**
	 * Sets the Parameters object for this instance.
	 * 
	 * @param prm
	 *            - The Parameters object
	 */

	public void setParameters(Parameters prm) {
		this.prm = prm;
	}

	/**
	 * Sets the Settlement object for this instance.
	 * 
	 * @param sm
	 *            - The Settlement object
	 */

	public void setSettlement(Settlement sm) {
		this.sm = sm;
	}

	/**
	 * Sets the timestamp (in milliseconds) for this instance.
	 * 
	 * @param time
	 *            - timestamp (in milliseconds)
	 */

	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Sets the TrajectoryWriter object for this instance.
	 * 
	 * @param tw
	 *            - The TrajectoryWriter object
	 */

	public void setTrajectoryWriter(TrajectoryWriter tw) {
		this.tw = tw;
	}

	/**
	 * Sets the VerticalMigration object for this instance.
	 * 
	 * @param vm
	 *            - The VerticalMigration object
	 */

	public void setVerticalMigration(VerticalMigration vm) {
		this.vm = vm;
	}

	/**
	 * Sets whether this instance should be terminated
	 */

	public boolean toBeKilled() {
		return kill;
	}
}
