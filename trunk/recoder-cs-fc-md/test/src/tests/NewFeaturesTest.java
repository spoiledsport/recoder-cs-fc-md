package tests;

import java.io.FileReader;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;
import recoder.ParserException;

public class NewFeaturesTest extends ParserTestCase {
	
	final String testDir = "personExp";

	public NewFeaturesTest(String name) {
		super(name);
		System.getProperties().put("input.path",
		"test/personExp:test/minicorlib");
	}

	private void newFeaturesTest(String fileName) throws IOException,
			ParserException {
		cu = sc.getProgramFactory().parseCompilationUnit(
				new FileReader(fileName));
	}

	public static Test suite() {
		return new TestSuite(NewFeaturesTest.class);
	}
	
	public void test() throws ParserException, IOException {
		newFeaturesTest("test/" + testDir + "/" + "Child.cs");
	}

}
