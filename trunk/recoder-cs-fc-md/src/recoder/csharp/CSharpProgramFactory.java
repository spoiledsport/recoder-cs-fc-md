// This file is part of the RECODER library and protected by the LGPL

package recoder.csharp;

import recoder.*;
import recoder.io.*;
import recoder.convenience.TreeWalker;
import recoder.list.*;
import recoder.csharp.attributes.*;
import recoder.csharp.attributes.targets.*;
import recoder.csharp.declaration.*;
import recoder.csharp.declaration.modifier.*;
import recoder.csharp.declaration.modifier.Override;
import recoder.csharp.reference.*;
import recoder.csharp.expression.*;
import recoder.csharp.expression.literal.*;
import recoder.csharp.expression.operator.*;
import recoder.csharp.statement.*;
import recoder.parser.*;
import recoder.util.*;

import recoder.csharp.SourceElement.Position;

import java.io.*;
import java.beans.*;

public class CSharpProgramFactory implements ProgramFactory, PropertyChangeListener {

	/**
	 Protected constructor - use {@link #getInstance} instead.
	 */
	protected CSharpProgramFactory() {}

	/** 
	 The singleton instance of the program factory.
	 */
	private static CSharpProgramFactory theFactory = new CSharpProgramFactory();

	/** 
	The singleton instance of the program factory.
	 */
	private static ServiceConfiguration serviceConfiguration;

	/**
	   StringWriter for toSource.
	 */
	private static StringWriter writer = new StringWriter();

	/**
	   PrettyPrinter, for toSource.
	 */
	private static PrettyPrinter sourcePrinter;

	/**
	   Called by the service configuration indicating that all services
	   are known. Services may now start communicating or linking among
	   their configuration partners. The service configuration can be
	   memorized if it has not been passed in by a constructor already.
	   @param cfg the service configuration this services has been assigned to.
	 */
	public void initialize(ServiceConfiguration cfg) {
		if (serviceConfiguration == null) {
			serviceConfiguration = cfg;
		} else {
			Debug.asserta(
				serviceConfiguration != cfg,
				"A JavaProgramFactory is a singleton and may be part of one and only one service configuration. We appologize for the inconveniences.");
		}
		ProjectSettings settings = serviceConfiguration.getProjectSettings();
		settings.addPropertyChangeListener(this);
		writer = new StringWriter();
		sourcePrinter = new PrettyPrinter(writer, settings.getProperties());

	}

	/**
	   Returns the service configuration this service is a part of.
	   @return the configuration of this service.
	 */
	public ServiceConfiguration getServiceConfiguration() {
		return serviceConfiguration;
	}

	/** 
	 Returns the single instance of this class.
	 */
	public static CSharpProgramFactory getInstance() {
		return theFactory;
	}

	/** 
	 For internal reuse and synchronization.
	 */
	private static CSharpParser parser = new CSharpParser(System.in);

	private final static Position ZERO_POSITION = new Position(0, 0);

	private static void attachComment(Comment c, ProgramElement pe) {
		ProgramElement dest = pe;
		if (!c.isPrefixed()) {
			NonTerminalProgramElement ppe = dest.getASTParent();
			int i = 0;
			if (ppe != null) {
				for (; ppe.getChildAt(i) != dest; i++);
			}
			if (i == 0) { // before syntactical parent
				c.setPrefixed(true);
			} else {
				dest = ppe.getChildAt(i - 1);
				while (dest instanceof NonTerminalProgramElement) {
					ppe = (NonTerminalProgramElement) dest;
					i = ppe.getChildCount();
					if (i == 0) {
						break;
					}
					dest = ppe.getChildAt(i - 1);
				}
			}
		}
		if (c instanceof SingleLineComment && c.isPrefixed()) {
			Position p = dest.getFirstElement().getRelativePosition();
			if (p.getLine() < 1) {
				p.setLine(1);
				dest.getFirstElement().setRelativePosition(p);
			}
		}
		CommentMutableList cml = dest.getComments();
		if (cml == null) {
			dest.setComments(cml = new CommentArrayList());
		}
		cml.add(c);
	}

	/**
	   Perform post work on the created element. Creates parent links
	   and assigns comments.
	 */
	private static void postWork(ProgramElement pe) {

		CommentMutableList comments = parser.getComments();
		int commentIndex = 0;
		int commentCount = comments.size();
		Position cpos = ZERO_POSITION;
		Comment current = null;
		if (commentIndex < commentCount) {
			current = comments.getComment(commentIndex);
			cpos = current.getFirstElement().getStartPosition();
		}
		TreeWalker tw = new TreeWalker(pe);
		while (tw.next()) {
			pe = tw.getProgramElement();
			if (pe instanceof NonTerminalProgramElement) {
				((NonTerminalProgramElement) pe).makeParentRoleValid();
			}
			if (pe.getFirstElement() == null) {
				System.out.println("K");
			}

			Position pos = pe.getFirstElement().getStartPosition();
			while ((commentIndex < commentCount) && pos.compareTo(cpos) > 0) {
				attachComment(current, pe);
				commentIndex += 1;
				if (commentIndex < commentCount) {
					current = comments.getComment(commentIndex);
					cpos = current.getFirstElement().getStartPosition();
				}
			}
		}
		if (commentIndex < commentCount) {
			while (pe.getASTParent() != null) {
				pe = pe.getASTParent();
			}
			CommentMutableList cml = pe.getComments();
			if (cml == null) {
				pe.setComments(cml = new CommentArrayList());
			}
			do {
				current = comments.getComment(commentIndex);
				current.setPrefixed(false);
				cml.add(current);
				commentIndex += 1;
			} while (commentIndex < commentCount);
		}
	}

	/**
	 Parse a {@link CompilationUnit} from the given reader.
	 */
	public CompilationUnit parseCompilationUnit(Reader in)
		throws IOException, ParserException {
		synchronized (parser) {
			parser.initialize(in);
			CompilationUnit res = parser.CompilationUnit();
			postWork(res);
			return res;
		}
	}

	/**
	 Parse a {@link TypeDeclaration} from the given reader.
	 */
	public TypeDeclaration parseTypeDeclaration(Reader in)
		throws IOException, ParserException {
		synchronized (parser) {
			parser.initialize(in);
			TypeDeclaration res = parser.TypeDeclaration();
			postWork(res);
			return res;
		}
	}

	/**
	 Parse a {@link FieldDeclaration} from the given reader.
	 */
	public FieldDeclaration parseFieldDeclaration(Reader in)
		throws IOException, ParserException {
		synchronized (parser) {
			parser.initialize(in);
			FieldDeclaration res = parser.FieldDeclaration();
			postWork(res);
			return res;
		}
	}

	/**
	 Parse a {@link MethodDeclaration} from the given reader.
	 */
	public MethodDeclaration parseMethodDeclaration(Reader in)
		throws IOException, ParserException {
		synchronized (parser) {
			parser.initialize(in);
			MethodDeclaration res = parser.MethodDeclaration();
			postWork(res);
			return res;
		}
	}

	/**
	 Parse a {@link MemberDeclaration} from the given reader.
	 */
	public MemberDeclaration parseMemberDeclaration(Reader in)
		throws IOException, ParserException {
		synchronized (parser) {
			parser.initialize(in);
			MemberDeclaration res = parser.ClassBodyDeclaration();
			postWork(res);
			return res;
		}
	}

	/**
	 Parse a {@link ParameterDeclaration} from the given reader.
	 */
	public ParameterDeclaration parseParameterDeclaration(Reader in)
		throws IOException, ParserException {
		synchronized (parser) {
			parser.initialize(in);
			ParameterDeclaration res = parser.FormalParameter();
			postWork(res);
			return res;
		}
	}

	/**
	 Parse a {@link ConstructorDeclaration} from the given reader.
	 */
	public ConstructorDeclaration parseConstructorDeclaration(Reader in)
		throws IOException, ParserException {
		synchronized (parser) {
			parser.initialize(in);
			ConstructorDeclaration res = parser.ConstructorDeclaration();
			postWork(res);
			return res;
		}
	}

	/**
	 Parse a {@link DestructorDeclaration} from the given reader.
	 */
	public DestructorDeclaration parseDestructorDeclaration(Reader in)
		throws IOException, ParserException {
		synchronized (parser) {
			parser.initialize(in);
			DestructorDeclaration res = parser.DestructorDeclaration();
			postWork(res);
			return res;
		}
	}

	/**
	 Parse a {@link TypeReference} from the given reader.
	 */
	public TypeReference parseTypeReference(Reader in)
		throws IOException, ParserException {
		synchronized (parser) {
			parser.initialize(in);
			TypeReference res = parser.ResultType();
			postWork(res);
			return res;
		}
	}

	/**
	 Parse an {@link Expression} from the given reader.
	 */
	public Expression parseExpression(Reader in) throws IOException, ParserException {
		synchronized (parser) {
			parser.initialize(in);
			Expression res = parser.Expression();
			postWork(res);
			return res;
		}
	}

	/**
	 Parse some {@link Statement}s from the given reader.
	 */
	public StatementMutableList parseStatements(Reader in)
		throws IOException, ParserException {
		synchronized (parser) {
			parser.initialize(in);
			StatementMutableList res = parser.GeneralizedStatements();
			for (int i = 0; i < res.size(); i += 1) {
				postWork(res.getStatement(i));
			}
			return res;
		}
	}

	/**
	 * Parse a {@link StatementBlock} from the given string.
	 */
	public StatementBlock parseStatementBlock(Reader in)
		throws IOException, ParserException {
		synchronized (parser) {
			parser.initialize(in);
			StatementBlock res = parser.Block();
			postWork(res);
			return res;
		}
	}

	/**
	 * Parse a {@link CompilationUnit} from the given string.
	 */
	public CompilationUnit parseCompilationUnit(String in) throws ParserException {
		try {
			return parseCompilationUnit(new StringReader(in));
		} catch (IOException ioe) {
			throw new ParserException(("" + ioe));
		}
	}

	/**
	 * Parse {@link CompilationUnit}s from the given string.
	 */
	public CompilationUnitMutableList parseCompilationUnits(String[] ins)
		throws ParserException {
		try {
			CompilationUnitMutableList cus = new CompilationUnitArrayList();
			for (int i = 0; i < ins.length; i++) {
				CompilationUnit cu = parseCompilationUnit(new FileReader(ins[i]));
				cus.add(cu);
			}
			return cus;
		} catch (IOException ioe) {
			throw new ParserException(("" + ioe));
		}
	}

	/**
	 * Parse a {@link TypeDeclaration} from the given string.
	 */
	public TypeDeclaration parseTypeDeclaration(String in) throws ParserException {
		try {
			return parseTypeDeclaration(new StringReader(in));
		} catch (IOException ioe) {
			throw new ParserException(("" + ioe));
		}
	}

	/**
	 * Parse a {@link MemberDeclaration} from the given string.
	 */
	public MemberDeclaration parseMemberDeclaration(String in) throws ParserException {
		try {
			return parseMemberDeclaration(new StringReader(in));
		} catch (IOException ioe) {
			throw new ParserException(("" + ioe));
		}
	}

	/**
	 * Parse a {@link FieldDeclaration} from the given string.
	 */
	public FieldDeclaration parseFieldDeclaration(String in) throws ParserException {
		try {
			return parseFieldDeclaration(new StringReader(in));
		} catch (IOException ioe) {
			throw new ParserException(("" + ioe));
		}
	}

	/**
	 * Parse a {@link MethodDeclaration} from the given string.
	 */
	public MethodDeclaration parseMethodDeclaration(String in) throws ParserException {
		try {
			return parseMethodDeclaration(new StringReader(in));
		} catch (IOException ioe) {
			throw new ParserException(("" + ioe));
		}
	}

	/**
	 * Parse a {@link ParameterDeclaration} from the given string.
	 */
	public ParameterDeclaration parseParameterDeclaration(String in)
		throws ParserException {
		try {
			return parseParameterDeclaration(new StringReader(in));
		} catch (IOException ioe) {
			throw new ParserException(("" + ioe));
		}
	}

	/**
	 * Parse a {@link ConstructorDeclaration} from the given string.
	 */
	public ConstructorDeclaration parseConstructorDeclaration(String in)
		throws ParserException {
		try {
			return parseConstructorDeclaration(new StringReader(in));
		} catch (IOException ioe) {
			throw new ParserException(("" + ioe));
		}
	}

	/**
	 * Parse a {@link DestructorDeclaration} from the given string.
	 */
	public DestructorDeclaration parseDestructorDeclaration(String in)
		throws ParserException {
		try {
			return parseDestructorDeclaration(new StringReader(in));
		} catch (IOException ioe) {
			throw new ParserException(("" + ioe));
		}
	}

	/**
	 * Parse a {@link TypeReference} from the given string.
	 */
	public TypeReference parseTypeReference(String in) throws ParserException {
		try {
			return parseTypeReference(new StringReader(in));
		} catch (IOException ioe) {
			throw new ParserException(("" + ioe));
		}
	}

	/**
	 * Parse an {@link Expression} from the given string.
	 */
	public Expression parseExpression(String in) throws ParserException {
		try {
			return parseExpression(new StringReader(in));
		} catch (IOException ioe) {
			throw new ParserException(("" + ioe));
		}
	}

	/**
	 * Parse a list of {@link Statement}s from the given string.
	 */
	public StatementMutableList parseStatements(String in) throws ParserException {
		try {
			return parseStatements(new StringReader(in));
		} catch (IOException ioe) {
			throw new ParserException(("" + ioe));
		}
	}

	/** 
	Replacement for Integer.parseInt allowing "supercharged"
	non-decimal constants.
	In contrast to Integer.parseInt, works for 0x80000000 and higher
	octal and hex constants as well as -MIN_VALUE which is allowed
	in case that the minus sign has been interpreted as an unary minus.
	The method will return Integer.MIN_VALUE in that case; this is
	fine as -MIN_VALUE == MIN_VALUE.
	
	
	C# addition: Unsinged integers of C# will also be reported as 
	singned types. To get the <b>real</b> value, you must add MIN_VALUE to the 
	given signed value.
	*/
	public static int parseInt(String nm) throws NumberFormatException {
		int radix;
		boolean negative = false;
		int result;

		int index = 0;
		if (nm.startsWith("-")) {
			negative = true;
			index++;
		}
		if (nm.startsWith("0x", index) || nm.startsWith("0X", index)) {
			index += 2;
			radix = 16;
		} else if (nm.startsWith("0", index) && nm.length() > 1 + index) {
			index++;
			radix = 8;
		} else {
			radix = 10;
		}
		if (nm.startsWith("-", index))
			throw new NumberFormatException("Negative sign in wrong position");
		int len = nm.length() - index;
		
		if (nm.endsWith("u") || nm.endsWith("U")) len--; // Cut ending u/U
														 // This feature is new in C#.
		
		if (radix == 16 && len == 8) {
			char first = nm.charAt(index);
			index += 1;
			result = Integer.valueOf(nm.substring(index), radix).intValue();
			result |= Character.digit(first, 16) << 28;
			return negative ? -result : result;
		}
		if (radix == 8 && len == 11) {
			char first = nm.charAt(index);
			index += 1;
			result = Integer.valueOf(nm.substring(index), radix).intValue();
			result |= Character.digit(first, 8) << 30;
			return negative ? -result : result;
		}
		if (!negative
			&& radix == 10
			&& len == 10
			&& nm.indexOf("2147483648", index) == index) {
			return Integer.MIN_VALUE;
		}
		result = Integer.valueOf(nm.substring(index), radix).intValue();
		return negative ? -result : result;
	}

	/** 
	Replacement for Long.parseLong which is not available in JDK 1.1
	and does not handle 'l' or 'L' suffices in JDK 1.2.
	
	C# addition: 
	This method interprets unsigned constants as if they were signed ones. 
	That means, that values larger than MAXLONG / 2 are interpreted as negative 
	numbers. To get their real value, you must add Long.MIN_VALUE
	*/
	public static long parseLong(String nm) throws NumberFormatException {
		int radix;
		boolean negative = false;
		long result;

		int index = 0;
		if (nm.startsWith("-")) {
			negative = true;
			index++;
		}
		if (nm.startsWith("0x", index) || nm.startsWith("0X", index)) {
			index += 2;
			radix = 16;
		} else if (nm.startsWith("0", index) && nm.length() > 1 + index) {
			index++;
			radix = 8;
		} else {
			radix = 10;
		}

		if (nm.startsWith("-", index))
			throw new NumberFormatException("Negative sign in wrong position");
		int endIndex = nm.length();

		if (nm.endsWith("uL")
			|| nm.endsWith("ul")
			|| nm.endsWith("UL")
			|| nm.endsWith("Ul")
			|| nm.endsWith("Lu")
			|| nm.endsWith("lu")
			|| nm.endsWith("LU")
			|| nm.endsWith("lU")) {
			endIndex -= 2;
		} else if (nm.endsWith("L") || nm.endsWith("l")) {
			endIndex -= 1;
		}

		int len = endIndex - index;
		if (radix == 16 && len == 16) {
			char first = nm.charAt(index);
			index += 1;
			result = Long.valueOf(nm.substring(index, endIndex), radix).longValue();
			result |= (long) Character.digit(first, 16) << 60;
			return negative ? -result : result;
		}
		if (radix == 8 && len == 21) {
			char first = nm.charAt(index);
			index += 1;
			result = Long.valueOf(nm.substring(index, endIndex), radix).longValue();
			result |= Character.digit(first, 8) << 63;
			return negative ? -result : result;
		}
		if (!negative
			&& radix == 10
			&& len == 19
			&& nm.indexOf("9223372036854775808", index) == index) {
			return Long.MIN_VALUE;
		}
		result = Long.valueOf(nm.substring(index, endIndex), radix).longValue();
		return negative ? -result : result;
	}

	/**
	   Creates a syntactical representation of the source element.
	 */
	String toSource(CSharpSourceElement jse) {
		synchronized (writer) {
			sourcePrinter.setIndentationLevel(0);
			jse.accept(sourcePrinter);
			StringBuffer buf = writer.getBuffer();
			String str = buf.toString();
			buf.setLength(0);
			return str;
		}
	}

	/**
	 * Returns a new suitable {@link recoder.csharp.PrettyPrinter} obeying
	 * the current project settings for the specified writer,
	 * @param out the (initial) writer to print to.
	 * @return a new pretty printer.
	 */
	public PrettyPrinter getPrettyPrinter(Writer out) {
		return new PrettyPrinter(
			out,
			serviceConfiguration.getProjectSettings().getProperties());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		sourcePrinter =
			new PrettyPrinter(
				writer,
				serviceConfiguration.getProjectSettings().getProperties());
		String changedProp = evt.getPropertyName();
	}

	/**
	 * Demo program reading a single source file and writing it back to
	 * stdout using the default {@link PrettyPrinter}.
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Requires a java source file as argument");
			System.exit(1);
		}
		try {
			CompilationUnit cu =
				new DefaultServiceConfiguration()
					.getProgramFactory()
					.parseCompilationUnit(
					new FileReader(args[0]));
			System.out.println(cu.toSource());
		} catch (IOException ioe) {
			System.err.println(ioe);
			ioe.printStackTrace();
		} catch (ParserException pe) {
			System.err.println(pe);
			pe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 Creates a new {@link Comment}.
	 @return a new instance of Comment.
	 */
	public Comment createComment() {
		return new Comment();
	}

	/**
	 Creates a new {@link Comment}.
	 @return a new instance of Comment.
	 */
	public Comment createComment(String text) {
		return new Comment(text);
	}

	/**
	 Creates a new {@link Comment}.
	 @return a new instance of Comment.
	 */
	public Comment createComment(String text, boolean prefixed) {
		return new Comment(text, prefixed);
	}

	/**
	 Creates a new {@link CompilationUnit}.
	 @return a new instance of CompilationUnit.
	 */
	public CompilationUnit createCompilationUnit() {
		return new CompilationUnit();
	}

	/**
	 Creates a new {@link CompilationUnit}.
	 @return a new instance of CompilationUnit.
	 */
	public CompilationUnit createCompilationUnit(
		NamespaceSpecificationMutableList pkg,
		UsingMutableList imports,
		TypeDeclarationMutableList typeDeclarations) {
		return new CompilationUnit(pkg, imports, typeDeclarations);
	}

	/**
	 Creates a new {@link XmlDocComment}.
	 @return a new instance of XmlDocComment.
	 */
	public XmlDocComment createXmlDocComment() {
		return new XmlDocComment();
	}

	/**
	 Creates a new {@link XmlDocComment}.
	 @return a new instance of XmlDocComment.
	 */
	public XmlDocComment createXmlDocComment(String text) {
		return new XmlDocComment(text);
	}

	/**
	 Creates a new {@link Identifier}.
	 @return a new instance of Identifier.
	 */
	public Identifier createIdentifier() {
		return new Identifier();
	}

	/**
	 Creates a new {@link Identifier}.
	 @return a new instance of Identifier.
	 */
	public Identifier createIdentifier(String text) {
		return new Identifier(text);
	}

	/**
	 Creates a new {@link Using}.
	 @return a new instance of Using.
	 */
	public Using createUsing() {
		return new Using();
	}

	/**
	 Creates a new {@link Using}.
	 @return a new instance of Using.
	 */
	public Using createUsing(TypeReference t, boolean multi) {
		return new Using(t, multi);
	}

	/**
	 Creates a new {@link Using}.
	 @return a new instance of Using.
	 */
	public Using createUsing(NamespaceReference t) {
		return new Using(t);
	}

	/**
	 Creates a new {@link NamespaceSpecification}.
	 @return a new instance of NamespaceSpecification.
	 */
	public NamespaceSpecification createNamespaceSpecification() {
		return new NamespaceSpecification();
	}

	/**
	 Creates a new {@link NamespaceSpecification}.
	 @return a new instance of NamespaceSpecification.
	 */
	public NamespaceSpecification createNamespaceSpecification(NamespaceReference pkg) {
		return new NamespaceSpecification(pkg);
	}

	/**
	 Creates a new {@link SingleLineComment}.
	 @return a new instance of SingleLineComment.
	 */
	public SingleLineComment createSingleLineComment() {
		return new SingleLineComment();
	}

	/**
	 Creates a new {@link SingleLineComment}.
	 @return a new instance of SingleLineComment.
	 */
	public SingleLineComment createSingleLineComment(String text) {
		return new SingleLineComment(text);
	}

	/**
	 Creates a new {@link TypeReference}.
	 @return a new instance of TypeReference.
	 */
	public TypeReference createTypeReference() {
		return new TypeReference();
	}

	/**
	 Creates a new {@link TypeReference}.
	 @return a new instance of TypeReference.
	 */
	public TypeReference createTypeReference(Identifier name) {
		return new TypeReference(name);
	}

	/**
	 Creates a new {@link TypeReference}.
	 @return a new instance of TypeReference.
	 */
	public TypeReference createTypeReference(ReferencePrefix prefix, Identifier name) {
		return new TypeReference(prefix, name);
	}

	/**
	 Creates a new {@link TypeReference}.
	 @return a new instance of TypeReference.
	 */
	public TypeReference createTypeReference(Identifier name, int[] dim) {
		return new TypeReference(name, dim);
	}

	/**
	 Creates a new {@link NamespaceReference}.
	 @return a new instance of NamespaceReference.
	 */
	public NamespaceReference createNamespaceReference() {
		return new NamespaceReference();
	}

	/**
	 Creates a new {@link NamespaceReference}.
	 @return a new instance of NamespaceReference.
	 */
	public NamespaceReference createNamespaceReference(Identifier id) {
		return new NamespaceReference(id);
	}

	/**
	 Creates a new {@link NamespaceReference}.
	 @return a new instance of NamespaceReference.
	 */
	public NamespaceReference createNamespaceReference(
		NamespaceReference path,
		Identifier id) {
		return new NamespaceReference(path, id);
	}

	/**
	 Creates a new {@link UncollatedReferenceQualifier}.
	 @return a new instance of UncollatedReferenceQualifier.
	 */
	public UncollatedReferenceQualifier createUncollatedReferenceQualifier() {
		return new UncollatedReferenceQualifier();
	}

	/**
	 Creates a new {@link UncollatedReferenceQualifier}.
	 @return a new instance of UncollatedReferenceQualifier.
	 */
	public UncollatedReferenceQualifier createUncollatedReferenceQualifier(Identifier id) {
		return new UncollatedReferenceQualifier(id);
	}

	/**
	 Creates a new {@link UncollatedReferenceQualifier}.
	 @return a new instance of UncollatedReferenceQualifier.
	 */
	public UncollatedReferenceQualifier createUncollatedReferenceQualifier(
		ReferencePrefix prefix,
		Identifier id) {
		return new UncollatedReferenceQualifier(prefix, id);
	}

	/**
	 Creates a new {@link ClassDeclaration}.
	 @return a new instance of ClassDeclaration.
	 */
	public ClassDeclaration createClassDeclaration() {
		return new ClassDeclaration();
	}

	/**
	 Creates a new {@link ClassDeclaration}.
	 @return a new instance of ClassDeclaration.
	 */
	public ClassDeclaration createClassDeclaration(
		ModifierMutableList modifiers,
		Identifier name,
		Extends extended,
		Implements implemented,
		MemberDeclarationMutableList members) {
		return new ClassDeclaration(modifiers, name, extended, implemented, members);
	}

	/**
	 Creates a new {@link ClassDeclaration}.
	 @return a new instance of ClassDeclaration.
	 */
	public ClassDeclaration createClassDeclaration(MemberDeclarationMutableList members) {
		return new ClassDeclaration(members);
	}

	/**
	 Creates a new {@link StructDeclaration}.
	 @return a new instance of StructDeclaration.
	 */
	public StructDeclaration createStructDeclaration() {
		return new StructDeclaration();
	}

	/**
	 Creates a new {@link StructDeclaration}.
	 @return a new instance of StructDeclaration.
	 */
	public StructDeclaration createStructDeclaration(
		ModifierMutableList modifiers,
		Identifier name,
		Extends extended,
		Implements implemented,
		MemberDeclarationMutableList members) {
		return new StructDeclaration(modifiers, name, extended, implemented, members);
	}

	/**
	 Creates a new {@link StructDeclaration}.
	 @return a new instance of StructDeclaration.
	 */
	public StructDeclaration createStructDeclaration(MemberDeclarationMutableList members) {
		return new StructDeclaration(members);
	}

	/**
	 Creates a new {@link ConstructorDeclaration}.
	 @return a new instance of ConstructorDeclaration.
	 */
	public ConstructorDeclaration createConstructorDeclaration() {
		return new ConstructorDeclaration();
	}

	/**
	 Creates a new {@link ConstructorDeclaration}.
	 @return a new instance of ConstructorDeclaration.
	 */
	public ConstructorDeclaration createConstructorDeclaration(
		VisibilityModifier modifier,
		Identifier name,
		ParameterDeclarationMutableList parameters,
		Throws exceptions,
		StatementBlock body) {
		return new ConstructorDeclaration(modifier, name, parameters, exceptions, body);
	}

	/**
	 Creates a new {@link StaticConstructorDeclaration}.
	 @return a new instance of StaticConstructorDeclaration.
	 */
	public StaticConstructorDeclaration createStaticConstructorDeclaration() {
		return new StaticConstructorDeclaration();
	}

	/**
	 Creates a new {@link StaticConstructorDeclaration}.
	 @return a new instance of StaticConstructorDeclaration.
	 */
	public StaticConstructorDeclaration createStaticConstructorDeclaration(
		VisibilityModifier modifier,
		Identifier name,
		ParameterDeclarationMutableList parameters,
		Throws exceptions,
		StatementBlock body) {
		return new StaticConstructorDeclaration(
			modifier,
			name,
			parameters,
			exceptions,
			body);
	}

	/**
	 Creates a new {@link DestructorDeclaration}.
	 @return a new instance of DestructorDeclaration.
	 */
	public DestructorDeclaration createDestructorDeclaration() {
		return new DestructorDeclaration();
	}

	/**
	 Creates a new {@link DestructorDeclaration}.
	 @return a new instance of DestructorDeclaration.
	 */
	public DestructorDeclaration createDestructorDeclaration(
		VisibilityModifier modifier,
		Identifier name,
		ParameterDeclarationMutableList parameters,
		Throws exceptions,
		StatementBlock body) {
		return new DestructorDeclaration(modifier, name, parameters, exceptions, body);
	}

	/**
	 Creates a new {@link Throws}.
	 @return a new instance of Throws.
	 */
	public Throws createThrows() {
		return new Throws();
	}

	/**
	 Creates a new {@link Throws}.
	 @return a new instance of Throws.
	 */
	public Throws createThrows(TypeReference exception) {
		return new Throws(exception);
	}

	/**
	 Creates a new {@link Throws}.
	 @return a new instance of Throws.
	 */
	public Throws createThrows(TypeReferenceMutableList list) {
		return new Throws(list);
	}

	/**
	 Creates a new {@link FieldDeclaration}.
	 @return a new instance of FieldDeclaration.
	 */
	public FieldDeclaration createFieldDeclaration() {
		return new FieldDeclaration();
	}

	/**
	 Creates a new {@link FieldDeclaration}.
	 @return a new instance of FieldDeclaration.
	 */
	public FieldDeclaration createFieldDeclaration(
		TypeReference typeRef,
		Identifier name) {
		return new FieldDeclaration(typeRef, name);
	}

	/**
	 Creates a new {@link FieldDeclaration}.
	 @return a new instance of FieldDeclaration.
	 */
	public FieldDeclaration createFieldDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		Identifier name,
		Expression init) {
		return new FieldDeclaration(mods, typeRef, name, init);
	}

	/**
	 Creates a new {@link FieldDeclaration}.
	 @return a new instance of FieldDeclaration.
	 */
	public FieldDeclaration createFieldDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		FieldSpecificationMutableList vars) {
		return new FieldDeclaration(mods, typeRef, vars);
	}

	/**
	 Creates a new {@link ConstantFieldDeclaration}.
	 @return a new instance of ConstantFieldDeclaration.
	 */
	public ConstantFieldDeclaration createConstantFieldDeclaration() {
		return new ConstantFieldDeclaration();
	}

	/**
	 Creates a new {@link ConstantFieldDeclaration}.
	 @return a new instance of ConstantFieldDeclaration.
	 */
	public ConstantFieldDeclaration createConstantFieldDeclaration(
		TypeReference typeRef,
		Identifier name) {
		return new ConstantFieldDeclaration(typeRef, name);
	}

	/**
	 Creates a new {@link ConstantFieldDeclaration}.
	 @return a new instance of ConstantFieldDeclaration.
	 */
	public ConstantFieldDeclaration createConstantFieldDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		Identifier name,
		Expression init) {
		return new ConstantFieldDeclaration(mods, typeRef, name, init);
	}

	/**
	 Creates a new {@link ConstantFieldDeclaration}.
	 @return a new instance of ConstantFieldDeclaration.
	 */
	public ConstantFieldDeclaration createConstantFieldDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		FieldSpecificationMutableList vars) {
		return new ConstantFieldDeclaration(mods, typeRef, vars);
	}

	/**
	 Creates a new {@link EnumDeclaration}.
	 @return a new instance of EnumDeclaration.
	 */
	public EnumDeclaration createEnumDeclaration() {
		return new EnumDeclaration();
	}

	/**
	 Creates a new {@link EnumMemberDeclaration}.
	 @return a new instance of EnumMemberDeclaration.
	 */
	public EnumMemberDeclaration createEnumMemberDeclaration(Identifier id) {
		return new EnumMemberDeclaration(id);
	}

	/**
	 Creates a new {@link PropertyDeclaration}.
	 @return a new instance of PropertyDeclaration.
	 */
	public PropertyDeclaration createPropertyDeclaration() {
		return new PropertyDeclaration();
	}

	/**
	 Creates a new {@link Extends}.
	 @return a new instance of Extends.
	 */
	public Extends createExtends() {
		return new Extends();
	}

	/**
	 Creates a new {@link Extends}.
	 @return a new instance of Extends.
	 */
	public Extends createExtends(TypeReference supertype) {
		return new Extends(supertype);
	}

	/**
	 Creates a new {@link Extends}.
	 @return a new instance of Extends.
	 */
	public Extends createExtends(TypeReferenceMutableList list) {
		return new Extends(list);
	}

	/**
	 Creates a new {@link Implements}.
	 @return a new instance of Implements.
	 */
	public Implements createImplements() {
		return new Implements();
	}

	/**
	 Creates a new {@link Implements}.
	 @return a new instance of Implements.
	 */
	public Implements createImplements(TypeReference supertype) {
		return new Implements(supertype);
	}

	/**
	 Creates a new {@link Implements}.
	 @return a new instance of Implements.
	 */
	public Implements createImplements(TypeReferenceMutableList list) {
		return new Implements(list);
	}

	/**
	 Creates a new {@link InterfaceDeclaration}.
	 @return a new instance of InterfaceDeclaration.
	 */
	public InterfaceDeclaration createInterfaceDeclaration() {
		return new InterfaceDeclaration();
	}

	/**
	 Creates a new {@link InterfaceDeclaration}.
	 @return a new instance of InterfaceDeclaration.
	 */
	public InterfaceDeclaration createInterfaceDeclaration(
		ModifierMutableList modifiers,
		Identifier name,
		Extends extended,
		MemberDeclarationMutableList members) {
		return new InterfaceDeclaration(modifiers, name, extended, members);
	}

	/**
	 Creates a new {@link LocalVariableDeclaration}.
	 @return a new instance of LocalVariableDeclaration.
	 */
	public LocalVariableDeclaration createLocalVariableDeclaration() {
		return new LocalVariableDeclaration();
	}

	/**
	 Creates a new {@link LocalVariableDeclaration}.
	 @return a new instance of LocalVariableDeclaration.
	 */
	public LocalVariableDeclaration createLocalVariableDeclaration(
		TypeReference typeRef,
		Identifier name) {
		return new LocalVariableDeclaration(typeRef, name);
	}

	/**
	 Creates a new {@link LocalVariableDeclaration}.
	 @return a new instance of LocalVariableDeclaration.
	 */
	public LocalVariableDeclaration createLocalVariableDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		VariableSpecificationMutableList vars) {
		return new LocalVariableDeclaration(mods, typeRef, vars);
	}

	/**
	 Creates a new {@link LocalVariableDeclaration}.
	 @return a new instance of LocalVariableDeclaration.
	 */
	public LocalVariableDeclaration createLocalVariableDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		Identifier name,
		Expression init) {
		return new LocalVariableDeclaration(mods, typeRef, name, init);
	}

	/**
	 Creates a new {@link LocalConstantDeclaration}.
	 @return a new instance of LocalConstantDeclaration.
	 */
	public LocalConstantDeclaration createLocalConstantDeclaration() {
		return new LocalConstantDeclaration();
	}

	/**
	 Creates a new {@link LocalConstantDeclaration}.
	 @return a new instance of LocalConstantDeclaration.
	 */
	public LocalConstantDeclaration createLocalConstantDeclaration(
		TypeReference typeRef,
		Identifier name) {
		return new LocalConstantDeclaration(typeRef, name);
	}

	/**
	 Creates a new {@link LocalConstantDeclaration}.
	 @return a new instance of LocalConstantDeclaration.
	 */
	public LocalConstantDeclaration createLocalConstantDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		VariableSpecificationMutableList vars) {
		return new LocalConstantDeclaration(mods, typeRef, vars);
	}

	/**
	 Creates a new {@link LocalConstantDeclaration}.
	 @return a new instance of LocalConstantDeclaration.
	 */
	public LocalConstantDeclaration createLocalConstantDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		Identifier name,
		Expression init) {
		return new LocalConstantDeclaration(mods, typeRef, name, init);
	}

	/**
	 Creates a new {@link OperatorDeclaration}.
	 @return a new instance of OperatorDeclaration.
	 */
	public OperatorDeclaration createOperatorDeclaration() {
		return new OperatorDeclaration();
	}

	/**
	 Creates a new {@link DelegateDeclaration}.
	 @return a new instance of DelegateDeclaration.
	 */
	public DelegateDeclaration createDelegateDeclaration() {
		return new DelegateDeclaration();
	}

	/**
	 Creates a new {@link MethodDeclaration}.
	 @return a new instance of MethodDeclaration.
	 */
	public MethodDeclaration createMethodDeclaration() {
		return new MethodDeclaration();
	}

	/**
	 Creates a new {@link MethodDeclaration}.
	 @return a new instance of MethodDeclaration.
	 */
	public MethodDeclaration createMethodDeclaration(
		ModifierMutableList modifiers,
		TypeReference returnType,
		Identifier name,
		ParameterDeclarationMutableList parameters,
		Throws exceptions) {
		return new MethodDeclaration(modifiers, returnType, name, parameters, exceptions);
	}

	/**
	 Creates a new {@link MethodDeclaration}.
	 @return a new instance of MethodDeclaration.
	 */
	public MethodDeclaration createMethodDeclaration(
		ModifierMutableList modifiers,
		TypeReference returnType,
		Identifier name,
		ParameterDeclarationMutableList parameters,
		Throws exceptions,
		StatementBlock body) {
		return new MethodDeclaration(
			modifiers,
			returnType,
			name,
			parameters,
			exceptions,
			body);
	}

	/**
	 Creates a new {@link ParameterDeclaration}.
	 @return a new instance of ParameterDeclaration.
	 */
	public ParameterDeclaration createParameterDeclaration() {
		return new ParameterDeclaration();
	}

	/**
	 Creates a new {@link ParameterDeclaration}.
	 @return a new instance of ParameterDeclaration.
	 */
	public ParameterDeclaration createParameterDeclaration(
		TypeReference typeRef,
		Identifier name) {
		return new ParameterDeclaration(typeRef, name);
	}

	/**
	 Creates a new {@link ParameterDeclaration}.
	 @return a new instance of ParameterDeclaration.
	 */
	public ParameterDeclaration createParameterDeclaration(
		ModifierMutableList mods,
		TypeReference typeRef,
		Identifier name) {
		return new ParameterDeclaration(mods, typeRef, name);
	}

	/**
	 Creates a new {@link VariableSpecification}.
	 @return a new instance of VariableSpecification.
	 */
	public VariableSpecification createVariableSpecification() {
		return new VariableSpecification();
	}

	/**
	 Creates a new {@link VariableSpecification}.
	 @return a new instance of VariableSpecification.
	 */
	public VariableSpecification createVariableSpecification(Identifier name) {
		return new VariableSpecification(name);
	}

	/**
	 Creates a new {@link VariableSpecification}.
	 @return a new instance of VariableSpecification.
	 */
	public VariableSpecification createVariableSpecification(
		Identifier name,
		Expression init) {
		return new VariableSpecification(name, init);
	}

	/**
	 Creates a new {@link ConstantSpecification}.
	 @return a new instance of ConstantSpecification.
	 */
	public ConstantSpecification createConstantSpecification() {
		return new ConstantSpecification();
	}

	/**
	 Creates a new {@link ConstantSpecification}.
	 @return a new instance of ConstantSpecification.
	 */
	public ConstantSpecification createConstantSpecification(Identifier name) {
		return new ConstantSpecification(name);
	}

	/**
	 Creates a new {@link ConstantSpecification}.
	 @return a new instance of ConstantSpecification.
	 */
	public ConstantSpecification createConstantSpecification(
		Identifier name,
		Expression init) {
		return new ConstantSpecification(name, init);
	}

	/**
	 Creates a new {@link FieldSpecification}.
	 @return a new instance of FieldSpecification.
	 */
	public FieldSpecification createFieldSpecification() {
		return new FieldSpecification();
	}

	/**
	 Creates a new {@link FieldSpecification}.
	 @return a new instance of FieldSpecification.
	 */
	public FieldSpecification createFieldSpecification(Identifier name) {
		return new FieldSpecification(name);
	}

	/**
	 Creates a new {@link FieldSpecification}.
	 @return a new instance of FieldSpecification.
	 */
	public FieldSpecification createFieldSpecification(
		Identifier name,
		Expression init) {
		return new FieldSpecification(name, init);
	}

	/**
	 Creates a new {@link ConstantFieldSpecification}.
	 @return a new instance of ConstantFieldSpecification.
	 */
	public ConstantFieldSpecification createConstantFieldSpecification() {
		return new ConstantFieldSpecification();
	}

	/**
	 Creates a new {@link ConstantFieldSpecification}.
	 @return a new instance of ConstantFieldSpecification.
	 */
	public ConstantFieldSpecification createConstantFieldSpecification(Identifier name) {
		return new ConstantFieldSpecification(name);
	}

	/**
	 Creates a new {@link ConstantFieldSpecification}.
	 @return a new instance of ConstantFieldSpecification.
	 */
	public ConstantFieldSpecification createConstantFieldSpecification(
		Identifier name,
		Expression init) {
		return new ConstantFieldSpecification(name, init);
	}

	/**
	 Creates a new {@link ArrayInitializer}.
	 @return a new instance of ArrayInitializer.
	 */
	public ArrayInitializer createArrayInitializer() {
		return new ArrayInitializer();
	}

	/**
	 Creates a new {@link ArrayInitializer}.
	 @return a new instance of ArrayInitializer.
	 */
	public ArrayInitializer createArrayInitializer(ExpressionMutableList args) {
		return new ArrayInitializer(args);
	}

	/**
	 Creates a new {@link ParenthesizedExpression}.
	 @return a new instance of ParenthesizedExpression.
	 */
	public ParenthesizedExpression createParenthesizedExpression() {
		return new ParenthesizedExpression();
	}

	/**
	 Creates a new {@link ParenthesizedExpression}.
	 @return a new instance of ParenthesizedExpression.
	 */
	public ParenthesizedExpression createParenthesizedExpression(Expression child) {
		return new ParenthesizedExpression(child);
	}

	/**
	 Creates a new {@link BooleanLiteral}.
	 @return a new instance of BooleanLiteral.
	 */
	public BooleanLiteral createBooleanLiteral() {
		return new BooleanLiteral();
	}

	/**
	 Creates a new {@link BooleanLiteral}.
	 @return a new instance of BooleanLiteral.
	 */
	public BooleanLiteral createBooleanLiteral(boolean value) {
		return new BooleanLiteral(value);
	}

	/**
	 Creates a new {@link CharLiteral}.
	 @return a new instance of CharLiteral.
	 */
	public CharLiteral createCharLiteral() {
		return new CharLiteral();
	}

	/**
	 Creates a new {@link CharLiteral}.
	 @return a new instance of CharLiteral.
	 */
	public CharLiteral createCharLiteral(char value) {
		return new CharLiteral(value);
	}

	/**
	 Creates a new {@link CharLiteral}.
	 @return a new instance of CharLiteral.
	 */
	public CharLiteral createCharLiteral(String value) {
		return new CharLiteral(value);
	}

	/**
	 Creates a new {@link DoubleLiteral}.
	 @return a new instance of DoubleLiteral.
	 */
	public DoubleLiteral createDoubleLiteral() {
		return new DoubleLiteral();
	}

	/**
	 Creates a new {@link DoubleLiteral}.
	 @return a new instance of DoubleLiteral.
	 */
	public DoubleLiteral createDoubleLiteral(double value) {
		return new DoubleLiteral(value);
	}

	/**
	 Creates a new {@link DoubleLiteral}.
	 @return a new instance of DoubleLiteral.
	 */
	public DoubleLiteral createDoubleLiteral(String value) {
		return new DoubleLiteral(value);
	}

	/**
	 Creates a new {@link FloatLiteral}.
	 @return a new instance of FloatLiteral.
	 */
	public FloatLiteral createFloatLiteral() {
		return new FloatLiteral();
	}

	/**
	 Creates a new {@link FloatLiteral}.
	 @return a new instance of FloatLiteral.
	 */
	public FloatLiteral createFloatLiteral(float value) {
		return new FloatLiteral(value);
	}

	/**
	 Creates a new {@link FloatLiteral}.
	 @return a new instance of FloatLiteral.
	 */
	public FloatLiteral createFloatLiteral(String value) {
		return new FloatLiteral(value);
	}

	/**
	 Creates a new {@link IntLiteral}.
	 @return a new instance of IntLiteral.
	 */
	public IntLiteral createIntLiteral() {
		return new IntLiteral();
	}

	/**
	 Creates a new {@link IntLiteral}.
	 @return a new instance of IntLiteral.
	 */
	public IntLiteral createIntLiteral(int value) {
		return new IntLiteral(value);
	}

	/**
	 Creates a new {@link IntLiteral}.
	 @return a new instance of IntLiteral.
	 */
	public IntLiteral createIntLiteral(String value) {
		return new IntLiteral(value);
	}

	/**
	 Creates a new {@link LongLiteral}.
	 @return a new instance of LongLiteral.
	 */
	public LongLiteral createLongLiteral() {
		return new LongLiteral();
	}

	/**
	 Creates a new {@link LongLiteral}.
	 @return a new instance of LongLiteral.
	 */
	public LongLiteral createLongLiteral(long value) {
		return new LongLiteral(value);
	}

	/**
	 Creates a new {@link LongLiteral}.
	 @return a new instance of LongLiteral.
	 */
	public LongLiteral createLongLiteral(String value) {
		return new LongLiteral(value);
	}

	/**
	 Creates a new {@link NullLiteral}.
	 @return a new instance of NullLiteral.
	 */
	public NullLiteral createNullLiteral() {
		return new NullLiteral();
	}

	/**
	 Creates a new {@link StringLiteral}.
	 @return a new instance of StringLiteral.
	 */
	public StringLiteral createStringLiteral() {
		return new StringLiteral();
	}

	/**
	 Creates a new {@link StringLiteral}.
	 @return a new instance of StringLiteral.
	 */
	public StringLiteral createStringLiteral(String value) {
		return new StringLiteral(value);
	}

	/**
	 Creates a new {@link VerbatimStringLiteral}.
	 @return a new instance of VerbatimStringLiteral.
	 */
	public VerbatimStringLiteral createVerbatimStringLiteral() {
		return new VerbatimStringLiteral();
	}

	/**
	 Creates a new {@link VerbatimStringLiteral}.
	 @return a new instance of VerbatimStringLiteral.
	 */
	public VerbatimStringLiteral createVerbatimStringLiteral(String value) {
		return new VerbatimStringLiteral(value);
	}

	/**
	 Creates a new {@link PreDefinedTypeLiteral}.
	 @return a new instance of PreDefinedTypeLiteral.
	 */
	public PreDefinedTypeLiteral createPreDefinedTypeLiteral() {
		return new PreDefinedTypeLiteral();
	}

	/**
	 Creates a new {@link PreDefinedTypeLiteral}.
	 @return a new instance of PreDefinedTypeLiteral.
	 */
	public PreDefinedTypeLiteral createPreDefinedTypeLiteral(String value) {
		return new PreDefinedTypeLiteral(value);
	}

	/**
	 Creates a new {@link ArrayReference}.
	 @return a new instance of ArrayReference.
	 */
	public ArrayReference createArrayReference() {
		return new ArrayReference();
	}

	/**
	 Creates a new {@link ArrayReference}.
	 @return a new instance of ArrayReference.
	 */
	public ArrayReference createArrayReference(
		ReferencePrefix accessPath,
		ExpressionMutableList initializers) {
		return new ArrayReference(accessPath, initializers);
	}

	/**
	 Creates a new {@link FieldReference}.
	 @return a new instance of FieldReference.
	 */
	public FieldReference createFieldReference() {
		return new FieldReference();
	}

	/**
	 Creates a new {@link FieldReference}.
	 @return a new instance of FieldReference.
	 */
	public FieldReference createFieldReference(Identifier id) {
		return new FieldReference(id);
	}

	/**
	 Creates a new {@link FieldReference}.
	 @return a new instance of FieldReference.
	 */
	public FieldReference createFieldReference(ReferencePrefix prefix, Identifier id) {
		return new FieldReference(prefix, id);
	}

	/**
	 Creates a new {@link MetaClassReference}.
	 @return a new instance of MetaClassReference.
	 */
	public MetaClassReference createMetaClassReference() {
		return new MetaClassReference();
	}

	/**
	 Creates a new {@link MetaClassReference}.
	 @return a new instance of MetaClassReference.
	 */
	public MetaClassReference createMetaClassReference(TypeReference reference) {
		return new MetaClassReference(reference);
	}

	/**
	 Creates a new {@link MethodReference}.
	 @return a new instance of MethodReference.
	 */
	public MethodReference createMethodReference() {
		return new MethodReference();
	}

	/**
	 Creates a new {@link MethodReference}.
	 @return a new instance of MethodReference.
	 */
	public MethodReference createMethodReference(Identifier name) {
		return new MethodReference(name);
	}

	/**
	 Creates a new {@link MethodReference}.
	 @return a new instance of MethodReference.
	 */
	public MethodReference createMethodReference(
		ReferencePrefix accessPath,
		Identifier name) {
		return new MethodReference(accessPath, name);
	}

	/**
	 Creates a new {@link MethodReference}.
	 @return a new instance of MethodReference.
	 */
	public MethodReference createMethodReference(
		Identifier name,
		ExpressionMutableList args) {
		return new MethodReference(name, args);
	}

	/**
	 Creates a new {@link MethodReference}.
	 @return a new instance of MethodReference.
	 */
	public MethodReference createMethodReference(
		ReferencePrefix accessPath,
		Identifier name,
		ExpressionMutableList args) {
		return new MethodReference(accessPath, name, args);
	}

	/**
	 Creates a new {@link SuperConstructorReference}.
	 @return a new instance of SuperConstructorReference.
	 */
	public SuperConstructorReference createSuperConstructorReference() {
		return new SuperConstructorReference();
	}

	/**
	 Creates a new {@link SuperConstructorReference}.
	 @return a new instance of SuperConstructorReference.
	 */
	public SuperConstructorReference createSuperConstructorReference(ExpressionMutableList arguments) {
		return new SuperConstructorReference(arguments);
	}

	/**
	 Creates a new {@link SuperConstructorReference}.
	 @return a new instance of SuperConstructorReference.
	 */
	public SuperConstructorReference createSuperConstructorReference(
		ReferencePrefix path,
		ExpressionMutableList arguments) {
		return new SuperConstructorReference(path, arguments);
	}

	/**
	 Creates a new {@link SuperReference}.
	 @return a new instance of SuperReference.
	 */
	public SuperReference createSuperReference() {
		return new SuperReference();
	}

	/**
	 Creates a new {@link SuperReference}.
	 @return a new instance of SuperReference.
	 */
	public SuperReference createSuperReference(ReferencePrefix accessPath) {
		return new SuperReference(accessPath);
	}

	/**
	 Creates a new {@link ThisConstructorReference}.
	 @return a new instance of ThisConstructorReference.
	 */
	public ThisConstructorReference createThisConstructorReference() {
		return new ThisConstructorReference();
	}

	/**
	 Creates a new {@link ThisConstructorReference}.
	 @return a new instance of ThisConstructorReference.
	 */
	public ThisConstructorReference createThisConstructorReference(ExpressionMutableList arguments) {
		return new ThisConstructorReference(arguments);
	}

	/**
	 Creates a new {@link ThisReference}.
	 @return a new instance of ThisReference.
	 */
	public ThisReference createThisReference() {
		return new ThisReference();
	}

	/**
	 Creates a new {@link ThisReference}.
	 @return a new instance of ThisReference.
	 */
	public ThisReference createThisReference(TypeReference outer) {
		return new ThisReference(outer);
	}

	/**
	 Creates a new {@link VariableReference}.
	 @return a new instance of VariableReference.
	 */
	public VariableReference createVariableReference() {
		return new VariableReference();
	}

	/**
	 Creates a new {@link VariableReference}.
	 @return a new instance of VariableReference.
	 */
	public VariableReference createVariableReference(Identifier id) {
		return new VariableReference(id);
	}

	/**
	 Creates a new {@link ArrayLengthReference}.
	 @return a new instance of ArrayLengthReference.
	 */
	public ArrayLengthReference createArrayLengthReference() {
		return new ArrayLengthReference();
	}

	/**
	 Creates a new {@link ArrayLengthReference}.
	 @return a new instance of ArrayLengthReference.
	 */
	public ArrayLengthReference createArrayLengthReference(ReferencePrefix prefix) {
		return new ArrayLengthReference(prefix);
	}

	/**
	 Creates a new {@link BinaryAnd}.
	 @return a new instance of BinaryAnd.
	 */
	public BinaryAnd createBinaryAnd() {
		return new BinaryAnd();
	}

	/**
	 Creates a new {@link BinaryAnd}.
	 @return a new instance of BinaryAnd.
	 */
	public BinaryAnd createBinaryAnd(Expression lhs, Expression rhs) {
		return new BinaryAnd(lhs, rhs);
	}

	/**
	 Creates a new {@link BinaryAndAssignment}.
	 @return a new instance of BinaryAndAssignment.
	 */
	public BinaryAndAssignment createBinaryAndAssignment() {
		return new BinaryAndAssignment();
	}

	/**
	 Creates a new {@link BinaryAndAssignment}.
	 @return a new instance of BinaryAndAssignment.
	 */
	public BinaryAndAssignment createBinaryAndAssignment(
		Expression lhs,
		Expression rhs) {
		return new BinaryAndAssignment(lhs, rhs);
	}

	/**
	 Creates a new {@link BinaryNot}.
	 @return a new instance of BinaryNot.
	 */
	public BinaryNot createBinaryNot() {
		return new BinaryNot();
	}

	/**
	 Creates a new {@link BinaryNot}.
	 @return a new instance of BinaryNot.
	 */
	public BinaryNot createBinaryNot(Expression child) {
		return new BinaryNot(child);
	}

	/**
	 Creates a new {@link BinaryOr}.
	 @return a new instance of BinaryOr.
	 */
	public BinaryOr createBinaryOr() {
		return new BinaryOr();
	}

	/**
	 Creates a new {@link BinaryOr}.
	 @return a new instance of BinaryOr.
	 */
	public BinaryOr createBinaryOr(Expression lhs, Expression rhs) {
		return new BinaryOr(lhs, rhs);
	}

	/**
	 Creates a new {@link BinaryOrAssignment}.
	 @return a new instance of BinaryOrAssignment.
	 */
	public BinaryOrAssignment createBinaryOrAssignment() {
		return new BinaryOrAssignment();
	}

	/**
	 Creates a new {@link BinaryOrAssignment}.
	 @return a new instance of BinaryOrAssignment.
	 */
	public BinaryOrAssignment createBinaryOrAssignment(Expression lhs, Expression rhs) {
		return new BinaryOrAssignment(lhs, rhs);
	}

	/**
	 Creates a new {@link BinaryXOr}.
	 @return a new instance of BinaryXOr.
	 */
	public BinaryXOr createBinaryXOr() {
		return new BinaryXOr();
	}

	/**
	 Creates a new {@link BinaryXOr}.
	 @return a new instance of BinaryXOr.
	 */
	public BinaryXOr createBinaryXOr(Expression lhs, Expression rhs) {
		return new BinaryXOr(lhs, rhs);
	}

	/**
	 Creates a new {@link BinaryXOrAssignment}.
	 @return a new instance of BinaryXOrAssignment.
	 */
	public BinaryXOrAssignment createBinaryXOrAssignment() {
		return new BinaryXOrAssignment();
	}

	/**
	 Creates a new {@link BinaryXOrAssignment}.
	 @return a new instance of BinaryXOrAssignment.
	 */
	public BinaryXOrAssignment createBinaryXOrAssignment(
		Expression lhs,
		Expression rhs) {
		return new BinaryXOrAssignment(lhs, rhs);
	}

	/**
	 Creates a new {@link Conditional}.
	 @return a new instance of Conditional.
	 */
	public Conditional createConditional() {
		return new Conditional();
	}

	/**
	 Creates a new {@link Conditional}.
	 @return a new instance of Conditional.
	 */
	public Conditional createConditional(
		Expression guard,
		Expression thenExpr,
		Expression elseExpr) {
		return new Conditional(guard, thenExpr, elseExpr);
	}

	/**
	 Creates a new {@link CopyAssignment}.
	 @return a new instance of CopyAssignment.
	 */
	public CopyAssignment createCopyAssignment() {
		return new CopyAssignment();
	}

	/**
	 Creates a new {@link CopyAssignment}.
	 @return a new instance of CopyAssignment.
	 */
	public CopyAssignment createCopyAssignment(Expression lhs, Expression rhs) {
		return new CopyAssignment(lhs, rhs);
	}

	/**
	 Creates a new {@link Divide}.
	 @return a new instance of Divide.
	 */
	public Divide createDivide() {
		return new Divide();
	}

	/**
	 Creates a new {@link Divide}.
	 @return a new instance of Divide.
	 */
	public Divide createDivide(Expression lhs, Expression rhs) {
		return new Divide(lhs, rhs);
	}

	/**
	 Creates a new {@link DivideAssignment}.
	 @return a new instance of DivideAssignment.
	 */
	public DivideAssignment createDivideAssignment() {
		return new DivideAssignment();
	}

	/**
	 Creates a new {@link DivideAssignment}.
	 @return a new instance of DivideAssignment.
	 */
	public DivideAssignment createDivideAssignment(Expression lhs, Expression rhs) {
		return new DivideAssignment(lhs, rhs);
	}

	/**
	 Creates a new {@link Equals}.
	 @return a new instance of Equals.
	 */
	public Equals createEquals() {
		return new Equals();
	}

	/**
	 Creates a new {@link Equals}.
	 @return a new instance of Equals.
	 */
	public Equals createEquals(Expression lhs, Expression rhs) {
		return new Equals(lhs, rhs);
	}

	/**
	 Creates a new {@link GreaterOrEquals}.
	 @return a new instance of GreaterOrEquals.
	 */
	public GreaterOrEquals createGreaterOrEquals() {
		return new GreaterOrEquals();
	}

	/**
	 Creates a new {@link GreaterOrEquals}.
	 @return a new instance of GreaterOrEquals.
	 */
	public GreaterOrEquals createGreaterOrEquals(Expression lhs, Expression rhs) {
		return new GreaterOrEquals(lhs, rhs);
	}

	/**
	 Creates a new {@link GreaterThan}.
	 @return a new instance of GreaterThan.
	 */
	public GreaterThan createGreaterThan() {
		return new GreaterThan();
	}

	/**
	 Creates a new {@link GreaterThan}.
	 @return a new instance of GreaterThan.
	 */
	public GreaterThan createGreaterThan(Expression lhs, Expression rhs) {
		return new GreaterThan(lhs, rhs);
	}

	/**
	 Creates a new {@link Instanceof}.
	 @return a new instance of Instanceof.
	 */
	public Instanceof createInstanceof() {
		return new Instanceof();
	}

	/**
	 Creates a new {@link Instanceof}.
	 @return a new instance of Instanceof.
	 */
	public Instanceof createInstanceof(Expression child, TypeReference typeref) {
		return new Instanceof(child, typeref);
	}

	/**
	 Creates a new {@link LessOrEquals}.
	 @return a new instance of LessOrEquals.
	 */
	public LessOrEquals createLessOrEquals() {
		return new LessOrEquals();
	}

	/**
	 Creates a new {@link LessOrEquals}.
	 @return a new instance of LessOrEquals.
	 */
	public LessOrEquals createLessOrEquals(Expression lhs, Expression rhs) {
		return new LessOrEquals(lhs, rhs);
	}

	/**
	 Creates a new {@link LessThan}.
	 @return a new instance of LessThan.
	 */
	public LessThan createLessThan() {
		return new LessThan();
	}

	/**
	 Creates a new {@link LessThan}.
	 @return a new instance of LessThan.
	 */
	public LessThan createLessThan(Expression lhs, Expression rhs) {
		return new LessThan(lhs, rhs);
	}

	/**
	 Creates a new {@link LogicalAnd}.
	 @return a new instance of LogicalAnd.
	 */
	public LogicalAnd createLogicalAnd() {
		return new LogicalAnd();
	}

	/**
	 Creates a new {@link LogicalAnd}.
	 @return a new instance of LogicalAnd.
	 */
	public LogicalAnd createLogicalAnd(Expression lhs, Expression rhs) {
		return new LogicalAnd(lhs, rhs);
	}

	/**
	 Creates a new {@link LogicalNot}.
	 @return a new instance of LogicalNot.
	 */
	public LogicalNot createLogicalNot() {
		return new LogicalNot();
	}

	/**
	 Creates a new {@link LogicalNot}.
	 @return a new instance of LogicalNot.
	 */
	public LogicalNot createLogicalNot(Expression child) {
		return new LogicalNot(child);
	}

	/**
	 Creates a new {@link LogicalOr}.
	 @return a new instance of LogicalOr.
	 */
	public LogicalOr createLogicalOr() {
		return new LogicalOr();
	}

	/**
	 Creates a new {@link LogicalOr}.
	 @return a new instance of LogicalOr.
	 */
	public LogicalOr createLogicalOr(Expression lhs, Expression rhs) {
		return new LogicalOr(lhs, rhs);
	}

	/**
	 Creates a new {@link Minus}.
	 @return a new instance of Minus.
	 */
	public Minus createMinus() {
		return new Minus();
	}

	/**
	 Creates a new {@link Minus}.
	 @return a new instance of Minus.
	 */
	public Minus createMinus(Expression lhs, Expression rhs) {
		return new Minus(lhs, rhs);
	}

	/**
	 Creates a new {@link MinusAssignment}.
	 @return a new instance of MinusAssignment.
	 */
	public MinusAssignment createMinusAssignment() {
		return new MinusAssignment();
	}

	/**
	 Creates a new {@link MinusAssignment}.
	 @return a new instance of MinusAssignment.
	 */
	public MinusAssignment createMinusAssignment(Expression lhs, Expression rhs) {
		return new MinusAssignment(lhs, rhs);
	}

	/**
	 Creates a new {@link Modulo}.
	 @return a new instance of Modulo.
	 */
	public Modulo createModulo() {
		return new Modulo();
	}

	/**
	 Creates a new {@link Modulo}.
	 @return a new instance of Modulo.
	 */
	public Modulo createModulo(Expression lhs, Expression rhs) {
		return new Modulo(lhs, rhs);
	}

	/**
	 Creates a new {@link ModuloAssignment}.
	 @return a new instance of ModuloAssignment.
	 */
	public ModuloAssignment createModuloAssignment() {
		return new ModuloAssignment();
	}

	/**
	 Creates a new {@link ModuloAssignment}.
	 @return a new instance of ModuloAssignment.
	 */
	public ModuloAssignment createModuloAssignment(Expression lhs, Expression rhs) {
		return new ModuloAssignment(lhs, rhs);
	}

	/**
	 Creates a new {@link Negative}.
	 @return a new instance of Negative.
	 */
	public Negative createNegative() {
		return new Negative();
	}

	/**
	 Creates a new {@link Negative}.
	 @return a new instance of Negative.
	 */
	public Negative createNegative(Expression child) {
		return new Negative(child);
	}

	/**
	 Creates a new {@link New}.
	 @return a new instance of New.
	 */
	public New createNew() {
		return new New();
	}

	/**
	 Creates a new {@link New}.
	 @return a new instance of New.
	 */
	public New createNew(
		ReferencePrefix accessPath,
		TypeReference constructorName,
		ExpressionMutableList arguments) {
		return new New(accessPath, constructorName, arguments);
	}

	/**
	 Creates a new {@link New}.
	 @return a new instance of New.
	 */
	public New createNew(
		ReferencePrefix accessPath,
		TypeReference constructorName,
		ExpressionMutableList arguments,
		ClassDeclaration anonymousClass) {
		return new New(accessPath, constructorName, arguments, anonymousClass);
	}

	/**
	 Creates a new {@link NewArray}.
	 @return a new instance of NewArray.
	 */
	public NewArray createNewArray() {
		return new NewArray();
	}

	/**
	 Creates a new {@link NewArray}.
	 @return a new instance of NewArray.
	 */
	public NewArray createNewArray(
		TypeReference arrayName,
		ExpressionMutableList dimExpr) {
		return new NewArray(arrayName, dimExpr);
	}

	/**
	 Creates a new {@link NewArray}.
	 @return a new instance of NewArray.
	 */
	public NewArray createNewArray(
		TypeReference arrayName,
		int[] dimensions,
		ArrayInitializer initializer) {
		return new NewArray(arrayName, dimensions, initializer);
	}

	/**
	 Creates a new {@link NewModifier}.
	 @return a new instance of New.
	 */
	public NewModifier createNewModifier() {
		return new NewModifier();
	}

	/**
	 Creates a new {@link NotEquals}.
	 @return a new instance of NotEquals.
	 */
	public NotEquals createNotEquals() {
		return new NotEquals();
	}

	/**
	 Creates a new {@link NotEquals}.
	 @return a new instance of NotEquals.
	 */
	public NotEquals createNotEquals(Expression lhs, Expression rhs) {
		return new NotEquals(lhs, rhs);
	}

	/**
	 Creates a new {@link Plus}.
	 @return a new instance of Plus.
	 */
	public Plus createPlus() {
		return new Plus();
	}

	/**
	 Creates a new {@link Plus}.
	 @return a new instance of Plus.
	 */
	public Plus createPlus(Expression lhs, Expression rhs) {
		return new Plus(lhs, rhs);
	}

	/**
	 Creates a new {@link PlusAssignment}.
	 @return a new instance of PlusAssignment.
	 */
	public PlusAssignment createPlusAssignment() {
		return new PlusAssignment();
	}

	/**
	 Creates a new {@link PlusAssignment}.
	 @return a new instance of PlusAssignment.
	 */
	public PlusAssignment createPlusAssignment(Expression lhs, Expression rhs) {
		return new PlusAssignment(lhs, rhs);
	}

	/**
	 Creates a new {@link Positive}.
	 @return a new instance of Positive.
	 */
	public Positive createPositive() {
		return new Positive();
	}

	/**
	 Creates a new {@link Positive}.
	 @return a new instance of Positive.
	 */
	public Positive createPositive(Expression child) {
		return new Positive(child);
	}

	/**
	 Creates a new {@link PostDecrement}.
	 @return a new instance of PostDecrement.
	 */
	public PostDecrement createPostDecrement() {
		return new PostDecrement();
	}

	/**
	 Creates a new {@link PostDecrement}.
	 @return a new instance of PostDecrement.
	 */
	public PostDecrement createPostDecrement(Expression child) {
		return new PostDecrement(child);
	}

	/**
	 Creates a new {@link PostIncrement}.
	 @return a new instance of PostIncrement.
	 */
	public PostIncrement createPostIncrement() {
		return new PostIncrement();
	}

	/**
	 Creates a new {@link PostIncrement}.
	 @return a new instance of PostIncrement.
	 */
	public PostIncrement createPostIncrement(Expression child) {
		return new PostIncrement(child);
	}

	/**
	 Creates a new {@link PreDecrement}.
	 @return a new instance of PreDecrement.
	 */
	public PreDecrement createPreDecrement() {
		return new PreDecrement();
	}

	/**
	 Creates a new {@link PreDecrement}.
	 @return a new instance of PreDecrement.
	 */
	public PreDecrement createPreDecrement(Expression child) {
		return new PreDecrement(child);
	}

	/**
	 Creates a new {@link PreIncrement}.
	 @return a new instance of PreIncrement.
	 */
	public PreIncrement createPreIncrement() {
		return new PreIncrement();
	}

	/**
	 Creates a new {@link PreIncrement}.
	 @return a new instance of PreIncrement.
	 */
	public PreIncrement createPreIncrement(Expression child) {
		return new PreIncrement(child);
	}

	/**
	 Creates a new {@link ShiftLeft}.
	 @return a new instance of ShiftLeft.
	 */
	public ShiftLeft createShiftLeft() {
		return new ShiftLeft();
	}

	/**
	 Creates a new {@link ShiftLeft}.
	 @return a new instance of ShiftLeft.
	 */
	public ShiftLeft createShiftLeft(Expression lhs, Expression rhs) {
		return new ShiftLeft(lhs, rhs);
	}

	/**
	 Creates a new {@link ShiftLeftAssignment}.
	 @return a new instance of ShiftLeftAssignment.
	 */
	public ShiftLeftAssignment createShiftLeftAssignment() {
		return new ShiftLeftAssignment();
	}

	/**
	 Creates a new {@link ShiftLeftAssignment}.
	 @return a new instance of ShiftLeftAssignment.
	 */
	public ShiftLeftAssignment createShiftLeftAssignment(
		Expression lhs,
		Expression rhs) {
		return new ShiftLeftAssignment(lhs, rhs);
	}

	/**
	 Creates a new {@link ShiftRight}.
	 @return a new instance of ShiftRight.
	 */
	public ShiftRight createShiftRight() {
		return new ShiftRight();
	}

	/**
	 Creates a new {@link ShiftRight}.
	 @return a new instance of ShiftRight.
	 */
	public ShiftRight createShiftRight(Expression lhs, Expression rhs) {
		return new ShiftRight(lhs, rhs);
	}

	/**
	 Creates a new {@link ShiftRightAssignment}.
	 @return a new instance of ShiftRightAssignment.
	 */
	public ShiftRightAssignment createShiftRightAssignment() {
		return new ShiftRightAssignment();
	}

	/**
	 Creates a new {@link ShiftRightAssignment}.
	 @return a new instance of ShiftRightAssignment.
	 */
	public ShiftRightAssignment createShiftRightAssignment(
		Expression lhs,
		Expression rhs) {
		return new ShiftRightAssignment(lhs, rhs);
	}

	/**
	 Creates a new {@link Times}.
	 @return a new instance of Times.
	 */
	public Times createTimes() {
		return new Times();
	}

	/**
	 Creates a new {@link Times}.
	 @return a new instance of Times.
	 */
	public Times createTimes(Expression lhs, Expression rhs) {
		return new Times(lhs, rhs);
	}

	/**
	 Creates a new {@link TimesAssignment}.
	 @return a new instance of TimesAssignment.
	 */
	public TimesAssignment createTimesAssignment() {
		return new TimesAssignment();
	}

	/**
	 Creates a new {@link TimesAssignment}.
	 @return a new instance of TimesAssignment.
	 */
	public TimesAssignment createTimesAssignment(Expression lhs, Expression rhs) {
		return new TimesAssignment(lhs, rhs);
	}

	/**
	 Creates a new {@link TypeCast}.
	 @return a new instance of TypeCast.
	 */
	public TypeCast createTypeCast() {
		return new TypeCast();
	}

	/**
	 Creates a new {@link TypeCast}.
	 @return a new instance of TypeCast.
	 */
	public TypeCast createTypeCast(Expression child, TypeReference typeref) {
		return new TypeCast(child, typeref);
	}

	/**
	 Creates a new {@link As}.
	 @return a new instance of As.
	 */
	public As createAs() {
		return new As();
	}

	/**
	 Creates a new {@link As}.
	 @return a new instance of As.
	 */
	public As createAs(Expression child, TypeReference typeref) {
		return new As(child, typeref);
	}

	/**
	 Creates a new {@link Abstract}.
	 @return a new instance of Abstract.
	 */
	public Abstract createAbstract() {
		return new Abstract();
	}

	/**
	 Creates a new {@link Abstract}.
	 @return a new instance of Abstract.
	 */
	public Internal createInternal() {
		return new Internal();
	}

	/**
	 Creates a new {@link Sealed}.
	 @return a new instance of Sealed.
	 */
	public Sealed createSealed() {
		return new Sealed();
	}

	/**
	 Creates a new {@link Private}.
	 @return a new instance of Private.
	 */
	public Private createPrivate() {
		return new Private();
	}

	/**
	 Creates a new {@link Protected}.
	 @return a new instance of Protected.
	 */
	public Protected createProtected() {
		return new Protected();
	}

	/**
	 Creates a new {@link Public}.
	 @return a new instance of Public.
	 */
	public Public createPublic() {
		return new Public();
	}

	/**
	 Creates a new {@link Static}.
	 @return a new instance of Static.
	 */
	public Static createStatic() {
		return new Static();
	}

	/**
	 Creates a new {@link Ref}.
	 @return a new instance of Ref.
	 */
	public Readonly createReadonly() {
		return new Readonly();
	}

	/**
	 Creates a new {@link Ref}.
	 @return a new instance of Ref.
	 */
	public Ref createRef() {
		return new Ref();
	}

	/**
	 Creates a new {@link Out}.
	 @return a new instance of Out.
	 */
	public Out createOut() {
		return new Out();
	}

	/**
	 Creates a new {@link Virtual}.
	 @return a new instance of Virtual.
	 */
	public Virtual createVirtual() {
		return new Virtual();
	}

	/**
	 Creates a new {@link Virtual}.
	 @return a new instance of Virtual.
	 */
	public Override createOverride() {
		return new Override();
	}
	/**
	 Creates a new {@link Virtual}.
	 @return a new instance of Virtual.
	 */
	public Extern createExtern() {
		return new Extern();
	}
	/**
	 Creates a new {@link Volatile}.
	 @return a new instance of Volatile.
	 */
	public Volatile createVolatile() {
		return new Volatile();
	}

	/**
	 Creates a new {@link Break}.
	 @return a new instance of Break.
	 */
	public Break createBreak() {
		return new Break();
	}

	/**
	 Creates a new {@link Case}.
	 @return a new instance of Case.
	 */
	public Case createCase() {
		return new Case();
	}

	/**
	 Creates a new {@link Case}.
	 @return a new instance of Case.
	 */
	public Case createCase(Expression e) {
		return new Case(e);
	}

	/**
	 Creates a new {@link Case}.
	 @return a new instance of Case.
	 */
	public Case createCase(Expression e, StatementMutableList body) {
		return new Case(e, body);
	}

	/**
	 Creates a new {@link Catch}.
	 @return a new instance of Catch.
	 */
	public Catch createCatch() {
		return new Catch();
	}

	/**
	 Creates a new {@link Catch}.
	 @return a new instance of Catch.
	 */
	public Catch createCatch(ParameterDeclaration e, StatementBlock body) {
		return new Catch(e, body);
	}

	/**
	 Creates a new {@link Continue}.
	 @return a new instance of Continue.
	 */
	public Continue createContinue() {
		return new Continue();
	}

	/**
	 Creates a new {@link Goto}.
	 @return a new instance of Goto.
	 */
	public Goto createGoto() {
		return new Goto();
	}

	/**
	 Creates a new {@link Goto}.
	 @return a new instance of Goto.
	 */
	public Goto createGoto(Identifier label) {
		return new Goto(label);
	}

	/**
	 Creates a new {@link Default}.
	 @return a new instance of Default.
	 */
	public Default createDefault() {
		return new Default();
	}

	/**
	 Creates a new {@link Default}.
	 @return a new instance of Default.
	 */
	public Default createDefault(StatementMutableList body) {
		return new Default(body);
	}

	/**
	 Creates a new {@link Do}.
	 @return a new instance of Do.
	 */
	public Do createDo() {
		return new Do();
	}

	/**
	 Creates a new {@link Do}.
	 @return a new instance of Do.
	 */
	public Do createDo(Expression guard) {
		return new Do(guard);
	}

	/**
	 Creates a new {@link Do}.
	 @return a new instance of Do.
	 */
	public Do createDo(Expression guard, Statement body) {
		return new Do(guard, body);
	}

	/**
	 Creates a new {@link Else}.
	 @return a new instance of Else.
	 */
	public Else createElse() {
		return new Else();
	}

	/**
	 Creates a new {@link Else}.
	 @return a new instance of Else.
	 */
	public Else createElse(Statement body) {
		return new Else(body);
	}

	/**
	 Creates a new {@link EmptyStatement}.
	 @return a new instance of EmptyStatement.
	 */
	public EmptyStatement createEmptyStatement() {
		return new EmptyStatement();
	}

	/**
	 Creates a new {@link Finally}.
	 @return a new instance of Finally.
	 */
	public Finally createFinally() {
		return new Finally();
	}

	/**
	 Creates a new {@link Finally}.
	 @return a new instance of Finally.
	 */
	public Finally createFinally(StatementBlock body) {
		return new Finally(body);
	}

	/**
	 Creates a new {@link CheckedBlock}.
	 @return a new instance of CheckedBlock.
	 */
	public CheckedBlock createCheckedBlock() {
		return new CheckedBlock();
	}

	/**
	 Creates a new {@link CheckedBlock}.
	 @return a new instance of CheckedBlock.
	 */
	public CheckedBlock createCheckedBlock(StatementBlock body) {
		return new CheckedBlock(body);
	}

	/**
	 Creates a new {@link UncheckedBlock}.
	 @return a new instance of UncheckedBlock.
	 */
	public UncheckedBlock createUncheckedBlock() {
		return new UncheckedBlock();
	}

	/**
	 Creates a new {@link CheckedBlock}.
	 @return a new instance of CheckedBlock.
	 */
	public UncheckedBlock createUncheckedBlock(StatementBlock body) {
		return new UncheckedBlock(body);
	}

	/**
	 Creates a new {@link Checked}.
	 @return a new instance of Checked.
	 */
	public Checked createChecked() {
		return new Checked();
	}

	/**
	 Creates a new {@link Unchecked}.
	 @return a new instance of Unchecked.
	 */
	public Unchecked createUnchecked() {
		return new Unchecked();
	}

	/**
	 Creates a new {@link Typeof}.
	 @return a new instance of Typeof.
	 */
	public Typeof createTypeof() {
		return new Typeof();
	}

	/**
	 Creates a new {@link For}.
	 @return a new instance of For.
	 */
	public For createFor() {
		return new For();
	}

	/**
	 Creates a new {@link For}.
	 @return a new instance of For.
	 */
	public For createFor(
		LoopInitializerMutableList inits,
		Expression guard,
		ExpressionMutableList updates,
		Statement body) {
		return new For(inits, guard, updates, body);
	}

	/**
	   Creates a new {@link Assert}.
	   @return a new instance of Assert.
	 */
	public Assert createAssert() {
		return new Assert();
	}

	/**
	   Creates a new {@link Assert}.
	   @return a new instance of Assert.
	 */
	public Assert createAssert(Expression cond) {
		return new Assert(cond);
	}

	/**
	   Creates a new {@link Assert}.
	   @return a new instance of Assert.
	 */
	public Assert createAssert(Expression cond, Expression msg) {
		return new Assert(cond, msg);
	}

	/**
	 Creates a new {@link If}.
	 @return a new instance of If.
	 */
	public If createIf() {
		return new If();
	}

	/**
	 Creates a new {@link If}.
	 @return a new instance of If.
	 */
	public If createIf(Expression e, Statement thenStatement) {
		return new If(e, thenStatement);
	}

	/**
	 Creates a new {@link If}.
	 @return a new instance of If.
	 */
	public If createIf(Expression e, Then thenBranch) {
		return new If(e, thenBranch);
	}

	/**
	 Creates a new {@link If}.
	 @return a new instance of If.
	 */
	public If createIf(Expression e, Then thenBranch, Else elseBranch) {
		return new If(e, thenBranch, elseBranch);
	}

	/**
	 Creates a new {@link If}.
	 @return a new instance of If.
	 */
	public If createIf(Expression e, Statement thenStatement, Statement elseStatement) {
		return new If(e, thenStatement, elseStatement);
	}

	/**
	 Creates a new {@link LabeledStatement}.
	 @return a new instance of LabeledStatement.
	 */
	public LabeledStatement createLabeledStatement() {
		return new LabeledStatement();
	}

	/**
	 Creates a new {@link LabeledStatement}.
	 @return a new instance of LabeledStatement.
	 */
	public LabeledStatement createLabeledStatement(Identifier id) {
		return new LabeledStatement(id);
	}

	/**
	 Creates a new {@link LabeledStatement}.
	 @return a new instance of LabeledStatement.
	 */
	public LabeledStatement createLabeledStatement(Identifier id, Statement statement) {
		return new LabeledStatement(id, statement);
	}

	/**
	 Creates a new {@link Return}.
	 @return a new instance of Return.
	 */
	public Return createReturn() {
		return new Return();
	}

	/**
	 Creates a new {@link Return}.
	 @return a new instance of Return.
	 */
	public Return createReturn(Expression expr) {
		return new Return(expr);
	}

	/**
	 Creates a new {@link StatementBlock}.
	 @return a new instance of StatementBlock.
	 */
	public StatementBlock createStatementBlock() {
		return new StatementBlock();
	}

	/**
	 Creates a new {@link StatementBlock}.
	 @return a new instance of StatementBlock.
	 */
	public StatementBlock createStatementBlock(StatementMutableList block) {
		return new StatementBlock(block);
	}

	/**
	 Creates a new {@link Switch}.
	 @return a new instance of Switch.
	 */
	public Switch createSwitch() {
		return new Switch();
	}

	/**
	 Creates a new {@link Switch}.
	 @return a new instance of Switch.
	 */
	public Switch createSwitch(Expression e) {
		return new Switch(e);
	}

	/**
	 Creates a new {@link Switch}.
	 @return a new instance of Switch.
	 */
	public Switch createSwitch(Expression e, BranchMutableList branches) {
		return new Switch(e, branches);
	}

	/**
	 Creates a new {@link LockedBlock}.
	 @return a new instance of LockedBlock.
	 */
	public LockedBlock createLockedBlock() {
		return new LockedBlock();
	}

	/**
	 Creates a new {@link LockedBlock}.
	 @return a new instance of LockedBlock.
	 */
	public LockedBlock createLockedBlock(StatementBlock body) {
		return new LockedBlock(body);
	}

	/**
	 Creates a new {@link LockedBlock}.
	 @return a new instance of LockedBlock.
	 */
	public LockedBlock createLockedBlock(Expression e, StatementBlock body) {
		return new LockedBlock(e, body);
	}

	/**
	 Creates a new {@link LockedBlock}.
	 @return a new instance of LockedBlock.
	 */
	public UsingBlock createUsingBlock() {
		return new UsingBlock();
	}

	/**
	 Creates a new {@link Then}.
	 @return a new instance of Then.
	 */
	public Then createThen() {
		return new Then();
	}

	/**
	 Creates a new {@link Then}.
	 @return a new instance of Then.
	 */
	public Then createThen(Statement body) {
		return new Then(body);
	}

	/**
	 Creates a new {@link Throw}.
	 @return a new instance of Throw.
	 */
	public Throw createThrow() {
		return new Throw();
	}

	/**
	 Creates a new {@link Throw}.
	 @return a new instance of Throw.
	 */
	public Throw createThrow(Expression expr) {
		return new Throw(expr);
	}

	/**
	 Creates a new {@link Try}.
	 @return a new instance of Try.
	 */
	public Try createTry() {
		return new Try();
	}

	/**
	 Creates a new {@link Try}.
	 @return a new instance of Try.
	 */
	public Try createTry(StatementBlock body) {
		return new Try(body);
	}

	/**
	 Creates a new {@link Try}.
	 @return a new instance of Try.
	 */
	public Try createTry(StatementBlock body, BranchMutableList branches) {
		return new Try(body, branches);
	}

	/**
	 Creates a new {@link While}.
	 @return a new instance of While.
	 */
	public While createWhile() {
		return new While();
	}

	/**
	 Creates a new {@link While}.
	 @return a new instance of While.
	 */
	public While createWhile(Expression guard) {
		return new While(guard);
	}

	/**
	 Creates a new {@link While}.
	 @return a new instance of While.
	 */
	public While createWhile(Expression guard, Statement body) {
		return new While(guard, body);
	}

	/**
	 * Creates a new {@link foreach}.
	 * @return Foreach
	 */
	public Foreach createForeach() {
		return new Foreach();
	}

	/**
	 * Method createIndexerDeclaration.
	 * @return IndexerDeclaration
	 */
	public IndexerDeclaration createIndexerDeclaration() {
		return new IndexerDeclaration();
	}

	/**
	 * Method createEventDeclaration.
	 * @return EventDeclaration
	 */
	public EventDeclaration createEventDeclaration() {
		return new EventDeclaration();
	}

	/**
	 * Method createUsingAlias.
	 * @return UsingAlias
	 */
	public UsingAlias createUsingAlias() {
		return new UsingAlias();
	}

	/**
	 * @see recoder.ProgramFactory#createMemberName(Identifier)
	 */
	public MemberName createMemberName(Identifier id) {
		return new MemberName(id);
	}

	/**
	 * Method createParams.
	 * @return Modifier
	 */
	public Modifier createParams() {
		return new Params();
	}

	/**
	 * Method createOutOperator.
	 * @param expr
	 * @return Expression
	 */
	public Expression createOutOperator(Expression expr) {
		return new OutOperator(expr);
	}

	/**
	 * Method createRefOperator.
	 * @param expr
	 * @return Expression
	 */
	public Expression createRefOperator(Expression expr) {
		return new RefOperator(expr);
	}

	/**
	 * Method createGetAccessor.
	 * @return GetAccessor
	 */
	public GetAccessor createGetAccessor() {
		return new GetAccessor();
	}

	/**
	 * Method createSetAccessor.
	 * @return SetAccessor
	 */
	public SetAccessor createSetAccessor() {
		return new SetAccessor();
	}

	/**
	 * Method createAddAccessor.
	 * @return AddAccessor
	 */
	public AddAccessor createAddAccessor() {
		return new AddAccessor();
	}

	/**
	 * Method createRemoveAccessor.
	 * @return RemoveAccessor
	 */
	public RemoveAccessor createRemoveAccessor() {
		return new RemoveAccessor();
	}

	/**
	 * Method createAttributeSection.
	 * @return AttributeSection
	 */
	public AttributeSection createAttributeSection() {
		return new AttributeSection();
	}

	/**
	 * Method createAssemblyTarget.
	 * @return AttributeTarget
	 */
	public AttributeTarget createAssemblyTarget() {
		return new AssemblyTarget();
	}

	/**
	 * Method createAttribute.
	 * @return Attribute
	 */
	public Attribute createAttribute() {
		return new Attribute();
	}

	/**
	 * Method createAttributeArgument.
	 * @param expr
	 * @return AttributeArgument
	 */
	public AttributeArgument createAttributeArgument(Expression expr) {
		return new AttributeArgument(expr);
	}

	/**
	 * Method createNamedAttributeArgument.
	 * @param id
	 * @param expr
	 * @return AttributeArgument
	 */
	public AttributeArgument createNamedAttributeArgument(
		Identifier id,
		Expression expr) {
		return new NamedAttributeArgument(id, expr);
	}

	/**
	 * Method createEventTarget.
	 * @return AttributeTarget
	 */
	public AttributeTarget createEventTarget() {
		return new EventTarget();
	}

	/**
	 * Method createFieldTarget.
	 * @return AttributeTarget
	 */
	public AttributeTarget createFieldTarget() {
		return new FieldTarget();
	}

	/**
	 * Method createMethodTarget.
	 * @return AttributeTarget
	 */
	public AttributeTarget createMethodTarget() {
		return new MethodTarget();
	}

	/**
	 * Method createModuleTarget.
	 * @return AttributeTarget
	 */
	public AttributeTarget createModuleTarget() {
		return new ModuleTarget();
	}

	/**
	 * Method createParamTarget.
	 * @return AttributeTarget
	 */
	public AttributeTarget createParamTarget() {
		return new ParamTarget();
	}

	/**
	 * Method createPropertyTarget.
	 * @return AttributeTarget
	 */
	public AttributeTarget createPropertyTarget() {
		return new PropertyTarget();
	}

	/**
	 * Method createReturnTarget.
	 * @return AttributeTarget
	 */
	public AttributeTarget createReturnTarget() {
		return new ReturnTarget();
	}

	/**
	 * Method createTypeTarget.
	 * @return AttributeTarget
	 */
	public AttributeTarget createTypeTarget() {
		return new TypeTarget();
	}

	/**
	 * @see recoder.ProgramFactory#createMethodGroupReference()
	 */
	public MethodGroupReference createMethodGroupReference() {
		return new MethodGroupReference();
	}

	/**
	 * Method createPropertySpecification.
	 * @param mname
	 * @return PropertySpecification
	 */
	public PropertySpecification createPropertySpecification(MemberName mname) {
		return new PropertySpecification(mname);
	}

	/**
	 * Method createPropertySpecification.
	 * @return PropertySpecification
	 */
	public PropertySpecification createPropertySpecification() {
		return new PropertySpecification();
	}

	/**
	 * Method createEventSpecification.
	 * @return EventSpecification
	 */
	public EventSpecification createEventSpecification() {
		return new EventSpecification();
	}

}
