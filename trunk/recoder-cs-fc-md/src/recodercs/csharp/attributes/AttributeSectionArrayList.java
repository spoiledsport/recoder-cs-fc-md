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
public class AttributeSectionArrayList
     extends AbstractArrayList
  implements AttributeSectionMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public AttributeSectionArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public AttributeSectionArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public AttributeSectionArrayList(AttributeSection[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public AttributeSectionArrayList(AttributeSection a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected AttributeSectionArrayList(AttributeSectionArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new AttributeSectionArrayList(this);
    }

    public final void set(int index, AttributeSection element) {
	super.set(index, element);
    }

    public final void insert(int index, AttributeSection element) {
	super.insert(index, element);
    }

    public final void insert(int index, AttributeSectionList list) {
	super.insert(index, list);
    }

    public final void add(AttributeSection element) {
	super.add(element);
    }

    public final void add(AttributeSectionList list) {
	super.add(list);
    }    

    public final AttributeSection getAttributeSection(int index) {
	return (AttributeSection)super.get(index);
    }

    public final AttributeSection[] toAttributeSectionArray() {
	AttributeSection[] result = new AttributeSection[size()];
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
