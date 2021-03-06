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

package au.gov.ga.conn4d.parameters;


import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.utils.TimeConvert;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;


public class Parameters_Test implements Parameters{
	
	private String locName = "Test";
	private int nPart;
	private int poolSize;
	private Geometry position;
	private double depth;
	private long stime;
	private long etime;
	private long time;
	private long relSp;
	private long relDuration;
	private long h = 7200000;
	private float[] k = {2f,2f,1.0E-5f};
	private long outputFreq;
	private long competencyStart;
	private String competencyStartUnits = "Days";
	private String  mortalityType = "Weibull";
	private double mortalityRate = 0;
	private double[] mortalityParameters = {1/.0635, .7559};
	private boolean verticalMigration = false;
	private String outputFolder = "Test";
	private String settlementType = "Simple";
	private String diffusionType = "Simple";
	private String initialPositionType = "centroid";
	private String mortalityUnits = "Days";
	private String writeFolder;
	private boolean effectiveMigration= true;
	GeometryFactory gf = new GeometryFactory();
	
	@Override
	public long getCompetencyStart() {
		return competencyStart;
	}
	
	public String getCompetencyStartUnits() {
		return competencyStartUnits;
	}
	
	@Override
	public Coordinate getCoordinates() {
		return new Coordinate(position.getCentroid().getX(),position.getCentroid().getY());
	}
	
	@Override
	public String getDiffusionType(){
		return diffusionType;
	}
	
	@Override
	public long getEtime() {
		return etime;
	}
	
	@Override
	public long getH() {
		return h;
	}
	
	@Override
	public float[] getK() {
		return k;
	}
	@Override
	public String getInitialPositionType(){
		return initialPositionType;
	}
	@Override
	public String getLocName() {
		return locName;
	}
	public double getLon() {
		return position.getCentroid().getX();
	}
	@Override
	public double getMaxReleaseDepth() {
		return depth;
	}
	@Override
	public double[] getMortalityParameters() {
		return mortalityParameters;
	}
	@Override
	public double getMortalityRate() {
		return mortalityRate/(TimeConvert.convertToMillis(mortalityUnits, 1));
	}
	@Override
	public String getMortalityType() {
		return mortalityType;
	}
	@Override
	public int getNPart() {
		return nPart;
	}
	@Override
	public String getOutputFolder() {
		return outputFolder;
	}
	@Override
	public long getOutputFreq() {
		return outputFreq;
	}
	
	/**
	 * Sets the thread pool size to be used by the ReleaseRunner
	 */
	
	@Override
	public int getPoolSize(){
		return poolSize;
	}
	
	@Override
	public Geometry getPosition(){
		return position;
	}
	@Override
	public long getRelDuration() {
		return relDuration;
	}
	@Override
	public double getReleaseDepth() {
		return depth;
	}
	@Override
	public long getRelSp() {
		return relSp;
	}
	@Override
	public String getSettlementType(){
		return settlementType;
	}
	@Override
	public long getStime() {
		return stime;
	}
	@Override
	public long getTime(){
		return time;
	}
	@Override
	public String getWriteFolder(){
		return writeFolder;
	}
	@Override
	public void setCompetencyStart(long competencyStart) {
		this.competencyStart = competencyStart;
	}
	@Override
	public void setCompetencyStartUnits(String units) {
		this.competencyStartUnits = units;
	}
	@Override
	public void setDepth(double depth) {
		this.depth = depth;
	}
	@Override
	public void setDepthRange(double mindepth, double maxdepth) {
		if (mindepth != maxdepth) {
			throw new IllegalArgumentException(
					"This class does not support the use of a depth range.");
		}
	}
	@Override
	public void setDiffusionType(String diffusionType){
		this.diffusionType = diffusionType;
	}
	@Override
	public void setEffectiveMigration(boolean effectiveMigration) {
		this.effectiveMigration = effectiveMigration;
	}
	@Override
	public void setEtime(long etime) {
		this.etime = etime;
	}
	@Override
	public void setH(long h) {
		this.h = h;
	}
	@Override
	public void setK(float[] k) {
		this.k = k;
	}
	@Override
	public void setInitialPositionType(String diffusionType){
		this.initialPositionType = diffusionType;
	}
	@Override
	public void setLocName(String locName) {
		this.locName = locName;
	}
	@Override
	public void setMortalityParameters(double[] mortalityParameters) {
		this.mortalityParameters = mortalityParameters;
	}
	@Override
	public void setMortalityRate(double mortalityRate) {
		this.mortalityRate = mortalityRate;
	}
	@Override
	public void setMortalityType(String mortalityType) {
		this.mortalityType = mortalityType;
	}
	@Override
	public void setMortalityUnits(String units){
		this.mortalityUnits = units;
	}
	@Override
	public void setNPart(int part) {
		nPart = part;
	}
	@Override
	public void setOutputFolder(String outputFolder) {
		if (outputFolder.equalsIgnoreCase("jobfs")||outputFolder.equalsIgnoreCase("$jobfs")){
			this.outputFolder=System.getenv("PBS_JOBFS");
		}
		else{
			this.outputFolder = outputFolder;
		}
	}
	@Override
	public void setOutputFreq(long outputFreq) {
		this.outputFreq = outputFreq;
	}
	
	/**
	 * Sets the thread pool size to be used by the ReleaseRunner
	 */
	
	@Override
	public void setPoolSize(int poolSize){
		this.poolSize=poolSize;
	}
	
	public void setPosition(double x, double y, double z){
		position = gf.createPoint(new Coordinate(x,y,z));
		depth = z;
	}
	@Override
	public void setPosition(Geometry position) {
		this.position = position;
		this.depth = position.getCoordinate().z;
	}
	@Override
	public void setRelDuration(long relDuration) {
		this.relDuration = relDuration;
	}
	@Override
	public void setRelSp(long relSp) {
		this.relSp = relSp;
	}
	@Override
	public void setSettlementType(String settlementType){
		this.settlementType = settlementType;
	}
	@Override
	public void setStime(long stime) {
		this.stime = stime;
	}
	@Override
	public void setTime(long time){
		this.time = time;
	}
	@Override
	public void setVerticalMigration(boolean verticalMigration) {
		this.verticalMigration = verticalMigration;
	}
	@Override
	public void setWriteFolder(String writeFolder){
		this.writeFolder = writeFolder;
	}
	@Override
	public boolean usesEffectiveMigration() {
		return effectiveMigration;
	}
	@Override
	public boolean usesVerticalMigration() {
		return verticalMigration;
	}
}
