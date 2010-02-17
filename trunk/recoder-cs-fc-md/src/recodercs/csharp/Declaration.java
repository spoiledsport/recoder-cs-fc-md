// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp;

import recodercs.*;
import recodercs.list.*;

/**
 Declaration.
 @author <TT>AutoDoc</TT>
 */

public interface Declaration extends NonTerminalProgramElement {

    /**
     * Get the modifiers.
     * @return the (original) list of modifiers.
     */
    ModifierMutableList getModifiers();

    /**
     * Sets the modifiers.
     * @param m the new (original) list of modifiers.
     */
    void setModifiers(ModifierMutableList m);
}
