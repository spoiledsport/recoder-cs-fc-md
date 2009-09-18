package recoder.list;
import recoder.csharp.Statement;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type Statement. This interface does not define
  mutators and is extended by StatementMutableList.
  @see StatementMutableList  
*/
public interface StatementList extends ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    StatementList EMPTY_LIST = new StatementArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    Statement getStatement(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    Statement[] toStatementArray();
    
}
