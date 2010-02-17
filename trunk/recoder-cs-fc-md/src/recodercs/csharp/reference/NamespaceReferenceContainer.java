// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.reference;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.list.*;

/**
 Element that contains a NamespaceReference.
 @author AL
 */

public interface NamespaceReferenceContainer extends NonTerminalProgramElement {

    /**
     Get the package reference.
     @return the package reference.
     */
    NamespaceReference getNamespaceReference();
}
