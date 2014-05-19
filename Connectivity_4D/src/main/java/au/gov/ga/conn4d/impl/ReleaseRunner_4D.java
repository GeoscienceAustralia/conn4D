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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.ReleaseRunner;
import au.gov.ga.conn4d.input.ConfigurationOverride;
import au.gov.ga.conn4d.output.TrajectoryWriter;

/**
 * Creates and sets the parameters of Factory Objects used to generate Release
 * threads. Also executes the Release Threads using a thread pool.  A single
 * Release corresponds to a set of Particles released from a single location
 * (polygon/raster cell or voxel)
 * 
 * @author Johnathan Kool
 */

public class ReleaseRunner_4D implements ReleaseRunner {

	private ReleaseFactory_4D relFactory;
	private int poolSize = 16;

	/**
	 * Single-argument constructor.
	 * 
	 * @param config
	 *            : Location-specific configuration parameters (filename)
	 */

	public ReleaseRunner_4D(ConfigurationOverride config) {
		relFactory = new ReleaseFactory_4D(config);
	}

	/**
	 * 
	 * @param prm
	 * @throws InterruptedException
	 */

	@Override
	public void run(Parameters prm) {

		// Set the parameters of the ReleaseTemplate using Parameters

		relFactory.setParameters(prm);

		// For all particles in the group...

		relFactory.setTime(prm.getTime());
		int n = prm.getNPart();
		
		poolSize = prm.getPoolSize();

		// Ensure that the group starts in bounds. With centroid we can test as
		// a group.

		if (!relFactory.getCollisionDetection().isInBounds(prm.getTime(),
				prm.getMaxReleaseDepth(), prm.getCoordinates().x,
				prm.getCoordinates().y)) {
			System.out
					.print("\t"	+ prm.getMaxReleaseDepth()
							+ " is not in the water column\t");
			relFactory.shutdown();
			return;
		}

		if (prm.getTime() < relFactory.getVelocityReader().getBounds()[0][0]) {
			System.out
					.print("\t Release time occurs outside the range of velocity data values. Continuing to the next release site.");
			relFactory.shutdown();
			return;
		}

		/*
		 * The CountDownLatch is used to ensure all threads have finished before
		 * calling the shutdown operation (flushing and closing the writers)
		 */

		ExecutorService service = Executors.newFixedThreadPool(poolSize);
		CountDownLatch doneSignal = new CountDownLatch(n);

		try {
			/*
			 * Use the Factory to generate releases, and submit them to the
			 * Executor.
			 */

			int ct = 0;
			for (long k = 0; k < n; k++) {
				Release rel = relFactory.generate();
				if (rel.toBeKilled()) {
					doneSignal.countDown();
					continue;
				}
				rel.setDoneSignal(doneSignal);
				service.submit(rel);
				//Thread th = new Thread(rel);

				// Setting the priority manually to 3 to avoid hogging
				// resources

				// th.setPriority(3);
				//th.start();
				ct++;
			}

			System.out.print("\t" + ct + " effective migrant"
					+ (ct == 1 ? "" : "s") + "\t");

			// Wait for the threads to all finish.


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				doneSignal.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			service.shutdown();
			relFactory.shutdown();
		}
		System.gc();
	}

	/**
	 * Performs cleanup operations - primarily shutting down the Executor
	 */

	public void close() {
		// exec.shutdown();
	}
	
	public void setPoolSize(int poolSize){
		this.poolSize=poolSize;
	}
	
	public void setWriter(TrajectoryWriter tw){
		relFactory.setTrajectoryWriter(tw);
	}
}
