// This file is part of the RECODER library and protected by the LGPL.

package recoder.abstraction;

/**
   A program model element representing fields.
   @author AL
   @author RN
 */
public interface Field extends Variable, Member {

	/** Returns if the specified field is volatile or not. */
	public boolean isVolatile();
	
	
}
