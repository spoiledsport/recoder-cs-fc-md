// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration;

import recodercs.*;
import recodercs.abstraction.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.modifier.*;
import recodercs.list.*;


/**
 * TODO: Consider converting the DestructorDeclaration to a pure 
 * implementor of the MemberDeclaration interface. Currently it is a
 * subtype of method, which does not correspond to the grammar of the 
 * language.
 The getTypeReference method returns null - Destructors do not have
 explicite return types.  A Destructor declaration contains its own
 name even though it must match the class name: the name occurs as
 syntactical element and hence must be represented.  */

public class DestructorDeclaration extends MethodDeclaration 
                                     implements Destructor {

    
    
    /**
     Destructor declaration.
     */

    public DestructorDeclaration() {}

    /**
     Destructor declaration.
     @param modifier a visibility modifier.
     @param name an identifier.
     @param parameters a parameter declaration mutable list.
     @param exceptions a throws.
     @param body a statement block.
     */

    public DestructorDeclaration(VisibilityModifier modifier, Identifier name, ParameterDeclarationMutableList parameters, Throws exceptions, StatementBlock body) {
        super(null, null, name, parameters, exceptions, body);
        ModifierMutableList mods = new ModifierArrayList(1);
        if (mods != null) {
            mods.add(modifier);
            setModifiers(mods);
        }
    }

    /**
     Destructor declaration.
     @param proto a Destructor declaration.
     */

    protected DestructorDeclaration(DestructorDeclaration proto) {
        super(proto);
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new DestructorDeclaration(this);
    }

    /**
     * Destructors are never abstract.
     */

    public boolean isAbstract() {
        return false;
    }

    /**
     * Destructors are never final.
     */

    public boolean isSealed() {
        return false;
    }

    /**
     * Destructors are never static.
     */

    public boolean isStatic() {
        return false;
    }

    public void accept(SourceVisitor v) {
        v.visitDestructorDeclaration(this);
    }
    
    /**
     * Returns the statement
     */
	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		return super.getChildAt(index);
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		return super.getChildCount();
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		return super.getChildPositionCode(child);
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		super.makeParentRoleValid();
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		if (super.replaceChild(p, q)) return true;
		return false;
	}
	

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isInternal()
	 */
	public boolean isInternal() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isNew()
	 */
	public boolean isNew() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isOverride()
	 */
	public boolean isOverride() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isPrivate()
	 */
	public boolean isPrivate() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isProtected()
	 */
	public boolean isProtected() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isPublic()
	 */
	public boolean isPublic() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isVirtual()
	 */
	public boolean isVirtual() {
		return false;
	}

}

