// This file is part of the RECODER library and protected by the LGPL.

package recodercs.service;

import recodercs.abstraction.ArrayType;
import recodercs.abstraction.ClassType;
import recodercs.abstraction.Constructor;
import recodercs.abstraction.Field;
import recodercs.abstraction.Method;
import recodercs.abstraction.PrimitiveType;
import recodercs.abstraction.ProgramModelElement;
import recodercs.abstraction.Type;
import recodercs.abstraction.Variable;

import recodercs.Service;
import recodercs.abstraction.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;
import recodercs.csharp.expression.*;
import recodercs.csharp.expression.operator.*;
import recodercs.csharp.reference.*;
import recodercs.list.*;


/** 
    Manages the global name mapping.
 */
public interface NameInfo extends Service {

    /** 
	Returns a package represented by the given fully qualified name. If
	the package has not been encountered before, a new package is created
	and returned.
	@param  name a qualified name.
	@return the according package, constructed on demand.
    */
    Namespace createNamespace(String name);

    /** 
	Returns the package represented by the given fully qualified name,
	if it already exists.
	@param  name a qualified name.
	@return the according package, or <CODE>null</CODE>.
    */
    Namespace getNamespace(String name);
    
    /** 
	Returns the list of globally known packages.
	@return the list of packages.
    */
    NamespaceList getNamespaces();

    /** 
	Returns the list of globally known packages.
	@return the list of packages.
    */
    NamespaceList getNamespacesInCompilationUnit(CompilationUnit cu);



    /** 
	Returns the type represented by the given fully qualified name. If
	the type is currently not available, the method returns <tt>null</tt>.
	A type may be unavailable either if it currently has not been analyzed
	or is undefined. The specific behavior depends on the compiler 
	configuration.
	@param  name a fully qualified type name.
	@return the according type, or <tt>null</tt>.
    */
    Type getType(String name);
    
    /** 
	Returns the list of globally known types.
	@return the list of types.
    */
    TypeList getTypes();

    /**
	Returns the list of known class types of the given package.
	@param pkg a package.
	@return the list of class types in the package.
    */
    DeclaredTypeList getTypes(Namespace pkg);

    /** 
	Returns the class type represented by the given fully qualified name. 
	If the type is currently not available or does not represent a class
	type, the method returns <tt>null</tt>.
	@param  name a fully qualified type name.
	@return the according class type, or <tt>null</tt>.
    */
    ClassType getClassType(String name);

    /**
       Returns the array type for the given base type, if it already exists.
       @param basetype the base type to find an array type for.
       @return the array type for the given base type, or <CODE>null</CODE>.
     */
    ArrayType getArrayType(Type basetype, int dimension);

    /**
       Returns the array type for the given base type. This method will
       create one if needed.
       @param basetype the base type to find an array type for.
       @return the array type for the given base type.
     */
    ArrayType createArrayType(Type basetype, int dimension);

    /**
       Returns the array type for the given base type. This method will
       create one if needed.
       @param basetype the base type to find an array type for.
       @return the array type for the given base type.
     */
    ArrayType createArrayType(Type basetype, int[] dimensions);

    /** 
	Returns the list of globally known class types.
	@return the list of class types.
    */
    ClassTypeList getClassTypes();

    /**
      Returns the predefined Null type.
      The Null type is widening to each class or array type.
      @return the Null type.
    */
    ClassType getNullType();

    /**
      Returns the predefined boolean type.
      @return the primitive boolean type.
    */
    PrimitiveType getBooleanType();

    /**
      Returns the predefined byte type.
      @return the primitive byte type.
    */
    PrimitiveType getByteType();

    /**
      Returns the predefined sbyte type.
      @return the primitive sbyte type.
    */
    PrimitiveType getSbyteType();

    /**
      Returns the predefined short type.
      @return the primitive short type.
    */
    PrimitiveType getShortType();

    /**
      Returns the predefined ushort type.
      @return the primitive ushort type.
    */
    PrimitiveType getUshortType();

    /**
      Returns the predefined int type.
      @return the primitive int type.
    */
    PrimitiveType getIntType();

    /**
      Returns the predefined uint type.
      @return the primitive uint type.
    */
    PrimitiveType getUintType();

    /**
      Returns the predefined long type.
      @return the primitive long type.
    */
    PrimitiveType getLongType();

    /**
      Returns the predefined ulong type.
      @return the primitive ulong type.
    */
    PrimitiveType getUlongType();

    /**
      Returns the predefined float type.
      @return the primitive float type.
    */
    PrimitiveType getFloatType();

    /**
      Returns the predefined double type.
      @return the primitive double type.
    */
    PrimitiveType getDoubleType();

    /**
      Returns the predefined char type.
      @return the primitive char type.
    */
    PrimitiveType getCharType();

    /**
      Returns the predefined char type.
      @return the primitive char type.
    */
    PrimitiveType getDecimalType();


    /**
      Returns the predefined Object type.
      @return the Object type.
    */
    ClassType getSystemObject();

    /**
      Returns the predefined String type.
      @return the String type.
    */
    ClassType getSystemString();
    
	/**
      Returns the predefined Array type.
	 * @return the System.Array type
	 */
	Type getSystemArray();    

    /**
      Returns the predefined Class type.
      The Class type appears as the type of ".class" references.
      @return the Class type.
    */
    ClassType getSystemType();

    /**
      Returns the predefined Cloneable type.
      The Cloneable type is a valid supertype of arrays.
      @return the Cloneable type.
    */
    ClassType getSystemICloneable();

    /**
      Returns the predefined Serializable type.
      The Serializable type is a valid supertype of arrays.
      @return the Serializable type.
    */
    ClassType getJavaIoSerializable();
    
    /** 
	Returns a field belonging to the given fully qualified name.
    	@param a fully qualified field name, e.g. "System.out".
    	@return the field with that name, or <tt>null</tt> if no such
	field is known.
    */
    Field getField(String name);

    /** 
	Returns the list of globally known fields.
	@return the list of fields.
    */
    FieldList getFields();

    /**
       Registers a class type.
       @param ct the class type to be recognized by this service.
    */
    void register(DeclaredType ct);

    /**
       Registers a field.
       @param f the field to be recognized by this service.
    */
    void register(Field f);

    /**
       Unregisters a class type.
       @param fullname the (former) class type name.
    */
    void unregisterClassType(String fullname);

    /**
       Unregisters a field.
       @param fullname the (former) field name.
    */
    void unregisterField(String fullname);


    /**
       Returns the placeholder for an unknown entity that might be
       a package, class type, or field.
       Unknown elements can model incomplete programs.
       Queries for properties of unknown elements will return only minimum 
       information, even though it often is possible to infer certain 
       information about single unknown elements. As the alias problem
       is not solvable, there is only one representative for all unknown
       elements of a certain type.
    */
    ProgramModelElement getUnknownElement();

    /**
       Returns the placeholder for an unknown type.  
       Unknown elements can model incomplete programs.
       Queries for properties of unknown elements will return only minimum 
       information, even though it often is possible to infer certain 
       information about single unknown elements. As the alias problem
       is not solvable, there is only one representative for all unknown
       elements of a certain type.
    */
    Type getUnknownType();

    /**
       Returns the placeholder for an unknown class type.
       Unknown elements can model incomplete programs.
       Queries for properties of unknown elements will return only minimum 
       information, even though it often is possible to infer certain 
       information about single unknown elements. As the alias problem
       is not solvable, there is only one representative for all unknown
       elements of a certain type.
    */
    ClassType getUnknownClassType();

    /**
       Returns the placeholder for an unknown package.
       Unknown elements can model incomplete programs.
       Queries for properties of unknown elements will return only minimum 
       information, even though it often is possible to infer certain 
       information about single unknown elements. As the alias problem
       is not solvable, there is only one representative for all unknown
       elements of a certain type.
    */
    Namespace getUnknownPackage();

    /**
       Returns the placeholder for an unknown method.
       Unknown elements can model incomplete programs.
       Queries for properties of unknown elements will return only minimum 
       information, even though it often is possible to infer certain 
       information about single unknown elements. As the alias problem
       is not solvable, there is only one representative for all unknown
       elements of a certain type.
    */
    Method getUnknownMethod();

    /**
       Returns the placeholder for an unknown constructor.
       Unknown elements can model incomplete programs.
       Queries for properties of unknown elements will return only minimum 
       information, even though it often is possible to infer certain 
       information about single unknown elements. As the alias problem
       is not solvable, there is only one representative for all unknown
       elements of a certain type.
    */
    Constructor getUnknownConstructor();

    /**
       Returns the placeholder for an unknown variable.
       Unknown elements can model incomplete programs.
       Queries for properties of unknown elements will return only minimum 
       information, even though it often is possible to infer certain 
       information about single unknown elements. As the alias problem
       is not solvable, there is only one representative for all unknown
       elements of a certain type.
    */
    Variable getUnknownVariable();

    /**
       Returns the placeholder for an unknown field.
       Unknown elements can model incomplete programs.
       Queries for properties of unknown elements will return only minimum 
       information, even though it often is possible to infer certain 
       information about single unknown elements. As the alias problem
       is not solvable, there is only one representative for all unknown
       elements of a certain type.
    */
    Field getUnknownField();


}

