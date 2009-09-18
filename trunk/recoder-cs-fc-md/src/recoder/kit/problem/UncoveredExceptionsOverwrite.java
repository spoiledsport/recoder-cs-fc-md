// This file is part of the RECODER library and protected by the LGPL.

package recoder.kit.problem;

import recoder.abstraction.*;

/**
   Problem report indicating that a method has been overwritten 
   with a version that offers more strict exceptions.
 */
public class UncoveredExceptionsOverwrite extends Conflict {

    private Method method;
    
    public UncoveredExceptionsOverwrite(Method method) {
	this.method = method;
    }

    public Method getMethod() {
	return method;
    }    
}

