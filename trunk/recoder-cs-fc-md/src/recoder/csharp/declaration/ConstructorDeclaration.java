// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp.declaration;

import recoder.*;
import recoder.abstraction.*;
import recoder.list.*;
import recoder.csharp.*;
import recoder.csharp.declaration.modifier.*;
import recoder.csharp.reference.SpecialConstructorReference;

/**
 * TODO: Consider converting the ConstructorDeclaration to a pure 
 * implementor of the MemberDeclaration interface. Currently it is a
 * subtype of method, which does not correspond to the grammar of the 
 * language.
 * 
 The getTypeReference method returns null - constructors do not have
 explicite return types.  A constructor declaration contains its own
 name even though it must match the class name: the name occurs as
 syntactical element and hence must be represented.  
 
*/

public class ConstructorDeclaration extends MethodDeclaration 
                                     implements Constructor, StatementContainer  {

    
    /** Special constructor reference */
    Statement specialConstructorReference;
    
    /**
     Constructor declaration.
     */

    public ConstructorDeclaration() {}

    /**
     Constructor declaration.
     @param modifier a visibility modifier.
     @param name an identifier.
     @param parameters a parameter declaration mutable list.
     @param exceptions a throws.
     @param body a statement block.
     */

    public ConstructorDeclaration(VisibilityModifier modifier, Identifier name, ParameterDeclarationMutableList parameters, Throws exceptions, StatementBlock body) {
        super(null, null, name, parameters, exceptions, body);
        ModifierMutableList mods = new ModifierArrayList(1);
        if (mods != null) {
            mods.add(modifier);
            setModifiers(mods);
        }
    }

    /**
     Constructor declaration.
     @param proto a constructor declaration.
     */

    protected ConstructorDeclaration(ConstructorDeclaration proto) {
        super(proto);
        if (proto.specialConstructorReference != null) {
            specialConstructorReference = (Statement)proto.specialConstructorReference.deepClone();
        }
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new ConstructorDeclaration(this);
    }

    /**
     * Constructors are never abstract.
     */

    public boolean isAbstract() {
        return false;
    }

    /**
     * Constructors are never final.
     */

    public boolean isSealed() {
        return false;
    }

    /**
     * Constructors are never static.
     */

    public boolean isStatic() {
        return false;
    }

	/**
	 * @see recoder.csharp.declaration.CSharpDeclaration#isNew()
	 */
	public boolean isNew() {
		return false;
	}

	/**
	 * @see recoder.csharp.declaration.CSharpDeclaration#isVirtual()
	 */
	public boolean isVirtual() {
		return false;
	}

    public void accept(SourceVisitor v) {
        v.visitConstructorDeclaration(this);
    }
    
    /**
     * Returns the statement
     */
	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		if (specialConstructorReference != null) {
			if (index == 0) return specialConstructorReference;
			index --;
		}
		return super.getChildAt(index);
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		return super.getChildCount()+ ( specialConstructorReference == null ? 0 : 1);
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		// role 14: special constructor reference
		if (specialConstructorReference == child) return 14;
		
		return super.getChildPositionCode(child);
	}

// THESE methods are no longer overridden. 
//    /**
//     Get the number of statements in this container.
//     @return the number of statements.
//     */
//    public int getStatementCount() {
//    	return specialConstructorReference == null ? 0 : 1;
//    }
//
//    /*
//      Return the statement at the specified index in this node's
//      "virtual" statement array.
//      @param index an index for a statement.
//      @return the statement with the given index.
//      @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
//      of bounds.
//    */
//    public Statement getStatementAt(int index) {
//    	if (index == 0 && specialConstructorReference != null) return specialConstructorReference;
//    	throw new ArrayIndexOutOfBoundsException();
//    }


	/**
	 * @see recoder.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		if (specialConstructorReference != null) specialConstructorReference.setStatementContainer(this);
		super.makeParentRoleValid();
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		if (super.replaceChild(p, q)) return true;
		if ( p == specialConstructorReference) {
			specialConstructorReference = (Statement) q;	
			if (specialConstructorReference != null) specialConstructorReference.setStatementContainer(this);
			return true;
		}
		return false;
	}
	
	/** Sets the special constructor reference */
	public void setSpecialConstructorReference(Statement st) {
		specialConstructorReference = st;
		makeParentRoleValid();
	}

	/**
	 * Returns the specialConstructorReference.
	 * @return Statement
	 */
	public Statement getSpecialConstructorReference() {
		return specialConstructorReference;
	}


}

