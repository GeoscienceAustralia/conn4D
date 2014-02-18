package au.gov.ga.conn4d.impl;

import java.io.File;
import java.io.IOException;

import au.gov.ga.conn4d.Boundary;
import au.gov.ga.conn4d.CollisionDetector;
import au.gov.ga.conn4d.Diffuser;
import au.gov.ga.conn4d.Mortality;
import au.gov.ga.conn4d.Movement;
import au.gov.ga.conn4d.Parameters;
import au.gov.ga.conn4d.Settlement;
import au.gov.ga.conn4d.VelocityReader;
import au.gov.ga.conn4d.VerticalMigration;
import au.gov.ga.conn4d.impl.behavior.Mortality_Exponential;
import au.gov.ga.conn4d.impl.behavior.Mortality_Weibull;
import au.gov.ga.conn4d.impl.behavior.Settlement_FloatOver;
import au.gov.ga.conn4d.impl.behavior.Settlement_Simple;
import au.gov.ga.conn4d.impl.behavior.VerticalSettling_Text;
import au.gov.ga.conn4d.impl.collision.CollisionDetector_3D_Raster;
import au.gov.ga.conn4d.impl.collision.Intersector_2D_Shapefile;
import au.gov.ga.conn4d.impl.movement.Advection_RK4_3D;
import au.gov.ga.conn4d.impl.movement.Diffusion_None;
import au.gov.ga.conn4d.impl.movement.Diffusion_Simple_3D;
import au.gov.ga.conn4d.impl.readers.Boundary_Raster_NetCDF;
import au.gov.ga.conn4d.impl.readers.Shapefile;
import au.gov.ga.conn4d.impl.readers.VelocityReader_HYCOMList_4D;
//import au.gov.ga.conn4d.impl.readers.VelocityReader_InMemHYCOMList_4D;
import au.gov.ga.conn4d.impl.readers.VelocityReader_NetCDFDir_4D;
import au.gov.ga.conn4d.impl.readers.VelocityReader_NetCDF_4D;

import au.gov.ga.conn4d.input.ConfigurationOverride;
import au.gov.ga.conn4d.output.TrajectoryWriter;
import au.gov.ga.conn4d.utils.TimeConvert;
import au.gov.ga.conn4d.utils.VectorUtils;

/**
 * Factory class used to generate individual instances of Release, which are
 * runnable as Threads.
 * 
 * @author Johnathan Kool
 * 
 */

public class ReleaseFactory_4D {

	private ConfigurationOverride lp;
	private VelocityReader vr;
	private TrajectoryWriter tw;
	private Mortality mort;
	private Settlement sm;
	private VerticalMigration vm;
	private Movement mv;
	private Shapefile sh;
	private CollisionDetector cd;
	private Diffuser df;
	private Parameters prm;
	private Boundary bathymetry;
	private long time;
	private long counter = -1;

	/**
	 * No-argument constructor
	 */
	
	public ReleaseFactory_4D() {
	}

	/**
	 * Constructor accepting a String containing the path of
	 * a configuration text file.
	 * 
	 * @param local_config
	 */
	
	public ReleaseFactory_4D(ConfigurationOverride local_config) {
		initialize(local_config);
	}

	/**
	 * Generates individual instances of Releases using factory settings.
	 * 
	 * @return an individual Release instance
	 */

	public Release generate() {

		Release rel = new Release();

		rel.setNegativeCoordinates(lp.negCoord);
		rel.setNegativeOceanCoordinates(lp.negOceanCoord);
		rel.setTrajectoryWriter(tw);
		rel.setParameters(prm);
		rel.setTime(time);
		rel.setMortality(mort.clone());

		if (rel.preKill()) {
			return rel;
		}

		counter++;
		rel.setId(counter);
		rel.setMovement(mv.clone());
		rel.setDiffusion(df.clone());
		
		if (cd != null) {
			rel.setCollisionDetector(cd.clone());
		}

		if (sm != null) {
			Settlement stmp = sm.clone();
			stmp.setIntersector(new Intersector_2D_Shapefile(sh));
			rel.setSettlement(stmp);
		}

		if (vm != null) {
			rel.setVerticalMigration(vm.clone());
		}

		return rel;
	}

	/**
	 * Initializes the ReleaseFactory object by setting values using the values
	 * in the LocalParameters object
	 * 
	 * @param local_config
	 *            - String pathname of the LocalParameters file/object.
	 */

	private void initialize(ConfigurationOverride local_config) {
		lp = local_config;
		boolean err = false;
		
		// Initialize the habitat class.

		try {
			if (!lp.polyFileName.isEmpty()) {
				// Set up the settlement habitat
				sh = new Shapefile();
				sh.setDataSource(lp.polyFileName);
				sh.setLookupField(lp.polyKey);
				sh.setNegLon(lp.negCoord);
			}
		} catch (IOException e) {
			System.out.println("Error reading settlement file: "
					+ lp.polyFileName + ".");
			e.printStackTrace();
			err = true;
		}

		try {

			if (!lp.bathymetryFileName.isEmpty()) {
				// Set up the land mask////////////////////////////
				bathymetry = new Boundary_Raster_NetCDF(lp.bathymetryFileName,
						lp.latName, lp.lonName);
				cd = new CollisionDetector_3D_Raster(bathymetry);
			}

		} catch (IOException e) {
			System.out.println("\nError reading terrain file: "
					+ lp.landFileName + ".\n");
			e.printStackTrace();
			err = true;
		}

		if (err) {
			System.out
					.println("\nErrors occurred during initialization.  Exiting.");
			shutdown();
			System.exit(-1);
		}

		System.out.println("\nInitialization successful.");
	}

	/**
	 * Sets the mutable parameters of the ReleaseFactory. Mutable items are
	 * contained in Parameters. Parameters is a composition of elements that are
	 * consistent across all populations and some that are population specific.
	 * 
	 * @param prm
	 *            - Parameters object
	 */

	public void setParameters(Parameters prm) {

		this.prm = prm;
		counter = 0;
		
		//File tod = new File (lp.trajOutputDir);
		
		//if(!tod.isDirectory()){
		//	tod.mkdirs();
		//}
		
		//String output = lp.trajOutputDir + File.separatorChar
		//		+ prm.getWriteFolder() + ".dat";
		
		//tw = new TrajectoryWriter_Binary(output);
		
		// Set the output writers

		//tw = new TrajectoryWriter_Text(output + prm.getLocName() + ".txt");
		//tw.setNegCoord(!lp.negOceanCoord && lp.negCoord);
		//mw = new MatrixWriter_Text(output + prm.getLocName() + ".sum");
		//dw = new DistanceWriter_Text(output + prm.getLocName() + ".dst");

		// Set mortality

		if (prm.getMortalityType().equalsIgnoreCase("None")) {
			prm.setMortalityRate(0);
		}
		if (prm.getMortalityType().equalsIgnoreCase("Weibull")) {
			mort = new Mortality_Weibull(prm.getMortalityParameters()[0],
					prm.getMortalityParameters()[1]);
		} else {
			mort = new Mortality_Exponential(prm.getMortalityRate());
		}

		mort.setTimeInterval(prm.getH());

		// Set settlement

		if (prm.getSettlementType().equalsIgnoreCase("NONE")) {
		} else if (prm.getSettlementType().equalsIgnoreCase("FloatOver")) {
			Settlement_FloatOver sfo = new Settlement_FloatOver();
			sfo.setSettlementPolys(sh);
			sm = sfo;

		} else {
			Settlement_Simple ssm = new Settlement_Simple();
			ssm.setSettlementPolys(sh);
			sm = ssm;
		}

		// Set the velocity reader

		// If we're using climatology, follow these steps.

		if (lp.velocityType.isEmpty()
				|| lp.velocityType.equalsIgnoreCase("NONE")) {
			System.out.println("WARNING:  Velocity Type is set as NONE");
		} else if (lp.velocityType.equalsIgnoreCase("IANN")) {
			VelocityReader_NetCDF_4D nvr = new VelocityReader_NetCDF_4D();
			try {
				nvr.setUFile(lp.ufile, lp.uname);
				nvr.setVFile(lp.vfile, lp.vname);
				nvr.setWFile(lp.wfile, lp.wname);
				nvr.setXLookup(lp.latName);
				nvr.setYLookup(lp.lonName);
				nvr.setZLookup(lp.kName);
				nvr.setTLookup(lp.tName);
			} catch (IOException e) {
				e.printStackTrace();
			}

			nvr.setTimeOffset(TimeConvert.convertToMillis(lp.timeOffsetUnits,
					lp.timeOffset));

			nvr.setLonName(lp.lonName);
			nvr.setLatName(lp.latName);
			nvr.setKName(lp.kName);
			nvr.setTName(lp.tName);

			nvr.setXLookup(lp.latName);
			nvr.setYLookup(lp.lonName);
			nvr.setZLookup(lp.kName);
			nvr.setTLookup(lp.tName);

			// nvr.setNegOceanCoord(lp.negOceanCoord);
			// nvr.setNegPolyCoord(lp.negPolyCoord);

			vr = nvr;

			// Otherwise, it's not supported (a new reader will have to be
			// coded and added here as a choice)

		} else if (lp.velocityType.equalsIgnoreCase("IANN_DIR")) {
			VelocityReader_NetCDFDir_4D ndr = null;
			try {
				ndr = new VelocityReader_NetCDFDir_4D();
				ndr.setLonName(lp.lonName);
				ndr.setLatName(lp.latName);
				// ndr.setNegOceanCoord(lp.negOceanCoord);
				// ndr.setNegPolyCoord(lp.negCoord);
				ndr.setUName(lp.uname);
				ndr.setVName(lp.vname);
				ndr.setWName(lp.wname); // uVar, vVar and wVar need to be
										// generated somewhere here. But they
										// depend on time.
				ndr.setZName(lp.kName); // Time is normally checked, but null is
										// not acceptable for cloning or as an
				ndr.setTName(lp.tName); // initial start.
				ndr.initialize(lp.veldir);
				ndr.setXLookup(lp.latName);
				ndr.setYLookup(lp.lonName);
				ndr.setZLookup(lp.kName);
				ndr.setTLookup(lp.tName);

			} catch (IOException e) {
				e.printStackTrace();
			}

			vr = ndr;

			// Otherwise, it's not supported (a new reader will have to be
			// coded and added here as a choice)

		} else if (lp.velocityType.equalsIgnoreCase("HYCOM_LIST")) {
			//VelocityReader_InMemHYCOMList_4D ndr = null;
			VelocityReader_HYCOMList_4D ndr = null;
			try {
				//ndr = new VelocityReader_InMemHYCOMList_4D();
				ndr = new VelocityReader_HYCOMList_4D();
				ndr.setLonName(lp.lonName);
				ndr.setLatName(lp.latName);
				ndr.setUName(lp.uname);
				ndr.setVName(lp.vname);
				ndr.setWName(lp.wname);
				ndr.setZName(lp.kName);
				ndr.setTName(lp.tName); 
				ndr.initialize(lp.veldir);
				ndr.setXLookup(lp.latName);
				ndr.setYLookup(lp.lonName);
				ndr.setZLookup(lp.kName);
				ndr.setTLookup(lp.tName);

			} catch (IOException e) {
				e.printStackTrace();
			}

			vr = ndr;

			// Otherwise, it's not supported (a new reader will have to be
			// coded and added here as a choice)

		} else {
			throw new UnsupportedOperationException("Velocity Type provided ("
					+ lp.velocityType + ") is not supported.");
		}

		// Initialize the Runge-Kutta engine

		Advection_RK4_3D rk4 = new Advection_RK4_3D();
		rk4.setVr(vr);
		rk4.setH(prm.getH());
		mv = rk4;

		// Initialize the Turbulence engine
		
		if(prm.getDiffusionType().equalsIgnoreCase("NONE")){
			df = new Diffusion_None();
		}
		
		else{
			df = new Diffusion_Simple_3D(prm.getH());
		}
		
		// Initialize vertical migration, if required.

		if (prm.usesVerticalMigration()) {
			VerticalSettling_Text tvm = new VerticalSettling_Text();
			tvm.setVmtx(VectorUtils.loadASCIIMatrix(new File(lp.vertFile)));
			tvm.setBathymetry(lp.bathymetryFileName);

			vm = tvm;
		}
	}

	/**
	 * Retrieves the CollisionDetection object associated with this instance.
	 * 
	 * @return - The CollisionDetection object
	 */

	public CollisionDetector getCollisionDetection() {
		return cd;
	}

	/**
	 * Retrieves the Diffusion object associated with this instance.
	 * 
	 * @return - The Diffusion object
	 */

	public Diffuser getDiffusion() {
		return df;
	}

	/**
	 * Retrieves the LocalParameters object associated with this instance.
	 * 
	 * @return - The LocalParameters object
	 */

	public ConfigurationOverride getLocalParameters() {
		return lp;
	}

	/**
	 * Retrieves the Mortality object associated with this instance.
	 * 
	 * @return - The Mortality object
	 */

	public Mortality getMortality() {
		return mort;
	}

	/**
	 * Retrieves the Movement object associated with this instance.
	 * 
	 * @return - The Movement object
	 */

	public Movement getMovement() {
		return mv;
	}

	/**
	 * Retrieves the Parameters object associated with this instance.
	 * 
	 * @return - The Parameters object
	 */

	public Parameters getParameters() {
		return prm;
	}

	/**
	 * Retrieves the Settlement object associated with this instance.
	 * 
	 * @return - The Settlement object
	 */

	public Settlement getSettlement() {
		return sm;
	}

	/**
	 * Retrieves the current time stamp (in milliseconds) associated with this
	 * instance
	 * 
	 * @return long - the time stamp (in milliseconds) associated with this
	 *         instance.
	 */

	public long getTime() {
		return time;
	}

	/**
	 * Retrieves the TrajectoryWriter object associated with this instance.
	 * 
	 * @return - The TrajectoryWriter object
	 */

	public TrajectoryWriter getTrajectoryWriter() {
		return tw;
	}

	/**
	 * Retrieves the VelocityReader object associated with this instance.
	 * 
	 * @return - The VelocityReader object
	 */

	public VelocityReader getVelocityReader() {
		return vr;
	}

	/**
	 * Retrieves the VerticalMigration object associated with this instance.
	 * 
	 * @return - The VerticalMigration object
	 */

	public VerticalMigration getVerticalMigration() {
		return vm;
	}

	/**
	 * Sets the CollisionDetection object for this instance.
	 * 
	 * @param cd
	 *            - The CollisionDetection object
	 */

	public void setCollisionDetection(CollisionDetector cd) {
		this.cd = cd;
	}

	/**
	 * Sets the Diffusion object for this instance.
	 * 
	 * @param df
	 *            - The Diffusion object
	 */

	public void setDiffusion(Diffuser df) {
		this.df = df;
	}

	/**
	 * Sets the LocalParameters object for this instance.
	 * 
	 * @param lp
	 *            - The LocalParameters object
	 */

	public void setLocalParameters(ConfigurationOverride lp) {
		this.lp = lp;
	}

	/**
	 * Sets the Mortality object for this instance.
	 * 
	 * @param mort
	 *            - The Mortality object
	 */

	public void setMortality(Mortality mort) {
		this.mort = mort;
	}

	/**
	 * Sets the Movement object for this instance.
	 * 
	 * @param mv
	 *            - The Movement object
	 */

	public void setMovement(Movement mv) {
		this.mv = mv;
	}

	/**
	 * Sets the Parameters object for this instance.
	 * 
	 * @param prm
	 *            - The Parameters object
	 */

	public void setPrm(Parameters prm) {
		this.prm = prm;
	}

	/**
	 * Sets the Settlement object for this instance.
	 * 
	 * @param sm
	 *            - The Settlement object
	 */

	public void setSettlement(Settlement sm) {
		this.sm = sm;
	}

	/**
	 * Sets the timestamp (in milliseconds) for this instance.
	 * 
	 * @param time
	 *            (long) - The timestamp (in milliseconds)
	 */

	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Sets the TrajectoryWriter object for this instance.
	 * 
	 * @param tw
	 *            - The TrajectoryWriter object
	 */

	public void setTrajectoryWriter(TrajectoryWriter tw) {
		this.tw = tw;
	}

	/**
	 * Sets the VelocityReader object for this instance.
	 * 
	 * @param vr
	 *            - The VelocityReader object
	 */

	public void setVelocityReader(VelocityReader vr) {
		this.vr = vr;
	}

	/**
	 * Sets the VerticalMigration object for this instance.
	 * 
	 * @param vm
	 *            - The VerticalMigration object
	 */

	public void setVerticalMigration(VerticalMigration vm) {
		this.vm = vm;
	}

	/**
	 * Closes Writer objects.
	 */

	public void shutdown() {
		if (mv != null) {
			mv.close();
		}
	}
}