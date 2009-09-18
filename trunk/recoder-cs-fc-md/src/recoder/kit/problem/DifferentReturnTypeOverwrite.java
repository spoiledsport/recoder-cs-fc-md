// This file is part of the RECODER library and protected by the LGPL.

package recoder.kit.problem;

import recoder.abstraction.*;

/**
   Problem report indicating that a method has been overwritten with
   a different return type.
 */
public class DifferentReturnTypeOverwrite extends Conflict {

    private Method method;
    
    public DifferentReturnTypeOverwrite(Method method) {
	this.method = method;
    }

    public Method getMethod() {
	return method;
    }    
}
