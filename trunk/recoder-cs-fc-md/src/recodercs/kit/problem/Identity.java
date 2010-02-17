// This file is part of the RECODER library and protected by the LGPL.

package recodercs.kit.problem;

/**
   Problem report indicating that the planned transformation
   is redundant. The syntactic transformation itself can be skipped.
   <P>
   Instead of creating a new object, 
   the {@link recodercs.kit.Transformation#IDENTITY} constant
   should be used.
   
   @author AL
*/
public class Identity extends Equivalence {
    public Identity() {}
}


