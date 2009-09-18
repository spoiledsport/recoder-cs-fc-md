package recoder.list;
import recoder.service.TreeChange;
// This file is part of the RECODER library and protected by the LGPL.

/**
   List implementation based on the doubling array technique for TreeChange
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class TreeChangeArrayList
     extends AbstractArrayList
  implements TreeChangeMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public TreeChangeArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public TreeChangeArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public TreeChangeArrayList(TreeChange[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public TreeChangeArrayList(TreeChange a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected TreeChangeArrayList(TreeChangeArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new TreeChangeArrayList(this);
    }

    public final void set(int index, TreeChange element) {
	super.set(index, element);
    }

    public final void insert(int index, TreeChange element) {
	super.insert(index, element);
    }

    public final void insert(int index, TreeChangeList list) {
	super.insert(index, list);
    }

    public final void add(TreeChange element) {
	super.add(element);
    }

    public final void add(TreeChangeList list) {
	super.add(list);
    }    

    public final TreeChange getTreeChange(int index) {
	return (TreeChange)super.get(index);
    }

    public final TreeChange[] toTreeChangeArray() {
	TreeChange[] result = new TreeChange[size()];
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
