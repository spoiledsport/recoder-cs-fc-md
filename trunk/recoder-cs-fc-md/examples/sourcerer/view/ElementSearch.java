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
import recoder.service.*;
import recoder.csharp.*;
import recoder.util.*;

/*
  Make concurrent.
  Make faster for arrays.
  Validate input: wellformed?
 */

public class ElementSearch extends ListSelector {

    private JTextField nameField;
    private JButton startButton;
    private ProgramModelElementList roots;
    
    public ElementSearch(SelectionModel model, ProgramModelElementList roots) {
	super(model, "Element Search", ModelElementList.EMPTY_LIST, "%c %N",
	      true, true);
	this.roots = roots;
	JPanel panel = new JPanel(new GridBagLayout());
	
	nameField = new JTextField();
	startButton = new JButton("Find");
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.weightx = 1.0;
	gbc.fill = GridBagConstraints.BOTH;
	panel.add(nameField, gbc);
	gbc.weightx = 0.0;
	panel.add(startButton, gbc);
	addNorthComponent(panel);
	ActionListener starter = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    String name = nameField.getText();
		    if (name.length() == 0) {
			nameField.requestFocus();
			return;
		    }
		    NameFilter filter = new NameFilter(name);
		    ProgramModelElementMutableList results = new ProgramModelElementArrayList(10);
		    MemberWalker w = new MemberWalker(ElementSearch.this.roots);
		    while (w.next(filter)) {
			results.add(w.getProgramModelElement());
		    }
		    update("Element Search: " + results.size() + " hits", results);
		    if (!results.isEmpty()) {
			selectorList.requestFocus();
		    }
		}
	    };
	nameField.addActionListener(starter);
	startButton.addActionListener(starter);
	
    }

    public void requestFocus() {
	nameField.requestFocus();
    }
    
    public static class NameFilter implements ModelElementFilter {
	
	private String name;
	private boolean isFullName;
	private boolean isMethodName;
	
	public NameFilter(String name) {
	    if (name == null) {
		name = "";
	    }
	    int posb = name.indexOf('(');
	    isMethodName = posb >= 0 && name.indexOf(')') > posb;
	    int posd = name.indexOf('.');
	    isFullName = posd >= 0 && (!isMethodName || posd < posb);
	    // !!! name should not contain whitespaces
	    this.name = name.trim();
	}
	
	public boolean accept(ModelElement me) {
	    if (me instanceof ProgramModelElement) {
		ProgramModelElement pme = (ProgramModelElement)me;
		String n = isFullName ? pme.getFullName() : pme.getName();
		if (isMethodName) {
		    if (pme instanceof Method) {
			// no whitespaces after comma
			n += Format.toString("%N", "(", ",", ")", ((Method)pme).getSignature());
		    } else {
			return false;
		    }
		} else {
		    if (n == null) {
			n = "";
		    }
		}
		return name.equals(n);
	    }
	    return false;
	}
    }


/**
   Walks packages and members in depth first order.
   Does not descend into other program model elements.
 */
public static class MemberWalker {

    ProgramModelElement[] stack;
    int count;
    ProgramModelElement current;

    public MemberWalker(ProgramModelElement root) {
	stack = new ProgramModelElement[4];
	stack[count++] = root;
    }

    public MemberWalker(ProgramModelElementList roots) {
	int s = roots.size();
	stack = new ProgramModelElement[Math.max(2, s)];
	for (count = 0; count < s; count += 1) {
	    stack[count] = roots.getProgramModelElement(count);
	}	
    }

    public boolean next(ModelElementFilter filter) {
	while (next()) {
	    if (filter.accept(current)) {
		return true;
	    }
	}
	return false;
    }

    /**
       Proceeds to the next element, if available. Returns <CODE>true</CODE>,
       if there  is one, <CODE>false</CODE> otherwise.
       @return <CODE>true</CODE> if the iterator points to an object.
     */
    public boolean next() {
	if (count == 0) {
	    current = null;
	    return false;
	}
	current = stack[--count]; // pop
	if (current instanceof DeclaredTypeContainer) {
	    pushAll(((DeclaredTypeContainer)current).getDeclaredTypes());
	    if (current instanceof ClassType) {
		ClassType cct = (ClassType)current;
		pushAll(cct.getFields());
		pushAll(cct.getConstructors());
		pushAll(cct.getMethods());
	    }
	}	
	return true;
    }

    private void pushAll(ProgramModelElementList additional) {
	int s = additional.size();
	if (count + s >= stack.length) {
	    ProgramModelElement[] newStack = 
		new ProgramModelElement[Math.max(stack.length * 2, count + s)];
	    System.arraycopy(stack, 0, newStack, 0, count);
	    stack = newStack;
	}
	for (int i = 0; i < s; i += 1) {
	    stack[count++] = additional.getProgramModelElement(i);
	}
    }
    

    /**
       Returns the current element of the iteration, or
       <CODE>null</CODE> if {@link #next} has not yet been called or
       has returned <CODE>false</CODE>.
       @return the current element, or <CODE>null</CODE>.
     */
    public ProgramModelElement getProgramModelElement() {
	return current;
    }
    
}
    
}
