// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp;

import recodercs.util.Debug;
import recodercs.*;
import recodercs.abstraction.*;
import recodercs.convenience.*;
import recodercs.csharp.attributes.*;
import recodercs.csharp.attributes.targets.*;
import recodercs.csharp.declaration.*;
import recodercs.csharp.declaration.modifier.*;
import recodercs.csharp.reference.*;
import recodercs.io.DataLocation;
import recodercs.list.*;
import recodercs.util.*;

import java.io.*;
import java.util.Enumeration;

/**
   A node representing a single source file containing
   {@link TypeDeclaration}s and an optional {@link NamespaceSpecification}
   and an optional set of {@link Using}s. In Java, any source file
   may contain at most one public class type definition.
   @author AL
   @author <TT>AutoDoc</TT>
 */

public class CompilationUnit
 extends CSharpNonTerminalProgramElement
 implements TypeDeclarationContainer, TypeScope,
 			  NamespaceSpecificationContainer, AttributableElement {

	/**
	 * Global attribute sections
	 */
	protected AttributeSectionMutableList attrSections;

    /**
     Current data location.
     */
    protected DataLocation location;

    /**
     Original data location.
     */

    protected DataLocation originalLocation;

    /**
     Namespace spec.
     */

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
       Undefined scope tag.
     */

    protected final static MutableMap UNDEFINED_SCOPE = new NaturalHashTable();

    /**
       Scope table.
    */

    protected MutableMap name2type = UNDEFINED_SCOPE;


    /**
     Compilation unit.
     */

    public CompilationUnit() {
        makeParentRoleValid();
    }

    /**
     Compilation unit.
     @param pkg a package specification.
     @param usings an import mutable list.
     @param typeDeclarations a type declaration mutable list.
     */

    public CompilationUnit(NamespaceSpecificationMutableList pkg, UsingMutableList imports, TypeDeclarationMutableList typeDeclarations) {
        setPackageSpecification(pkg);
        setUsings(imports);
        setDeclarations(typeDeclarations);
        makeParentRoleValid();
    }

    /**
     Compilation unit. Does not copy the data location.
     @param proto a compilation unit.
     */

    protected CompilationUnit(CompilationUnit proto) {
        super(proto);
        if (proto.namespaces != null) {
            namespaces = (NamespaceSpecificationMutableList)proto.namespaces.deepClone();
        }
        if (proto.usings != null) {
            usings = (UsingMutableList)proto.usings.deepClone();
        }
        if (proto.typeDeclarations != null) {
            typeDeclarations = (TypeDeclarationMutableList)proto.typeDeclarations.deepClone();
        }
        makeParentRoleValid();
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new CompilationUnit(this);
    }

    /**
     Make parent role valid.
     */

    public void makeParentRoleValid() {
        super.makeParentRoleValid();
//        if (namespaces != null) {
//            namespaces.setParent(this);
//        }
//        if (attrSections != null) {
//            for (int i = attrSections.size() - 1; i >= 0; i -= 1) {
//                attrSections.getAttributeSection(i).setParent(this);
//            }
//        }
		if (attrSections != null) {
			for (int i = attrSections.size() - 1; i >= 0; i--) {
				attrSections.getAttributeSection(i).setParent(this);
			}
		}
        if (namespaces != null) {
            for (int i = namespaces.size() - 1; i >= 0; i -= 1) {
                namespaces.getNamespaceSpecification(i).setParent(this);
            }
        }
        if (usings != null) {
            for (int i = usings.size() - 1; i >= 0; i -= 1) {
                usings.getUsing(i).setParent(this);
            }
        }
        if (typeDeclarations != null) {
            for (int i = typeDeclarations.size() - 1; i >= 0; i -= 1) {
                typeDeclarations.getTypeDeclaration(i).setParent(this);
            }
        }
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
//        if (namespaces == p) {
//            NamespaceSpecification r = (NamespaceSpecification)q;
//            namespaces = r;
//            if (r != null) {
//                r.setParent(this);
//            }
//            return true;
//        }
        count = (attrSections == null) ? 0 : attrSections.size();
        for (int i = 0; i < count; i++) {
            if (attrSections.getAttributeSection(i) == p) {
                if (q == null) {
                    attrSections.remove(i);
                } else {
                    AttributeSection r = (AttributeSection)q;
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
                    NamespaceSpecification r = (NamespaceSpecification)q;
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
                    Using r = (Using)q;
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
                    TypeDeclaration r = (TypeDeclaration)q;
                    typeDeclarations.set(i, r);
                    r.setParent(this);
                }
                return true;
            }
        }
        return false;
    }

    public SourceElement getFirstElement() {
        return (getChildCount() > 0) ? getChildAt(0).getFirstElement() : this;
    }

    public SourceElement getLastElement() {
		return this;
    }

    /**
       Get name of the unit. The name is empty if no data location is set;
       otherwise, the name of the current data location is returned.
       @return the name of this compilation unit.
       @see #getDataLocation()
     */

    public String getName() {
        return (location == null) ? "" : location.toString();
    }

    /** A compilation unit has no syntactical parent */

    public NonTerminalProgramElement getASTParent() {
        return null;
    }

    /**
     Returns the number of children of this node.
     @return an int giving the number of children of this node
    */

    public int getChildCount() {
        int result = 0;
//        if (namespaces      != null) result++;
		if (attrSections != null)
			result += attrSections.size();

        if (namespaces          != null) result += namespaces.size();
        if (usings          != null) result += usings.size();
        if (typeDeclarations != null) result += typeDeclarations.size();
        return result;
    }

    /**
     Returns the child at the specified index in this node's "virtual"
     child array
     @param index an index into this node's "virtual" child array
     @return the program element at the given position
     @exception ArrayIndexOutOfBoundsException if <tt>index</tt> is out
                of bounds
    */

    public ProgramElement getChildAt(int index) {
        int len;
//        if (namespaces != null) {
//            if (index == 0) return namespaces;
//            index--;
//        }
		if (attrSections != null) {
			len = attrSections.size();
			if (len > index) {
				return attrSections.getAttributeSection(index);
			}
			index -= len;
		}
        if (usings != null) {
            len = usings.size();
            if (len > index) {
                return usings.getProgramElement(index);
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
            return typeDeclarations.getProgramElement(index);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getChildPositionCode(ProgramElement child) {
        // role 0: package spec
        // role 1 (IDX): import
        // role 2 (IDX): declarations
        // role 15 : attribute
//        if (child == namespaces) {
//            return 0;
//        }
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
        return -1;
    }

    /**
       Gets the current data location.
       @return the data location.
     */

    public DataLocation getDataLocation() {
        return location;
    }

    /**
       Sets the current data location. If the data location has been
       <CODE>null</CODE>, the location also becomes the new original
       location.
       @param location a data location.
     */

    public void setDataLocation(DataLocation location) {
        if (this.location == null) {
            originalLocation = location;
        }
        this.location = location;
    }

    /**
     Gets the original data location.
     @return the original data location.
     */

    public DataLocation getOriginalDataLocation() {
        return originalLocation;
    }

    /**
     Get usings.
     @return the import mutable list.
     */

    public UsingMutableList getUsings() {
        return usings;
    }

    /**
     Set usings.
     @param list an import mutable list.
     */

    public void setUsings(UsingMutableList list) {
        usings = list;
    }

    /**
     Get package specification.
     @return the package specification.
     */

    public NamespaceSpecificationMutableList getNamespaceSpecifications() {
        return namespaces;
    }

    /**
     Set package specification.
     @param p a package specification.
     */

    public void setPackageSpecification(NamespaceSpecificationMutableList p) {
        namespaces = p;
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

    /**
     * Gets the primary type declaration of the compilation unit.
     * The primary declaration is the first declaration of the unit,
     * or the single public declaration. If there is no unambiguous primary 
     * declaration, this method returns <CODE>null</CODE>.
     */

    public TypeDeclaration getPrimaryTypeDeclaration() {
        TypeDeclaration res = null;
        int s = typeDeclarations.size();
        for (int i = 0; i < s; i += 1) {
            TypeDeclaration t = typeDeclarations.getTypeDeclaration(i);
            if (t.isPublic()) {
                if (res == null || !res.isPublic()) {
                    res = t;
                } else {
                    res = null;
                    break;
                }
            } else {
                if (res == null) {
                    res = t;
                }
            }
        }
        return res;
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
            res.add((DeclaredType)menum.nextElement());
        }
        return res;
    }

    public DeclaredType getTypeInScope(String name) {
        Debug.asserta(name);
        if (name2type == null || name2type == UNDEFINED_SCOPE) {
            return null;
        }
        return (DeclaredType)name2type.get(name);
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

    public void accept(SourceVisitor v) {
        v.visitCompilationUnit(this);
    }
    
	public NamespaceSpecification getNamespaceSpecificationAt(int index) {
		return namespaces.getNamespaceSpecification(index);
	}

	public int getNamespaceSpecificationCount() {
		return namespaces.size();
	}
    
	/**
	 * @see recodercs.csharp.attributes.AttributableElement#getAttributeSectionAt(int)
	 */
	public AttributeSection getAttributeSectionAt(int index) {
		if (attrSections != null) {
			if (index < attrSections.size()) return attrSections.getAttributeSection(index);
		}
		throw new ArrayIndexOutOfBoundsException();		
	}

	/**
	 * @see recodercs.csharp.attributes.AttributableElement#getAttributeSectionCount()
	 */
	public int getAttributeSectionCount() {
		return (attrSections != null) ? attrSections.size() : 0 ;
	}

	/**
	 * @see recodercs.csharp.attributes.AttributableElement#setAttributeSections(AttributeSectionMutableList)
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
		return  attrSections;
	}

}
