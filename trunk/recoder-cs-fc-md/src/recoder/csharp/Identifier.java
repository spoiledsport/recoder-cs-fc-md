// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp;

import recoder.*;

/**
 Identifier.
 @author AL
 @author <TT>AutoDoc</TT>
 */

public class Identifier extends CSharpProgramElement
 implements TerminalProgramElement {

    /**
     Parent.
     */

    protected NamedProgramElement parent;

    /**
     Id.
     */

    protected String id;
    
    protected boolean verbatimId;

    /**
     Identifier.
     */

    public Identifier() {
        id = "";
    }

    /**
     Identifier.
     @param text a string.
     */

    public Identifier(String text) {
        setText(text);
    }

    /**
     Identifier.
     @param proto an identifier.
     */

    protected Identifier(Identifier proto) {
        super(proto);
        id = proto.id;
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Identifier(this);
    }

    /**
     Get AST parent.
     @return the non terminal program element.
     */

    public NonTerminalProgramElement getASTParent() {
        return parent;
    }

    /**
     Get parent.
     @return the named program element.
     */

    public NamedProgramElement getParent() {
        return parent;
    }

    /**
     Set parent.
     @param p a named program element.
     */

    public void setParent(NamedProgramElement p) {
        parent = p;
    }

    /**
     Get text. The String is made unambiguous.
     @return the string.
     @see #setText(String)
     */

    public final String getText() {
        return (verbatimId) ? "@" + id : id;
    }

    /**
     Set text. The text becomes internalized such that 
     x.getText().equals(y.getText()) is equivalent to 
     x.getText()==y.getText() except for null Strings.
     @param text a string.
     */

    protected void setText(String text) {
        if (text.startsWith("@")) {
        	verbatimId = true;
        	text = text.substring(1);
        } else {
        	verbatimId = false;
        }
        if (!Character.isJavaIdentifierStart(text.charAt(0))) {
            throw new IllegalArgumentException((text + " is not a valid Java identifier"));
        }
        for (int i = text.length() - 1; i >= 1; i -= 1) {
            if (!Character.isJavaIdentifierPart(text.charAt(i))) {
                throw new IllegalArgumentException((text + "is not a valid Java identifier"));
            }
        }
        id = text.intern();
    }

    public void accept(SourceVisitor v) {
        v.visitIdentifier(this);
    }
    
    public boolean isVerbatim() {
    	return verbatimId;
    }
}
