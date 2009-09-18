// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.literal;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Null literal.
 @author <TT>AutoDoc</TT>
 */

public class NullLiteral extends Literal {

    /**
     Null literal.
     */

    public NullLiteral() {}

    /**
     Null literal.
     @param proto a null literal.
     */

    protected NullLiteral(NullLiteral proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new NullLiteral(this);
    }

    public void accept(SourceVisitor v) {
        v.visitNullLiteral(this);
    }
}
