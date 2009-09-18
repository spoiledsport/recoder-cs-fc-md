// This file is part of the RECODER library and protected by the LGPL.

package recoder.service;

import recoder.*;
import recoder.abstraction.*;
import recoder.abstraction.Namespace;

/**
   Handles requests for implicitely defined program model elements.
   In particular these are {@link recoder.abstraction.NullType},
   {@link recoder.abstraction.Namespace}, 
   {@link recoder.abstraction.ArrayType}, and
   {@link recoder.abstraction.DefaultConstructor}.
 */
public interface ImplicitElementInfo extends ProgramModelInfo {

    /**
       Returns the default constructor associated with the given
       class type, or <CODE>null</CODE> if there is none.
       @param ct a class type.
       @return the default constructor of the given type, or <CODE>null</CODE>.
     */
    //OK
    DefaultConstructor getDefaultConstructor(ClassType ct);
    
}
