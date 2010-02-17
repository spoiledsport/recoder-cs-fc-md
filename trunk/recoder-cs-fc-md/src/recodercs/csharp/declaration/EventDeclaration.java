package recodercs.csharp.declaration;

import recodercs.csharp.Expression;
import recodercs.csharp.Identifier;
import recodercs.csharp.MemberName;
import recodercs.csharp.MemberNameContainer;
import recodercs.csharp.NamedProgramElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.SourceElement;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.Statement;
import recodercs.csharp.StatementBlock;
import recodercs.csharp.StatementContainer;
import recodercs.csharp.attributes.AttributeSection;
import recodercs.csharp.attributes.AttributeSectionMutableList;
import recodercs.csharp.reference.TypeReference;
import recodercs.list.FieldSpecificationMutableList;
import recodercs.list.ModifierMutableList;

/**
 * @author kis
 *
 * The EventDeclaration class represents the event construct of the C# 
 * language. 
 * 
 */
 // TODO: Investigate whether the current implementation is correct. 
 // Currently the implementation extends the FieldDeclaration class.
 
 // YES, it is correct, accoridng to the ECMA spec event is a field.

public class EventDeclaration
	extends FieldDeclaration
	implements AccessorContainer {


	/**
	 * Contains the statementblock for add operation.
	 */
	protected AddAccessor addBlock;

	/**
	 * Contains the statementblock for remove operation.
	 */
	protected RemoveAccessor removeBlock;

	/**
	 * Constructor for EventDeclaration.
	 */
	public EventDeclaration() {
		super();
	}

	/**
	 * Constructor for EventDeclaration.
	 * @param proto
	 */
	public EventDeclaration(EventDeclaration proto) {
		super(proto);
		if (proto.attrSections != null) {
			attrSections =
				(AttributeSectionMutableList) proto.attrSections.deepClone();
		}
		if (proto.addBlock != null) {
			this.addBlock = (AddAccessor) proto.addBlock.deepClone();
		}
		if (proto.removeBlock != null) {
			this.removeBlock = (RemoveAccessor) proto.removeBlock.deepClone();
		}
		makeParentRoleValid();
	}

	/**
	 * @see recodercs.csharp.StatementContainer#getStatementCount()
	 */
	public int getAccessorCount() {
		int result = 0;
		if (addBlock != null)
			result++;
		if (removeBlock != null)
			result++;
		return result;
	}

	/**
	 * @see recodercs.csharp.StatementContainer#getStatementAt(int)
	 */
	public Accessor getAccessorAt(int index) {
		if (addBlock != null) {
			if (index == 0)
				return addBlock;
			index--;
		}
		if (removeBlock != null) {
			if (index == 0)
				return addBlock;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		int result = 0;
		if (attrSections != null)
			result += attrSections.size();
		if (modifiers != null)
			result += modifiers.size();
		if (typeReference != null)
			result++;
		if (addBlock != null)
			result++;
		if (removeBlock != null)
			result++;
		if (fieldSpecs != null)
			result += fieldSpecs.size();

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
			if (index == 0)
				return typeReference;
			index--;
		}
		if (addBlock != null) {
			if (index == 0)
				return addBlock;
			index--;
		}
		if (removeBlock != null) {
			if (index == 0)
				return removeBlock;
			index--;
		}
		if (fieldSpecs != null) {
			len = fieldSpecs.size();
			if (len > index) {
				return fieldSpecs.getProgramElement(index);
			}
			index -= len;
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
		// role 5: name
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
		if (addBlock == child) {
			return 0 << 4 | 4;
		}
		if (addBlock == child) {
			return 1 << 4 | 4;
		}
		if (fieldSpecs != null) {
			int index = fieldSpecs.indexOf(child);
			if (index >= 0) {
				return (index << 4) | 2;
			}
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
					Modifier r = (Modifier) q;
					modifiers.set(i, r);
					r.setParent(this);
				}
				return true;
			}
		}
		if (typeReference == p) {
			TypeReference r = (TypeReference) q;
			typeReference = r;
			if (r != null) {
				r.setParent(this);
			}
			return true;
		}


		if (removeBlock == p) {
			RemoveAccessor r = (RemoveAccessor) q;
			removeBlock = r;
			if (r != null) {
				r.setAccessorContainer(this);
			}
			return true;
		}
		if (addBlock == p) {
			AddAccessor r = (AddAccessor) q;
			addBlock = r;
			if (r != null) {
				r.setAccessorContainer(this);
			}
			return true;
		}

		return false;

	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitEventDeclaration(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new EventDeclaration(this);
	}



	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		try {
			addBlock.setAccessorContainer(this);
		} catch (NullPointerException nex) {
		}
		try {
			removeBlock.setAccessorContainer(this);
		} catch (NullPointerException nex) {
		}
	}

	public AddAccessor getAddAccessor() {
		return addBlock;
	}
	public RemoveAccessor getRemoveAccessor() {
		return removeBlock;
	}

	public void setAddAccessor(AddAccessor s) {
		addBlock = s;
		makeParentRoleValid();
	}

	public void setRemoveAccessor(RemoveAccessor s) {
		removeBlock = s;
		makeParentRoleValid();
	}

	public boolean hasAddAccessor() {
		return (addBlock != null);
	}

	public boolean hasRemoveAccessor() {
		return (removeBlock != null);
	}

	/** Returns true, if the defined event is a property-like event (event with
	 *  add and remove accessors.
	 */
	public boolean isPropertyLike() {
		return hasAddAccessor() || hasRemoveAccessor();
	}

	/**
	 * @see recodercs.csharp.SourceElement#getFirstElement()
	 */
	public SourceElement getFirstElement() {
		if (getModifiers() != null && getModifiers().size() >0) {
			return getModifiers().getModifier(0);
		}
		return this;
	}

	/**
	 * @see recodercs.csharp.SourceElement#getLastElement()
	 */
	public SourceElement getLastElement() {
		if (! (hasAddAccessor() || hasRemoveAccessor())) {
			if (getFieldSpecifications() != null && getFieldSpecifications().size()>0) {
				return getFieldSpecifications().getFieldSpecification(getFieldSpecifications().size()-1);
			}
		}
		return this;
	}

}
