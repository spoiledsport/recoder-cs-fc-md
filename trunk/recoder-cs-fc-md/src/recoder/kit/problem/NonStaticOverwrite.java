// This file is part of the RECODER library and protected by the LGPL.

package recoder.kit.problem;

import recoder.abstraction.*;


/**
   Problem report indicating that a static method has been overwritten 
   with a non-static version.
 */
public class NonStaticOverwrite extends Conflict {

    private Method method;
    
    public NonStaticOverwrite(Method method) {
	this.method = method;
    }

    public Method getMethod() {
	return method;
    }    
}
