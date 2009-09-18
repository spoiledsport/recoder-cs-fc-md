// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.reference;

import recoder.*;
import recoder.list.*;
import recoder.csharp.*;

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
