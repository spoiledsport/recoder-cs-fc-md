// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp;

import java.io.*;

import recoder.*;
import recoder.list.*;
import recoder.convenience.TreeWalker;

/**
 Top level implementation of a Java {@link NonTerminalProgramElement}.
 @author AL
 */

public abstract class CSharpNonTerminalProgramElement extends CSharpProgramElement
 implements NonTerminalProgramElement {

    /**
     Java program element.
     */

    public CSharpNonTerminalProgramElement() {}

    /**
     Java program element.
     @param proto a java program element.
     */

    protected CSharpNonTerminalProgramElement(CSharpProgramElement proto) {
        super(proto);
    }

    /**
     Defaults to do nothing.
     */

    public void makeParentRoleValid() {}

    /**
     Defaults to attempt a depth-first traversal using a
     {@link recoder.convenience.TreeWalker}.
     */

    public void makeAllParentRolesValid() {
        TreeWalker tw = new TreeWalker(this);
        while (tw.next(NonTerminalProgramElement.class)) {
            ((NonTerminalProgramElement)tw.getProgramElement()).makeParentRoleValid();
        }
    }

    /**
       Extracts the index of a child from its position code.
       @param positionCode the position code.
       @return the index of the given position code.
       @see NonTerminalProgramElement#getChildPositionCode(ProgramElement)
    */
    public int getIndexOfChild(int positionCode) {
	return positionCode >> 4;
    }

    /**
       Extracts the role of a child from its position code.
       @param positionCode the position code.
       @return the role code of the given position code.
       @see NonTerminalProgramElement#getChildPositionCode(ProgramElement)
    */
    public int getRoleOfChild(int positionCode) {
	return positionCode & 15;
    }
    
    /**
       Returns the index of the given child, or <CODE>-1</CODE> if there is 
       no such child. The child is searched for by identity:
       <CODE>getChildAt(getIndexOfChild(x)) == x</CODE>.
       @param child the exact child to look for.
       @return the index of the given child, or <CODE>-1</CODE>.
    */
    public int getIndexOfChild(ProgramElement child) {
        int i;
        for (i = getChildCount() - 1; i >= 0 && (getChildAt(i) != child); i--);
        return i;
    }
}
