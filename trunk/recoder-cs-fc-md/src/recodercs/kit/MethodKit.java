// This file is part of the RECODER library and protected by the LGPL

package recodercs.kit;

import recodercs.ProgramFactory;
import recodercs.convenience.Naming;
import recodercs.convenience.TreeWalker;
import recodercs.abstraction.ClassType;
import recodercs.abstraction.Constructor;
import recodercs.abstraction.Method;
import recodercs.abstraction.PrimitiveType;
import recodercs.abstraction.Type;
import recodercs.service.CrossReferenceSourceInfo;
import recodercs.service.NameInfo;
import recodercs.service.ProgramModelInfo;
import recodercs.service.SourceInfo;
import recodercs.util.Debug;
import recodercs.*;
import recodercs.abstraction.*;
import recodercs.convenience.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;
import recodercs.csharp.declaration.modifier.*;
import recodercs.csharp.expression.*;
import recodercs.csharp.expression.operator.*;
import recodercs.csharp.reference.*;
import recodercs.csharp.statement.*;
import recodercs.io.*;
import recodercs.kit.problem.*;
import recodercs.list.*;
import recodercs.service.*;
import recodercs.util.*;

/**
   This class implements auxiliary method related operations.
   
   THIS CLASS HAS NOT YET BEEN REVISED FOR C#, but should be OK, since
   the method syntax in the two languages does not differ that much.
   
   @author Jozsef Orosz (C# adaptation)

   @author UA
   @author AL
   @author RN
   @author AM (getAllRelatedMethods)
      
*/
public class MethodKit
{

	private MethodKit()
	{}

	/** Creates a list of argument expressions from a parameter container.
	This method is usefull for creating wrapper methods since the actual
	parameters are taken from the given parameter list.
	 */
	public static ExpressionMutableList createArguments(ParameterContainer p)
	{
		int c = p.getParameterDeclarationCount();
		ExpressionMutableList res = new ExpressionArrayList(c);
		for (int i = 0; i < c; i += 1)
		{
			res.add(VariableKit.createVariableReference(p.getParameterDeclarationAt(i)));
		}
		return res;
	}

	/** Makes a method reference to the method declaration with the same
	actual argument names as in the declaration. For constructing
	adapters. Don't use a reference prefix.
	<P>
	The parent role of the result is valid.
	 */
	public static MethodReference createMethodReference(MethodDeclaration decl)
	{
		ProgramFactory factory = decl.getFactory();
		return factory.createMethodReference(
			factory.createIdentifier(decl.getName()),
			createArguments(decl));
	}

	/** makes a method reference to the method declaration with the same
	actual argument names as in the declaration. For constructing
	adapters. Use a reference prefix.
	<P>
	The parent role of the result is valid.
	*/
	public static MethodReference createMethodReference(
		ReferencePrefix prefix,
		MethodDeclaration decl)
	{
		ProgramFactory factory = decl.getFactory();
		return factory.createMethodReference(
			prefix,
			factory.createIdentifier(decl.getName()),
			createArguments(decl));
	}

	/**
	    Make a new allocation corresponding to the constructor declaration
	    with the same actual argument names as in the declaration. 
	 */
	public static New createNew(ConstructorDeclaration decl)
	{
		return decl.getFactory().createNew(
			null,
			TypeKit.createTypeReference(decl),
			createArguments(decl));
	}

	/**
	    Make an new abstract method declaration from a concrete one.
	    The given method may not be static.
	    If the method is for an interface, any existing redundant abstract
	    modifier is removed, otherwise its existance is ensured;
	    any visibility modifier is removed - this changes the
	    visibility to public.
	@deprecated not tested
	 */
	public static MethodDeclaration createAbstractMethodDeclaration(
		MethodDeclaration decl,
		boolean forInterface)
	{
		ProgramFactory factory = decl.getFactory();
		// create some prototypes
		if (decl.isStatic())
		{
			throw new IllegalArgumentException("A static method cannot made abstract!");
		}
		StatementBlock body = decl.getBody();
		decl.setBody(null); // not necessary to clone this.
		MethodDeclaration res = (MethodDeclaration) decl.deepClone();
		decl.setBody(body);
		Abstract anAbstract = factory.createAbstract();
		int abstractPos;
		ModifierMutableList modList = res.getModifiers();
		if (modList == null)
		{
			abstractPos = -1;
		} else
		{
			abstractPos = modList.indexOf(anAbstract);
		}
		VisibilityModifier vismod = res.getVisibilityModifier();
		if (forInterface)
		{
			// interfaces should not have an abstract
			if (abstractPos >= 0)
			{
				modList.remove(abstractPos);
			}
			// interfaces should not have a visibility modifier	    
			if (vismod != null)
			{
				modList.remove(modList.indexOf(vismod));
			}
		} else
		{
			if (abstractPos < 0)
			{
				// we need an abstract here
				if (modList == null)
				{
					res.setModifiers(modList = new ModifierArrayList(1));
				}
				modList.insert((vismod == null) ? 0 : 1, anAbstract);
			} else
			{
				return res; // already there
			}
		}
		return res;
	}

	/**
	   Create a simple adapter method for a method declaration. If the method is
	   <p> m(int i, int i2) { ..}
	   <p> the created method is
	   <p> m(int i, int i2) {  delegatingObject.m(i,i2); }
	*/
	public static MethodDeclaration createAdapterMethod(
		ReferencePrefix delegationObject,
		MethodDeclaration method)
	{
		MethodDeclaration clone = (MethodDeclaration) method.deepClone();
		clone.setComments(
			new CommentArrayList(
				new XmlDocComment("/** generated by createAdapterMethod */")));

		// empty the clone method and add the to the member list  
		clone.setBody(new StatementBlock(new StatementArrayList()));

		// add the adapter statements
		MethodReference call = createMethodReference(delegationObject, method);
		clone.getBody().getBody().add(call);
		return clone;
	}

	/**
	   Creates a packer class for parameter list of 
	   a method. This Packer class contains one
	   constructor which tuples all parameters of the method into an object.
	   Works on AST elements.
	   @deprecated needs severe rework (AL)
	 */
	public static ClassDeclaration createPackerClass(
		String packerClassName,
		ParameterDeclarationMutableList parameters)
	{
		StatementBlock statements;
		MemberDeclarationMutableList memberList;
		ClassDeclaration packClass;
		ParameterDeclaration parameter;
		ConstructorDeclaration constructor;

		Debug.printlno("debugPackifier", "creating packer class " + packerClassName);
		ProgramFactory factory = CSharpProgramFactory.getInstance();

		// Turn parameter list into member list of the Packer class.
		memberList = new MemberDeclarationArrayList();
		for (int j = 0; j < parameters.size(); j++)
		{
			parameter = parameters.getParameterDeclaration(j);

			// Don't destroy the original modifier list, copy it.
			ModifierMutableList modifierlist = new ModifierArrayList();
			// Parameter may not be public. 
			// All fields must be set to public.
			modifierlist.add(factory.createPublic());

			// XX normalize parameter decls //
			Identifier fieldName =
				factory.createIdentifier(
					parameter.getVariables().getVariableSpecification(0).getName());
			// Stupid, Name is no parameter to createFieldDeclaration// XX must be done for all arguments
			FieldDeclaration fd =
				factory.createFieldDeclaration(parameter.getTypeReference(), fieldName);
			fd.setModifiers(modifierlist);
			memberList.add(fd);
		}

		// Add a constructor to the Packer class.
		ModifierMutableList modifierlist = new ModifierArrayList();
		modifierlist.add(factory.createPublic());
		constructor =
			factory
				.createConstructorDeclaration(
					factory.createPublic(),
					factory.createIdentifier(packerClassName),
			//(ParameterDeclarationMutableList) parameters 
	null, null, statements = factory.createStatementBlock());

		// Create the initialization statements for all parameters
		// in the constructor.
		for (int j = 0; j < parameters.size(); j++)
		{
			parameter = parameters.getParameterDeclaration(j);
			String paramString =
				parameter.getVariables().getVariableSpecification(0).getName();
			// XX must be done for all arguments
			// XX should be an assign??
			FieldReference fieldRef =
				factory.createFieldReference(
					factory.createThisReference(),
					factory.createIdentifier(paramString));
			CopyAssignment assign =
				factory.createCopyAssignment(
					fieldRef,
					factory.createVariableReference(
						factory.createIdentifier(paramString)));
			statements.getBody().add(assign);
		}
		memberList.add(constructor);

		// Create the Packer class itself.
		modifierlist = new ModifierArrayList();
		modifierlist.add(factory.createPublic());
		// should we not add public anymore since the help class is within the
		// modified class?
		packClass =
			factory.createClassDeclaration(
				modifierlist,
				factory.createIdentifier(packerClassName),
				factory.createExtends(
					factory.createTypeReference(
						factory.createNamespaceReference(
							factory.createNamespaceReference(
								factory.createIdentifier("java")),
							factory.createIdentifier("lang")),
						factory.createIdentifier("Object"))),
				factory.createImplements(),
				memberList);

		Debug.printlno("debugPackifier", "created packer " + packerClassName);
		return packClass;
	}

	/**
	   Query that tries to identify getter methods for the given field
	   within the class declaration of the given field.
	   The criteria used are quite conservative and detect obvious cases of
	   "getters" only. A method is regarded a getter if it is defined in the
	   class of the field, has a return type wider than the type of the field
	   (or matching if they are primitive) and
	   has a return statement as last top level statement of the method body
	   referring to the field.
	   @param si the source info service to be used.
	   @param f the field to find a getter for.
	   @return the list of getters; may be empty if there are no getters that
	   match the criteria in the class.
	*/
	public static MethodDeclarationMutableList getGetters(
		SourceInfo si,
		FieldSpecification f)
	{
		Debug.asserta(si, f);
		MethodDeclarationMutableList res = new MethodDeclarationArrayList();
		TypeDeclaration tdecl = (TypeDeclaration) f.getContainingClassType();
		if (tdecl instanceof InterfaceDeclaration)
		{
			return res;
		}
		MemberDeclarationList mems = tdecl.getMembers();
		if (mems == null)
		{
			return res;
		}
		Type fieldType = si.getType(f);
		for (int i = mems.size() - 1; i >= 0; i -= 1)
		{
			MemberDeclaration md = mems.getMemberDeclaration(i);
			if (!(md instanceof MethodDeclaration))
			{
				continue;
			}
			MethodDeclaration m = (MethodDeclaration) md;
			if (fieldType instanceof PrimitiveType)
			{
				if (m.getReturnType() != fieldType)
				{
					continue;
				}
			} else
			{
				if (!si.isWidening(fieldType, m.getReturnType()))
				{
					continue;
				}
			}
			StatementBlock body = m.getBody();
			if (body == null)
			{
				continue;
			}
			StatementList statements = body.getBody();
			if (statements == null)
			{
				continue;
			}
			Statement last = statements.getStatement(statements.size() - 1);
			if (!(last instanceof Return))
			{
				continue;
			}
			Expression expr = ((Return) last).getExpression();
			if (!(expr instanceof FieldReference))
			{
				continue;
			}
			FieldReference fr = (FieldReference) expr;
			if (si.getField(fr) == f)
			{
				res.add(m);
			}
		}
		return res;
	}

	// DISABLED: Was deprecated.

	//	/**
	//	   Transformation that renames a method and all known references
	//	   to it. The new name should not hide another method.
	//	   @param ch the change history (may be <CODE>null</CODE>).
	//	   @param xr the cross referencer service.
	//	   @param method the method declaration to be renamed;
	//	   may neither be <CODE>null</CODE> nor a constructor declaration.
	//	   @param newName the new name for the method;
	//	   may not be <CODE>null</CODE> and must denote a valid identifier name.
	//	   @return <CODE>true</CODE>, if a rename has been necessary,
	//	   <CODE>false</CODE> otherwise.
	//	   @deprecated replaced by recoder.kit.transformation.RenameMethod
	//	 */
	//	public static boolean rename(
	//		ChangeHistory ch,
	//		CrossReferenceSourceInfo xr,
	//		MethodDeclaration method,
	//		String newName) {
	//		Debug.assert(xr, method, newName);
	//		Debug.assert(method.getName());
	//		Debug.assert(!(method instanceof ConstructorDeclaration));
	//		if (!newName.equals(method.getName())) {
	//			MemberReferenceList refs = xr.getReferences(method);
	//			MiscKit.rename(ch, method, newName);
	//			for (int i = refs.size() - 1; i >= 0; i -= 1) {
	//				MiscKit.rename(ch, (MethodReference) refs.getMemberReference(i), newName);
	//			}
	//			return true;
	//		}
	//		return false;
	//	}

	/**
	   Query that returns a list of methods that the given method directly
	   overwrites or implements. A method that is multiply inherited
	   (from interfaces) occurs multiple times, accordingly.
	   @param m a method.
	   @return a list of methods that are overwritten or implemented by
	   <CODE>m</CODE>.
	*/
	public static MethodList getRedefinedMethods(Method m)
	{
		Debug.asserta(m);
		if (m instanceof Constructor)
		{
			return MethodList.EMPTY_LIST;
		}
		ClassType ct = m.getContainingClassType();
		String mname = m.getName();
		TypeList msig = m.getSignature();
		MethodMutableList result = new MethodArrayList();
		ClassTypeList supers = ct.getSupertypes();
		for (int i = supers.size() - 1; i >= 0; i -= 1)
		{
			MethodList meths = supers.getClassType(i).getAllMethods();
			for (int j = meths.size() - 1; j >= 0; j -= 1)
			{
				Method m2 = meths.getMethod(j);
				if (m2.getName().equals(mname) && m2.getSignature().equals(msig))
				{
					result.add(m2);
				}
			}
		}
		return result;
	}

	/**
	   Query that returns a list of methods that redefine or implement
	   the given method.
	   @param xr the cross referencer service to use.
	   @param m a method.
	   @return a list of methods that redefine or implement
	   <CODE>m</CODE>.
	*/
	public static MethodList getRedefiningMethods(CrossReferenceSourceInfo xr, Method m)
	{
		Debug.asserta(m);
		if (m instanceof Constructor)
		{
			return MethodList.EMPTY_LIST;
		}
		ClassType ct = m.getContainingClassType();
		String mname = m.getName();
		TypeList msig = m.getSignature();
		MethodMutableList result = new MethodArrayList();
		ClassTypeList subs = xr.getAllSubtypes(ct);
		for (int i = subs.size() - 1; i >= 0; i -= 1)
		{
			MethodList meths = subs.getClassType(i).getMethods();
			for (int j = meths.size() - 1; j >= 0; j -= 1)
			{
				Method m2 = meths.getMethod(j);
				if (m2.getName().equals(mname) && m2.getSignature().equals(msig))
				{
					result.add(m2);
				}
			}
		}
		return result;
	}

	/**
	   Updating query that checks if the given method is a main method.
	   @param ni the NameInfo service to use.
	   @param m the method to check.
	   @return <CODE>true</CODE> if the given method has the form
	   "public static void Main(String[] ...)", <CODE>false</CODE>
	   otherwise.
	 */
	public static boolean isMain(NameInfo ni, Method m)
	{
		if (!m.isPublic())
		{
			return false;
		}
		if (!m.isStatic())
		{
			return false;
		}
		if (!m.getName().equals("Main"))
		{
			return false;
		}
		if (m.getReturnType() != null)
		{
			return false;
		}
		TypeList list = m.getSignature();
		if (list.size() != 1)
		{
			return false;
		}
		// we do not have to create an array type, as this would have been
		// done by the getSignature call already.
		return list.getType(0) == ni.getArrayType(ni.getSystemString(), 1);
		// Should be String[]
	}

	// DISABLED: This has no sense in C#

	//	/**
	//	   Updating query that checks if the given method is one of the
	//	   serialization methods <CODE>writeObject</CODE>, <CODE>readObject</CODE>,
	//	   <CODE>writeReplace</CODE>, <CODE>readResolve</CODE>.
	//	   @param ni the NameInfo service to use.
	//	   @param m the method to check.
	//	   @return <CODE>true</CODE> if the given method is one of the
	//	   serialization methods, <CODE>false</CODE> otherwise.
	//	 */
	//	public static boolean isSerializationMethod(NameInfo ni, Method m) {
	//		if (m.getName().equals("writeObject")
	//			&& m.isPrivate()
	//			&& m.getReturnType() == null
	//			&& m.getSignature().size() == 1
	//			&& m.getSignature().getType(0)
	//				== ni.getClassType("java.io.ObjectOutputStream")) {
	//			return true;
	//		}
	//		if (m.getName().equals("readObject")
	//			&& m.isPrivate()
	//			&& m.getReturnType() == null
	//			&& m.getSignature().size() == 1
	//			&& m.getSignature().getType(0)
	//				== ni.getClassType("java.io.ObjectInputStream")) {
	//			return true;
	//		}
	//		if (m.getName().equals("writeReplace")
	//			&& m.getReturnType() == ni.getSystemObject()
	//			&& m.getSignature().isEmpty()) {
	//			return true;
	//		}
	//		if (m.getName().equals("readResolve")
	//			&& m.getReturnType() == ni.getSystemObject()
	//			&& m.getSignature().isEmpty()) {
	//			return true;
	//		}
	//		return false;
	//	}

	/**
	   Returns a deep clone of the header of the given declaration;
	   the body of the result is <CODE>null</CODE>.
	   @param md the method declaration to clone the header from.
	   @return a new method declaration sharing the header with the given one.
	   @see recodercs.csharp.SourceElement#deepClone()
	 */
	public static MethodDeclaration cloneHeader(MethodDeclaration md)
	{
		StatementBlock body = md.getBody();
		md.setBody(null);
		MethodDeclaration result = (MethodDeclaration) md.deepClone();
		md.setBody(body);
		return result;
	}

	/**
	   Query returning a method locally defined in the given type with the
	   given name and signature.
	   @param type the class type the method might be defined in.
	   @param name the name of the method.
	   @param signature the signature of the method.
	   @return the method as defined in the class type, or <CODE>null</CODE>
	   if there is no match.
	 */
	public static Method getDefinedMethod(
		ClassType type,
		String name,
		TypeList signature)
	{
		MethodList methods = type.getMethods();
		for (int j = methods.size() - 1; j >= 0; j -= 1)
		{
			Method m = methods.getMethod(j);
			if (name.equals(m.getName()) && signature.equals(m.getSignature()))
			{
				return m;
			}
		}
		return null;
	}

	/**
	   Query returning the methods which a method in the given class with the
	   given name and signature would redefine. If there are several
	   candidates from independent interfaces, the bottom-most ones are
	   reported. If there is a version defined in a super class, it will
	   be the first entry in the list (position 0).
	   @param ni the name info service to use.
	   @param base the class type which would contain the redefining method.
	   @param name the name of the possibly redefining method.
	   @param signature the signature of the possibly redefining method.
	   @return a list of methods that are directly redefined by a method with
	   the given name and signature; the first entry is the method inherited
	   from a class, if any.
	*/
	public static MethodList getRedefinedMethods(
		NameInfo ni,
		ClassType base,
		String name,
		TypeList signature)
	{
		ClassTypeList supers = base.getSupertypes();
		MethodMutableList result = new MethodArrayList();
		boolean hasClass = false;
		for (int i = 0; i < supers.size(); i += 1)
		{
			ClassType ct = supers.getClassType(i);
			Method m = getDefinedMethod(ct, name, signature);
			if (m != null)
			{
				if (!ct.isInterface())
				{
					result.insert(0, m);
					hasClass = true;
				} else
				{
					result.add(m);
				}
			}
		}
		if (!hasClass)
		{
			ClassType ct = base;
			do
			{
				ct = TypeKit.getSuperClass(ni, ct);
				Method m = getDefinedMethod(ct, name, signature);
				if (m != null)
				{
					result.insert(0, m);
					break;
				}
			} while (ct != ni.getSystemObject());
		}
		return result;
	}

	/**
	   Query that finds out problems if one method redefines another one.
	   The redefining method does not have to actually redefine the other
	   method, the query just assumes it does. The query also assumes that
	   names and signatures of the methods will match and that both have
	   the same class. The query will not check any contents of the
	   redefining method, e.g. to see if private members of the 
	   super class are accessed.
	   @param pmi a program model info to use.
	   @param redefined the method to be redefined.
	   @param redefining the method that is / would be redefining.
	   @return a problem report, one of the following:
	   <UL>
	   <LI>FinalOverwrite, if the redefined method is final;
	   <LI>DifferentReturnTypeOverwrite, if the redefining method has a
	   different return type;
	   <LI>MorePrivateOverwrite, if the redefining method is more private;
	   <LI>NonStaticOverwrite, if the redefined method is static but the
	   redefining is not (if both are static, no problem is reported, even
	   though no real redefinition is taking place);
	   <LI>UncoveredExceptionsOverwrite, if the redefined method is
	   less exceptional;
	   <LI><CODE>null</CODE>, otherwise.
	   </UL>   
	*/
	public static Problem checkMethodRedefinition(
		ProgramModelInfo pmi,
		Method redefined,
		Method redefining)
	{

		if (redefining instanceof Constructor)
		{
			return null;
		}
		if (redefined.isSealed() || redefined.getContainingClassType().isSealed())
		{
			return new FinalOverwrite(redefined);
		}
		if (redefined.getReturnType() != redefining.getReturnType())
		{
			return new DifferentReturnTypeOverwrite(redefined);
		}
		if (TypeKit.isLessVisible(redefining, redefined))
		{
			return new MorePrivateOverwrite(redefined);
		}
		if (!redefining.isStatic() && redefined.isStatic())
		{
			return new NonStaticOverwrite(redefined);
		}
		// check exceptions
		ClassTypeList exceptions = redefining.getExceptions();
		if (exceptions != null)
		{
			ClassTypeList redefinedex = redefined.getExceptions();
			if (redefinedex == null || !TypeKit.isCovered(pmi, redefinedex, exceptions))
			{
				return new UncoveredExceptionsOverwrite(redefined);
			}
		}
		return null;
	}

	/**
	   Query that finds out problems before inserting a new method declaration.
	   @param ni the name info to use.
	   @param si the source info to use.
	   @param context the future context of the method.
	   @param candidate the method declaration that might be inserted.
	   @return a problem report, one of the following:
	   <UL>
	   <LI>IllegalInterfaceMember,
	   if the context is an interface and the candidate is not a valid member;
	   <LI>IllegalName, if the name is a keyword;
	   <LI>NameConflict, if the candidate is a constructor
	   and its name does not match the type name;
	   <LI>NameConflict, if there is a method in the context with the same name
	   and signature;
	   <LI>FinalOverwrite, if there is a redefined method that is final;
	   <LI>DifferentReturnTypeOverwrite, if there is a redefined method with
	   different return type;
	   <LI>MorePrivateOverwrite, if there is a redefined method that is more
	   public;
	   <LI>NonStaticOverwrite, if there is a redefined method that is static;
	   <LI>UncoveredExceptionsOverwrite, if there is a redefined method that is
	   less exceptional;
	   <LI><CODE>null</CODE>, otherwise.
	   </UL>   
	*/
	public static Problem checkMethodDeclaration(
		NameInfo ni,
		SourceInfo si,
		ClassTypeDeclaration context,
		MethodDeclaration candidate)
	{

		if (context instanceof InterfaceDeclaration)
		{
			if (!TypeKit.isValidInterfaceMember(candidate))
			{
				return new IllegalInterfaceMember(candidate);
			}
		}
		if (candidate instanceof Constructor)
		{
			if (!candidate.getName().equals(context.getName()))
			{
				return new NameConflict(context);
			}
		} else
		{
			if (Naming.isKeyword(candidate.getName()))
			{
				return new IllegalName(candidate);
			}
		}
		MemberDeclarationList members = context.getMembers();
		String name = candidate.getName();
		TypeList signature = candidate.getSignature();
		if (members != null)
		{
			for (int i = members.size() - 1; i >= 0; i -= 1)
			{
				MemberDeclaration md = members.getMemberDeclaration(i);
				if (md instanceof MethodDeclaration)
				{
					MethodDeclaration m = (MethodDeclaration) md;
					if (m.getName().equals(name) && m.getSignature().equals(signature))
					{
						return new NameConflict(m);
					}
				}
			}
		}

		if (candidate instanceof Constructor)
		{
			return null;
		}
		MethodList redefined =
			MethodKit.getRedefinedMethods(ni, context, name, signature);
		for (int i = 0; i < redefined.size(); i += 1)
		{
			Problem problem =
				checkMethodRedefinition(si, redefined.getMethod(i), candidate);
			if (problem != null)
			{
				return problem;
			}
		}
		return null;
	}

	/**
	   Query that retrieves all references to a given method that are contained
	   within the given tree. The specified flag defines the strategy to use:
	   either the cross reference information is filtered, or the cross
	   reference information is collected from the tree. The filtering
	   mode is faster if the tree contains more nodes than there are global 
	   references to the given method.
	   @param xr the cross referencer to use.
	   @param m a method.
	   @param root the root of an arbitrary syntax tree.
	   @param scanTree flag indicating the search strategy; if 
	   <CODE>true</CODE>, local cross reference information is build,
	   otherwise the global cross reference information is filtered.
	   @return the list of references to the given method in the given tree,
	   can be empty but not <CODE>null</CODE>.
	   @since 0.63
	 */
	public static MemberReferenceMutableList getReferences(
		CrossReferenceSourceInfo xr,
		Method m,
		NonTerminalProgramElement root,
		boolean scanTree)
	{
		Debug.asserta(xr, m, root);
		MemberReferenceMutableList result = new MemberReferenceArrayList();
		if (scanTree)
		{
			TreeWalker tw = new TreeWalker(root);
			if (m instanceof Constructor)
			{
				while (tw.next(ConstructorReference.class))
				{
					ConstructorReference cr =
						(ConstructorReference) tw.getProgramElement();
					if (xr.getConstructor(cr) == m)
					{
						result.add(cr);
					}
				}
			} else
			{
				while (tw.next(MethodReference.class))
				{
					MethodReference mr = (MethodReference) tw.getProgramElement();
					if (xr.getMethod(mr) == m)
					{
						result.add(mr);
					}
				}
			}
		} else
		{
			MemberReferenceList refs = xr.getReferences(m);
			for (int i = 0, s = refs.size(); i < s; i += 1)
			{
				MemberReference mr = (MemberReference) refs.getMemberReference(i);
				if (MiscKit.contains(root, mr))
				{
					result.add(mr);
				}
			}
		}
		return result;
	}

	/**
	   @author AM
	 */
	private static class RelatedMethodsHelper
	{

		private MethodMutableList methods = new MethodArrayList();
		private MutableSet searchedUp = new IdentityHashSet();
		private MutableSet searchedDown = new IdentityHashSet();
		private CrossReferenceSourceInfo xrsi;
		private ClassType type;
		private String methodName;
		private TypeList signature;

		public RelatedMethodsHelper(
			CrossReferenceSourceInfo xrsi,
			ClassType type,
			String methodName,
			TypeList signature)
		{
			this.xrsi = xrsi;
			this.methodName = methodName;
			this.signature = signature;
			this.type = type;
		}

		public MethodList findRelatedMethods()
		{
			addMethodsFromSubTypes(type);
			return methods;
		}

		private void addMethodsFromSubTypes(ClassType type)
		{
			if (searchedDown.add(type) == type)
			{
				return;
			}
			ClassTypeList subTypes = xrsi.getSubtypes(type);
			if (subTypes.isEmpty())
			{
				// leaf class
				addMethodsFromSuperTypes(type);
			} else
			{
				for (int i = subTypes.size() - 1; i >= 0; i--)
				{
					ClassType child = subTypes.getClassType(i);
					addMethodsFromSubTypes(child);
				}
			}
		}

		private void addMethodsFromSuperTypes(ClassType type)
		{
			if (searchedUp.add(type) == type)
			{
				return;
			}
			Method m = getDefinedMethod(type, methodName, signature);
			if (m != null)
			{
				methods.add(m);
				addMethodsFromSubTypes(type);
			}
			ClassTypeList superTypes = type.getSupertypes();
			for (int i = superTypes.size() - 1; i >= 0; i--)
			{
				ClassType parent = superTypes.getClassType(i);
				addMethodsFromSuperTypes(parent);
			}
		}
	}

	/**
	   Query that returns a list of methods would redefine, implement
	   or are overriden or implemented each other starting from method
	   <CODE>methodName</CODE> in <CODE>type</CODE> with specified
	   <CODE>signature</CODE>.  The method does not have to actually
	   exist in <CODE>type</CODE> the query just assumes it does.
	   @param xrsi the cross referencer service to use.
	   @param type the type which contain method.
	   @param methodName name of the method.
	   @param signature method signature.
	   @return a list of related methods.
	   @since 0.72
	*/
	public static MethodList getAllRelatedMethods(
		CrossReferenceSourceInfo xrsi,
		ClassType type,
		String methodName,
		TypeList signature)
	{
		Debug.asserta(xrsi, type, methodName, signature);

		RelatedMethodsHelper rmh =
			new RelatedMethodsHelper(xrsi, type, methodName, signature);
		return rmh.findRelatedMethods();
	}

	/**
	   Query that returns a list of methods that redefine, implement
	   or are overriden or implemented each other starting from method
	   <CODE>method</CODE>.  There are some cases where related
	   methods might be outside of descendants or ascendants of type
	   containing <CODE>method</CODE>. For instance,
	   <CODE>Collection.size()</CODE> is related to
	   <CODE>Dictionary.size()</CODE>, because <CODE>Hashtable</CODE>
	   extends <CODE>Dictionary</CODE> and indirectly implements
	   <CODE>Collection</CODE>.
	   @param xrsi the cross referencer service to use.
	   @param method a method.
	   @return a list of related methods including <CODE>method</CODE>.
	   @since 0.72
	*/
	public static MethodList getAllRelatedMethods(
		CrossReferenceSourceInfo xrsi,
		Method method)
	{
		Debug.asserta(method);
		return getAllRelatedMethods(
			xrsi,
			method.getContainingClassType(),
			method.getName(),
			method.getSignature());
	}

	public static MethodList getAllRelatedMethods(
		NameInfo ni,
		CrossReferenceSourceInfo xrsi,
		ClassType type,
		String methodName,
		TypeList signature)
	{
		MutableSet visited = new IdentityHashSet();
		Queue q = new Queue();
		q.enqueue(type);
		visited.add(type);
		MethodMutableList result = new MethodArrayList();
		while (!q.isEmpty())
		{
			type = (ClassType) q.dequeue();
			Method m = getDefinedMethod(type, methodName, signature);
			if (m != null)
			{
				result.add(m);
			}
			MethodList redefined = getRedefinedMethods(ni, type, methodName, signature);
			for (int i = redefined.size() - 1; i >= 0; i--)
			{
				ClassType ct = redefined.getMethod(i).getContainingClassType();
				if (visited.add(ct) == null)
				{
					q.enqueue(ct);
				}
			}
			if (m != null || !redefined.isEmpty())
			{
				ClassTypeList types = xrsi.getSubtypes(type);
				for (int i = types.size() - 1; i >= 0; i--)
				{
					ClassType ct = types.getClassType(i);
					if (visited.add(ct) == null)
					{
						q.enqueue(ct);
					}
				}
			}
		}
		return result;
	}

}
