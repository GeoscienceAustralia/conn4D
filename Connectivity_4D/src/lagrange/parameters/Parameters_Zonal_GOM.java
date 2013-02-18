package lagrange.parameters;

import java.util.Date;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.shape.random.RandomPointsBuilder;

import cern.jet.random.Empirical;
import cern.jet.random.Uniform;
import cern.jet.random.EmpiricalWalker;
import cern.jet.random.engine.MersenneTwister64;
import lagrange.Parameters;
import lagrange.utils.TimeConvert;

public class Parameters_Zonal_GOM implements Parameters {

	private String locName = "Test";
	private int nPart;
	private Geometry position;
	private double depth;
	private long stime;
	private long etime;
	private long time;
	private long competencyStart;
	private String competencyStartUnits = "Days";
	private long relSp;
	private long relDuration;
	private long h;
	private long outputFreq;
	private String mortalityType = "Exponential"; //"Weibull";
	private double mortalityRate = 0;
	private double[] mortalityParameters = { 1 / .0635, .7559 };
	private boolean verticalMigration = false;
	private boolean centroid = true;
	private String outputFolder = "Test";
	public String settlementType = "Simple";
	private String writeFolder;
	private String mortalityUnits = "Days";
	private boolean effectiveMigration = true;
	private RandomPointsBuilder rpb = new RandomPointsBuilder();
	//private double[] da = {.272251d,.157068d,.062827d,.188482d,.235602d,.08377d};//dpl
	//private double[][] daa = {{2,4},{4,5},{5,6},{6,7.5},{7.5,9},{9,10}};//dpl
	private double[] da = {.008d,.144d,.232d,.144d,.208d,.104d,.16d};//frnk
	private double[][] daa = {{15.5,17.5},{21.5,48.5},{48.5,80},{80,90.5},{90.5,100.5},{100.5,112.5},{112.5,120}};//frnk
	
	private EmpiricalWalker ew = new EmpiricalWalker(da,Empirical.NO_INTERPOLATION, new MersenneTwister64(new Date(System.currentTimeMillis())));

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

	public double getDepth() {
		return depth;
	}

	public long getEtime() {
		return etime;
	}

	public long getH() {
		return h;
	}

	public String getLocName() {
		return locName;
	}

	public double getMortalityRate() {
		return mortalityRate/(TimeConvert.convertToMillis(mortalityUnits, 1));
	}

	public double[] getMortalityParameters() {
		return mortalityParameters;
	}

	public int getNPart() {
		return nPart;
	}

	public long getOutputFreq() {
		return outputFreq;
	}

	public long getCompetencyStart() {
		int index = ew.nextInt();
		double day = Uniform.staticNextDoubleFromTo(daa[index][0], daa[index][1]);
		competencyStart = TimeConvert.convertToMillis(competencyStartUnits, day);
		return competencyStart;
	}

	public Geometry getPosition() {
		return position;
	}

	public long getRelDuration() {
		return relDuration;
	}

	public long getRelSp() {
		return relSp;
	}

	public String getSettlementType() {
		return settlementType;
	}

	public long getStime() {
		return stime;
	}

	public long getTime() {
		return time;
	}

	public String getWriteFolder() {
		return writeFolder;
	}

	public boolean usesVerticalMigration() {
		return verticalMigration;
	}

	public boolean usesEffectiveMigration() {
		return effectiveMigration;
	}

	public void setDepth(double depth) {
		this.depth = depth;
	}

	public void setEffectiveMigration(boolean effectiveMigration) {
		this.effectiveMigration = effectiveMigration;
	}

	public void setEtime(long etime) {
		this.etime = etime;
	}

	public void setH(long h) {
		this.h = h;
	}

	public void setPosition(Geometry position) {
		this.position = position;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}

	public void setMortalityRate(double mortalityRate) {
		this.mortalityRate = mortalityRate;
	}

	public void setMortalityParameters(double[] mortalityParameters) {
		this.mortalityParameters = mortalityParameters;
	}

	public void setMortalityUnits(String units){
		this.mortalityUnits = units;
	}
	
	public void setNPart(int part) {
		nPart = part;
	}

	public void setOutputFreq(long outputFreq) {
		this.outputFreq = outputFreq;
	}

	public void setCompetencyStart(long competencyStart) {
		this.competencyStart = competencyStart;
	}
	
	public void setCompetencyStartUnits(String units) {
		this.competencyStartUnits = units;
	}

	public void setRelDuration(long relDuration) {
		this.relDuration = relDuration;
	}

	public void setRelSp(long relSp) {
		this.relSp = relSp;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	public void setStime(long stime) {
		this.stime = stime;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setVerticalMigration(boolean verticalMigration) {
		this.verticalMigration = verticalMigration;
	}

	public void setWriteFolder(String writeFolder) {
		this.writeFolder = writeFolder;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public String getMortalityType() {
		return mortalityType;
	}

	public void setMortalityType(String mortalityType) {
		this.mortalityType = mortalityType;
	}
}
