package recoder.csharp.attributes;

import recoder.csharp.reference.*;
import recoder.csharp.statement.Return;
import recoder.list.ProgramElementList;
import recoder.csharp.*;

/**
 * @author orosz
 *
 * Represents an Attribute usage in a C# program.
 */
public class Attribute extends CSharpNonTerminalProgramElement
						implements TypeReferenceContainer, AttributeArgumentContainer
{
	/**
	 * Constructor Attribute.
	 * @param attribute
	 */
	public Attribute(Attribute proto) {
		if (proto.ref != null) ref = (TypeReference) proto.ref.deepClone();
		if (proto.arguments != null) arguments = (AttributeArgumentMutableList) proto.arguments.deepClone();
		makeParentRoleValid();
	}

	/**
	 * Constructor Attribute.
	 */
	public Attribute() {
	}


	/** The type reference */
	protected TypeReference ref;
	
	/** The parent, which contains this attribute. */
	protected AttributeContainer parent;
	
	/** The parameters of this attribute. */
	protected AttributeArgumentMutableList arguments;


	/**
	 * @see recoder.csharp.reference.TypeReferenceContainer#getTypeReferenceAt(int)
	 */
	public TypeReference getTypeReferenceAt(int index) {
		if (index == 0 && ref != null) return ref;
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recoder.csharp.reference.TypeReferenceContainer#getTypeReferenceCount()
	 */
	public int getTypeReferenceCount() {
		return ( ref == null ? 0 : 1);
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		if (ref != null) {
			if (index == 0) return ref;
			index--;
		}
		if (arguments != null) {
			if (index < arguments.size()) return arguments.getAttributeArgument(index);
			index -= arguments.size();
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		int result = getTypeReferenceCount();
		if (arguments != null) result+=arguments.size();
		return result;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		if (child == ref) return 0;
		for (int idx=0; idx < arguments.size(); idx++)
        if (arguments != null) {
            int index = arguments.indexOf(child);
            if (index >= 0) {
                return (index << 4) | 1;
            }
        }
        return -1;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		if (p==null) throw new NullPointerException();
		
        int count;
        
        count = (arguments == null) ? 0 : arguments.size();
        for (int i = 0; i < count; i++) {
            if (arguments.getAttributeArgument(i) == p) {
                if (q == null) {
                    arguments.remove(i);
                } else {
                    AttributeArgument r = (AttributeArgument)q;
                    arguments.set(i, r);
                    r.setParent(this);
                }
                return true;
            }
        }
        
        if (p == ref) {
        	ref = (TypeReference) q;
        	if (ref != null) makeParentRoleValid();
        	return true;
        }
        
        return false;
        
	}

	/**
	 * @see recoder.csharp.ProgramElement#getASTParent()
	 */
	public NonTerminalProgramElement getASTParent() {
		return parent;
	}

	/**
	 * @see recoder.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitAttribute(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new Attribute(this);
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (ref != null) ref.setParent(this);
		if (arguments != null) {
			for (int i=0; i<arguments.size(); i++) arguments.getAttributeArgument(i).setParent(this);
		}
	}

	/**
	 * @see recoder.csharp.attributes.AttributeArgumentContainer#getAttributeParameterAt()
	 */
	public AttributeArgument getAttributeParameterAt(int idx) {
		if (arguments != null) return arguments.getAttributeArgument(idx);
		throw new IndexOutOfBoundsException();
	}

	/**
	 * @see recoder.csharp.attributes.AttributeArgumentContainer#getAttributeParameterCount()
	 */
	public int getAttributeParameterCount() {
		if (arguments != null) return arguments.size();
		return 0;
	}

	/**
	 * Returns the parent.
	 * @return AttributeContainer
	 */
	public AttributeContainer getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * @param parent The parent to set
	 */
	public void setParent(AttributeContainer parent) {
		this.parent = parent;
	}

	/**
	 * Sets the arguments.
	 * @param arguments The arguments to set
	 */
	public void setArguments(AttributeArgumentMutableList params) {
		this.arguments = params;
	}

	/**
	 * Sets the ref.
	 * @param ref The ref to set
	 */
	public void setTypeReference(TypeReference ref) {
		this.ref = ref;
	}

	/**
	 * Method getAttributeArguments.
	 * @return ProgramElementList
	 */
	public AttributeArgumentList getAttributeArguments() {
		return arguments;
	}

}
