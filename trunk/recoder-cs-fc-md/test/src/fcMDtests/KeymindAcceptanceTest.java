package fcMDtests;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import recodercs.ParserException;
import recodercs.io.DefaultSourceFileRepository;
import recodercs.util.FileCollector;
import fcMDtests.util.FileSlurper;

/**
 * This unit test tests the acceptance rate for keymind projects. <br>
 * The test is parametrized. In the method {@link #getTestFiles()} a
 * {@link FileSlurper} to get an array of Files that match a certain regex. <br>
 * The test is run with method {@link #test}.
 * 
 * @author Jan Schumacher, jansch@gmail.com
 * @see #GenericsTest(String name)
 */
@RunWith(value = Parameterized.class)
public class KeymindAcceptanceTest extends ParserTestCase {

	// configuration options
	static String testDir = "/Users/janschumacher/Dropbox/WORK/fc-md/wsp/bcaweb/bcacweb/bcac";
//	static String testDir = "test/NewMonoTests/failingTests";
	String testFile;
	static int testNumber = 0;

	public KeymindAcceptanceTest(String testFile) {
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
	public static LinkedList<String[]> getTestFiles() {
		
		FileCollector col = new FileCollector(testDir);
		ArrayList<String> list = new ArrayList<String>();
		LinkedList<String[]> params = new LinkedList<String[]>();

		while (col.next(DefaultSourceFileRepository.CSHARP_FILENAME_FILTER)) {
			String path;
			try {
				path = col.getFile().getCanonicalPath();
			} catch (IOException ioe) {
				path = col.getFile().getAbsolutePath();
			}
			list.add(path);
		}
		
		for (String f : list) {
			params.add(new String[] { f });
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
			//if (e.toString().contains("Encountered \">\""))
				throw e;
		}

	}

}
