package recoder.list;
import recoder.csharp.Comment;
// This file is part of the RECODER library and protected by the LGPL.

/**
   List implementation based on the doubling array technique for Comment
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class CommentArrayList
     extends AbstractArrayList
  implements CommentMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public CommentArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public CommentArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public CommentArrayList(Comment[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public CommentArrayList(Comment a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected CommentArrayList(CommentArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new CommentArrayList(this);
    }

    public final void set(int index, Comment element) {
	super.set(index, element);
    }

    public final void insert(int index, Comment element) {
	super.insert(index, element);
    }

    public final void insert(int index, CommentList list) {
	super.insert(index, list);
    }

    public final void add(Comment element) {
	super.add(element);
    }

    public final void add(CommentList list) {
	super.add(list);
    }    

    public final Comment getComment(int index) {
	return (Comment)super.get(index);
    }

    public final Comment[] toCommentArray() {
	Comment[] result = new Comment[size()];
	copyInto(result);
	return result;
    }


    public final Object getObject(int index) {
	return (Object)get(index);
    }

    public final Object[] toObjectArray() {
	Object[] result = new Object[size()];
	copyInto(result);
	return result;
    }

}
