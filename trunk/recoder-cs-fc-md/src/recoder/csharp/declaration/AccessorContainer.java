package recoder.csharp.declaration;

import recoder.csharp.NonTerminalProgramElement;

/**
 * @author kis
 *
 * Every class that contain one ore more references to Accessors should 
 * implement this interface.
 */
public interface AccessorContainer extends NonTerminalProgramElement {
	
	/**
	 * How many Accessors?
	 */
	int getAccessorCount();
	
	/**
	 * Get a specific Accessor or ArrayIndexOutOfBound on error.
	 */
	Accessor getAccessorAt(int index);
	
}
