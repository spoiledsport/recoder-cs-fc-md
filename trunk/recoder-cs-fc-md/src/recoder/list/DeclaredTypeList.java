package recoder.list;
import recoder.abstraction.DeclaredType;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type DeclaredType. This interface does not define
  mutators and is extended by DeclaredTypeMutableList.
  @see DeclaredTypeMutableList  
*/
public interface DeclaredTypeList extends TypeList, MemberList, ProgramModelElementList, NamedModelElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    DeclaredTypeList EMPTY_LIST = new DeclaredTypeArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    DeclaredType getDeclaredType(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    DeclaredType[] toDeclaredTypeArray();
    
}
