// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration.modifier;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;

/**
 Visibility modifier.
 @author <TT>AutoDoc</TT>
 */

public abstract class VisibilityModifier extends recodercs.csharp.declaration.Modifier {

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
