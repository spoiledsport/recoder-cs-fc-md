package tests;
import junit.framework.TestCase;
import recoder.*;
import recoder.csharp.*;
import java.io.*;
/**
 * @author kis
 *
 * Abstract class for parser testing.
 */
public abstract class ParserTestCase extends TestCase {
	
	protected String fileName;
	protected CompilationUnit cu;
	protected ServiceConfiguration sc;
	
	public ParserTestCase(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		sc = new ParserTestServiceConfiguration();
	}

}
