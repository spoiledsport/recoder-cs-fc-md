// This file is part of the RECODER library and protected by the LGPL

package recodercs.kit;

import java.util.Stack;

import recodercs.*;
import recodercs.abstraction.*;
import recodercs.convenience.*;
import recodercs.csharp.*;
import recodercs.csharp.declaration.*;
import recodercs.csharp.declaration.modifier.*;
import recodercs.csharp.declaration.modifier.Override;
import recodercs.csharp.expression.*;
import recodercs.csharp.expression.operator.*;
import recodercs.csharp.reference.*;
import recodercs.csharp.statement.*;
import recodercs.io.*;
import recodercs.list.*;
import recodercs.service.*;
import recodercs.util.Debug;

/** this class implements basic functions for modifier handling.
    @author Andreas Ludwig
    @author Rainer Neumann
*/
public class ModifierKit implements recodercs.kit.Modifiers {

	private ModifierKit() {}

	public static Modifier createModifier(ProgramFactory f, int code) {
		Debug.asserta(f);
		switch (code) {
			case PUBLIC :
				return new Public();
			case PRIVATE :
				return new Private();
			case PROTECTED :
				return new Protected();
			case INTERNAL :
				return new Internal();
			case STATIC :
				return new Static();
			case SEALED :
				return new Sealed();
			case EXTERN :
				return new Extern();
			case ABSTRACT :
				return new Abstract();
			case READONLY :
				return new Readonly();
			case VOLATILE :
				return new Volatile();
			case NEW :
				return new NewModifier();
			case OVERIDE :
				return new Override();
			case VIRTUAL :
				return new Virtual();
			case REF :
				return new Ref();
			case PARAMS :
				return new Params();
			case OUT :
				return new Out();
			default :
				throw new IllegalArgumentException("Unsupported modifier code " + code);
		}
	}

	public static int getCode(Modifier m) {
		if (m == null) {
			return INTERNAL;
		}
		if (m instanceof Public) {
			return PUBLIC;
		} else if (m instanceof Protected) {
			return PROTECTED;
		} else if (m instanceof Private) {
			return PRIVATE;
		} else if (m instanceof Static) {
			return STATIC;
		} else if (m instanceof Sealed) {
			return SEALED;
		} else if (m instanceof Abstract) {
			return ABSTRACT;
		} else if (m instanceof Extern) {
			return EXTERN;
		} else if (m instanceof NewModifier) {
			return NEW;
		} else if (m instanceof Virtual) {
			return VIRTUAL;
		} else if (m instanceof Override) {
			return OVERIDE;
		} else if (m instanceof Internal) {
			return INTERNAL;
		} else if (m instanceof Out) {
			return OUT;
		} else if (m instanceof Ref) {
			return REF;
		} else if (m instanceof Params) {
			return PARAMS;
		} else if (m instanceof Volatile) {
			return VOLATILE;
		} else if (m instanceof Readonly) {
			return READONLY;
		}

		throw new IllegalArgumentException("Unknown Modifier " + m.getClass().getName());
	}



	/** Returns the code for the visibility of the given declaration */
	public static int getVisibilityCode(Declaration decl) {
		Debug.asserta(decl);
		ModifierList mods = decl.getModifiers();
		if (mods == null) {
			return 0;
		}
		
		int result = 0;
		
		for (int i = 0; i < mods.size(); i += 1) {
			Modifier res = mods.getModifier(i);
			if (res instanceof VisibilityModifier) {
				result |= getCode(res);
			}
		}
		return result;
	}

	private static boolean containsModifier(Declaration decl, Class mod) {
		Debug.asserta(decl, mod);
		ModifierList mods = decl.getModifiers();
		if (mods == null) {
			return false;
		}
		for (int i = 0; i < mods.size(); i += 1) {
			Modifier res = mods.getModifier(i);
			if (mod.isInstance(res)) {
				return true;
			}
		}
		return false;
	}

	// DISABLED: These two methods were deprecated...

	//	// does not check vadility, but replaces an existing visibility modifier
	//	// if there is one. Understands the PACKAGE_VISIBILITY pseudo modifier.
	//	// obeys the standard JavaDOC modifier order convention:
	//	// VisibilityModifier as first, then abstract or (static  - final)
	//	// all others go to the last position
	//	/**
	//	   @deprecated replaced by recoder.kit.transformation.Modify
	//	 */
	//	private static Modifier modify(ChangeHistory ch, int code, Declaration decl) {
	//		Debug.assert(decl);
	//		ModifierMutableList mods = decl.getModifiers();
	//		ProgramFactory fact = decl.getFactory();
	//		Modifier m;
	//		int insertPos = 0;
	//		switch (code) {
	//			case PACKAGE :
	//				if (code == PACKAGE) {
	//					m = getVisibilityModifier(decl);
	//					if (m != null) {
	//						MiscKit.remove(ch, m);
	//					}
	//					return null;
	//				}
	//			case PUBLIC :
	//				m = getVisibilityModifier(decl);
	//				if (m instanceof Public) {
	//					return null;
	//				}
	//				if (m != null) {
	//					MiscKit.remove(ch, m);
	//				}
	//				if (mods == null) {
	//					decl.setModifiers(mods = new ModifierArrayList());
	//				}
	//				m = fact.createPublic();
	//				insertPos = 0;
	//				break;
	//			case PROTECTED :
	//				m = getVisibilityModifier(decl);
	//				if (m instanceof Protected) {
	//					return null;
	//				}
	//				if (m != null) {
	//					MiscKit.remove(ch, m);
	//				}
	//				if (mods == null) {
	//					decl.setModifiers(mods = new ModifierArrayList());
	//				}
	//				m = fact.createProtected();
	//				insertPos = 0;
	//				break;
	//			case PRIVATE :
	//				m = getVisibilityModifier(decl);
	//				if (m instanceof Private) {
	//					return null;
	//				}
	//				if (m != null) {
	//					MiscKit.remove(ch, m);
	//				}
	//				if (mods == null) {
	//					decl.setModifiers(mods = new ModifierArrayList());
	//				}
	//				m = fact.createPrivate();
	//				insertPos = 0;
	//				break;
	//			case STATIC :
	//				if (containsModifier(decl, Static.class)) {
	//					return null;
	//				}
	//				m = getVisibilityModifier(decl);
	//				insertPos = (m == null) ? 0 : 1;
	//				m = fact.createStatic();
	//				break;
	//			case FINAL :
	//				if (containsModifier(decl, Sealed.class)) {
	//					return null;
	//				}
	//				m = getVisibilityModifier(decl);
	//				insertPos = (m == null) ? 0 : 1;
	//				if (containsModifier(decl, Static.class)) {
	//					insertPos += 1;
	//				}
	//				m = fact.createSealed();
	//				break;
	//			case ABSTRACT :
	//				if (containsModifier(decl, Abstract.class)) {
	//					return null;
	//				}
	//				m = getVisibilityModifier(decl);
	//				insertPos = (m == null) ? 0 : 1;
	//				m = fact.createAbstract();
	//				break;
	//			case SYNCHRONIZED :
	//				if (containsModifier(decl, Synchronized.class)) {
	//					return null;
	//				}
	//				insertPos = (mods == null) ? 0 : mods.size();
	//				m = fact.createSynchronized();
	//				break;
	//			case TRANSIENT :
	//				if (containsModifier(decl, Transient.class)) {
	//					return null;
	//				}
	//				insertPos = (mods == null) ? 0 : mods.size();
	//				m = fact.createTransient();
	//				break;
	//			case STRICT :
	//				if (containsModifier(decl, Ref.class)) {
	//					return null;
	//				}
	//				insertPos = (mods == null) ? 0 : mods.size();
	//				m = fact.createRef();
	//				break;
	//			case VOLATILE :
	//				if (containsModifier(decl, Volatile.class)) {
	//					return null;
	//				}
	//				insertPos = (mods == null) ? 0 : mods.size();
	//				m = fact.createVolatile();
	//				break;
	//			case NATIVE :
	//				if (containsModifier(decl, Native.class)) {
	//					return null;
	//				}
	//				insertPos = (mods == null) ? 0 : mods.size();
	//				m = fact.createNative();
	//				break;
	//			default :
	//				throw new IllegalArgumentException("Unsupported modifier code " + code);
	//		}
	//		mods.insert(insertPos, m);
	//		m.setParent(decl); // make parent role valid
	//		if (ch != null) {
	//			ch.attached(m);
	//		}
	//		return m;
	//	}
	//
	//	// mdecl must be a valid recoder.abstraction.Member (not a class initializer)
	//	// returns true if nothing was to be done
	//	// does not check if private would be sufficient dealing with multiple
	//	// inner types; this case rarely occurs and will be handled by assigning
	//	// package visibility -- which also is what a byte code compiler would
	//	// generate anyway.
	//	/**
	//	   @deprecated will be replaced; does not make visible redefined members
	//	 */
	//	public static boolean makeVisible(
	//		ChangeHistory ch,
	//		SourceInfo si,
	//		MemberDeclaration mdecl,
	//		ClassType ct) {
	//		Debug.assert(si, mdecl, ct);
	//		Debug.assert(mdecl instanceof Member);
	//		if (si.isVisibleFor((Member) mdecl, ct)) {
	//			return true;
	//		}
	//		int minimumNeeded;
	//		TypeDeclaration mt = mdecl.getMemberParent();
	//		if (mt == ct) {
	//			minimumNeeded = PRIVATE;
	//		} else if (mt.getNamespace() == ct.getNamespace()) {
	//			minimumNeeded = PACKAGE;
	//		} else if (si.isSubtype(mt, ct)) {
	//			minimumNeeded = PROTECTED;
	//		} else {
	//			minimumNeeded = PUBLIC;
	//		}
	//		modify(ch, minimumNeeded, mdecl);
	//		return false;
	//	}

}
