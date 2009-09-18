package recoder.list;
import recoder.csharp.reference.MemberReference;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type MemberReference. This interface does not define
  mutators and is extended by MemberReferenceMutableList.
  @see MemberReferenceMutableList  
*/
public interface MemberReferenceList extends ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    MemberReferenceList EMPTY_LIST = new MemberReferenceArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    MemberReference getMemberReference(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    MemberReference[] toMemberReferenceArray();
    
}
