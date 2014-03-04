package au.gov.ga.conn4d.input;

import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.utils.TimeConvert;

/**
 * Class for reading Global model parameters. Also contains a method for
 * packaging the values into a Datagram.
 * 
 * @author Johnathan Kool
 */

public class ParameterOverride extends ParameterReader {

	public boolean centroid = true;
	public String competencyStart = "30"; // Competency threshold (days)
	public String competencyStartUnits = "Days";
	public String diffusionType = "Simple";
	public boolean effectiveMigration = true; // Use effective migration? i.e.
	public String h = "7200"; // Integration time step (seconds)
	public String hUnits = "Seconds"; // Units of h
	public String initialPositionType = "centroid";
	public String maxTime = "12/25/2005";// Latest bound of the model run
	public String maxTimeUnits = "Date";
	public String minTime = "01/02/2003";// Earliest bound of the model run
	public String minTimeUnits = "Date";
	public String mortalityType = "None";
	public double mrate = 0.0d; // Mortality rate
	public String mUnits = "Days";
	public boolean negReleaseCoord = true; // Do the release coordinates use negative values?
	public final double NODATA = 1e34f; // NO DATA code
	public String outputFolder = "";
	public String outputFreq = "1"; // Frequency of writing to the output file.
	public String outputFreqUnits = "Days";
	public int poolSize = 8;
	public String relDuration = "30"; // Pelagic Larval Duration (days)
	public String relDurationUnits = "Days";
	public String relFileName = "release.txt"; // Path and name of the release
	public String relSp = "30"; // Release spacing (days)
											public String relSpUnits = "Days";
	public boolean saveTracks = true; // Save particle tracks?
												public String settleChkFreq = "1"; // Frequency of settling attempts (days)
	public String settleChkFreqUnits = "Days";
	public String settlementType = "Simple";
											public String timezone = "UTC";
	public boolean true3D = true; // Incorporate vertical velocity?
	public boolean useAdvection = true;
	public boolean vmgrt = false; // Incorporate vertical migration?
	public long writeSkip = 0; // Skip writing trajectory files by this value
	public String writeSkipUnits = "Days"; // Units by which to skip writing
	public boolean writeTrajectories = true; // Write trajectories to disk?
	public String zName = "Depth";

	/**
	 * Performs an update on Parameter class values (for transmission for distributed
	 * processing)
	 * 
	 * @param parameters
	 *            - The Parameter set to be updated
	 */

	public void setParameters(Parameters parameters) {
		parameters.setStime(TimeConvert.convertToMillis(minTimeUnits, minTime));
		parameters.setEtime(TimeConvert.convertToMillis(maxTimeUnits, maxTime));
		parameters.setRelSp(TimeConvert.convertToMillis(relSpUnits, relSp));
		parameters.setRelDuration(TimeConvert.convertToMillis(relDurationUnits,
				relDuration));
		parameters.setH(TimeConvert.convertToMillis(hUnits, h));
		parameters.setOutputFreq(TimeConvert
				.convertToMillis(outputFreqUnits, outputFreq));
		parameters.setCompetencyStart(TimeConvert.convertToMillis(competencyStartUnits,
				competencyStart));
		parameters.setMortalityRate(mrate);
		parameters.setMortalityUnits(mUnits);
		parameters.setPoolSize(poolSize);
		parameters.setVerticalMigration(vmgrt);
		parameters.setOutputFolder(outputFolder);
		parameters.setEffectiveMigration(effectiveMigration);
		parameters.setSettlementType(settlementType);
		parameters.setDiffusionType(diffusionType);
		parameters.setInitialPositionType(initialPositionType);
	}
}
