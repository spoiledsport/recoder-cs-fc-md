package recoder.list;
import recoder.service.TreeChange;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type TreeChange. This interface does not define
  mutators and is extended by TreeChangeMutableList.
  @see TreeChangeMutableList  
*/
public interface TreeChangeList extends ObjectList {

    /**
     * An empty list.
     */
    TreeChangeList EMPTY_LIST = new TreeChangeArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    TreeChange getTreeChange(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    TreeChange[] toTreeChangeArray();
    
}
