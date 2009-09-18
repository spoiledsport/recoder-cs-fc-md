// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp;

import recoder.*;
import recoder.list.*;
import recoder.csharp.expression.*;
import recoder.csharp.expression.operator.*;
import recoder.csharp.expression.literal.*;
import recoder.csharp.reference.*;
import recoder.csharp.attributes.AttributableElement;
import recoder.csharp.attributes.Attribute;
import recoder.csharp.attributes.AttributeArgument;
import recoder.csharp.attributes.AttributeSection;
import recoder.csharp.attributes.AttributeTarget;
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
import recoder.csharp.statement.*;
import recoder.io.PropertyNames;
import recoder.io.ProjectSettings;

import recoder.csharp.SourceElement.*;
import recoder.util.Debug;
import recoder.util.StringUtils;

import java.io.*;
import java.util.*;

/**
   A configurable pretty printer for Java source elements.
   The settings of the pretty printer is given by the current project 
   settings and cannot be changed.
   Instances of this class are available from 
   {@link recoder.ProgramFactory#getPrettyPrinter}.
   Remember to <CODE>close()</CODE> the writer once finished.
   @author AL
 */
public class PrettyPrinter extends SourceVisitor implements PropertyNames {

	/**
	   A snapshot of the system properties at creation time of this
	   instance.
	 */
	private Properties properties;

	/**
	   The destination writer stream.
	 */
	private Writer out = null;

	/**
	   Line number.
	*/
	private int line = 1;

	/**
	   Column number. Used to keep track of indentations.
	*/
	private int column = 1;

	/**
	   Level.
	*/
	private int level = 0;

	/**
	   Worklist of single line comments that must be delayed till the
	   next linefeed.
	 */
	private CommentMutableList singleLineCommentWorkList =
		new CommentArrayList();

	/**
	   Flag to indicate if a single line comment is being put out.
	   Needed to disable the worklist meanwhile.
	 */
	private boolean isPrintingSingleLineComments = false;

	/**
	   Set up a pretty printer with given options.
	   Note that it is not wise to change the options during pretty
	   printing - nothing dangerous will happen, but the results might
	   look strange. It can make sense to change options between source
	   files, however.
	*/
	protected PrettyPrinter(Writer out, Properties props) {
		setWriter(out);
		properties = props;
		cacheFrequentlyUsed();
	}

	/**
	   Set a new stream to write to. Useful to redirect the output
	   while retaining all other settings. Resets the current source
	   positions and comments.
	*/
	public void setWriter(Writer out) {
		if (out == null) {
			throw new IllegalArgumentException("Impossible to write to null");
		}
		this.out = out;
		column = 1;
		line = 1;
		singleLineCommentWorkList.clear();
		isPrintingSingleLineComments = false;
	}

	/**
	   Gets the currently used writer. Be careful when using.
	   @return the currently used writer.
	 */
	public Writer getWriter() {
		return out;
	}

	/**
	   Get current line number.
	   @return the line number, starting with 0.
	*/
	public int getLine() {
		return line;
	}

	/**
	   Get current column number.
	   @return the column number, starting with 0.
	*/
	public int getColumn() {
		return column;
	}

	/**
	   Get indentation level.
	   @return the int value.
	*/
	public int getIndentationLevel() {
		return level;
	}

	/**
	   Set indentation level.
	   @param level an int value.
	*/
	public void setIndentationLevel(int level) {
		this.level = level;
	}

	/**
	   Get total indentation.
	   @return the int value.
	*/
	public int getTotalIndentation() {
		return indentation * level;
	}

	/**
	   Change level.
	   @param delta an int value.
	*/
	public void changeLevel(int delta) {
		level += delta;
	}

	private static char[] BLANKS = new char[128];

	private static char[] FEEDS = new char[8];

	static {
		for (int i = 0; i < FEEDS.length; i++) {
			FEEDS[i] = '\n';
		}
		for (int i = 0; i < BLANKS.length; i++) {
			BLANKS[i] = ' ';
		}
	}

	/**
	   Replace all unicode characters above ÿ
	   by their explicite representation.
	   @param str the input string.
	   @return the encoded string.
	*/
	protected static String encodeUnicodeChars(String str) {
		int len = str.length();
		StringBuffer buf = new StringBuffer(len + 4);
		for (int i = 0; i < len; i += 1) {
			char c = str.charAt(i);
			if (c >= 0x0100) {
				if (c < 0x1000) {
					buf.append("\\u0" + Integer.toString(c, 16));
				} else {
					buf.append("\\u" + Integer.toString(c, 16));
				}
			} else {
				buf.append(c);
			}
		}
		return buf.toString();
	}

	/**
	   Convenience method to write indentation chars.
	*/
	protected void printIndentation(int lf, int blanks) {
		if (lf > 0) {
			do {
				int n = Math.min(lf, FEEDS.length);
				print(FEEDS, 0, n);
				lf -= n;
			} while (lf > 0);
		}
		while (blanks > 0) {
			int n = Math.min(blanks, BLANKS.length);
			print(BLANKS, 0, n);
			blanks -= n;
		}
	}

	/**
	   Shared and reused position object.
	 */
	private Position overwritePosition = new Position(0, 0);

	/**
	   Sets the indentation of the specified element to
	   at least the specified minimum.
	   @return the final relative position of the element.
	 */
	protected Position setElementIndentation(
		int minlf,
		int minblanks,
		SourceElement element) {
		Position indent = element.getRelativePosition();
		if (indent == Position.UNDEFINED) {
			if (minlf > 0) {
				minblanks += getTotalIndentation();
			}
			indent = new Position(minlf, minblanks);
		} else if (overwriteIndentation) {
			if (minlf > 0) {
				minblanks += getTotalIndentation();
			}
			indent.setPosition(minlf, minblanks);
		} else {
			if (minlf > 0
				&& indent.getColumn() == 0
				&& indent.getLine() == 0) {
				indent.setLine(1);
			}
			if (indent.getLine() > 0) {
				minblanks += getTotalIndentation();
			}
			if (minblanks > indent.getColumn()) {
				indent.setColumn(minblanks);
			}
		}
		element.setRelativePosition(indent);
		return indent;
	}

	/**
	   Sets the indentation of the specified element to
	   at least the specified minimum and writes it.
	 */
	protected void printElementIndentation(
		int minlf,
		int minblanks,
		SourceElement element) {
		Position indent = setElementIndentation(minlf, minblanks, element);
		printIndentation(indent.getLine(), indent.getColumn());
		if (overwriteParsePositions) {
			indent.setPosition(line, column);
			element.setStartPosition(indent);
		}
	}

	protected void printElementIndentation(
		int minblanks,
		SourceElement element) {
		printElementIndentation(0, minblanks, element);
	}

	protected void printElementIndentation(SourceElement element) {
		printElementIndentation(0, 0, element);
	}

	/**
	   Adds indentation for a program element if necessary and if required,
	   but does not print the indentation itself.       
	*/
	protected void printElement(
		int lf,
		int levelChange,
		int blanks,
		SourceElement elem) {
		Debug.asserta(elem);
		level += levelChange;

		setElementIndentation(lf, blanks, elem.getFirstElement());
		elem.accept(this);
	}

	/**
	   Write a source element.
	   @param lf an int value.
	   @param blanks an int value.
	   @param elem a source element.
	*/
	protected void printElement(int lf, int blanks, SourceElement elem) {
		setElementIndentation(lf, blanks, elem.getFirstElement());
		elem.accept(this);
	}

	/**
	   Write source element.
	   @param blanks an int value.
	   @param elem a source element.
	*/
	protected void printElement(int blanks, SourceElement elem) {
		setElementIndentation(0, blanks, elem.getFirstElement());
		elem.accept(this);
	}

	/**
	   Write source element.
	   @param elem a source element.
	*/
	protected void printElement(SourceElement elem) {
		setElementIndentation(0, 0, elem.getFirstElement());
		elem.accept(this);
	}

	/**
	   Write a complete ProgramElementList.
	*/
	protected void printProgramElementList(
		int firstLF,
		int levelChange,
		int firstBlanks,
		String separationSymbol,
		int separationLF,
		int separationBlanks,
		ProgramElementList list) {
		int s = list.size();
		if (s == 0) {
			return;
		}
		printElement(
			firstLF,
			levelChange,
			firstBlanks,
			list.getProgramElement(0));
		for (int i = 1; i < s; i += 1) {
			print(separationSymbol);
			printElement(
				separationLF,
				separationBlanks,
				list.getProgramElement(i));
		}
	}

	/**
	   Write a complete ProgramElementList using "Keyword" style.
	   @param list a program element list.
	*/
	protected void printKeywordList(ProgramElementList list) {
		printProgramElementList(0, 0, 0, "", 0, 1, list);
	}

	/**
	   Write a complete ProgramElementList using "Comma" style.
	*/
	protected void printCommaList(
		int firstLF,
		int levelChange,
		int firstBlanks,
		ProgramElementList list) {
		printProgramElementList(
			firstLF,
			levelChange,
			firstBlanks,
			",",
			0,
			1,
			list);
	}

	/**
	   Write comma list.
	   @param list a program element list.
	*/
	protected void printCommaList(
		int separationBlanks,
		ProgramElementList list) {
		printProgramElementList(0, 0, 0, ",", 0, separationBlanks, list);
	}

	/**
	   Write comma list.
	   @param list a program element list.
	*/
	protected void printCommaList(ProgramElementList list) {
		printProgramElementList(0, 0, 0, ",", 0, 1, list);
	}

	/**
	   Write a complete ProgramElementList using "Line" style.
	*/
	protected void printLineList(
		int firstLF,
		int levelChange,
		ProgramElementList list) {
		printProgramElementList(firstLF, levelChange, 0, "", 1, 0, list);
	}

	/**
	   Write a complete ProgramElementList using "Block" style.
	*/
	protected void printBlockList(
		int firstLF,
		int levelChange,
		ProgramElementList list) {
		printProgramElementList(firstLF, levelChange, 0, "", 2, 0, list);
	}

	private void dumpComments() {
		int size = singleLineCommentWorkList.size();
		if (size > 0) {
			isPrintingSingleLineComments = true;
			for (int i = 0; i < size; i++) {
				singleLineCommentWorkList.getComment(i).accept(this);
			}
			singleLineCommentWorkList.clear();
			isPrintingSingleLineComments = false;
		}
	}

	/**
	   Write a single character.
	   @param c an int value.
	   @exception PrettyPrintingException wrapping an IOException.
	*/
	protected void print(int c) {
		if (c == '\n') {
			if (!isPrintingSingleLineComments) {
				dumpComments();
			}
			column = 1;
			line += 1;
		} else {
			column += 1;
		}
		try {
			out.write(c);
		} catch (IOException ioe) {
			throw new PrettyPrintingException(ioe);
		}
	}

	/**
	   Write a sequence of characters.
	   @param cbuf an array of char.
	   @param off an int value.
	   @param len an int value.
	*/
	protected void print(char[] cbuf, int off, int len) {
		boolean col = false;

		for (int i = off + len - 1; i >= off; i -= 1) {
			if (cbuf[i] == '\n') {
				if (!isPrintingSingleLineComments) {
					dumpComments();
				}
				line += 1;
				if (!col) {
					column = (off + len - 1 - i) + 1;
					col = true;
				}
			}
		}
		if (!col) {
			column += len;
			//int i;
			//  for (i = off + len - 1; (i >= off && cbuf[i] != '\n'); i -= 1) ;
			//  column = (i >= off) ? (off + len - 1 - i) : (column + len);
		}
		try {
			out.write(cbuf, off, len);
		} catch (IOException ioe) {
			throw new PrettyPrintingException(ioe);
		}
	}

	/**
	   Writes a string.
	   @param str a string.
	   @exception PrettyPrintingException wrapping an IOException.
	*/
	protected void print(String str) {
		int i = str.lastIndexOf('\n');
		if (i >= 0) {
			column = str.length() - i + 1 + 1;
			do {
				dumpComments();
				line += 1;
				i = str.lastIndexOf('\n', i - 1);
			} while (i >= 0);
		} else {
			column += str.length();
		}
		try {
			out.write(str);
		} catch (IOException ioe) {
			throw new PrettyPrintingException(ioe);
		}
	}

	/**
	   Indentation (cached).
	*/
	private int indentation;

	/*
	   Wrap threshold (cached).
	   private int wrap;
	*/

	/**
	   Overwrite indentation flag (cached).
	*/
	private boolean overwriteIndentation;

	/**
	   Overwrite parse positions flag (cached).
	 */
	private boolean overwriteParsePositions;

	public boolean getBooleanProperty(String key) {
		return StringUtils.parseBooleanProperty(properties.getProperty(key));
	}

	// parse and cache some important settings
	private void cacheFrequentlyUsed() {
		indentation =
			Integer.parseInt(properties.getProperty(INDENTATION_AMOUNT));
		if (indentation < 0) {
			throw new IllegalArgumentException("Negative indentation");
		}
		/*
		wrap = Integer.parseInt(properties.getProperty("wrappingThreshold"));
		if (wrap < 40) {
		throw new IllegalArgumentException("Wrapping threshold " + 
					       wrap + " is useless");
		}
		*/
		overwriteIndentation = getBooleanProperty(OVERWRITE_INDENTATION);
		overwriteParsePositions = getBooleanProperty(OVERWRITE_PARSE_POSITIONS);
	}

	/**
	   Get indentation amount (blanks per level).
	   @return the value of getIntegerProperty("indentationAmount").
	*/
	protected int getIndentation() {
		return indentation;
	}

	/**
	   Returns true if the pretty printer should also reformat existing
	   code. 
	   @return the value of the overwriteIndentation property.
	*/
	protected boolean isOverwritingIndentation() {
		return overwriteIndentation;
	}

	/**
	   Returns true if the pretty printer should reset the parse positions
	   accordingly.
	   @return the value of the overwriteParsePositions property.
	*/
	protected boolean isOverwritingParsePositions() {
		return overwriteParsePositions;
	}

	/**
	   Print program element header.
	   @param lf an int value.
	   @param blanks an int value.
	   @param elem a program element.
	*/
	protected void printHeader(int lf, int blanks, ProgramElement elem) {
		printHeader(lf, 0, blanks, elem);
	}

	/**
	   Print program element header.
	   @param blanks an int value.
	   @param elem a program element.
	*/
	protected void printHeader(int blanks, ProgramElement elem) {
		printHeader(0, 0, blanks, elem);
	}

	/**
	   Print program element header.
	   @param elem a program element.
	*/
	protected void printHeader(ProgramElement elem) {
		printHeader(0, 0, 0, elem);
	}

	/**
	   Print program element header.
	   @param lf number of line feeds.
	   @param levelChange the level change.
	   @param blanks number of white spaces.
	   @param x the program element.
	*/
	protected void printHeader(
		int lf,
		int levelChange,
		int blanks,
		ProgramElement x) {
		level += levelChange;
		if (lf > 0) {
			blanks += getTotalIndentation();
		}
		SourceElement first = x.getFirstElement();
		setElementIndentation(lf, blanks, first);
		/*
		    Position indent = first.getRelativePosition();
		    if (indent == Position.UNDEFINED) {
		        indent = new Position(lf, blanks);
		    } else if (overwriteIndentation) {
		        indent.setPosition(lf, blanks);
		    } else {
		        if (lf > indent.getLine()) {
		            indent.setLine(lf);
		        }
		        if (blanks > indent.getColumn()) {
		            indent.setColumn(blanks);
		        }
		    }
		    first.setRelativePosition(indent);
		*/
		int s = (x.getComments() != null) ? x.getComments().size() : 0;
		for (int i = 0; i < s; i += 1) {
			Comment c = x.getComments().getComment(i);
			if (c.isPrefixed()) {
				c.accept(this);
			}
		}
	}

	/**
	   Sets end positions if required, and prints program element footer.
	   @param x the program element.
	*/
	protected void printFooter(ProgramElement x) {
		// also in visitComment!
		if (overwriteParsePositions) {
			overwritePosition.setPosition(line, column);
			x.setEndPosition(overwritePosition);
		}
		int s = (x.getComments() != null) ? x.getComments().size() : 0;
		for (int i = 0; i < s; i += 1) {
			Comment c = x.getComments().getComment(i);
			if (!c.isPrefixed()) {
				if (c instanceof SingleLineComment) {
					// Store until the next line feed is written.	   
					singleLineCommentWorkList.add((SingleLineComment) c);
				} else {
					c.accept(this);
				}
			}
		}
	}

	protected void printOperator(Operator x, String symbol) {
		ExpressionList children = x.getArguments();
		if (children != null) {
			boolean addParentheses = x.isToBeParenthesized();
			if (addParentheses) {
				print('(');
			}
			switch (x.getArity()) {
				case 2 :
					printElement(0, children.getExpression(0));
					if (getBooleanProperty(GLUE_INFIX_OPERATORS)) {
						printElementIndentation(0, x);
						print(symbol);
						printElement(children.getExpression(1));
					} else {
						printElementIndentation(1, x);
						print(symbol);
						printElement(1, children.getExpression(1));
					}
					break;
				case 1 :
					switch (x.getNotation()) {
						case Operator.PREFIX :
							printElementIndentation(x);
							print(symbol);
							if (getBooleanProperty(GLUE_UNARY_OPERATORS)) {
								printElement(0, children.getExpression(0));
							} else {
								printElement(1, children.getExpression(0));
							}
							break;
						case Operator.POSTFIX :
							printElement(0, children.getExpression(0));
							if (getBooleanProperty(GLUE_UNARY_OPERATORS)) {
								printElementIndentation(x);
								print(symbol);
							} else {
								printElementIndentation(1, x);
								print(symbol);
							}
							break;
						default :
							break;
					}
			}
			if (addParentheses) {
				print(')');
			}
			if (x instanceof Assignment) {
				if (((Assignment) x).getStatementContainer() != null) {
					print(';');
				}
			}
		}
	}

	public void visitIdentifier(Identifier x) {
		printHeader(x);
		printElementIndentation(x);
		print(x.getText());
		printFooter(x);
	}

	public void visitIntLiteral(IntLiteral x) {
		printHeader(x);
		printElementIndentation(x);
		print(x.getValue());
		printFooter(x);
	}

	public void visitBooleanLiteral(BooleanLiteral x) {
		printHeader(x);
		printElementIndentation(x);
		print(x.getValue() ? "true" : "false");
		printFooter(x);
	}

	public void visitStringLiteral(StringLiteral x) {
		printHeader(x);
		printElementIndentation(x);
		print(encodeUnicodeChars(x.getValue()));
		printFooter(x);
	}

	public void visitNullLiteral(NullLiteral x) {
		printHeader(x);
		printElementIndentation(x);
		print("null");
		printFooter(x);
	}

	public void visitCharLiteral(CharLiteral x) {
		printHeader(x);
		printElementIndentation(x);
		print(encodeUnicodeChars(x.getValue()));
		printFooter(x);
	}

	public void visitDoubleLiteral(DoubleLiteral x) {
		printHeader(x);
		printElementIndentation(x);
		print(x.getValue());
		printFooter(x);
	}

	public void visitLongLiteral(LongLiteral x) {
		printHeader(x);
		printElementIndentation(x);
		print(x.getValue());
		printFooter(x);
	}

	public void visitFloatLiteral(FloatLiteral x) {
		printHeader(x);
		printElementIndentation(x);
		print(x.getValue());
		printFooter(x);
	}

	public void visitNamespaceSpecification(NamespaceSpecification x) {
		printHeader(x);
		printElementIndentation(x);
		boolean hasNamespaces = (x.getNamespaceSpecificationCount() > 0);
		boolean hasDeclarations = (x.getTypeDeclarationCount() > 0);
		boolean hasAttributes = (x.getAttributeSectionCount() > 0);
		boolean hasUsings = (x.getUsings() != null && x.getUsings().size() > 0);
		if (hasAttributes) {
			printAttributeSections(x);
		}
		printIndentation(1, 0);
		print("namespace");
		printElement(1, x.getNamespaceReference());
		printIndentation(0, 1);
		print('{');
		if (hasUsings) {
			printLineList(2, 1, x.getUsings());
			changeLevel(-1);
		}
		if (hasNamespaces) {
			printBlockList(2, 1, x.getNamespaceSpecifications());
			changeLevel(-1);
		}
		if (hasDeclarations) {
			printBlockList(2, 1, x.getDeclarations());
			changeLevel(-1);
		}
		printIndentation(
			1,
			x.getFirstElement().getRelativePosition().getColumn());
		print('}');
		printFooter(x);
	}

	public void visitTypeReference(TypeReference x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
			printElementIndentation(x);
			print('.');
		}
		if (x.getIdentifier() != null) {
			printElement(x.getIdentifier());
		}
		int[] dimensions = x.getDimensions();
		if (dimensions != null)
			for (int j = 0; j < dimensions.length; j++) {
				int currdim = dimensions[j];
				print('[');
				for (int i = 0; i < currdim - 1; i += 1) {
					print(',');
				}
				print(']');
			}
		printFooter(x);
	}

	public void visitNamespaceReference(NamespaceReference x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
			printElementIndentation(x);
			print('.');
		}
		if (x.getIdentifier() != null) {
			printElement(x.getIdentifier());
		}
		printFooter(x);
	}

	public void visitThrows(Throws x) {
		printHeader(x);
		if (x.getExceptions() != null) {
			printElementIndentation(x);
			print("throws");
			printCommaList(0, 0, 1, x.getExceptions());
		}
		printFooter(x);
	}

	public void visitArrayInitializer(ArrayInitializer x) {
		printHeader(x);
		printElementIndentation(x);
		print('{');
		if (x.getArguments() != null) {
			printCommaList(0, 0, 1, x.getArguments());
		}
		if (x.getArguments() != null
			&& x.getArguments().size() > 0
			&& x.getRelativePosition().getLine() > 0) {
			printIndentation(1, getTotalIndentation());
			print('}');
		} else {
			print(" }");
		}
		printFooter(x);
	}

	public void visitCompilationUnit(CompilationUnit x) {
		line = column = 1;
		printHeader(x);
		setIndentationLevel(0);
		boolean hasUsings =
			(x.getUsings() != null) && (x.getUsings().size() > 0);
		if (hasUsings) {
			printLineList(0, 0, x.getUsings());
		}
		boolean hasAttributes = (x.getAttributeSectionCount() > 0);

		printAttributeSections(x);

		boolean hasNamespaces =
			(x.getNamespaceSpecifications() != null)
				&& (x.getNamespaceSpecifications().size() > 0);
		if (hasNamespaces) {
			printBlockList(
				(x.getNamespaceSpecifications() != null) ? 2 : 0,
				0,
				x.getNamespaceSpecifications());
		}
		if (x.getDeclarations() != null) {
			printBlockList(
				(hasUsings || hasNamespaces) ? 2 : 0,
				0,
				x.getDeclarations());
		}
		printFooter(x);
		// we do this linefeed here to allow flushing of the pretty printer
		// single line comment work list
		printIndentation(1, 0);
	}

	public void visitClassDeclaration(ClassDeclaration x) {
		printHeader(x);
		printElementIndentation(x);
		int m = 0;

		printAttributeSections(x);

		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
		}
		if (m > 0) {
			printKeywordList(x.getModifiers());
			m = 1;
		}
		if (x.getIdentifier() != null) {
			printElementIndentation(m, x);
			print("class");
			printElement(1, x.getIdentifier());
		}
		if (x.getExtendedTypes() != null) {
			printElement(1, x.getExtendedTypes());
		}
		if (x.getIdentifier() != null) {
			print(' ');
		}
		print('{');
		if (x.getMembers() != null && !x.getMembers().isEmpty()) {
			printBlockList(2, 1, x.getMembers());
			changeLevel(-1);
		}
		printIndentation(1, getTotalIndentation());
		print('}');
		printFooter(x);
	}

	public void visitInterfaceDeclaration(InterfaceDeclaration x) {
		printHeader(x);

		printAttributeSections(x);

		int m = 0;
		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
		}
		if (m > 0) {
			printKeywordList(x.getModifiers());
			m = 1;
		}
		if (x.getIdentifier() != null) {
			printElementIndentation(m, x);
			print("interface");
			printElement(1, x.getIdentifier());
		}
		if (x.getExtendedTypes() != null) {
			printElement(1, x.getExtendedTypes());
		}
		print(" {");
		if (x.getMembers() != null && !x.getMembers().isEmpty()) {
			printBlockList(2, 1, x.getMembers());
			changeLevel(-1);
		}
		printIndentation(1, getTotalIndentation());
		print('}');
		printFooter(x);
	}

	public void visitFieldDeclaration(FieldDeclaration x) {
		printHeader(x);

		printAttributeSections(x);

		int m = 0;
		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
			printKeywordList(x.getModifiers());
		}
		if (x.getTypeReference() != null)
			printElement((m > 0) ? 1 : 0, x.getTypeReference());
		VariableSpecificationList varSpecs = x.getVariables();
		if (varSpecs != null) {
			printCommaList(0, 0, 1, varSpecs);
		}
		print(';');
		printFooter(x);
	}

	public void visitEnumMemberDeclaration(EnumMemberDeclaration x) {
		printHeader(x);

		printAttributeSections(x);

		VariableSpecification varSpec =
			x.getVariables().getVariableSpecification(0);
		printElement(varSpec);

		printFooter(x);
	}

	public void visitLocalVariableDeclaration(LocalVariableDeclaration x) {
		printHeader(x);
		int m = 0;
		// In C# there are no modifiers. 
		//		if (x.getModifiers() != null) {
		//			m = x.getModifiers().size();
		//			printKeywordList(x.getModifiers());
		//		}
		printElement((m > 0) ? 1 : 0, x.getTypeReference());
		VariableSpecificationList varSpecs = x.getVariables();
		if (varSpecs != null) {
			printCommaList(0, 0, 1, varSpecs);
		}
		if (!(x.getStatementContainer() instanceof LoopStatement)) {
			print(';');
		}
		printFooter(x);
	}

	protected void visitVariableDeclaration(VariableDeclaration x) {
		printHeader(x);
		int m = 0;
		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
			printKeywordList(x.getModifiers());
		}
		printElement((m > 0) ? 1 : 0, x.getTypeReference());
		VariableSpecificationList varSpecs = x.getVariables();
		if (varSpecs != null) {
			printCommaList(0, 0, 1, varSpecs);
		}
		printFooter(x);
	}

	public void visitMethodDeclaration(MethodDeclaration x) {
		printHeader(x);
		printElementIndentation(x);

		printAttributeSections(x);

		int m = 0;

		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
			printKeywordList(x.getModifiers());
		}
		if (x.getTypeReference() != null) {
			if (m > 0) {
				printElement(1, x.getTypeReference());
			} else {
				printElement(x.getTypeReference());
			}
			printElement(1, x.getMemberName());
		} else {
			if (m > 0) {
				printElement(1, x.getMemberName());
			} else {
				printElement(x.getMemberName());
			}
		}
		if (getBooleanProperty(GLUE_PARAMETER_LISTS)) {
			print('(');
		} else {
			print(" (");
		}
		if (x.getParameters() != null) {
			printCommaList(
				getBooleanProperty(GLUE_PARAMETERS) ? 0 : 1,
				x.getParameters());
		}
		print(')');
		if (x.getThrown() != null) {
			printElement(1, x.getThrown());
		}
		if (x.getBody() != null) {
			printElement(1, x.getBody());
		} else {
			print(';');
		}
		printFooter(x);
	}

	public void visitStatementBlock(StatementBlock x) {
		printHeader(x);
		printElementIndentation(x);
		print('{');
		if (x.getBody() != null && x.getBody().size() > 0) {
			printLineList(1, +1, x.getBody());
			changeLevel(-1);
			printIndentation(1, getTotalIndentation());
		}
		print('}');
		printFooter(x);
	}

	public void visitBreak(Break x) {
		printHeader(x);
		printElementIndentation(x);
		print("break");
		print(';');
		printFooter(x);
	}

	public void visitContinue(Continue x) {
		printHeader(x);
		printElementIndentation(x);
		print("continue");
		print(';');
		printFooter(x);
	}

	public void visitReturn(Return x) {
		printHeader(x);
		printElementIndentation(x);
		print("return");
		if (x.getExpression() != null) {
			printElement(1, x.getExpression());
		}
		print(';');
		printFooter(x);
	}

	public void visitThrow(Throw x) {
		printHeader(x);
		printElementIndentation(x);
		print("throw");
		if (x.getExpression() != null) {
			printElement(1, x.getExpression());
		}
		print(';');
		printFooter(x);
	}

	public void visitDo(Do x) {
		printHeader(x);
		printElementIndentation(x);
		print("do");
		if (x.getBody() == null || x.getBody() instanceof EmptyStatement) {
			print(';');
			//w.printElement(1, body);
		} else {
			if (getBooleanProperty(GLUE_STATEMENT_BLOCKS)) {
				printElement(1, x.getBody());
			} else {
				if (x.getBody() instanceof StatementBlock) {
					printElement(1, 0, x.getBody());
				} else {
					printElement(1, +1, 0, x.getBody());
					changeLevel(-1);
				}
			}
		}
		if (getBooleanProperty(GLUE_STATEMENT_BLOCKS)) {
			print(" while");
		} else {
			printIndentation(1, getTotalIndentation());
			print("while");
		}
		if (getBooleanProperty(GLUE_PARAMETER_LISTS)) {
			print('(');
		} else {
			print(" (");
		}
		if (x.getGuard() != null) {
			boolean glueExprParentheses =
				getBooleanProperty(GLUE_EXPRESSION_PARENTHESES);
			if (!glueExprParentheses) {
				print(' ');
			}
			printElement(x.getGuard());
			if (!glueExprParentheses) {
				print(' ');
			}
		}
		print(");");
		printFooter(x);
	}

	public void visitFor(For x) {
		printHeader(x);
		printElementIndentation(x);
		print(getBooleanProperty(GLUE_CONTROL_EXPRESSIONS) ? "for(" : "for (");
		boolean glueExprParentheses =
			getBooleanProperty(GLUE_EXPRESSION_PARENTHESES);
		if (!glueExprParentheses) {
			print(' ');
		}
		if (x.getInitializers() != null) {
			printCommaList(x.getInitializers());
		}
		print(';');
		if (x.getGuard() != null) {
			printElement(1, x.getGuard());
		}
		print(';');
		if (x.getUpdates() != null) {
			printCommaList(0, 0, 1, x.getUpdates());
		}
		if (!glueExprParentheses) {
			print(' ');
		}
		print(')');
		if (x.getBody() == null || x.getBody() instanceof EmptyStatement) {
			print(';');
		} else {
			if (getBooleanProperty(GLUE_STATEMENT_BLOCKS)) {
				printElement(1, x.getBody());
			} else {
				if (x.getBody() instanceof StatementBlock) {
					printElement(1, 0, x.getBody());
				} else {
					printElement(1, +1, 0, x.getBody());
					changeLevel(-1);
				}
			}
		}
		printFooter(x);
	}

	public void visitWhile(While x) {
		printHeader(x);
		printElementIndentation(x);
		print(
			getBooleanProperty(GLUE_CONTROL_EXPRESSIONS)
				? "while("
				: "while (");
		boolean glueExpParentheses =
			getBooleanProperty(GLUE_EXPRESSION_PARENTHESES);
		if (!glueExpParentheses) {
			print(' ');
		}
		if (x.getGuard() != null) {
			printElement(x.getGuard());
		}
		if (glueExpParentheses) {
			print(')');
		} else {
			print(" )");
		}
		if (x.getBody() == null || x.getBody() instanceof EmptyStatement) {
			print(';');
		} else {
			if (getBooleanProperty(GLUE_STATEMENT_BLOCKS)) {
				printElement(1, x.getBody());
			} else {
				if (x.getBody() instanceof StatementBlock) {
					printElement(1, 0, x.getBody());
				} else {
					printElement(1, +1, 0, x.getBody());
					changeLevel(-1);
				}
			}
		}
		printFooter(x);
	}

	/** @deprecated There is no assert in C# */
	public void visitAssert(Assert x) {
		printHeader(x);
		printElementIndentation(x);
		print("assert");
		if (x.getCondition() != null) {
			printElement(1, x.getCondition());
		}
		if (x.getMessage() != null) {
			print(" :");
			printElement(1, x.getMessage());
		}
		print(';');
		printFooter(x);
	}

	public void visitIf(If x) {
		printHeader(x);
		printElementIndentation(x);
		print(getBooleanProperty(GLUE_CONTROL_EXPRESSIONS) ? "if(" : "if (");
		boolean glueExpr = getBooleanProperty(GLUE_EXPRESSION_PARENTHESES);
		if (x.getExpression() != null) {
			if (glueExpr) {
				printElement(x.getExpression());
			} else {
				printElement(1, x.getExpression());
			}
		}
		if (glueExpr) {
			print(')');
		} else {
			print(" )");
		}
		if (x.getThen() != null) {
			if (getBooleanProperty(GLUE_STATEMENT_BLOCKS)) {
				printElement(1, x.getThen());
			} else {
				if (x.getThen().getBody() instanceof StatementBlock) {
					printElement(1, 0, x.getThen());
				} else {
					printElement(1, +1, 0, x.getThen());
					changeLevel(-1);
				}
			}
		}
		if (x.getElse() != null) {
			if (getBooleanProperty(GLUE_SEQUENTIAL_BRANCHES)) {
				printElement(1, x.getElse());
			} else {
				printElement(1, 0, x.getElse());
			}
		}
		printFooter(x);
	}

	public void visitSwitch(Switch x) {
		printHeader(x);
		printElementIndentation(x);
		print("switch (");
		if (x.getExpression() != null) {
			printElement(x.getExpression());
		}
		print(") {");
		if (x.getBranchList() != null) {
			if (getBooleanProperty(GLUE_SEQUENTIAL_BRANCHES)) {
				printLineList(1, +1, x.getBranchList());
				changeLevel(-1);
			} else {
				printLineList(1, 0, x.getBranchList());
			}
		}
		printIndentation(1, getTotalIndentation());
		print('}');
		printFooter(x);
	}

	public void visitTry(Try x) {
		printHeader(x);
		printElementIndentation(x);
		print("try");
		if (x.getBody() != null) {
			if (getBooleanProperty(GLUE_STATEMENT_BLOCKS)) {
				printElement(1, x.getBody());
			} else {
				printElement(1, 0, x.getBody());
			}
		}
		if (x.getBranchList() != null) {
			if (getBooleanProperty(GLUE_SEQUENTIAL_BRANCHES)) {
				for (int i = 0; i < x.getBranchList().size(); i++) {
					printElement(1, x.getBranchList().getBranch(i));
				}
			} else {
				printLineList(1, 0, x.getBranchList());
			}
		}
		printFooter(x);
	}

	public void visitLabeledStatement(LabeledStatement x) {
		printHeader(x);
		if (x.getIdentifier() != null) {
			printElement(x.getIdentifier());
			printElementIndentation(x);
			print(':');
		}
		if (x.getBody() != null) {
			printElement(1, 0, x.getBody());
		}
		printFooter(x);
	}

	public void visitLockedBlock(LockedBlock x) {
		printHeader(x);
		printElementIndentation(x);
		print("locked");
		if (x.getExpression() != null) {
			print('(');
			printElement(x.getExpression());
			print(')');
		}
		if (x.getBody() != null) {
			printElement(1, x.getBody());
		}
		printFooter(x);
	}

	public void visitUsing(Using x) {
		printHeader(x);
		printElementIndentation(x);
		print("using");
		printElement(1, x.getReference());
		print(';');
		printFooter(x);
	}

	public void visitUncollatedReferenceQualifier(UncollatedReferenceQualifier x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
			printElementIndentation(x);
			print('.');
		}
		if (x.getIdentifier() != null) {
			printElement(x.getIdentifier());
		}
		printFooter(x);
	}

	public void visitExtends(Extends x) {
		printHeader(x);
		if (x.getSupertypes() != null) {
			printElementIndentation(x);
			print(":");
			printCommaList(0, 0, 1, x.getSupertypes());
		}
		printFooter(x);
	}

	/** @deprecated There is no implements keyword in C#.
	 */
	public void visitImplements(Implements x) {
		printHeader(x);
		if (x.getSupertypes() != null) {
			printElementIndentation(x);
			print("implements");
			printCommaList(0, 0, 1, x.getSupertypes());
		}
		printFooter(x);
	}

	public void visitVariableSpecification(VariableSpecification x) {
		printHeader(x);
		printElement(x.getIdentifier());
		// In C# variable specifications don't have a dimension
		//        for (int i = 0; i < x.getDimensions(); i += 1) {
		//            print("[]");
		//        }
		if (x.getInitializer() != null) {
			print(" =");
			printElement(0, 0, 1, x.getInitializer());
		}
		printFooter(x);
	}

	public void visitBinaryAnd(BinaryAnd x) {
		printHeader(x);
		printOperator(x, "&");
		printFooter(x);
	}

	public void visitBinaryAndAssignment(BinaryAndAssignment x) {
		printHeader(x);
		printOperator(x, "&=");
		printFooter(x);
	}

	public void visitBinaryOrAssignment(BinaryOrAssignment x) {
		printHeader(x);
		printOperator(x, "|=");
		printFooter(x);
	}

	public void visitBinaryXOrAssignment(BinaryXOrAssignment x) {
		printHeader(x);
		printOperator(x, "^=");
		printFooter(x);
	}

	public void visitCopyAssignment(CopyAssignment x) {
		printHeader(x);
		printOperator(x, "=");
		printFooter(x);
	}

	public void visitDivideAssignment(DivideAssignment x) {
		printHeader(x);
		printOperator(x, "/=");
		printFooter(x);
	}

	public void visitMinusAssignment(MinusAssignment x) {
		printHeader(x);
		printOperator(x, "-=");
		printFooter(x);
	}

	public void visitModuloAssignment(ModuloAssignment x) {
		printHeader(x);
		printOperator(x, "%=");
		printFooter(x);
	}

	public void visitPlusAssignment(PlusAssignment x) {
		printHeader(x);
		printOperator(x, "+=");
		printFooter(x);
	}

	public void visitPostDecrement(PostDecrement x) {
		printHeader(x);
		printOperator(x, "--");
		printFooter(x);
	}

	public void visitPostIncrement(PostIncrement x) {
		printHeader(x);
		printOperator(x, "++");
		printFooter(x);
	}

	public void visitPreDecrement(PreDecrement x) {
		printHeader(x);
		printOperator(x, "--");
		printFooter(x);
	}

	public void visitPreIncrement(PreIncrement x) {
		printHeader(x);
		printOperator(x, "++");
		printFooter(x);
	}

	public void visitShiftLeftAssignment(ShiftLeftAssignment x) {
		printHeader(x);
		printOperator(x, "<<=");
		printFooter(x);
	}

	public void visitShiftRightAssignment(ShiftRightAssignment x) {
		printHeader(x);
		printOperator(x, ">>=");
		printFooter(x);
	}

	public void visitTimesAssignment(TimesAssignment x) {
		printHeader(x);
		printOperator(x, "*=");
		printFooter(x);
	}

	public void visitBinaryNot(BinaryNot x) {
		printHeader(x);
		printOperator(x, "~");
		printFooter(x);
	}

	public void visitBinaryOr(BinaryOr x) {
		printHeader(x);
		printOperator(x, "|");
		printFooter(x);
	}

	public void visitBinaryXOr(BinaryXOr x) {
		printHeader(x);
		printOperator(x, "^");
		printFooter(x);
	}

	public void visitConditional(Conditional x) {
		printHeader(x);
		boolean addParentheses = x.isToBeParenthesized();
		if (x.getArguments() != null) {
			if (addParentheses) {
				print('(');
			}
			printElement(0, x.getArguments().getExpression(0));
			print(" ?");
			printElement(1, x.getArguments().getExpression(1));
			print(" :");
			printElement(1, x.getArguments().getExpression(2));
			if (addParentheses) {
				print(')');
			}
		}
		printFooter(x);
	}

	public void visitDivide(Divide x) {
		printHeader(x);
		printOperator(x, "/");
		printFooter(x);
	}

	public void visitEquals(Equals x) {
		printHeader(x);
		printOperator(x, "==");
		printFooter(x);
	}

	public void visitGreaterOrEquals(GreaterOrEquals x) {
		printHeader(x);
		printOperator(x, ">=");
		printFooter(x);
	}

	public void visitGreaterThan(GreaterThan x) {
		printHeader(x);
		printOperator(x, ">");
		printFooter(x);
	}

	public void visitLessOrEquals(LessOrEquals x) {
		printHeader(x);
		printOperator(x, "<=");
		printFooter(x);
	}

	public void visitLessThan(LessThan x) {
		printHeader(x);
		printOperator(x, "<");
		printFooter(x);
	}

	public void visitNotEquals(NotEquals x) {
		printHeader(x);
		printOperator(x, "!=");
		printFooter(x);
	}

	public void visitNewArray(NewArray x) {
		boolean hadArguments = false;

		printHeader(x);
		boolean addParentheses = x.isToBeParenthesized();
		if (addParentheses) {
			print('(');
		}
		printElementIndentation(x);
		print("new");
		printElement(1, x.getTypeReference());
		int i = 0;
		if (x.getArguments() != null) {
			hadArguments = true;
			print('[');
			for (; i < x.getArguments().size(); i += 1) {
				printElement(x.getArguments().getExpression(i));
				if (i < x.getArguments().size() - 1)
					print(',');
			}
			print(']');
		}
		int[] dimensions = x.getDimensions();
		for (i = hadArguments ? 1 : 0; i < dimensions.length; i++) {
			print('[');
			for (int j = 0; j < dimensions[i] - 1; j++)
				print(',');
			print(']');
		}
		if (x.getArrayInitializer() != null) {
			printElement(1, x.getArrayInitializer());
		}
		if (addParentheses) {
			print(')');
		}
		printFooter(x);
	}

	public void visitInstanceof(Instanceof x) {
		printHeader(x);
		boolean addParentheses = x.isToBeParenthesized();
		if (addParentheses) {
			print('(');
		}
		if (x.getArguments() != null) {
			printElement(0, x.getArguments().getExpression(0));
		}
		printElementIndentation(1, x);
		print("is");
		if (x.getTypeReference() != null) {
			printElement(1, x.getTypeReference());
		}
		if (addParentheses) {
			print(')');
		}
		printFooter(x);
	}

	public void visitNew(New x) {
		printHeader(x);
		boolean addParentheses = x.isToBeParenthesized();
		if (addParentheses) {
			print('(');
		}
		if (x.getReferencePrefix() != null) {
			printElement(0, x.getReferencePrefix());
			print('.');
		}
		printElementIndentation(x);
		print("new");
		printElement(1, x.getTypeReference());
		if (getBooleanProperty(GLUE_PARAMETER_LISTS)) {
			print('(');
		} else {
			print(" (");
		}
		if (x.getArguments() != null) {
			printCommaList(x.getArguments());
		}
		print(')');
		if (x.getClassDeclaration() != null) {
			printElement(1, x.getClassDeclaration());
		}
		if (addParentheses) {
			print(')');
		}
		if (x.getStatementContainer() != null) {
			print(';');
		}
		printFooter(x);
	}

	public void visitTypeCast(TypeCast x) {
		printHeader(x);
		boolean addParentheses = x.isToBeParenthesized();
		if (addParentheses) {
			print('(');
		}
		printElementIndentation(x);
		print('(');
		if (x.getTypeReference() != null) {
			printElement(0, x.getTypeReference());
		}
		print(')');
		if (x.getArguments() != null) {
			printElement(0, x.getArguments().getExpression(0));
		}
		if (addParentheses) {
			print(')');
		}
		printFooter(x);
	}

	public void visitLogicalAnd(LogicalAnd x) {
		printHeader(x);
		printOperator(x, "&&");
		printFooter(x);
	}

	public void visitLogicalNot(LogicalNot x) {
		printHeader(x);
		printOperator(x, "!");
		printFooter(x);
	}

	public void visitLogicalOr(LogicalOr x) {
		printHeader(x);
		printOperator(x, "||");
		printFooter(x);
	}

	public void visitMinus(Minus x) {
		printHeader(x);
		printOperator(x, "-");
		printFooter(x);
	}

	public void visitModulo(Modulo x) {
		printHeader(x);
		printOperator(x, "%");
		printFooter(x);
	}

	public void visitNegative(Negative x) {
		printHeader(x);
		printOperator(x, "-");
		printFooter(x);
	}

	public void visitPlus(Plus x) {
		printHeader(x);
		printOperator(x, "+");
		printFooter(x);
	}

	public void visitPositive(Positive x) {
		printHeader(x);
		printOperator(x, "+");
		printFooter(x);
	}

	public void visitShiftLeft(ShiftLeft x) {
		printHeader(x);
		printOperator(x, "<<");
		printFooter(x);
	}

	public void visitShiftRight(ShiftRight x) {
		printHeader(x);
		printOperator(x, ">>");
		printFooter(x);
	}

	public void visitTimes(Times x) {
		printHeader(x);
		printOperator(x, "*");
		printFooter(x);
	}

	public void visitArrayReference(ArrayReference x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
		}
		if (x.getDimensionExpressions() != null) {
			int s = x.getDimensionExpressions().size();
			print('[');
			for (int i = 0; i < s; i += 1) {
				printElement(x.getDimensionExpressions().getExpression(i));
				if (i < s - 1)
					print(',');
			}
			print(']');
		}
		printFooter(x);
	}

	public void visitFieldReference(FieldReference x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
			printElementIndentation(x);
			print('.');
		}
		if (x.getIdentifier() != null) {
			printElement(x.getIdentifier());
		}
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitMethodGroupReference(MethodGroupReference)
	 */
	public void visitMethodGroupReference(MethodGroupReference x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
			printElementIndentation(x);
			print('.');
		}
		if (x.getIdentifier() != null) {
			printElement(x.getIdentifier());
		}
		printFooter(x);
	}

	public void visitVariableReference(VariableReference x) {
		printHeader(x);
		if (x.getIdentifier() != null) {
			printElement(x.getIdentifier());
		}
		printFooter(x);
	}

	public void visitMetaClassReference(MetaClassReference x) {
		printHeader(x);
		if (x.getTypeReference() != null) {
			printElement(x.getTypeReference());
			printElementIndentation(x);
			print('.');
		}
		print("class");
		printFooter(x);
	}

	public void visitMethodReference(MethodReference x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
			// printElementIndentation(x);  not yet implemented
			print('.');
		}
		if (x.getIdentifier() != null) {
			printElement(x.getIdentifier());
		}
		if (getBooleanProperty(GLUE_PARAMETER_LISTS)) {
			print('(');
		} else {
			print(" (");
		}
		if (x.getArguments() != null) {
			printCommaList(x.getArguments());
		}
		print(')');
		if (x.getStatementContainer() != null) {
			print(';');
		}
		printFooter(x);
	}

	public void visitSuperConstructorReference(SuperConstructorReference x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
			print('.');
		}
		printElementIndentation(x);
		if (getBooleanProperty(GLUE_PARAMETER_LISTS)) {
			print("base(");
		} else {
			print("base (");
		}
		if (x.getArguments() != null) {
			printCommaList(x.getArguments());
		}
		print(")");
		printFooter(x);
	}

	public void visitThisConstructorReference(ThisConstructorReference x) {
		printHeader(x);
		printElementIndentation(x);
		print(getBooleanProperty(GLUE_PARAMETER_LISTS) ? "this(" : "this (");
		if (x.getArguments() != null) {
			printCommaList(x.getArguments());
		}
		print(")");
		printFooter(x);
	}

	public void visitSuperReference(SuperReference x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
			printElementIndentation(x);
			print(".base");
		} else {
			printElementIndentation(x);
			print("base");
		}
		printFooter(x);
	}

	public void visitThisReference(ThisReference x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
			printElementIndentation(x);
			print(".this");
		} else {
			printElementIndentation(x);
			print("this");
		}
		printFooter(x);
	}

	/** @deprecated This is different in C# */
	public void visitArrayLengthReference(ArrayLengthReference x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
			print('.');
		}
		printElementIndentation(x);
		print("length");
		printFooter(x);
	}

	public void visitThen(Then x) {
		printHeader(x);
		if (x.getBody() != null) {
			printElement(x.getBody());
		}
		printFooter(x);
	}

	public void visitElse(Else x) {
		printHeader(x);
		printElementIndentation(x);
		print("else");
		if (x.getBody() != null) {
			if (getBooleanProperty(GLUE_STATEMENT_BLOCKS)) {
				printElement(1, x.getBody());
			} else {
				if (x.getBody() instanceof StatementBlock) {
					printElement(1, 0, x.getBody());
				} else {
					printElement(1, +1, 0, x.getBody());
					changeLevel(-1);
				}
			}
		}
		printFooter(x);
	}

	public void visitCase(Case x) {
		printHeader(x);
		printElementIndentation(x);
		print("case");
		if (x.getExpression() != null) {
			printElement(1, x.getExpression());
		}
		print(':');
		if (x.getBody() != null && x.getBody().size() > 0) {
			printLineList(1, +1, x.getBody());
			changeLevel(-1);
		}
		printFooter(x);
	}

	public void visitCatch(Catch x) {
		printHeader(x);
		if (getBooleanProperty(GLUE_CONTROL_EXPRESSIONS)) {
			printElementIndentation(x);
			print("catch(");
		} else {
			printElementIndentation(x);
			print("catch (");
		}
		if (x.getParameterDeclaration() != null) {
			printElement(x.getParameterDeclaration());
		}
		print(')');
		if (x.getBody() != null) {
			printElement(1, x.getBody());
		}
		printFooter(x);
	}

	public void visitDefault(Default x) {
		printHeader(x);
		printElementIndentation(x);
		print("default:");
		if (x.getBody() != null && x.getBody().size() > 0) {
			printLineList(1, +1, x.getBody());
			changeLevel(-1);
		}
		printFooter(x);
	}

	public void visitFinally(Finally x) {
		printHeader(x);
		printElementIndentation(x);
		print("finally");
		if (x.getBody() != null) {
			printElement(1, x.getBody());
		}
		printFooter(x);
	}

	public void visitAbstract(Abstract x) {
		printHeader(x);
		printElementIndentation(x);
		print("abstract");
		printFooter(x);
	}

	public void visitSealed(Sealed x) {
		printHeader(x);
		printElementIndentation(x);
		print("sealed");
		printFooter(x);
	}

	public void visitPrivate(Private x) {
		printHeader(x);
		printElementIndentation(x);
		print("private");
		printFooter(x);
	}

	public void visitProtected(Protected x) {
		printHeader(x);
		printElementIndentation(x);
		print("protected");
		printFooter(x);
	}

	public void visitPublic(Public x) {
		printHeader(x);
		printElementIndentation(x);
		print("public");
		printFooter(x);
	}

	public void visitStatic(Static x) {
		printHeader(x);
		printElementIndentation(x);
		print("static");
		printFooter(x);
	}

	public void visitRef(Ref x) {
		printHeader(x);
		printElementIndentation(x);
		print("ref");
		printFooter(x);
	}

	public void visitVolatile(Volatile x) {
		printHeader(x);
		printElementIndentation(x);
		print("volatile");
		printFooter(x);
	}

	public void visitEmptyStatement(EmptyStatement x) {
		printHeader(x);
		printElementIndentation(x);
		print(';');
		printFooter(x);
	}

	public void visitComment(Comment x) {
		printElementIndentation(x);
		print(x.getText());
		if (overwriteParsePositions) {
			overwritePosition.setPosition(line, Math.max(0, column - 1));
			x.getLastElement().setEndPosition(overwritePosition);
		}
	}

	public void visitParenthesizedExpression(ParenthesizedExpression x) {
		printHeader(x);
		printElementIndentation(x);
		print('(');
		if (x.getArguments() != null) {
			printElement(x.getArguments().getExpression(0));
		}
		print(')');
		printFooter(x);
	}
	/**
	 * @see recoder.csharp.SourceVisitor#visitAddAccessor(AddAccessor)
	 */
	public void visitAddAccessor(AddAccessor x) {
		printHeader(x);
		printElementIndentation(x);
		print("add");
		if (x.getStatementCount() > 0)
			printElement(1, x.getStatementAt(0));
		else
			print(';');
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitAs(As)
	 */
	public void visitAs(As x) {
		printHeader(x);
		boolean addParentheses = x.isToBeParenthesized();
		if (addParentheses) {
			print('(');
		}
		if (x.getArguments() != null) {
			printElement(0, x.getArguments().getExpression(0));
		}
		printElementIndentation(1, x);
		print("as");
		if (x.getTypeReference() != null) {
			printElement(1, x.getTypeReference());
		}
		if (addParentheses) {
			print(')');
		}
		printFooter(x);

	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitAssemblyTarget(AssemblyTarget)
	 */
	public void visitAssemblyTarget(AssemblyTarget x) {
		visitTarget("assembly", x);
	}

	/**
	 * Method visitTarget.
	 * @param string
	 */
	private void visitTarget(String string, AttributeTarget target) {
		printHeader(target);
		printElementIndentation(target);
		print(string);
		printIndentation(0, 1);
		print(':');
		printFooter(target);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitAttribute(Attribute)
	 */
	public void visitAttribute(Attribute x) {
		printHeader(x);
		printElementIndentation(x);
		printElement(x.getTypeReferenceAt(0));
		if (x.getAttributeArguments() != null) {
			print('(');
			printProgramElementList(
				0,
				0,
				0,
				",",
				0,
				1,
				x.getAttributeArguments());
			print(')');
		}
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitAttributeSection(AttributeSection)
	 */
	public void visitAttributeSection(AttributeSection x) {
		printHeader(x);
		printElementIndentation(x);
		print('[');
		int blanks = 0;
		if (x.getTarget() != null) {
			printElement(0, x.getTarget());
			blanks = 1;
		}
		printProgramElementList(0, 0, blanks, ",", 0, 1, x.getAttributes());
		print(']');
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitChecked(Checked)
	 */
	public void visitChecked(Checked x) {
		printHeader(x);
		boolean addParentheses = x.isToBeParenthesized();
		if (addParentheses) {
			print('(');
		}
		printElementIndentation(x);
		print("checked");
		print('(');
		if (x.getArguments() != null) {
			printElement(0, x.getArguments().getExpression(0));
		}
		print(')');
		if (addParentheses) {
			print(')');
		}
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitCheckedBlock(CheckedBlock)
	 */
	public void visitCheckedBlock(CheckedBlock x) {
		printHeader(x);
		printElementIndentation(x);
		print("checked");
		if (x.getBody() != null) {
			printElement(1, x.getBody());
		}
		printFooter(x);
	}

	//	/**
	//	 * @see recoder.csharp.SourceVisitor#visitConstructorDeclaration(ConstructorDeclaration)
	//	 */
	//	public void visitConstructorDeclaration(ConstructorDeclaration x) {
	//		super.visitConstructorDeclaration(x);
	//	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitDelegateDeclaration(DelegateDeclaration)
	 */
	public void visitDelegateDeclaration(DelegateDeclaration x) {
		printHeader(x);

		printAttributeSections(x);

		int m = 0;

		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
			printKeywordList(x.getModifiers());
		}

		if (m == 0)
			printElementIndentation(1, x);
		print("delegate");

		if (x.getTypeReference() != null) {
			printElement(1, x.getTypeReference());
			printElement(1, x.getIdentifier());
		} else {
			printElement(1, x.getIdentifier());
		}
		if (getBooleanProperty(GLUE_PARAMETER_LISTS)) {
			print('(');
		} else {
			print(" (");
		}
		if (x.getParameters() != null) {
			printCommaList(
				getBooleanProperty(GLUE_PARAMETERS) ? 0 : 1,
				x.getParameters());
		}
		print(')');
		print(';');
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitDestructorDeclaration(DestructorDeclaration)
	 */
	public void visitDestructorDeclaration(DestructorDeclaration x) {
		printHeader(x);
		//		printElementIndentation(x);

		printAttributeSections(x);

		int m = 0;
		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
			printKeywordList(x.getModifiers());
		}

		printIndentation(0, 1);
		print('~');
		printElement(x.getMemberName());

		if (getBooleanProperty(GLUE_PARAMETER_LISTS)) {
			print("()");
		} else {
			print(" ( )");
		}
		if (x.getBody() != null) {
			printElement(1, x.getBody());
		} else {
			print(';');
		}
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitDocComment(XmlDocComment)
	 */
	public void visitDocComment(XmlDocComment x) {
		super.visitDocComment(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitEnumDeclaration(EnumDeclaration)
	 */
	public void visitEnumDeclaration(EnumDeclaration x) {
		printHeader(x);
		//		printElementIndentation(x);
		int m = 0;

		printAttributeSections(x);

		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
		}
		if (m > 0) {
			printKeywordList(x.getModifiers());
			m = 1;
		}
		if (x.getIdentifier() != null) {
			printElementIndentation(m, x);
			print("enum");
			printElement(1, x.getIdentifier());
		}
		if (x.getBaseTypeReference() != null) {
			print(" :");
			printElement(1, x.getBaseTypeReference());
		}
		if (x.getIdentifier() != null) {
			print(' ');
		}
		print('{');
		if (x.getMembers() != null && !x.getMembers().isEmpty()) {
			printCommaList(2, 1, 0, x.getMembers());
			changeLevel(-1);
		}
		printIndentation(1, getTotalIndentation());
		print('}');
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitEventDeclaration(EventDeclaration)
	 */
	public void visitEventDeclaration(EventDeclaration x) {
		printHeader(x);
		printElementIndentation(x);
		int m = 0;

		printAttributeSections(x);

		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
		}
		if (m > 0) {
			printKeywordList(x.getModifiers());
			m = 1;
		}
		printElementIndentation(m, x);
		print("event");
		if (x.getTypeReference() != null) {
			printElement(1, x.getTypeReference());
		}
		printCommaList(0, 0, 1, x.getVariables());
		print(';');
		if (x.hasAddAccessor() || x.hasRemoveAccessor()) {
			print(" {");
			if (x.hasAddAccessor())
				printElement(x.getAddAccessor());
			if (x.hasRemoveAccessor())
				printElement(x.getRemoveAccessor());
			print('}');
		}

		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitExtern(Extern)
	 */
	public void visitExtern(Extern x) {
		printHeader(x);
		printElementIndentation(x);
		print("extern");
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitFieldSpecification(FieldSpecification)
	 */
	public void visitFieldSpecification(FieldSpecification x) {
		super.visitFieldSpecification(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitGetAccessor(GetAccessor)
	 */
	public void visitGetAccessor(GetAccessor x) {
		printHeader(x);
		printElementIndentation(x);
		print("get");
		if (x.getStatementCount() > 0)
			printElement(1, x.getStatementAt(0));
		else
			print(';');
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitGoto(Goto)
	 */
	public void visitGoto(Goto x) {
		printHeader(x);
		printElementIndentation(x);
		print("goto");
		if (x.getIdentifier() != null) {
			printElement(1, x.getIdentifier());
		}
		print(';');
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitIndexerDeclaration(IndexerDeclaration)
	 */
	public void visitIndexerDeclaration(IndexerDeclaration x) {
		printHeader(x);
		printElementIndentation(x);

		printAttributeSections(x);

		int m = 0;

		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
			printKeywordList(x.getModifiers());
		}
		if (x.getTypeReference() != null) {
			if (m > 0) {
				printElement(1, x.getTypeReference());
			} else {
				printElement(x.getTypeReference());
			}
		}
		if (x.getMemberName() != null) {
			if (m>0 || x.getTypeReference() != null) {
				printElement(1,x.getMemberName()); 
			}
			else {
				printElement(0,x.getMemberName()); 
			}	
		}	
 
		if (getBooleanProperty(GLUE_PARAMETER_LISTS)) {
			print('[');
		} else {
			print(" [");
		}
		if (x.getParameters() != null) {
			printCommaList(
				getBooleanProperty(GLUE_PARAMETERS) ? 0 : 1,
				x.getParameters());
		}
		print(']');
		print(" {");
		if (x.hasGetAccessor())
			printElement(x.getGetAccessor());
		if (x.hasSetAccessor())
			printElement(x.getSetAccessor());
		print('}');

		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitLiteral(Literal)
	 */
	protected void visitLiteral(Literal x) {
		super.visitLiteral(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitMemberName(MemberName)
	 */
	public void visitMemberName(MemberName x) {
		printHeader(x);
		if (x.getTypeReferenceCount() == 1) {
			printElement(x.getTypeReferenceAt(0));
			print('.');
		}
		printElement(x.getIdentifier());
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitModifier(Modifier)
	 */
	protected void visitModifier(Modifier x) {
		super.visitModifier(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitOperator(Operator)
	 */
	protected void visitOperator(Operator x) {
		super.visitOperator(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitOperatorDeclaration(OperatorDeclaration)
	 */
	public void visitOperatorDeclaration(OperatorDeclaration x) {
		printHeader(x);
		printElementIndentation(x);

		printAttributeSections(x);

		int m = 0;

		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
			printKeywordList(x.getModifiers());
		}

		if (x.getOperatorType() != OperatorDeclaration.EXPLICIT_CAST
			&& x.getOperatorType() != OperatorDeclaration.IMPLICIT_CAST) {

			if (x.getTypeReference() != null) {
				if (m > 0) {
					printElement(1, x.getTypeReference());
				} else {
					printElement(x.getTypeReference());
				}
				printIndentation(0, 1);
				print("operator" + x.getOperatorSymbol());
			} else {
				if (m > 0) {
					printIndentation(0, 1);
					print("operator" + x.getOperatorSymbol());
				} else {
					print("operator" + x.getOperatorSymbol());
				}
			}
		} else {
			// This is an explicit or implicit cast
			if (x.getOperatorType() == OperatorDeclaration.IMPLICIT_CAST)
				print("implicit operator");
			else
				print("explicit operator");

			printElement(x.getTypeReference());

		}

		if (getBooleanProperty(GLUE_PARAMETER_LISTS)) {
			print('(');
		} else {
			print(" (");
		}
		if (x.getParameters() != null) {
			printCommaList(
				getBooleanProperty(GLUE_PARAMETERS) ? 0 : 1,
				x.getParameters());
		}
		print(')');
		if (x.getThrown() != null) {
			printElement(1, x.getThrown());
		}
		if (x.getBody() != null) {
			printElement(1, x.getBody());
		} else {
			print(';');
		}
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitOut(Out)
	 */
	public void visitOut(Out x) {
		printHeader(x);
		printElementIndentation(x);
		print("out");
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitOutOperator(OutOperator)
	 */
	public void visitOutOperator(OutOperator x) {
		printHeader(x);
		printElementIndentation(x);
		print("out");
		printElement(1, x.getArguments().getExpression(0));
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitOverride(Override)
	 */
	public void visitOverride(Override x) {
		printHeader(x);
		printElementIndentation(x);
		print("override");
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitParams(Params)
	 */
	public void visitParams(Params x) {
		printHeader(x);
		printElementIndentation(x);
		print("params");
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitPreDefinedTypeLiteral(PreDefinedTypeLiteral)
	 */
	public void visitPreDefinedTypeLiteral(PreDefinedTypeLiteral x) {
		printHeader(x);
		printElementIndentation(x);
		print(x.getValue());
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitPropertyDeclaration(PropertyDeclaration)
	 */
	public void visitPropertyDeclaration(PropertyDeclaration x) {
		printHeader(x);

		printAttributeSections(x);

		int m = 0;
		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
			printKeywordList(x.getModifiers());
		}
		printElement((m > 0) ? 1 : 0, x.getTypeReference());
		printElement(1, x.getPropertySpecification().getIdentifier());
		print(" {");
		if (x.hasGetAccessor())
			printElement(x.getGetAccessor());
		if (x.hasSetAccessor())
			printElement(x.getSetAccessor());
		print('}');
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitReadonly(Readonly)
	 */
	public void visitReadonly(Readonly x) {
		printHeader(x);
		printElementIndentation(x);
		print("readonly");
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitRefOperator(RefOperator)
	 */
	public void visitRefOperator(RefOperator x) {
		printHeader(x);
		printElementIndentation(x);
		print("ref");
		printElement(1, x.getArguments().getExpression(0));
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitRemoveAccessor(RemoveAccessor)
	 */
	public void visitRemoveAccessor(RemoveAccessor x) {
		printHeader(x);
		printElementIndentation(x);
		print("remove");
		if (x.getStatementCount() > 0)
			printElement(1, x.getStatementAt(0));
		else
			print(';');
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitSetAccessor(SetAccessor)
	 */
	public void visitSetAccessor(SetAccessor x) {
		printHeader(x);
		printElementIndentation(x);
		print("set");
		if (x.getStatementCount() > 0)
			printElement(1, x.getStatementAt(0));
		else
			print(';');
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitStructDeclaration(StructDeclaration)
	 */
	public void visitStructDeclaration(StructDeclaration x) {
		printHeader(x);
		printElementIndentation(x);
		int m = 0;

		printAttributeSections(x);

		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
		}
		if (m > 0) {
			printKeywordList(x.getModifiers());
			m = 1;
		}
		if (x.getIdentifier() != null) {
			printElementIndentation(m, x);
			print("struct");
			printElement(1, x.getIdentifier());
		}
		if (x.getExtendedTypes() != null) {
			printElement(1, x.getExtendedTypes());
		}
		if (x.getIdentifier() != null) {
			print(' ');
		}
		print('{');
		if (x.getMembers() != null && !x.getMembers().isEmpty()) {
			printBlockList(2, 1, x.getMembers());
			changeLevel(-1);
		}
		printIndentation(1, getTotalIndentation());
		print('}');
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitTypeof(Typeof)
	 */
	public void visitTypeof(Typeof x) {
		printHeader(x);
		boolean addParentheses = x.isToBeParenthesized();
		if (addParentheses) {
			print('(');
		}
		printElementIndentation(x);
		print("typeof");
		print('(');
		if (x.getTypeReference() != null) {
			printElement(0, x.getTypeReference());
		}
		print(')');
		if (addParentheses) {
			print(')');
		}
		printFooter(x);

	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitUnchecked(Unchecked)
	 */
	public void visitUnchecked(Unchecked x) {
		printHeader(x);
		boolean addParentheses = x.isToBeParenthesized();
		if (addParentheses) {
			print('(');
		}
		printElementIndentation(x);
		print("unchecked");
		print('(');
		if (x.getArguments() != null) {
			printElement(0, x.getArguments().getExpression(0));
		}
		print(')');
		if (addParentheses) {
			print(')');
		}
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitUncheckedBlock(UncheckedBlock)
	 */
	public void visitUncheckedBlock(UncheckedBlock x) {
		printHeader(x);
		printElementIndentation(x);
		print("unchecked");
		if (x.getBody() != null) {
			printElement(1, x.getBody());
		}
		printFooter(x);

	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitUsingBlock(UsingBlock)
	 */
	public void visitUsingBlock(UsingBlock x) {
		printHeader(x);
		printElementIndentation(x);
		print("using");
		if (x.getLocalVariableDeclaration() != null) {
			print('(');
			printElement(x.getLocalVariableDeclaration());
			print(')');
		} else if (x.getExpression() != null) {
			print('(');
			printElement(x.getExpression());
			print(')');
		}
		if (x.getBody() != null) {
			printElement(1, x.getBody());
		}
		printFooter(x);

	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitVirtual(Virtual)
	 */
	public void visitVirtual(Virtual virtual) {
		printHeader(virtual);
		printElementIndentation(virtual);
		print("virtual");
		printFooter(virtual);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitStaticConstructorDeclaration(StaticConstructorDeclaration)
	 */
	public void visitStaticConstructorDeclaration(StaticConstructorDeclaration staticConstructorDeclaration) {
		visitConstructorDeclaration(staticConstructorDeclaration);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitAttributeArgument(AttributeArgument)
	 */
	public void visitAttributeArgument(AttributeArgument x) {
		printHeader(x);
		printElementIndentation(x);
		if (x.getExpressionCount() > 0) {
			printElement(x.getExpressionAt(0));
		}
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitEventTarget(EventTarget)
	 */
	public void visitEventTarget(EventTarget eventTarget) {
		visitTarget("event", eventTarget);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitFieldTarget(FieldTarget)
	 */
	public void visitFieldTarget(FieldTarget fieldTarget) {
		visitTarget("field", fieldTarget);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitInternal(Internal)
	 */
	public void visitInternal(Internal x) {
		printHeader(x);
		printElementIndentation(x);
		print("internal");
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitMethodTarget(MethodTarget)
	 */
	public void visitMethodTarget(MethodTarget methodTarget) {
		visitTarget("method", methodTarget);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitModuleTarget(ModuleTarget)
	 */
	public void visitModuleTarget(ModuleTarget moduleTarget) {
		visitTarget("module", moduleTarget);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitNamedAttributeArgument(NamedAttributeArgument)
	 */
	public void visitNamedAttributeArgument(NamedAttributeArgument x) {
		printHeader(x);
		printElementIndentation(x);
		printElement(x.getIdentifier());
		printIndentation(0, 1);
		print('=');
		printElement(1, x.getExpressionAt(0));
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitNewModifier(NewModifier)
	 */
	public void visitNewModifier(NewModifier x) {
		printHeader(x);
		printElementIndentation(x);
		print("new");
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitParamTarget(ParamTarget)
	 */
	public void visitParamTarget(ParamTarget paramTarget) {
		visitTarget("param", paramTarget);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitPropertyTarget(PropertyTarget)
	 */
	public void visitPropertyTarget(PropertyTarget propertyTarget) {
		visitTarget("property", propertyTarget);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitReturnTarget(ReturnTarget)
	 */
	public void visitReturnTarget(ReturnTarget returnTarget) {
		visitTarget("return", returnTarget);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitTypeTarget(TypeTarget)
	 */
	public void visitTypeTarget(TypeTarget typeTarget) {
		visitTarget("type", typeTarget);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitUsingAlias(UsingAlias)
	 */
	public void visitUsingAlias(UsingAlias x) {
		printHeader(x);
		printElementIndentation(x);
		print("using");
		printElement(1, x.getIdentifier());
		printIndentation(0, 1);
		print('=');
		printElement(1, x.getReference());
		print(';');
		printFooter(x);

	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitForeach(Foreach)
	 */
	public void visitForeach(Foreach x) {
		printHeader(x);
		printElementIndentation(x);
		print(
			getBooleanProperty(GLUE_CONTROL_EXPRESSIONS)
				? "foreach("
				: "foreach (");
		boolean glueExprParentheses =
			getBooleanProperty(GLUE_EXPRESSION_PARENTHESES);
		if (!glueExprParentheses) {
			print(' ');
		}
		printElement(x.getTypeReferenceAt(0));
		print(' ');
		printElement(1, x.getIdentifier());

		print(" in ");
		printElement(x.getExpression());
		if (!glueExprParentheses) {
			print(' ');
		}
		print(')');
		if (x.getBody() == null || x.getBody() instanceof EmptyStatement) {
			print(';');
		} else {
			if (getBooleanProperty(GLUE_STATEMENT_BLOCKS)) {
				printElement(1, x.getBody());
			} else {
				if (x.getBody() instanceof StatementBlock) {
					printElement(1, 0, x.getBody());
				} else {
					printElement(1, +1, 0, x.getBody());
					changeLevel(-1);
				}
			}
		}
		printFooter(x);

	}

	protected void printAttributeSections(AttributableElement x) {
		if (x.getAttributeSectionCount() > 0)
			printProgramElementList(
				1,
				0,
				0,
				"",
				1,
				0,
				x.getAttributeSections());
		//		printIndentation(1, 0);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitConstantFieldDeclaration(ConstantFieldDeclaration)
	 */
	public void visitConstantFieldDeclaration(ConstantFieldDeclaration x) {
		printHeader(x);
		//		printElementIndentation(x);

		printAttributeSections(x);

		int m = 0;
		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
			printKeywordList(x.getModifiers());
		}
		printIndentation(0, (m > 0) ? 1 : 0);
		print("const");
		printElement(x.getTypeReference());
		VariableSpecificationList varSpecs = x.getVariables();
		if (varSpecs != null) {
			printCommaList(0, 0, 1, varSpecs);
		}
		print(';');
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitLocalConstantDeclaration(LocalConstantDeclaration)
	 */
	public void visitLocalConstantDeclaration(LocalConstantDeclaration x) {
		printHeader(x);
		int m = 0;
		print("const");
		printElement((m > 0) ? 1 : 0, x.getTypeReference());
		VariableSpecificationList varSpecs = x.getVariables();
		if (varSpecs != null) {
			printCommaList(0, 0, 1, varSpecs);
		}
		if (!(x.getStatementContainer() instanceof LoopStatement)) {
			print(';');
		}
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitParameterDeclaration(ParameterDeclaration)
	 */
	public void visitParameterDeclaration(ParameterDeclaration x) {
		printHeader(x);
		if (x.getAttributeSectionCount() > 0)
			printProgramElementList(
				0,
				0,
				1,
				"",
				0,
				1,
				x.getAttributeSections());

		int m = 0;
		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
			printKeywordList(x.getModifiers());
		}

		printElement((m > 0) ? 1 : 0, x.getTypeReference());
		VariableSpecificationList varSpecs = x.getVariables();
		if (varSpecs != null) {
			printCommaList(0, 0, 1, varSpecs);
		}
		printFooter(x);

	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitConstructorDeclaration(ConstructorDeclaration)
	 */
	public void visitConstructorDeclaration(ConstructorDeclaration x) {
		printHeader(x);
		printElementIndentation(x);

		printAttributeSections(x);

		int m = 0;

		if (x.getModifiers() != null) {
			m = x.getModifiers().size();
			printKeywordList(x.getModifiers());
		}
		if (m > 0) {
			printElement(1, x.getMemberName());
		} else {
			printElement(x.getMemberName());
		}

		if (getBooleanProperty(GLUE_PARAMETER_LISTS)) {
			print('(');
		} else {
			print(" (");
		}
		if (x.getParameters() != null) {
			printCommaList(
				getBooleanProperty(GLUE_PARAMETERS) ? 0 : 1,
				x.getParameters());
		}
		print(')');
		if (x.getSpecialConstructorReference() != null) {
			print(':');
			printElement(1, x.getSpecialConstructorReference());
		}

		if (x.getBody() != null) {
			printElement(1, x.getBody());
		} else {
			print(';');
		}
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitEventSpecification(EventSpecification)
	 */
	public void visitEventSpecification(EventSpecification x) {
		printHeader(x);
		printElement(x.getMemberName());
		// In C# variable specifications don't have a dimension
		//        for (int i = 0; i < x.getDimensions(); i += 1) {
		//            print("[]");
		//        }
		if (x.getInitializer() != null) {
			print(" =");
			printElement(0, 0, 1, x.getInitializer());
		}
		printFooter(x);
	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitDelegateCallReference(DelegateCallReference)
	 */
	public void visitDelegateCallReference(DelegateCallReference x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
			// printElementIndentation(x);  not yet implemented
			print('.');
		}
		if (x.getIdentifier() != null) {
			printElement(x.getIdentifier());
		}
		if (getBooleanProperty(GLUE_PARAMETER_LISTS)) {
			print('(');
		} else {
			print(" (");
		}
		if (x.getArguments() != null) {
			printCommaList(x.getArguments());
		}
		print(')');
		if (x.getStatementContainer() != null) {
			print(';');
		}
		printFooter(x);

	}

	/**
	 * @see recoder.csharp.SourceVisitor#visitUncollatedMethodCallReference(UncollatedMethodCallReference)
	 */
	public void visitUncollatedMethodCallReference(UncollatedMethodCallReference x) {
		printHeader(x);
		if (x.getReferencePrefix() != null) {
			printElement(x.getReferencePrefix());
			// printElementIndentation(x);  not yet implemented
			print('.');
		}
		if (x.getIdentifier() != null) {
			printElement(x.getIdentifier());
		}
		if (getBooleanProperty(GLUE_PARAMETER_LISTS)) {
			print('(');
		} else {
			print(" (");
		}
		if (x.getArguments() != null) {
			printCommaList(x.getArguments());
		}
		print(')');
		if (x.getStatementContainer() != null) {
			print(';');
		}
		printFooter(x);
	}

}
