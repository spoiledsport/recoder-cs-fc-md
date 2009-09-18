// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.expression.operator;

import recoder.csharp.*;
import recoder.csharp.reference.*;
import recoder.csharp.expression.*;
import recoder.list.*;

import java.io.*;

/**
 As.
 @author <TT>AutoDoc</TT>
 */

public class As extends TypeCast {

    /**
     As.
     */

    public As() {}

    /**
     Note: The ordering of the arguments does not match the syntactical
     appearance of a Java type case, but the order in the superclass
     TypeOperator. However, getASTChildren yields them in the right
     order.
     */

    public As(Expression child, TypeReference typeref) {
        super(child, typeref);
        makeParentRoleValid();
    }

    /**
     As.
     @param proto a type cast.
     */

    protected As(As proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new As(this);
    }

    public void accept(SourceVisitor v) {
       v.visitAs(this);
    }
}
