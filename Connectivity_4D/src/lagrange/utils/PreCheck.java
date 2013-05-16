package lagrange.utils;

import java.io.File;
import java.io.IOException;

import lagrange.Boundary_Raster;
import lagrange.CollisionDetector;
import lagrange.impl.collision.CollisionDetector_3D_Raster;
import lagrange.impl.readers.Boundary_Raster_NetCDF;
import lagrange.impl.readers.Shapefile;
import lagrange.input.ConfigurationOverride;

public class PreCheck {

	String parameterFile = null;
	String configurationFile = null;

	public static void main(String[] args) {
		PreCheck p = new PreCheck();
		System.out
				.println(p
						.verifyConfiguration("V:/Projects/Modeling/AUS/Input/Configuration/AUS_DIR.cfg"));
	}

	public boolean verifyConfiguration(String configurationFile) {
		boolean pass = true;
		File cfgFile = new File(configurationFile);

		if (!cfgFile.exists()) {
			System.out.println("Configuration file " + cfgFile.getPath()
					+ " does not exist.");
			pass = false;
		}

		ConfigurationOverride cfg = new ConfigurationOverride(cfgFile.getPath());
		System.out.println();
		System.out
				.println("> CONFIGURATION FILE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out
				.println("Velocity Type is specified as: " + cfg.velocityType);
		System.out.println("Variables are specified as: ");
		System.out.println("\tTime variable name = " + cfg.tName);
		System.out.println("\tDepth variable name = " + cfg.kName);
		System.out.println("\tLongitude variable name = " + cfg.lonName);
		System.out.println("\tLatitude variable name = " + cfg.latName);
		System.out.println("\tEast-west velocity variable name = " + cfg.uname);
		System.out.println("\tNorth-south velocity variable name = "
				+ cfg.vname);
		System.out.println("\tVertical velocity variable name = " + cfg.wname);
		System.out.println();

		// Iterate through to get bounds, ensure contiguity, and test that
		// variables are as they should be.

		Shapefile sh;

		System.out
				.println("> SETTLEMENT DATA >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("Settlement area resource location: "
				+ cfg.polyFileName);
		System.out.println("Settlement area ID field name: " + cfg.polyKey);
		System.out
				.println("Settlement area uses a negative coordinate system? "
						+ cfg.negCoord);
		System.out.println();

		Double set_mnx = Double.NaN;
		Double set_mny = Double.NaN;
		Double set_mxx = Double.NaN;
		Double set_mxy = Double.NaN;

		try {
			if (!cfg.polyFileName.isEmpty()) {
				// Set up the settlement habitat
				sh = new Shapefile();
				sh.setDataSource(cfg.polyFileName);
				sh.setLookupField(cfg.polyKey);
				sh.setNegLon(cfg.negCoord);
				int n_patches = sh.getNPatches();
				System.out.println("Number of settlement patches: "
						+ sh.getNPatches());

				if (n_patches == 0) {
					System.out
							.println("*** WARNING:  Number of patches equals 0");
				}

				set_mnx = sh.getMinx();
				set_mny = sh.getMiny();
				set_mxx = sh.getMaxx();
				set_mxy = sh.getMaxy();

				System.out.println();
			}
		} catch (IOException e) {
			System.out.println("Error reading settlement patch file: "
					+ cfg.polyFileName + ".");
			pass = false;
		}

		System.out.println("Settlement patch bounding box is: ");
		System.out.println("Min X = " + set_mnx);
		System.out.println("Min Y = " + set_mny);
		System.out.println("Max X = " + set_mxx);
		System.out.println("Max Y = " + set_mxy);
		System.out.println();

		Boundary_Raster bathymetry;
		CollisionDetector cd;

		try {

			if (!cfg.bathymetryFileName.isEmpty()) {
				bathymetry = new Boundary_Raster_NetCDF(cfg.bathymetryFileName,
						cfg.latName, cfg.lonName);
				cd = new CollisionDetector_3D_Raster(bathymetry);
			}

		} catch (IOException e) {
			System.out.println("\nError reading terrain file: "
					+ cfg.landFileName + ".\n");
			e.printStackTrace();
		}

		File f = new File(cfg.trajOutputDir);
		if (!f.exists()) {
			System.out.println("Trajectory output directory "
					+ cfg.trajOutputDir
					+ " does not exist, and must be created manually.");
			pass = false;
		}

		else if (!f.isDirectory()) {
			System.out.println(cfg.trajOutputDir + " is not a directory");
			pass = false;
		}

		return pass;
	}

}
