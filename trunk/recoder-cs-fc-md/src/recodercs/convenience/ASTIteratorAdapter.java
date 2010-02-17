// This file is part of the RECODER library and protected by the LGPL.

package recodercs.convenience;


import recodercs.convenience.ASTIterator;
import recodercs.convenience.ASTIteratorListener;
import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.ProgramElement;


/** the "default" implementation for iterator listeners. This class may serve
    as a base class for derived specialized versions.
   @author RN
*/
public class ASTIteratorAdapter implements ASTIteratorListener {

    public void enteringNode(ASTIterator it,
				ProgramElement node) {
    }

    public void leavingNode(ASTIterator it,
			    ProgramElement node) {
    }

    public int enterChildren(ASTIterator it,
			     NonTerminalProgramElement thisNode) {
	return ENTER_ALL;
    }

    public boolean enterChildNode(ASTIterator it,
				  NonTerminalProgramElement thisNode,
				  ProgramElement childNode) {
	return true;
    }

    public void returnedFromChildNode(ASTIterator it,
				      NonTerminalProgramElement thisNode,
				      ProgramElement childNode) {
    }


}
