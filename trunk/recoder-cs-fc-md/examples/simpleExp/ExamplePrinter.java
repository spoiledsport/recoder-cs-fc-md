package simpleExp;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import recoder.DefaultServiceConfiguration;
import recoder.ParserException;
import recoder.convenience.Naming;
import recoder.csharp.CompilationUnit;
import recoder.csharp.PrettyPrinter;
import recoder.list.CompilationUnitList;
import recoder.service.SourceInfo;

/**
 * @author kis
 */
public class ExamplePrinter extends PrettyPrinter {

	public static void main(String[] args)

	throws IOException, ParserException, Exception {

		System.getProperties().put("input.path",".");
		DefaultServiceConfiguration sc = new DefaultServiceConfiguration();

		ExamplePrinter epr = new ExamplePrinter(sc, new PrintWriter(System.out));
		CompilationUnitList list = sc.getSourceFileRepository()
				.getCompilationUnits();
		System.out.println("we have: " + list.size());
		epr.printCompilationUnits(list);
	}

	private DefaultServiceConfiguration serviceConfiguration;
	private SourceInfo sourceInfo; // cached

	public ExamplePrinter(DefaultServiceConfiguration sc, Writer out) {
		super(out, sc.getProjectSettings().getProperties());
		this.serviceConfiguration = sc;
		sourceInfo = sc.getSourceInfo();
	}

	public void printCompilationUnits(CompilationUnitList cus)
			throws IOException {
		for (int i = 0, s = cus.size(); i < s; i += 1) {
			CompilationUnit cu = cus.getCompilationUnit(i);
			printCompilationUnit(cu);
		}
	}

	public void printCompilationUnit(CompilationUnit cu) throws IOException {
		String name = cu.getDataLocation().toString();
		System.out.println("Visiting compilation unit:" + name);
		visitCompilationUnit(cu);
		getWriter().flush();
	}
}
