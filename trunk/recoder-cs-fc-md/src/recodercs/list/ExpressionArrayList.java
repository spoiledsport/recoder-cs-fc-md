package recodercs.list;
import recodercs.ModelElement;
import recodercs.csharp.Expression;
import recodercs.csharp.ProgramElement;

/**
   List implementation based on the doubling array technique for Expression
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class ExpressionArrayList
     extends AbstractArrayList
  implements ExpressionMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public ExpressionArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public ExpressionArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public ExpressionArrayList(Expression[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public ExpressionArrayList(Expression a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected ExpressionArrayList(ExpressionArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new ExpressionArrayList(this);
    }

    public final void set(int index, Expression element) {
	super.set(index, element);
    }

    public final void insert(int index, Expression element) {
	super.insert(index, element);
    }

    public final void insert(int index, ExpressionList list) {
	super.insert(index, list);
    }

    public final void add(Expression element) {
	super.add(element);
    }

    public final void add(ExpressionList list) {
	super.add(list);
    }    

    public final Expression getExpression(int index) {
	return (Expression)super.get(index);
    }

    public final Expression[] toExpressionArray() {
	Expression[] result = new Expression[size()];
	copyInto(result);
	return result;
    }


    public final ModelElement getModelElement(int index) {
	return (ModelElement)get(index);
    }

    public final ModelElement[] toModelElementArray() {
	ModelElement[] result = new ModelElement[size()];
	copyInto(result);
	return result;
    }


    public final Object getObject(int index) {
	return (Object)get(index);
    }

    public final Object[] toObjectArray() {
	Object[] result = new Object[size()];
	copyInto(result);
	return result;
    }


    public final ProgramElement getProgramElement(int index) {
	return (ProgramElement)get(index);
    }

    public final ProgramElement[] toProgramElementArray() {
	ProgramElement[] result = new ProgramElement[size()];
	copyInto(result);
	return result;
    }

}
