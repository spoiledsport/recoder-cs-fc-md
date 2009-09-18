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
import recoder.service.*;
import recoder.csharp.*;
import recoder.list.*;
import recoder.convenience.*;

/**

*/
public class ChangeEventView extends ListSelector implements ChangeHistoryListener {

    private final static String DEFAULT_TITLE = "Last Changes";

    public ChangeEventView(SelectionModel model) {
	super(model, DEFAULT_TITLE, new SelectorList(model, ObjectList.EMPTY_LIST, TREE_CHANGE_RENDERER, ELEMENT_SELECTOR), false);
    }
    
    public void modelChanged(ChangeHistoryEvent changes) {
	TreeChangeList list = changes.getChanges();
	update(DEFAULT_TITLE, list);
    }

    public void modelUpdated(boolean selectionRemoved) {
	// do not disable anything
    }

    public final static SelectorList.ElementSelector ELEMENT_SELECTOR = new SelectorList.ElementSelector() {
	    public ModelElement getElementToSelect(Object selected) {
		TreeChange tc = (TreeChange)selected;
		if (tc instanceof AttachChange) {
		    return tc.getChangeRoot();
		} else {
		    return tc.getChangeRootParent();
		}
	    }
	};

    public final static ListCellRenderer TREE_CHANGE_RENDERER = new DefaultListCellRenderer() {

	    StringBuffer buffer = new StringBuffer();

	    public Component getListCellRendererComponent
		(JList list, Object value, int index, 
		 boolean isSelected, boolean hasFocus) {	    
		super.getListCellRendererComponent
		    (list, value, index, isSelected, hasFocus);
		if (value instanceof TreeChange) {
		    TreeChange tc = (TreeChange)value;
		    if (tc.isMinor()) {
			setForeground(Color.gray);
		    } else {
			setForeground(Color.black);
		    }
		    buffer.setLength(0);
		    if (tc instanceof AttachChange) {
			buffer.append("Attached ");
		    } else if (tc instanceof DetachChange) {
			buffer.append("Detached ");
		    }
		    ProgramElement el = tc.getChangeRoot();
		    if (el instanceof CompilationUnit) {
			buffer.append(Format.toString("%c %u", el));
		    } else {
			buffer.append(Format.toString("%c @%p ", el));
			el = tc.getChangeRootParent();
			String format = "";
			if (tc instanceof AttachChange) {
			    format = "to ";
			} else {
			    format = "from ";
			}
			if (el instanceof CompilationUnit) {
			    format += "%c %u";
			} else {
			    format += "%c in %u";
			}
			buffer.append(Format.toString(format, el));
		    }
		    setText(buffer.toString());
		} else {
		    setText("");
		    setForeground(Color.black);
		}
		setIcon(null);
		return this;
	    }
	};
}

