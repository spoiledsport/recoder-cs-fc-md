// This file is part of the RECODER library and protected by the LGPL.

package recoder.abstraction;

import recoder.service.*;
import recoder.util.Debug;

import recoder.csharp.*;
import recoder.list.*;

/**
   A program model element representing primitive types.
   @author AL
   @author RN
 */
public class PrimitiveType implements Type {

	private String name;
	private ProgramModelInfo pmi;

	/** The name of the class, this primitive type can be boxed into. */
	private String boxname; 

	public PrimitiveType(String name, String boxname, ProgramModelInfo pmi) {
		this.name = name.intern();
		this.pmi = pmi;
		this.boxname = boxname;
	}

	/**
	   Returns the name of this type.
	   @return the name of this type.
	 */
	public String getName() {
		return name;
	}

	/**
	   Returns the name of type.
	   @return the full name of this program model element.
	 */
	public String getFullName() {
		return name;
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
	@param service the program model info for this element.
	*/
	public void setProgramModelInfo(ProgramModelInfo service) {
		this.pmi = service;
	}

	public void validate() {
	}

	/**
	 * Method getBoxingClass.
	 * @return Type
	 */
	public Type getBoxingClass() {
		Debug.asserta(pmi);
		Debug.asserta(pmi.getServiceConfiguration());
		Debug.asserta(pmi.getServiceConfiguration().getNameInfo());
		return pmi.getServiceConfiguration().getNameInfo().getType(boxname);
	}

}
