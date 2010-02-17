// This file is part of the RECODER library and protected by the LGPL.

package recodercs.abstraction;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.list.*;

/**
   A program model element representing class types.
   @author AL
   @author RN
 */
public interface ClassType extends DeclaredType, DeclaredTypeContainer {

	/**
	   Checks if this class type denotes an interface.
	   @return <CODE>true</CODE> if this object represents an interface,
	   <CODE>false</CODE> if it is an ordinary class.
	 */
	boolean isInterface();

	/** 
	 * Checks, if this class denotes a structure.
	 */
	boolean isStruct();

	/** 
	Returns the list of locally declared supertypes of this class type.
	@return the list of locally defined supertypes of this type.
	*/
	ClassTypeList getSupertypes();

	/** 
	Returns the list of all supertypes of this class type,
	in topological order, including the class type isself as first element.
	The order allows to resolve member overloading or overloading.
	@return the list of all supertypes of this type in topological order.
	*/
	ClassTypeList getAllSupertypes();

	/** 
	Returns the fields locally defined within this class type.
	@return the list of field members of this type.
	*/
	FieldList getFields();

	/** 
	Returns all visible fields that are defined in this class type
	or any of its supertypes. The fields are in topological order
	with respect to the inheritance hierarchy.
	@return the list of visible field members of this type and its
	supertypes.
	*/
	FieldList getAllFields();

	/** 
	Returns the methods locally defined within this class type.
	@return the list of methods of this type.
	*/
	MethodList getMethods();

	/** 
	Returns all visible methods that are defined in this class type
	or any of its supertypes. The methods are in topological order
	with respect to the inheritance hierarchy.
	@return the list of visible methods of this type and its supertypes.
	*/
	MethodList getAllMethods();

	/** 
	Returns the constructors locally defined within this class type.
	@return the list of constructors of this type.
	*/
	ConstructorList getConstructors();

	/** 
	Returns all declared types that are inner types of this class type,
	including visible inherited types.
	@return a list of class types that are members of this type
	or any of its supertypes.
	@see #getAllSupertypes
	*/
	DeclaredTypeList getAllTypes();

}
