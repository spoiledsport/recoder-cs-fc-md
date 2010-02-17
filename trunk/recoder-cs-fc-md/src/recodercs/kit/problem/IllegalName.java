// This file is part of the RECODER library and protected by the LGPL.

package recodercs.kit.problem;

import recodercs.NamedModelElement;
import recodercs.*;

/**
   Problem report indicating that a chosen name is illegal,
   for instance a keyword.
 */
public class IllegalName extends Problem {

    private NamedModelElement element;
    
    public IllegalName(NamedModelElement element) {
	this.element = element;
    }

    public NamedModelElement getElement() {
	return element;
    }    
}

