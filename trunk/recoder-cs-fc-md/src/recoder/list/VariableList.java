package recoder.list;
import recoder.abstraction.Variable;
// This file is part of the RECODER library and protected by the LGPL.

/**
  A list for objects of type Variable. This interface does not define
  mutators and is extended by VariableMutableList.
  @see VariableMutableList  
*/
public interface VariableList extends ProgramModelElementList, NamedModelElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    VariableList EMPTY_LIST = new VariableArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    Variable getVariable(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    Variable[] toVariableArray();
    
}
