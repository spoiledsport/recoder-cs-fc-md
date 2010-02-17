// This file is part of the RECODER library and protected by the LGPL.

package recodercs.kit;

import recodercs.*;
import recodercs.convenience.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;
import recodercs.csharp.reference.*;
import recodercs.list.*;
import recodercs.service.*;
import recodercs.util.Debug;

/** Class with utilities for commenting methods, fields, compilation units.
 *  CURRENTLY NOT AVAILABLE IN RECODER-C#.
 */
public class CommentKit {
// DISABLED: C# uses a completely different syntax

// TODO: Write a commenting tool for C#
 
//
//    private CommentKit() {}
//
//    /**
//       Factory method creating an empty or faked comment conforming to
//       JavaDoc conventions for the given method declaration.
//       @param method a non-<CODE>null</CODE> method declaration.
//       @param dummy flag to indicate whether a fake comment text should
//       be inserted.
//       @return a brand-new doc comment.
//     */
//    public static XmlDocComment createDoc(MethodDeclaration method,
//				       boolean dummy) {
//
//	Debug.assert(method);
//	StringBuffer text = new StringBuffer("/**\n");
//	if (dummy) {
//	    text.append("  ");
//	    text.append(guessDocumentation(method.getName(), true));
//	    text.append("\n");
//	}
//	int c = method.getParameterDeclarationCount();
//	for (int i = 0; i < c; i += 1) {
//	    ParameterDeclaration param = method.getParameterDeclarationAt(i);
//	    text.append("  @param "
//		+ param.getVariables().getVariableSpecification(0).getName()
//		+ ' ');
//	    if (dummy) {
//		text.append(guessDocumentation(param.getTypeReference(), false));
//	    }
//	    text.append('\n');
//	}
//	TypeReference ret = method.getTypeReference();
//	if (ret != null && !"void".equals(ret.getName())) {
//	    text.append("  @return ");
//	    if (dummy) {
//		text.append(guessDocumentation(ret, true));
//	    }
//	    text.append('\n');
//	}
//	Throws th = method.getThrown();
//	if (th != null) {
//	    TypeReferenceList excepts = th.getExceptions();
//	    for (int i = 0; i < excepts.size(); i += 1) {
//		TypeReference tr = excepts.getTypeReference(i);
//		text.append("  @exception " + tr.getName());
//		if (dummy) {
//		    text.append(" occasionally thrown.\n");
//		}
//	    }
//	}
//	text.append("*/");
//	return method.getFactory().createXmlDocComment(text.toString());
//    }
//
//    /**
//       Factory method creating an empty or faked comment conforming to
//       JavaDoc conventions for the given field declaration.
//       @param field a non-<CODE>null</CODE> field declaration.
//       @param dummy flag to indicate whether a fake comment text should
//       be inserted.
//       @return a brand-new doc comment.
//     */
//    public static XmlDocComment createDoc(FieldDeclaration field, 
//				       boolean dummy) {
//	Debug.assert(field);
//	ProgramFactory factory = field.getFactory();
//	if (dummy) {
//	    String name = 
//		field.getVariables().getVariableSpecification(0).getName();
//	    return factory.createXmlDocComment("/**\n  " + 
//					    guessDocumentation(name, true) + 
//					    "\n*/");
//	} else {
//	    return factory.createXmlDocComment("/**\n  \n*/");
//	}
//    }
//
//    /**
//       Factory method creating an empty or faked comment conforming to
//       JavaDoc conventions for the given field declaration.
//       This variant creates an empty serial tag when the enclosing type
//       is a class implementing <CODE>java.io.Serializable</CODE>.
//       @param si the source info service.
//       @param ni the name info service.
//       @param field a non-<CODE>null</CODE> field declaration.
//       @param dummy flag to indicate whether a fake comment text should
//       be inserted.
//       @return a brand-new doc comment.
//     */
//    public static XmlDocComment createDoc(SourceInfo si, NameInfo ni,
//				       FieldDeclaration field, 
//				       boolean dummy) {
//	Debug.assert(field);
//	boolean isSerial;
//	TypeDeclaration td = MiscKit.getParentTypeDeclaration(field);
//	if (td instanceof ClassDeclaration) {
//	    isSerial = si.isSubtype((ClassDeclaration)td, ni.getJavaIoSerializable());
//	} else {
//	    isSerial = false;
//	}
//	ProgramFactory factory = field.getFactory();
//	if (dummy) {
//	    String name = 
//		field.getVariables().getVariableSpecification(0).getName();
//	    return factory.createXmlDocComment("/**\n  " + 
//					    guessDocumentation(name, true) + 
//					    (isSerial ? "\n  @serial" : "") +
//					    "\n*/");
//	} else {
//	    return factory.createXmlDocComment("/**\n  " + 
//    					    (isSerial ? "\n  @serial" : "") +
//					    "n*/");
//	}
//    }
//
//
//    /**
//       Factory method creating an empty or faked comment conforming to
//       JavaDoc conventions for the given type declaration.
//       @param type a non-<CODE>null</CODE> type declaration.
//       @param dummy flag to indicate whether a fake comment text should
//       be inserted.
//       @return a brand-new doc comment.
//     */
//    public static XmlDocComment createDoc(TypeDeclaration type, boolean dummy) {
//	Debug.assert(type);
//	ProgramFactory factory = type.getFactory();
//	if (dummy) {
//	    String name = type.getName();	    
//	    return factory.createXmlDocComment("/**\n  " + 
//					    guessDocumentation(name, true) + 
//					    "\n  @author " + 
//					    "\n*/");
//	} else {
//	    return factory.createXmlDocComment("/**\n  \n*/");
//	}
//    }
//
//    /**
//       Guesses a documentation for the given type (reference).
//       The generated documentation perfectly describes the type, given that 
//       it is perfectly self-explanatory ;)       
//       @param tr a type reference.
//       @param returned flag indicating if the documentation should describe
//       a method return value.
//       @return the description string.
//     */
//    static String guessDocumentation(TypeReference tr, boolean returned) {
//	String tn = tr.getName();
//	if (tr.getDimensions() == 0) {
//	    if (tn.equals("int") || 
//		tn.equals("boolean") ||
//		tn.equals("short") ||
//		tn.equals("long") ||
//		tn.equals("byte") ||
//		tn.equals("char") ||
//		tn.equals("float") ||
//		tn.equals("double")) {
//		tn += " value";
//	    }
//	}
//	String ty = guessDocumentation(tn, false);
//	switch (tr.getDimensions()) {
//	case 0:
//	    if (returned) {
//		return "the " + ty;
//	    }
//	    if ("aeiouAEIOU".indexOf(ty.charAt(0)) >= 0) {
//		return "an " + ty;
//	    } else {
//		return "a " + ty;
//	    }		    
//	case 1:
//	    return (returned ? "the" : "an") + " array of " + ty;
//	case 2:
//	    return (returned ? "the" : "a") + " matrix of " + ty;
//	default: 
//	    return (returned ? "the" : "a") + " multi-dimensional array of " + ty;
//	}
//	return ty;
//    }
//
//    /**
//       Derives a documentation from a given name. The method assumes that
//       the Sun conventions are met and separates the words:
//       <TT>guessDocumentation("HelloWorld", false) == "hello world."</TT>
//       @param name a string used as an identifier.
//       @param capital flag indicating if the first word of the derived
//       documentation should start with a capital letter.
//       @return the description string.
//     */
//    static String guessDocumentation(String name, boolean capital) {
//    // to do: enable '_' as separator, check if parts are completely 
//    // capitalized (e.g. for constants)
//	int len = name.length();
//	StringBuffer res = new StringBuffer(len + 6);	
//	for (int i = 0; i < len; i += 1) {
//	    char ch = name.charAt(i);
//	    if (Character.isUpperCase(ch)) {
//		if (i < len - 1 && Character.isUpperCase(name.charAt(i + 1))) {
//		    if (i > 0 && !Character.isUpperCase(name.charAt(i - 1))) {
//			res.append(' ');
//		    }
//		    res.append(ch);
//		} else {
//		    if (i > 0) {
//			res.append(' ');
//		    }
//		    res.append(Character.toLowerCase(ch));
//		}
//	    } else {
//		res.append(ch);
//	    }
//	}
//	if (capital) {
//	    res.setCharAt(0, Character.toUpperCase(res.charAt(0)));
//	}
//	res.append('.');
//	return res.toString();
//    }
}
