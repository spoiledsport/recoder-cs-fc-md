// This file is part of the RECODER library and protected by the LGPL.

package recoder.abstraction;

/**
   A program model element representing variables.
   @author AL
   @author RN
 */
public interface Variable extends ProgramModelElement {
    
    /**
       Checks if this variable is final.
       @return <CODE>true</CODE> if this variable is final,
       <CODE>false</CODE> otherwise.
     */
    boolean isReadOnly();

    /**
       Returns the type of this variable.
       @return the type of this variable.
    */
    Type getType();
    
}
