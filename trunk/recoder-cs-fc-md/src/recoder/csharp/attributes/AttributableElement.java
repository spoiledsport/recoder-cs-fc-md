package recoder.csharp.attributes;

import recoder.csharp.NonTerminalProgramElement;

/**
 * @author orosz
 *
 * Every attributable element must implement this interface.
 */
public interface AttributableElement extends NonTerminalProgramElement {
	
	/**
	 * @return the number of AttributeSections
	 */
	int getAttributeSectionCount();
	
	/**
	 * @return the AttributeSection specified by the index parameter
	 * @throws ArrayIndexOutOfBoundException if the index is out of range.
	 */
	AttributeSection getAttributeSectionAt(int index);
	
	/**
	 * Sets the AttributeSections for the current element.
	 */
	void setAttributeSections(AttributeSectionMutableList attrs);
	
	/** Returns an array of the attribute sections. */
	AttributeSectionList getAttributeSections();
	
}
