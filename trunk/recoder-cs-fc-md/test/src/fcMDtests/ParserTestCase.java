package fcMDtests;

import org.junit.Before;

import recodercs.ServiceConfiguration;
import recodercs.csharp.CompilationUnit;
import fcMDtests.util.ParserTestServiceConfiguration;

public abstract class ParserTestCase {
	
	protected String fileName;
	protected CompilationUnit cu;
	protected ServiceConfiguration sc;

	@Before
	public void setUp() throws Exception {
		sc = new ParserTestServiceConfiguration();
	}

}
