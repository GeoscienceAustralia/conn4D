package lagrange.utils;

import java.io.File;

import lagrange.input.ConfigurationOverride;

public class PreCheck {

	String parameterFile = null;
	String configurationFile = null;
	
	public boolean verifyConfiguration(String configurationFile){
		boolean pass = true;
		File cfg = new File(configurationFile);
		
		if(!cfg.exists()){
			System.out.println("Configuration file " + cfg.getPath() + " does not exist.");
			pass = false;
		}
		
		ConfigurationOverride co = new ConfigurationOverride(cfg.getPath());
		System.out.println("Velocity Type is specified as: " + co.velocityType);
		System.out.println("Variables are specified as: ");
		System.out.println("\tTime variable name = " + co.tName);
		System.out.println("\tDepth variable name = " + co.kName);
		System.out.println("\tLongitude variable name = " + co.lonName);
		System.out.println("\tLatitude variable name = " + co.latName);
		
		
		
		


		
		
		
		return pass;
	}
	
}
