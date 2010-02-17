// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.reference.*;
import recodercs.list.*;

/**
 Variable declaration.
 @author <TT>AutoDoc</TT>
 */

public abstract class VariableDeclaration
    extends CSharpDeclaration
    implements TypeReferenceContainer {

    /**
     Type reference.
     */

    protected TypeReference typeReference;

    /**
     Variable declaration.
     */

    public VariableDeclaration() {}

    /**
     Variable declaration.
     @param mods a modifier mutable list.
     @param typeRef a type reference.
     @param vars a variable specification mutable list.
     */

    public VariableDeclaration(ModifierMutableList mods, TypeReference typeRef) {
        setModifiers(mods);
        setTypeReference(typeRef);
    }

    /**
     Variable declaration.
     @param proto a variable declaration.
     */

    protected VariableDeclaration(VariableDeclaration proto) {
        super(proto);
        if (proto.typeReference != null) {
            typeReference = (TypeReference)proto.typeReference.deepClone();
        }
    }

    /**
     Make parent role valid.
     */

    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        if (typeReference != null) {
            typeReference.setParent(this);
        }
    }

    public SourceElement getFirstElement() {
        return getChildAt(0).getFirstElement();
    }

    public SourceElement getLastElement() {
        return getChildAt(getChildCount() - 1).getLastElement();
    }

    /**
     Get the number of type references in this container.
     @return the number of type references.
     */

    public int getTypeReferenceCount() {
        return (typeReference != null) ? 1 : 0;
    }

    /*
      Return the type reference at the specified index in this node's
      "virtual" type reference array.
      @param index an index for a type reference.
      @return the type reference with the given index.
      @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
      of bounds.
    */

    public TypeReference getTypeReferenceAt(int index) {
        if (typeReference != null && index == 0) {
            return typeReference;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     Get type reference.
     @return the type reference.
     */

    public TypeReference getTypeReference() {
        return typeReference;
    }

    /**
     Set type reference.
     @param t a type reference.
     */

    public void setTypeReference(TypeReference t) {
        typeReference = t;
    }

    /**
     Get variables.
     @return the variable specification mutable list.
     */

    public abstract VariableSpecificationList getVariables();
   
}
