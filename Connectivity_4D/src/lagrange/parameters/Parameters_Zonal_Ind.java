package lagrange.parameters;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.shape.random.RandomPointsBuilder;

import cern.jet.random.Uniform;
import lagrange.Parameters;
import lagrange.utils.TimeConvert;

public class Parameters_Zonal_Ind implements Parameters {

	private String locName = "Test";
	private int nPart;
	private Geometry position;
	private double depth;
	private long stime;
	private long etime;
	private long time;
	private long relSp;
	private long relDuration;
	private long h;
	private long outputFreq;
	private long competencyStart;
	private String competencyStartUnits = "Days";
	private String mortalityType = "Exponential"; //"Weibull";
	private double mortalityRate = 0;
	private double[] mortalityParameters = { 1 / .0635, .7559 };
	private boolean verticalMigration = false;
	private boolean centroid = true;
	private String outputFolder = "Test";
	private String settlementType = "Simple";
	private String diffusionType = "Simple";
	private String writeFolder;
	private String mortalityUnits = "Days";
	private boolean effectiveMigration = true;
	private RandomPointsBuilder rpb = new RandomPointsBuilder();

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
	public double getReleaseDepth() {
		return depth;
	}

	@Override
	public String getDiffusionType(){
		return diffusionType;
	}
	
	@Override
	public void setDiffusionType(String diffusionType){
		this.diffusionType = diffusionType;
	}
	
	@Override
	public double getMaxReleaseDepth() {
		return depth;
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
	public String getLocName() {
		return locName;
	}

	@Override
	public double getMortalityRate() {
		return mortalityRate/(TimeConvert.convertToMillis(mortalityUnits, 1));
	}

	@Override
	public double[] getMortalityParameters() {
		return mortalityParameters;
	}

	@Override
	public int getNPart() {
		return nPart;
	}

	@Override
	public long getOutputFreq() {
		return outputFreq;
	}

	@Override
	public long getCompetencyStart() {
		return competencyStart;
	}
	
	public String getCompetencyStartUnits() {
		return competencyStartUnits;
	}

	@Override
	public Geometry getPosition() {
		return position;
	}

	@Override
	public long getRelDuration() {
		 //double[] da = new double[]{8.4,10.1,10.7,11,11.8,12.15,13.7,14.3,14.9,14.95,15.2,15.45,15.8,16,16,16.35,16.4,16.5,16.8,17,17,17.06666667,17.3,17.3,17.5,17.5,17.65,17.66666667,17.7,17.86666667,18,18,18,18,18.2,18.3,18.4,18.4,19,19.2,19.2,19.3,19.3,19.3,19.35,19.35,19.5,19.5,19.5,19.65,19.75,19.9,20,20,20,20,20.16666667,20.2,20.2,20.3,20.4,20.5,20.6,20.68333333,20.75,20.8,20.85,21,21,21.1,21.1,21.1,21.2,21.4,21.45,21.5,21.6,21.7,22,22,22,22,22,22.1,22.1,22.2,22.2,22.26666667,22.35,22.8,22.8,22.9,23,23,23,23,23.1,23.15,23.2,23.3,23.3,23.4,23.5,23.5,23.5,23.89,23.9,23.9,24,24,24.2,24.2,24.4,24.4,24.4,24.5,24.5,24.5,24.7,24.8,24.96666667,25,25,25,25,25,25,25,25.1,25.1,25.18666667,25.3,25.3,25.5,25.61666667,25.7,25.8,26,26,26.1,26.1,26.1,26.1,26.2,26.25,26.6,26.7,26.8,26.8,27,27.1,28,28,28,28.2,28.55,28.8,28.8,29,29.3,29.6,29.7,29.8,30,30,30,30.3,30.3,30.4,30.5,31,31,31,31,31.1,31.2,31.2,31.4,31.5,31.5,31.7,31.8,32,32,32,32,32.3,32.5,32.7,33,33.55,33.75,34,34,34.3,35,35,35,35.6,36,36,36,36,36.2,37,37,37,38,38,39.5,39.5,40,40,40.3,40.8,41,41.1,41.6,42,42.3,42.4,43,43,43.5,43.7,44.06666667,44.9,46.7,47,48,48,49.3,50,50,50.7,51,51.16666667,51.8,52,53,54,56,56.1,56.4,57,57,57,57,57.7,58.6,59.8,60.8,61,62,63.3,65,66.8,69,69,69,70.5,71,71,72.4,78,78.3,84,90.8,91.2,54.5};
		double[] da = new double[] { 11, 11, 11, 12, 13, 13.7, 15.8, 16, 16,
				16, 16, 16.5, 16.8, 17, 17, 17, 17.3, 17.5, 17.5, 17.7, 18, 18,
				18, 18, 18, 18, 18.4, 19, 19, 19.2, 19.3, 19.5, 20, 20, 20, 20,
				20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 21, 21, 21.5, 22, 22,
				22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22.9, 23, 23, 23,
				23, 23, 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24,
				24, 24, 24, 24.7, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25,
				25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26,
				26, 26, 26, 26.1, 26.1, 26.6, 26.7, 27, 27, 27, 27, 27, 27, 27,
				28, 28, 28, 28, 28, 28, 28, 28, 28.2, 28.8, 29, 29, 29, 29, 29,
				29, 29, 29.6, 30, 30, 30, 30, 31, 31, 31, 31, 31, 31, 31, 31.1,
				31.2, 32, 32, 32, 32, 32, 32.7, 33, 33, 33, 33, 33, 34, 34, 34,
				34, 34, 34, 34, 34, 34, 34, 35, 35, 36, 36, 36, 36, 36, 36, 37,
				37, 37, 37, 38, 38, 38, 38, 38, 39, 40, 40, 40, 40, 40, 40,
				40.8, 41, 41, 42, 42, 42, 42, 42, 43, 43, 43, 43.7, 46, 46, 47,
				47, 48, 48, 48, 48, 50, 50, 52, 52, 52, 53, 54, 55, 55, 56, 56,
				56, 57, 57, 57, 57, 57.7, 58.6, 59.8, 60, 60, 60.8, 61, 62, 63,
				67, 67, 68, 69, 70, 70, 71, 71, 71, 72, 72, 73, 73, 77, 78, 78,
				84, 85, 90, 90.8, 91.2, 99 };
		// double[] da = new double[]
		// {78,54,90,91,209,110,69,130,120,195,63,142.5,56,60,90,70,244,209,124,101.5,51};
		int choice = Uniform.staticNextIntFromTo(0, da.length - 1);
		relDuration = TimeConvert.convertToMillis("Days", da[choice]);
		return relDuration;
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

	@Override
	public boolean usesVerticalMigration() {
		return verticalMigration;
	}

	@Override
	public boolean usesEffectiveMigration() {
		return effectiveMigration;
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
	public void setPosition(Geometry position) {
		this.position = position;
	}

	@Override
	public void setLocName(String locName) {
		this.locName = locName;
	}

	@Override
	public void setMortalityRate(double mortalityRate) {
		this.mortalityRate = mortalityRate;
	}

	@Override
	public void setMortalityParameters(double[] mortalityParameters) {
		this.mortalityParameters = mortalityParameters;
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
	public void setOutputFreq(long outputFreq) {
		this.outputFreq = outputFreq;
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

	@Override
	public void setVerticalMigration(boolean verticalMigration) {
		this.verticalMigration = verticalMigration;
	}

	@Override
	public void setWriteFolder(String writeFolder) {
		this.writeFolder = writeFolder;
	}

	@Override
	public String getOutputFolder() {
		return outputFolder;
	}

	@Override
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	@Override
	public String getMortalityType() {
		return mortalityType;
	}

	@Override
	public void setMortalityType(String mortalityType) {
		this.mortalityType = mortalityType;
	}
}
