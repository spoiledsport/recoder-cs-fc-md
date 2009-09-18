package recoder.list;
import recoder.abstraction.Namespace;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type Namespace. This interface does not define
  mutators and is extended by NamespaceMutableList.
  @see NamespaceMutableList  
*/
public interface NamespaceList extends ProgramModelElementList, NamedModelElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    NamespaceList EMPTY_LIST = new NamespaceArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    Namespace getNamespace(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    Namespace[] toNamespaceArray();
    
}
