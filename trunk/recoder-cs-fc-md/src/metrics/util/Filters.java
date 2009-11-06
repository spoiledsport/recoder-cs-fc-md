package metrics.util;

import java.io.File;
import java.io.FilenameFilter;

import recoder.ModelElement;
import recoder.abstraction.ClassType;
import recoder.abstraction.Method;
import recoder.convenience.ModelElementFilter;

public class Filters {

	public final static ModelElementFilter METHOD_FILTER = new ModelElementFilter() {
		public boolean accept(ModelElement e) {
			if (e instanceof Method)
				return true;
			else
				return false;
		}
	};

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
