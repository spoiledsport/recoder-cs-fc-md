package recodercs.list;
import recodercs.csharp.declaration.VariableSpecification;

/**
  A list for objects of type VariableSpecification. This interface does not define
  mutators and is extended by VariableSpecificationMutableList.
  @see VariableSpecificationMutableList  
*/
public interface VariableSpecificationList extends VariableList, ProgramElementList, ProgramModelElementList, NamedModelElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    VariableSpecificationList EMPTY_LIST = new VariableSpecificationArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    VariableSpecification getVariableSpecification(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    VariableSpecification[] toVariableSpecificationArray();
    
}
