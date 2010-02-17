// This file is part of the RECODER library and protected by the LGPL.

package recodercs.service;

import recodercs.abstraction.ClassType;
import recodercs.abstraction.DefaultConstructor;
import recodercs.service.ProgramModelInfo;
import recodercs.*;
import recodercs.abstraction.*;

/**
   Handles requests for implicitely defined program model elements.
   In particular these are {@link recodercs.abstraction.NullType},
   {@link recodercs.abstraction.Namespace}, 
   {@link recodercs.abstraction.ArrayType}, and
   {@link recodercs.abstraction.DefaultConstructor}.
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
