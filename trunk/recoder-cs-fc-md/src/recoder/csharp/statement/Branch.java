// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.statement;

import recoder.*;
import recoder.list.*;
import recoder.csharp.*;

/**
 Branch.
 @author <TT>AutoDoc</TT>
 */

public abstract class Branch extends CSharpNonTerminalProgramElement
 implements StatementContainer {

    /**
     Parent.
     */

    protected BranchStatement parent;

    /**
     Branch.
     */

    public Branch() {}

    /**
     Branch.
     @param proto a branch.
     */

    protected Branch(Branch proto) {
        super(proto);
    }

    /**
     Get AST parent.
     @return the non terminal program element.
     */

    public NonTerminalProgramElement getASTParent() {
        return parent;
    }

    /**
     Get parent.
     @return the branch statement.
     */

    public BranchStatement getParent() {
        return parent;
    }
}
