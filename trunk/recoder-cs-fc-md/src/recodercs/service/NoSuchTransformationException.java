// This file is part of the RECODER library and protected by the LGPL.

package recodercs.service;

import recodercs.ModelException;
import recodercs.kit.Transformation;


/**
   Exception indicating that a transformation is not accessible.
   @author AL.
 */
public class NoSuchTransformationException extends ModelException {

    /**
       Empty constructor.
     */
    public NoSuchTransformationException() {}

    /**
       Empty constructor.
     */
    public NoSuchTransformationException(Transformation transformation) {
	this("Transformation not found: " + transformation.toString());
    }

    /**
       Constructor with an explanation text.
       @param s an explanation.
     */
    public NoSuchTransformationException(String s) {
        super(s);
    }
}

