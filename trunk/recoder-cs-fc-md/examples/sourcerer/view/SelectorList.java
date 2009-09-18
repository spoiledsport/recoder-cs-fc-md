package sourcerer.view;

import sourcerer.*;
import sourcerer.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import recoder.*;
import recoder.abstraction.*;
import recoder.list.*;
import recoder.convenience.*;
import recoder.csharp.*;
import recoder.util.*;

public class SelectorList extends JList {

    private SelectionModel selectionModel;

    public interface ElementSelector {
	ModelElement getElementToSelect(Object selectedItem);
    }

    private ElementSelector elementSelector = new ElementSelector() {
	    public ModelElement getElementToSelect(Object selectedItem) {
		return (ModelElement)selectedItem;
	    }	    
	};

    private void addListener() {
	addListSelectionListener(new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
		    Object o = getSelectedValue();
		    if (o != null) {
			selectionModel.setSelectedElement(elementSelector.getElementToSelect(o));
		    }
		}
	    });
    }

    public SelectorList(SelectionModel model, final ObjectList list, ListCellRenderer renderer, final ElementSelector selector) {
	this.selectionModel = model;
	this.elementSelector = selector;
	setModel(new AbstractListModel() {
                public int getSize() { return list.size(); }
                public Object getElementAt(int i) { return list.getObject(i); }
            });
	setCellRenderer(renderer);	
	addListener();
    }

    public SelectorList(SelectionModel model, ModelElementList list, final String format, boolean sort) {
	this.selectionModel = model;
	final Object[] a = list.toObjectArray();
	if (sort) {
	    Sorting.heapSort(a, new Order.Lexical() {
		    protected String toString(Object x) {
			return Format.toString(format, (ModelElement)x);
		    }
		});
	}
	setModel(new AbstractListModel() {
                public int getSize() { return a.length; }
                public Object getElementAt(int i) { return a[i]; }
            });
	setCellRenderer(new ModelElementRenderer(format));
	addListener();
    }
}
