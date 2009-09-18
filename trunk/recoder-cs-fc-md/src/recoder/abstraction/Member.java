// This file is part of the RECODER library and protected by the LGPL.

package recoder.abstraction;

import recoder.*;
import recoder.csharp.*;
import recoder.list.*;

/**
   A program model element representing members.
   @author AL
   @author RN
 */
public interface Member extends ProgramModelElement {

    /**
       Checks if this member is final.
       @return <CODE>true</CODE> if this member is final,
       <CODE>false</CODE> otherwise.
     */
    boolean isSealed();
    
    /**
       Checks if this member is static. Returns <CODE>true</CODE>
       for {@link recoder.abstraction.Constructor}s.
       @return <CODE>true</CODE> if this member is static,
       <CODE>false</CODE> otherwise.
     */
    boolean isStatic();
    
    /**
       Checks if this member is private.
       @return <CODE>true</CODE> if this member is private,
       <CODE>false</CODE> otherwise.
     */
    boolean isPrivate();
    
    /**
       Checks if this member is protected.
       @return <CODE>true</CODE> if this member is protected,
       <CODE>false</CODE> otherwise.
     */
    boolean isProtected();

    /**
       Checks if this member is internal.
       @return <CODE>true</CODE> if this member is internal,
       <CODE>false</CODE> otherwise.
     */
    boolean isInternal();
    
    /**
       Checks if this member is public.
       @return <CODE>true</CODE> if this member is public,
       <CODE>false</CODE> otherwise.
     */
    boolean isPublic();
    
    /**
       Checks if this member is new (overriding).
       @return <CODE>true</CODE> if this member is new,
       <CODE>false</CODE> otherwise.
     */
    boolean isNew();

    /** 
	Returns the logical parent class of this member.
	@return the class type containing this member.
    */
    ClassType getContainingClassType();

}
