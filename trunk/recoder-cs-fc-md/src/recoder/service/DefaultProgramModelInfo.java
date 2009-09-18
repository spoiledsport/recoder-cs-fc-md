// This file is part of the RECODER library and protected by the LGPL.

package recoder.service;

import java.util.Enumeration;

import recoder.*;
import recoder.abstraction.*;
import recoder.abstraction.Namespace;
import recoder.convenience.Format;
import recoder.util.*;
import recoder.list.*;

public abstract class DefaultProgramModelInfo
	extends AbstractService
	implements ProgramModelInfo {

	static class ClassTypeCacheEntry {
		ClassTypeList supertypes; // used in specialized services only
		MutableSet subtypes;
		ClassTypeList allSupertypes;
		DeclaredTypeList allMemberTypes;
		FieldList allFields;
		MethodList allMethods;
	}

	final MutableMap classTypeCache = new IdentityHashTable(256);
	// <ClassType, ClassTypeCacheEntry>

	/**
	@param config the configuration this services becomes part of.
	*/
	protected DefaultProgramModelInfo(ServiceConfiguration config) {
		super(config);
	}

	final ChangeHistory getChangeHistory() {
		return serviceConfiguration.getChangeHistory();
	}

	ErrorHandler getErrorHandler() {
		return serviceConfiguration.getProjectSettings().getErrorHandler();
	}

	final NameInfo getNameInfo() {
		return serviceConfiguration.getNameInfo();
	}

	final void updateModel() {
		getChangeHistory().updateModel();
	}

	/**
	   Internally used to register a subtype link.
	 */
	protected void registerSubtype(ClassType subtype, ClassType supertype) {
		ProgramModelInfo pmi = supertype.getProgramModelInfo();
		if (pmi != this) {
			((DefaultProgramModelInfo) pmi).registerSubtype(subtype, supertype);
		}
		ClassTypeCacheEntry ctce = (ClassTypeCacheEntry) classTypeCache.get(supertype);
		if (ctce == null) {
			classTypeCache.put(supertype, ctce = new ClassTypeCacheEntry());
		}
		if (ctce.subtypes == null) {
			ctce.subtypes = new IdentityHashSet();
		}
		ctce.subtypes.add(subtype);
	}

	/**
	   Internally used to remove a subtype link.
	 */
	protected void removeSubtype(ClassType subtype, ClassType supertype) {
		ProgramModelInfo pmi = supertype.getProgramModelInfo();
		if (pmi != this) {
			((DefaultProgramModelInfo) pmi).registerSubtype(subtype, supertype);
		}
		ClassTypeCacheEntry ctce = (ClassTypeCacheEntry) classTypeCache.get(supertype);
		if (ctce != null) {
			if (ctce.subtypes != null) {
				ctce.subtypes.remove(subtype);
			}
		}
	}

	/** @see recoder.service.ProgramModelInfo#getSubtypes(ClassType)
	 */
	public ClassTypeList getSubtypes(ClassType ct) {
		Debug.asserta(ct);
		updateModel();
		ProgramModelInfo pmi = ct.getProgramModelInfo();
		if (pmi != this) {
			Debug.asserta(pmi);
			return pmi.getSubtypes(ct);
		}
		ClassTypeCacheEntry ctce = (ClassTypeCacheEntry) classTypeCache.get(ct);
		if (ctce == null) {
			classTypeCache.put(ct, ctce = new ClassTypeCacheEntry());
		}
		if (ctce.subtypes == null) {
			return ClassTypeList.EMPTY_LIST;
		}
		int s = ctce.subtypes.size();
		ClassTypeMutableList result = new ClassTypeArrayList(s);
		Enumeration e = ctce.subtypes.elements();
		for (int i = 0; i < s; i++) {
			result.add((ClassType) e.nextElement());
		}
		return result;
	}

	/** @see recoder.service.ProgramModelInfo#getAllSubtypes(ClassType)
	 */
	public ClassTypeList getAllSubtypes(ClassType ct) {
		updateModel();
		ClassTypeMutableList ctl = new SubTypeTopSort().getAllTypes(ct);
		// begin at second entry - the top sort includes the input class
		ctl.remove(0);
		return ctl;
	}

	/** @see recoder.service.ProgramModelInfo#getAllSupertypes(ClassType)
	 */
	public ClassTypeList getAllSupertypes(ClassType ct) {
		updateModel();
		ProgramModelInfo pmi = ct.getProgramModelInfo();
		if (pmi != this) {
			Debug.asserta(pmi);
			return pmi.getAllSupertypes(ct);
		}
		ClassTypeCacheEntry ctce = (ClassTypeCacheEntry) classTypeCache.get(ct);
		if (ctce == null) {
			classTypeCache.put(ct, ctce = new ClassTypeCacheEntry());
		}
		if (ctce.allSupertypes == null) {
			computeAllSupertypes(ct, ctce);
		}
		return ctce.allSupertypes;
	}

	/** Internal method to compute all superclasses of a class. 
	 */
	private void computeAllSupertypes(ClassType ct, ClassTypeCacheEntry ctce) {
		ctce.allSupertypes = new SuperTypeTopSort().getAllTypes(ct);
	}

	/** @see recoder.service.ProgramModelInfo#getAllFields(ClassType)
	 */
	public FieldList getAllFields(ClassType ct) {
		updateModel();
		ProgramModelInfo pmi = ct.getProgramModelInfo();
		if (pmi != this) {
			Debug.asserta(pmi);
			return pmi.getAllFields(ct);
		}
		ClassTypeCacheEntry ctce = (ClassTypeCacheEntry) classTypeCache.get(ct);
		if (ctce == null) {
			classTypeCache.put(ct, ctce = new ClassTypeCacheEntry());
		}
		if (ctce.allFields == null) {
			computeAllFields(ct, ctce);
		}
		return ctce.allFields;
	}

	/** Internal method to compute all fields.
	 */
	private void computeAllFields(ClassType ct, ClassTypeCacheEntry ctce) {
		if (ctce.allSupertypes == null) {
			computeAllSupertypes(ct, ctce);
		}
		ClassTypeList classes = ctce.allSupertypes;
		// if (classes == null) return null;
		int s = classes.size();
		FieldMutableList result = new FieldArrayList(s * 4); // simple heuristic
		int result_size = 0;
		for (int i = 0; i < s; i++) {
			ClassType c = classes.getClassType(i);
			FieldList fl = c.getFields();
			if (fl == null) {
				continue;
			}
			int fs = fl.size();
			add_fields : for (int j = 0; j < fs; j++) {
				Field f = fl.getField(j);
				if (isVisibleFor(f, ct)) {
					String fname = f.getName();
					for (int k = 0; k < result_size; k++) {
						Field rf = result.getField(k);
						if (rf.getName() == fname) {
							continue add_fields;
						}
					}
					result.add(f);
					result_size++;
				}
			}
		}
		result.trim();
		ctce.allFields = result;
	}

	/** @see recoder.service.ProgramModelInfo#getAllMethods(ClassType)
	 */
	public MethodList getAllMethods(ClassType ct) {
		updateModel();
		ProgramModelInfo pmi = ct.getProgramModelInfo();
		if (pmi != this) {
			Debug.asserta(pmi);
			return pmi.getAllMethods(ct);
		}
		ClassTypeCacheEntry ctce = (ClassTypeCacheEntry) classTypeCache.get(ct);
		if (ctce == null) {
			classTypeCache.put(ct, ctce = new ClassTypeCacheEntry());
		}
		if (ctce.allMethods == null) {
			computeAllMethods(ct, ctce);
		}
		return ctce.allMethods;
	}

	/** Internal method to compute all methods of the given class
	 */
	private void computeAllMethods(ClassType ct, ClassTypeCacheEntry ctce) {
		if (ctce.allSupertypes == null) {
			computeAllSupertypes(ct, ctce);
		}
		ClassTypeList classes = ctce.allSupertypes;
		int s = classes.size();
		MethodMutableList result = new MethodArrayList(s * 8);

		int result_size = 0;
		for (int i = 0; i < s; i++) {
			ClassType c = classes.getClassType(i);
			MethodList ml = c.getMethods();
			if (ml == null) {
				continue;
			}
			int ms = ml.size();
			add_methods : for (int j = 0; j < ms; j++) {
				Method m = ml.getMethod(j);
				if (isVisibleFor(m, ct)) {
					TypeList msig = m.getSignature();
					String mname = m.getName();
					for (int k = 0; k < result_size; k++) {
						Method rm = result.getMethod(k);
						if (rm.getName() == mname) {
							TypeList rsig = rm.getSignature();
							if (rsig.equals(msig)) {
								// skip this method: we already had it
								continue add_methods;
							}
						}
					}
					result.add(m);
					result_size++;
				}
			}
		}
		result.trim();
		ctce.allMethods = result;
	}

	/** Returns all types, declared and visible in the class type. 
	 */
	public DeclaredTypeList getAllTypes(ClassType ct) {
		updateModel();
		ProgramModelInfo pmi = ct.getProgramModelInfo();
		if (pmi != this) {
			Debug.asserta(pmi);
			return pmi.getAllTypes(ct);
		}

		ClassTypeCacheEntry ctce = (ClassTypeCacheEntry) classTypeCache.get(ct);
		if (ctce == null) {
			classTypeCache.put(ct, ctce = new ClassTypeCacheEntry());
		}

		if (ctce.allMemberTypes == null) {
			computeAllMemberTypes(ct, ctce);
		}

		return ctce.allMemberTypes;
	}

	/** Internal method to compute all member types.
	 */
	private void computeAllMemberTypes(ClassType ct, ClassTypeCacheEntry ctce) {
		if (ctce.allSupertypes == null) {
			computeAllSupertypes(ct, ctce);
		}
		ClassTypeList classes = ctce.allSupertypes;
		int s = classes.size();
		DeclaredTypeMutableList result = new DeclaredTypeArrayList(s);
		int result_size = 0;
		for (int i = 0; i < s; i++) {
			ClassType c = classes.getClassType(i);
			DeclaredTypeList cl = c.getDeclaredTypes();
			if (cl == null) {
				continue;
			}
			int cs = cl.size();
			add_ClassTypes : for (int j = 0; j < cs; j++) {
				DeclaredType hc = cl.getDeclaredType(j);
				if ((hc != ct) && isVisibleFor(hc, ct)) {
					String cname = hc.getName();
					for (int k = 0; k < result_size; k++) {
						DeclaredType rt = result.getDeclaredType(k);
						if (rt instanceof ClassType) {
							ClassType rc = (ClassType) rt;
							if (rc.getName() == cname) {
								continue add_ClassTypes;
							}
						}
					}
					result.add(hc);
					result_size++;
				}
			}
		}

		result.trim();
		ctce.allMemberTypes = result;

	}

	/** Internal helper class for sorting super and subtypes.
	 */
	static class SuperTypeTopSort extends ClassTypeTopSort {

		protected final ClassTypeList getAdjacent(ClassType c) {
			return c.getSupertypes();
		}
	}

	/** Internal class fot sorting subtypes of a given class.
	 */
	class SubTypeTopSort extends ClassTypeTopSort {

		protected final ClassTypeList getAdjacent(ClassType c) {
			return getSubtypes(c);
		}
	}

	/** @see recoder.service.ProgramModelInfo#getPromotedType(PrimitiveType, PrimitiveType)
	 */
	public PrimitiveType getPromotedType(PrimitiveType a, PrimitiveType b) {
		// Has been changed according to the CSHARP spec...		

		if (a == b) {
			return a;
		}

		NameInfo ni = getNameInfo();

		if (a == ni.getBooleanType() || b == ni.getBooleanType()) {
			return null;
		}

		if (a == ni.getDecimalType() || b == ni.getDecimalType()) {
			if (a == ni.getFloatType()
				|| b == ni.getFloatType()
				|| a == ni.getDoubleType()
				|| b == ni.getDoubleType())
				return null;
			return ni.getDecimalType();

		}

		if (a == ni.getDoubleType() || b == ni.getDoubleType()) {
			return ni.getDoubleType();
		}

		if (a == ni.getFloatType() || b == ni.getFloatType()) {
			return ni.getFloatType();
		}

		if (a == ni.getUlongType() || b == ni.getUlongType()) {
			if (a == ni.getSbyteType()
				|| b == ni.getSbyteType()
				|| a == ni.getShortType()
				|| b == ni.getShortType()
				|| a == ni.getIntType()
				|| b == ni.getIntType()
				|| a == ni.getLongType()
				|| b == ni.getLongType())
				return null;
			return ni.getUlongType();
		}

		if (a == ni.getLongType() || b == ni.getLongType()) {
			return ni.getLongType();
		}

		if (a == ni.getUintType() || b == ni.getUintType()) {
			if (a == ni.getSbyteType()
				|| b == ni.getSbyteType()
				|| a == ni.getShortType()
				|| b == ni.getShortType()
				|| a == ni.getIntType()
				|| b == ni.getIntType())
				return ni.getLongType();

			return ni.getUintType();
		}

		return ni.getIntType();
	}

	/** @see recoder.service.ProgramModelInfo#isWidening(PrimitiveType, PrimitiveType)
	 */
	public boolean isWidening(PrimitiveType from, PrimitiveType to) {
		// NOTE: This is not yet updated to the C# spec...
		// Should be done...
		

		// we do not handle null's
		if (from == null || to == null)
			return false;
		// equal types can be coerced
		if (from == to)
			return true;
		NameInfo ni = getNameInfo();
		// boolean types cannot be coerced into something else
		if (from == ni.getBooleanType() || to == ni.getBooleanType())
			return false;
//		// everything else can be coerced to a double
//		if (to == ni.getDoubleType())
//			return true;
//		// but a double cannot be coerced to anything else
//		if (from == ni.getDoubleType())
//			return false;
//		// everything except doubles can be coerced to a float
//		if (to == ni.getFloatType())
//			return true;
//		// but a float cannot be coerced to anything but float or double
//		if (from == ni.getFloatType())
//			return false;
//		// everything except float or double can be coerced to a long
//		if (to == ni.getLongType())
//			return true;
//		// but a long cannot be coerced to anything but float, double or long
//		if (from == ni.getLongType())
//			return false;
//		// everything except long, float or double can be coerced to an int
//		if (to == ni.getIntType())
//			return true;
//		// but an int cannot be coerced to the remaining byte, char, short
//		if (from == ni.getIntType())
//			return false;
//		// between byte, char, short, only one conversion is admissible
//		return (from == ni.getByteType() && to == ni.getShortType());
		// Double only with double
		if (from == ni.getDoubleType() && to != ni.getDoubleType()) {
			return false;
		}
		if (from == ni.getDecimalType() && to != ni.getDecimalType()) {
			return false;
		}
		if (from == ni.getSbyteType()) {
			if (to == ni.getShortType()
			|| to == ni.getIntType()
			|| to == ni.getLongType()
			|| to == ni.getFloatType()
			|| to == ni.getDoubleType()
			|| to == ni.getDecimalType()) {
				return true;
			} else {
				return false;
			}
		}
		if (from == ni.getByteType()) {
			if (to == ni.getShortType()
			|| to == ni.getIntType()
			|| to == ni.getLongType()
			|| to == ni.getUshortType()
			|| to == ni.getUintType()
			|| to == ni.getUlongType()
			|| to == ni.getFloatType()
			|| to == ni.getDoubleType()
			|| to == ni.getDecimalType()) {
				return true;
			} else {
				return false;
			}
		}
		if (from == ni.getShortType()) {
			if (to == ni.getIntType()
			|| to == ni.getLongType()
			|| to == ni.getFloatType()
			|| to == ni.getDoubleType()
			|| to == ni.getDecimalType()) {
				return true;
			} else {
				return false;
			}
		}
		if (from == ni.getUshortType()) {
			if (to == ni.getIntType()
			|| to == ni.getLongType()
			|| to == ni.getUintType()
			|| to == ni.getUlongType()
			|| to == ni.getFloatType()
			|| to == ni.getDoubleType()
			|| to == ni.getDecimalType()) {
				return true;
			} else {
				return false;
			}
		}
		if (from == ni.getIntType()) {
			if (to == ni.getLongType()
			|| to == ni.getFloatType()
			|| to == ni.getDoubleType()
			|| to == ni.getDecimalType()) {
				return true;
			} else {
				return false;
			}
		}
		if (from == ni.getUintType()) {
			if (to == ni.getLongType()
			|| to == ni.getUlongType()
			|| to == ni.getFloatType()
			|| to == ni.getDoubleType()
			|| to == ni.getDecimalType()) {
				return true;
			} else {
				return false;
			}
		}
		if (from == ni.getLongType()) {
			if (to == ni.getDecimalType()) {
				return true;
			} else {
				return false;
			}
		}
		if (from == ni.getUlongType()) {
			if (to == ni.getDecimalType()) {
				return true;
			} else {
				return false;
			}
		}
		if (from == ni.getFloatType()) {
			if (to == ni.getDoubleType()) {
				return true;
			} else {
				return false;
			}
		}
		
		return false;				

	}

	/** @see recoder.service.ProgramModelInfo#isWidening(ClassType, ClassType)
	 */
	public boolean isWidening(ClassType from, ClassType to) {
		return isSubtype(from, to);
	}

	/** @see recoder.service.ProgramModelInfo#isWidening(ArrayType, ArrayType)
	 */
	public boolean isWidening(ArrayType from, ArrayType to) {
		Type toBase = to.getBaseType();
		if (toBase == getNameInfo().getSystemObject()) {
			return true;
		}
		Type fromBase = from.getBaseType();
		if (toBase instanceof PrimitiveType) {
			return toBase.equals(fromBase);
		}
		return isWidening(fromBase, toBase);
	}

	/** 
	 * @see ProgramModelInfo#isWidening(Type, Type)
	 * @returns true, if the conversion is widening, or if to == null
	 */
	///// if to == null, returns true (for sake of conveniency)
	public boolean isWidening(Type from, Type to) {

		// TODO: Think this over once again, cosidering C# spec...

		if (from instanceof ClassType) {
			if (to instanceof ClassType) {
				return isWidening((ClassType) from, (ClassType) to);
			} else if ((from instanceof NullType) && (to instanceof ArrayType)) {
				return true;
			}
		} else if (from instanceof PrimitiveType) {
			if (to instanceof PrimitiveType) {
				return isWidening((PrimitiveType) from, (PrimitiveType) to);
			}
		} else if (from instanceof ArrayType) {
			if (to instanceof ClassType) {
				NameInfo ni = getNameInfo();
				if (to == ni.getSystemObject()) {
					return true;
				}
				if (to == ni.getSystemICloneable()) {
					return true;
				}
				if (to == ni.getJavaIoSerializable()) {
					return true;
				}
				return false;
			} else if (to instanceof ArrayType) {
				return isWidening((ArrayType) from, (ArrayType) to);
			}
		}
		return false;
	}

	/** @see ProgramModelInfo#isSubtype(ClassType, ClassType)
	 */
	public boolean isSubtype(ClassType a, ClassType b) {
		boolean result = false;
		if ((a != null) && (b != null)) {
			if ((a == b)
				|| (a == getNameInfo().getNullType())
				|| (b == getNameInfo().getSystemObject())) {
				result = true;
			} else {
				// Optimization by non-recursive bfs possible!!!
				ClassTypeList superA = a.getSupertypes();
				if (superA != null) {
					int s = superA.size();
					for (int i = 0;(i < s) && !result; i++) {
						ClassType sa = superA.getClassType(i);
						if (sa == a) {
							getErrorHandler().reportError(
								new CyclicInheritanceException(a));
						}
						if (isSubtype(sa, b)) {
							result = true;
						}
					}
				}
			}
		}
		return result;
	}

	/** @see ProgramModelInfo#isSupertype(ClassType, ClassType)
	 */
	public boolean isSupertype(ClassType a, ClassType b) {
		return isSubtype(b, a);
	}

	/** @see recoder.service.ProgramModelInfo#isCompatibleSignature(TypeList, TypeList)
	 */
	public final boolean isCompatibleSignature(TypeList a, TypeList b) {

		// TODO: Think of this once again considering C#'s parameter arrays

		int s = a.size();
		if (s != b.size()) {
			return false;
		}
		for (int i = 0; i < s; i += 1) {
			Type ta = a.getType(i);
			Type tb = b.getType(i);
			if (ta != null && !isWidening(ta, tb)) {
				return false;
			}
		}
		return true;
	}

	/** Returns the outermost type of a given type.
	 */
	protected DeclaredType getOutermostType(DeclaredType t) {
		DeclaredType c = t;
		DeclaredTypeContainer cc = t.getContainer();

		while (cc != null && (cc instanceof ClassType)) {
			c = (ClassType) cc;
			cc = ((ClassType) cc).getContainer();
		}

		return c;

	}

	/** @see recoder.service.ProgramModelInfo#isVisibleFor(Member, DeclaredType)
	 */
	public boolean isVisibleFor(Member m, DeclaredType t) {
		if (m.isPublic()) {
			// public members are always visible
			return true;
		}

		ClassType mt = m.getContainingClassType();

		if (mt == null) {
			// a classless member is not visible
			return false;
		}

		if (mt == t) {
			// all members are visible to their own class
			return true;
		}

		if (m.isProtected()) {
			// Protected members are only visible to the members of the same class
			if (t instanceof ClassType && isSubtype((ClassType) t, mt)) {
				// protected members are visible to subtypes
				return true;
			}
		}

		if (m.isInternal()) {
			// TODO: Add visibility handling for "internal" (how?)

			System.err.println(
				"WARNING: Member has 'internal' access, which is still not supported.");
			System.err.println("         Member will be considered as public.");

			return true;

		}

		if (m.isPrivate()) {
			// private members are only visible to members that share
			// an outer type
			return getOutermostType(t) == getOutermostType(mt);
		}

		// DEFAULT VISIBILITY

		if (mt.getNamespace() == t.getNamespace()) {
			// non-private members are visible to their own package
			return true;
		}

		// all others are not visible
		return false;
	}

	/** @see recoder.service.ProgramModelInfo#filterApplicableMethods(MethodMutableList, String, TypeList, ClassType)
	 */
	public void filterApplicableMethods(
		MethodMutableList list,
		String name,
		TypeList signature,
		ClassType context) {
		Debug.asserta(name, signature, context);

		// the following looks complicated but it pays off

		// for the weak minded people: it looks for the first non-matching method
		// and then it copies all matching ones behind this one (instead of removing
		// each element one by one)

		int s = list.size();
		int i = 0;
		while (i < s) {
			Method m = list.getMethod(i);
			if (!name.equals(m.getName())
				|| !isCompatibleSignature(signature, m.getSignature())
				|| !isVisibleFor(m, context)) {
				break;
			} else {
				i += 1;
			}
		}
		// if no element has been rejected, we are done
		if (i < s) {
			int j = i;
			for (i += 1; i < s; i += 1) {
				Method m = list.getMethod(i);
				if (name.equals(m.getName())
					&& isCompatibleSignature(signature, m.getSignature())
					&& isVisibleFor(m, context)) {
					list.set(j, m);
					j += 1;
				}
			}
			list.removeRange(j);
		}
	}

	/** @see recoder.service.ProgramModelInfo#filterMostSpecificMethods(MethodMutableList)
	 */
	public void filterMostSpecificMethods(MethodMutableList list) {
		int size = list.size();
		if (size <= 1) {
			return;
		}
		// cache signatures (avoid multiple allocations)
		TypeList[] signatures = new TypeList[size];
		signatures[0] = list.getMethod(0).getSignature();
		// size should not be very large - using a naive n² algorithm
		// signatures/methods to be removed are marked as null
		for (int i = 1; i < size; i += 1) {
			TypeList sig = signatures[i] = list.getMethod(i).getSignature();
			if (sig != null) {
				for (int j = i - 1; j >= 0; j -= 1) {
					TypeList sig2 = signatures[j];
					if (sig2 != null) {
						if (isCompatibleSignature(sig2, sig)) {
							signatures[i] = null;
						} else if (isCompatibleSignature(sig, sig2)) {
							signatures[j] = null;
							break;
						}
					}
				}
			}
		}
		// do the cleanup work - remove all less specific methods
		int k = 0;
		for (int i = size - 1; i >= 0; i -= 1) {
			if (signatures[i] == null) {
				k += 1;
			} else if (k > 0) {
				list.removeRange(i + 1, i + k + 1);
				k = 0;
			}
		}
		if (k > 0) {
			list.removeRange(0, k);
		}
	}

	/** @see ProgramModelInfo#getConstructors(ClassType, TypeList)
	 */
	public ConstructorList getConstructors(ClassType ct, TypeList signature) {
		Debug.asserta(ct, signature);
		if (ct.isInterface()) {
			//			if (signature.isEmpty()) {
			//				// Fake: yield java.lang.Object()
			//				return getNameInfo().getSystemObject().getConstructors();
			//			}
			return ConstructorList.EMPTY_LIST;
		}

		ConstructorList list = ct.getConstructors(); // only local ones!!!
		MethodMutableList meths = new MethodArrayList();
		meths.add(list);
		String name = ct.getName();
		name = name.substring(name.lastIndexOf('.') + 1);
		filterApplicableMethods(meths, name, signature, ct);
		filterMostSpecificMethods(meths);
		ConstructorMutableList result = new ConstructorArrayList();
		for (int i = 0, s = meths.size(); i < s; i += 1) {
			result.add((Constructor) meths.getMethod(i));
		}
		return result;
	}

	/** @see recoder.service.ProgramModelInfo#getMethods(ClassType)
	 */
	public MethodList getMethods(ClassType ct, String name, TypeList signature) {
		Debug.asserta(ct, name, signature);
		MethodList result = null;
		MethodMutableList allMeths = new MethodArrayList();
		allMeths.add(ct.getAllMethods());
		filterApplicableMethods(allMeths, name, signature, ct);
		filterMostSpecificMethods(allMeths);
		result = allMeths;
		return result;
	}

	public void reset() {
		// it would be possible to reuse cache entry objects by
		// iterating over the cache and erasing all cached lists only.
		// however, whole class types might have vanished and their entries
		// have to vanish, too, so there is little choice
		classTypeCache.clear();
	}
	
///////////////////// CSHARP EXTENSIONS ////////////////////////

	public boolean isCompatibleMethod(Method m, Delegate d) {
		
		return m.getReturnType() == d.getReturnType() && isCompatibleSignature(m.getSignature(),d.getSignature());
	}

}
