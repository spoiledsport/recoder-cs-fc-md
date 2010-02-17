// This file is part of the RECODER library and protected by the LGPL.

package recodercs.kit.problem;

/**
   Problem report indicating that the planned transformation
   is applicable and will not change the functional behavior
   of the program.
   <P>
   Instead of creating a new object, 
   the {@link recodercs.kit.Transformation#EQUIVALENCE} constant
   should be used.

   @author AL
*/
public class Equivalence extends NoProblem {

    public Equivalence() {}
}
