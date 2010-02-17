package recodercs.csharp.attributes;

import recodercs.csharp.*;
import recodercs.list.ProgramElementList;

/**
 * @author orosz
 * Represents an AttributeSection in a C# compilation unit.
 */
public class AttributeSection extends CSharpNonTerminalProgramElement
							   implements AttributeContainer {
	/**
	 * Constructor AttributeSection.
	 * @param attributeSection
	 */
	public AttributeSection(AttributeSection proto) {
		if (proto.attributes != null) attributes = (AttributeMutableList) proto.attributes.deepClone();
		if (proto.target != null) target = (AttributeTarget) proto.target.deepClone();
		makeParentRoleValid();		
	}

	/** Default constructor. */
	public AttributeSection() {}

	/**
	 * Constructor AttributeSection.
	 * @param target
	 */
	public AttributeSection(GlobalAttributeTarget target) {
		this.target = target;
		makeParentRoleValid();
	}


	/** Attributes defined in this attribute section. */
	protected AttributeMutableList attributes;

	/** Target specifier. These packages can be found in the @see recoder.csharp.attributes.targets package. */
	protected AttributeTarget target;

	/** The parent in the AST is marked up by an interface. */
	protected AttributableElement parent;	

	/** 

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {		
		if (attributes != null) {
			for (int i=0; i<attributes.size(); i++) attributes.getAttribute(i).setParent(this);
		}		
		if (target != null) target.setParent(this);
		super.makeParentRoleValid();
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		if (target != null) {
			if (index == 0) return target;
			index--;
		}
		if (attributes != null) {
			int len = attributes.size();
			if (index < len) return attributes.getAttribute(index);
			index -= len;
		}
		throw new IndexOutOfBoundsException();
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		int result = 0;
		if (target != null) result++;
		if (attributes != null) result += attributes.size();
		return result;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		
		if (target != null && child == target) return 0;
		
		for (int idx=0; idx < attributes.size(); idx++)
        if (attributes != null) {
            int index = attributes.indexOf(child);
            if (index >= 0) {
                return (index << 4) | 1;
            }
        }
        
        return -1;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {

		if (p==null) throw new NullPointerException();
		
        int count;
        
        if (target == p) {
        	target = (AttributeTarget) q;
        	if (target != null) target.setParent(this);
        	return true;
        }
        
        count = (attributes == null) ? 0 : attributes.size();
        for (int i = 0; i < count; i++) {
            if (attributes.getAttribute(i) == p) {
                if (q == null) {
                    attributes.remove(i);
                } else {
                    Attribute r = (Attribute)q;
                    attributes.set(i, r);
                    r.setParent(this);
                }
                return true;
            }
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
		v.visitAttributeSection(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new AttributeSection(this);
	}

	/**
	 * @see recodercs.csharp.attributes.AttributeContainer#getAttributeAt(int)
	 */
	public Attribute getAttributeAt(int idx) {
		if (attributes != null) return attributes.getAttribute(idx);
		throw new IndexOutOfBoundsException();
	}

	/**
	 * @see recodercs.csharp.attributes.AttributeContainer#getAttributeCount()
	 */
	public int getAttributeCount() {
		if (attributes != null) return attributes.size();
		return 0;
	}

	/**
	 * Sets the attributes.
	 * @param attributes The attributes to set
	 */
	public void setAttributes(AttributeMutableList sections) {
		this.attributes = sections;
	}

	/**
	 * Returns the parent.
	 * @return AttributableElement
	 */
	public AttributableElement getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * @param parent The parent to set
	 */
	public void setParent(AttributableElement parent) {
		this.parent = parent;
	}

	/**
	 * Returns the target.
	 * @return AttributeTarget
	 */
	public AttributeTarget getTarget() {
		return target;
	}

	/**
	 * Sets the target.
	 * @param target The target to set
	 */
	public void setTarget(AttributeTarget target) {
		this.target = target;
	}

	/**
	 * Method getAttributes.
	 * @return ProgramElementList
	 */
	public AttributeList getAttributes() {
		return attributes;
	}

}
