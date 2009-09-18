// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration;

import recoder.*;
import recoder.csharp.*;
import recoder.csharp.attributes.AttributableElement;

/**
 Member declaration.
 @author <TT>AutoDoc</TT>
 */

public interface MemberDeclaration extends Declaration, AttributableElement {

    /**
     Get member parent.
     @return the type declaration.
     */
    TypeDeclaration getMemberParent();

    /** does *not* add to parent's children list automatically */
    void setMemberParent(TypeDeclaration t);

    /**
     * Test whether the declaration is private.
     */
    boolean isPrivate();

    /**
     * Test whether the declaration is protected.
     */
    boolean isProtected();

    /**
     * Test whether the declaration is public.
     */
    boolean isPublic();

    /**
     * Test whether the declaration is protected.
     */
    boolean isInternal();

    /**
     * Test whether the declaration is static.
     */
    boolean isStatic();

    /**
     * Test whether the declaration is new.
     */
    boolean isNew();

    /**
       Checks if this member is final.
       @return <CODE>true</CODE> if this member is final,
       <CODE>false</CODE> otherwise.
     */
    boolean isSealed();
	
}
