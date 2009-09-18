// This file is part of the RECODER library and protected by the LGPL.

package recoder.service;

import recoder.Service;
import recoder.abstraction.*;
import recoder.abstraction.Namespace;
import recoder.csharp.*;
import recoder.csharp.declaration.*;
import recoder.csharp.expression.*;
import recoder.csharp.expression.operator.*;
import recoder.csharp.reference.*;
import recoder.csharp.statement.*;
import recoder.list.*;
import recoder.util.*;

/** 
    Implements queries for program model elements with concrete syntactical
    representations.
    @author RN, AL
 */
public interface SourceInfo extends ProgramModelInfo {

    /**
	Returns the class type that contains the given program element.
	@param  context a program element.
	@return the type to which the given program element belongs
	(may be <CODE>null</CODE>).
    */
    ClassType getContainingClassType(ProgramElement context);
// OK

    /**
       Returns the referred namespace
       @param r a namespace reference.
       @return the referred namespace (may be <CODE>null</CODE>).
     */
    Namespace getNamespace(NamespaceReference r);
// OK
    
    /** 
	Returns the type for the given program element.
	@param  pe the program element to compute the type for.
	@return the type for that object (may be <CODE>null</CODE>).
    */
    Type getType(ProgramElement pe);
// OK    

    /** 
	Returns the referred type.
	@param  tr a type reference.
	@return the referred type (may be <CODE>null</CODE>).
    */
    Type getType(TypeReference tr);
// OK

    /**
       Returns the declared type.
       @param td a type declaration.
       @return the corresponding type.
     */
    DeclaredType getType(TypeDeclaration td); //changed to DeclaredType
// OK

    /** 
	Returns the type for the variable specification.
	@param  vs a variable specification.
	@return the type for that variable (may be <CODE>null</CODE>).
    */
    Type getType(VariableSpecification vs);
// OK

    /** 
	Tries to find a type with the given name using the given
	program element as context. Useful to check for name clashes
	when introducing a new identifier.
	Neither name nor context may be <CODE>null</CODE>.
	@param  name the name for the type to be looked up;
	may or may not be qualified.
	@param  context a program element defining the lookup context (scope).
	@return the corresponding type (may be <CODE>null</CODE>).
    */
    Type getType(String name, ProgramElement context);
// OK

    /**
       Returns the type of the given expression (may be <CODE>null</CODE>).
       @param expr an expression.
       @return the type of the expression, or <CODE>null</CODE> if the
       type could not be computed.
    */
    Type getType(Expression expr);
// OK

    /**
       Checks automatic narrowing from compile-time constant ints
       to byte, char, or short. Returns false if the primitive type
       is not one of byte, char, short, or if the expression is
       not a compile-time constant int as defined in the Java language
       specification.
       @param expr the expression that might be a compile-time constant 
       fitting into the given type.
       @param to the type that the expression might fit into.
       @return <CODE>true</CODE> if the expression value is "constant" and
       would fit into a variable of the given type without loss of information,
       <CODE>false</CODE> in any other case.
     */
    boolean isNarrowingTo(Expression expr, PrimitiveType to);
// OK
// TODO: Look at this method and the C# specification
    
    /** 
	Tries to find a variable with the given name using the given 
	program element as context. The variable may be a local variable,
	a parameter or a field. Useful to check for name clashes
	when introducing a new identifier.
	@param  name the name of the variable to be looked up.
	@param  context a program element defining the lookup context.
	@return the corresponding variable (may be <CODE>null</CODE>).
    */
    Variable getVariable(String name, ProgramElement context);
// OK

    /**
       Returns the declared variable.
       @param vs a variable specification.
       @return the corresponding variable.
     */
    Variable getVariable(VariableSpecification vs);
// OK

    /** 
	Returns the referred variable.
	@param  vr a variable reference.
	@return the referred variable (may be <CODE>null</CODE>).
    */
    Variable getVariable(VariableReference vr);
// OK

    /** 
    Returns the referred variable.
    @param  dr a DelegateCallReference
    @return the referred variable (may be <CODE>null</CODE>).
    */
    Variable getVariable(DelegateCallReference dr);
// OK




    /** 
	Returns the referred field.
	@param  fr a field reference.
	@return the referred field (may be <CODE>null</CODE>).
    */
    Field getField(FieldReference fr);
// OK

    /** 
	Returns the locally defined fields of the given type declaration.
	This method does not report inherited fields.
	The returned list matches the syntactic order of the declarations.
	@param  td a type declaration.
	@return a list of fields that are members of the declaration.
	@see ProgramModelInfo#getFields
	@see ProgramModelInfo#getAllFields	
    */
    FieldList getFields(ClassTypeDeclaration td); // was: TypeDeclaration
// OK

    /** 
	Returns the locally defined inner types of the given type declaration.
	This method does not report inherited types.
	The returned list matches the syntactic order of the declarations.
	@param  td a type declaration.
	@return a list of inner types that are members of the declaration.
	@see ProgramModelInfo#getTypes
	@see ProgramModelInfo#getAllTypes
    */
    DeclaredTypeList getTypes(TypeDeclaration td);
// OK - However it shall only have sense for ClassTypeDeclarations
    
    /**
       Returns the declared method.
       @param md a method declaration.
       @return the corresponding method.
     */
    Method getMethod(MethodDeclaration md);
// OK

    /** 
	Returns the locally defined methods of the given type declaration.
	This method does not report inherited methods.
	The returned list matches the syntactic order of the declarations.
	@param  td a type declaration.
	@return a list of methods that are members of the declaration.
	@see ProgramModelInfo#getMethods
	@see ProgramModelInfo#getAllMethods
    */
    MethodList getMethods(ClassTypeDeclaration td);
// OK - However it shall only have sense for ClassTypeDeclarations


    /**
       Returns the referred method, if there is an unambiguous one.
       @param mr the method reference to collate.
       @return the referred method.
       @exception AmbiguousReferenceException if there are no single most 
       specific applicable method.
       @exception UnresolvedReferenceException if there are no applicable 
       method.
       @see #getMethods(MethodReference)
     */
    Method getMethod(MethodReference mr);
// OK

    /**
       Returns all methods that are applicable with respect to the given
       reference. Only the most specific methods are returned; the program
       is not semantically correct if there is more than one such method.
       @param mr the method reference to collate.
       @return the list of applicable methods.
       @see #getMethod(MethodReference)
     */
    MethodList getMethods(MethodReference mr);
// OK
    
    /**
       Returns the declared constructor.
       @param cd a constructor declaration.
       @return the corresponding constructor.
     */
    Constructor getConstructor(ConstructorDeclaration cd);
// OK

    /** 
	Returns the locally defined constructors of the given type declaration.
	The returned list matches the syntactic order of the declarations.
	This method always returns a non-empty list, at least containing
	a {@link recoder.abstraction.DefaultConstructor}.
	@param  td a type declaration.
	@return a list of constructors that are members of the declaration.
	@see ProgramModelInfo#getConstructors
    */
    ConstructorList getConstructors(ClassTypeDeclaration td); // Was TypeDeclaration
// OK

    /**
       Returns the referred constructor, if there is an unambiguous one.
       @param cr the constructor reference to collate.
       @return the constructor referred.
       @exception AmbiguousReferenceException if there are is no most specific
       applicable constructor.
       @exception UnresolvedReferenceException if there are no applicable 
       constructor.
       @see #getConstructors(ConstructorReference)
     */
    Constructor getConstructor(ConstructorReference cr);
// OK

    /**
       Returns all constructors that are applicable with respect to the given
       reference. Only the most specific constructors are returned; the program
       is not semantically correct if there is more than one such constructor.
       @param cr the constructor reference to collate.
       @return the list of applicable constructors.
       @see #getConstructor(ConstructorReference)
     */
    ConstructorList getConstructors(ConstructorReference cr);
// OK
    
    /**
       Creates a signature by resolving the types of the given expression list.
       An empty or <CODE>null</CODE> expression list results in an empty
       type list.
       @param args a list of expressions (may be <CODE>null</CODE>).
       @return a list of types that correspond to the respective expressions.
     */
    TypeList makeSignature(ExpressionList args);
// OK

    /**
       Replaces the uncollated qualifier by its proper counterpart.
       Returns <CODE>null</CODE>, if this was not possible, the
       replaced reference otherwise. Note that this method does not
       notify the change history of the change!
       @param urq an uncollated reference.
       @return a resolved reference, or <CODE>null</CODE> if the reference
       could not be resolved.       
     */
    Reference resolveURQ(UncollatedReferenceQualifier urq);
// OK

    /**
       Check in a program element and built up all scopes. 
       @param pe the root of a syntax tree that shall be analyzed.
       This method was deprecated in recoder 0.72, but caused problems when loading classes on demand.
     */
    void register(ProgramElement pe);
// TODO: Erase this method then...
 

    /**
       Returns the syntactical counterpart of the given classtype.
       Returns <CODE>null</CODE>, if the given type is not a type declaration.
       @param ct a class type.
       @return the corresponding type declaration, or <CODE>null</CODE>, if 
       the given type has no syntactical representation.
     */
    TypeDeclaration getTypeDeclaration(DeclaredType ct); // Was: ClassType

    /**
       Returns the syntactical counterpart of the given method.
       Returns <CODE>null</CODE>, if the given method is not a method 
       declaration.
       @param m a method.
       @return the corresponding method declaration, or <CODE>null</CODE>, if
       the given method has no syntactical representation.
     */
    MethodDeclaration getMethodDeclaration(Method m);
// OK

    /**
       Returns the syntactical counterpart of the given method.
       Returns <CODE>null</CODE>, if the given method is not a method 
       declaration.
       @param m a method.
       @return the corresponding method declaration, or <CODE>null</CODE>, if
       the given method has no syntactical representation.
     */
    ConstructorDeclaration getConstructorDeclaration(Constructor c);
// OK

    /**
       Returns the syntactical counterpart of the given variable.
       Returns <CODE>null</CODE>, if the given variable is not a variable 
       specification.
       @param m a variable.
       @return the corresponding variable specification, or <CODE>null</CODE>, 
       if the given variable has no syntactical representation.
     */
    VariableSpecification getVariableSpecification(Variable v);
// OK

    /**
       This is a pseudo-statement representing the exit of a method
       as returned by {@link #getSucceedingStatements}.
       @since 0.71
     */
    Statement METHOD_EXIT = new EmptyStatement();

    /**
       Returns this list of statements that will possibly be executed
       after the specified statement. This method defines the edges of
       a control flow graph and obeys the Java compile-time definitions
       for reachability.
       The graph is not transitive: The last statement of a loop will
       return the loop as successor only, and the loop may have a
       successor that actually leaves the loop.
       Statement that can leave the top level statement block will
       report {@link #METHOD_EXIT} as a successor.
       @param s a statement.
       @return a list of succeeding statements.
       @since 0.71
     */
    StatementList getSucceedingStatements(Statement s);
// OK

    /**
       Adds a progress listener for the model update process.
       @since 0.72
     */
    void addProgressListener(ProgressListener l);
// OK

    /**
       Removes a progress listener for the model update process.
       @since 0.72
     */
    void removeProgressListener(ProgressListener l);
// OK

///////////////////////// CSHARP EXTENSIONS ////////////////////////////////

	/** Returns the associated delegate declaration with the delegate. 
	 */
	DelegateDeclaration getDelegateDeclaration(Delegate d);
	

}

