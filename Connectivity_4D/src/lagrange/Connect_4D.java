package lagrange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import lagrange.impl.ReleaseRunner_4D;
import lagrange.impl.readers.ReleaseFileReader_Shapefile;
import lagrange.impl.readers.ReleaseFileReader_Text;
import lagrange.input.GlobalParameters;
import lagrange.input.ReleaseFileReader;
import lagrange.parameters.Parameters_Zonal;
import lagrange.utils.TimeConvert;

/**
 * Lagrangian particle tracking program.
 */

public class Connect_4D {
	private static GlobalParameters gp = new GlobalParameters();
	private static ReleaseFileReader rf;
	private static ReleaseRunner_4D rr;
	private static String prmfile = "default.prm";
	private static String cfgfile = "default.cfg";
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private static String restartAt = "#";
	private static boolean pass = true;

	/**
	 * 2-argument constructor
	 * 
	 * @param prmfile - String representing the path to the 'global' model parameters
	 * @param cfgfile - String representing the path to the 'local' machine-dependent parameters
	 */
	
	public Connect_4D(String prmfile, String cfgfile){

		gp.readFile(prmfile);
		TimeZone.setDefault(TimeZone.getTimeZone(gp.timezone));

		// Initialize the release runner using the local configuration.

		rr = new ReleaseRunner_4D(cfgfile);
	}
	
	public void run(){
		
		long outertimer = System.currentTimeMillis();
		System.out.println("\nSimulation started " + new Date(outertimer)+"\n");
		
		try {
			
			long start = TimeConvert.convertToMillis(gp.minTimeUnits, gp.minTime);
			long end = TimeConvert.convertToMillis(gp.maxTimeUnits, gp.maxTime);
			long relsp = gp.relSp.equalsIgnoreCase("-1") ? Long.MAX_VALUE:TimeConvert.convertToMillis(gp.relSpUnits, gp.relSp);
			
			//'Hard' end (simulation terminates such that releases stop
			// in advance of end date
			
			// for (long time = start; time < end-relDuration; time += relsp) {
			
			//'Soft' end (simulation carries past end date until release duration is complete)
			
			for (long time = start; time < end; time += relsp) {
				
				long reltimer = System.currentTimeMillis();
				if(gp.minTimeUnits.equalsIgnoreCase("Date")){
					System.out.println("Release date " + df.format(time) + ":");	
				}
				else{System.out.println("Release " + (time+1) + ":");}			

				// Try reading the release file (if it can't be found, then quit).

					if(gp.relFileName.endsWith(".shp")){
						rf = new ReleaseFileReader_Shapefile(gp.relFileName);
					}
					else{
						rf = new ReleaseFileReader_Text(gp.relFileName);
					}
				
				// Allows for re-starting the code	
					
				while (rf.hasNext()) {
					
					if(pass && !restartAt.equalsIgnoreCase("#")){
						if(rf.getLocName().equalsIgnoreCase(restartAt)){;
							 pass = false;
						}
						else{
							rf.next();
							continue;
						}
					}
					
					Parameters prm = new Parameters_Zonal();
					//Parameters prm = new Parameters_Zonal_Ind();
					long timer = System.currentTimeMillis();

					// Set parameters using the current line of the release file
					// as well as the 'global' parameters

					rf.setParameters(prm);
					gp.setParameters(prm);

					// Produce a single run.
					// if time units are dates, name folders by date
					String folder="";
					if(gp.minTimeUnits.equalsIgnoreCase("Date")){
						folder = df.format(new Date(time));	
					}
					
					// otherwise name according to the time value
					else{
						folder = "T_" + TimeConvert.convertToMillis(gp.minTimeUnits, time);
					}
					
					prm.setTime(time);
					prm.setWriteFolder(prm.getOutputFolder() + "/" + folder);
					System.out.print("\t"  + prm.getLocName());
					
					rr.run(prm);

					System.out.println("\tComplete\t("
							+ TimeConvert.millisToString(System.currentTimeMillis()
									- timer) + ")\t" + new Date(System.currentTimeMillis()));
					rf.next();
					System.gc();
				}
				
				if(pass && !restartAt.equalsIgnoreCase("#")){
					System.out.println("\n" + restartAt + " was set as the restart target, but was not found (must be an exact match).  Exiting.");
					break;
				}
				
				if(gp.relSpUnits.equalsIgnoreCase("Date")){
					System.out.println("\nRelease date " + df.format(time) + "complete. (" +TimeConvert.millisToString(System.currentTimeMillis()-reltimer)+ ")");	
				}
				else{System.out.println("\nRelease " + (time+1) + " complete. (" +TimeConvert.millisToString(System.currentTimeMillis()-reltimer)+ ")");}	
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		rr.close();

		System.out.println("\nTime finished: "
				+ new Date(System.currentTimeMillis())
				+ " ("
				+ TimeConvert.millisToString(System.currentTimeMillis()
						- outertimer) + ")");

		System.exit(0);
	}
	
	/**
	 * Main class
	 * 
	 * @param args
	 * @throws Exception
	 */
	
	public static void main(String args[]) throws Exception {
		if (args.length > 0) {
			prmfile = args[0];
		} else {
			if (prmfile == null) {
				System.out
						.println("Usage: java -jar JB4.jar <parameter file> <configuration file>");
				System.exit(-1);
			}
		}
		
		if (args.length > 1) {
			cfgfile = args[1];
		} else {
			System.out
					.println("Configuration file not provided.  Using default configuration.");
		}
		
		if (args.length > 2){
			restartAt = args[2];
		}
		
		Connect_4D connect = new Connect_4D(prmfile,cfgfile);
		connect.run();
	}
}
