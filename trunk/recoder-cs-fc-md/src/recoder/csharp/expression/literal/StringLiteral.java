// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.literal;

import recoder.*;
import recoder.list.*;
import recoder.csharp.*;
import recoder.csharp.expression.*;
import recoder.csharp.reference.*;

/**
 String literal.
 @author <TT>AutoDoc</TT>
 */

public class StringLiteral extends ReferencePrefixLiteral {

    /**
     The value.
     */

    protected String value;

    /**
     String literal.
     */

    public StringLiteral() {
        setValue("");
    }

    /**
     String literal.
     @param value a string.
     */

    public StringLiteral(String value) {
        setValue(value);
    }

    /**
     String literal.
     @param proto a string literal.
     */

    protected StringLiteral(StringLiteral proto) {
        super(proto);
        value = proto.value;
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new StringLiteral(this);
    }


    /**
     Set value.
     @param str a string value.
     */

    public void setValue(String str) {
        if (!str.startsWith("\"") || !str.endsWith("\"")) {
            throw new IllegalArgumentException("Bad string literal " + str);
        }
        this.value = str.intern();
    }

    /**
     Get value.
     @return the string.
     */

    public String getValue() {
        return value;
    }

    public void accept(SourceVisitor v) {
        v.visitStringLiteral(this);
    }
}
