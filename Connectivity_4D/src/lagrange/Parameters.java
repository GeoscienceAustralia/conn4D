package lagrange;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Class used for shuttling parameters between the Client and
 * Server.
 */

public interface Parameters{
	
	/**
	 * Retrieves the duration after which the object is eligible 
	 * to settle (in milliseconds)
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
	 * 
	 * @return
	 */
	
	public String getDiffusionType();
	
	/**
	 * Retrieves the end time (in Java milliseconds) of the simulated release
	 * 
	 * @return
	 */
	
	
	public long getEtime();
	
	/**
	 * Retrieves the minimum integration time step (the minimum duration over which
	 * the Particles move)
	 * @return
	 */
	
	public long getH();
	
	/**
	 * Retrieves the location name of the release set.
	 * @return
	 */
	
	public String getLocName();
	
	/**
	 * Retrieves the maximum depth at which Particles are to be released 
	 * @return
	 */
	
	public double getMaxReleaseDepth();
	
	/**
	 * Retrieves an array of double corresponding to mortality parameter values
	 * @return
	 */
	
	public double[] getMortalityParameters();
	
	public abstract double getMortalityRate();
	public String getMortalityType();
	public int getNPart();
	public String getOutputFolder();
	public long getOutputFreq();
	public Geometry getPosition();
	public long getRelDuration();
	/**
	 * Retrieves the depth at which the Particles are to be released
	 * @return
	 */
	
	public double getReleaseDepth();
	public long getRelSp();
	public String getSettlementType();
	public long getStime();
	public long getTime();
	public String getWriteFolder();
	public void setCompetencyStart(long competencyStart);
	public void setCompetencyStartUnits(String competencyStartUnits);
	public void setDepth(double depth);
	public void setDepthRange(double minDepth, double maxDepth);
	public void setDiffusionType(String diffusionType);
	public void setEffectiveMigration(boolean effectiveMigration);
	public void setEtime(long etime);
	public void setH(long h);
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
