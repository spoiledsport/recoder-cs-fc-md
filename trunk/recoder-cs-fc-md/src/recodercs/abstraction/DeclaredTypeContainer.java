// This file is part of the RECODER library and protected by the LGPL.

package recodercs.abstraction;

import recodercs.abstraction.ProgramModelElement;
import recodercs.list.ClassTypeList;
import recodercs.list.DeclaredTypeList;

/**
   A program model element that may contain declared types (classes, 
   enums and delegates)
   .
   @author AL
   @author RN
 */
public interface DeclaredTypeContainer extends ProgramModelElement {

    /** 
	Returns the class types locally defined within this container.
	Returns inner types when this container is a class type.
	@return a list of contained class types.
	
	This method was originally called getTypes, but has been modified,
	since now a DeclaredTypeContainer can hold Enums and Delegates as well.
    */
    DeclaredTypeList getDeclaredTypes();

    /**
       Returns the enclosing package or class type, or method.
       Namespaces shall report <tt>null</tt>.
       @return the container of this element.
     */
    DeclaredTypeContainer getContainer();

}
