// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp;

import java.util.Enumeration;

import recoder.*;
import recoder.list.*;
import recoder.util.Debug;
import recoder.util.MutableMap;
import recoder.util.NaturalHashTable;
import recoder.abstraction.DeclaredType;
import recoder.csharp.attributes.AttributableElement;
import recoder.csharp.attributes.AttributeSection;
import recoder.csharp.attributes.AttributeSectionList;
import recoder.csharp.attributes.AttributeSectionMutableList;
import recoder.csharp.declaration.*;
import recoder.csharp.reference.*;

/**
 Namespace specification.
 @author <TT>AutoDoc</TT>
 */

public class NamespaceSpecification
	extends CSharpNonTerminalProgramElement
	implements
		NamespaceReferenceContainer,
		NamespaceSpecificationContainer,
		TypeDeclarationContainer,
		AttributableElement
		, TypeScope
{

	/** Other namespaces inside this namespace */
	protected NamespaceSpecificationMutableList namespaces;

	/**
	 Imports.
	 */

	protected UsingMutableList usings;

	/**
	 Type declarations.
	 */

	protected TypeDeclarationMutableList typeDeclarations;

	/**
	 Parent.
	 */
	protected NamespaceSpecificationContainer parent;

	/**
	 Reference.
	 */
	protected NamespaceReference reference;

	/**
	   Undefined scope tag.
	 */
	protected final static MutableMap UNDEFINED_SCOPE = new NaturalHashTable();

	/**
	   Scope table for types.
	*/
	protected MutableMap name2type = UNDEFINED_SCOPE;



	/**
	 Namespace specification.
	 */
	public NamespaceSpecification() {
	}

	/**
	 Namespace specification.
	 @param pkg a package reference.
	 */

	public NamespaceSpecification(NamespaceReference pkg) {
		setNamespaceReference(pkg);
		makeParentRoleValid();
	}

	/**
	 Namespace specification.
	 @param proto a package specification.
	 */

	protected NamespaceSpecification(NamespaceSpecification proto) {
		super(proto);
		if (proto.reference != null) {
			reference = (NamespaceReference) proto.reference.deepClone();
		}
		if (proto.namespaces != null) {
			namespaces =
				(NamespaceSpecificationMutableList) proto
					.namespaces
					.deepClone();
		}
		if (proto.typeDeclarations != null) {
			typeDeclarations =
				(TypeDeclarationMutableList) proto.typeDeclarations.deepClone();
		}
		if (proto.usings != null) {
			usings = (UsingMutableList) proto.usings.deepClone();
		}
		if (proto.attrSections != null) {
			attrSections =
				(AttributeSectionMutableList) proto.attrSections.deepClone();
		}
		makeParentRoleValid();
	}

	/**
	 Deep clone.
	 @return the object.
	 */

	public Object deepClone() {
		return new NamespaceSpecification(this);
	}

	/**
	 Make parent role valid.
	 */

	public void makeParentRoleValid() {
		super.makeParentRoleValid();
		reference.setParent(this);
		if (namespaces != null) {
			for (int i = 0; i < namespaces.size(); i++)
				namespaces.getNamespaceSpecification(i).setParent(this);
		}
		if (typeDeclarations != null) {
			for (int i = 0; i < typeDeclarations.size(); i++)
				typeDeclarations.getTypeDeclaration(i).setParent(this);
		}
		if (usings != null) {
			for (int i = 0; i < usings.size(); i++)
				usings.getUsing(i).setParent(this);
		}
	}

	public SourceElement getLastElement() {
		return this;
	}

	/**
	 Get AST parent.
	 @return the non terminal program element.
	 */

	public NonTerminalProgramElement getASTParent() {
		return parent;
	}

	/**
	 Returns the number of children of this node.
	 @return an int giving the number of children of this node
	*/

	public int getChildCount() {
		// A namespace has always an identifier
		int result = 1;
		if (attrSections != null)
			result += attrSections.size();
		if (namespaces != null)
			result += namespaces.size();
		if (usings != null)
			result += usings.size();
		if (typeDeclarations != null)
			result += typeDeclarations.size();
		return result;
	}

	// FIXME
	/**
	 Returns the child at the specified index in this node's "virtual"
	 child array.
	 This method returns children NOT in the order as they were found in the
	 original code, but first, it returns usings, then namespaces and only then
	 type declarations.
	 
	 @param index an index into this node's "virtual" child array
	 @return the program element at the given position
	 @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
	            of bounds
	*/

	public ProgramElement getChildAt(int index) {
		// A namespace has always an identifier
		int len = 1;
		if (len > index) {
			return reference;
		}
		index -= len;
		if (usings != null) {
			len = usings.size();
			if (len > index) {
				return usings.getProgramElement(index);
			}
			index -= len;
		}
		if (attrSections != null) {
			len = attrSections.size();
			if (len > index) {
				return attrSections.getAttributeSection(index);
			}
			index -= len;
		}
		if (namespaces != null) {
			len = namespaces.size();
			if (len > index) {
				return namespaces.getProgramElement(index);
			}
			index -= len;
		}
		if (typeDeclarations != null) {
			len = typeDeclarations.size();
			if (len > index) {
				return typeDeclarations.getProgramElement(index);
			}
			index -= len;
		}
		return null;
	}

	/** Returns child's position codes.
	 *  Role 0 is the namespaces. 
	 */
	public int getChildPositionCode(ProgramElement child) {
		// role 0: namespaces
		if (attrSections != null) {
			int index = attrSections.indexOf(child);
			if (index >= 0) {
				return (index << 4) | 15;
			}
		}
		if (namespaces != null) {
			int index = namespaces.indexOf(child);
			if (index >= 0) {
				return (index << 4) | 0;
			}
		}
		if (usings != null) {
			int index = usings.indexOf(child);
			if (index >= 0) {
				return (index << 4) | 1;
			}
		}
		if (typeDeclarations != null) {
			int index = typeDeclarations.indexOf(child);
			if (index >= 0) {
				return (index << 4) | 2;
			}
		}
		return 0;
	}

	/**
	 * Replace a single child in the current node.
	 * The child to replace is matched by identity and hence must be known
	 * exactly. The replacement element can be null - in that case, the child
	 * is effectively removed.
	 * The parent role of the new child is validated, while the
	 * parent link of the replaced child is left untouched.
	 * @param p the old child.
	 * @param p the new child.
	 * @return true if a replacement has occured, false otherwise.
	 * @exception ClassCastException if the new child cannot take over
	 *            the role of the old one.
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
		count = (namespaces == null) ? 0 : namespaces.size();
		for (int i = 0; i < count; i++) {
			if (namespaces.getProgramElement(i) == p) {
				if (q == null) {
					namespaces.remove(i);
				} else {
					NamespaceSpecification r = (NamespaceSpecification) q;
					namespaces.set(i, r);
					r.setParent(this);
				}
				return true;
			}
		}
		count = (usings == null) ? 0 : usings.size();
		for (int i = 0; i < count; i++) {
			if (usings.getProgramElement(i) == p) {
				if (q == null) {
					usings.remove(i);
				} else {
					Using r = (Using) q;
					usings.set(i, r);
					r.setParent(this);
				}
				return true;
			}
		}
		count = (typeDeclarations == null) ? 0 : typeDeclarations.size();
		for (int i = 0; i < count; i++) {
			if (typeDeclarations.getProgramElement(i) == p) {
				if (q == null) {
					typeDeclarations.remove(i);
				} else {
					TypeDeclaration r = (TypeDeclaration) q;
					typeDeclarations.set(i, r);
					r.setParent(this);
				}
				return true;
			}
		}

		return false;
	}

	/**
	 Get parent.
	 @return the compilation unit.
	 */

	public NamespaceSpecificationContainer getParent() {
		return parent;
	}

	/**
	 Set parent.
	 @param u a compilation unit.
	 */

	public void setParent(NamespaceSpecificationContainer u) {
		parent = u;
	}

	/**
	 Get package reference.
	 @return the package reference.
	 */

	public NamespaceReference getNamespaceReference() {
		return reference;
	}

	/**
	 Set package reference.
	 @param ref a package reference.
	 */

	public void setNamespaceReference(NamespaceReference ref) {
		reference = ref;
	}

	public void accept(SourceVisitor v) {
		v.visitNamespaceSpecification(this);
	}

	/**
	 * Returns the namespaces.
	 * @return NamespaceSpecificationMutableList
	 */
	public NamespaceSpecificationMutableList getNamespaces() {
		return namespaces;
	}

	/**
	 * Sets the namespaces.
	 * @param namespaces The namespaces to set
	 */
	public void setNamespaces(NamespaceSpecificationMutableList namespaces) {
		this.namespaces = namespaces;
	}

	public NamespaceSpecification getNamespaceSpecificationAt(int index) {
		return namespaces.getNamespaceSpecification(index);
	}

	public int getNamespaceSpecificationCount() {
		return namespaces.size();
	}

	/**
	 Get the number of type declarations in this container.
	 @return the number of type declarations.
	 */

	public int getTypeDeclarationCount() {
		return (typeDeclarations != null) ? typeDeclarations.size() : 0;
	}

	/*
	  Return the type declaration at the specified index in this node's
	  "virtual" type declaration array.
	  @param index an index for a type declaration.
	  @return the type declaration with the given index.
	  @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
	  of bounds.
	*/

	public TypeDeclaration getTypeDeclarationAt(int index) {
		if (typeDeclarations != null) {
			return typeDeclarations.getTypeDeclaration(index);
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Returns the usings.
	 * @return UsingMutableList
	 */
	public UsingMutableList getUsings() {
		return usings;
	}

	/**
	 * Sets the usings.
	 * @param usings The usings to set
	 */
	public void setUsings(UsingMutableList usings) {
		this.usings = usings;
	}

	/**
	 Get declarations.
	 @return the type declaration mutable list.
	 */

	public TypeDeclarationMutableList getDeclarations() {
		return typeDeclarations;
	}

	/**
	 Set declarations.
	 @param list a type declaration mutable list.
	 */

	public void setDeclarations(TypeDeclarationMutableList list) {
		typeDeclarations = list;
	}

	/** Attribute sections. */
	protected AttributeSectionMutableList attrSections;

	/**
	 * @see recoder.csharp.attributes.AttributableElement#getAttributeSectionAt(int)
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
	 * @see recoder.csharp.attributes.AttributableElement#getAttributeSectionCount()
	 */
	public int getAttributeSectionCount() {
		int result = 0;
		if (attrSections != null)
			result += attrSections.size();
		return result;
	}

	/**
	 * @see recoder.csharp.attributes.AttributableElement#setAttributeSections(AttributeSectionMutableList)
	 */
	public void setAttributeSections(AttributeSectionMutableList attrs) {
		attrSections = attrs;
		makeParentRoleValid();
	}

	/**
	 * Method getAttributeSections.
	 * @return ProgramElementList
	 */
	public AttributeSectionList getAttributeSections() {
		return attrSections;
	}

	/**
	 * Method getNamespaceSpecifications.
	 * @return ProgramElementList
	 */
	public NamespaceSpecificationList getNamespaceSpecifications() {
		return namespaces;
	}
	
	/** Returns the not-fully qualified name of the namespace. 
	 * */
	public String getName() {
		return reference.getFullName();
	}	

	public boolean isDefinedScope() {
		return name2type != UNDEFINED_SCOPE;
	}

	public void setDefinedScope(boolean defined) {
		if (!defined) {
			name2type = UNDEFINED_SCOPE;
		} else {
			name2type = null;
		}
	}

	public DeclaredTypeList getTypesInScope() {
		if (name2type == null || name2type.isEmpty()) {
			return DeclaredTypeList.EMPTY_LIST;
		}
		DeclaredTypeMutableList res = new DeclaredTypeArrayList();
		Enumeration menum = name2type.elements();
		while (menum.hasMoreElements()) {
			res.add((DeclaredType) menum.nextElement());
		}
		return res;
	}

	public DeclaredType getTypeInScope(String name) {
		Debug.asserta(name);
		if (name2type == null) {
			return null;
		}
		return (DeclaredType) name2type.get(name);
	}

	public void addTypeToScope(DeclaredType type, String name) {
		Debug.asserta(type, name);
		if (name2type == null || name2type == UNDEFINED_SCOPE) {
			name2type = new NaturalHashTable();
		}
		name2type.put(name, type);
	}

	public void removeTypeFromScope(String name) {
		Debug.asserta(name);
		if (name2type == null || name2type == UNDEFINED_SCOPE) {
			return;
		}
		name2type.remove(name);
	}



	/**
	 * @see recoder.csharp.SourceElement#getFirstElement()
	 */
	public SourceElement getFirstElement() {
		if (attrSections != null && attrSections.size() >0) {
			return attrSections.getAttributeSection(0).getFirstElement();
		} 
		return super.getFirstElement();
	}

}
