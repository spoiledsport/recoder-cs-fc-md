// This file is part of the RECODER library and protected by the LGPL

package recodercs.abstraction;

import recodercs.ModelException;
import recodercs.abstraction.ClassType;
import recodercs.abstraction.Constructor;
import recodercs.abstraction.Type;
import recodercs.service.ProgramModelInfo;
import recodercs.*;
import recodercs.convenience.Naming;
import recodercs.list.*;
import recodercs.service.*;
import recodercs.util.Debug;

/**
   Default constructor of class types.

   @author AL
 */
public class DefaultConstructor implements Constructor {

	protected ProgramModelInfo service;

	protected ClassType ownerClass;

	/**
	   Create a new default constructor for the given class type.
	   The name of the constructor is set appropriately.
	   @param ownerClass the owner class of this constructor.
	 */
	public DefaultConstructor(ClassType ownerClass) {
		Debug.asserta(ownerClass);
		this.ownerClass = ownerClass;
	}

	/** 
	Returns the instance that can retrieve information about this
	    program model element.
	@return the program model info of this element.
	*/
	public ProgramModelInfo getProgramModelInfo() {
		return service;
	}

	/** 
	Sets the instance that can retrieve information about this
	    program model element.
	@param service the program model info for this element.
	*/
	public void setProgramModelInfo(ProgramModelInfo service) {
		this.service = service;
	}

	public void validate() throws ModelException {}

	/**
	   Checks if this member is final.
	   @return <CODE>false</CODE>.
	 */
	public boolean isSealed() {
		return false;
	}

	/**
	   Checks if this member is static.
	   @return <CODE>true</CODE>.
	 */
	public boolean isStatic() {
		return true;
	}

	/**
	   Checks if this member is private.
	   @return <CODE>false</CODE>.
	 */
	public boolean isPrivate() {
		return false;
	}

	/**
	   Checks if this member is protected.
	   @return <CODE>false</CODE>.
	 */
	public boolean isProtected() {
		return false;
	}

	/**
	   Checks if this member is public.
	   @return <CODE>true</CODE>, if the containing class type is public,
	   <CODE>false</CODE> otherwise.
	 */
	public boolean isPublic() {
		return getContainingClassType().isPublic();
		// else, it is package visible
	}

	/**
	   Checks if this member is abstract.
	   @return <CODE>false</CODE>.
	 */
	public boolean isAbstract() {
		return false;
	}

	/**
	   Checks if this member is native.
	   @return <CODE>false</CODE>.
	 */
	public boolean isExtern() {
		return false;
	}

	/** 
	Returns the logical parent class of this member.
	@return the class type containing this member.
	@see recodercs.service.ProgramModelInfo#getContainingClassType
	*/
	public ClassType getContainingClassType() {
		return ownerClass;
	}

	/** 
	Returns the return type of this method.
	@return the return type of this method.
	*/
	public Type getReturnType() {
		return service.getReturnType(this);
	}

	/** 
	Returns the (empty) signature of this constructor.
	@return the (empty) signature of this constructor.
	*/
	public TypeList getSignature() {
		return service.getSignature(this);
	}

	/** 
	Returns the (empty) exception list of this constructor.
	@return the (empty) exception list of this constructor.
	*/
	public ClassTypeList getExceptions() {
		return service.getExceptions(this);
	}

	/**
	   Returns the package this element is defined in.
	   @return the package of this element.
	 */
	public Namespace getNamespace() {
		return service.getNamespace(this);
	}

	/**
	   Returns the name of this element.
	   @return the name of this element.
	 */
	public String getName() {
		return getContainingClassType().getName();
	}

	/**
	   Returns the maximal expanded name including all applicable
	   qualifiers.
	   @return the full name of this program model element.
	 */
	public String getFullName() {
		return Naming.getFullName(this);
	}

	/**
	 * @see recodercs.abstraction.Method#isOverride()
	 */
	public boolean isOverride() {
		return false;
	}

	/**
	 * @see recodercs.abstraction.Method#isVirtual()
	 */
	public boolean isVirtual() {
		return false;
	}

	/**
	 * @see recodercs.abstraction.Member#isInternal()
	 */
	public boolean isInternal() {
		return false;
	}

	/**
	 * @see recodercs.abstraction.Member#isNew()
	 */
	public boolean isNew() {
		return false;
	}

}
