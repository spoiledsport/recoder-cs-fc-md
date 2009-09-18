// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp;

import recoder.*;
import recoder.list.*;
import recoder.util.*;
import recoder.csharp.attributes.Attribute;
import recoder.csharp.attributes.AttributeArgument;
import recoder.csharp.attributes.AttributeSection;
import recoder.csharp.attributes.NamedAttributeArgument;
import recoder.csharp.attributes.targets.AssemblyTarget;
import recoder.csharp.attributes.targets.EventTarget;
import recoder.csharp.attributes.targets.FieldTarget;
import recoder.csharp.attributes.targets.MethodTarget;
import recoder.csharp.attributes.targets.ModuleTarget;
import recoder.csharp.attributes.targets.ParamTarget;
import recoder.csharp.attributes.targets.PropertyTarget;
import recoder.csharp.attributes.targets.ReturnTarget;
import recoder.csharp.attributes.targets.TypeTarget;
import recoder.csharp.declaration.*;
import recoder.csharp.declaration.modifier.*;
import recoder.csharp.declaration.modifier.Override;
import recoder.csharp.reference.*;
import recoder.csharp.expression.*;
import recoder.csharp.expression.literal.*;
import recoder.csharp.expression.operator.*;
import recoder.csharp.statement.*;

/**
   A source visitor defines actions to be triggered while visiting source 
   elements.
   The {@link recoder.csharp.PrettyPrinter} is an instance of
   this visitor.
 */
public abstract class SourceVisitor {

	/**
	   Visits the specified compilation unit.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitCompilationUnit(CompilationUnit x) {}

	/**
	   Visits the specified identifier.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitIdentifier(Identifier x) {}

	/**
	   Visits the specified import.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitUsing(Using x) {}

	/**
	   Visits the specified package specification.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitNamespaceSpecification(NamespaceSpecification x) {}

	/**
	   Visits the specified statement block.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitStatementBlock(StatementBlock x) {}

	/**
	   Visits the specified class declaration.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitClassDeclaration(ClassDeclaration x) {}

	/**
	   Visits the specified constructor declaration.
	   The default implementation calls {@link #visitMethodDeclaration}.
	   @param x the program element to visit.
	 */
	public void visitConstructorDeclaration(ConstructorDeclaration x) {
		visitMethodDeclaration(x);
	}

	/**
	   Visits the specified extends.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitExtends(Extends x) {}

	/**
	   Visits the specified field declaration.
	   The default implementation calls {@link #visitVariableDeclaration}.
	   @param x the program element to visit.
	 */
	public void visitFieldDeclaration(FieldDeclaration x) {
		visitVariableDeclaration(x);
	}

	/**
	   Visits the specified implements clause.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitImplements(Implements x) {}

	/**
	   Visits the specified interface declaration.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitInterfaceDeclaration(InterfaceDeclaration x) {}

	/**
	   Visits the specified local variable declaration.
	   The default implementation calls {@link #visitVariableDeclaration}.
	   @param x the program element to visit.
	 */
	public void visitLocalVariableDeclaration(LocalVariableDeclaration x) {
		visitVariableDeclaration(x);
	}

	/**
	   Visits the specified method declaration.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitMethodDeclaration(MethodDeclaration x) {}

	/**
	   Visits the specified parameter declaration.
	   The default implementation calls {@link #visitVariableDeclaration}.
	   @param x the program element to visit.
	 */
	public void visitParameterDeclaration(ParameterDeclaration x) {
		visitVariableDeclaration(x);
	}

	/**
	   Visits the specified throws clause.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitThrows(Throws x) {}

	/**
	   Visits the specified variable specification.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitVariableSpecification(VariableSpecification x) {}

	/**
	   Visits the specified field specification.
	   The default implementation calls {@link #visitVariableSpecification}.
	   @param x the program element to visit.
	 */
	public void visitFieldSpecification(FieldSpecification x) {
		visitVariableSpecification(x);
	}

	/**
	   Visits the specified abstract modifier.
	   The default implementation calls {@link #visitModifier}.
	   @param x the program element to visit.
	 */
	public void visitAbstract(Abstract x) {
		visitModifier(x);
	}

	/**
	   Visits the specified final modifier.
	   The default implementation calls {@link #visitModifier}.
	   @param x the program element to final.
	 */
	public void visitSealed(Sealed x) {
		visitModifier(x);
	}

	/**
	   Visits the specified private modifier.
	   The default implementation calls {@link #visitModifier}.
	   @param x the program element to final.
	 */
	public void visitPrivate(Private x) {
		visitModifier(x);
	}

	/**
	   Visits the specified protected modifier.
	   The default implementation calls {@link #visitModifier}.
	   @param x the program element to final.
	 */
	public void visitProtected(Protected x) {
		visitModifier(x);
	}

	/**
	   Visits the specified public modifier.
	   The default implementation calls {@link #visitModifier}.
	   @param x the program element to final.
	 */
	public void visitPublic(Public x) {
		visitModifier(x);
	}

	/**
	   Visits the specified static modifier.
	   The default implementation calls {@link #visitModifier}.
	   @param x the program element to final.
	 */
	public void visitStatic(Static x) {
		visitModifier(x);
	}

	/**
	   Visits the specified strictfp modifier.
	   The default implementation calls {@link #visitModifier}.
	   @param x the program element to final.
	 */
	public void visitRef(Ref x) {
		visitModifier(x);
	}


	/**
	   Visits the specified volatile modifier.
	   The default implementation calls {@link #visitModifier}.
	   @param x the program element to final.
	 */
	public void visitVolatile(Volatile x) {
		visitModifier(x);
	}

	/**
	   Visits the specified array initializer.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitArrayInitializer(ArrayInitializer x) {}

	/**
	   Visits the specified parenthesized expression.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitParenthesizedExpression(ParenthesizedExpression x) {}

	/**
	   Visits the specified boolean literal.
	   The default implementation calls {@link #visitLiteral}.
	   @param x the program element to visit.
	 */
	public void visitBooleanLiteral(BooleanLiteral x) {
		visitLiteral(x);
	}

	/**
	   Visits the specified char literal.
	   The default implementation calls {@link #visitLiteral}.
	   @param x the program element to visit.
	 */
	public void visitCharLiteral(CharLiteral x) {
		visitLiteral(x);
	}

	/**
	   Visits the specified double literal.
	   The default implementation calls {@link #visitLiteral}.
	   @param x the program element to visit.
	 */
	public void visitDoubleLiteral(DoubleLiteral x) {
		visitLiteral(x);
	}

	/**
	   Visits the specified float literal.
	   The default implementation calls {@link #visitLiteral}.
	   @param x the program element to visit.
	 */
	public void visitFloatLiteral(FloatLiteral x) {
		visitLiteral(x);
	}

	/**
	   Visits the specified int literal.
	   The default implementation calls {@link #visitLiteral}.
	   @param x the program element to visit.
	 */
	public void visitIntLiteral(IntLiteral x) {
		visitLiteral(x);
	}

	/**
	   Visits the specified long literal.
	   The default implementation calls {@link #visitLiteral}.
	   @param x the program element to visit.
	 */
	public void visitLongLiteral(LongLiteral x) {
		visitLiteral(x);
	}

	/**
	   Visits the specified null literal.
	   The default implementation calls {@link #visitLiteral}.
	   @param x the program element to visit.
	 */
	public void visitNullLiteral(NullLiteral x) {
		visitLiteral(x);
	}

	/**
	   Visits the specified string literal.
	   The default implementation calls {@link #visitLiteral}.
	   @param x the program element to visit.
	 */
	public void visitStringLiteral(StringLiteral x) {
		visitLiteral(x);
	}

	/**
	   Visits the specified binary-and operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitBinaryAnd(BinaryAnd x) {
		visitOperator(x);
	}

	/**
	   Visits the specified binary-and assignment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitBinaryAndAssignment(BinaryAndAssignment x) {
		visitOperator(x);
	}

	/**
	   Visits the specified binary-not operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitBinaryNot(BinaryNot x) {
		visitOperator(x);
	}

	/**
	   Visits the specified binary-or operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitBinaryOr(BinaryOr x) {
		visitOperator(x);
	}

	/**
	   Visits the specified binary-or assignment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitBinaryOrAssignment(BinaryOrAssignment x) {
		visitOperator(x);
	}

	/**
	   Visits the specified binary-xor operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitBinaryXOr(BinaryXOr x) {
		visitOperator(x);
	}

	/**
	   Visits the specified binary-xor assignment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitBinaryXOrAssignment(BinaryXOrAssignment x) {
		visitOperator(x);
	}

	/**
	   Visits the specified conditional operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitConditional(Conditional x) {
		visitOperator(x);
	}

	/**
	   Visits the specified copy assignment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitCopyAssignment(CopyAssignment x) {
		visitOperator(x);
	}

	/**
	   Visits the specified divide operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitDivide(Divide x) {
		visitOperator(x);
	}

	/**
	   Visits the specified divide assignment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitDivideAssignment(DivideAssignment x) {
		visitOperator(x);
	}

	/**
	   Visits the specified equals operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitEquals(Equals x) {
		visitOperator(x);
	}

	/**
	   Visits the specified greater-or-equals operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitGreaterOrEquals(GreaterOrEquals x) {
		visitOperator(x);
	}

	/**
	   Visits the specified greater-than operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitGreaterThan(GreaterThan x) {
		visitOperator(x);
	}

	/**
	   Visits the specified instanceof operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitInstanceof(Instanceof x) {
		visitOperator(x);
	}

	/**
	   Visits the specified less-or-equals operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitLessOrEquals(LessOrEquals x) {
		visitOperator(x);
	}

	/**
	   Visits the specified less-than operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitLessThan(LessThan x) {
		visitOperator(x);
	}

	/**
	   Visits the specified logical-and operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitLogicalAnd(LogicalAnd x) {
		visitOperator(x);
	}

	/**
	   Visits the specified logical-not operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitLogicalNot(LogicalNot x) {
		visitOperator(x);
	}

	/**
	   Visits the specified logical-or operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitLogicalOr(LogicalOr x) {
		visitOperator(x);
	}

	/**
	   Visits the specified minus operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitMinus(Minus x) {
		visitOperator(x);
	}

	/**
	   Visits the specified minus assignment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitMinusAssignment(MinusAssignment x) {
		visitOperator(x);
	}

	/**
	   Visits the specified modulo operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitModulo(Modulo x) {
		visitOperator(x);
	}

	/**
	   Visits the specified modulo assignment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitModuloAssignment(ModuloAssignment x) {
		visitOperator(x);
	}

	/**
	   Visits the specified negative operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitNegative(Negative x) {
		visitOperator(x);
	}

	/**
	   Visits the specified new operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitNew(New x) {
		visitOperator(x);
	}

	/**
	   Visits the specified new-array operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitNewArray(NewArray x) {
		visitOperator(x);
	}

	/**
	   Visits the specified not-equals operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitNotEquals(NotEquals x) {
		visitOperator(x);
	}

	/**
	   Visits the specified plus operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitPlus(Plus x) {
		visitOperator(x);
	}

	/**
	   Visits the specified plus assignment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitPlusAssignment(PlusAssignment x) {
		visitOperator(x);
	}

	/**
	   Visits the specified positive operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitPositive(Positive x) {
		visitOperator(x);
	}

	/**
	   Visits the specified post-decrement operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitPostDecrement(PostDecrement x) {
		visitOperator(x);
	}

	/**
	   Visits the specified post-increment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitPostIncrement(PostIncrement x) {
		visitOperator(x);
	}

	/**
	   Visits the specified pre-decrement operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitPreDecrement(PreDecrement x) {
		visitOperator(x);
	}

	/**
	   Visits the specified pre-increment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitPreIncrement(PreIncrement x) {
		visitOperator(x);
	}

	/**
	   Visits the specified shift-left operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitShiftLeft(ShiftLeft x) {
		visitOperator(x);
	}

	/**
	   Visits the specified shift-left assignment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitShiftLeftAssignment(ShiftLeftAssignment x) {
		visitOperator(x);
	}

	/**
	   Visits the specified shift-right operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitShiftRight(ShiftRight x) {
		visitOperator(x);
	}

	/**
	   Visits the specified shift-right assignment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitShiftRightAssignment(ShiftRightAssignment x) {
		visitOperator(x);
	}

	/**
	   Visits the specified times operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitTimes(Times x) {
		visitOperator(x);
	}

	/**
	   Visits the specified times assignment operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitTimesAssignment(TimesAssignment x) {
		visitOperator(x);
	}

	/**
	   Visits the specified type cast operator.
	   The default implementation calls {@link #visitOperator}.
	   @param x the program element to visit.
	 */
	public void visitTypeCast(TypeCast x) {
		visitOperator(x);
	}

	/**
	   Visits the specified break statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitBreak(Break x) {}

	/**
	   Visits the specified case statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitCase(Case x) {}

	/**
	   Visits the specified catch branch.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitCatch(Catch x) {}

	/**
	   Visits the specified continue statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitContinue(Continue x) {}

	/**
	   Visits the specified default branch.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitDefault(Default x) {}

	/**
	   Visits the specified do statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitDo(Do x) {}

	/**
	   Visits the specified else branch.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitElse(Else x) {}

	/**
	   Visits the specified empty statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitEmptyStatement(EmptyStatement x) {}

	/**
	   Visits the specified finally branch.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitFinally(Finally x) {}

	/**
	   Visits the specified for statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitFor(For x) {}

	/**
	   Visits the specified assert statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitAssert(Assert x) {}

	/**
	   Visits the specified if statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitIf(If x) {}

	/**
	   Visits the specified labeled statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitLabeledStatement(LabeledStatement x) {}

	/**
	   Visits the specified return statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitReturn(Return x) {}

	/**
	   Visits the specified switch statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitSwitch(Switch x) {}

	/**
	   Visits the specified synchronized block.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitLockedBlock(LockedBlock x) {}

	/**
	   Visits the specified then branch.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitThen(Then x) {}

	/**
	   Visits the specified throw statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitThrow(Throw x) {}

	/**
	   Visits the specified try statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitTry(Try x) {}

	/**
	   Visits the specified while statement.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitWhile(While x) {}

	/**
	   Visits the specified array reference.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitArrayReference(ArrayReference x) {}

	/**
	   Visits the specified array-length reference.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitArrayLengthReference(ArrayLengthReference x) {}

	/**
	   Visits the specified field reference.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitFieldReference(FieldReference x) {}

	/**
	   Visits the specified meta-class reference.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitMetaClassReference(MetaClassReference x) {}

	/**
	   Visits the specified method reference.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitMethodReference(MethodReference x) {}

	/**
	   Visits the specified package reference.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitNamespaceReference(NamespaceReference x) {}

	/**
	   Visits the specified super-constructor reference.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitSuperConstructorReference(SuperConstructorReference x) {}

	/**
	   Visits the specified super reference.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitSuperReference(SuperReference x) {}

	/**
	   Visits the specified this-constructor reference.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitThisConstructorReference(ThisConstructorReference x) {}

	/**
	   Visits the specified this reference.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitThisReference(ThisReference x) {}

	/**
	   Visits the specified type reference.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitTypeReference(TypeReference x) {}

	/**
	   Visits the specified uncollated reference qualifier.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitUncollatedReferenceQualifier(UncollatedReferenceQualifier x) {}

	/**
	   Visits the specified variable reference.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	public void visitVariableReference(VariableReference x) {}

	/**
	   Hook method that visits the specified modifier.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	protected void visitModifier(Modifier x) {}

	/**
	   Hook method that visits the specified literal.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	protected void visitLiteral(Literal x) {}

	/**
	   Hook method that visits the specified operator.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	protected void visitOperator(Operator x) {}

	/**
	   Visits the specified variable declaration.
	   Defaults to do nothing.
	   @param x the program element to visit.
	 */
	protected void visitVariableDeclaration(VariableDeclaration x) {}

	/**
	   Visits the specified single-line comment.
	   The default implementation calls {@link #visitComment}.
	   @param x the comment to visit.
	 */
	public void visitSingleLineComment(SingleLineComment x) {
		visitComment(x);
	}

	/**
	   Visits the specified doc comment.
	   The default implementation calls {@link #visitComment}.
	   @param x the comment to visit.
	 */
	public void visitDocComment(XmlDocComment x) {
		visitComment(x);
	}

	/**
	   Visits the specified comment.
	   Defaults to do nothing.
	   @param x the comment to visit.
	 */
	public void visitComment(Comment x) {}

	/**
	 * Method visitOperatorDeclaration.
	 * @param operatorDeclaration
	 */
	public void visitOperatorDeclaration(OperatorDeclaration x) {}

	/**
	 * Method visitIndexerDeclaration.
	 * @param indexerDeclaration
	 */
	public void visitIndexerDeclaration(IndexerDeclaration indexerDeclaration) {}

	/**
	 * Method visitDestructorDeclaration.
	 * @param destructorDeclaration
	 */
	public void visitDestructorDeclaration(DestructorDeclaration destructorDeclaration) {}

	/**
	 * Method visitAddAccessor.
	 * @param addAccessor
	 */
	public void visitAddAccessor(AddAccessor addAccessor) {}

	/**
	 * Method visitAs.
	 * @param as
	 */
	public void visitAs(As as) {}

	/**
	 * Method visitAssemblyTarget.
	 * @param assemblyTarget
	 */
	public void visitAssemblyTarget(AssemblyTarget assemblyTarget) {}

	/**
	 * Method visitAttribute.
	 * @param attribute
	 */
	public void visitAttribute(Attribute attribute) {}

	/**
	 * Method visitAttributeSection.
	 * @param attributeSection
	 */
	public void visitAttributeSection(AttributeSection attributeSection) {}

	/**
	 * Method visitChecked.
	 * @param checked
	 */
	public void visitChecked(Checked checked) {}

	/**
	 * Method visitCheckedBlock.
	 * @param checkedBlock
	 */
	public void visitCheckedBlock(CheckedBlock checkedBlock) {}

	/**
	 * Method visitDelegateDeclaration.
	 * @param delegateDeclaration
	 */
	public void visitDelegateDeclaration(DelegateDeclaration delegateDeclaration) {}

	/**
	 * Method visitEnumDeclaration.
	 * @param enumDeclaration
	 */
	public void visitEnumDeclaration(EnumDeclaration enumDeclaration) {}

	/**
	 * Method visitEventDeclaration.
	 * @param eventDeclaration
	 */
	public void visitEventDeclaration(EventDeclaration eventDeclaration) {}

	/**
	 * Method visitExtern.
	 * @param extern
	 */
	public void visitExtern(Extern extern) {}

	/**
	 * Method visitGetAccessor.
	 * @param getAccessor
	 */
	public void visitGetAccessor(GetAccessor getAccessor) {}

	/**
	 * Method visitGoto.
	 * @param goto
	 */
	public void visitGoto(Goto g) {}

	/**
	 * Method visitMemberName.
	 * @param memberName
	 */
	public void visitMemberName(MemberName memberName) {}

	/**
	 * Method visitOut.
	 * @param out
	 */
	public void visitOut(Out out) {}

	/**
	 * Method visitOutOperator.
	 * @param outOperator
	 */
	public void visitOutOperator(OutOperator outOperator) {}

	/**
	 * Method visitOverride.
	 * @param override
	 */
	public void visitOverride(Override override) {}

	/**
	 * Method visitParams.
	 * @param params
	 */
	public void visitParams(Params params) {}

	/**
	 * Method visitPreDefinedTypeLiteral.
	 * @param preDefinedTypeLiteral
	 */
	public void visitPreDefinedTypeLiteral(PreDefinedTypeLiteral preDefinedTypeLiteral) {}

	/**
	 * Method visitPropertyDeclaration.
	 * @param propertyDeclaration
	 */
	public void visitPropertyDeclaration(PropertyDeclaration propertyDeclaration) {}

	/**
	 * Method visitReadonly.
	 * @param readonly
	 */
	public void visitReadonly(Readonly readonly) {}

	/**
	 * Method visitRefOperator.
	 * @param refOperator
	 */
	public void visitRefOperator(RefOperator refOperator) {}

	/**
	 * Method visitRemoveAccessor.
	 * @param removeAccessor
	 */
	public void visitRemoveAccessor(RemoveAccessor removeAccessor) {}

	/**
	 * Method visitSetAccessor.
	 * @param setAccessor
	 */
	public void visitSetAccessor(SetAccessor setAccessor) {}

	/**
	 * Method visitStructDeclaration.
	 * @param structDeclaration
	 */
	public void visitStructDeclaration(StructDeclaration structDeclaration) {}

	/**
	 * Method visitTypeof.
	 * @param typeof
	 */
	public void visitTypeof(Typeof typeof) {}

	/**
	 * Method visitUnchecked.
	 * @param unchecked
	 */
	public void visitUnchecked(Unchecked unchecked) {}

	/**
	 * Method visitUncheckedBlock.
	 * @param uncheckedBlock
	 */
	public void visitUncheckedBlock(UncheckedBlock uncheckedBlock) {}

	/**
	 * Method visitUsingBlock.
	 * @param usingBlock
	 */
	public void visitUsingBlock(UsingBlock usingBlock) {}

	/**
	 * Method visitVirtual.
	 * @param virtual
	 */
	public void visitVirtual(Virtual virtual) {}

	/**
	 * Method visitStaticConstructorDeclaration.
	 * @param staticConstructorDeclaration
	 */
	public void visitStaticConstructorDeclaration(StaticConstructorDeclaration staticConstructorDeclaration) {}

	/**
	 * Method visitAttributeArgument.
	 * @param attributeArgument
	 */
	public void visitAttributeArgument(AttributeArgument attributeArgument) {}

	/**
	 * Method visitNamedAttributeArgument.
	 * @param namedAttributeArgument
	 */
	public void visitNamedAttributeArgument(NamedAttributeArgument namedAttributeArgument) {}

	/**
	 * Method visitEventTarget.
	 * @param eventTarget
	 */
	public void visitEventTarget(EventTarget eventTarget) {}

	/**
	 * Method visitFieldTarget.
	 * @param fieldTarget
	 */
	public void visitFieldTarget(FieldTarget fieldTarget) {}

	/**
	 * Method visitMethodTarget.
	 * @param methodTarget
	 */
	public void visitMethodTarget(MethodTarget methodTarget) {}

	/**
	 * Method visitModuleTarget.
	 * @param moduleTarget
	 */
	public void visitModuleTarget(ModuleTarget moduleTarget) {}

	/**
	 * Method visitParamTarget.
	 * @param paramTarget
	 */
	public void visitParamTarget(ParamTarget paramTarget) {}

	/**
	 * Method visitPropertyTarget.
	 * @param propertyTarget
	 */
	public void visitPropertyTarget(PropertyTarget propertyTarget) {}

	/**
	 * Method visitReturnTarget.
	 * @param returnTarget
	 */
	public void visitReturnTarget(ReturnTarget returnTarget) {}

	/**
	 * Method visitTypeTarget.
	 * @param typeTarget
	 */
	public void visitTypeTarget(TypeTarget typeTarget) {}

	/**
	 * Method visitInternal.
	 * @param internal
	 */
	public void visitInternal(Internal internal) {}

	/**
	 * Method visitNewModifier.
	 * @param newModifier
	 */
	public void visitNewModifier(NewModifier newModifier) {}

	/**
	 * Method visitUsingAlias.
	 * @param usingAlias
	 */
	public void visitUsingAlias(UsingAlias usingAlias) {}

	/**
	 * Method visitForeach.
	 * @param foreach
	 */
	public void visitForeach(Foreach foreach) {}

	/**
	 * Method visitConstantFieldDeclaration.
	 * @param constantFieldDeclaration
	 */
	public void visitConstantFieldDeclaration(ConstantFieldDeclaration constantFieldDeclaration) {}

	/**
	 * Method visitLocalConstantDeclaration.
	 * @param localConstantDeclaration
	 */
	public void visitLocalConstantDeclaration(LocalConstantDeclaration localConstantDeclaration) {}

	/**
	 * Method visitEnumMemberDeclaration.
	 * @param enumMemberDeclaration
	 */
	public void visitEnumMemberDeclaration(EnumMemberDeclaration enumMemberDeclaration) {}

	/**
	 * Method visitMethodGroupReference.
	 * @param methodGroupReference
	 */
	public void visitMethodGroupReference(MethodGroupReference methodGroupReference) {}

	/**
	 * Method visitPropertySpecification.
	 * @param propertySpecification
	 */
	public void visitPropertySpecification(PropertySpecification propertySpecification) {
	}

	/**
	 * Method visitEventSpecification.
	 * @param eventSpecification
	 */
	public void visitEventSpecification(EventSpecification eventSpecification) {
	}

	/**
	 * Method visitDelegateCallReference.
	 * @param delegateCallReference
	 */
	public void visitDelegateCallReference(DelegateCallReference delegateCallReference) {}

	/**
	 * Method visitUncollatedMethodCallReference.
	 * @param uncollatedMethodCallReference
	 */
	public void visitUncollatedMethodCallReference(UncollatedMethodCallReference uncollatedMethodCallReference) {}

	/**
	 * Method visitVerbatimStringLiteral.
	 * @param verbatimStringLiteral
	 */
	public void visitVerbatimStringLiteral(VerbatimStringLiteral verbatimStringLiteral) {
		visitStringLiteral(verbatimStringLiteral);
	}

}
