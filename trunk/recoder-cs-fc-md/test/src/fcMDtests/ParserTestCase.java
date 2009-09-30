package fcMDtests;

import org.junit.Before;

import fcMDtests.util.ParserTestServiceConfiguration;

import recoder.ServiceConfiguration;
import recoder.csharp.CompilationUnit;

public abstract class ParserTestCase {
	
	protected String fileName;
	protected CompilationUnit cu;
	protected ServiceConfiguration sc;

	@Before
	public void setUp() throws Exception {
		sc = new ParserTestServiceConfiguration();
	}

}
