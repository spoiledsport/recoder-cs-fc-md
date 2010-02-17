package recodercs.csharp;

import recodercs.csharp.reference.TypeReference;
import recodercs.csharp.reference.TypeReferenceContainer;

/**
 * @author kis
 * Class for holding a member name. It is an Identifier optionally combined 
 * with a TypeReference to an Interface type.
 */
public class MemberName
	extends CSharpNonTerminalProgramElement
	implements NamedProgramElement, TypeReferenceContainer, Reference {

	protected Identifier name;
	
	protected TypeReference interfaceType;
	
	MemberNameContainer parent;
	
	/**
	 * Constructor for MemberName.
	 */
	public MemberName() {
		super();
	}

	/**
	 * Constructor for MemberName.
	 * @param proto
	 */
	public MemberName(MemberName proto) {
		super(proto);
		if (proto.name != null) {
			this.name = (Identifier) proto.name.deepClone();
		}
		if (proto.interfaceType != null) {
			this.interfaceType = (TypeReference) proto.interfaceType.deepClone();
		}
		makeParentRoleValid();
	}

	/**
	 * Constructor MemberName.
	 * @param id
	 */
	public MemberName(Identifier id) {
		setIdentifier(id);
		makeParentRoleValid();
	}


	/**
	 * @see recodercs.csharp.NamedProgramElement#getIdentifier()
	 */
	public Identifier getIdentifier() {
		return name;
	}

	/**
	 * @see recodercs.csharp.NamedProgramElement#setIdentifier(Identifier)
	 */
	public void setIdentifier(Identifier id) {
		name = id;
		if (id != null) id.setParent(this);
	}

	/**
	 * @see recodercs.csharp.reference.TypeReferenceContainer#getTypeReferenceCount()
	 */
	public int getTypeReferenceCount() {
		return (interfaceType != null) ? 1 : 0;
	}

	/**
	 * @see recodercs.csharp.reference.TypeReferenceContainer#getTypeReferenceAt(int)
	 */
	public TypeReference getTypeReferenceAt(int index) {
		if (index == 0 && interfaceType != null) {
			return interfaceType;
		}
		throw new ArrayIndexOutOfBoundsException();
		
	}

	/**
	 * @see recodercs.NamedModelElement#getName()
	 */
	public String getName() {
		return (name != null) ? name.getText() : null;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildCount()
	 */
	public int getChildCount() {
		int result = 0;
		if (name != null) result ++;
		if (interfaceType != null) result ++;
		return result;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildAt(int)
	 */
	public ProgramElement getChildAt(int index) {
		if (name != null) {
			if (index == 0) return name;
			index --;
		}
		if	 (interfaceType != null) {
			if (index == 0) return interfaceType;
			index --;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#getChildPositionCode(ProgramElement)
	 */
	public int getChildPositionCode(ProgramElement child) {
		// role 0: name
		// role 1: interface type reference
		if (name == child) return 0;
		if (interfaceType == child) return 1;
		
		return -1;
	}

	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#replaceChild(ProgramElement, ProgramElement)
	 */
	public boolean replaceChild(ProgramElement p, ProgramElement q) {
		if (p == null) 
			throw new NullPointerException();
		if (name == p) {
			name = (Identifier) q;
			if (name != null) name.setParent(this);
			return true;
		}
		if (interfaceType == p) {
			interfaceType = (TypeReference) q;
			if (interfaceType != null) interfaceType.setParent(this); 
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
		v.visitMemberName(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#deepClone()
	 */
	public Object deepClone() {
		return new MemberName(this);
	}
	
	public void setMemberNameContainer(MemberNameContainer c) {
		parent = c;
	}
	
	public String getText() {
		StringBuffer result = new StringBuffer();
		if (interfaceType != null) {
			result.append(interfaceType.getName());
			result.append(".");
		}
		result.append(name.getText());
		return result.toString();
	}
	
	public void setInterfaceType(TypeReference t) {
		interfaceType = t;
	}
	/**
	 * @see recodercs.csharp.NonTerminalProgramElement#makeParentRoleValid()
	 */
	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		if (interfaceType != null) interfaceType.setParent(this);
		if (name != null) name.setParent(this);
	}

	/**
	 * @see recodercs.csharp.SourceElement#getFirstElement()
	 */
	public SourceElement getFirstElement() {
		if (interfaceType != null) {
			return interfaceType;
		}
		else if (name != null) {
			return name;
		}
		return this;
	}

	/**
	 * @see recodercs.csharp.SourceElement#getLastElement()
	 */
	public SourceElement getLastElement() {
		if (name != null) {
			return name;
		} 
		else if (interfaceType != null) {
			return interfaceType;
		}
		return this;
	}

	/**
	 * Returns the interfaceType.
	 * @return TypeReference
	 */
	public TypeReference getInterfaceType() {
		return interfaceType;
	}

}
