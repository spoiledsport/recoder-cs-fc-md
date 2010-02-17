// This file is part of the RECODER library and protected by the LGPL.

package recodercs.abstraction;

import recodercs.service.ProgramModelInfo;
import recodercs.abstraction.ClassType;
import recodercs.*;
import recodercs.list.*;
import recodercs.service.*;

/**
   A program model element representing the null type.
   @author AL
   @author RN
 */
public class NullType implements ClassType {

	/**
	   The name of this type.
	 */
	public static final String NULL = "null";

	private ProgramModelInfo pmi;

	/**
	   Create a new null type for the given program model info.
	   @param info the program model info responsible for this type.       
	 */
	public NullType(ProgramModelInfo info) {
		this.pmi = info;
	}

	/**
	   Returns the name of this element.
	   @return <CODE>"null"</CODE>.
	 */
	public String getName() {
		return NULL;
	}

	/**
	   Returns the name of this element.
	   @return <CODE>"null"</CODE>.
	 */
	public String getFullName() {
		return NULL;
	}

	/** 
	Returns the instance that can retrieve information about this
	    program model element.
	@return the program model info of this element.
	*/
	public ProgramModelInfo getProgramModelInfo() {
		return pmi;
	}

	/** 
	Sets the instance that can retrieve information about this
	    program model element.
	@param info the program model info for this element.
	*/
	public void setProgramModelInfo(ProgramModelInfo info) {
		pmi = info;
	}

	public void validate() {}

	/**
	   Checks if this member is final.
	   @return <CODE>true</CODE>.
	 */
	public boolean isSealed() {
		return true;
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
	   @return <CODE>true</CODE>.
	 */
	public boolean isPublic() {
		return true;
	}

	/** 
	Returns the (empty) list of class types locally defined within this
	container.
	@return an empty list of contained class types.
	*/
	public DeclaredTypeList getDeclaredTypes() {
		return pmi.getTypes(this);
	}

	/** 
	Returns all class types that are inner types of this class type.
	@return an empty list of class types.
	*/
	public DeclaredTypeList getAllTypes() {
		return pmi.getAllTypes(this);
	}

	/** 
	Returns the logical parent class of this member.
	@return the class type containing this member.
	*/
	public ClassType getContainingClassType() {
		return pmi.getContainingClassType(this);
	}

	/**
	   Returns the enclosing package or class type, or method.
	   @return <CODE>null</CODE>.
	 */
	public DeclaredTypeContainer getContainer() {
		return pmi.getDeclaredTypeContainer(this);
	}

	/**
	   Checks if this class type denotes an interface.
	   @return <CODE>false</CODE>.
	 */
	public boolean isInterface() {
		return false;
	}

	/**
	   Checks if this member is abstract. 
	   @return <CODE>false</CODE>.
	 */
	public boolean isAbstract() {
		return false;
	}

	/** 
	Returns the list of locally declared supertypes of this class type.
	@return the empty list of supertypes of this type.
	*/
	public ClassTypeList getSupertypes() {
		return pmi.getSupertypes(this);
	}

	/** 
	Returns the list of all supertypes of this class type, including this
	type.
	@return a list with this element as single member.
	*/
	public ClassTypeList getAllSupertypes() {
		return pmi.getAllSupertypes(this);
	}

	/** 
	Returns the fields locally defined within this class type.
	@return the (empty) list of field members of this type.
	*/
	public FieldList getFields() {
		return pmi.getFields(this);
	}

	/** 
	Returns all visible fields that are defined in this class type
	or any of its supertypes. 
	@return the (empty) list of visible field members of this type.
	*/
	public FieldList getAllFields() {
		return pmi.getAllFields(this);
	}

	/** 
	Returns the methods locally defined within this class type.
	@return the (empty) list of methods of this type.
	*/
	public MethodList getMethods() {
		return pmi.getMethods(this);
	}

	/** 
	Returns the constructors locally defined within this class type.
	@return the (empty) list of constructors of this type.
	*/
	public ConstructorList getConstructors() {
		return pmi.getConstructors(this);
	}

	/** 
	Returns all visible methods that are defined in this class type
	or any of its supertypes. 
	@return the (empty) list of visible methods of this type.
	*/
	public MethodList getAllMethods() {
		return pmi.getAllMethods(this);
	}

	/**
	   Returns the package this element is defined in. 
	   @return <CODE>null</CODE>.
	 */
	public Namespace getNamespace() {
		return pmi.getNamespace(this);
	}

	/**
	 * @see recodercs.abstraction.ClassType#isStruct()
	 */
	public boolean isStruct() {
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
