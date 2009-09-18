// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration.modifier;

import recoder.*;
import recoder.csharp.*;

/**
 Protected.
 @author <TT>AutoDoc</TT>
 */

public class Protected extends VisibilityModifier {

    /**
     Protected.
     */

    public Protected() {}

    /**
     Protected.
     @param proto a protected.
     */

    protected Protected(Protected proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Protected(this);
    }

    public void accept(SourceVisitor v){
        v.visitProtected(this);
    }
}
