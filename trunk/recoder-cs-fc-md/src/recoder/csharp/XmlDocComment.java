// This file is part of the RECODER library and protected by the LGPL.

package recoder.csharp;

import java.util.StringTokenizer;
import recoder.*;
import recoder.list.*;
import recoder.csharp.reference.*;
import recoder.csharp.declaration.*;

/** 
 Contains tags with @-prefix and corresponding entries.
 */

public class XmlDocComment extends Comment {

    /**
     Doc comment.
     */

    public XmlDocComment() {
        super();
        setPrefixed(true);
    }

    /**
     Doc comment.
     @param text a string.
     */

    public XmlDocComment(String text) {
        super(text, true);
    }

    /**
     Doc comment.
     @param proto a doc comment.
     */

    protected XmlDocComment(XmlDocComment proto) {
        super(proto);
    }

    /**
     Deep clone.
     @return the object.
     */

    public Object deepClone() {
        return new XmlDocComment(this);
    }

    /**
       Factory method that creates a tag info object that
       can analyze this comment.       
       @return a tag info object describing the tags in this comment.
       @see recoder.csharp.TagInfo
     */

    public TagInfo createTagInfo() {
// TODO: Implement working TagInfo.
//        return new TagInfo(this);
		return null;
    }

    public void accept(SourceVisitor v) {
        v.visitDocComment(this);
    }

}
