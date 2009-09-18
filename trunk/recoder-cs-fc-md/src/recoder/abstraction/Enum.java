package recoder.abstraction;

import recoder.list.EnumMemberList;

/**
 * Enum.java
 * @author orosz
 *
 * 
 */
public interface Enum extends DeclaredType {


	/** Returns the members in this enum. */
	EnumMemberList getFields();
	
    /**
       Returns the basetype of the enum.
    */
    Type getBaseType();


}
