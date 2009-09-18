// This file is part of the RECODER library and protected by the LGPL.

package recoder.kit;

import recoder.*;
import recoder.abstraction.*;
import recoder.abstraction.Namespace;
import recoder.convenience.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;
import recoder.csharp.expression.*;
import recoder.csharp.expression.operator.*;
import recoder.csharp.statement.*;
import recoder.csharp.reference.*;
import recoder.list.*;
import recoder.service.*;
import recoder.util.Debug;

/** 
 * @author Jozsef Orosz (adaptation for C#)
 * @author <others>
 * 
 */
public class ExpressionKit {

	private ExpressionKit() {}

	/**
	   Query deciding if the given expression tree contains statements
	   as a conservative estimate if it has side effects. An expression
	   that contains no statements (method calls, assignments) cannot
	   have any side-effects. Parenthesized expressions are not considered
	   statements in this context, even though they technically may appear
	   as such.
	   @param expr an expression.
	   @return <CODE>true</CODE>, if the expression contains expressions,
	   <CODE>false</CODE> if it does not.
	 */
	public static boolean containsStatements(Expression expr) {
		if (expr instanceof Statement) {
			if (!(expr instanceof ParenthesizedExpression)) {
				return true;
			}
		}
		if (expr instanceof ExpressionContainer) {
			ExpressionContainer con = (ExpressionContainer) expr;
			for (int i = 0, s = con.getExpressionCount(); i < s; i += 1) {
				if (containsStatements(con.getExpressionAt(i))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	   Non-updating query deciding if the given expression is used
	   as a left-hand value ("L-value").
	   L-values are either variables, or array references.
	   As there are no call-by-reference output parameters in Java, L-value
	   references must occur as first argument of an assigment operator 
	   such as <CODE>=</CODE> or <CODE>++</CODE>.
	   @param r an expression.
	   @return <CODE>true</CODE> if the specified expression is an
	   L-value, <CODE>false</CODE> otherwise.
	   @since 0.63
	 */
	public static boolean isLValue(Expression r) {
		
		if ((r instanceof VariableReference) || (r instanceof ArrayReference)) {
			ExpressionContainer c = r.getExpressionContainer();
			return (c instanceof Assignment) && (c.getExpressionAt(0) == r);
		}
		
		return false;
	}

	/**
	   Query that collects all expressions that are evaluated before the
	   given expression in its statement or initializer in the correct order.
	   
	   <b>THIS METHOD HAS NOT YET BEEN REVISED FOR C#.</b>
	   
	   @param x an expression as part of a statement or an initializer.
	   @return a mutable list of expressions that preceed the given one.
	 */
	public static ExpressionMutableList collectPreceedingExpressions(Expression x) {
		Debug.asserta(x);
		
		ExpressionMutableList dest = new ExpressionArrayList();
		
		if ((x instanceof MethodReference) || (x instanceof ConstructorReference)) {
			ExpressionContainer ec = (ExpressionContainer) x;
			for (int i = 0, s = ec.getExpressionCount(); i < s; i += 1) {
				dest.add(ec.getExpressionAt(i));
			}
		} else if (x instanceof ReferenceSuffix) {
			ReferencePrefix rp = ((ReferenceSuffix) x).getReferencePrefix();
			if (rp instanceof Expression) {
				dest.add((Expression) rp);
			}
		}
		
		while (true) {
			ExpressionContainer parent = x.getExpressionContainer();
			if (parent == null) {
				return dest;
			}

			boolean leftAssociative;

			if (parent instanceof Operator) {
				leftAssociative = ((Operator) parent).isLeftAssociative();
			} else {
				// all non-operator expression containers such as method calls
				// or array initializers are left-associative
				leftAssociative = true;
			}

			// collect all child expressions of parent that are evaluated before x
			Expression expr;
			if (leftAssociative) {
				int i = 0;
				if (parent instanceof ReferenceSuffix) {
					if (((ReferenceSuffix) parent).getReferencePrefix()
						instanceof Expression) {
						i = 1;
					}
				}
				while ((expr = parent.getExpressionAt(i)) != x) {
					dest.add(expr);
					i += 1;
				}
			} else {
				for (int i = parent.getExpressionCount() - 1;
					(expr = parent.getExpressionAt(i)) != x;
					i -= 1) {
					dest.add(expr);
				}
			}

			if (!(parent instanceof Expression)) {
				return dest;
			}
			x = (Expression) parent;
		}
	}

// DISABLED: Was either way a deprecated method.
//
//	/**
//	   Transformation that ensures that the given expression is
//	   evaluated first during execution of the resulting statement,
//	   while preserving the behavior. The method changes all statement
//	   expressions (which might have side-effects) that are evaluated
//	   before the given expression into initializations of temporary
//	   variables. These will preceed the statement the given
//	   expression is located within. If the expression is contained in
//	   a statement, a statement block is inserted if needed. If the
//	   expression is part of a field initializer, a new class
//	   initializer with appropriate modifier executing the
//	   initialization code is inserted before the field.
//	   <BR>
//	   To obtain the statement the expression is located in,
//	   a parent traversal beginning at the given expression (which is
//	   relocated) and ending at the first non-expression statement
//	   is sufficient. In case of the field specification, the expression
//	   is contained within the assignment to the field in the newly
//	   created class or object initializer block.
//	   @param si the source info service.
//	   @param x the expression that shall be accessed first in its statement
//	   or initializer.
//	   @param ch the change history service (may be <CODE>null</CODE>).
//	   @return <CODE>true</CODE> if the shift has been necessary,
//	   <CODE>false</CODE> otherwise.
//	   @deprecated replaced by transformation
//	*/
//	public static boolean shiftPreceedingStatementExpressions(
//		SourceInfo si,
//		Expression x,
//		ChangeHistory ch) {
//
//		// get all expressions that are executed before x
//		ExpressionMutableList exprs = collectPreceedingExpressions(x);
//		// retain only expressions that might have side-effects
//		for (int i = exprs.size() - 1; i >= 0; i -= 1) {
//			if (!containsStatements(exprs.getExpression(i))) {
//				exprs.remove(i);
//			}
//		}
//		if (exprs.isEmpty()) {
//			return false;
//		}
//		ProgramFactory f = x.getFactory();
//		int exSize = exprs.size();
//		StatementMutableList tempVarDecls = new StatementArrayList(exSize);
//		ScopeDefiningElement sde = MiscKit.getScopeDefiningElement(x);
//		Type[] exTypes = new Type[exSize];
//		for (int i = 0; i < exSize; i += 1) {
//			Expression ex = exprs.getExpression(i);
//			exTypes[i] = si.getType(ex);
//		}
//		String[] varNames = VariableKit.getNewVariableNames(si, exTypes, sde);
//		for (int i = 0; i < exSize; i += 1) {
//			// create local temporary variable declarations for remaining exprs
//			Expression ex = exprs.getExpression(i);
//			Type t = exTypes[i];
//			TypeReference minTypeRef = TypeKit.createTypeReference(si, t, sde);
//			String varName = varNames[i];
//			LocalVariableDeclaration lvd =
//				f.createLocalVariableDeclaration(minTypeRef, f.createIdentifier(varName));
//			lvd.getVariables().getVariableSpecification(0).setInitializer(ex);
//			// lvd.makeAllParentRolesValid(); done later
//			tempVarDecls.add(lvd);
//
//			// replace old expressions by variable references
//			VariableReference vref =
//				f.createVariableReference(f.createIdentifier(varName));
//			ex.getASTParent().replaceChild(ex, vref);
//			Debug.assert(vref.getASTParent());
//			if (ch != null) {
//				ch.replaced(ex, vref);
//			}
//		}
//
//		// get destination statement list and position to insert into
//		StatementMutableList destination = null;
//		NonTerminalProgramElement destParent = null;
//		int destIndex = 0;
//		ProgramElement pe = x;
//		do {
//			NonTerminalProgramElement parent = pe.getASTParent();
//			Debug.assert(parent);
//			if ((parent instanceof Statement)
//				&& (((Statement) parent).getStatementContainer() != null)) {
//				Statement parentStatement = (Statement) parent;
//				destination =
//					StatementKit.prepareStatementMutableList(parentStatement, ch);
//				destParent = parentStatement.getStatementContainer();
//				for (destIndex = 0;
//					destination.getStatement(destIndex) != parent;
//					destIndex += 1);
//				break;
//			}
//			if (parent instanceof FieldSpecification) {
//
//				// create class initializer and insert it before the field
//				// Replaced by a static constructor declaration.
//				// TODO: Add a check, that there is only one 
//				// Static constructor declaration.
//
//				System.err.println(
//					"WARNING: ExpressionKit::shiftPreceedingStatementExpression is not fully functional yet.\n"
//						+ "Please check results.");
//
//				FieldSpecification fs = (FieldSpecification) parent;
//				FieldDeclaration fd = (FieldDeclaration) fs.getParent();
//				destination = new StatementArrayList();
//				StatementBlock body = f.createStatementBlock(destination);
//				StaticConstructorDeclaration ci;
//				if (fd.isStatic()) {
//					ci = ((CSharpProgramFactory) f).createStaticConstructorDeclaration();
//					ModifierMutableList ml = new ModifierArrayList();
//					ml.add(f.createStatic());
//					ci.setBody(body);
//					ci.setModifiers(ml);
//				} else {
//					// TODO
//					// Well, this should not be static...
//					// Should not even be a static initializer...
//
//					ci = ((CSharpProgramFactory) f).createStaticConstructorDeclaration();
//					ModifierMutableList ml = new ModifierArrayList();
//					ml.add(f.createStatic());
//					ci.setBody(body);
//					ci.setModifiers(ml);
//				}
//				// ci.makeAllParentRolesValid(); done later
//				destParent = ci;
//				TypeDeclaration tdecl = fd.getMemberParent();
//				MemberDeclarationMutableList mdml = tdecl.getMembers();
//				mdml.insert(mdml.indexOf(fd) + 1, ci);
//				ci.setMemberParent(tdecl); // manual parent link validation
//				// tdecl.makeParentRoleValid(); 
//				if (ch != null) {
//					ch.attached(ci);
//				}
//				// shift field specification initializer to the new block
//				Expression init = fs.getInitializer(); // contains expr
//				int initIndex = fs.getIndexOfChild(init);
//				fs.setInitializer(null); // erase initializer
//				if (ch != null) {
//					ch.detached(init, initIndex);
//					// parent link is still valid
//				}
//				CopyAssignment ca =
//					f.createCopyAssignment(
//						f.createVariableReference(f.createIdentifier(fs.getName())),
//						init);
//				ca.makeAllParentRolesValid();
//				destination.add(ca); // add to end of body list
//				// we already reported ci (parent of ca) as attached
//
//				destIndex = 0;
//				break;
//			}
//			pe = parent;
//		} while (true);
//		// insert variable declarations into statement block
//		destination.insert(destIndex, tempVarDecls);
//		destParent.makeAllParentRolesValid();
//		if (ch != null) {
//			for (int i = 0; i < tempVarDecls.size(); i += 1) {
//				ch.attached(tempVarDecls.getStatement(i));
//			}
//		}
//		return true;
//	}

	/**
	   Factory method that creates the default literal to a given type.
	   For non-primitive type, the result is a
	   {@link recoder.csharp.expression.literal.NullLiteral},
	   for primitive types their corresponding default value (<CODE>0</CODE>,
	   <CODE>false</CODE>, <CODE>'\0'</CODE>).
	   @param f the program factory for the literal to create.
	   @param ni the name info defining the primitive type objects.
	   @param t the type to create a default value for.
	   @return a new literal object widening to the given type.
	 */
	public static Literal createDefaultValue(ProgramFactory f, NameInfo ni, Type t) {
		Debug.asserta(f, ni, t);
		if (t instanceof PrimitiveType) {
			
			if (t == ni.getIntType() || t == ni.getUintType()) {
				return f.createIntLiteral(0);
			}
			if (t == ni.getBooleanType()) {
				return f.createBooleanLiteral(false);
			}
			if (t == ni.getCharType()) {
				return f.createCharLiteral('\0');
			}
			if (t == ni.getShortType() || t == ni.getUshortType()) {
				return f.createIntLiteral(0);
			}
			if (t == ni.getByteType() || t == ni.getUlongType()) {
				return f.createIntLiteral(0);
			}
			if (t == ni.getLongType() || t == ni.getUlongType()) {
				return f.createLongLiteral(0L);
			}
			if (t == ni.getFloatType()) {
				return f.createFloatLiteral(0.0f);
			}
			if (t == ni.getDoubleType()) {
				return f.createDoubleLiteral(0.0);
			}
			if (t == ni.getDecimalType())  {
				return f.createIntLiteral(0);
			}
			throw new IllegalArgumentException("Unknown primitive type " + t.getName());
		} else {
			return f.createNullLiteral();
		}
	}

}
