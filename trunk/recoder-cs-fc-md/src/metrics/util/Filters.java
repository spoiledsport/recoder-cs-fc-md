package metrics.util;

import recoder.ModelElement;
import recoder.abstraction.ClassType;
import recoder.abstraction.Constructor;
import recoder.abstraction.Field;
import recoder.abstraction.Method;
import recoder.convenience.ModelElementFilter;
import recoder.csharp.declaration.GetAccessor;
import recoder.csharp.declaration.SetAccessor;
import recoder.csharp.reference.FieldReference;
import recoder.csharp.reference.MethodReference;

/**
 * This class holds a variety of filters, that are used when walking a tree of
 * ProgramElemnts
 * 
 * @author Jan Schumacher, jansch@gmail.com
 */
public class Filters {

	/**
	 * returns true, if ModelElement is a Method
	 */
	public final static ModelElementFilter ACCESSOR_PUBLIC_GETTERSETTER_FILTER_EXCL_CONSTR_STATIC_ABSTR = new ModelElementFilter() {
		public boolean accept(ModelElement e) {
			if (e instanceof Method) {
				Method m = (Method) e;
				if (MetricUtils.isGetterSetterSimple(m) && m.isPublic()
						&& !(m instanceof Constructor) && !m.isStatic()
						&& !m.isAbstract()) {
					return true;
				}
				return false;
			} else if (e instanceof GetAccessor || e instanceof SetAccessor) {
				return true;
			} else
				return false;
		}
	};

	/**
	 * returns true, if ModelElement is a Method
	 */
	public final static ModelElementFilter METHOD_FILTER = new ModelElementFilter() {
		public boolean accept(ModelElement e) {
			if (e instanceof Method)
				return true;
			else
				return false;
		}
	};

	/**
	 * returns true, if ModelElement is a Method, that is not abstract
	 */
	public final static ModelElementFilter METHOD_FILTER_EXCL_ABSTRACT = new ModelElementFilter() {
		public boolean accept(ModelElement e) {
			if (e instanceof Method) {
				Method m = (Method) e;
				if (!m.isAbstract())
					return true;
				return false;
			}
			return false;
		}
	};

	/**
	 * returns true, if ModelElement is a FieldReference, that is not abstract
	 */
	public final static ModelElementFilter FIELDREFERENCE_FILTER = new ModelElementFilter() {
		public boolean accept(ModelElement e) {
			if (e instanceof FieldReference) {
				return true;
			}
			return false;
		}
	};

	/**
	 * returns true, if ModelElement is a FieldReference, or a Methodreference
	 */
	public final static ModelElementFilter FIELDREFERENCE_GETTERSETTER_FILTER = new ModelElementFilter() {
		public boolean accept(ModelElement e) {
			// a FieldReference
			if (e instanceof FieldReference) {
				return true;
				// a MethodReference
			} else if (e instanceof MethodReference) {
				return true;
			} else {
				return false;
			}
		}
	};

	/**
	 * returns true, if ModelElement is a ClassType that is not abstract nore an
	 * Interface
	 */
	public final static ModelElementFilter CLASS_FILTER_EXCL_ABSTRACT_INTERFACE = new ModelElementFilter() {
		public boolean accept(ModelElement e) {
			if (e instanceof ClassType) {
				ClassType clazz = (ClassType) e;
				if (!clazz.isAbstract() && !clazz.isInterface()) {
					return true;
				}
			}
			return false;
		}
	};

	public final static ModelElementFilter CLASS_FILTER_EXCL_INTERFACE = new ModelElementFilter() {
		public boolean accept(ModelElement e) {
			if (e instanceof ClassType) {
				ClassType clazz = (ClassType) e;
				if (!clazz.isInterface()) {
					return true;
				}
			}
			return false;
		}
	};

	/**
	 * returns true, if ModelElement is a public Field, that is not static,
	 * constant
	 */
	public final static ModelElementFilter PUBLIC_FIELD_FILTER_EXCL_STATIC_CONSTANT = new ModelElementFilter() {
		public boolean accept(ModelElement e) {
			if (e instanceof Field) {
				Field a = (Field) e;
				if (a.isPublic() && !a.isStatic() && !a.isReadOnly())
					return true;
				return false;
			}
			return false;
		}
	};
}
