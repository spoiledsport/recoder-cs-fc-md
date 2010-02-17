// This file is part of the RECODER library and protected by the LGPL.

package recodercs.kit.problem;

import recodercs.kit.problem.*;

/**
   Problem report returned by the analysis phase of a
   {@link Transformation} indicating that the planned transformation
   is applicable. This does not guarantee that the functional behavior
   will be retained.
   <P>
   Instead of creating a new object, 
   the {@link recodercs.kit.Transformation#NO_PROBLEM} constant
   should be used.
   @see recoder.kit.Equivalence

   @author AL
*/
public class NoProblem implements ProblemReport {

    /**
       Externally invisible constructor.
     */
    public NoProblem() {}
}

