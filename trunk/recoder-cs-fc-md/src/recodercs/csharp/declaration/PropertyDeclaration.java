package recodercs.csharp.declaration;

import recodercs.csharp.Identifier;
import recodercs.csharp.MemberName;
import recodercs.csharp.MemberNameContainer;
import recodercs.csharp.NamedProgramElement;
import recodercs.csharp.NonTerminalProgramElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.SourceElement;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.Statement;
import recodercs.csharp.StatementBlock;
import recodercs.csharp.StatementContainer;
import recodercs.csharp.attributes.AttributeSection;
import recodercs.csharp.attributes.AttributeSectionMutableList;
import recodercs.csharp.reference.TypeReference;
import recodercs.list.FieldSpecificationArrayList;
import recodercs.list.FieldSpecificationMutableList;
import recodercs.list.ModifierMutableList;
import recodercs.list.VariableSpecificationArrayList;
import recodercs.list.VariableSpecificationList;
import recodercs.list.VariableSpecificationMutableList;

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
	 * @see recodercs.csharp.declaration.VariableDeclaration#getVariables()
	 */
	public VariableSpecificationList getVariables() {
		VariableSpecificationMutableList l = new VariableSpecificationArrayList();
 		if (propSpec != null) l.add((VariableSpecification)propSpec);
		return l;
	}


	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildCount()
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
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildAt(int)
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
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
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
	 * @see recodercs.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
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
	 * @see recodercs.csharp.ProgramElement#getASTParent()
	 */
	public NonTerminalProgramElement getASTParent() {
		return parent;
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitPropertyDeclaration(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new PropertyDeclaration(this);
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isPrivate()
	 */
	public boolean isPrivate() {
		return super.isPrivate();
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isProtected()
	 */
	public boolean isProtected() {
		return super.isProtected();
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isPublic()
	 */
	public boolean isPublic() {
		return super.isPublic();
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isStatic()
	 */
	public boolean isStatic() {
		return super.isStatic();
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#makeParentRoleValid()
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
	 * @see recodercs.csharp.declaration.AccessorContainer#getAccessorAt(int)
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
	 * @see recodercs.csharp.declaration.AccessorContainer#getAccessorCount()
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
	 * @see recodercs.csharp.declaration.MemberDeclaration#isInternal()
	 */
	public boolean isInternal() {
		return super.isInternal();
	}

	/**
	 * @see recodercs.csharp.declaration.MemberDeclaration#isNew()
	 */
	public boolean isNew() {
		return super.isNew();
	}

	/**
	 * @see recodercs.csharp.declaration.MemberDeclaration#isSealed()
	 */
	public boolean isSealed() {
		return super.isSealed();
	}

	/**
	 * @see recodercs.csharp.declaration.MemberDeclaration#isOverride()
	 */
	public boolean isOverride() {
		return super.isOverride();
	}
	/**
	 * @see recodercs.csharp.declaration.MemberDeclaration#isAbstract()
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
	 * @see recodercs.csharp.declaration.FieldDeclaration#getFieldSpecifications()
	 */
	public FieldSpecificationMutableList getFieldSpecifications() {
		FieldSpecificationMutableList l = new FieldSpecificationArrayList();
		if (propSpec != null) l.add(propSpec);
		return l;
		
	}

	/**
	 * @see recodercs.csharp.SourceElement#getLastElement()
	 */
	public SourceElement getLastElement() {
		return this;
	}

}
