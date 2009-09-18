// This file is part of the RECODER library and protected by the LGPL.

package recoder.service;

import recoder.ModelException;
import recoder.csharp.Using;
import recoder.abstraction.ClassType;
import recoder.list.*;

/**
   Exception indicating that a particular import is ambiguous.
   @author AL
 */
public class AmbiguousImportException extends ModelException {

    private Using importStatement;
    private ClassType version1;
    private ClassType version2;


    /**
       Constructor without explanation text.
       @param importStatement the import found to be ambiguous.
       @param version1 the first possible type.
       @param version2 the second possible type.
     */
    public AmbiguousImportException(Using importStatement, ClassType version1, ClassType version2) {
	this.importStatement = importStatement;
	this.version1 = version1;
	this.version2 = version2;
    }


    /**
       Constructor with an explanation text.
       @param s an explanation.
       @param importStatement the import found to be ambiguous.
       @param version1 the first possible type.
       @param version2 the second possible type.
     */
    public AmbiguousImportException(String s, Using importStatement, ClassType version1, ClassType version2) {
	super(s);
	this.importStatement = importStatement;
	this.version1 = version1;
	this.version2 = version2;
    }

    /**
       Returns the import statement that was found ambiguous.
     */
    public Using getAmbiguousImport() {
	return importStatement;
    }

    /**
       Returns the possible imported class types.
     */
    public ClassTypeList getChoices() {
	ClassTypeMutableList list = new ClassTypeArrayList(2);
	list.add(version1);
	list.add(version2);
	return list;
    }

}
