package recoder.list;
import recoder.csharp.reference.UncollatedReferenceQualifier;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type UncollatedReferenceQualifier. This interface does not define
  mutators and is extended by UncollatedReferenceQualifierMutableList.
  @see UncollatedReferenceQualifierMutableList  
*/
public interface UncollatedReferenceQualifierList extends ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    UncollatedReferenceQualifierList EMPTY_LIST = new UncollatedReferenceQualifierArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    UncollatedReferenceQualifier getUncollatedReferenceQualifier(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    UncollatedReferenceQualifier[] toUncollatedReferenceQualifierArray();
    
}
