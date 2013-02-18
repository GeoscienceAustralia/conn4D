package lagrange.parameters;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

//import cern.jet.random.Uniform;
import lagrange.Parameters;
import lagrange.utils.TimeConvert;

public class Parameters_Simple implements Parameters{
	
	private String locName = "Test";
	private int nPart = 10;
	private Geometry position;
	private double depth;
	private long stime;
	private long etime;
	private long time;
	private long relSp;
	private long relDuration;
	private long h = 720000;  //minimum integration time step: 2 hours in milliseconds
	private long outputFreq;
	private long competencyStart = 1;
	private String competencyStartUnits = "Days";
	private String  mortalityType = "Weibull";
	private double mortalityRate;
	private double[] mortalityParameters = {1/.0635, .7559};
	private boolean verticalMigration = false;
	private String outputFolder = "Test";
	public String settlementType = "Simple";
	private String mortalityUnits = "Days";
	private String writeFolder;
	private boolean effectiveMigration= true;
	
	public double getDepth() {
		return depth;
	}
	public long getEtime() {
		return etime;
	}
	public long getH() {
		return h;
	}
	public Coordinate getCoordinates() {
		return new Coordinate(position.getCentroid().getX(),position.getCentroid().getY());
	}
	public String getLocName() {
		return locName;
	}
	public double getLon() {
		return position.getCentroid().getX();
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
		return competencyStart;
	}
	public String getCompetencyStartUnits() {
		return competencyStartUnits;
	}
	public Geometry getPosition(){
		return position;
	}
	public long getRelDuration() {
		return relDuration;
	}
	public long getRelSp() {
		return relSp;
	}
	public String getSettlementType(){
		return settlementType;
	}
	public long getStime() {
		return stime;
	}
	public long getTime(){
		return time;
	}
	public String getWriteFolder(){
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
	public void setSettlementType(String settlementType){
		this.settlementType = settlementType;
	}
	public void setStime(long stime) {
		this.stime = stime;
	}
	public void setTime(long time){
		this.time = time;
	}
	public void setVerticalMigration(boolean verticalMigration) {
		this.verticalMigration = verticalMigration;
	}
	public void setWriteFolder(String writeFolder){
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
