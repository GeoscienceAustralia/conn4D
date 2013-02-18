package lagrange.utils;
import java.io.File;

/**
 * Displays current working directory, disk roots and directory children. 
 * 
 * @author Johnathan Kool
 *
 */

public class DiskEnvironmentInfo {

	/**
	 * Displays current working directory, disk roots and directory children. 
	 *
	 */
	
	public static void go() {
		System.out.println("Current directory: "
				+ System.getProperty("user.dir"));

		File[] roots = File.listRoots();
		System.out.println("\nRoots: ");
		for (int i = 0; i < roots.length; i++) {
			System.out.println("\t" + roots[i].toString());
		}

		File dir = new File(System.getProperty("user.dir"));

		String[] children = dir.list();
		System.out.println("\nChildren:");
		
		if (children == null) {
		    // Either dir does not exist or is not a directory
		} else {
		    for (String s:children) {
		        // Get filename of file or directory
		        System.out.println("\t" + s);
		    }
		}
	}
}