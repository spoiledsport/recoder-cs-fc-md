// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration.modifier;

import recoder.*;
import recoder.csharp.*;

/**
 NewModifier
 @author <TT>AutoDoc</TT>
 */

public class NewModifier extends VisibilityModifier {

    /**
     NewModifier
     */

    public NewModifier() {}

    /**
     NewModifier.
     @param proto a NewModifier.
     */

    protected NewModifier(NewModifier proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new NewModifier(this);
    }

    public void accept(SourceVisitor v){

        v.visitNewModifier(this);
    }
}
