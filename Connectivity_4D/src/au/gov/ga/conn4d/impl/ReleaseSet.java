package au.gov.ga.conn4d.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.ReleaseRunner;
import au.gov.ga.conn4d.impl.readers.ReleaseFileReader_Shapefile_4D;
import au.gov.ga.conn4d.impl.readers.ReleaseFileReader_Text;
import au.gov.ga.conn4d.impl.writers.TrajectoryWriter_Binary;
import au.gov.ga.conn4d.input.ConfigurationOverride;
import au.gov.ga.conn4d.input.ParameterOverride;
import au.gov.ga.conn4d.input.ReleaseFileReader;
import au.gov.ga.conn4d.output.TrajectoryWriter;
import au.gov.ga.conn4d.parameters.Parameters_Zonal_4D;
import au.gov.ga.conn4d.utils.TimeConvert;

public class ReleaseSet {

	private ReleaseFileReader rf_reader;
	private ReleaseRunner rr;
	private SimpleDateFormat innerFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat fullFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss zzz");
	private NumberFormat deltaformat = new DecimalFormat("#.000");
	private ParameterOverride prm_override;
	private ConfigurationOverride cfg_override;
	private long time;
	private boolean pass;
	private String restartAt;
	//private TrajectoryWriter tw;
	private TrajectoryWriter_Binary tb = null;
	
	public ReleaseSet(ParameterOverride prm_override, ConfigurationOverride cfg_override){
		this.prm_override = prm_override;
		this.cfg_override = cfg_override;
		this.rr = new ReleaseRunner_4D(cfg_override);
	}
	
	public void runSet(){
		
		long reltimer = System.currentTimeMillis();
		if (prm_override.minTimeUnits.equalsIgnoreCase("Date")) {
			System.out.println("Release date "
					+ fullFormat.format(time) + ":");
		} else {
			System.out.println("Release " + (time + 1) + ":");
		}

		String outputFolder = cfg_override.trajOutputDir;
		int bufferSize = cfg_override.bufferSize;
		
		if(outputFolder.equalsIgnoreCase("jobfs")){
			outputFolder = System.getenv("PBS_JOBFS");
		}

		File outputDir = new File(outputFolder + File.separator + prm_override.outputFolder);
		
		if(!outputDir.exists()){
			outputDir.mkdir();
		}
		
		String outputPath = outputFolder + File.separator + prm_override.outputFolder+File.separator+innerFormat.format(new Date(time))+".dat";
		
		System.out.println("Writing to " + outputPath + "...");
		
		try {
			tb = new TrajectoryWriter_Binary(outputPath, bufferSize);
			tb.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		rr.setWriter(tb);
		
		// Try reading the release file (if it can't be found, then
		// quit).

		try {
			if (prm_override.relFileName.endsWith(".shp")) {
				rf_reader = new ReleaseFileReader_Shapefile_4D(prm_override.relFileName);
			} else {
				rf_reader = new ReleaseFileReader_Text(prm_override.relFileName);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Iterate through each release point
		
		while (rf_reader.hasNext()) {

			// Allows for re-starting the code
			
			if (pass && !restartAt.equalsIgnoreCase("#")) {
				if (rf_reader.getLocName().equalsIgnoreCase(restartAt)) {
					pass = false;
				} else {
					rf_reader.next();
					continue;
				}
			}

			Parameters prm = new Parameters_Zonal_4D();
			long timer = System.currentTimeMillis();

			// Set parameters using the current line of the release file
			// as well as the 'global' parameters

			rf_reader.setParameters(prm);
			prm_override.setParameters(prm);

			// Produce a single run.
			// if time units are dates, name folders by date
			String folder = "";
			if (prm_override.minTimeUnits.equalsIgnoreCase("Date")) {
				folder = innerFormat.format(new Date(time));
			}

			// otherwise name according to the time value
			else {
				folder = "T_"
						+ TimeConvert.convertToMillis(prm_override.minTimeUnits,
								time);
			}

			prm.setTime(time);
			prm.setWriteFolder(prm.getOutputFolder() + "/" + folder);
			System.out.print("\t" + prm.getLocName());

			rr.run(prm);

			System.out.println("\tComplete\t("
					+ TimeConvert.millisToString(System
							.currentTimeMillis() - timer)
					+ ")\t"
					+ fullFormat.format(new Date(System
							.currentTimeMillis())));
			rf_reader.next();
			System.gc();
		}
		
		if (prm_override.minTimeUnits.equalsIgnoreCase("Date")) {
			System.out.println("Release date "
					+ fullFormat.format(time)
					+ " complete. ("
					+ TimeConvert.millisToString(System
							.currentTimeMillis() - reltimer) + ")\n");
		} else {
			System.out
					.println("\nRelease "
							+ (time + 1)
							+ " complete. ("
							+ deltaformat.format(((double) System
									.currentTimeMillis() - (double) reltimer) / 1000d)
							+ "s)\n");
		}
		tb.close();
	}
	
	public void close(){
		rr.close();

	}

	public ParameterOverride getPrm_override() {
		return prm_override;
	}

	public void setPrm_override(ParameterOverride prm_override) {
		this.prm_override = prm_override;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public String getRestartAt() {
		return restartAt;
	}

	public void setRestartAt(String restartAt) {
		this.restartAt = restartAt;
	}
}
