// This file is part of the RECODER library and protected by the LGPL.

package recoder.service;

import recoder.*;
import recoder.abstraction.*;
import recoder.abstraction.Enum;
import recoder.list.*;
import recoder.util.*;

/**
   Handles requests for implicitely defined program model elements.
   In particular these are {@link recoder.abstraction.NullType},
   {@link recoder.abstraction.Namespace}, 
   {@link recoder.abstraction.ArrayType}, and
   {@link recoder.abstraction.DefaultConstructor}.
 */
public class DefaultImplicitElementInfo
	extends DefaultProgramModelInfo
	implements ImplicitElementInfo {

	/** maps type declarations to default constructors */
	private final MutableMap type2defaultConstructor = new IdentityHashTable();

	/**
	@param config the configuration this services becomes part of.
	*/
	public DefaultImplicitElementInfo(ServiceConfiguration config) {
		super(config);
	}

	public DefaultConstructor getDefaultConstructor(ClassType ct) {
		Debug.asserta(ct);
		updateModel();
		DefaultConstructor cons =
			(DefaultConstructor) type2defaultConstructor.get(ct);
		if (cons == null) {
			cons = new DefaultConstructor(ct);
			cons.setProgramModelInfo(this);
			type2defaultConstructor.put(ct, cons);
		}
		return cons;
	}

	public Type getType(ProgramModelElement pme) {
		if (pme instanceof NullType || pme instanceof ArrayType) {
			return (Type) pme;
		} else {
			// valid for DefaultConstructor and Namespace
			return null;
		}
	}

	public Namespace getNamespace(ProgramModelElement pme) {
		if (pme instanceof Namespace) {
			return (Namespace) pme;
		}
		if (pme instanceof DefaultConstructor) {
			updateModel();
			return getContainingClassType((DefaultConstructor) pme)
				.getNamespace();
		}
		return null;
	}

	public DeclaredTypeList getTypes(DeclaredTypeContainer ctc) {
		if (ctc instanceof Namespace) {
			return serviceConfiguration.getNameInfo().getTypes((Namespace) ctc);
		}
		return null;
	}

	public DeclaredTypeList getAllTypes(ClassType ct) {
		// valid for NullType
		return null;
	}

	public DeclaredTypeContainer getDeclaredTypeContainer(DeclaredType ct) {
		// valid for NullType
		return null;
	}

	public ClassTypeList getSupertypes(ClassType ct) {
		// valid for NullType
		return null;
	}

	public ClassTypeList getAllSupertypes(ClassType ct) {
		// valid for NullType
		if (ct instanceof NullType) {
			return new ClassTypeArrayList(ct);
		}
		return null;
	}

	public FieldList getFields(ClassType ct) {
		// valid for NullType
		return null;
	}

	public FieldList getAllFields(ClassType ct) {
		// valid for NullType
		return null;
	}

	public MethodList getMethods(ClassType ct) {
		// valid for NullType
		return null;
	}

	public MethodList getAllMethods(ClassType ct) {
		// valid for NullType
		return null;
	}

	public ConstructorList getConstructors(ClassType ct) {
		// valid for NullType
		return null;
	}

	public ClassType getContainingClassType(Member m) {
		if (m instanceof DefaultConstructor) {
			return m.getContainingClassType();
		}
		return null;
	}

	public TypeList getSignature(Method m) {
		// valid for Default Constructor
		return TypeList.EMPTY_LIST;
	}

	public ClassTypeList getExceptions(Method m) {
		// valid for Default Constructor
		return ClassTypeList.EMPTY_LIST;
	}

	public Type getReturnType(Method m) {
		// valid for Default Constructor
		return null;
	}
	
/////////////////////////////// CSHARP EXTENSION METHODS ///////////////
///// These methods shall not be called, they are only provided for correct
///// interface implementation only.
	
	
	/** This method shall never be called...
	 * @see recoder.service.ProgramModelInfo#getReturnType(Delegate)
	 */
	public Type getReturnType(Delegate d) {
		return null;
	}

	/** This method shall never be called...
	 * @see recoder.service.ProgramModelInfo#getSignature(Delegate)
	 */
	public TypeList getSignature(Delegate d) {
		return null;
	}

	/** This method shall never be called...
	 * @see recoder.service.ProgramModelInfo#getBaseType(Enum)
	 */
	public Type getBaseType(Enum e) {
		return null;
	}

	/** This method shall never be called...
	 * @see recoder.service.ProgramModelInfo#getFields()
	 */
	public EnumMemberList getFields(Enum e) {
		return null;
	}

	/**
	 * @see recoder.service.ProgramModelInfo#getContainingEnum(EnumMember)
	 */
	public Enum getContainingEnum(EnumMember e) {
		return null;
	}

}
