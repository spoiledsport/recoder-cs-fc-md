// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration.modifier;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;

/**
 Visibility modifier.
 @author <TT>AutoDoc</TT>
 */

public abstract class VisibilityModifier extends recoder.csharp.declaration.Modifier {

    /**
     Visibility modifier.
     */

    public VisibilityModifier() {}

    /**
     Visibility modifier.
     @param proto a visibility modifier.
     */

    protected VisibilityModifier(VisibilityModifier proto) {
        super(proto);
    }
}
