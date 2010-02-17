// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp;

import recodercs.*;
import recodercs.csharp.declaration.*;
import recodercs.list.*;

/**
   The property of a non terminal program element to define a scope for 
   variables.
 */

public interface VariableScope extends ScopeDefiningElement {

    VariableSpecificationList getVariablesInScope();
    VariableSpecification getVariableInScope(String name);
    void addVariableToScope(VariableSpecification var);
    void removeVariableFromScope(String name);
}
