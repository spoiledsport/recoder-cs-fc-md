// This file is part of the RECODER library and protected by the LGPL.

package recodercs.abstraction;


import recodercs.ModelException;
import recodercs.list.*;
import recodercs.service.ProgramModelInfo;
import recodercs.util.Debug;

/**
   A program model element representing packages.
   @author AL
   @author RN
 */
public class Namespace implements DeclaredTypeContainer {

	private String name;
	private ProgramModelInfo pmi;

	/**
	   Creates a new package with the given name, organized by
	   the given program model info.
	   @param name the name of the package.
	   @param pmi the program model info responsible for this package.
	 */
	public Namespace(String name, ProgramModelInfo pmi) {
		Debug.asserta(name);
		this.name = name;
		this.pmi = pmi;
	}

	/**
	   Returns the name of this package.
	   @return the name of this package.
	 */
	public String getName() {
		return name;
	}

	/**
	   Returns the name of this package.
	   @return the full name of this program model element.
	 */
	public String getFullName() {
		return getName();
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

	/** 
	Returns the list of class types defined within this ontainer.
	@return a list of contained class types.
	*/
	public DeclaredTypeList getDeclaredTypes() {
		return pmi.getTypes(this);
	}

	/**
	   Returns the enclosing package or class type, or method.
	   @return <CODE>null</CODE>.
	 */
	public DeclaredTypeContainer getContainer() {
		return null;
	}

	/**
	   Returns the enclosing package.
	   @return <CODE>null</CODE>.
	 */
	public Namespace getNamespace() {
		return this;
	}

	public void validate() throws ModelException {
	}

}
