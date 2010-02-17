// This file is part of the RECODER library and protected by the LGPL.

package recodercs;

import recodercs.ModelException;

// import recodercs.list.*;

/** 
 * A semantic part of the software model. A model element is
 * not necessarily connected to a piece of syntax.
 *
 * @see recodercs.csharp.SourceElement
 */
public interface ModelElement {

    /** 
     * Check consistency and admissibility of a construct, e.g.
     * cardinality of participants.
     *
     * @exception ModelException
     */
    void validate() throws ModelException;
}
