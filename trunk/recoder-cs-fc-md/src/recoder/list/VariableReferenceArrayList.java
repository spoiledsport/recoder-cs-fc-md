package recoder.list;
import recoder.csharp.reference.VariableReference;
import recoder.csharp.ProgramElement;
import recoder.ModelElement;
import recoder.csharp.Expression;
// This file is part of the RECODER library and protected by the LGPL.

/**
   List implementation based on the doubling array technique for VariableReference
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class VariableReferenceArrayList
     extends AbstractArrayList
  implements VariableReferenceMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public VariableReferenceArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public VariableReferenceArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public VariableReferenceArrayList(VariableReference[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public VariableReferenceArrayList(VariableReference a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected VariableReferenceArrayList(VariableReferenceArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new VariableReferenceArrayList(this);
    }

    public final void set(int index, VariableReference element) {
	super.set(index, element);
    }

    public final void insert(int index, VariableReference element) {
	super.insert(index, element);
    }

    public final void insert(int index, VariableReferenceList list) {
	super.insert(index, list);
    }

    public final void add(VariableReference element) {
	super.add(element);
    }

    public final void add(VariableReferenceList list) {
	super.add(list);
    }    

    public final VariableReference getVariableReference(int index) {
	return (VariableReference)super.get(index);
    }

    public final VariableReference[] toVariableReferenceArray() {
	VariableReference[] result = new VariableReference[size()];
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


    public final Expression getExpression(int index) {
	return (Expression)get(index);
    }

    public final Expression[] toExpressionArray() {
	Expression[] result = new Expression[size()];
	copyInto(result);
	return result;
    }

}
