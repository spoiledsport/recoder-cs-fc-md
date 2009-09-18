// This file is part of the RECODER library and protected by the LGPL.

package recoder.abstraction;

import recoder.list.*;

/**
   A program model element representing methods.
 */
public interface Method extends Member {

    /** 
	Returns the signature of this method or constructor.
	@return the signature of this method.
    */
    TypeList getSignature();

    /** 
	Returns the exceptions of this method or constructor.
	@return the exceptions of this method.
	@deprecated This method has no sense anymore, since C# methods don't
	              have to report exceptions.
    */    
    ClassTypeList getExceptions();

    /** 
	Returns the return type of this method.
	@return the return type of this method.
    */    
    Type getReturnType();
    
    /**
       Checks if this member is abstract. A constructor will report
       <CODE>false</CODE>.
       @return <CODE>true</CODE> if this member is abstract,
       <CODE>false</CODE> otherwise.
       @see recoder.abstraction.Constructor
     */
    boolean isAbstract();

    /**
       Checks if this method is native.
       @return <CODE>true</CODE> if this method is extern,
       <CODE>false</CODE> otherwise.
       @see recoder.abstraction.Constructor
     */
    boolean isExtern();

    /**
       Checks if this method is virtual. A constructor will report
       <CODE>false</CODE>.
       @return <CODE>true</CODE> if this method is virtual,
       <CODE>false</CODE> otherwise.
       @see recoder.abstraction.Constructor
     */
    boolean isVirtual();

    /**
       Checks if this method is overriding an another method. A constructor will report
       <CODE>false</CODE>.
       @return <CODE>true</CODE> if this method has the override modifier,
       <CODE>false</CODE> otherwise.
       @see recoder.abstraction.Constructor
     */
    boolean isOverride();
    
    
    
}
