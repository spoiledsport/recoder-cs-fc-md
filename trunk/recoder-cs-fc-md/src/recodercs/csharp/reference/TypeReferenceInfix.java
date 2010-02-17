// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.reference;

import recodercs.csharp.*;

/**
 ReferencePrefix and -suffix that is admissible for outer type references.
 */

public interface TypeReferenceInfix
 extends ReferencePrefix, ReferenceSuffix, NameReference {

    /**
     Set reference prefix.
     @param prefix a reference prefix.
     */
    void setReferencePrefix(ReferencePrefix prefix);
}
