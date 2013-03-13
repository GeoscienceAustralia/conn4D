package lagrange.impl;

import java.util.concurrent.CountDownLatch;

import lagrange.Parameters;
import lagrange.ReleaseRunner;

/**
 * Creates and sets the parameters of Factory Objects used to generate Release
 * threads. Also executes the Release Threads using a cached thread pool.
 * 
 * @author Johnathan Kool
 */

public class ReleaseRunner_4D implements ReleaseRunner {

	private ReleaseFactory_4D relFactory;

	/**
	 * Single-argument constructor.
	 * 
	 * @param config
	 *            : Location-specific configuration parameters (filename)
	 */

	public ReleaseRunner_4D(String config) {
		relFactory = new ReleaseFactory_4D(config);
	}

	/**
	 * 
	 * @param prm
	 * @throws InterruptedException
	 */

	@Override
	public void run(Parameters prm) {

		// Set the parameters of the ReleaseTemplate using the Datagram

		relFactory.setParameters(prm);

		// For each and every particle...

		relFactory.setTime(prm.getTime());
		int n = prm.getNPart();

		// Ensure we start in bounds
		
		if(!relFactory.getCollisionDetection().isInBounds(prm.getTime(), prm.getMaxReleaseDepth(), prm.getCoordinates().x, prm.getCoordinates().y)){
			System.out.print("\t Initial depth/range ("+ prm.getMaxReleaseDepth() + ") is not in the water column. Continuing to the next release site.");
			relFactory.shutdown();
			return;
		}
		
		if(prm.getTime()<relFactory.getVelocityReader().getBounds()[0][0]){
			System.out.print("\t Release time occurs outside the range of velocity data values. Continuing to the next release site.");
			relFactory.shutdown();
			return;
		}

		/*
		 * The CountDownLatch is used to ensure all threads have finished before
		 * calling the shutdown operation (flushing and closing the writers)
		 */

		CountDownLatch doneSignal = new CountDownLatch(n);

		try {
			/*
			 * Use the Factory to generate releases, and submit them to the
			 * Executor.
			 */

			int ct = 0;
			
			for (long k = 0; k < n; k++) {
				Release rel = relFactory.generate();
				if(rel.toBeKilled()){doneSignal.countDown(); continue;}
				rel.setDoneSignal(doneSignal);
				Thread th = new Thread(rel);

				// Setting the priority manually to 3 to avoid hogging
				// resources

				th.setPriority(3);
				th.start();
				ct++;
			}
			
			System.out.print("\t" + ct + " effective migrant" + (ct==1?"":"s") +"\t");

			// Wait for the threads to all finish.

			//doneSignal.await();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				doneSignal.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			relFactory.shutdown();
		}
		System.gc();
	}

	/**
	 * Performs cleanup operations - primarily shutting down the Executor
	 */

	public void close() {
		//exec.shutdown();
	}
}