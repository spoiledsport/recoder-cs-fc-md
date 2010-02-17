package recodercs.csharp.attributes;
import recodercs.ModelElement;
import recodercs.csharp.ProgramElement;
import recodercs.list.AbstractArrayList;

/**
   List implementation based on the doubling array technique for DesignPattern
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class AttributeArgumentArrayList
     extends AbstractArrayList
  implements AttributeArgumentMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public AttributeArgumentArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public AttributeArgumentArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public AttributeArgumentArrayList(AttributeArgument[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public AttributeArgumentArrayList(AttributeArgument a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected AttributeArgumentArrayList(AttributeArgumentArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new AttributeArgumentArrayList(this);
    }

    public final void set(int index, AttributeArgument element) {
	super.set(index, element);
    }

    public final void insert(int index, AttributeArgument element) {
	super.insert(index, element);
    }

    public final void insert(int index, AttributeArgumentList list) {
	super.insert(index, list);
    }

    public final void add(AttributeArgument element) {
	super.add(element);
    }

    public final void add(AttributeArgumentList list) {
	super.add(list);
    }    

    public final AttributeArgument getAttributeArgument(int index) {
	return (AttributeArgument)super.get(index);
    }

    public final AttributeArgument[] toAttributeArgumentArray() {
	AttributeArgument[] result = new AttributeArgument[size()];
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


    public final ModelElement getModelElement(int index) {
	return (ModelElement)get(index);
    }

    public final ModelElement[] toModelElementArray() {
	ModelElement[] result = new ModelElement[size()];
	copyInto(result);
	return result;
    }

	/**
	 * @see recodercs.list.ProgramElementList#getProgramElement(int)
	 */
	public ProgramElement getProgramElement(int index) {
		return (ProgramElement)get(index);
	}

	/**
	 * @see recodercs.list.ProgramElementList#toProgramElementArray()
	 */
	public ProgramElement[] toProgramElementArray() {
	ProgramElement[] result = new ProgramElement[size()];
	copyInto(result);
	return result;
	}

}
