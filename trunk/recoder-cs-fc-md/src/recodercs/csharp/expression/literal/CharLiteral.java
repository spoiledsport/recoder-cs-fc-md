// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.literal;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.expression.*;

/**
 Char literal.
 @author <TT>AutoDoc</TT>
 */

public class CharLiteral extends ReferencePrefixLiteral {

    protected String value;

    /**
     Char literal.
     */

    public CharLiteral() {}

    /**
     Char literal.
     @param value a char value.
     */

    public CharLiteral(char value) {
        setValue(value);
    }

    /**
     Char literal.
     @param value a string.
     */

    public CharLiteral(String value) {
        setValue(value);
    }

    /**
     Char literal.
     @param proto a char literal.
     */

    protected CharLiteral(CharLiteral proto) {
        super(proto);
        value = proto.value;
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new CharLiteral(this);
    }

    /**
     Set value.
     @param c a char value.
     */

    public void setValue(char c) {
        setValue("'" + c + "'");
    }

    /**
     Set value.
     @param str a string value.
     */

    public void setValue(String str) {
        if (!str.startsWith("'") || !str.endsWith("'")) {
            throw new IllegalArgumentException("Bad char literal " + value);
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
        v.visitCharLiteral(this);
    }
}
