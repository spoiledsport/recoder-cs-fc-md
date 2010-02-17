// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.literal;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.expression.*;
import recodercs.csharp.reference.*;
import recodercs.list.*;

/**
 PreDefinedTypeLiteral.
 @author <TT>AutoDoc</TT>
 */

public class PreDefinedTypeLiteral extends ReferencePrefixLiteral {


    /**
     The value.
     */

    protected String value;

    /**
     PreDefinedTypeLiteral.
     */

    public PreDefinedTypeLiteral() {
        setValue("");
    }

    /**
     PreDefinedTypeLiteral.
     @param value a string.
     */

    public PreDefinedTypeLiteral(String value) {
        setValue(value);
    }

    /**
     PreDefinedTypeLiteral.
     @param proto a string literal.
     */

    protected PreDefinedTypeLiteral(PreDefinedTypeLiteral proto) {
        super(proto);
        value = proto.value;
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new PreDefinedTypeLiteral(this);
    }


    /**
     Set value.
     @param str a string value.
     */

    public void setValue(String str) {
    	this.value = str;
    }

    /**
     Get value.
     @return the string.
     */

    public String getValue() {
        return value;
    }

    public void accept(SourceVisitor v) {
        v.visitPreDefinedTypeLiteral(this);
    }
}
