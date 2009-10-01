package fcMDtests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import recoder.ParserException;
import fcMDtests.util.FileSlurper;

/**
 * This unit test tests the generics related features of the parser. <br>
 * The test is parametrized. In the method {@link #getTestFiles()} a
 * {@link FileSlurper} to get an array of Files that match a certain regex. <br>
 * The test is run with method {@link #test}.
 * 
 * @author Jan Schumacher, jansch@gmail.com
 * @see #GenericsTest(String name)
 */
@RunWith(value = Parameterized.class)
public class PartialClassesTest extends ParserTestCase {

	// configuration options
	static String testDir = "test" + System.getProperty("file.separator")
			+ "NewMonoTests" + System.getProperty("file.separator")
			+ "partial";
	static String regex = ".*cs$";;
	String testFile;
	static int testNumber = 0;

	public PartialClassesTest(String testFile) {
		this.testFile = testFile;
	}

	/**
	 * This method parses the file that is currently beeing tested.
	 * 
	 * @param fileName
	 *            the file to be prased
	 */
	private void parse(String fileName) throws IOException, ParserException {
		cu = sc.getProgramFactory().parseCompilationUnit(
				new FileReader(fileName));
	}

	/**
	 * The method that supplies the files for the parametrized test case
	 * {@link #test()}. It uses a {@link FileSlurper} to fill the field testFile
	 * with the file to be tested.
	 */
	@Parameters
	public static LinkedList getTestFiles() {
		File[] testFiles = null;
		LinkedList params = new LinkedList();
		try {
			// get list of files matching regEx
			testFiles = new FileSlurper(testDir, regex).slurp();
			for (File f : testFiles) {
				// add files to the list used for the parametrized test
				params.add(new String[] { f.getPath() });
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return params;
	}

	/**
	 * The paremetrized test case itself. It is data driven and gets the files
	 * to parse from {@link #getTestFiles()}.
	 * 
	 * @throws ParserException
	 */
	@Test
	public void test() throws IOException, ParserException {
		System.out.println("PARSING file #" + testNumber++ + ":\n" + testFile);
		try {
			parse(testFile);
		} catch (ParserException e) {
			System.err.println("parser exception!");
			throw e;
		}

	}

}
