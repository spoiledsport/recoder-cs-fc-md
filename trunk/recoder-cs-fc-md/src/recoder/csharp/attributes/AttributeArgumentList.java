package recoder.csharp.attributes;
import recoder.list.ObjectList;
import recoder.list.ProgramElementList;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type DesignPattern. This interface does not define
  mutators and is extended by AttributeArgumentMutableList.
  @see AttributeArgumentMutableList  
*/
public interface AttributeArgumentList extends ProgramElementList, ObjectList {

    /**
     * An empty list.
     */
    AttributeArgumentList EMPTY_LIST = new AttributeArgumentArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    AttributeArgument getAttributeArgument(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    AttributeArgument[] toAttributeArgumentArray();
    
}
