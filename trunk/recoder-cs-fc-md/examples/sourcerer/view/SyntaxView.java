package sourcerer.view;

import sourcerer.*;
import sourcerer.util.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import recoder.*;
import recoder.abstraction.*;
import recoder.convenience.*;
import recoder.io.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;
import recoder.csharp.reference.*;
import recoder.kit.*;
import recoder.list.*;
import recoder.service.*;
import recoder.util.*;

import recoder.abstraction.Namespace;

/**
*/
public class SyntaxView extends JPanel implements SelectionView {

	protected SelectionModel model;

	boolean showNames = true;
	boolean useColors = true;

	boolean showSyntaxTrees = true;

	protected JTree tree;

	public SyntaxView(final SelectionModel model, ServiceConfiguration sc) {
		super(new BorderLayout());
		setName("Syntax Forest");
		tree = new JTree(new SyntaxModel(sc));
		tree.setLargeModel(true); // optimizing; all cells have equal height
		tree.setCellRenderer(new SyntaxRenderer());
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.getSelectionModel().setSelectionMode(
			TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.putClientProperty("JTree.lineStyle", "Angled");
		tree.addTreeSelectionListener(selectionListener);
		add(new JScrollPane(tree));
		setModel(model);
	}

	public SelectionModel getModel() {
		return model;
	}

	public JTree getTree() {
		return tree;
	}

	/**
	   Use setSelectionModel(null) to detach from the model.
	 */
	public void setModel(SelectionModel model) {
		if (this.model != model) {
			if (this.model != null) {
				this.model.removeChangeListener(changeListener);
			}
			if (model != null) {
				this.model = model;
				model.addChangeListener(changeListener);
			}
			changeSelection();
		}
	}

	public void modelUpdated(boolean selectionRemoved) {
		if (selectionRemoved) {
			tree.clearSelection();
		}
		((SyntaxModel) tree.getModel()).update();
	}

	public boolean isShowingSyntaxTrees() {
		return showSyntaxTrees;
	}

	public void setShowingSyntaxTrees(boolean show) {
		if (show != showSyntaxTrees) {
			showSyntaxTrees = show;
			((SyntaxModel) tree.getModel()).update();
			changeSelection();
		}
	}

	public boolean isShowingNames() {
		return showNames;
	}

	public void setShowingNames(boolean show) {
		if (show != showNames) {
			showNames = show;
			tree.revalidate();
			tree.repaint();
		}
	}

	public boolean isUsingColors() {
		return useColors;
	}

	public void setUsingColors(boolean colored) {
		if (colored != useColors) {
			useColors = colored;
			tree.revalidate();
			tree.repaint();
		}
	}

	SyntaxRenderer getRenderer() {
		return (SyntaxRenderer) tree.getCellRenderer();
	}

	class SyntaxTreeSelectionListener implements TreeSelectionListener {

		/** 
		    Used to indicated an internal redirection of a model
		    element that shall be displayed by the tree but not
		    propagated to the model.  With this trick we do not have
		    to remove / re-add the listener to the tree each time.
		*/
		ModelElement redirected;

		public void valueChanged(TreeSelectionEvent e) {
			TreePath path = tree.getSelectionPath();
			if (path != null) {
				Object leaf = path.getLastPathComponent();
				if (leaf instanceof ModelElement && leaf != redirected) {
					model.setSelectedElement((ModelElement) leaf);
				}
			}
		}
	}

	SyntaxTreeSelectionListener selectionListener =
		new SyntaxTreeSelectionListener();

	ChangeListener changeListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			changeSelection();
		}
	};

	private void changeSelection() {
		ModelElement e = model == null ? null : model.getSelectedElement();
		if (e instanceof Namespace) {
			selectElement(e, false, true);
		} else if (e instanceof ProgramElement) {
			if (showSyntaxTrees || e instanceof CompilationUnit) {
				selectElement(e, false, true);
			} else {
				e = UnitKit.getCompilationUnit((ProgramElement) e);
				selectionListener.redirected = e;
				selectElement(e, false, true);
				selectionListener.redirected = null;
			}
		} else {
			tree.clearSelection();
		}
	}

	// expands and scrolls to the element
	public void selectElement(
		ModelElement p,
		boolean autoExpand,
		boolean autoScroll) {
		TreePath tpath = ((SyntaxModel) tree.getModel()).createPath(p);
		tree.setSelectionPath(tpath);
		if (autoExpand) {
			tree.expandPath(tpath);
		}
		if (autoScroll) {
			tree.scrollPathToVisible(tpath);
		}
	}

	final static Order UNIT_ORDER = new Order.Lexical() {
		protected String toString(Object x) {
			return RecoderUtils.getShortName((CompilationUnit) x);
		}
	};

	final static Order PACKAGE_SHORT_ORDER = new Order.Lexical() {
		protected String toString(Object x) {
			return RecoderUtils.getShortName((Namespace) x);
		}
	};

	/**
	   Tree model representing the directory - unit hierarchy and
	   additionally, the syntax tree.
	*/
	public class SyntaxModel implements TreeModel {

		private Vector treeModelListeners = new Vector();

		private String root = "";

		private NameInfo nameInfo;
		private SourceFileRepository sfr;

		/**
		   Contains mapping <Object, Object[]>, e.g.
		   root -> Namespaces,
		   Namespaces -> Namespaces | units
		 */
		private MutableMap cache;

		public SyntaxModel(ServiceConfiguration config) {
			this.nameInfo = config.getNameInfo();
			this.sfr = config.getSourceFileRepository();
			cache = new IdentityHashTable();
			computeCache();
		}

		public void update() {
			computeCache();
			if (!treeModelListeners.isEmpty()) {
				Enumeration enum = treeModelListeners.elements();
				TreeModelEvent event =
					new TreeModelEvent(this, new TreePath(root));
				while (enum.hasMoreElements()) {
					(
						(TreeModelListener) enum
							.nextElement())
							.treeStructureChanged(
						event);
				}
			}
		}

		private void computeCache() {
			cache.clear();
			
			ModelElementMutableList defNSList = (ModelElementMutableList) cache.get(root);
			if (defNSList == null) {
				defNSList = new ModelElementArrayList();
				cache.put(root,defNSList);
			}
			CompilationUnitList units = sfr.getCompilationUnits();
			for (int i = units.size() - 1; i >= 0; i -= 1) {

				CompilationUnit cu = units.getCompilationUnit(i);
				defNSList.add(cu);
				

				NamespaceList nlist =
					nameInfo.getNamespacesInCompilationUnit(cu);

				for (int ncount = nlist.size() - 1; ncount >= 0; ncount--) {
					Namespace p = nlist.getNamespace(ncount);
					String pname = p.getFullName();
					ModelElementMutableList list =
						(ModelElementMutableList) cache.get(p);

					if (list == null) {
						list = new ModelElementArrayList();
						cache.put(p, list);
					}
					// insert to proper position (insert from end)
					for (int j = list.size() - 1; true; j -= 1) {
						if (j < 0) {
							list.insert(0, cu);
							break;
						}
						ModelElement m = list.getModelElement(j);
						if ((m instanceof Namespace)
							|| UNIT_ORDER.lessOrEquals(m, cu)) {
							list.insert(j + 1, cu);
							break;
						}
					}
					int dot = pname.lastIndexOf('.');
					while (dot >= 0) {
						pname = pname.substring(0, dot);
						Namespace q = nameInfo.createNamespace(pname);
						list = (ModelElementMutableList) cache.get(q);
						if (list == null) {
							list = new ModelElementArrayList();
							cache.put(q, list);
						}
						// insert to proper position (insert from beginning)
						for (int j = 0, s = list.size(); true; j += 1) {
							if (j >= s) {
								list.add(p);
								break;
							}
							ModelElement m = list.getModelElement(j);
							if (m == p) {
								// only insert once
								break;
							}
							if ((m instanceof Namespace)
								&& PACKAGE_SHORT_ORDER.greaterOrEquals(m, p)) {
								list.insert(j, p);
								break;
							}
						}
						// Debug.log(Format.toString("%c %N %u", list));  
						p = q;
						dot = pname.lastIndexOf('.');
					}
// In C# it has no sense to include the Namespaces in the Syntax Forest.
//					list = (ModelElementMutableList) cache.get(root);
//					if (list == null) {
//						list = new ModelElementArrayList();
//						cache.put(root, list);
//					}
//					for (int j = 0, s = list.size(); true; j += 1) {
//						if (j >= s) {
//							list.add(p);
//							break;
//						}
//						ModelElement m = list.getModelElement(j);
//						if (m == p) {
//							// only insert once
//							break;
//						}
//						if ((m instanceof Namespace)
//							&& PACKAGE_SHORT_ORDER.greaterOrEquals(m, p)) {
//							list.insert(j, p);
//							break;
//						}
//					}
				}
			}
		}

		protected TreePath createPath(ModelElement node) {
			Debug.assert(node!=null);
			ObjectMutableList path = new ObjectArrayList();
			do {
				path.add(node);
				if (node instanceof Namespace) {
					String name = ((Namespace) node).getName();
					int dot = name.lastIndexOf('.');
					if (dot >= 0) {
						name = name.substring(0, dot);
						node = nameInfo.getNamespace(name);
					} else {
						node = null;
					}
				} else 
				if (node instanceof ProgramElement) {
					if (node instanceof CompilationUnit) {
// In C# CompilationUnits represent the default namespace, 
// but it will be added after the loop.
//						node =
//							nameInfo.getNamespace(
//								Naming.getPackageName((CompilationUnit) node));
						node = null;
					} else {
						node = ((ProgramElement) node).getASTParent();
					}
				}
			} while (node != null);
			path.add(root);
			Object[] elements = new Object[path.size()];
			for (int i = 0, s = elements.length; i < s; i += 1) {
				Debug.assert(path.getObject(s-i-1)!=null);
				elements[i] = path.getObject(s - i - 1);
			}
			return new TreePath(elements);
		}

		public void addTreeModelListener(TreeModelListener l) {
			treeModelListeners.addElement(l);
		}

		public void removeTreeModelListener(TreeModelListener l) {
			treeModelListeners.removeElement(l);
		}

		public Object getRoot() {
			return root;
		}

		public int getChildCount(Object parent) {
			if (parent instanceof ProgramElement) {
				if (showSyntaxTrees
					&& (parent instanceof NonTerminalProgramElement)) {
					return ((NonTerminalProgramElement) parent).getChildCount();
				} else {
					return 0;
				}
			}
			ModelElementList children = (ModelElementList) cache.get(parent);
			return (children == null) ? 0 : children.size();
		}

		public int getIndexOfChild(Object parent, Object child) {
			if (parent instanceof ProgramElement) {
				if (showSyntaxTrees
					&& (parent instanceof NonTerminalProgramElement)) {
					return (
						(NonTerminalProgramElement) parent).getIndexOfChild(
						(ProgramElement) child);
				} else {
					return -1;
				}
			}
			ModelElementList children = (ModelElementList) cache.get(parent);
			return (children == null) ? -1 : children.indexOf(child);
		}

		public Object getChild(Object parent, int index) {
			if (parent instanceof ProgramElement) {
				if (showSyntaxTrees
					&& (parent instanceof NonTerminalProgramElement)) {
					return ((NonTerminalProgramElement) parent).getChildAt(
						index);
				} else {
					return null;
				}
			}
			ModelElementList children = (ModelElementList) cache.get(parent);
			return (children == null) ? null : children.getObject(index);
		}

		public boolean isLeaf(Object node) {
			return showSyntaxTrees
				? (node instanceof TerminalProgramElement)
				: (node instanceof CompilationUnit);
		}

		public void valueForPathChanged(TreePath path, Object newValue) {
		}
	}

	class SyntaxRenderer extends DefaultTreeCellRenderer {

		public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {
			super.getTreeCellRendererComponent(
				tree,
				value,
				sel,
				expanded,
				leaf,
				row,
				hasFocus);
			if (value instanceof CompilationUnit) {
				if (useColors) {
					this.setForeground(Resources.COMPILATION_UNIT_COLOR);
				}
				String name =
					RecoderUtils.getShortName((CompilationUnit) value);
				if (showSyntaxTrees) {
					setText("CompilationUnit \"" + name + "\"");
				} else {
					setText(name);
				}
			} else if (value instanceof Namespace) {
				if (useColors) {
					this.setForeground(Resources.PACKAGE_COLOR);
				}
				String name = RecoderUtils.getShortName((Namespace) value);
				if (name.length() == 0) {
					name = "(Default Namespace)";
				}
				setText(name);
			} else {
				String text = value.getClass().getName();
				text = text.substring(text.lastIndexOf('.') + 1);
				if (showNames) {
					if (value instanceof NamedProgramElement) {
						text += " \""
							+ ((NamedProgramElement) value).getName()
							+ "\"";
					}
				}
				setText(text);
				if (useColors) {
					if (value instanceof Reference) {
						if (value instanceof TypeReference) {
							this.setForeground(Resources.TYPE_REFERENCE_COLOR);
						} else if (value instanceof VariableReference) {
							this.setForeground(
								Resources.VARIABLE_REFERENCE_COLOR);
						} else if (value instanceof ConstructorReference) {
							this.setForeground(
								Resources.CONSTRUCTOR_REFERENCE_COLOR);
						} else if (value instanceof MethodReference) {
							this.setForeground(
								Resources.METHOD_REFERENCE_COLOR);
						} else if (value instanceof NamespaceReference) {
							this.setForeground(
								Resources.PACKAGE_REFERENCE_COLOR);
						} else {
							this.setForeground(Color.black);
						}
					} else if (value instanceof Declaration) {
						if (value instanceof VariableSpecification) {
							this.setForeground(Resources.VARIABLE_COLOR);
						} else if (value instanceof ConstructorDeclaration) {
							this.setForeground(Resources.CONSTRUCTOR_COLOR);
						} else if (value instanceof MethodDeclaration) {
							this.setForeground(Resources.METHOD_COLOR);
						} else if (value instanceof TypeDeclaration) {
							this.setForeground(Resources.TYPE_COLOR);
						} else {
							this.setForeground(Color.black);
						}
					} else {
						this.setForeground(Color.black);
					}
				} else {
					this.setForeground(Color.black);
				}
			}
			setIcon(null);
			return this;
		}
	}
}
