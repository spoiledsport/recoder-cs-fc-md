// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp;

import recoder.*;
import recoder.abstraction.*;
import recoder.csharp.declaration.*;
import recoder.list.*;

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
