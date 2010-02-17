// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp.declaration;

import recodercs.*;
import recodercs.csharp.*;
import recodercs.csharp.attributes.*;
import recodercs.csharp.declaration.modifier.*;
import recodercs.csharp.declaration.modifier.Override;
import recodercs.list.*;

/**
 Java declaration.
 @author <TT>AutoDoc</TT>
 */

public abstract class CSharpDeclaration
 extends CSharpNonTerminalProgramElement
 implements Declaration, AttributableElement {

	/**
	 * AttributeSections
	 */
	protected AttributeSectionMutableList attrSections;

    /**
     Modifiers.
     */

    protected ModifierMutableList modifiers;

    /**
     Java declaration.
     */

    public CSharpDeclaration() {}

    /**
     Java declaration.
     @param mods a modifier mutable list.
     */

    public CSharpDeclaration(ModifierMutableList mods) {
        setModifiers(mods);
    }

    /**
     Java declaration.
     @param proto a java declaration.
     */

    protected CSharpDeclaration(CSharpDeclaration proto) {
        super(proto);
        if (proto.modifiers != null) {
            modifiers = (ModifierMutableList)proto.modifiers.deepClone();
        }
		if (proto.attrSections != null) {
			attrSections = (AttributeSectionMutableList) proto.attrSections.deepClone();
		}
    }

    /**
     Get modifiers.
     @return the modifier mutable list.
     */

    public ModifierMutableList getModifiers() {
        return modifiers;
    }

    /**
     Set modifiers.
     @param m a modifier mutable list.
     */

    public void setModifiers(ModifierMutableList m) {
        modifiers = m;
    }

    /**
     Returns a Public, Protected, or Private Modifier, if there
     is one, null otherwise. A return value of null can usually be
     interpreted as package visibility.
     */

    public VisibilityModifier getVisibilityModifier() {
        if (modifiers == null) {
            return null;
        }
        for (int i = modifiers.size() - 1; i >= 0; i -= 1) {
            Modifier m = modifiers.getModifier(i);
            if (m instanceof VisibilityModifier) {
                return (VisibilityModifier)m;
            }
        }
        return null;
    }

    protected boolean containsModifier(Class type) {
        int s = (modifiers == null) ? 0 : modifiers.size();
        for (int i = 0; i < s; i += 1) {
            if (type.isInstance(modifiers.getModifier(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test whether the declaration is abstract.
     */

    protected boolean isAbstract() {
        return containsModifier(Abstract.class);
    }

    /**
     * Test whether the declaration is private.
     */

    protected boolean isPrivate() {
        return containsModifier(Private.class);
    }

    /**
     * Test whether the declaration is protected.
     */

    protected boolean isProtected() {
        return containsModifier(Protected.class);
    }

    /**
     * Test whether the declaration is public.
     */

    protected boolean isPublic() {
        return containsModifier(Public.class);
    }

    /**
     * Test whether the declaration is static.
     */

    protected boolean isStatic() {
        return containsModifier(Static.class);
    }

    /**
     * Test whether the declaration is transient.
     */

    protected boolean isInternal() {
        return containsModifier(Internal.class);
    }

    /**
     * Test whether the declaration is final.
     */

    protected boolean isSealed() {
        return containsModifier(Sealed.class);
    }

    /**
     * Test whether the declaration is extern.
     */

    protected boolean isExtern() {
        return containsModifier(Extern.class);
    }

    /**
     * Test whether the declaration is new.
     */

    protected boolean isNew() {
        return containsModifier(NewModifier.class);
    }
    
    protected boolean isOverride() {
        return containsModifier(Override.class);
    }
    
    protected boolean isVirtual() {
        return containsModifier(Virtual.class);
    }


    /**
     Make parent role valid.
     */

    public void makeParentRoleValid() {
        super.makeParentRoleValid();
        if (modifiers != null) {
            for (int i = modifiers.size() - 1; i >= 0; i -= 1) {
                modifiers.getModifier(i).setParent(this);
            }
        }
		if (attrSections != null) {
			for (int i = attrSections.size() - 1; i >= 0; i--) {
				attrSections.getAttributeSection(i).setParent(this);
			}
		}
    }
    
	/**
	 * @see recodercs.csharp.attributes.AttributableElement#getAttributeSectionAt(int)
	 */
	public AttributeSection getAttributeSectionAt(int index) {
		int len;
		if (attrSections != null) {
			len = attrSections.size();
			if (len > index) {
				return attrSections.getAttributeSection(index);
			}
			index -= len;
		}
	
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recodercs.csharp.attributes.AttributableElement#getAttributeSectionCount()
	 */
	public int getAttributeSectionCount() {
		int result = 0;
		if (attrSections != null)
			result += attrSections.size();
		return result;
	}

	/**
	 * @see recodercs.csharp.attributes.AttributableElement#setAttributeSections(AttributeSectionMutableList)
	 */
	public void setAttributeSections(AttributeSectionMutableList attrs) {
		attrSections = attrs;
		makeParentRoleValid();
	}

    
	/**
	 * @see recodercs.csharp.attributes.AttributableElement#getAttributeSections()
	 */
	public AttributeSectionList getAttributeSections() {
		return attrSections;
	}

	/**
	 * @see recodercs.csharp.SourceElement#getFirstElement()
	 */
	public SourceElement getFirstElement() {
		AttributeSectionList list = getAttributeSections();
		if (list != null && list.size()>0) {
			return list.getAttributeSection(0).getFirstElement();
		} else if (modifiers != null && !modifiers.isEmpty()) {
			return modifiers.getModifier(0);
		} 
		return super.getFirstElement();
	}


}
