// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.statement;

import recoder.*;
import recoder.csharp.*;

/**
   Default implementation for non-terminal Java statements.
   @author <TT>AutoDoc</TT>
 */

public abstract class CSharpStatement
 extends CSharpNonTerminalProgramElement
 implements Statement {

    /**
     Parent.
     */

    protected StatementContainer parent;

    /**
     Java statement.
     */

    public CSharpStatement() {}

    /**
     Java statement.
     @param proto a java statement.
     */

    protected CSharpStatement(CSharpStatement proto) {
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
     Get statement container.
     @return the statement container.
     */

    public StatementContainer getStatementContainer() {
        return parent;
    }

    /**
     Set statement container.
     @param c a statement container.
     */

    public void setStatementContainer(StatementContainer c) {
        parent = c;
    }
}
