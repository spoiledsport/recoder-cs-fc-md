package recodercs.list;
import recodercs.csharp.Expression;

/**
  A list for objects of type Expression. This interface does not define
  mutators and is extended by ExpressionMutableList.
  @see ExpressionMutableList  
*/
public interface ExpressionList extends ProgramElementList, ModelElementList, ObjectList {

    /**
     * An empty list.
     */
    ExpressionList EMPTY_LIST = new ExpressionArrayList();

    /**
     * Retrieve an element from the given index.
     * Does not return <tt>null</tt>.
     * @param index the index of the element.
     * @return the element at the given index.
     * @exception IndexOutOfBoundsException if index &lt; 0 or 
     * index &gt;= size()
     */
    Expression getExpression(int index);

    /**
       Creates a new array containing all elements of this list.
       @return a new dense array with the contents of this list.
       @since 0.71
     */
    Expression[] toExpressionArray();
    
}
