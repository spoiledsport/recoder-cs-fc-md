// This file is part of the RECODER library and protected by the LGPL.

package recoder.service;

import recoder.ModelException;
import recoder.abstraction.Type;

/**
   Exception indicating that a vital class file is missing, usually
   a super type of a known class file.
   @author AL
   @since 0.72
 */
public class MissingClassFileException extends ModelException {

    private String missingClass;

    /**
       Empty constructor.
       @param name the name of the missing file.
     */
    public MissingClassFileException(String name) {
	this.missingClass = name;
    }

    /**
       Constructor with an explanation text.
       @param s an explanation.
       @param name the name of the missing file.
     */
    public MissingClassFileException(String s, String name) {
        super(s);
	this.missingClass = name;
    }

    /**
       Returns the program element that could not be resolved.
     */
    public String getMissingClassFileName() {
	return missingClass;
    }
}
