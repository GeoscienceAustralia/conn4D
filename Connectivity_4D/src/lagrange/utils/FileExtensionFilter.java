package lagrange.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Helper Filter class - used for filtering out file names having the provided
 * extension.
 */

public class FileExtensionFilter implements FilenameFilter {
	private String extension;

	/**
	 * Constructor accepting string input representing the file extension to include.
	 * 
	 * @param extension
	 */
	
	public FileExtensionFilter(String extension) {
		this.extension = extension;
	}

	/**
	 * identifies whether the provided file has the appropriate extension
	 * 
	 * @param dir - The directory to be searched
	 * @param name - The name of the file to be queried
	 */
	
	@Override
	public boolean accept(File dir, String name) {
		if (name.endsWith(extension))
			return true;
		else
			return (new File(dir, name)).isDirectory();
	}
}