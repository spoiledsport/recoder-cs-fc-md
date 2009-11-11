package metrics.util;

import recoder.ModelElement;
import recoder.abstraction.ClassType;
import recoder.abstraction.Method;
import recoder.convenience.ModelElementFilter;

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
				if (!clazz.isAbstract()) {
					return true;
				}
			}
			return false;
		}
	};
}
