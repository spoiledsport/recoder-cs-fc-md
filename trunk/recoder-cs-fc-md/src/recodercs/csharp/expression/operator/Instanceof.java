// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.expression.operator;

import recodercs.csharp.*;
import recodercs.csharp.expression.*;
import recodercs.csharp.reference.*;
import recodercs.list.*;

import java.io.*;

/**
 Instanceof.
 @author <TT>AutoDoc</TT>
 */

public class Instanceof extends TypeOperator {

    /**
     Instanceof.
     */

    public Instanceof() {}

    /**
     Instanceof.
     @param child an expression.
     @param typeref a type reference.
     */

    public Instanceof(Expression child, TypeReference typeref) {
        super(child, typeref);
        makeParentRoleValid();
    }

    /**
     Instanceof.
     @param proto an instanceof.
     */

    protected Instanceof(Instanceof proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Instanceof(this);
    }

    /**
     Returns the number of children of this node.
     @return an int giving the number of children of this node
    */

    public int getChildCount() {
        int result = 0;
        if (children != null) result += children.size();
        if (typeReference != null) result++;
        return result;
    }

    public SourceElement getLastElement() {
        return typeReference.getLastElement();
    }

    /**
     Returns the child at the specified index in this node's "virtual"
     child array
     @param index an index into this node's "virtual" child array
     @return the program element at the given position
     @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
                of bounds
    */

    public ProgramElement getChildAt(int index) {
        int len;
        if (children != null) {
            len = children.size();
            if (len > index) {
                return children.getProgramElement(index);
            }
            index -= len;
        }
        if (typeReference != null) {
            if (index == 0) return typeReference;
            index--;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     Get arity.
     @return the int value.
     */

    public int getArity() {
        return 1;
    }

    /**
     Get precedence.
     @return the int value.
     */

    public int getPrecedence() {
        return 5;
    }

    /**
     Get notation.
     @return the int value.
     */

    public int getNotation() {
        return POSTFIX;
    }

    public void accept(SourceVisitor v) {
        v.visitInstanceof(this);
    }
}
