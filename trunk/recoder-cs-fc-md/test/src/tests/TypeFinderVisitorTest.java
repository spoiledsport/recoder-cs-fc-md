package tests;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import recoder.*;
import recoder.csharp.*;
import recoder.csharp.declaration.TypeDeclaration;
import recoder.io.TypeFinderVisitor;
import recoder.list.TypeDeclarationList;

import java.io.*;
/**
 * @author kis
 *
 * Abstract class for parser testing.
 */
public class TypeFinderVisitorTest extends TestCase {
	
	protected String fileName="testdata/classes.cs";
	protected CompilationUnit cu;
	protected DefaultServiceConfiguration sc;
	protected TypeFinderVisitor tfv;
	
	public TypeFinderVisitorTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		sc = new DefaultServiceConfiguration();
		cu = sc.getProgramFactory().parseCompilationUnit(new FileReader(fileName));
		tfv = new TypeFinderVisitor();
	}
	
	public void testGlobalFind() {
		TypeDeclarationList tdl = tfv.findType("Hello",cu);
		if (tdl.size() != 1) 
			Assert.fail("Couldn't find class Hello");
		if (!tdl.getTypeDeclaration(0).getName().equals("Hello"))
			Assert.fail("Found wrong type"+tdl.getTypeDeclaration(0).toString());	
	}

	public void testNestedFind() {
		TypeDeclarationList tdl = tfv.findType("a.b.Y.Z",cu);
		if (tdl.size() != 1) 
			Assert.fail("Couldn't find class a.b.Y.Z");	
		if (!tdl.getTypeDeclaration(0).getName().equals("Z"))
			Assert.fail("Found wrong type"+tdl.getTypeDeclaration(0).toString());	
							
	}
	
	public void testErrorFind() {
		TypeDeclarationList tdl = tfv.findType("error.test.X",cu);
		if (tdl.size() != 3) 
			Assert.fail("Couldn't find all types called error.test.X");	
	}
	
	public void testNestedErrorFind() {
		TypeDeclarationList tdl = tfv.findType("doubleerror.error.test.X",cu);
		if (tdl.size() != 3) 
			Assert.fail("Couldn't find all types called doubleerror.error.test.X");	
	}
	


	public static Test suite() {
		return new TestSuite(TypeFinderVisitorTest.class);
	}
}
