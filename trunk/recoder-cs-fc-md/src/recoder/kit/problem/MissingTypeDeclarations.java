// This file is part of the RECODER library and protected by the LGPL

package recoder.kit.problem;

import recoder.list.*;

/**
   Problem report indicating that certain class types have no
   source representation.
*/
public class MissingTypeDeclarations extends MissingSources {
    
    private ClassTypeList nonTypeDeclarations;
    
    public MissingTypeDeclarations(ClassTypeList nonTypeDeclarations) {
	this.nonTypeDeclarations = nonTypeDeclarations;
    }
    
    public ClassTypeList getNonTypeDeclarations() {
	return nonTypeDeclarations;
    }
}
