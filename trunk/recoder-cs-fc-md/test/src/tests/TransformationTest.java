package tests;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.transaction.TransactionRequiredException;

import recoder.CrossReferenceServiceConfiguration;
import recoder.csharp.CompilationUnit;
import recoder.csharp.NonTerminalProgramElement;
import recoder.csharp.declaration.ClassDeclaration;
import recoder.kit.Transformation;
import recoder.util.Debug;
import simpleExp.PlainAnalysisErrorHandler;


/**
 * @author joey
 *
 * A simple transformation on delegates.cs.
 */
public class TransformationTest {

	public static void main(String[] args) {
		try {
			CrossReferenceServiceConfiguration sc =
				new CrossReferenceServiceConfiguration();
			sc.getProjectSettings().setErrorHandler(new PlainAnalysisErrorHandler());
			sc.getChangeHistory().updateModel();
			
			ClassDeclaration cd = (ClassDeclaration) sc.getNameInfo().getClassType(args[0]);
			Debug.asserta(cd);
			
			NonTerminalProgramElement parent = cd;
			
			do {
			 parent = parent.getASTParent();
			} while (parent != null && ! (parent instanceof CompilationUnit));

			Debug.asserta(parent);
			
			CompilationUnit cu = (CompilationUnit) parent;
			
			Transformation trf = new SimpleTransformation(sc, cd);

			trf.execute();

			TestPrinter tp =
				new TestPrinter(
					new OutputStreamWriter(System.err),
					sc.getProjectSettings().getProperties());
			tp.printFile(cu);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
