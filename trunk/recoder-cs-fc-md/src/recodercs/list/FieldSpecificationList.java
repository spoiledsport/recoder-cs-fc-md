package recodercs.list;
import recodercs.csharp.declaration.FieldSpecification;

/**
  A list for objects of type FieldSpecification. This interface does not define
  mutators and is extended by FieldSpecificationMutableList.
  @see FieldSpecificationMutableList  
*/
public interface FieldSpecificationList extends VariableSpecificationList, FieldList, VariableList, MemberList, ProgramModelElementList, NamedModelElementList, ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    FieldSpecificationList EMPTY_LIST = new FieldSpecificationArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    FieldSpecification getFieldSpecification(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    FieldSpecification[] toFieldSpecificationArray();
    
}
