// This file is part of the RECODER library and protected by the LGPL.

package recoder.kit.problem;

import recoder.*;

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

