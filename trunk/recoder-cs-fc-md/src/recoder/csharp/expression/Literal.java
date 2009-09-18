// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression;

import recoder.*;
import recoder.csharp.*;

/**
 Literal.
 @author <TT>AutoDoc</TT>
 */

public abstract class Literal extends CSharpProgramElement implements Expression, TerminalProgramElement {

    /**
     Expression parent.
     */

    protected ExpressionContainer expressionParent;

    /**
     Literal.
     */

    public Literal() {}

    /**
     Literal.
     @param proto a literal.
     */

    protected Literal(Literal proto) {
        super(proto);
    }

    /**
     Get AST parent.
     @return the non terminal program element.
     */

    public NonTerminalProgramElement getASTParent() {
        return expressionParent;
    }

    /**
     Get expression container.
     @return the expression container.
     */

    public ExpressionContainer getExpressionContainer() {
        return expressionParent;
    }

    /**
     Set expression container.
     @param c an expression container.
     */

    public void setExpressionContainer(ExpressionContainer c) {
        expressionParent = c;
    }
}
