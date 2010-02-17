// This file is part of the RECODER library and protected by the LGPL.

package recodercs.csharp;

import recodercs.*;

/**
   A comment, possibly with multiple lines.
 
   @author AL
 */

public class Comment extends CSharpSourceElement {

    /**
     The text string.
     */

    protected String text;

    /**
     Mark if the comment stands before its associated element.
     */

    protected boolean prefixed;

    /**
     Parent.
     */

    protected ProgramElement parent;

    /**
     Create a new empty comment. The comment contains an empty string,
     the slash-asterics markers are not created.
     */

    public Comment() {
        text = "";
    }

    /**
     Create a new comment with the given content. No extra comment markers
     are created.
     @param text the text of the comment.
     */

    public Comment(String text) {
        setText(text);
    }

    /**
     Create a new comment with the given content. No extra comment markers
     are created.
     @param text the text of the comment.
     */

    public Comment(String text, boolean prefixed) {
        setText(text);
        setPrefixed(prefixed);
    }

    /**
     Comment.
     @param proto a comment.
     */

    protected Comment(Comment proto) {
        super(proto);
        text = new String(proto.text);
        prefixed = proto.prefixed;
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new Comment(this);
    }

    /**
       Check if this comment should be prefixed in front of the parent 
       element, or if it should follow it.
       @return the boolean value.
     */

    public boolean isPrefixed() {
        return prefixed;
    }

    /**
       Define if this comment should be prefixed in front of the parent 
       element, or if it should follow it.
       @param prefixed the boolean value.
     */

    public void setPrefixed(boolean prefixed) {
        this.prefixed = prefixed;
    }

    /**
     Set parent of the comment.
     @param p a program element.
     */

    public void setParent(ProgramElement p) {
        parent = p;
    }

    /**
     Get parent of the comment.
     @return the parent element.
     */

    public ProgramElement getParent() {
        return parent;
    }

    /**
     Get the comment text.
     @return the string with the complete content.
     */

    public String getText() {
        return text;
    }

    /**
     Set text, including all markers. The text must contain all necessary
     leading and closing tokens.
     @param text a string.
     */

    public void setText(String text) {
        this.text = text;
    }

    public void accept(SourceVisitor v) {
        v.visitComment(this);
    }
}
