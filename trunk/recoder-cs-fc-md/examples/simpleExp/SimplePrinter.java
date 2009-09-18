package simpleExp;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import recoder.DefaultServiceConfiguration;
import recoder.ParserException;
import recoder.csharp.CompilationUnit;
import recoder.csharp.PrettyPrinter;
import recoder.list.CompilationUnitList;
import recoder.service.SourceInfo;

/**
   @author AL
*/
public class SimplePrinter extends PrettyPrinter {
	

    public static void main(String[] args) throws IOException, ParserException, Exception {
	DefaultServiceConfiguration sc = new DefaultServiceConfiguration();
	        
	File f = new File(args[0]);
	Reader reader = new FileReader(f);	    
	CompilationUnit cu = sc.getProgramFactory().parseCompilationUnit(reader);
	
//	RecoderProgram.setup(sc, SimplePrinter.class, args);
//	CompilationUnitList units = 
//	    sc.getSourceFileRepository().getCompilationUnits();

//	System.getProperties().setProperty(OVERWRITE_INDENTATION,"true");
//	System.getProperties().setProperty(OVERWRITE_PARSE_POSITIONS,"true");
//	System.getProperties().setProperty(INDENTATION_AMOUNT,"4");
	SimplePrinter spr = 
	    new SimplePrinter(sc, new PrintWriter(System.out));
//	spr.printFiles(units);
	spr.printFile(cu);
    }

    private DefaultServiceConfiguration serviceConfiguration;
    private SourceInfo sourceInfo; // cached	

    public SimplePrinter(DefaultServiceConfiguration sc, Writer out) {
	super(out, sc.getProjectSettings().getProperties());
	this.serviceConfiguration = sc;
	sourceInfo = sc.getSourceInfo();
	}				

	
    public void printFiles(CompilationUnitList cus) throws IOException {
		for (int i = 0, s = cus.size(); i < s; i += 1) {
	    	CompilationUnit cu = cus.getCompilationUnit(i);
//	    	String cuname = Naming.toCanonicalName(cu);
	    	System.out.println("Visiting compilation unit...");
			visitCompilationUnit(cu);
			getWriter().flush();
	}
    }
    
    public void printFile(CompilationUnit cu) throws IOException {
    	visitCompilationUnit(cu);
    	getWriter().flush();
    }        
}

