package recoder.list;
import recoder.csharp.NamespaceSpecification;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type Using. This interface does not define
  mutators and is extended by UsingMutableList.
  @see UsingMutableList  
*/
public interface NamespaceSpecificationList extends ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    NamespaceSpecificationList EMPTY_LIST = new NamespaceSpecificationArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    NamespaceSpecification getNamespaceSpecification(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    NamespaceSpecification[] toNamespaceSpecificationArray();
    
}
