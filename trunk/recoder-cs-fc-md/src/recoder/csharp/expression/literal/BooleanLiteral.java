// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.literal;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Boolean literal.
 @author <TT>AutoDoc</TT>
 */

public class BooleanLiteral extends ReferencePrefixLiteral {

    protected boolean value;

    /**
     Boolean literal.
     */

    public BooleanLiteral() {}

    /**
     Boolean literal.
     @param value a boolean value.
     */

    public BooleanLiteral(boolean value) {
        setValue(value);
    }

    /**
     Boolean literal.
     @param value a string.
     */

    protected BooleanLiteral(String value) {
        if ("true".equals(value)) {
            setValue(true);
        } else if ("false".equals(value)) {
            setValue(false);
        } else {
            throw new IllegalArgumentException("Bad boolean literal " + value);
        }
    }

    /**
     Boolean literal.
     @param proto a boolean literal.
     */

    protected BooleanLiteral(BooleanLiteral proto) {
        super(proto);
        value = proto.value;
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new BooleanLiteral(this);
    }

    /**
     Set value.
     @param b a boolean value.
     */

    public void setValue(boolean b) {
        value = b;
    }

    /**
     Get value.
     @return the string.
     */

    public boolean getValue() {
        return value;
    }

    public void accept(SourceVisitor v) {
        v.visitBooleanLiteral(this);
    }
}
