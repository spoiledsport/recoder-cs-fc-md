package recoder.csharp.declaration;

import recoder.csharp.Identifier;
import recoder.csharp.MemberName;
import recoder.csharp.MemberNameContainer;
import recoder.csharp.NamedProgramElement;
import recoder.csharp.NonTerminalProgramElement;
import recoder.csharp.ProgramElement;
import recoder.csharp.SourceElement;
import recoder.csharp.SourceVisitor;
import recoder.csharp.Statement;
import recoder.csharp.StatementBlock;
import recoder.csharp.StatementContainer;
import recoder.csharp.attributes.AttributeSection;
import recoder.csharp.attributes.AttributeSectionMutableList;
import recoder.csharp.reference.TypeReference;
import recoder.list.FieldSpecificationArrayList;
import recoder.list.FieldSpecificationMutableList;
import recoder.list.ModifierMutableList;
import recoder.list.VariableSpecificationArrayList;
import recoder.list.VariableSpecificationList;
import recoder.list.VariableSpecificationMutableList;

/**
 * @author kis
 * This class represent the property declaration construct of the 
 * C# language.
 */
public class PropertyDeclaration
	extends FieldDeclaration
	implements AccessorContainer
	 {

	
	protected GetAccessor getBlock;
	protected SetAccessor setBlock;

	protected PropertySpecification propSpec;


//    /**
//     Parent.
//     */
//
//    protected TypeDeclaration parent;

	/**
	 * Constructor for PropertyDeclaration.
	 */
	public PropertyDeclaration() {
		super();
	}

	/**
	 * Constructor for PropertyDeclaration.
	 * @param mods
	 * @param typeRef
	 */
	public PropertyDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef) {
		super();
		setModifiers(mods);
		setTypeReference(typeRef);
		makeParentRoleValid();
	}

	/**
	 * Constructor for PropertyDeclaration.
	 * @param proto
	 */
	public PropertyDeclaration(PropertyDeclaration proto) {
		super(proto);
		if (proto.attrSections != null) {
			attrSections =  (AttributeSectionMutableList) proto.attrSections.deepClone();
		}
		if (proto.getBlock != null) {
			this.getBlock = (GetAccessor) proto.getBlock.deepClone();
		}
		if (proto.setBlock != null) {
			this.setBlock = (SetAccessor) proto.setBlock.deepClone();
		}
		if (proto.propSpec != null) {
			this.propSpec = (PropertySpecification) proto.propSpec.deepClone();
		}
		makeParentRoleValid();
	}

	/**
	 * @see recoder.csharp.declaration.VariableDeclaration#getVariables()
	 */
	public VariableSpecificationList getVariables() {
		VariableSpecificationMutableList l = new VariableSpecificationArrayList();
 		if (propSpec != null) l.add((VariableSpecification)propSpec);
		return l;
	}


	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
        int result = 0;
        if (attrSections != null)
			result += attrSections.size();
        if (modifiers     != null) result += modifiers.size();
        if (typeReference != null) result++;
		if (getBlock != null) result++;
		if (setBlock != null) result++;		
		if (propSpec != null) result++;
        
		return result;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
        int len;
        if (attrSections != null) {
			len = attrSections.size();
			if (len > index) {
				return attrSections.getAttributeSection(index);
			}
			index -= len;
		}
        if (modifiers != null) {
            len = modifiers.size();
            if (len > index) {
                return modifiers.getProgramElement(index);
            }
            index -= len;
        }
        if (typeReference != null) {
            if (index == 0) return typeReference;
            index--;
        }
        if (propSpec != null) {
            if (index == 0) return propSpec;
            index--;
        }
		if (getBlock != null) {
			if (index==0) return getBlock;
			index--;
		}
		if (setBlock != null) {
			if (index==0) return setBlock;
			index--;
		}
        throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
        // role 0 (IDX): modifier
        // role 1: type reference
        // role 2 (IDX): var specs
        // role 4: (IDX): getblock setblock 
        // role 5: property specification
        // role 15: attributes
        if (attrSections != null) {
			int index = attrSections.indexOf(child);
			if (index >= 0) {
				return (index << 4) | 15;
			}
		}
        if (modifiers != null) {
            int index = modifiers.indexOf(child);
            if (index >= 0) {
                return (index << 4) | 0;
            }
        }
        if (typeReference == child) {
            return 1;
        }
        if (getBlock == child) {
            return 0 << 4 | 4;
        }
        if (getBlock == child) {
            return 1 << 4 | 4;
        }
        if (propSpec == child) {
            return 5;
        }
        return -1;
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
        if (p == null) {
            throw new NullPointerException();
        }
        int count;
        count = (attrSections == null) ? 0 : attrSections.size();
		for (int i = 0; i < count; i++) {
			if (attrSections.getAttributeSection(i) == p) {
				if (q == null) {
					attrSections.remove(i);
				} else {
					AttributeSection r = (AttributeSection) q;
					attrSections.set(i, r);
					r.setParent(this);
				}
				return true;
			}
		}
        count = (modifiers == null) ? 0 : modifiers.size();
        for (int i = 0; i < count; i++) {
            if (modifiers.getProgramElement(i) == p) {
                if (q == null) {
                    modifiers.remove(i);
                } else {
                    Modifier r = (Modifier)q;
                    modifiers.set(i, r);
                    r.setParent(this);
                }
                return true;
            }
        }
        if (typeReference == p) {
            TypeReference r = (TypeReference)q;
            typeReference = r;
            if (r != null) {
                r.setParent(this);
            }
            return true;
        }

		if (propSpec == p) {
            PropertySpecification r = (PropertySpecification)q;
            propSpec = r;
            if (r != null) {
                r.setParent(this);
            }
            return true;
		}

		if (setBlock == p) {
            SetAccessor r = (SetAccessor)q;
            setBlock = r;
            if (r != null) {
                r.setAccessorContainer(this);
            }
            return true;
		}
		if (getBlock == p) {
            GetAccessor r = (GetAccessor)q;
            getBlock = r;
            if (r != null) {
                r.setAccessorContainer(this);
            }
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
		v.visitPropertyDeclaration(this);
	}

	/**
	 * @see recoder.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new PropertyDeclaration(this);
	}

	/**
	 * @see recoder.csharp.declaration.CSharpDeclaration#isPrivate()
	 */
	public boolean isPrivate() {
		return super.isPrivate();
	}

	/**
	 * @see recoder.csharp.declaration.CSharpDeclaration#isProtected()
	 */
	public boolean isProtected() {
		return super.isProtected();
	}

	/**
	 * @see recoder.csharp.declaration.CSharpDeclaration#isPublic()
	 */
	public boolean isPublic() {
		return super.isPublic();
	}

	/**
	 * @see recoder.csharp.declaration.CSharpDeclaration#isStatic()
	 */
	public boolean isStatic() {
		return super.isStatic();
	}

	/**
	 * @see recoder.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		try {
			propSpec.setParent(this);
		} catch (NullPointerException nex) {}
		try {
			getBlock.setAccessorContainer(this);
		} catch (NullPointerException nex) {}
		try {
			setBlock.setAccessorContainer(this);
		} catch (NullPointerException nex) {}
	}
	
	public boolean hasGetAccessor() {
		return (getBlock != null);
	}
	
	public boolean hasSetAccessor() {
		return (setBlock != null);
	}

	/**
	 * @see recoder.csharp.declaration.AccessorContainer#getAccessorAt(int)
	 */
	public Accessor getAccessorAt(int index) {
		if (getBlock != null) {
			if (index == 0) return getBlock;
			index --;
		}
		if (setBlock != null) {
			if (index == 0) return setBlock;
			index --;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recoder.csharp.declaration.AccessorContainer#getAccessorCount()
	 */
	public int getAccessorCount() {
		int result = 0;
		if (getBlock != null) result ++;
		if (setBlock != null) result ++;
		return result;
	}

	public void setGetAccessor(GetAccessor s) { 
		getBlock = s; 
		makeParentRoleValid();
	}
	
	public void setSetAccessor(SetAccessor s) { 
		setBlock = s; 
		makeParentRoleValid();
	}


	/**
	 * Returns the getBlock.
	 * @return GetAccessor
	 */
	public GetAccessor getGetAccessor() {
		return getBlock;
	}

	/**
	 * Returns the setBlock.
	 * @return SetAccessor
	 */
	public SetAccessor getSetAccessor() {
		return setBlock;
	}

	/**
	 * @see recoder.csharp.declaration.MemberDeclaration#isInternal()
	 */
	public boolean isInternal() {
		return super.isInternal();
	}

	/**
	 * @see recoder.csharp.declaration.MemberDeclaration#isNew()
	 */
	public boolean isNew() {
		return super.isNew();
	}

	/**
	 * @see recoder.csharp.declaration.MemberDeclaration#isSealed()
	 */
	public boolean isSealed() {
		return super.isSealed();
	}

	/**
	 * @see recoder.csharp.declaration.MemberDeclaration#isOverride()
	 */
	public boolean isOverride() {
		return super.isOverride();
	}
	/**
	 * @see recoder.csharp.declaration.MemberDeclaration#isAbstract()
	 */
	public boolean isAbstract() {
		return super.isAbstract();
	}

	public void setPropertySpecification(PropertySpecification p) {
		propSpec = p;
		makeParentRoleValid();
	}
	
	public PropertySpecification getPropertySpecification() {
		return propSpec;
	}


	/**
	 * @see recoder.csharp.declaration.FieldDeclaration#getFieldSpecifications()
	 */
	public FieldSpecificationMutableList getFieldSpecifications() {
		FieldSpecificationMutableList l = new FieldSpecificationArrayList();
		if (propSpec != null) l.add(propSpec);
		return l;
		
	}

	/**
	 * @see recoder.csharp.SourceElement#getLastElement()
	 */
	public SourceElement getLastElement() {
		return this;
	}

}
