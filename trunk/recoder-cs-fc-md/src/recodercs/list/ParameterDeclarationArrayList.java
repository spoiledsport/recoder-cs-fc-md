package recodercs.list;
import recodercs.ModelElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.declaration.ParameterDeclaration;

/**
   List implementation based on the doubling array technique for ParameterDeclaration
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class ParameterDeclarationArrayList
     extends AbstractArrayList
  implements ParameterDeclarationMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public ParameterDeclarationArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public ParameterDeclarationArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public ParameterDeclarationArrayList(ParameterDeclaration[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public ParameterDeclarationArrayList(ParameterDeclaration a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected ParameterDeclarationArrayList(ParameterDeclarationArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new ParameterDeclarationArrayList(this);
    }

    public final void set(int index, ParameterDeclaration element) {
	super.set(index, element);
    }

    public final void insert(int index, ParameterDeclaration element) {
	super.insert(index, element);
    }

    public final void insert(int index, ParameterDeclarationList list) {
	super.insert(index, list);
    }

    public final void add(ParameterDeclaration element) {
	super.add(element);
    }

    public final void add(ParameterDeclarationList list) {
	super.add(list);
    }    

    public final ParameterDeclaration getParameterDeclaration(int index) {
	return (ParameterDeclaration)super.get(index);
    }

    public final ParameterDeclaration[] toParameterDeclarationArray() {
	ParameterDeclaration[] result = new ParameterDeclaration[size()];
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
