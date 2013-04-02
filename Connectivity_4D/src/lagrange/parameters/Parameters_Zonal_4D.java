package lagrange.parameters;

import cern.jet.random.Uniform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.shape.random.RandomPointsBuilder;

import lagrange.Parameters;
import lagrange.utils.TimeConvert;

public class Parameters_Zonal_4D implements Parameters {

	private String locName = "Test";
	private int nPart;
	private Geometry position;
	private double minDepth;
	private double maxDepth;
	private long stime;
	private long etime;
	private long time;
	private long relSp;
	private long relDuration;
	private long h;
	private long outputFreq;
	private long competencyStart;
	private String competencyStartUnits;
	private String mortalityType = "Exponential"; //"Weibull";
	private double mortalityRate = 0;
	private double[] mortalityParameters = { 1 / .0635, .7559 };
	private boolean verticalMigration = false;
	private boolean true3D = true;
	private boolean centroid = true;
	private String outputFolder = "Test";
	private String settlementType = "Simple";
	private String diffusionType = "Simple";
	private String initialPositionType = "centroid";
	private String writeFolder;
	private String mortalityUnits = "Days";
	private boolean effectiveMigration = true;
	private RandomPointsBuilder rpb = new RandomPointsBuilder();

	@Override
	public long getCompetencyStart() {
		return competencyStart;
	}

	public String getCompetencyStartUnits() {
		return competencyStartUnits;
	}
	
	@Override
	public Coordinate getCoordinates() {
		if (position.getGeometryType().equalsIgnoreCase("Point")) {
			return position.getCoordinate();
		}
		if (position.getGeometryType().equalsIgnoreCase("Polygon") || position.getGeometryType().equalsIgnoreCase("MultiPolygon")) {
			if(!centroid){
				rpb.setExtent(position);
				rpb.setNumPoints(1);
			return rpb.getGeometry().getCoordinate();}
			else{
				return position.getCentroid().getCoordinate();
			}
		} else {
			throw new IllegalArgumentException(
					"Release file error: Spatial type "
							+ position.getGeometryType()
							+ " could not be resolved");
		}
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
	public String getInitialPositionType(){
		return initialPositionType;
	}
	
	@Override
	public String getLocName() {
		return locName;
	}
	
	@Override
	public double getMaxReleaseDepth(){
		return maxDepth;
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
	
	@Override
	public Geometry getPosition() {
		return position;
	}

	@Override
	public long getRelDuration() {
		return relDuration;
	}

	@Override
	public double getReleaseDepth() {
		if(minDepth==maxDepth){
			return maxDepth;
		}
		return Uniform.staticNextDoubleFromTo(minDepth, maxDepth);
	}

	@Override
	public long getRelSp() {
		return relSp;
	}

	@Override
	public String getSettlementType() {
		return settlementType;
	}

	@Override
	public long getStime() {
		return stime;
	}

	@Override
	public long getTime() {
		return time;
	}

	@Override
	public String getWriteFolder() {
		return writeFolder;
	}
	
	public boolean isTrue3D(){
		return true3D;
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
	public void setDepth(double depth){
		this.minDepth = depth;
		this.maxDepth = depth;
	}

	@Override
	public void setDepthRange(double minDepth, double maxDepth) {
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
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
		this.outputFolder = outputFolder;
	}
	
	@Override
	public void setOutputFreq(long outputFreq) {
		this.outputFreq = outputFreq;
	}

	@Override
	public void setPosition(Geometry position) {
		this.position = position;
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
	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	@Override
	public void setStime(long stime) {
		this.stime = stime;
	}

	@Override
	public void setTime(long time) {
		this.time = time;
	}

	public void setTrue3D(boolean true3D){
		this.true3D = true3D;
	}

	@Override
	public void setVerticalMigration(boolean verticalMigration) {
		this.verticalMigration = verticalMigration;
	}

	@Override
	public void setWriteFolder(String writeFolder) {
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
