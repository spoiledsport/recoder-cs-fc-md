// This file is part of the RECODER library and protected by the LGPL.

package recodercs.kit.problem;

import recodercs.abstraction.Method;
import recodercs.abstraction.*;


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
