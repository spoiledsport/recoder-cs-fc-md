// This file is part of the RECODER library and protected by the LGPL.
package recoder.service;

import java.beans.*;
import java.util.Enumeration;
import recoder.*;
import recoder.io.*;
import recoder.util.*;
import recoder.list.*;
import recoder.convenience.*;
import recoder.convenience.csharp.MultidimArrayUtils;
import recoder.bytecode.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;
import recoder.csharp.expression.*;
import recoder.csharp.expression.operator.*;
import recoder.csharp.reference.*;
import recoder.abstraction.*;
import recoder.abstraction.Namespace;

public class DefaultNameInfo
	extends AbstractService
	implements NameInfo, PropertyChangeListener {

	private final static boolean DEBUG = false;

	/** Maps fully qualified class names to their according types. */
	private final MutableMap name2type = new NaturalHashTable(128);

	/** maps fully qualified variable names to their according variables */
	private final MutableMap name2field = new NaturalHashTable(128);

	/** maps namespace names to Namespace objects */
	private MutableMap name2namespace = new NaturalHashTable(64);
	
	/** maps CompilationUnits to NamespaceList objects */
	private MutableMap cu2namespaces = new NaturalHashTable(64);

	// the predefined types
	private final PrimitiveType booleanType;
	private final PrimitiveType byteType;
	private final PrimitiveType sbyteType;
	private final PrimitiveType shortType;
	private final PrimitiveType ushortType;
	private final PrimitiveType longType;
	private final PrimitiveType ulongType;
	private final PrimitiveType intType;
	private final PrimitiveType uintType;
	private final PrimitiveType floatType;
	private final PrimitiveType doubleType;
	private final PrimitiveType charType;
	private final PrimitiveType decimalType;
	private ClassType nullType;
	private ClassType systemObject;
	private ClassType systemString;
	private Type systemArray;
	private ClassType systemType;
	private ClassType systemICloneable;
	private ClassType javaIoSerializable;

	/**
	   Creates a new initialized definition table.
	   @param config the configuration this services becomes part of.
	*/
	public DefaultNameInfo(ServiceConfiguration config) {
		super(config);
		booleanType = createPrimitiveType("bool","System.Boolean");
		byteType = createPrimitiveType("byte","System.Byte");
		sbyteType = createPrimitiveType("sbyte","System.SByte");
		shortType = createPrimitiveType("short","System.Int16");
		ushortType = createPrimitiveType("ushort","System.UInt16");
		longType = createPrimitiveType("long","System.Int64");
		ulongType = createPrimitiveType("ulong","System.UInt64");
		intType = createPrimitiveType("int","System.Int32");
		uintType = createPrimitiveType("uint","System.UInt32");
		floatType = createPrimitiveType("float","System.Single");
		doubleType = createPrimitiveType("double","System.Double");
		charType = createPrimitiveType("char","System.Char");
		decimalType = createPrimitiveType("decimal","System.Decimal");
	}

	/** Initialize with a given ServiceConfiguration.
	 * Null type and System namespace will be created an cached.
	 */
	public void initialize(ServiceConfiguration cfg) {
		super.initialize(cfg);

		//Create null type
		nullType = new NullType(cfg.getImplicitElementInfo());

		//Create System namespace
		createNamespace("System");

		//Add a listener for the project settings change
		cfg.getProjectSettings().addPropertyChangeListener(this);
		updateSearchMode();
	}

	/** Listener for property changes in the project settings
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String changedProp = evt.getPropertyName();
		if (changedProp.equals(PropertyNames.CLASS_SEARCH_MODE)) {
			updateSearchMode();
		}
	}

	// search mode codes: search modes define, in what order classes are looked up

	/** Search mode code to define no search. */
	private final static int NO_SEARCH = 0;
	/** Search for sources first */
	private final static int SEARCH_SOURCE = 1;
	/** Search in the bytecodes first. Not yet available. */
	private final static int SEARCH_CLASS = 2;
	/** Load the class by reflection. Not yet available. */
	private final static int SEARCH_REFLECT = 3;

	// the current search mode
	/** The list of the current search modes. */
	private int[] searchMode;

	/** Parses the class search mode property and creates the internal
	 *  representation. Ignores everything that does not fit.
	 */
	private void updateSearchMode() {
		String prop =
			serviceConfiguration.getProjectSettings().getProperty(
				PropertyNames.CLASS_SEARCH_MODE);
		if (prop == null) {
			// just in case...
			prop = "";
		}
		searchMode = new int[prop.length()];
		for (int i = 0; i < searchMode.length; i++) {
			switch (prop.charAt(i)) {
				case 's' :
				case 'S' :
					searchMode[i] = SEARCH_SOURCE;
					break;
				case 'c' :
				case 'C' :
					searchMode[i] = SEARCH_CLASS;
					break;
				case 'r' :
				case 'R' :
					searchMode[i] = SEARCH_REFLECT;
					break;
				default :
					searchMode[i] = NO_SEARCH;
			}
		}
	}

	/** creates and initializes a new primitive type with the given name.
	    @param name the name of the primitive type to be registered
	    @return the according type object
	*/
	private PrimitiveType createPrimitiveType(String name, String boxname) {
		PrimitiveType res = new PrimitiveType(name, boxname, getImplicitElementInfo());
		name2type.put(res.getName(), res);
		return res;
	}

	/** Proxy method to force the ChangeHistory to update the model. 
	 */
	final void updateModel() {
		serviceConfiguration.getChangeHistory().updateModel();
	}

	//	/** Proxy method for the class file repository in the service 
	//	 * configuration. */
	//	ClassFileRepository getClassFileRepository() {
	//		return serviceConfiguration.getClassFileRepository();
	//	}

	/** Proxy methos to the source file repository in the service 
	 * configuration. */
	SourceFileRepository getSourceFileRepository() {
		return serviceConfiguration.getSourceFileRepository();
	}

	//	/** Proxy method for obtaining the ByteCodeInfo service from the 
	//	 * ServiceConfiguration. */
	//	ByteCodeInfo getByteCodeInfo() {
	//		return serviceConfiguration.getByteCodeInfo();
	//	}

	/** Proxy method for obtaining the SourceInfo service from the 
	 * ServiceConfiguration. */
	SourceInfo getSourceInfo() {
		return serviceConfiguration.getSourceInfo();
	}

	/** Proxy method for obtaining the ChangeHistory service from the 
	 * ServiceConfiguration. */
	ChangeHistory getChangeHistory() {
		return serviceConfiguration.getChangeHistory();
	}

	/** Proxy method for obtaining the ImplicitElementInfo service from the 
	 * ServiceConfiguration. */
	ImplicitElementInfo getImplicitElementInfo() {
		return serviceConfiguration.getImplicitElementInfo();
	}

	/////////////////////////////////////////////////////////////////////////////////
	// Real logic starts here
	/////////////////////////////////////////////////////////////////////////////////	

	/** Registers a ClassType into the type map. */
	public void register(DeclaredType ct) {
		Debug.asserta(ct);
		String name = ct.getFullName();
		Object ob = name2type.put(name, ct);
		if (ob != null && ob != ct) {
			Debug.log(
				"Internal Warning - Multiple registration of "
					+ Format.toString("%N [%i]", ct)
					+ Format.toString(" --- was: %N [%i]", (ProgramModelElement) ob));
		}
	}
	public void register(Field f) {
		Debug.asserta(f);
		name2field.put(f.getFullName(), f);
	}
	/** Returns the cache object of the System.Object class. (This method
	 * was originally called getJavaLangObject. */
	public ClassType getSystemObject() {
		if (systemObject == null) {
			systemObject = getClassType("System.Object");
		}
		return systemObject;
	}
	/** Returns the cache object of the System.String class. (This method
	 * was originally called getJavaLangString. */
	public ClassType getSystemString() {
		if (systemString == null) {
			systemString = getClassType("System.String");
		}
		return systemString;
	}

	/** Returns the cache object of the System.Array class. */
	public Type getSystemArray() {
		if (systemArray == null) {
			systemArray = getClassType("System.Array");
		}
		return systemArray;
	}

	/** Returns the cache object of the System.Type class. (This method
	 * was originally called getJavaLangClass.
	 */
	public ClassType getSystemType() {
		if (systemType == null) {
			systemType = getClassType("System.Type");
		}
		return systemType;
	}
	public ClassType getSystemICloneable() {
		if (systemICloneable == null) {
			systemICloneable = getClassType("System.ICloneable");
		}
		return systemICloneable;
	}
	public ClassType getJavaIoSerializable() {
		if (javaIoSerializable == null) {
			javaIoSerializable = getClassType("java.io.Serializable");
		}
		return javaIoSerializable;
	}
	public ClassType getNullType() {
		return nullType;
	}
	public PrimitiveType getShortType() {
		return shortType;
	}
	public PrimitiveType getByteType() {
		return byteType;
	}
	public PrimitiveType getBooleanType() {
		return booleanType;
	}
	public PrimitiveType getIntType() {
		return intType;
	}
	public PrimitiveType getLongType() {
		return longType;
	}
	public PrimitiveType getFloatType() {
		return floatType;
	}
	public PrimitiveType getDoubleType() {
		return doubleType;
	}
	public PrimitiveType getCharType() {
		return charType;
	}
	/**
	 * Returns the sbyteType.
	 * @return PrimitiveType
	 */
	public PrimitiveType getSbyteType() {
		return sbyteType;
	}
	/**
	 * Returns the uintType.
	 * @return PrimitiveType
	 */
	public PrimitiveType getUintType() {
		return uintType;
	}
	/**
	 * Returns the ulongType.
	 * @return PrimitiveType
	 */
	public PrimitiveType getUlongType() {
		return ulongType;
	}
	/**
	 * Returns the ushortType.
	 * @return PrimitiveType
	 */
	public PrimitiveType getUshortType() {
		return ushortType;
	}
	/**
	 * Returns the decimalType.
	 * @return PrimitiveType
	 */
	public PrimitiveType getDecimalType() {
		return decimalType;
	}
	public boolean isNamespace(String name) {
		updateModel();
		return name2namespace.get(name) != null;
	}
	/** Creates a new namespace from the given name.
	 *  Namespaces are created recursively.
	 */
	public Namespace createNamespace(String name) {
		Namespace result = (Namespace) name2namespace.get(name);
		if (result == null) {
			result = new Namespace(name, serviceConfiguration.getImplicitElementInfo());
			name2namespace.put(result.getName(), result);
			int ldp = name.lastIndexOf('.');
			if (ldp > 0) {
				createNamespace(name.substring(0, ldp));
			}
		}
		return result;
	}
	public Namespace getNamespace(String name) {
		Debug.asserta(name);
		updateModel();
		return (Namespace) name2namespace.get(name);
	}
	public NamespaceList getNamespaces() {
		updateModel();
		int size = name2namespace.size();
		NamespaceMutableList result = new NamespaceArrayList(size);
		for (Enumeration menum = name2namespace.elements(); size > 0; size -= 1) {
			result.add((Namespace) menum.nextElement());
		}
		return result;
	}
	public ClassType getClassType(String name) {
		Type result = getType(name);
		if (result instanceof ClassType) {
			return (ClassType) result;
		}
		return null;
	}

	/** 
	 * Creates a new (multidimensional) array type, with the given basetype 
	 * and dimension. 
	 */
	public ArrayType createArrayType(Type basetype, int dimension) {
		String aname =
			MultidimArrayUtils.appendDimensions(basetype.getFullName(), dimension);
		ArrayType result = (ArrayType) name2type.get(aname);
		if (result == null) {
			result =
				new ArrayType(
					basetype,
					serviceConfiguration.getImplicitElementInfo(),
					dimension);
			name2type.put(result.getFullName(), result);
		}
		return result;
	}

	/** Creates an array type with the specified dimensions */
	public ArrayType createArrayType(Type basetype, int[] dims) {
		Debug.asserta(dims.length > 0);

		ArrayType result = createArrayType(basetype, dims[0]);

		for (int i = 1; i < dims.length; i++) {
			result = createArrayType(result, dims[i]);
		}

		return result;
	}

	/** 
	 * Returns the array type with the specified base type, and dimension.
	 * (Arrays with the same basetype, but different dimensions are not considered
	 * to be equal.)
	 */
	public ArrayType getArrayType(Type basetype, int dimension) {
		Debug.asserta(basetype);
		updateModel();
		String aname =
			MultidimArrayUtils.appendDimensions(basetype.getFullName(), dimension);
		return (ArrayType) name2type.get(aname);
	}

	/** 
	 * Returns the type for the given name. 
	 */
	public Type getType(String name) {
		Debug.asserta(name);

		updateModel();

		if (DEBUG)
			Debug.log("Search requested for type " + name);

		// First try to look the name up in the cache.

		Type result = (Type) name2type.get(name);
		if (result == unknownType) {
			if (DEBUG)
				Debug.log(name + " is known to be unknown");
			return null; // report null
		} else if (result == null) {
			if (name.endsWith("]")) {
				String base = MultidimArrayUtils.getBase(name);
				int dimensions = MultidimArrayUtils.getDimensions(name);

				result = getType(base);

				if (result != null) {
					return createArrayType(result, dimensions);
				}
			}
			// try to load the required information
			if (result == null && loadClass(name)) {
				result = (Type) name2type.get(name);
				if (result == unknownType) {
					if (DEBUG)
						Debug.log(name + " is known to be unknown");
					return null;
				}
			}
			// cache positive or negative results
			if (DEBUG && result == null)
				Debug.log(name + " is set to unknown");
			name2type.put(name, (result != null) ? result : unknownType);
		}
		if (DEBUG && result != null)
			Debug.log(name + " has been found");
		return result;
	}
	public TypeList getTypes() {
		updateModel();
		int size = name2type.size();
		TypeMutableList result = new TypeArrayList(size / 2);
		// size / 2: approx. 50% of all types are expected to be unknown
		for (Enumeration menum = name2type.elements(); size > 0; size -= 1) {
			Type t = (Type) menum.nextElement();
			if (t != unknownType) {
				result.add(t);
			}
		}
		return result;
	}

	/*
	  Here is room for improvement: Cache that stuff.
	 */

	public DeclaredTypeList getTypes(Namespace pkg) {
		Debug.asserta(pkg);
		updateModel();
		DeclaredTypeMutableList result = new DeclaredTypeArrayList();
		TypeList tl = getTypes();
		int s = tl.size();
		for (int i = 0; i < s; i++) {
			Type t = tl.getType(i);
			if (t instanceof DeclaredType) {
				DeclaredType ct = (DeclaredType) t;
				if (ct.getContainer() == pkg) {
					result.add(ct);
				}
			}
		}
		return result;
	}
	public ClassTypeList getClassTypes() {
		updateModel();
		ClassTypeMutableList result = new ClassTypeArrayList(name2type.size() - 8);
		TypeList tl = getTypes();
		int s = tl.size();
		for (int i = 0; i < s; i++) {
			Type t = tl.getType(i);
			if (t instanceof ClassType) {
				result.add((ClassType) t);
			}
		}
		return result;
	}
	public Field getField(String name) {
		Debug.asserta(name);
		updateModel();
		Field result = (Field) name2field.get(name);
		if (result != null) {
			return result;
		}
		// we can try to get the type first
		int ldp = name.lastIndexOf('.');
		if (ldp == -1) {
			return null;
		}
		ClassType ct = getClassType(name.substring(0, ldp));
		if (ct == null) {
			return null;
		}
		VariableList fields = ct.getFields();
		if (fields == null) {
			return null;
		}
		for (int i = 0; i < fields.size(); i++) {
			String fname = fields.getVariable(i).getName();
			if (name == fname || name.equals(fname)) {
				result = (Field) fields.getVariable(i);
				if (result != null) {
					break;
				}
			}
		}
		return result;
	}
	public FieldList getFields() {
		updateModel();
		int size = name2field.size();
		FieldMutableList result = new FieldArrayList(size);
		for (Enumeration menum = name2field.elements(); size > 0; size -= 1) {
			result.add((Field) menum.nextElement());
		}
		return result;
	}
	private boolean loadClass(String classname) {
		boolean result = false;
		for (int i = 0; !result && i < searchMode.length; i += 1) {
			switch (searchMode[i]) {
				case SEARCH_SOURCE :
					if (DEBUG)
						Debug.log("Searching source code: " + classname);
					result = loadClassFromSourceCode(classname);
					break;
				case SEARCH_CLASS :
					//					if (DEBUG)
					//						Debug.log("Searching class file: " + classname);
					//					result = loadClassFromPrecompiledCode(classname);
					//					break;
				case SEARCH_REFLECT :
					//					if (DEBUG)
					//						Debug.log("Searching class: " + classname);
					//					result = loadClassByReflection(classname);
					//					break;
					// TODO: Make these alternative loadings also available... at some time...
				default :
					break;
			}
		}
		return result;
	}
	private boolean loadClassFromPrecompiledCode(String classname) {
		//		boolean result = false;
		//		ClassFileRepository cfr = getClassFileRepository();
		//		ClassFile cf = cfr.getClassFile(classname);
		//		if (cf != null) {
		//			getByteCodeInfo().register(cf);
		//			result = true;
		//		}
		//		return result;
		// TODO: Reimplement for C# ?
		return false;
	}
	private boolean loadClassFromSourceCode(String classname) {
		boolean result = false;
		CompilationUnit cu = null;
		try {
			cu = getSourceFileRepository().getCompilationUnit(classname);
			if (cu == null) {
				// try to load member classes by loading outer classes
				int ldp = classname.lastIndexOf('.');
				if (ldp >= 0) {
					String shortedname = classname.substring(0, ldp);
					// not a top-level type, parent type was loaded
					// and member type has been registered:
					return !name2namespace.containsKey(shortedname)
						&& loadClassFromSourceCode(shortedname)
						&& name2type.containsKey(classname);
				}
			}
			if (cu != null) {
                                getSourceInfo().register(cu); // was deprecated
                                // The class is already attached in sourcefilerepository
				// getChangeHistory().attached(cu);
				result = true;
			}
		} catch (Exception e) {
			Debug.error(
				"Error trying to retrieve source file for type "
					+ classname
					+ "\n"
					+ "Exception was "
					+ e);
			e.printStackTrace();
		}
		return result;
	}
	private boolean loadClassByReflection(String classname) {
		//		ClassFile cf = ReflectionImport.getClassFile(classname);
		//		if (cf != null) {
		//			getByteCodeInfo().register(cf);
		//			return true;
		//		}
		//		return false;
		// TODO: Implement for C#? Seems to be very impossible, since we
		// are in JAVA...
		return false;
	}

	public String information() {
		int unknown = 0;
		Enumeration menum = name2type.elements();
		while (menum.hasMoreElements()) {
			Type t = (Type) menum.nextElement();
			if (t == unknownType || t == unknownClassType) {
				unknown += 1;
			}
		}

		menum = name2type.elements();
		while (menum.hasMoreElements()) {
			Type t = (Type) menum.nextElement();
			System.out.println(t.getFullName());
		}

		System.out.println("Namespaces...");

		menum = name2namespace.elements();
		while (menum.hasMoreElements()) {
			Namespace t = (Namespace) menum.nextElement();
			System.out.println(t.getFullName());
		}
		

		return ""
			+ name2namespace.size()
			+ " packages with "
			+ (name2type.size() - unknown)
			+ " types ("
			+ unknown
			+ " were pure speculations) and "
			+ name2field.size()
			+ " fields";	
	}

	public void unregisterClassType(String fullname) {
		Debug.asserta(fullname);
		name2type.remove(fullname);
		// deregister array types
		// This is a slow implmentation, but does it.
		Enumeration names = name2type.keys();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			if (name.startsWith(fullname)) name2type.remove(name);
		}
	}
	public void unregisterField(String fullname) {
		Debug.asserta(fullname);
		name2field.remove(fullname);
	}

	public void unregisterNamespaces() {
		MutableMap n2p = new NaturalHashTable(64);


		// DISABLED Because we do not want to use the class file repository (since it is not
		// implemented yet...)

		//		ClassFileList cf = getClassFileRepository().getKnownClassFiles();
		//		for (int i = cf.size() - 1; i >= 0; i -= 1) {
		//			DeclaredTypeContainer ctc = cf.getClassFile(i).getContainer();
		//			if (ctc instanceof Namespace) {
		//				n2p.put(ctc.getFullName(), ctc);
		//			}
		//		}
		name2namespace.clear(); // help gc a bit
		name2namespace = n2p;
	}
	///////////////////////////////////////////////////////////////////////////
	// CLASSES FOR THE UNKNOWN MEMBERS, ETC.
	///////////////////////////////////////////////////////////////////////////
	class UnknownProgramModelElement implements ProgramModelElement {
		public String getName() {
			return "<unkownElement>";
		}
		public String getFullName() {
			return getName();
		}
		public ProgramModelInfo getProgramModelInfo() {
			return null;
		}
		public void setProgramModelInfo(ProgramModelInfo pmi) {}
		public void validate() {}
	}

	abstract class UnknownMember extends UnknownProgramModelElement implements Member {
		public ClassType getContainingClassType() {
			return unknownClassType;
		}

		/**
		 * @see recoder.abstraction.Member#isInternal()
		 */
		public boolean isInternal() {
			return false;
		}

		/**
		 * @see recoder.abstraction.Member#isNew()
		 */
		public boolean isNew() {
			return false;
		}

		/**
		 * @see recoder.abstraction.Member#isPrivate()
		 */
		public boolean isPrivate() {
			return false;
		}

		/**
		 * @see recoder.abstraction.Member#isProtected()
		 */
		public boolean isProtected() {
			return false;
		}

		/**
		 * @see recoder.abstraction.Member#isPublic()
		 */
		public boolean isPublic() {
			return false;
		}

		/**
		 * @see recoder.abstraction.Member#isSealed()
		 */
		public boolean isSealed() {
			return false;
		}

		/**
		 * @see recoder.abstraction.Member#isStatic()
		 */
		public boolean isStatic() {
			return false;
		}

	}

	class UnknownClassType extends UnknownMember implements ClassType {

		public String getName() {
			return "<unknownClassType>";
		}

		public DeclaredTypeContainer getContainer() {
			return null;
		}

		public DeclaredTypeList getDeclaredTypes() {
			return DeclaredTypeList.EMPTY_LIST;
		}

		public Namespace getNamespace() {
			return unknownPackage;
		}

		public ClassTypeList getSupertypes() {
			return ClassTypeList.EMPTY_LIST;
		}

		public ClassTypeList getAllSupertypes() {
			ClassTypeMutableList result = new ClassTypeArrayList();
			result.add(this);
			result.add(getSystemObject());
			return result;
		}

		public FieldList getFields() {
			return getSystemObject().getFields();
		}

		public FieldList getAllFields() {
			return getSystemObject().getAllFields();
		}

		public MethodList getMethods() {
			return getSystemObject().getMethods();
		}

		public MethodList getAllMethods() {
			return getSystemObject().getAllMethods();
		}

		public ConstructorList getConstructors() {
			return ConstructorList.EMPTY_LIST;
		}

		public DeclaredTypeList getAllTypes() {
			return DeclaredTypeList.EMPTY_LIST;
		}

		public boolean isInterface() {
			return false;
		}

		public boolean isStruct() {
			return false;
		}

		public boolean isAbstract() {
			return false;
		}

	}

	class UnknownMethod extends UnknownMember implements Method {

		public String getName() {
			return "<unknownMethod>";
		}

		public Namespace getNamespace() {
			return unknownPackage;
		}

		public DeclaredTypeContainer getContainer() {
			return unknownClassType;
		}

		public ClassTypeList getClassTypes() {
			return ClassTypeList.EMPTY_LIST;
		}

		public ClassTypeList getExceptions() {
			return ClassTypeList.EMPTY_LIST;
		}

		public Type getReturnType() {
			return unknownType;
		}

		public TypeList getSignature() {
			return TypeList.EMPTY_LIST;
		}

		public boolean isAbstract() {
			return false;
		}

		public boolean isExtern() {
			return false;
		}

		public boolean isOverride() {
			return false;
		}

		public boolean isVirtual() {
			return false;
		}

	}

	class UnknownConstructor extends UnknownMethod implements Constructor {

		public String getName() {
			return "<unknownConstructor>";
		}

	}

	class UnknownVariable extends UnknownProgramModelElement implements Variable {

		public String getName() {
			return "<unknownVariable>";
		}

		public Type getType() {
			return unknownType;
		}

		public boolean isReadOnly() {
			return false;
		}

	}

	class UnknownField extends UnknownMember implements Field {

		public String getName() {
			return "<unknownField>";
		}

		public Type getType() {
			return unknownType;
		}

		public boolean isVolatile() {
			return false;
		}

		public boolean isReadOnly() {
			return false;
		}

	}

	/**
	   The unknown elements. They are used for error handling and to mark
	   entities as "known-as-unknown" internally.
	 */
	private final ProgramModelElement unknownElement = new UnknownProgramModelElement();
	private final ClassType unknownClassType = new UnknownClassType();
	private final Type unknownType = unknownClassType;
	private final Namespace unknownPackage = new Namespace("<unknownPackage>", null);
	private final Method unknownMethod = new UnknownMethod();
	private final Constructor unknownConstructor = new UnknownConstructor();
	private final Variable unknownVariable = new UnknownVariable();
	private final Field unknownField = new UnknownField();
	public ClassType getUnknownClassType() {
		return unknownClassType;
	}
	public ProgramModelElement getUnknownElement() {
		return unknownElement;
	}
	public Namespace getUnknownPackage() {
		return unknownPackage;
	}
	public Method getUnknownMethod() {
		return unknownMethod;
	}
	public Constructor getUnknownConstructor() {
		return unknownConstructor;
	}
	public Variable getUnknownVariable() {
		return unknownVariable;
	}
	public Field getUnknownField() {
		return unknownField;
	}
	public Type getUnknownType() {
		return unknownType;
	}
	/**
	 * @see recoder.service.NameInfo#getNamespacesInCompilationUnit(CompilationUnit)
	 */
	public NamespaceList getNamespacesInCompilationUnit(CompilationUnit cu) {
		NamespaceList list;
		list = (NamespaceList) cu2namespaces.get(cu);
		if (list == null) {
			NamespaceFinderVisitor vis = new NamespaceFinderVisitor(this);
			list = vis.find(cu);
			cu2namespaces.put(cu,list);
		}
		return list;
	}

}
