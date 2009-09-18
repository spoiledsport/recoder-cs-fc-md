// This file is part of the RECODER library and protected by the LGPL.

package recoder.kit;

/** This interface contains constants for modifier codings. Was formely named 
 *  access modifiers in recoder.bytecode.
 */
public interface Modifiers {

	/** These modifiers can be coded into an integer, if it is needed.
	 */
	int PUBLIC 	= 0x0001;
	int PRIVATE 	= 0x0002;
	int PROTECTED 	= 0x0004;
	int INTERNAL 	= 0x0008;
	int STATIC 	= 0x0010;
	int SEALED 	= 0x0020;
	int EXTERN 	= 0x0040;
	int ABSTRACT 	= 0x0080;
	int READONLY 	= 0x0100;
	int VOLATILE 	= 0x0200;
	int NEW 		= 0x0400;
	int OVERIDE 	= 0x0800;
	int VIRTUAL 	= 0x1000;
	int REF 		= 0x2000;
	int PARAMS 	= 0x4000;
	int OUT 		= 0x8000;
	
	/** This is just for the modifier checking. */
	int INTERNAL_PROTECTED = INTERNAL | PROTECTED;
	
}
