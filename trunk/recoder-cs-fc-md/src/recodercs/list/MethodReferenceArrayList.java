package recodercs.list;
import recodercs.ModelElement;
import recodercs.csharp.Expression;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.Statement;
import recodercs.csharp.reference.MemberReference;
import recodercs.csharp.reference.MethodReference;

/**
   List implementation based on the doubling array technique for MethodReference
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class MethodReferenceArrayList
     extends AbstractArrayList
  implements MethodReferenceMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public MethodReferenceArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public MethodReferenceArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public MethodReferenceArrayList(MethodReference[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public MethodReferenceArrayList(MethodReference a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected MethodReferenceArrayList(MethodReferenceArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new MethodReferenceArrayList(this);
    }

    public final void set(int index, MethodReference element) {
	super.set(index, element);
    }

    public final void insert(int index, MethodReference element) {
	super.insert(index, element);
    }

    public final void insert(int index, MethodReferenceList list) {
	super.insert(index, list);
    }

    public final void add(MethodReference element) {
	super.add(element);
    }

    public final void add(MethodReferenceList list) {
	super.add(list);
    }    

    public final MethodReference getMethodReference(int index) {
	return (MethodReference)super.get(index);
    }

    public final MethodReference[] toMethodReferenceArray() {
	MethodReference[] result = new MethodReference[size()];
	copyInto(result);
	return result;
    }


    public final Statement getStatement(int index) {
	return (Statement)get(index);
    }

    public final Statement[] toStatementArray() {
	Statement[] result = new Statement[size()];
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


    public final MemberReference getMemberReference(int index) {
	return (MemberReference)get(index);
    }

    public final MemberReference[] toMemberReferenceArray() {
	MemberReference[] result = new MemberReference[size()];
	copyInto(result);
	return result;
    }

}
