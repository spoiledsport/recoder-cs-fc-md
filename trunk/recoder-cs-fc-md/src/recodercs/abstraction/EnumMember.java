// This file is part of the RECODER library and protected by the LGPL.

package recodercs.abstraction;

import recodercs.abstraction.Field;

/**
   A program model element representing fields inside enums.
   
   The fields of an enum behave exactly as fields, except that they
   are considered as "public static final".
   
   @author Jozsef OROSZ
*/
public interface EnumMember extends Field {

	public Enum getEnum();
	
}
