// This file is part of the RECODER library and protected by the LGPL.

package recoder.abstraction;

import recoder.convenience.csharp.MultidimArrayUtils;
import recoder.service.*;

/**
   A program model element representing array types.
   @author AL
   @author RN
 */
public class ArrayType implements Type {

	private Type basetype;
	private String shortName;
	private String fullName;
	ProgramModelInfo pmi;

	/**
	   Creates a new array type for the given base type, organized by
	   the given program model info.
	   Arrays are multidimensional.
	   @param basetype the base type of the array.
	   @param pmi the program model info responsible for this type.
	   @param dim the dimension of the array type
	 */
	public ArrayType(Type basetype, ProgramModelInfo pmi, int dim) {
		this.basetype = basetype;
		this.pmi = pmi;
		shortName =
			MultidimArrayUtils.appendDimensions(basetype.getName(),dim);
		fullName =
			MultidimArrayUtils.appendDimensions(basetype.getFullName(),dim);
	}

	/**
	   Returns the base type of this array type.
	   @return the base type.
	 */
	public Type getBaseType() {
		return basetype;
	}

	/**
	   Returns the name of this array type.
	   @return the name of this type.
	 */
	public String getName() {
		return shortName;
	}

	/**
	   Returns the maximal expanded name including all applicable
	   qualifiers.
	   @return the full name of this program model element.
	 */
	public String getFullName() {
		return fullName;
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

	/////////////////// ADDED FOR C#: Multidimensional arrays

	/** Stores the dimension of the array. 
	 */
	private int dimension;

	/**
	 * Returns the dimension of the array. In C# arrays may be multidimensional.
	 * @return int
	 */
	public int getDimension() {
		return dimension;
	}

}
