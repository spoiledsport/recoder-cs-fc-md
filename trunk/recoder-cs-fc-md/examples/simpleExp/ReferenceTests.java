package simpleExp;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

import recoder.CrossReferenceServiceConfiguration;
import recoder.DefaultServiceConfiguration;
import recoder.ParserException;
import recoder.abstraction.ClassType;
import recoder.abstraction.Field;
import recoder.abstraction.Method;
import recoder.abstraction.ProgramModelElement;
import recoder.convenience.Naming;
import recoder.convenience.TreeWalker;
import recoder.csharp.CSharpSourceElement;
import recoder.csharp.CompilationUnit;
import recoder.csharp.PrettyPrinter;
import recoder.csharp.ProgramElement;
import recoder.csharp.declaration.TypeDeclaration;
import recoder.csharp.reference.FieldReference;
import recoder.csharp.reference.MethodReference;
import recoder.list.CompilationUnitList;
import recoder.list.MethodList;
import recoder.list.TypeDeclarationList;
import recoder.service.CrossReferenceSourceInfo;
import recoder.service.DefaultCrossReferenceSourceInfo;
import recoder.service.SourceInfo;

/**
 * @author AL
 */
public class ReferenceTests extends PrettyPrinter {



	protected ReferenceTests(Writer out, Properties props) {
		super(out, props);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException, ParserException,
			Exception {

		System
				.getProperties()
				.put("input.path",
						"/Users/janschumacher/Dropbox/WORK/fc-md/cSharp/cars/cars:test/minicorlib");

		CrossReferenceServiceConfiguration cs = new CrossReferenceServiceConfiguration();
		CrossReferenceSourceInfo si = cs.getCrossReferenceSourceInfo();


		RecoderProgram.setup(cs, SimplePrinter.class, args);
		CompilationUnitList units = cs.getSourceFileRepository()
				.getCompilationUnits();

		System.out.println("no cus: " + units.size());

		// loop over all cus
		for (int i = 0, s = units.size(); i < s; i += 1) {
			CompilationUnit cu = units.getCompilationUnit(i);
			System.out.println("Visiting compilation unit number: " + i
					+ " named: " + cu.getName());
			System.out.println();
			
			// tree walker to walk the AST of cu
			TreeWalker johnnieWalker = new TreeWalker(cu);
			
			// we want to get class type of current cu
			// first we need to get all types of cu
			TypeDeclarationList typeDecs = cu.getDeclarations();
			System.out.println("number of typeDecs: " + typeDecs.size());
			//cu.get
			
			
			
			//MethodList ml = si.getAllMethods(cu.);
			
			while (johnnieWalker.next()) {
				ProgramElement e = johnnieWalker.getProgramElement();
				
				if (e instanceof FieldReference) {
					FieldReference fr = (FieldReference) e;
					System.out.println(fr.getName());
					Field mf = si.getField(fr);
					
					System.out.println("type of reference: " + mf.getContainingClassType().getName());
					//System.out.println("found fieldRef: " + e.toSource());
				} else if (e instanceof MethodReference) {
					System.out.println("found methRef: " + e.toSource());
					System.out.println("method is private? " + si.getMethod((MethodReference) e).isPrivate());
					System.out.println("method is public? " + si.getMethod((MethodReference) e).isPublic());
					System.out.println("method is static? " + si.getMethod((MethodReference) e).isStatic());
					// this does not work
					ProgramElement parent = e.getASTParent();
					System.out.println("this method belongs to class: " + e.toSource());
				} else if (e instanceof ClassType) {
					System.out.println("found class: " + e.toSource());
				} else if (e instanceof Method) {
					System.out.println("found method: " + e.toSource());
					int loc = 0;
					// start with the line of the method head
					recoder.csharp.SourceElement.Position start= ((CSharpSourceElement) e).getStartPosition();			
					// end with the last curly brace of method.
					recoder.csharp.SourceElement.Position end= ((CSharpSourceElement) e).getEndPosition();	
					
					loc = (end.getLine()- start.getLine())+1;
					System.out.println("loc: " + loc);
				}
			}
			
//			// TODO: not getting typedeclerations
//			TypeDeclarationList typeDecs2 = cu.getDeclarations();
//			System.out.println("number of typedecs: " + cu.getTypeDeclarationCount());
//			// smell detection
//			for (int j = 0, t = typeDecs.size(); j < t; i++) {
//				TypeDeclaration td = typeDecs2.getTypeDeclaration(i);
//
//				
//				TreeWalker walker = new TreeWalker(typeDecs
//						.getProgramElement(j));
//
//				while (walker.next()) {
//					ProgramElement e = walker.getProgramElement();
//					
//					if (e instanceof FieldReference) {
//						System.out.println("we have a fieldreference: \n" + e.toSource());
//					} else if(e instanceof recoder.csharp.reference.MethodReference) {
//						System.out.println("we have a methodreference: \n" + e.toSource());
//					}
//
//				}
//			}
		}
	}


}
