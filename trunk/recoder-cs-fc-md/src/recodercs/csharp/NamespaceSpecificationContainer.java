// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.list.*;

/**
 Namespace specification container.
 @author <TT>AutoDoc</TT>
 */

public interface NamespaceSpecificationContainer extends NonTerminalProgramElement {

    /**
     Get the number of namespace specifications in this container.
     @return the number of namespace specifications.
     */
    int getNamespaceSpecificationCount();

    /*
      Returns the namespace specification at the specified index in this node's
      "virtual" namespace array.
      @param index an index for a namespace specification.
      @return the namespace specification with the given index.
      @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
      of bounds.
    */
    NamespaceSpecification getNamespaceSpecificationAt(int index);
}
