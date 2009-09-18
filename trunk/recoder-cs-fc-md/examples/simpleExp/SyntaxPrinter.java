package simpleExp;
import java.io.*;

import recoder.*;
import recoder.csharp.*;
import recoder.convenience.*;

/**
 * @author orosz
 * A simple class to print the AST.
 */
public class SyntaxPrinter {
	
	
	final static String indentStr = "   ";
	
	public static void main(String[] args) throws Exception {
		
      DefaultServiceConfiguration sc = new DefaultServiceConfiguration();
	        
      Reader reader = new FileReader(args[0]);	    
	  CompilationUnit cu = sc.getProgramFactory().parseCompilationUnit(reader);

	  

	  LeveledTreeWalker johnnieWalker = new LeveledTreeWalker(cu);
	  while (johnnieWalker.next()) {
	  	ProgramElement pe = johnnieWalker.getProgramElement();
	  	
	  	StringBuffer indent = new StringBuffer();
	  	for (int i = 0 ; i<johnnieWalker.getCurrentLevel() ; i++) {
	  		indent.append(indentStr);
	  	}
	  	System.out.print(indent);
	  	System.out.print(pe.getClass().getName());
	  	if (pe instanceof NamedModelElement) 
	  	  System.out.print(" - "+((NamedModelElement)pe).getName());
	  	System.out.print(" Pos:"+pe.getStartPosition().toString()+"->"+pe.getEndPosition().toString());
	  	System.out.println();
	  }

	    
	  
	}
	

}
