package recoder.list;
import recoder.csharp.declaration.VariableSpecification;
import recoder.csharp.ProgramElement;
import recoder.abstraction.ProgramModelElement;
import recoder.NamedModelElement;
import recoder.ModelElement;
import recoder.abstraction.Variable;
// This file is part of the RECODER library and protected by the LGPL.

/**
   List implementation based on the doubling array technique for VariableSpecification
   elements. Tries to reduce memory usage.
   @since 0.01
   @author AL
*/
public class VariableSpecificationArrayList
     extends AbstractArrayList
  implements VariableSpecificationMutableList {

    /**
       Creates an empty list.
       @since 0.01
    */
    public VariableSpecificationArrayList() {
	super();
    }

    /**
       Creates an empty list that guarantees efficient addition of
       up to the given amount of elements.
       @since 0.01
    */
    public VariableSpecificationArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    /**
       Create a list containing all elements of the given array.       
       @since 0.01
    */
    public VariableSpecificationArrayList(VariableSpecification[] a) {
	super(a);
    }

    /**
       Creates a list with one element.
       @since 0.03
    */
    public VariableSpecificationArrayList(VariableSpecification a) {
	super(a);
    }
    
    /**
       Create a new list as a deep clone of the given one.
    */
    protected VariableSpecificationArrayList(VariableSpecificationArrayList proto) {
	super(proto);
    }
    
    public Object deepClone() {
	return new VariableSpecificationArrayList(this);
    }

    public final void set(int index, VariableSpecification element) {
	super.set(index, element);
    }

    public final void insert(int index, VariableSpecification element) {
	super.insert(index, element);
    }

    public final void insert(int index, VariableSpecificationList list) {
	super.insert(index, list);
    }

    public final void add(VariableSpecification element) {
	super.add(element);
    }

    public final void add(VariableSpecificationList list) {
	super.add(list);
    }    

    public final VariableSpecification getVariableSpecification(int index) {
	return (VariableSpecification)super.get(index);
    }

    public final VariableSpecification[] toVariableSpecificationArray() {
	VariableSpecification[] result = new VariableSpecification[size()];
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


    public final ProgramModelElement getProgramModelElement(int index) {
	return (ProgramModelElement)get(index);
    }

    public final ProgramModelElement[] toProgramModelElementArray() {
	ProgramModelElement[] result = new ProgramModelElement[size()];
	copyInto(result);
	return result;
    }


    public final NamedModelElement getNamedModelElement(int index) {
	return (NamedModelElement)get(index);
    }

    public final NamedModelElement[] toNamedModelElementArray() {
	NamedModelElement[] result = new NamedModelElement[size()];
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


    public final Variable getVariable(int index) {
	return (Variable)get(index);
    }

    public final Variable[] toVariableArray() {
	Variable[] result = new Variable[size()];
	copyInto(result);
	return result;
    }

}
