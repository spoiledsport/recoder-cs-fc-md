// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.literal;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Double literal.
 @author <TT>AutoDoc</TT>
 */

public class DoubleLiteral extends ReferencePrefixLiteral {

    /**
     Textual representation of the value.
     */

    protected String value;

    /**
     Double literal.
     */

    public DoubleLiteral() {
        setValue("0.0");
    }

    /**
     Double literal.
     @param value a double value.
     */

    public DoubleLiteral(double value) {
        setValue("" + value);
    }

    /**
     Double literal.
     @param value a string.
     */

    public DoubleLiteral(String value) {
        setValue(value);
    }

    /**
     Double literal.
     @param proto a double literal.
     */

    protected DoubleLiteral(DoubleLiteral proto) {
        super(proto);
        value = proto.value;
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new DoubleLiteral(this);
    }

    /**
     Set value.
     @param str a string value.
     */

    public void setValue(String str) {
        // unchecked
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
        v.visitDoubleLiteral(this);
    }
}
