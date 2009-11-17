package tests;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import recoder.DefaultServiceConfiguration;
import recoder.ModelException;
import recoder.csharp.CompilationUnit;
import recoder.io.ProjectSettings;
import recoder.io.TypeFinderVisitor;
import recoder.list.CompilationUnitList;
/**
 * @author kis
 *
 * Abstract class for parser testing.
 */
public class DefaultSourceFileRepositoryTest extends TestCase {
	
	protected String dirName="/Users/janschumacher/Dropbox/WORK/fc-md/wsp/recoder-cs/test/testdata";
	protected CompilationUnit cu;
	protected DefaultServiceConfiguration sc;
	protected TypeFinderVisitor tfv;
	
	public DefaultSourceFileRepositoryTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		System.setProperty(ProjectSettings.INPUT_PATH,dirName);
		sc = new DefaultServiceConfiguration();
		
	}
	
	
	public void testGetCompilationUnits() {
		CompilationUnitList cul = sc.getSourceFileRepository().getCompilationUnits();
		if (cul == null) {
			Assert.fail("No list???");
		}
		System.out.println("number of compilationunits: " + cul.size());
	}
	
	
	public void testGetCompilationUnitSuccess() {
		try {
			CompilationUnit cu = sc.getSourceFileRepository().getCompilationUnit("HAhaha");
		} catch (ModelException ex) {
			Assert.fail("Somehow we found too much CUs...");
		}		
	}

	public void testGetCompilationUnitFailure() {
		try {
			CompilationUnit cu = sc.getSourceFileRepository().getCompilationUnit("X");
		} catch (ModelException ex) {
			return ;
		}		
		Assert.fail("Somehow we found too few CUs...");
	}

	public static Test suite() {
		return new TestSuite(DefaultSourceFileRepositoryTest.class);
	}
}
