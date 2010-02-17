package tests;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import recodercs.csharp.CompilationUnit;
import recodercs.csharp.PrettyPrinter;

/**
 * @author kis
 *
 * PrettyPrinter used in the PrettyPrinterTest class
 */
public class TestPrinter extends PrettyPrinter {

	/**
	 * Constructor for TestPrinter.
	 * @param out
	 * @param props
	 */
	public TestPrinter(Writer out, Properties props) {
		super(out, props);
	}
	
    public void printFile(CompilationUnit cu) throws IOException {
    	visitCompilationUnit(cu);
    	getWriter().flush();
    }        
	

}
