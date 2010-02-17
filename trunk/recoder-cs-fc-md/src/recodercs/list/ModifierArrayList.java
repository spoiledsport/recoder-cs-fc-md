package recodercs.list;
import recodercs.ModelElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.declaration.Modifier;

/**
   List implementation based on the doubling array technique for Modifier
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class ModifierArrayList
     extends AbstractArrayList
  implements ModifierMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public ModifierArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public ModifierArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public ModifierArrayList(Modifier[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public ModifierArrayList(Modifier a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected ModifierArrayList(ModifierArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new ModifierArrayList(this);
    }

    public final void set(int index, Modifier element) {
	super.set(index, element);
    }

    public final void insert(int index, Modifier element) {
	super.insert(index, element);
    }

    public final void insert(int index, ModifierList list) {
	super.insert(index, list);
    }

    public final void add(Modifier element) {
	super.add(element);
    }

    public final void add(ModifierList list) {
	super.add(list);
    }    

    public final Modifier getModifier(int index) {
	return (Modifier)super.get(index);
    }

    public final Modifier[] toModifierArray() {
	Modifier[] result = new Modifier[size()];
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
