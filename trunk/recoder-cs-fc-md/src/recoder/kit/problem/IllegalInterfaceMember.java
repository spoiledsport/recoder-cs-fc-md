// This file is part of the RECODER library and protected by the LGPL.

package recoder.kit.problem;

import recoder.csharp.declaration.*;

/**
   Problem report indicating that a member declaration is not a valid
   member of an interface.
 */
public class IllegalInterfaceMember extends Conflict {

    private MemberDeclaration member;
    
    public IllegalInterfaceMember(MemberDeclaration member) {
	this.member = member;
    }

    public MemberDeclaration getMember() {
	return member;
    }    
}
