package recodercs.list;
import recodercs.ModelElement;
import recodercs.csharp.ProgramElement;
import recodercs.csharp.statement.Branch;

/**
   List implementation based on the doubling array technique for Branch
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class BranchArrayList
     extends AbstractArrayList
  implements BranchMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public BranchArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public BranchArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public BranchArrayList(Branch[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public BranchArrayList(Branch a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected BranchArrayList(BranchArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new BranchArrayList(this);
    }

    public final void set(int index, Branch element) {
	super.set(index, element);
    }

    public final void insert(int index, Branch element) {
	super.insert(index, element);
    }

    public final void insert(int index, BranchList list) {
	super.insert(index, list);
    }

    public final void add(Branch element) {
	super.add(element);
    }

    public final void add(BranchList list) {
	super.add(list);
    }    

    public final Branch getBranch(int index) {
	return (Branch)super.get(index);
    }

    public final Branch[] toBranchArray() {
	Branch[] result = new Branch[size()];
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
