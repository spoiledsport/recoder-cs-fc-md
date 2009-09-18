// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration.modifier;

import recoder.*;
import recoder.csharp.*;

/**
 Private.
 @author <TT>AutoDoc</TT>
 */

public class Private extends VisibilityModifier {

    /**
     Private.
     */

    public Private() {}

    /**
     Private.
     @param proto a private.
     */

    protected Private(Private proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Private(this);
    }

    public void accept(SourceVisitor v){
        v.visitPrivate(this);
    }
}
