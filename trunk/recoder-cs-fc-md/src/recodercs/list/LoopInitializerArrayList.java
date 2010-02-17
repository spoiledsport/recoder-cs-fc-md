package recodercs.list;
import recodercs.ModelElement;
import recodercs.csharp.LoopInitializer;
import recodercs.csharp.ProgramElement;

/**
   List implementation based on the doubling array technique for LoopInitializer
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class LoopInitializerArrayList
     extends AbstractArrayList
  implements LoopInitializerMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public LoopInitializerArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public LoopInitializerArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public LoopInitializerArrayList(LoopInitializer[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public LoopInitializerArrayList(LoopInitializer a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected LoopInitializerArrayList(LoopInitializerArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new LoopInitializerArrayList(this);
    }

    public final void set(int index, LoopInitializer element) {
	super.set(index, element);
    }

    public final void insert(int index, LoopInitializer element) {
	super.insert(index, element);
    }

    public final void insert(int index, LoopInitializerList list) {
	super.insert(index, list);
    }

    public final void add(LoopInitializer element) {
	super.add(element);
    }

    public final void add(LoopInitializerList list) {
	super.add(list);
    }    

    public final LoopInitializer getLoopInitializer(int index) {
	return (LoopInitializer)super.get(index);
    }

    public final LoopInitializer[] toLoopInitializerArray() {
	LoopInitializer[] result = new LoopInitializer[size()];
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
