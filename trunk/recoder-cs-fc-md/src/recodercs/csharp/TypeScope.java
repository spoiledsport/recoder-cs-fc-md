// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp;

import recodercs.*;
import recodercs.abstraction.*;
import recodercs.csharp.declaration.*;
import recodercs.list.*;

/**
   The property of a non terminal program element to define a scope for 
   types.
 */

public interface TypeScope extends ScopeDefiningElement {

    DeclaredTypeList getTypesInScope();
    
    DeclaredType getTypeInScope(String name);
    
    void addTypeToScope(DeclaredType type, String name);
    
    void removeTypeFromScope(String name);
    
}
