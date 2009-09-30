package fcMDtests.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

/**
 * This class can return an array of files that match a regular expression for
 * an directory. It is used for the test cases that test parser functionality.
 * No recursion! <br>
 * 
 * @author Jan Schumacher, jansch@gmail.com
 * @see #GenericsTest(String name)
 */
public class FileSlurper {
	private File inputPath;
	private String regex; // e.g. "^gtest-\.cs$"

	/**
	 * @param inputPath
	 *            the path where the FileSlurper should look for files
	 * @param endsWith
	 *            the ending of the files we are interested in
	 */
	public FileSlurper(String inputPath, String endsWith) {
		this.inputPath = new File(inputPath);
		this.regex = endsWith;
		// this.recursive = recursive;
	}

	/**
	 * @return an array of files that math the regex in inputPath
	 */
	public File[] slurp() throws FileNotFoundException {
		File[] children = inputPath.listFiles(filter);

		if (children == null)
			throw new FileNotFoundException("In directory " + inputPath
					+ "are no files matching the filter patter: "
					+ filter.toString());
		else
			return children;
	}

	FilenameFilter filter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.matches(regex);
		}
	};
}
