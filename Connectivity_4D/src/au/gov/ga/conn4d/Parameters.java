package au.gov.ga.conn4d;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Class used for shuttling parameters between a Client and Server.
 */

public interface Parameters {

	/**
	 * Retrieves the duration after which the object is eligible to settle (in
	 * milliseconds)
	 * 
	 * @return - long representing the duration
	 */

	public long getCompetencyStart();

	/**
	 * Retrieves the position of the object as a jts.geom.Coordinate
	 * 
	 * @return - Coordinate representing the object's position
	 */

	public Coordinate getCoordinates();

	/**
	 * Sets the type of diffusion to be used by the application using a String
	 */

	public String getDiffusionType();

	/**
	 * Retrieves the end time (in Java milliseconds) of the simulated release
	 */

	public long getEtime();

	/**
	 * Retrieves the minimum integration time step (the minimum duration over
	 * which the Particles move)
	 */

	public long getH();

	/**
	 * Retrieves the location name of the release set.
	 */

	public String getLocName();

	/**
	 * Retrieves the maximum depth at which Particles are to be released.
	 */

	public double getMaxReleaseDepth();

	/**
	 * Retrieves an array of double corresponding to mortality parameter values.
	 */

	public double[] getMortalityParameters();

	/**
	 * Retrieves a scalar mortality rate value.
	 */

	public abstract double getMortalityRate();

	/**
	 * Retrieves a String indicating the type of Mortality to be used by the
	 * model.
	 */

	public String getMortalityType();

	/**
	 * Retrieves the number of Particles to be released.
	 */

	public int getNPart();

	/**
	 * Retrieves the name of the folder that will contain the output data.
	 */

	public String getOutputFolder();

	/**
	 * Retrieves the frequency with which output data will be written (according
	 * to model time)
	 */

	public long getOutputFreq();

	/**
	 * Retrieves an initial release position as a Geometry
	 */

	public Geometry getPosition();

	/**
	 * Retrieves the duration over which a release occurs (in model time)
	 */

	public long getRelDuration();

	/**
	 * Retrieves the depth at which the Particles are to be released
	 */

	public double getReleaseDepth();

	/**
	 * Retrieves the spacing between releases in model time.
	 */

	public long getRelSp();

	/**
	 * Retrieves a String indicating the type of Settlement to be used by the
	 * model.
	 */

	public String getSettlementType();

	/**
	 * Retrieves a String indicating the manner in which the initial position of
	 * the Particles is determined.
	 */

	public String getInitialPositionType();

	public long getStime();

	public long getTime();

	/**
	 * Retrieves the base folder for Output writing
	 */

	public String getWriteFolder();

	/**
	 * Sets the duration after which Particles become competent to settle.
	 * 
	 * @param competencyStart
	 *            - the duration after which Particles become competent to
	 *            settle in milliseconds.
	 */

	public void setCompetencyStart(long competencyStart);

	/**
	 * Sets the units of the competency onset duration
	 * 
	 * @param competencyStartUnits
	 */

	public void setCompetencyStartUnits(String competencyStartUnits);

	/**
	 * Sets the initial release depth as a single value.
	 * 
	 * @param depth
	 */

	public void setDepth(double depth);

	/**
	 * Sets the initial depth as a range
	 * 
	 * @param minDepth
	 *            - the minimum initial depth value.
	 * @param maxDepth
	 *            - the maximum initial depth value.
	 */

	public void setDepthRange(double minDepth, double maxDepth);

	/**
	 * Sets the DiffusionType to be used by the model (e.g. SimpleDiffusion).
	 * 
	 * @param diffusionType
	 *            - the DiffusionType to be used by the model (e.g.
	 *            SimpleDiffusion)
	 */

	public void setDiffusionType(String diffusionType);

	/**
	 * Flags whether only effective migrants (i.e. those surviving to the onset
	 * of settlement) should be processed.
	 * 
	 * @param effectiveMigration
	 *            - value indicating whether only effective migrants should be
	 *            processed.
	 */

	public void setEffectiveMigration(boolean effectiveMigration);

	public void setEtime(long etime);

	/**
	 * 
	 * @param h
	 */
	
	public void setH(long h);

	public void setInitialPositionType(String initialPositionType);

	public void setLocName(String locName);

	public void setMortalityParameters(double[] mortalityParameters);

	public void setMortalityRate(double mortalityRate);

	public void setMortalityType(String mortalityType);

	public void setMortalityUnits(String mUnits);

	public void setNPart(int part);

	public void setOutputFolder(String outputFolder);

	public void setOutputFreq(long outputFreq);

	public void setPosition(Geometry position);

	public void setRelDuration(long relDuration);

	public void setRelSp(long relSp);

	public void setSettlementType(String settlementType);

	public void setStime(long stime);

	public void setTime(long time);

	public void setVerticalMigration(boolean verticalMigration);

	public void setWriteFolder(String writeFolder);

	public boolean usesEffectiveMigration();

	public boolean usesVerticalMigration();
}
