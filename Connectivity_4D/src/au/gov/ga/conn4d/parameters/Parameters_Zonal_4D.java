package au.gov.ga.conn4d.parameters;

import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.utils.TimeConvert;
import cern.jet.random.Uniform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.shape.random.RandomPointsBuilder;

/**
 * Zonal-type Parameters class for 4-D releases. Zonal releases release
 * particles within a given geometry. If the geometry is a point, then the
 * particles are released at that location. If the geometry is a polygon, then
 * points are released randomly within that polygon.
 * 
 * @author Johnathan Kool
 * 
 */

public class Parameters_Zonal_4D implements Parameters {

	private String locName = "Test";
	private int nPart = 1;
	private Geometry position;
	private double minDepth = 0;
	private double maxDepth = 0;
	private long stime = 0;
	private long etime = 0;
	private long time = 0;
	private long relSp = 1;
	private long relDuration = 1;
	private long h = 7200;
	private long outputFreq = 1;
	private long competencyStart = 0;
	private String competencyStartUnits = "Days";
	private String mortalityType = "Exponential"; // "Weibull";
	private double mortalityRate = 0;
	private double[] mortalityParameters = { 1 / .0635, .7559 };
	private boolean verticalMigration = false;
	private boolean true3D = true;
	private String outputFolder = "Test";
	private String settlementType = "Simple";
	private String diffusionType = "Simple";
	private String initialPositionType = "Centroid";
	private String writeFolder = "./Test";
	private String mortalityUnits = "Days";
	private boolean effectiveMigration = true;
	private RandomPointsBuilder rpb = new RandomPointsBuilder();

	/**
	 * Retrieves the onset of competency according to the associated units of
	 * measurement.
	 */

	@Override
	public long getCompetencyStart() {
		return competencyStart;
	}

	/**
	 * Retrieves the units of the onset of competency.
	 */

	public String getCompetencyStartUnits() {
		return competencyStartUnits;
	}

	/**
	 * Retrieves the coordinates of a release location. This performs the work
	 * of converting from a distribution (uniform within a Geometry) to a
	 * specific point location.
	 */
	
	@Override
	public Coordinate getCoordinates() {
		if (position.getGeometryType().equalsIgnoreCase("Point")) {
			return position.getCoordinate();
		}
		if (position.getGeometryType().equalsIgnoreCase("Polygon")
				|| position.getGeometryType().equalsIgnoreCase("MultiPolygon")) {
			if (initialPositionType.equalsIgnoreCase("RANDOM")) {
				rpb.setExtent(position);
				rpb.setNumPoints(1);
				return rpb.getGeometry().getCoordinate();
			} else {
				return position.getCentroid().getCoordinate();
			}
		} else {
			throw new IllegalArgumentException(
					"Release file error: Spatial type "
							+ position.getGeometryType()
							+ " could not be resolved");
		}
	}

	/**
	 * Retrieves the form of turbulent diffusion to be used by the model - e.g.
	 * None, Simple3D.
	 */

	@Override
	public String getDiffusionType() {
		return diffusionType;
	}

	/**
	 * Retrieves the ending time of the model run in Java milliseconds.
	 */

	@Override
	public long getEtime() {
		return etime;
	}

	/**
	 * Retrieves the minimum integration time step being used by the model
	 * (typically 2 hours). This is the time period over which a single
	 * Runge-Kutta integration pass occurs.
	 */

	@Override
	public long getH() {
		return h;
	}

	/**
	 * Retrieves the initial positioning type being used by the model (e.g.
	 * Centroid vs. Random)
	 */

	@Override
	public String getInitialPositionType() {
		return initialPositionType;
	}

	/**
	 * Retrieves the name of the release location.
	 */

	@Override
	public String getLocName() {
		return locName;
	}

	/**
	 * Retrieves the maximum release depth.
	 */

	@Override
	public double getMaxReleaseDepth() {
		return maxDepth;
	}

	/**
	 * Retrieves parameter values (as an array of doubles) associated with the
	 * Mortality function.
	 */

	@Override
	public double[] getMortalityParameters() {
		return mortalityParameters;
	}

	/**
	 * Retrieves a single scalar mortality rate associated with the Mortality
	 * function (implicitly assumed to be Exponential).
	 */

	@Override
	public double getMortalityRate() {
		return mortalityRate / (TimeConvert.convertToMillis(mortalityUnits, 1));
	}

	/**
	 * Retrieves the type of Mortality to be used by the model (e.g. None,
	 * Exponential, Weibull)
	 */

	@Override
	public String getMortalityType() {
		return mortalityType;
	}

	/**
	 * Retrieves the number of Particles to be released
	 */

	@Override
	public int getNPart() {
		return nPart;
	}

	/**
	 * Retrieves the path location of the output data folder
	 */

	@Override
	public String getOutputFolder() {
		return outputFolder;
	}

	/**
	 * Retrieves the frequency with which output should be written out in model
	 * time as Java milliseconds.
	 */

	@Override
	public long getOutputFreq() {
		return outputFreq;
	}

	/**
	 * Retrieves the geometry over which Particles are to be released.
	 */

	@Override
	public Geometry getPosition() {
		return position;
	}
	
	/**
	 * Retrieves the total duration of the release (e.g. 30 days)
	 */

	@Override
	public long getRelDuration() {
		return relDuration;
	}

	/**
	 * Retrieves the coordinates of a release depth. This performs the work
	 * of converting from a distribution (uniform over a depth range) to a
	 * specific point location.
	 */
	
	@Override
	public double getReleaseDepth() {
		if (minDepth == maxDepth) {
			return maxDepth;
		}
		return Uniform.staticNextDoubleFromTo(minDepth, maxDepth);
	}
	
	/**
	 * Retrieves the spacing between releases in milliseconds.
	 */

	@Override
	public long getRelSp() {
		return relSp;
	}

	/**
	 * Retrieves the Settlement type to be used by the model (e.g. None, Simple,
	 * FloatOver).
	 */
	
	@Override
	public String getSettlementType() {
		return settlementType;
	}
	
	/**
	 * Retrieves the starting time of the model run (in model time).
	 */

	@Override
	public long getStime() {
		return stime;
	}
	
	/**
	 * Retrieves the current model time.
	 */

	@Override
	public long getTime() {
		return time;
	}
	
	/**
	 * Retrieves the path of the output write folder.
	 */

	@Override
	public String getWriteFolder() {
		return writeFolder;
	}
	
	/**
	 * Designates whether the model is true 3D (i.e. vertical movement)
	 * or is semi-3D - layered horizontal motion.
	 */

	public boolean isTrue3D() {
		return true3D;
	}

	/**
	 * Sets the onset of competency according to the associated units of
	 * measurement.
	 */
	
	@Override
	public void setCompetencyStart(long competencyStart) {
		this.competencyStart = competencyStart;
	}

	/**
	 * Sets the units of the onset of competency.
	 */
	
	@Override
	public void setCompetencyStartUnits(String units) {
		this.competencyStartUnits = units;
	}
	
	/**
	 * Sets the release depth as a single value.
	 */

	@Override
	public void setDepth(double depth) {
		this.minDepth = depth;
		this.maxDepth = depth;
	}
	
	/**
	 * Sets the release depth as a range.
	 */

	@Override
	public void setDepthRange(double minDepth, double maxDepth) {
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
	}

	@Override
	public void setDiffusionType(String diffusionType) {
		this.diffusionType = diffusionType;
	}
	
	/**
	 * Sets whether effective migration should be used (i.e. pre-kill
	 * Particles to avoid unnecessary processing).
	 */

	@Override
	public void setEffectiveMigration(boolean effectiveMigration) {
		this.effectiveMigration = effectiveMigration;
	}

	/**
	 * Sets the ending time of the model run in Java milliseconds.
	 */
	
	@Override
	public void setEtime(long etime) {
		this.etime = etime;
	}

	/**
	 * Sets the minimum integration time step being used by the model
	 * (typically 2 hours). This is the time period over which a single
	 * Runge-Kutta integration pass occurs.
	 */
	
	@Override
	public void setH(long h) {
		this.h = h;
	}

	/**
	 * Sets the initial positioning type being used by the model (e.g. Centroid
	 * vs. Random)
	 */
	
	@Override
	public void setInitialPositionType(String diffusionType) {
		this.initialPositionType = diffusionType;
	}

	/**
	 * Sets the name of the release location.
	 */
	
	@Override
	public void setLocName(String locName) {
		this.locName = locName;
	}

	/**
	 * Sets parameter values (as an array of doubles) associated with the
	 * Mortality function.
	 */
	
	@Override
	public void setMortalityParameters(double[] mortalityParameters) {
		this.mortalityParameters = mortalityParameters;
	}

	/**
	 * Sets the mortality rate using a scalar value
	 */
	
	@Override
	public void setMortalityRate(double mortalityRate) {
		this.mortalityRate = mortalityRate;
	}

	/**
	 * Sets the type of Mortality to be used by the model (e.g. None,
	 * Exponential, Weibull)
	 */
	
	@Override
	public void setMortalityType(String mortalityType) {
		this.mortalityType = mortalityType;
	}

	/**
	 * Sets the units associated with the Mortality parameters (e.g.
	 * per second, day etc.)
	 */
	
	@Override
	public void setMortalityUnits(String units) {
		this.mortalityUnits = units;
	}

	/**
	 * Sets the number of Particles to be released
	 */
	
	@Override
	public void setNPart(int part) {
		nPart = part;
	}

	/**
	 * Sets the path location of the output data folder
	 */
	
	@Override
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	/**
	 * Sets the frequency with which output should be written out in model
	 * time as Java milliseconds.
	 */
	
	@Override
	public void setOutputFreq(long outputFreq) {
		this.outputFreq = outputFreq;
	}

	/**
	 * Sets the geometry over which Particles are to be released.
	 */
	
	@Override
	public void setPosition(Geometry position) {
		this.position = position;
	}

	/**
	 * Sets the total duration of the release (e.g. 30 days)
	 */
	
	@Override
	public void setRelDuration(long relDuration) {
		this.relDuration = relDuration;
	}

	/**
	 * Retrieves the spacing between releases in milliseconds.
	 */
	
	@Override
	public void setRelSp(long relSp) {
		this.relSp = relSp;
	}

	/**
	 * Retrieves the Settlement type to be used by the model (e.g. None, Simple,
	 * FloatOver).
	 */
	
	@Override
	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	/**
	 * Sets the starting time of the model run (in model time).
	 */
	
	@Override
	public void setStime(long stime) {
		this.stime = stime;
	}
	
	/**
	 * Sets the current model time.
	 */

	@Override
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Sets whether the model is true 3D (i.e. vertical movement)
	 * or is semi-3D - layered horizontal motion.
	 */
	
	public void setTrue3D(boolean true3D) {
		this.true3D = true3D;
	}

	/**
	 * Sets whether the model should use behaviourally-driven vertical
	 * movement routines.
	 */
	
	@Override
	public void setVerticalMigration(boolean verticalMigration) {
		this.verticalMigration = verticalMigration;
	}

	/**
	 * Designates the path of the folder for writing Output data.
	 */
	
	@Override
	public void setWriteFolder(String writeFolder) {
		this.writeFolder = writeFolder;
	}

	/**
	 * Indicates whether the model is making use of effective migration.
	 */
	
	@Override
	public boolean usesEffectiveMigration() {
		return effectiveMigration;
	}

	/**
	 * Indicates whether the model should use behaviourally-driven vertical
	 * movement routines.
	 */
	
	@Override
	public boolean usesVerticalMigration() {
		return verticalMigration;
	}
}
