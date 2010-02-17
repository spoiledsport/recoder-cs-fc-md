package tests;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.transaction.TransactionRequiredException;

import recodercs.CrossReferenceServiceConfiguration;
import recodercs.csharp.CompilationUnit;
import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.declaration.ClassDeclaration;
import recodercs.kit.Transformation;
import recodercs.util.Debug;
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
