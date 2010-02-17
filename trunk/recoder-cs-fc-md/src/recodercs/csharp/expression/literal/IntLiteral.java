// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.literal;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Int literal.
 @author <TT>AutoDoc</TT>
 */

public class IntLiteral extends ReferencePrefixLiteral {

    /**
     Textual representation of the value.
     */

    protected String value;

    /**
     Int literal.
     */

    public IntLiteral() {
        setValue("0");
    }

    /**
     Int literal.
     @param value an int value.
     */

    public IntLiteral(int value) {
        setValue("" + value);
    }

    /**
     Int literal.
     @param value a string.
     */

    public IntLiteral(String value) {
        setValue(value);
    }

    /**
     Int literal.
     @param proto an int literal.
     */

    protected IntLiteral(IntLiteral proto) {
        super(proto);
        value = proto.value;
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new IntLiteral(this);
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
        v.visitIntLiteral(this);
    }
}
