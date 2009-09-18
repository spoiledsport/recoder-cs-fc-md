// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.literal;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Long literal.
 @author <TT>AutoDoc</TT>
 */

public class LongLiteral extends ReferencePrefixLiteral {

    /**
     Textual representation of the value.
     */

    protected String value;

    /**
     Long literal.
     */

    public LongLiteral() {
        setValue("0L");
    }

    /**
     Long literal.
     @param value a long value.
     */

    public LongLiteral(long value) {
        setValue("" + value + 'L');
    }

    /**
     Long literal.
     @param value a string.
     */

    public LongLiteral(String value) {
        setValue((value.endsWith("L") || value.endsWith("l")) ? value : (value + 'L'));
    }

    /**
     Long literal.
     @param proto a long literal.
     */

    protected LongLiteral(LongLiteral proto) {
        super(proto);
        value = proto.value;
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new LongLiteral(this);
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
        v.visitLongLiteral(this);
    }
}
