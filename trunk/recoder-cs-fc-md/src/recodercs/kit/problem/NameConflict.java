// This file is part of the RECODER library and protected by the LGPL

package recodercs.kit.problem;

import recodercs.NamedModelElement;
import recodercs.*;

/**
   Problem report indicating that the planned transformation
   produces a name conflict with an existing model element.
*/	
public class NameConflict extends Conflict {

    private NamedModelElement reason;

    /**
       Creates a new problem report with the given element as the
       reason of the conflict.
     */
    public NameConflict(NamedModelElement reason) {
	this.reason = reason;
    }
    
    /**
       Returns the element that produced the name conflict.
       @return a named element.
     */
    public NamedModelElement getReason() {
	return reason;
    }
}


