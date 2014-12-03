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
	 * Retrieves the scalar coefficient of diffusivity
	 */
	
	public float[] getK();

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
	 * Gets the thread pool size used by the ReleaseRunner
	 */

	public int getPoolSize();

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

	/**
	 * @return the starting time of the simulation as a long value.
	 */

	public long getStime();

	/**
	 * @return the current time of the simulation as a long value.
	 */

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
	 * @param competencyStartUnits
	 *            - the units of the competency onset duration
	 */

	public void setCompetencyStartUnits(String competencyStartUnits);

	/**
	 * @param depth
	 *            - the initial release depth as a single value.
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

	/**
	 * @param etime
	 *            - the end time of the simulation as a long value.
	 */

	public void setEtime(long etime);

	/**
	 * @param h
	 *            - the minimum integration time step interval as a long value.
	 */

	public void setH(long h);

	/**
	 * @param k
	 *            - the scalar coefficient of turbulent eddy diffusivity as a double.
	 */

	public void setK(float[] k);
	
	/**
	 * @param initialPositionType
	 *            - the initial positioning type - e.g. centroid or random
	 *            placement
	 */

	public void setInitialPositionType(String initialPositionType);

	/**
	 * @param locName
	 *            - the location designation of the release area.
	 */

	public void setLocName(String locName);

	/**
	 * @param mortalityParameters
	 *            - an array of mortality parameters for more complex types.
	 */

	public void setMortalityParameters(double[] mortalityParameters);

	/**
	 * @param mortalityRate
	 *            - sets the exponential mortality rate as a double.
	 */

	public void setMortalityRate(double mortalityRate);

	/**
	 * @param mortalityType
	 *            - sets the type of mortality to be used.
	 */

	public void setMortalityType(String mortalityType);

	/**
	 * @param mUnits
	 *            - sets the units over which the mortality is applied (e.g.
	 *            Days).
	 */

	public void setMortalityUnits(String mUnits);

	/**
	 * @param part
	 *            - the number of Particles to be released from the area.
	 */

	public void setNPart(int nPart);

	/**
	 * @param outputFolder
	 *            - the directory where output data will be written.
	 */

	public void setOutputFolder(String outputFolder);

	/**
	 * @param outputFreq
	 *            - the frequency (in model time) with which output will be
	 *            written.
	 */

	public void setOutputFreq(long outputFreq);

	/**
	 * @param poolSize
	 *            - the limit for the number of simultaneous Release threads.
	 */

	public void setPoolSize(int poolSize);

	/**
	 * @param position
	 *            - the Geometry of the area Particles will be released from.
	 */

	public void setPosition(Geometry position);

	/**
	 * @param relDuration
	 *            - the length of time a Release will span (e.g. 60 days)
	 */

	public void setRelDuration(long relDuration);

	/**
	 * @param relSp
	 *            - the spacing between Release events as a long.
	 */

	public void setRelSp(long relSp);

	/**
	 * @param settlementType
	 *            - the type of Settlement to be used (e.g. FloatOver).
	 */

	public void setSettlementType(String settlementType);

	/**
	 * @param stime
	 *            - sets the start time of the Release.
	 */

	public void setStime(long stime);

	/**
	 * @param stime
	 *            - sets the current time of the Release.
	 */

	public void setTime(long time);

	/**
	 * @param verticalMigration
	 *            - indicates whether vertical migration operations should
	 *            occur.
	 */

	public void setVerticalMigration(boolean verticalMigration);

	public void setWriteFolder(String writeFolder);

	/**
	 * @return - indicates whether effective migration should be used - i.e.
	 *         pre-kill Particles that would not make it to settlement.
	 */

	public boolean usesEffectiveMigration();

	/**
	 * @param verticalMigration
	 *            - indicates whether vertical migration operations are being
	 *            used.
	 */

	public boolean usesVerticalMigration();
}