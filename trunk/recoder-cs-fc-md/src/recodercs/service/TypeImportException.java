// This file is part of the RECODER library and protected by the LGPL.

package recodercs.service;

import recodercs.ModelException;
import recodercs.ParserException;
import recodercs.*;

/**
   Exception indicating that a certain type could not be imported.
   This might have been a consequence of a parse or IO exception.
   @author AL
 */
public class TypeImportException extends ModelException {

    /**
       Empty constructor.
     */
    public TypeImportException() {}

    /**
       Constructor with an explanation text.
       @param s an explanation.
     */
    public TypeImportException(String s) {
        super(s);
    }

    /**
       Constructor to wrap a parser exception.
       @param p a parser exception.
     */
    public TypeImportException(ParserException p) {
        super(p.toString());
    }
}

