package lagrange.parameters;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

//import cern.jet.random.Uniform;
import lagrange.Parameters;
import lagrange.utils.TimeConvert;

public class Parameters_Simple implements Parameters {

	private String locName = "Test";
	private int nPart = 10;
	private Geometry position;
	private double depth;
	private long stime;
	private long etime;
	private long time;
	private long relSp;
	private long relDuration;
	private long h = 720000; // minimum integration time step: 2 hours in
								// milliseconds
	private long outputFreq;
	private long competencyStart = 1;
	private String competencyStartUnits = "Days";
	private String mortalityType = "Weibull";
	private double mortalityRate;
	private double[] mortalityParameters = { 1 / .0635, .7559 };
	private boolean verticalMigration = false;
	private String outputFolder = "Test";
	public String settlementType = "Simple";
	private String mortalityUnits = "Days";
	private String writeFolder;
	private String diffusionType;
	private boolean effectiveMigration = true;

	@Override
	public double getReleaseDepth() {
		return depth;
	}

	@Override
	public double getMaxReleaseDepth() {
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
	public long getEtime() {
		return etime;
	}

	@Override
	public long getH() {
		return h;
	}

	@Override
	public Coordinate getCoordinates() {
		return new Coordinate(position.getCentroid().getX(), position
				.getCentroid().getY());
	}

	@Override
	public String getLocName() {
		return locName;
	}

	public double getLon() {
		return position.getCentroid().getX();
	}

	@Override
	public double getMortalityRate() {
		return mortalityRate / (TimeConvert.convertToMillis(mortalityUnits, 1));
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
	public void setMortalityUnits(String units) {
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
