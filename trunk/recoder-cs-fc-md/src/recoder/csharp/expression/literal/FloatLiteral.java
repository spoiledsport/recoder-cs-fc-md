// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.literal;

import recoder.csharp.*;
import recoder.csharp.expression.*;

/**
 Float literal.
 @author <TT>AutoDoc</TT>
 */

public class FloatLiteral extends ReferencePrefixLiteral {

    /**
     Textual representation of the value.
     */

    protected String value;

    /**
     Float literal.
     */

    public FloatLiteral() {
        setValue("0.0F");
    }

    /**
     Float literal.
     @param value a float value.
     */

    public FloatLiteral(float value) {
        setValue("" + value + 'F');
    }

    /**
     Float literal.
     @param value a string.
     */

    public FloatLiteral(String value) {
        setValue((value.endsWith("F") || value.endsWith("f")) ? value :
              (value + 'F'));
    }

    /**
     Float literal.
     @param proto a float literal.
     */

    protected FloatLiteral(FloatLiteral proto) {
        super(proto);
        value = proto.value;
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new FloatLiteral(this);
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
        v.visitFloatLiteral(this);
    }
}
