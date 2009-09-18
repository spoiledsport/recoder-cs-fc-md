// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.statement;

import recoder.*;
import recoder.csharp.*;
import recoder.list.*;

/**
 Branch statement.
 @author AL
 @author <TT>AutoDoc</TT>
 */

public abstract class BranchStatement extends CSharpStatement implements NonTerminalProgramElement {

    /**
     Branch statement.
     */

    public BranchStatement() {}

    /**
     Branch statement.
     @param proto a branch statement.
     */

    protected BranchStatement(BranchStatement proto) {
        super(proto);
    }

    /**
     Get the number of branches in this container.
     @return the number of branches.
     */

    public abstract int getBranchCount();

    /*
      Return the branch at the specified index in this node's
      "virtual" branch array.
      @param index an index for a branch.
      @return the branch with the given index.
      @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
      of bounds.
    */

    public abstract Branch getBranchAt(int index);
}
