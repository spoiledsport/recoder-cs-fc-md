package recoder.abstraction;

import recoder.list.TypeList;

/**
 * Delegate.java
 * @author orosz
 *
 * This is an abstract interface for the delegate types.
 * 
 */
public interface Delegate extends DeclaredType {


	/** Checks, if the method can fit into this delegate. */
	public boolean isCompatibleMethod(Method m);
	
	/** 
	Returns the signature of the delegate.
	@return the signature, which a compatible method must have.
    */
    public TypeList getSignature();

    /** 
	Returns the return type of the delegate.
	@return the return type, a compatible method must have.	
    */    
    Type getReturnType();

}
