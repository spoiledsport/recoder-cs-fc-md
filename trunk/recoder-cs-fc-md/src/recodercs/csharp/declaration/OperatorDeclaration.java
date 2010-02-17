package recodercs.csharp.declaration;

import recodercs.abstraction.OperatorOverload;
import recodercs.csharp.Identifier;
import recodercs.csharp.SourceVisitor;
import recodercs.csharp.StatementBlock;
import recodercs.csharp.reference.TypeReference;
import recodercs.list.ModifierMutableList;
import recodercs.list.ParameterDeclarationMutableList;

/**
 * @author kis
 *
 * User defined Operator declaration
 */
public class OperatorDeclaration extends MethodDeclaration 
	implements OperatorOverload {

// Constants tell the type of the operator declared.

public static final int PLUS              = 0;
public static final int MINUS             = 1;
public static final int TIMES             = 2;
public static final int DIVIDE            = 3;
public static final int MODULO            = 4;
public static final int NOT               = 5;
public static final int BINARY_NOT        = 6;
public static final int BINARY_AND        = 7;
public static final int BINARY_OR         = 8;
public static final int BINARY_XOR        = 9;
public static final int INCREMENT         = 10;
public static final int DECREMENT         = 11;
public static final int TRUE              = 12;
public static final int FALSE             = 13;
public static final int SHIFT_LEFT        = 14;
public static final int SHIFT_RIGHT       = 15;
public static final int EQUALS            = 16;
public static final int NOT_EQUALS        = 17;
public static final int LESS_THAN         = 18;
public static final int GREATER_THAN      = 19;
public static final int LESS_OR_EQUALS    = 20;
public static final int GREATER_OR_EQUALS = 21;
public static final int IMPLICIT_CAST     = 22;
public static final int EXPLICIT_CAST     = 23;

	/** Operator type */
	protected int operatorType;
	
	/**
	 * Constructor for OperatorDeclaration.
	 */
	public OperatorDeclaration() {
		super();
	}

	/**
	 * Constructor for OperatorDeclaration.
	 * @param modifiers
	 * @param returnType
	 * @param name
	 * @param parameters
	 * @param exceptions
	 */
	public OperatorDeclaration(
		ModifierMutableList modifiers,
		TypeReference returnType,
		Identifier name,
		ParameterDeclarationMutableList parameters,
		Throws exceptions) {
		super(modifiers, returnType, name, parameters, exceptions);
	}

	/**
	 * Constructor for OperatorDeclaration.
	 * @param modifiers
	 * @param returnType
	 * @param name
	 * @param parameters
	 * @param exceptions
	 * @param body
	 */
	public OperatorDeclaration(
		ModifierMutableList modifiers,
		TypeReference returnType,
		Identifier name,
		ParameterDeclarationMutableList parameters,
		Throws exceptions,
		StatementBlock body) {
		super(modifiers, returnType, name, parameters, exceptions, body);
	}

	/**
	 * Constructor for OperatorDeclaration.
	 * @param proto
	 */
	public OperatorDeclaration(MethodDeclaration proto) {
		super(proto);
	}

	/**
	 * Returns the operatorType.
	 * @return int
	 */
	public int getOperatorType() {
		return operatorType;
	}

	/**
	 * Sets the operatorType.
	 * @param operatorType The operatorType to set
	 */
	public void setOperatorType(int operatorType) {
		this.operatorType = operatorType;
		setIdentifier(new Identifier(getOperatorName()));
	}

	/**
	 * Returns the operator's name (e.g. +/++/etc.)
	 * @return String
	 */
	public String getOperatorSymbol() {
		switch (operatorType) {
			case PLUS              : return("+");
			case MINUS             : return("-");
			case TIMES             : return("*");
			case DIVIDE            : return("/");
			case MODULO            : return("%"); 
			case NOT               : return("!"); 
			case BINARY_NOT        : return("~"); 
			case BINARY_AND        : return("&"); 
			case BINARY_OR         : return("|"); 
			case BINARY_XOR        : return("^"); 
			case INCREMENT         : return("++"); 
			case DECREMENT         : return("--");
			case TRUE              : return("true"); 
			case FALSE             : return("false"); 
			case SHIFT_LEFT        : return("<<"); 
			case SHIFT_RIGHT       : return(">>"); 
			case EQUALS            : return("=="); 
			case NOT_EQUALS        : return("!="); 
			case LESS_THAN         : return("<"); 
			case GREATER_THAN      : return(">"); 
			case LESS_OR_EQUALS    : return("<="); 
			case GREATER_OR_EQUALS : return(">="); 
		}	
		return "";	
	}

	public String getOperatorName() {
		switch (operatorType) {
			case PLUS              : return("PLUS");
			case MINUS             : return("MINUS");
			case TIMES             : return("TIMES");
			case DIVIDE            : return("DIVIDE");
			case MODULO            : return("MODULO"); 
			case NOT               : return("NOT"); 
			case BINARY_NOT        : return("BINARY_NOT"); 
			case BINARY_AND        : return("BINARY_AND"); 
			case BINARY_OR         : return("BINARY_OR"); 
			case BINARY_XOR        : return("BINARY_XOR"); 
			case INCREMENT         : return("INCREMENT"); 
			case DECREMENT         : return("DECREMENT");
			case TRUE              : return("TRUE"); 
			case FALSE             : return("FALSE"); 
			case SHIFT_LEFT        : return("SHIFT_LEFT"); 
			case SHIFT_RIGHT       : return("SHIFT_RIGHT"); 
			case EQUALS            : return("EQUALS"); 
			case NOT_EQUALS        : return("NOT_EQUALS"); 
			case LESS_THAN         : return("LESS_THAN"); 
			case GREATER_THAN      : return("GREATER_THAN"); 
			case LESS_OR_EQUALS    : return("LESS_OR_EQUALS"); 
			case GREATER_OR_EQUALS : return("GREATER_OR_EQUALS"); 
			case IMPLICIT_CAST     : return("IMPLICIT_CAST"); 
			case EXPLICIT_CAST     : return("EXPLICIT_CAST"); 
		}	
		return "";	
	}

	/**
	 * @see recodercs.csharp.SourceElement#accept(SourceVisitor)
	 */
	public void accept(SourceVisitor v) {
		v.visitOperatorDeclaration(this);
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isAbstract()
	 */
	public boolean isAbstract() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isInternal()
	 */
	public boolean isInternal() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isNew()
	 */
	public boolean isNew() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isOverride()
	 */
	public boolean isOverride() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isPrivate()
	 */
	public boolean isPrivate() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isProtected()
	 */
	public boolean isProtected() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isSealed()
	 */
	public boolean isSealed() {
		return false;
	}

	/**
	 * @see recodercs.csharp.declaration.CSharpDeclaration#isVirtual()
	 */
	public boolean isVirtual() {
		return false;
	}
	
	public String getName() {
		return getOperatorName();
	}

}
