package recoder.list;
import recoder.abstraction.EnumMember;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type EnumMember. This interface does not define
  mutators and is extended by EnumMemberMutableList.
  @see EnumMemberMutableList  
*/
public interface EnumMemberList extends VariableList, MemberList, ProgramModelElementList, NamedModelElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    EnumMemberList EMPTY_LIST = new EnumMemberArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    EnumMember getEnumMember(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    EnumMember[] toEnumMemberArray();
    
}
