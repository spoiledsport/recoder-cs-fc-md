// This file is part of the RECODER library and protected by the LGPL.

package recoder.service;

import recoder.*;
import recoder.abstraction.*;
import recoder.convenience.*;
import recoder.abstraction.*;
import recoder.abstraction.Namespace;
import recoder.csharp.*;
import recoder.csharp.declaration.*;
import recoder.csharp.statement.*;
import recoder.csharp.declaration.modifier.*;
import recoder.csharp.expression.*;
import recoder.csharp.expression.operator.*;
import recoder.csharp.reference.*;
import recoder.kit.*;
import recoder.list.*;
import recoder.io.*;
import recoder.util.*;

import java.util.EventObject;
import java.util.Enumeration;

/**
   Keeps records on the syntactical changes that occured after the
   last validation of the model.
   All transformations should inform this instance of their changes that
   are visible to the model.
   In addition, the change history allows to define top-level transformation 
   sequences and perform rollbacks on these.
   @author AL, RN
   @since 0.5
 */
public class ChangeHistory extends AbstractService {

	// set to false ignore the warnings for property style getters/setters
	private final static boolean DEBUG = true;

	/**
	   Creates a new change history for the given configuration.
	 */
	public ChangeHistory(ServiceConfiguration config) {
		super(config);
	}

	/**
	   A list (queue) for tree changes used for update propagation.
	 */
	private TreeChangeMutableList changeList = new TreeChangeArrayList();

	/**
	   A map for change roots to tree changes used for fast 
	   duplicate identification.
	 */
	private final MutableMap root2change = new IdentityHashTable();

	/**
	   Flag indicating that the change history has a non-empty update queue.
	 */
	private boolean needsUpdate = false;

	/**
	   Flag indicating that the change history update is in the progress.
	 */
	private boolean isUpdating = false;

	/**
	   Flag indicating that a model update has been requested while the
	   change history was updating.
	 */
	private boolean delayedUpdate = false;

	/**
	   A stack for transformation begin marks and change reports,
	   used for rollbacks.
	 */
	private Object[] reportStack = new Object[8];

	/**
	   The stack counter.
	 */
	private int reportCount = 0;

	/**
	   The listeners of the change history.
	   An implementation using a dense array is sufficient for our purposes:
	   we expect only a few listeners.
	 */
	private ChangeHistoryListener[] changeListeners = new ChangeHistoryListener[0];

	/**
	   The listeners of the change history.
	   An implementation using a dense array is sufficient for our purposes:
	   we expect only a few listeners.
	 */
	private ModelUpdateListener[] updateListeners = new ModelUpdateListener[0];

	/**
	   Adds a change history listener to the history.
	   @param chl a listener.
	 */
	public void addChangeHistoryListener(ChangeHistoryListener chl) {
		synchronized (changeListeners) {
			ChangeHistoryListener[] newListeners =
				new ChangeHistoryListener[changeListeners.length + 1];
			System.arraycopy(changeListeners, 0, newListeners, 0, changeListeners.length);
			newListeners[changeListeners.length] = chl;
			changeListeners = newListeners;
		}
	}

	/**
	   Removes a change history listener from the history.
	   @param chl a listener.
	 */
	public void removeChangeHistoryListener(ChangeHistoryListener chl) {
		synchronized (changeListeners) {
			for (int i = changeListeners.length - 1; i >= 0; i -= 1) {
				if (changeListeners[i] == chl) {
					ChangeHistoryListener[] newListeners =
						new ChangeHistoryListener[changeListeners.length - 1];
					if (i > 0) {
						System.arraycopy(changeListeners, 0, newListeners, 0, i);
					}
					if (i < changeListeners.length - 1) {
						System.arraycopy(
							changeListeners,
							i + 1,
							newListeners,
							i,
							changeListeners.length - 1 - i);
					}
					changeListeners = newListeners;
					break;
				}
			}
		}
	}

	/**
	   Adds a model update listener to the history.
	   @param mul a listener.
	 */
	public void addModelUpdateListener(ModelUpdateListener l) {
		synchronized (updateListeners) {
			ModelUpdateListener[] newListeners =
				new ModelUpdateListener[updateListeners.length + 1];
			System.arraycopy(updateListeners, 0, newListeners, 0, updateListeners.length);
			newListeners[updateListeners.length] = l;
			updateListeners = newListeners;
		}
	}

	/**
	   Removes a model update listener from the history.
	   @param mul a listener.
	 */
	public void removeModelUpdateListener(ModelUpdateListener l) {
		synchronized (updateListeners) {
			for (int i = updateListeners.length - 1; i >= 0; i -= 1) {
				if (updateListeners[i] == l) {
					ModelUpdateListener[] newListeners =
						new ModelUpdateListener[updateListeners.length - 1];
					if (i > 0) {
						System.arraycopy(updateListeners, 0, newListeners, 0, i);
					}
					if (i < updateListeners.length - 1) {
						System.arraycopy(
							updateListeners,
							i + 1,
							newListeners,
							i,
							updateListeners.length - 1 - i);
					}
					updateListeners = newListeners;
					break;
				}
			}
		}
	}

	/** 
	Check if there is a problem with the new change; it has the same
	root as the former old change.
	*/
	private void checkConflict(TreeChange oldChange, TreeChange newChange) {
		// two trees are considered equals if their root nodes are identical
		boolean sameParent =
			newChange.getChangeRootParent() == oldChange.getChangeRootParent();

		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// add reattachment information to replacement node?
		// combination dettach-attach to different positions is admissible
		//   ignoring attaches is okay, but we may not remove dettaches
		//   because this root could hide an attach

		if (oldChange instanceof AttachChange) {
			if (newChange instanceof AttachChange) { // attach(x) - attach(x)
				if (sameParent) {
					// duplicate change report, ignore the new one
					root2change.put(oldChange.getChangeRoot(), oldChange);
					// the new change is the last one in the change list
					changeList.remove(changeList.size() - 1);
				} else {
					throw new IllegalChangeReportException(
						"Duplicate attachment of one element in different places: "
							+ newChange
							+ " followed "
							+ oldChange);
				}
			}
			if (newChange instanceof DetachChange) { // attach(x) - detach(x)
				if (sameParent) {
					// redundant change report, ignore both
					// (it is okay to drop the old attach node)
				} else {
					// complete nonsense; exception
				}
			}
		} else if (oldChange instanceof DetachChange) {
			if (newChange instanceof AttachChange) { // detach(x) - attach(x)
				if (sameParent) {
					// nonsense (there is no way to change the role in the
					// parent, by construction of the AST)
				} else {
					// okay; let the old one hide the new one
				}
			}
			if (newChange instanceof DetachChange) { // detach(x) - detach(x)
				if (sameParent) {
					// redundant
				} else {
					// NONSENSE if from different places
				}
			}
		}
	}

	/**
	   Informs the change history of the addition of a new subtree
	   given by its root element.
	   @param root the root of the change.
	*/
	public void attached(ProgramElement root) {
		Debug.asserta(root);
		Debug.asserta((root.getASTParent() != null) || (root instanceof CompilationUnit));
		AttachChange ac = new AttachChange(root);
		enqueueChange(ac); // to the update queue
		pushChange(ac); // to the transformation sequence stack
		if (DEBUG) {
			if (!(root instanceof CompilationUnit)) {
				root = root.getASTParent();
			}
			ProgramElement orphan = MiscKit.checkParentLinks(root);
			if (orphan != null) {
				Debug.log(
					"### Orphan detected: "
						+ Format.toString(Formats.ELEMENT_LONG, orphan)
						+ " in call from \n"
						+ Debug.makeStackTrace());
			}
		}
	}

	/**
	   Informs the change history of the deletion of a subtree
	   given by its root element.
	   In case that the detached element is reattached, the former parent is
	   given explicitly.
	   @param root the root of the detached subtree.
	   @param parent the former parent of the detached subtree; may be
	   <CODE>null</CODE> only if the root is a compilation unit.
	   @param pos the positional code of the root in its former parent,
	   as obtained by 
	   {@link recoder.csharp.NonTerminalProgramElement#getChildPositionCode};
	   the code may be arbitrary if the root is a compilation unit.
	*/
	public void detached(
		ProgramElement root,
		NonTerminalProgramElement parent,
		int pos) {
		Debug.asserta(root);
		Debug.asserta((parent != null) || (root instanceof CompilationUnit));
		DetachChange dc = new DetachChange(root, parent, pos);
		enqueueChange(dc); // to the update queue
		pushChange(dc); // to the transformation sequence stack
	}

	/**
	   Informs the change history of the deletion of a subtree
	   given by its root element.
	   This method assumes that the parent link of the root element still
	   points to the old parent.
	   @param root the root of the detached subtree.
	   @param pos the positional code of the root in its former parent,
	   as obtained by 
	   {@link recoder.csharp.NonTerminalProgramElement#getChildPositionCode};
	   the code may be arbitrary if the root is a compilation unit.
	*/
	public void detached(ProgramElement root, int pos) {
		detached(root, root.getASTParent(), pos);
	}

	/**
	   Informs the change history of the replacement of a subtree by
	   another one given by their root elements. The replacement must have
	   a valid parent.
	   @param root the root of a subtree that has been replaced.
	   @param replacement the root of a subtree that took over the role of
	   the former tree.
	*/
	public void replaced(ProgramElement root, ProgramElement replacement) {
		Debug.asserta(root, replacement);
		NonTerminalProgramElement parent = replacement.getASTParent();
		Debug.asserta((parent != null) || (replacement instanceof CompilationUnit));
		AttachChange ac = new AttachChange(replacement);
		DetachChange dc = new DetachChange(root, ac);
		enqueueChange(dc); // to the update queue
		enqueueChange(ac); // to the update queue
		pushChange(dc); // to the transformation sequence stack
		pushChange(ac); // to the transformation sequence stack
		if (DEBUG) {
			ProgramElement orphan = MiscKit.checkParentLinks(parent);
			if (orphan != null) {
				Debug.log(
					"### Orphan detected: "
						+ Format.toString(Formats.ELEMENT_LONG, orphan)
						+ " in call from \n"
						+ Debug.makeStackTrace());
			}
		}
	}

	/**
	   Stores the current tree change.
	   @param tc the tree change.
	 */
	private void enqueueChange(TreeChange tc) {
		changeList.add(tc);
		if (DEBUG) {
			Debug.log(tc.toString());
		}
		TreeChange duplicate = (TreeChange) root2change.put(tc.getChangeRoot(), tc);
		if (duplicate != null) {
			checkConflict(duplicate, tc);
		}
		needsUpdate = true;
	}

	/**
	   Returns the last change from the update queue, or null if the
	   queue is empty.
	   Relevant for rollback.
	 */
	private TreeChange getTailChange() {
		int s = changeList.size();
		return s == 0 ? null : changeList.getTreeChange(s - 1);
	}

	/**
	   Removes the last change from the update queue.
	   Relevant for rollback.
	 */
	private void removeTailChange() {
		int s = changeList.size();
		TreeChange tc = changeList.getTreeChange(s - 1);
		if (DEBUG) {
			Debug.log("SUPPRESSING " + tc);
		}
		root2change.remove(tc.getChangeRoot());
		changeList.remove(s - 1);
		if (s == 1) {
			needsUpdate = false;
		}
	}

	/**
	   Locates and marks minor changes.
	 */
	private void normalize() {
		for (int i = 0, s = changeList.size(); i < s; i += 1) {
			TreeChange tc = changeList.getTreeChange(i);
			// there are no duplicates of roots by construction,
			// except certain detach-attach combinations.	    
			ProgramElement current = tc.getChangeRoot();
			NonTerminalProgramElement parent = tc.getChangeRootParent();
			while (parent != null) {
				current = parent;
				if (root2change.containsKey(current)) {
					tc.setMinor(true);
					current = UnitKit.getCompilationUnit(current);
					break;
				}
				parent = parent.getASTParent();
			}
			tc.setCompilationUnit((CompilationUnit) current);
		}
	}

	/**
	   Checks if there are changes in the change queue.
	   @return <CODE>true</CODE>, if there are changes left in the queue,
	   <CODE>false</CODE> otherwise.
	 */
	public final boolean needsUpdate() {
		return needsUpdate;
	}

	/**
	   Notifies all listeners of the current changes and resets the lists.
	   Services that require up to date information should call this method
	   before accessing their cache or precalculated information.
	 */
	public final void updateModel() {
		if (!needsUpdate) {
			return;
		}
		if (isUpdating) {
			delayedUpdate = true;
			return;
		}
		if (DEBUG) {
			Debug.log("MODEL UPDATE >>>>>");
		}
		synchronized (updateListeners) {
			int s = updateListeners.length;
			if (s > 0) {
				for (int i = 0; i < s; i += 1) {
					updateListeners[i].modelUpdating(updateEvent);
				}
			}
		}
		do {
			needsUpdate = false;
			isUpdating = true;
			normalize();
			ChangeHistoryEvent event = new ChangeHistoryEvent(this, changeList);
			changeList = new TreeChangeArrayList();
			root2change.clear();
			if (DEBUG) {
				Debug.log("  EVENT: " + event + " END EVENT");
			}
			ChangeHistoryListener[] listeners = this.changeListeners;
			// it is important to exactly follow the listeners order
			for (int i = 0, s = listeners.length; i < s; i += 1) {
				listeners[i].modelChanged(event);
			}
			isUpdating = false;
			if (!delayedUpdate) {
				break;
			} else {
				delayedUpdate = false;
			}
		} while (needsUpdate);
		if (DEBUG) {
			Debug.log("<<<<< END UPDATE");
		}
		synchronized (updateListeners) {
			int s = updateListeners.length;
			if (s > 0) {
				for (int i = 0; i < s; i += 1) {
					updateListeners[i].modelUpdated(updateEvent);
				}
			}
		}
	}

	private final EventObject updateEvent = new EventObject(this);

	private void pushChange(TreeChange tc) {
		push(tc);
	}

	private void push(Object transformationOrTreeChange) {
		if (reportCount == reportStack.length) {
			Object[] newt = new Object[reportCount * 2];
			System.arraycopy(reportStack, 0, newt, 0, reportCount);
			reportStack = newt;
		}
		reportStack[reportCount++] = transformationOrTreeChange;
	}

	/**
	   Reports the start of a new transformation.
	   After initialization or {@link #commit}, a dummy transformation
	   is inserted which is automatically overwritten if a new transformation
	   begins and the default transformation report sequence is still empty.
	   @param transformation the transformation that begins.
	   @since 0.53
	 */
	public void begin(Transformation transformation) {
		if (DEBUG)
			Debug.log("BEGIN \"" + transformation.toString() + "\"");
		push(transformation);
	}

	/**
	   Rollback all entries down to and including the given position.
	   @param position last position to rollback.
	 */
	private void rollback(int position) {
		// undo all transformations until the position is met
		if (DEBUG)
			Debug.log("BEGIN ROLLBACK");
		while (reportCount > position) {
			reportCount -= 1;
			if (reportStack[reportCount] instanceof TreeChange) {
				TreeChange lastChange = (TreeChange) reportStack[reportCount];
				TreeChange undoChange = undo(lastChange);
				if (lastChange == getTailChange()) {
					// if the change is still in the update queue, remove it
					// and presume that nothing has happened
					removeTailChange();
				} else {
					// if the change is no longer in the update queue, 
					// add the undo
					enqueueChange(undoChange);
				}
			} else if (DEBUG) {
				Debug.log("ROLLED BACK \"" + reportStack[reportCount] + "\"");
			}
			reportStack[reportCount] = null;
		}
		if (DEBUG)
			Debug.log("END ROLLBACK");
	}

	/**
	   Returns the internal index of the given transformation, or
	   <CODE>-1</CODE> if this transformation is not in the rollback
	   stack.
	 */
	private int locate(Transformation transformation) {
		int position = reportCount;
		while (position >= 0) {
			position -= 1;
			if (reportStack[position] == transformation) {
				break;
			}
		}
		return position;
	}

	/**
	   Removes the last transformations and reverts all
	   their changes until the given transformation has been
	   rolled back. The given transformation is removed.
	   @exception NoSuchTransformationException if the given transformation
	   is not known, for instance if it has already been removed.
	   @since 0.53
	 */
	public void rollback(Transformation transformation)
		throws NoSuchTransformationException {
		int position = locate(transformation);
		if (position < 0) {
			throw new NoSuchTransformationException(transformation);
		}
		rollback(position);
	}

	/**
	   Removes all transformations in the stack and reverts all
	   changes that have not been committed yet.
	   @see #commit
	   @since 0.53
	 */
	public void rollback() {
		rollback(0);
	}

	/**
	   Checks if the given transformation is reported in this history
	   and can be rolled back.
	   @param transformation the transformation to locate.
	   @return <CODE>true</CODE>, if the given transformation can be 
	   rolled back, <CODE>false</CODE> otherwise.
	   @since 0.53
	*/
	public boolean isReported(Transformation transformation) {
		return locate(transformation) >= 0;
	}

	/**
	   Flushes all transformation sequences making them irreversible.
	   @since 0.53
	 */
	public void commit() {
		while (reportCount > 0) {
			reportStack[--reportCount] = null;
		}
		if (DEBUG) {
			Debug.log("COMMITTING");
		}
	}

	/**
	   Undoes a tree change by remove/inserting a child from/to its 
	   current/former parent.
	   @param tc the change to undo.
	   @return the resulting tree change description.
	   @exception ClassCastException if the child does not fit to the position.
	   @exception IllegalArgumentException if the child is at wrong position.
	   @exception IndexOutOfBoundsException if the child is at wrong position.
	 */
	private TreeChange undo(TreeChange tc) {
		
throw new UnsupportedOperationException("This operation is not yet implemented in Recoder-C#");
// TODO: Write this method
		
//		if (DEBUG)
//			Debug.log("Undoing " + tc.toString());
//		TreeChange result;
//		ProgramElement child = tc.getChangeRoot();
//		NonTerminalProgramElement parent = tc.getChangeRootParent();
//		if (tc instanceof AttachChange) {
//			if (parent != null) {
//				int position = parent.getChildPositionCode(child);
//				parent.replaceChild(child, null);
//				result = new DetachChange(child, parent, position);
//			} else {
//				result = new DetachChange(child, null, 0);
//			}
//			if (DEBUG)
//				Debug.log(" -> " + result.toString());
//			return result;
//		}
//		if (!(tc instanceof DetachChange)) {
//			return null;
//		}
//		DetachChange dc = (DetachChange) tc;
//
//		// !!!!!!!!!!!!!!!!!!!!!!
//		// if (dc.getReplacement() != null) ...
//		// !!!!!!!!!!!!!!!!!!!!!!
//
//		int pos = dc.getChangePositionCode();
//		int role = pos & 0x0F;
//		int index = pos >> 4;
//		if (parent == null) {
//			CompilationUnit x = (CompilationUnit) child;
//			x.setDataLocation(null); // assume that this unit is new
//		} else if (parent instanceof CompilationUnit) {
//			CompilationUnit x = (CompilationUnit) parent;
//			switch (role) {
//				case 0 :
//					//		x.setPackageSpecification((NamespaceSpecification)child);
//					NamespaceSpecificationMutableList list0 =
//						x.getNamespaceSpecifications();
//					if (list0 == null) {
//						list0 = new NamespaceSpecificationArrayList();
//						x.setPackageSpecification(list0);
//					}
//					list0.insert(index, (NamespaceSpecification) child);
//					break;
//				case 1 :
//					UsingMutableList list = x.getUsings();
//					if (list == null) {
//						list = new UsingArrayList();
//						x.setUsings(list);
//					}
//					list.insert(index, (Using) child);
//					break;
//				case 2 :
//					TypeDeclarationMutableList list2 = x.getDeclarations();
//					if (list2 == null) {
//						list2 = new TypeDeclarationArrayList();
//						x.setDeclarations(list2);
//					}
//					list2.insert(index, (TypeDeclaration) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof Using) {
//			Using x = (Using) parent;
//			x.setReference((TypeReferenceInfix) child);
//		} else if (parent instanceof NamespaceSpecification) {
//			NamespaceSpecification x = (NamespaceSpecification) parent;
//			x.setNamespaceReference((NamespaceReference) child);
//		} else if (parent instanceof StatementBlock) {
//			StatementBlock x = (StatementBlock) parent;
//			StatementMutableList list = x.getBody();
//			if (list == null) {
//				list = new StatementArrayList();
//				x.setBody(list);
//			}
//			list.insert(index, (Statement) child);
//		} else if (parent instanceof ClassDeclaration) {
//			ClassDeclaration x = (ClassDeclaration) parent;
//			switch (role) {
//				case 0 :
//					ModifierMutableList list = x.getModifiers();
//					if (list == null) {
//						list = new ModifierArrayList();
//						x.setModifiers(list);
//					}
//					list.insert(index, (Modifier) child);
//					break;
//				case 1 :
//					x.setIdentifier((Identifier) child);
//					break;
//				case 2 :
//					x.setExtendedTypes((Extends) child);
//					break;
//				case 3 :
//					x.setImplementedTypes((Implements) child);
//					break;
//				case 4 :
//					MemberDeclarationMutableList list2 = x.getMembers();
//					if (list2 == null) {
//						list2 = new MemberDeclarationArrayList();
//						x.setMembers(list2);
//					}
//					list2.insert(index, (MemberDeclaration) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof FieldDeclaration) {
//			FieldDeclaration x = (FieldDeclaration) parent;
//			switch (role) {
//				case 0 :
//					ModifierMutableList list = x.getModifiers();
//					if (list == null) {
//						list = new ModifierArrayList();
//						x.setModifiers(list);
//					}
//					list.insert(index, (Modifier) child);
//					break;
//				case 1 :
//					x.setTypeReference((TypeReference) child);
//					break;
//				case 2 :
//					FieldSpecificationMutableList list2 = x.getFieldSpecifications();
//					if (list2 == null) {
//						list2 = new FieldSpecificationArrayList();
//						x.setFieldSpecifications(list2);
//					}
//					list2.insert(index, (FieldSpecification) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof InheritanceSpecification) {
//			InheritanceSpecification x = (InheritanceSpecification) parent;
//			TypeReferenceMutableList list = x.getSupertypes();
//			if (list == null) {
//				list = new TypeReferenceArrayList();
//				x.setSupertypes(list);
//			}
//			list.insert(index, (TypeReference) child);
//		} else if (parent instanceof InterfaceDeclaration) {
//			InterfaceDeclaration x = (InterfaceDeclaration) parent;
//			switch (role) {
//				case 0 :
//					ModifierMutableList list = x.getModifiers();
//					if (list == null) {
//						list = new ModifierArrayList();
//						x.setModifiers(list);
//					}
//					list.insert(index, (Modifier) child);
//					break;
//				case 1 :
//					x.setIdentifier((Identifier) child);
//					break;
//				case 2 :
//					x.setExtendedTypes((Extends) child);
//					break;
//				case 4 :
//					MemberDeclarationMutableList list2 = x.getMembers();
//					if (list2 == null) {
//						list2 = new MemberDeclarationArrayList();
//						x.setMembers(list2);
//					}
//					list2.insert(index, (MemberDeclaration) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof LocalVariableDeclaration) {
//			LocalVariableDeclaration x = (LocalVariableDeclaration) parent;
//			switch (role) {
//				case 0 :
//					ModifierMutableList list2 = x.getModifiers();
//					if (list2 == null) {
//						list2 = new ModifierArrayList();
//						x.setModifiers(list2);
//					}
//					list2.insert(index, (Modifier) child);
//					break;
//				case 1 :
//					x.setTypeReference((TypeReference) child);
//					break;
//				case 2 :
//					VariableSpecificationMutableList list3 =
//						x.getVariableSpecifications();
//					if (list3 == null) {
//						list3 = new VariableSpecificationArrayList();
//						x.setVariableSpecifications(list3);
//					}
//					list3.insert(index, (VariableSpecification) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//
//		} else if (parent instanceof MethodDeclaration) {
//			MethodDeclaration x = (MethodDeclaration) parent;
//			switch (role) {
//				case 0 :
//					ModifierMutableList list = x.getModifiers();
//					if (list == null) {
//						list = new ModifierArrayList();
//						x.setModifiers(list);
//					}
//					list.insert(index, (Modifier) child);
//					break;
//				case 1 :
//					x.setTypeReference((TypeReference) child);
//					break;
//				case 2 :
//					x.setIdentifier((Identifier) child);
//					break;
//				case 3 :
//					ParameterDeclarationMutableList list2 = x.getParameters();
//					if (list2 == null) {
//						list2 = new ParameterDeclarationArrayList();
//						x.setParameters(list2);
//					}
//					list2.insert(index, (ParameterDeclaration) child);
//					break;
//				case 4 :
//					x.setThrown((Throws) child);
//					break;
//				case 5 :
//					x.setBody((StatementBlock) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//
//		} else if (parent instanceof ParameterDeclaration) {
//			ParameterDeclaration x = (ParameterDeclaration) parent;
//			switch (role) {
//				case 0 :
//					ModifierMutableList list = x.getModifiers();
//					if (list == null) {
//						list = new ModifierArrayList();
//						x.setModifiers(list);
//					}
//					list.insert(index, (Modifier) child);
//					break;
//				case 1 :
//					x.setTypeReference((TypeReference) child);
//					break;
//				case 2 :
//					x.setVariableSpecification((VariableSpecification) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof Throws) {
//			Throws x = (Throws) parent;
//			TypeReferenceMutableList list = x.getExceptions();
//			if (list == null) {
//				list = new TypeReferenceArrayList();
//				x.setExceptions(list);
//			}
//			list.insert(index, (TypeReference) child);
//		} else if (parent instanceof VariableSpecification) {
//			VariableSpecification x = (VariableSpecification) parent;
//			switch (role) {
//				case 0 :
//					x.setIdentifier((Identifier) child);
//					break;
//				case 1 :
//					x.setInitializer((Expression) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof ArrayInitializer) {
//			ArrayInitializer x = (ArrayInitializer) parent;
//			ExpressionMutableList list = x.getArguments();
//			if (list == null) {
//				list = new ExpressionArrayList();
//				x.setArguments(list);
//			}
//			list.insert(index, (Expression) child);
//		} else if (parent instanceof Operator) {
//			Operator x = (Operator) parent;
//			if (parent instanceof TypeOperator) {
//				if (parent instanceof New) {
//					New y = (New) parent;
//					switch (role) {
//						case 0 :
//							ExpressionMutableList list = y.getArguments();
//							if (list == null) {
//								list = new ExpressionArrayList();
//								y.setArguments(list);
//							}
//							list.insert(index, (Expression) child);
//							break;
//						case 1 :
//							y.setTypeReference((TypeReference) child);
//							break;
//						case 2 :
//							y.setReferencePrefix((ReferencePrefix) child);
//							break;
//						case 3 :
//							y.setClassDeclaration((ClassDeclaration) child);
//							break;
//						default :
//							throw new IllegalChangeReportException(
//								"Illegal child role in " + dc);
//					}
//				} else if (parent instanceof NewArray) {
//					NewArray y = (NewArray) parent;
//					switch (role) {
//						case 0 :
//							ExpressionMutableList list = y.getArguments();
//							if (list == null) {
//								list = new ExpressionArrayList();
//								y.setArguments(list);
//							}
//							list.insert(index, (Expression) child);
//							break;
//						case 1 :
//							y.setTypeReference((TypeReference) child);
//							break;
//						case 3 :
//							y.setArrayInitializer((ArrayInitializer) child);
//							break;
//						default :
//							throw new IllegalChangeReportException(
//								"Illegal child role in " + dc);
//					}
//				} else {
//					// includes TypeCast and Instanceof
//					TypeOperator y = (TypeOperator) parent;
//					switch (role) {
//						case 0 :
//							ExpressionMutableList list = y.getArguments();
//							if (list == null) {
//								list = new ExpressionArrayList();
//								y.setArguments(list);
//							}
//							list.insert(index, (Expression) child);
//							break;
//						case 1 :
//							y.setTypeReference((TypeReference) child);
//							break;
//						default :
//							throw new IllegalChangeReportException(
//								"Illegal child role in " + dc);
//					}
//				}
//			} else {
//				ExpressionMutableList list = x.getArguments();
//				if (list == null) {
//					list = new ExpressionArrayList();
//					x.setArguments(list);
//				}
//				list.insert(index, (Expression) child);
//			}
//		} else if (parent instanceof ArrayLengthReference) {
//			ArrayLengthReference x = (ArrayLengthReference) parent;
//			x.setReferencePrefix((ReferencePrefix) child);
//		} else if (parent instanceof ArrayReference) {
//			ArrayReference x = (ArrayReference) parent;
//			switch (role) {
//				case 0 :
//					x.setReferencePrefix((ReferencePrefix) child);
//					break;
//				case 1 :
//					ExpressionMutableList list = x.getDimensionExpressions();
//					if (list == null) {
//						list = new ExpressionArrayList();
//						x.setDimensionExpressions(list);
//					}
//					list.insert(index, (Expression) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof FieldReference) {
//			FieldReference x = (FieldReference) parent;
//			switch (role) {
//				case 0 :
//					x.setReferencePrefix((ReferencePrefix) child);
//					break;
//				case 1 :
//					x.setIdentifier((Identifier) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof VariableReference) {
//			VariableReference x = (VariableReference) parent;
//			x.setIdentifier((Identifier) child);
//		} else if (parent instanceof MetaClassReference) {
//			MetaClassReference x = (MetaClassReference) parent;
//			x.setReferencePrefix((ReferencePrefix) child);
//		} else if (parent instanceof MethodReference) {
//			MethodReference x = (MethodReference) parent;
//			switch (role) {
//				case 0 :
//					x.setReferencePrefix((ReferencePrefix) child);
//					break;
//				case 1 :
//					x.setIdentifier((Identifier) child);
//					break;
//				case 2 :
//					ExpressionMutableList list = x.getArguments();
//					if (list == null) {
//						list = new ExpressionArrayList();
//						x.setArguments(list);
//					}
//					list.insert(index, (Expression) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof TypeReferenceInfix) {
//			// includes NamespaceReference, TypeReference, URQ
//			TypeReferenceInfix x = (TypeReferenceInfix) parent;
//			switch (role) {
//				case 0 :
//					x.setReferencePrefix((ReferencePrefix) child);
//					break;
//				case 1 :
//					x.setIdentifier((Identifier) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof SuperConstructorReference) {
//			SuperConstructorReference x = (SuperConstructorReference) parent;
//			switch (role) {
//				case 0 :
//					x.setReferencePrefix((ReferencePrefix) child);
//					break;
//				case 1 :
//					ExpressionMutableList list = x.getArguments();
//					if (list == null) {
//						list = new ExpressionArrayList();
//						x.setArguments(list);
//					}
//					list.insert(index, (Expression) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof SuperReference) {
//			SuperReference x = (SuperReference) parent;
//			x.setReferencePrefix((ReferencePrefix) child);
//		} else if (parent instanceof ThisConstructorReference) {
//			ThisConstructorReference x = (ThisConstructorReference) parent;
//			ExpressionMutableList list = x.getArguments();
//			if (list == null) {
//				list = new ExpressionArrayList();
//				x.setArguments(list);
//			}
//			list.insert(index, (Expression) child);
//		} else if (parent instanceof ThisReference) {
//			ThisReference x = (ThisReference) parent;
//			x.setReferencePrefix((ReferencePrefix) child);
//		} else if (parent instanceof LabelJumpStatement) {
//			// includes break and continue
//			LabelJumpStatement x = (LabelJumpStatement) parent;
//			x.setIdentifier((Identifier) child);
//		} else if (parent instanceof Assert) {
//			Assert x = (Assert) parent;
//			switch (role) {
//				case 0 :
//					x.setCondition((Expression) child);
//					break;
//				case 1 :
//					x.setMessage((Expression) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof Case) {
//			Case x = (Case) parent;
//			switch (role) {
//				case 0 :
//					x.setExpression((Expression) child);
//					break;
//				case 1 :
//					StatementMutableList list = x.getBody();
//					if (list == null) {
//						list = new StatementArrayList();
//						x.setBody(list);
//					}
//					list.insert(index, (Statement) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof Catch) {
//			Catch x = (Catch) parent;
//			switch (role) {
//				case 0 :
//					x.setParameterDeclaration((ParameterDeclaration) child);
//					break;
//				case 1 :
//					x.setBody((StatementBlock) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof Default) {
//			Default x = (Default) parent;
//			StatementMutableList list = x.getBody();
//			if (list == null) {
//				list = new StatementArrayList();
//				x.setBody(list);
//			}
//			list.insert(index, (Statement) child);
//		} else if (parent instanceof LoopStatement) {
//			LoopStatement x = (LoopStatement) parent;
//			switch (role) {
//				case 0 :
//					LoopInitializerMutableList list = x.getInitializers();
//					if (list == null) {
//						list = new LoopInitializerArrayList();
//						((For) x).setInitializers(list);
//					}
//					list.insert(index, (LoopInitializer) child);
//					break;
//				case 1 :
//					x.setGuard((Expression) child);
//					break;
//				case 2 :
//					ExpressionMutableList list2 = x.getUpdates();
//					if (list2 == null) {
//						list2 = new ExpressionArrayList();
//						((For) x).setUpdates(list2);
//					}
//					list2.insert(index, (Expression) child);
//					break;
//				case 3 :
//					x.setBody((Statement) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof Else) {
//			Else x = (Else) parent;
//			x.setBody((Statement) child);
//		} else if (parent instanceof Finally) {
//			Finally x = (Finally) parent;
//			x.setBody((StatementBlock) child);
//		} else if (parent instanceof If) {
//			If x = (If) parent;
//			switch (role) {
//				case 0 :
//					x.setExpression((Expression) child);
//					break;
//				case 1 :
//					x.setThen((Then) child);
//					break;
//				case 2 :
//					x.setElse((Else) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof LabeledStatement) {
//			LabeledStatement x = (LabeledStatement) parent;
//			switch (role) {
//				case 0 :
//					x.setIdentifier((Identifier) child);
//					break;
//				case 1 :
//					x.setBody((Statement) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof ExpressionJumpStatement) {
//			// Return and Throw
//			ExpressionJumpStatement x = (ExpressionJumpStatement) parent;
//			x.setExpression((Expression) child);
//		} else if (parent instanceof Switch) {
//			Switch x = (Switch) parent;
//			switch (role) {
//				case 0 :
//					x.setExpression((Expression) child);
//					break;
//				case 1 :
//					BranchMutableList list = x.getBranchList();
//					if (list == null) {
//						list = new BranchArrayList();
//						x.setBranchList(list);
//					}
//					list.insert(index, (Branch) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof LockedBlock) {
//			LockedBlock x = (LockedBlock) parent;
//			switch (role) {
//				case 0 :
//					x.setExpression((Expression) child);
//					break;
//				case 1 :
//					x.setBody((StatementBlock) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else if (parent instanceof Then) {
//			Then x = (Then) parent;
//			x.setBody((Statement) child);
//		} else if (parent instanceof Try) {
//			Try x = (Try) parent;
//			switch (role) {
//				case 0 :
//					x.setBody((StatementBlock) child);
//					break;
//				case 1 :
//					BranchMutableList list = x.getBranchList();
//					if (list == null) {
//						list = new BranchArrayList();
//						x.setBranchList(list);
//					}
//					list.insert(index, (Branch) child);
//					break;
//				default :
//					throw new IllegalChangeReportException("Illegal child role in " + dc);
//			}
//		} else {
//			throw new RuntimeException();
//		}
//		result = new AttachChange(child);
//		if (DEBUG)
//			Debug.log(" -> " + result.toString());
//		return result;
	}
}
