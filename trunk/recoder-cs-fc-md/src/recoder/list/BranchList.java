package recoder.list;
import recoder.csharp.statement.Branch;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type Branch. This interface does not define
  mutators and is extended by BranchMutableList.
  @see BranchMutableList  
*/
public interface BranchList extends ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    BranchList EMPTY_LIST = new BranchArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    Branch getBranch(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    Branch[] toBranchArray();
    
}
