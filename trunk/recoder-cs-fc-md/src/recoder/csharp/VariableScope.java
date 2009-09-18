// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp;

import recoder.*;
import recoder.csharp.declaration.*;
import recoder.list.*;

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
