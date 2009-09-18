// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp;

import java.io.*;

import recoder.*;
import recoder.list.*;
import recoder.convenience.TreeWalker;

/**
 Top level implementation of a Java {@link ProgramElement}.
 @author AL
 */

public abstract class CSharpProgramElement
 extends CSharpSourceElement
 implements ProgramElement {

    /**
       Comments.
    */

    protected CommentMutableList comments;


    /**
     Java program element.
     */

    public CSharpProgramElement() {}

    /**
     Java program element.
     @param proto a java program element.
     */

    protected CSharpProgramElement(CSharpProgramElement proto) {
        super(proto);
        if (proto.comments != null) {
            comments = (CommentMutableList)proto.comments.deepClone();
        }
    }

    /**
     Get comments.
     @return the comments.
     */

    public CommentMutableList getComments() {
        return comments;
    }

    /**
     Set comments.
     @param c a comment list.
     */

    public void setComments(CommentMutableList list) {
        comments = list;
        if (comments != null) {
            for (int i = 0; i < comments.size(); i++) {
                comments.getComment(i).setParent(this);
            }
        }
    }

    /** Defaults to do nothing. */

    public void validate() throws ModelException {}
}
