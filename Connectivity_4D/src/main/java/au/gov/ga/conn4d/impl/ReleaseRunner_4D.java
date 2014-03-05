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
	private int poolSize = 8;

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