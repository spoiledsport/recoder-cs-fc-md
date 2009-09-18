package sourcerer;

import sourcerer.view.*;
import sourcerer.util.*;
import sourcerer.tool.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.metal.*;

import recoder.*;
import recoder.abstraction.*;
import recoder.abstraction.Namespace;
import recoder.bytecode.*;
import recoder.list.*;
import recoder.io.*;
import recoder.convenience.*;
import recoder.service.*;
import recoder.kit.*;
// import recoder.kit.transformation.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;
import recoder.csharp.reference.*;
import recoder.util.*;

/**
   @author AL
*/
public class Main extends JFrame {
  
    private final static String VERSION = "0.133";
   
    SelectionModel model;

    JSplitPane mainPane;
    JTabbedPane selectorsPane;
    StatusBar statusBar;

    Component recentlyUsedStandardSelector = null;

    SourceCodeView codeView;
    SyntaxView syntaxView;
    MemberView memberView;
    HistoryView historyView;
    ChangeEventView changesView;    
    
    JMenu viewsMenu;
    int firstSelectorMenuItem;
    JMenu contextMenu;

    JCheckBoxMenuItem memberOptionShowColorsItem;
    JCheckBoxMenuItem syntaxOptionShowColorsItem;
    JCheckBoxMenuItem syntaxOptionShowNamesItem;
    JCheckBoxMenuItem syntaxOptionShowSyntaxTreesItem;

    CrossReferenceServiceConfiguration config;

    long lastUpdateDuration;
    
    Vector allViews = new Vector();
    
    final static String BSH_MAIN_CLASS = "bsh.Interpreter";
    
    private static boolean hasBeanShell() {
	Class c = null;
	try {
	    c = Class.forName(BSH_MAIN_CLASS);
	} catch (ClassNotFoundException cnfe) {
	    c = null;
	}
	return c != null;
    }



    public static void main(String[] args) {
	MetalLookAndFeel.setCurrentTheme(new ThinMetalTheme());
	UIManager.put("SplitPane.border", BorderFactory.createEmptyBorder());
	UIManager.put("SplitPaneDivider.border", BorderFactory.createEmptyBorder());
	// UIManager.put("SplitPane.dividerSize", new Integer(7));

	JLabel logo = new JLabel("V" + VERSION, 
				 Resources.Loader.loadIcon("Logo.jpg"), 
				 JLabel.CENTER);
	// do not yet use the Resources class to avoid initialization time
	logo.setVerticalTextPosition(JLabel.BOTTOM);
	logo.setHorizontalTextPosition(JLabel.CENTER);	
	logo.setBorder(BorderFactory.createEtchedBorder());
	logo.setOpaque(true);
	logo.setBackground(Color.white);

	Window splasher = SwingUtils.createSplashScreen(logo);
	splasher.setVisible(true);

	logo.setIcon(Resources.LOGO_ICON); // trigger resource loading
	CrossReferenceServiceConfiguration config = 
	    new CrossReferenceServiceConfiguration();
	Main me = new Main(config);
	me.setVisible(true);
	splasher.dispose();

	String[] newArgs = new String[args.length + 1];
	System.arraycopy(args, 0, newArgs, 0, args.length);
	newArgs[args.length] = "-q";
	config.getProjectSettings().setErrorHandler(new SourcererErrorHandler());
	RecoderProgram.setup(config, Main.class, newArgs);
	// important setting: force pretty printer to redefine parse positions
	config.getProjectSettings().setProperty(PropertyNames.OVERWRITE_PARSE_POSITIONS, "true");
	config.getChangeHistory().updateModel();
	
    }

    public Main(CrossReferenceServiceConfiguration cfg) {
	super("RECODER Sourcerer v" + VERSION);

	this.config = cfg;

	model = new DefaultSelectionModel();

	model.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
		    ModelElement p = model.getSelectedElement();
//		    openMethodRenamerAction.setEnabled(p instanceof MethodDeclaration && !(p instanceof ConstructorDeclaration));
		    selectParentAction.setEnabled((p instanceof ProgramElement)
						  && ((ProgramElement)p).getASTParent() != null);
		    
		    contextMenu.removeAll();
		    addElementInfos(contextMenu, p);		    
		}
	    });


	JMenuBar menuBar = new JMenuBar();
	setJMenuBar(menuBar);

	JMenu prjMenu = new JMenu("Project");
	prjMenu.setMnemonic('p');
	menuBar.add(prjMenu);

	SwingUtils.addAction(prjMenu, saveAction);
	prjMenu.addSeparator();		
	SwingUtils.addAction(prjMenu, quitAction);

	JMenu navMenu = new JMenu("Navigate");
	navMenu.setMnemonic('n');
	menuBar.add(navMenu);

	SwingUtils.addAction(navMenu, selectParentAction);
	navMenu.addSeparator();	
	SwingUtils.addAction(navMenu, previousElementOnLevelAction);
	SwingUtils.addAction(navMenu, nextElementOnLevelAction);
	SwingUtils.addAction(navMenu, previousElementAction);
	SwingUtils.addAction(navMenu, nextElementAction);
	navMenu.addSeparator();	
	SwingUtils.addAction(navMenu, backInHistoryAction);
	SwingUtils.addAction(navMenu, forwardInHistoryAction);

	contextMenu = new JMenu("Relations");
	contextMenu.setMnemonic('r');
	menuBar.add(contextMenu);
	
	JMenu toolMenu = new JMenu("Tools");
	toolMenu.setMnemonic('t');
	menuBar.add(toolMenu);

//	SwingUtils.addAction(toolMenu, openCodeCleanerAction);
//	SwingUtils.addAction(toolMenu, openObfuscatorAction);
//	SwingUtils.addAction(toolMenu, openMethodRenamerAction);
//	openMethodRenamerAction.setEnabled(false);

	viewsMenu = new JMenu("Selectors");
	viewsMenu.setMnemonic('s');
	menuBar.add(viewsMenu);

	SwingUtils.addAction(viewsMenu, closeCurrentSelectorAction);
	viewsMenu.addSeparator();
	SwingUtils.addAction(viewsMenu, openSyntaxViewAction);
	SwingUtils.addAction(viewsMenu, openMemberViewAction);
	SwingUtils.addAction(viewsMenu, openHistoryViewAction);
	SwingUtils.addAction(viewsMenu, openChangesViewAction);
	SwingUtils.addAction(viewsMenu, openSearchViewAction);
	SwingUtils.addAction(viewsMenu, openShellViewAction);
	viewsMenu.addSeparator();
	firstSelectorMenuItem = viewsMenu.getItemCount();
	
	JMenu optionsMenu = new JMenu("Options");
	optionsMenu.setMnemonic('o');
	menuBar.add(optionsMenu);

	JMenu memberOptionMenu = new JMenu("Member View");
	memberOptionMenu.setMnemonic('m');
	optionsMenu.add(memberOptionMenu);
	memberOptionShowColorsItem = 
	    SwingUtils.addCheckBoxAction(memberOptionMenu, 
					 changeMemberViewOptionShowColorsAction);
	memberOptionShowColorsItem.setSelected(true);

	JMenu syntaxOptionMenu = new JMenu("Syntax View");
	syntaxOptionMenu.setMnemonic('y');
	optionsMenu.add(syntaxOptionMenu);
	syntaxOptionShowNamesItem = 
	    SwingUtils.addCheckBoxAction(syntaxOptionMenu, 
					 changeSyntaxOptionShowNamesAction);
	syntaxOptionShowNamesItem.setSelected(true);
	syntaxOptionShowColorsItem = 
	    SwingUtils.addCheckBoxAction(syntaxOptionMenu, 
					 changeSyntaxOptionShowColorsAction);
	syntaxOptionShowColorsItem.setSelected(true);
	syntaxOptionShowSyntaxTreesItem =
	    SwingUtils.addCheckBoxAction(syntaxOptionMenu, 
					 changeSyntaxOptionShowSyntaxTreesAction);
	syntaxOptionShowSyntaxTreesItem.setSelected(true);

	JMenu helpMenu = new JMenu("Help");
	helpMenu.setMnemonic('h');
	menuBar.add(Box.createHorizontalGlue());
	menuBar.add(helpMenu);
	SwingUtils.addAction(helpMenu, aboutAction);
	SwingUtils.addAction(helpMenu, helpAction);

	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    quit();
		}
	    });

	selectorsPane = new JTabbedPane(JTabbedPane.TOP); // BOTTOM
	selectorsPane.setMinimumSize(new Dimension(300, 300));

	selectorsPane.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
		    Component view = selectorsPane.getSelectedComponent();
		    if (view == syntaxView || view == memberView || view == historyView) {
			recentlyUsedStandardSelector = view;
		    }
		}
	    });

	openSyntaxView();
	openMemberView();
	openHistoryView();
	openChangesView();

	openCodeView();

	viewToFront(memberView);

	statusBar = new StatusBar(config, model);
	statusBar.getSelectionLabel().addMouseListener(popupActivation);
	statusBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

	/*
	progressBar = new ProgressBar();
	progressBar.setBorder(StatusBar.BORDER);
	progressBar.setFont(progressBar.getFont().deriveFont(9f));
	*/

	config.getSourceInfo().addProgressListener(statusBar);
	config.getSourceFileRepository().addProgressListener(statusBar);

	JPanel elementInfo = new JPanel(new BorderLayout());
	elementInfo.add(codeView);
	//	JPanel barPanel = new JPanel(new BorderLayout());
	//   barPanel.add(statusBar, BorderLayout.NORTH);
	// barPanel.add(progressBar, BorderLayout.CENTER); // leave room for text
	// barPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

	elementInfo.add(statusBar, BorderLayout.NORTH);
	mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, selectorsPane, elementInfo);
	// mainPane.setOneTouchExpandable(true);
	
	getContentPane().add(mainPane);
	setSize(1020, 760);
	
	config.getChangeHistory().addModelUpdateListener(new ModelUpdateListener() {
		long startTime;
		
		public void modelUpdating(EventObject event) {
		    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		    startTime = System.currentTimeMillis();
		}

		public void modelUpdated(EventObject event) {
		    lastUpdateDuration = System.currentTimeMillis() - startTime;
		    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		    ModelElement e = model.getSelectedElement();
		    boolean selectionRemoved = (e != null) && 
			RecoderUtils.isModelPart(config, e);
		    if (selectionRemoved) {
			ModelElement root = model.getRoot();
			if (RecoderUtils.isModelPart(config, root)) {
			    model.setSelectedElement(root);
			} else {
			    model.setSelectedElement(null);
			}
		    }
		    Enumeration enum = allViews.elements();
		    while (enum.hasMoreElements()) {
			Object v = enum.nextElement();
			if (v instanceof SelectionView) {
			    ((SelectionView)v).modelUpdated(selectionRemoved);
			}
		    }
		}
	    });
	config.getChangeHistory().addChangeHistoryListener(changesView);
    }

    public CrossReferenceServiceConfiguration getServiceConfiguration() {
	return config;
    }

    public SelectionModel getModel() {
	return model;
    }

    public StatusBar getStatusBar() {
	return statusBar;
    }


    private Hashtable view2item = new Hashtable();

    void registerView(final Component view, boolean toMenu) {
	allViews.add(view);
	if (toMenu) {
	    int selectors = viewsMenu.getItemCount() - firstSelectorMenuItem;
	    if (selectors < 9) {
		JMenuItem item = new JMenuItem();
		item.setText((1 + selectors) + " " + view.getName());
		item.setMnemonic((char)('1' + selectors));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    viewToFront(view);
			}
		    });
		viewsMenu.add(item);
		view2item.put(view, item);
	    }
	}
    }

    public void addView(final Component view, boolean toMenu) {	
	registerView(view, toMenu);
	selectorsPane.add(view);
	viewToFront(view);
	if (view instanceof ElementSelector) {
	    JButton closeButton = ((ElementSelector)view).getCloseButton();
	    if (closeButton != null) {
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    closeView(view);
			}
		    });
	    }
	}
	// mainPane.resetToPreferredSizes();
    }

    public void closeView(Component view) {
	allViews.remove(view);
	if (view instanceof SelectionView) {
	    ((SelectionView)view).setModel(null);
	}
	JMenuItem item = (JMenuItem)view2item.get(view);
	if (item != null) {
	    int i;
	    int s = viewsMenu.getItemCount();
	    for (i = firstSelectorMenuItem; i < s; i += 1) {
		if (viewsMenu.getItem(i) == item) {
		    break;
		}
	    }
	    if (i < s) {
		viewsMenu.remove(item);
		for (s -= 1; i < s; i += 1) {
		    JMenuItem next = viewsMenu.getItem(i);
		    char id = next.getLabel().charAt(0);
		    id -= 1;
		    next.setLabel(id + next.getLabel().substring(1));
		    next.setMnemonic(id);
		}
	    }
	}
	while (view.getParent() != selectorsPane) {
	    view = view.getParent();
	}
	selectorsPane.remove(view);
	if (recentlyUsedStandardSelector == null) {
	    recentlyUsedStandardSelector = memberView;
	}
	selectorsPane.setSelectedComponent(recentlyUsedStandardSelector);
    }

    /*
      Implement focus management. E.g. for search.
     */
    public void viewToFront(final Component view) {
	selectorsPane.setSelectedComponent(view);
	view.requestFocus();
    }

    public CrossReferenceServiceConfiguration getConfiguration() {
	return config;
    }


    final MouseListener popupActivation = new MouseAdapter() {

	    void showPopup(MouseEvent e) {
		ModelElement m = model.getSelectedElement();
		if (m == null) {
		    return;
		}
		JPopupMenu popup = new JPopupMenu();
		addElementInfos(popup, m);
		popup.show(e.getComponent(), e.getX(), e.getY());
	    }
	    
	    public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
		    showPopup(e);
		} else if ((e.getModifiers() & MouseEvent.BUTTON2_MASK) != 0) {
		    selectParent();
		}
	    }
	    
	    public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
		    showPopup(e);
		}
	    }
	};    

    
    private final Action selectParentAction = new AbstractAction() {
	    {
		putValue(NAME, "Goto Parent");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
		putValue(SMALL_ICON, Resources.UP_ICON);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt PAGE_UP"));
	    }

	    public void actionPerformed(ActionEvent e) {
		selectParent();
	    }
	}; 


    private void selectParent() {
	ModelElement p = model.getSelectedElement();
	if (p instanceof ProgramElement) {
	    p = ((ProgramElement)p).getASTParent();
	    if (p != null) {
		model.setSelectedElement(p);
	    }
	}
    }

    protected final Action previousElementAction = new AbstractAction() {
	    {
		putValue(NAME, "Goto Previous Descending");
		// putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift alt UP"));
	    }
	    
	    public void actionPerformed(ActionEvent e) {
		ModelElement p = model.getSelectedElement();
		if (p instanceof ProgramElement) {
		    p = RecoderUtils.getPreviousInSource((ProgramElement)p);
		    if (p != null) {
			model.setSelectedElement(p);
		    }
		}
	    }
	};

    protected final Action nextElementAction = new AbstractAction() {
	    {
		putValue(NAME, "Goto Next Descending");
		// putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift alt DOWN"));
	    }
	    
	    public void actionPerformed(ActionEvent e) {
		ModelElement p = model.getSelectedElement();
		if (p instanceof ProgramElement) {
		    p = RecoderUtils.getNextInSource((ProgramElement)p);
		    if (p != null) {
			model.setSelectedElement(p);
		    }
		}
	    }
	};

    protected final Action previousElementOnLevelAction = new AbstractAction() {
	    {
		putValue(NAME, "Goto Previous");
		// putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_V));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt UP"));
	    }
	    
	    public void actionPerformed(ActionEvent e) {
		ModelElement p = model.getSelectedElement();
		if (p instanceof ProgramElement) {
		    p = RecoderUtils.getPreviousOnLevel((ProgramElement)p);
		    if (p != null) {
			model.setSelectedElement(p);
		    }
		}
	    }
	};

    protected final Action nextElementOnLevelAction = new AbstractAction() {
	    {
		putValue(NAME, "Goto Next");
		// putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt DOWN"));
	    }
	    
	    public void actionPerformed(ActionEvent e) {
		ModelElement p = model.getSelectedElement();
		if (p instanceof ProgramElement) {
		    p = RecoderUtils.getNextOnLevel((ProgramElement)p);
		    if (p != null) {
			model.setSelectedElement(p);
		    }
		}
	    }
	};

    protected final Action backInHistoryAction = new AbstractAction() {
	    {
		putValue(NAME, "Goto Previous Visited");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt LEFT"));
		putValue(SMALL_ICON, Resources.BACK_ICON);
	    }
	    
	    public void actionPerformed(ActionEvent e) {
		historyView.back();
	    }
	};

    protected final Action forwardInHistoryAction = new AbstractAction() {
	    {
		putValue(NAME, "Goto Next Visited");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt RIGHT"));
		putValue(SMALL_ICON, Resources.FORWARD_ICON);
	    }
	    
	    public void actionPerformed(ActionEvent e) {
		historyView.forward();
	    }
	};


    private final Action aboutAction = new AbstractAction() {
	    {
		putValue(NAME, "About...");
	    }

	    static final String MESSAGE = "Sourcerer is a demonstration for the RECODER toolkit.\nThis software is LGPL'ed and written by Andreas Ludwig in 2001/2002.\nAbsolutely no warranty given - any usefulness on your behalf is accidental.";

	    public void actionPerformed(ActionEvent e) {
		JTextArea textArea = new JTextArea(15, 40);
		textArea.setEditable(false);
		textArea.setBackground(getBackground());
		textArea.setLineWrap(true);
		textArea.setText(MESSAGE + "\nTime required for last update: " + lastUpdateDuration + "ms");
		textArea.setCaretPosition(0);
		JOptionPane.showMessageDialog(Main.this, new JScrollPane(textArea));
	    }
	};

    private final Action helpAction = new AbstractAction() {
	    {
		putValue(NAME, "Help...");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F1"));
	    }
	    
	    public void actionPerformed(ActionEvent e) {

		JOptionPane.showMessageDialog(Main.this, new MiniBrowser(Resources.HELP_PAGE_URL), "SOURCERER Help", JOptionPane.PLAIN_MESSAGE);
	    }
	};

//    private final Action openCodeCleanerAction = new AbstractAction() {
//
//	    {
//		putValue(NAME, "Code Cleaner...");
//		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
//	    }
//
//	    public void actionPerformed(ActionEvent e) {
//		CodeCleaner cleaner = new CodeCleaner(Main.this);
//		addView(cleaner, true);
//	    }
//	};


//    private final Action openObfuscatorAction = new AbstractAction() {
//
//	    {
//		putValue(NAME, "Global Obfuscator...");
//		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
//	    }
//
//	    public void actionPerformed(ActionEvent e) {
//		Obfuscator obfus = new Obfuscator(Main.this);
//		addView(obfus, true);
//	    }
//	};
//
//    
//    private final Action openMethodRenamerAction = new AbstractAction() {
//
//	    {
//		putValue(NAME, "Rename Method...");
//		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_M));
//	    }
//
//	    public void actionPerformed(ActionEvent e) {
//		if (model.getSelectedElement() instanceof MethodDeclaration) {
//		    MethodRenamer renamer = new MethodRenamer(Main.this, (MethodDeclaration)model.getSelectedElement());
//		    addView(renamer, true);
//		}
//	    }
//	};

    
    private void openSyntaxView() {
	if (syntaxView == null) {
	    syntaxView = new SyntaxView(model, config);
	    syntaxView.getTree().addMouseListener(popupActivation);
	    addView(syntaxView, false);
	} else {
	    viewToFront(syntaxView);
	}
    }


    private final Action openSyntaxViewAction = new AbstractAction() {
	    {
		putValue(NAME, "Syntax Forest");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_Y));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Y"));
	    }

	    public void actionPerformed(ActionEvent e) {
		openSyntaxView();
	    }
	};


    
    private void openMemberView() {
	if (memberView == null) {
	    memberView = new MemberView(model, config.getNameInfo());    
	    memberView.getTree().addMouseListener(popupActivation);
	    memberView.setUsingColors(memberOptionShowColorsItem.isSelected());
	    addView(memberView, false);
	} else {
	    viewToFront(memberView);
	}
    }

    private final Action openMemberViewAction = new AbstractAction() {
	    {
		putValue(NAME, "Member Hierarchy");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_M));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control M"));
	    }

	    public void actionPerformed(ActionEvent e) {
		openMemberView();
	    }
	};


    private void openCodeView() {
	if (codeView == null) {
	    codeView = new SourceCodeView();
	    codeView.getTextArea().addMouseListener(popupActivation);
	    codeView.setModel(model);
	    registerView(codeView, false);
	}
    }

    /**
       This proxy class is independent of its parent class and 
       will be used only if the BeanShellView class is resolvable.
       The class loader needs the bsh.Interpreter in order to do so.
     */
    private static class BeanShellFactory {

	public static ElementSelector createBeanShell(Main main) {
	    return new BeanShellView(main);
	}	
    }    

    private void openShellView() {
	if (hasBeanShell()) {
	    addView(BeanShellFactory.createBeanShell(this), false);
	}
    }


    private final Action openShellViewAction = new AbstractAction() {
	    {
		putValue(NAME, "BeanShell Console");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_B));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control B"));
		this.setEnabled(hasBeanShell());
	    }

	    public void actionPerformed(ActionEvent e) {
		openShellView();
	    }
	};

    
    private void openHistoryView() {
	if (historyView == null) {
	    historyView = new HistoryView(model);
	    historyView.getList().addMouseListener(popupActivation);
	    addView(historyView, false);
	} else {
	    viewToFront(historyView);
	}
    }
	
    private final Action openHistoryViewAction = new AbstractAction() {
	    {
		putValue(NAME, "Visit History");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_H));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control H"));
	    }

	    public void actionPerformed(ActionEvent e) {
		openHistoryView();
	    }
	};

    private void openChangesView() {
	if (changesView == null) {
	    changesView = new ChangeEventView(model);
	    addView(changesView, false);
	} else {
	    viewToFront(changesView);
	}
    }

    private final Action openChangesViewAction = new AbstractAction() {
	    {
		putValue(NAME, "Change History");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
	    }

	    public void actionPerformed(ActionEvent e) {
		openChangesView();
	    }
	};


    private final Action openSearchViewAction = new AbstractAction() {
	    {
		putValue(NAME, "Element Search");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_F));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F"));
	    }
	    
	    public void actionPerformed(ActionEvent e) {
		ProgramModelElementMutableList candidRoots = 
		    new ProgramModelElementArrayList(128);
		candidRoots.add(config.getNameInfo().getNamespaces());
		TypeList types =  config.getNameInfo().getTypes();
		for (int i = types.size() - 1; i >= 0; i--) {
		    if (!(types.getType(i) instanceof ClassType)) {
			candidRoots.add(types.getType(i));
		    }
		}
		addView(new ElementSearch(model, candidRoots), true);
	    }
	};
    

    private final Action closeCurrentSelectorAction = new AbstractAction() {
	    {
		putValue(NAME, "Close Current Selector");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F4"));
	    }
	    
	    public void actionPerformed(ActionEvent e) {
		Component c = selectorsPane.getSelectedComponent();
		if (c instanceof ElementSelector) {
		    closeView(c);
		}
	    }
	};

    private final Action saveAction = new AbstractAction() {
	    {
		putValue(NAME, "Save Project...");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
		putValue(SMALL_ICON, Resources.SAVEAS_ICON);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
	    }

	    public void actionPerformed(ActionEvent e) {
		String output = config.getProjectSettings().getProperty(PropertyNames.OUTPUT_PATH);
		JTextField jtf = new JTextField();
		// TO DO: create panel w/ label, text field and checkbox
		// for "create file even though unmodified"
		File f = new File(output);
		if (!f.isDirectory()) {
		    output = "";
		} else {
		    try {
			output = f.getCanonicalPath();
		    } catch (IOException ioe) {
			output = f.getAbsolutePath();
		    }
		}
		jtf.setText(output);
		if (JOptionPane.showConfirmDialog(Main.this,
						  jtf,
						  "Save Project to Directory...",
						  JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
		    f = new File(jtf.getText());
		    if (!f.isFile()) {
  		        // TO DO: query if no changes (?)
			// TO DO: check if output dir is part of input path
			// -- if so, warn the user (overwrite originals???)
			config.getProjectSettings().setProperty(PropertyNames.OUTPUT_PATH, jtf.getText());
			
			SourceFileRepository sfr = config.getSourceFileRepository();
			try {
		    // TO DO: if file does not yet exist, query & mkdirs()
		    // TO DO: if files to write do exist, query for overwrite
		    // (optionally: OVERWRITE ALL) - this might be nonsense
		    // use two-phased writing: write copy, if successful, replace old version
			    sfr.printAll(true);
			    sfr.cleanUp();
			} catch (IOException ioe) {
			    JOptionPane.showMessageDialog(Main.this,
							  ioe,
							  "Error While Saving",
							  JOptionPane.ERROR_MESSAGE);
			}
		    }
		}		
		/*
		  Save to |_________________________|  <--- output.path
                  [x] create all files, also if unmodified
		 */
	    }
	};

    private final Action changeMemberViewOptionShowColorsAction = new AbstractAction() {
	    {
		putValue(NAME, "Metatype Colors");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
	    }

	    public void actionPerformed(ActionEvent e) {
		if (memberView != null) {
		    memberView.setUsingColors(memberOptionShowColorsItem.isSelected());
		}
	    }
	};

    
    private final Action changeSyntaxOptionShowNamesAction = new AbstractAction() {
	    {
		putValue(NAME, "Show Names");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
	    }

	    public void actionPerformed(ActionEvent e) {
		if (syntaxView != null) {
		    syntaxView.setShowingNames(syntaxOptionShowNamesItem.isSelected());
		}
	    }
	};

    private final Action changeSyntaxOptionShowColorsAction = new AbstractAction() {
	    {
		putValue(NAME, "Metatype Colors");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
	    }

	    public void actionPerformed(ActionEvent e) {
		syntaxView.setUsingColors(syntaxOptionShowColorsItem.isSelected());
	    }
	};


    private final Action changeSyntaxOptionShowSyntaxTreesAction = new AbstractAction() {
	    {
		putValue(NAME, "Syntax Trees");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_Y));
	    }

	    public void actionPerformed(ActionEvent e) {
		syntaxView.setShowingSyntaxTrees(syntaxOptionShowSyntaxTreesItem.isSelected());
	    }
	};


    private void quit() {
	int doit = JOptionPane.showConfirmDialog(Main.this, "Really quit?", "Leave Program", JOptionPane.YES_NO_OPTION);
	if (doit == JOptionPane.YES_OPTION) {
	    dispose();
	    System.exit(0);
	}
    }

    private final Action quitAction = new AbstractAction() {
	    {
		putValue(NAME, "Quit");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_Q));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
	    }

	    public void actionPerformed(ActionEvent e) {
		quit();
	    }
	};



    final static int MENU_PAGE_SIZE = 20;
    final static int MAX_MENU_PAGES = 5;

    void addElementInfos(JComponent menu, ModelElement m) {
	if (m == null) {
	    return;
	}
	Vector items = new Vector();	
	final String SEPARATOR = "---";
	
	items.add(createTypeInfo(m));	
	if (m instanceof ClassType) {
	    items.add(createExtendsInfo((ClassType)m));
	    items.add(createImplementsInfo((ClassType)m));
	}
	if (m instanceof Method) {
	    items.add(createParameterTypesInfo((Method)m));
	    items.add(createExceptionTypesInfo((Method)m));
	}
	items.add(SEPARATOR);

	items.add(createContainersInfo(m));
	if (m instanceof ProgramElement) {
	    items.add(createParentsInfo((ProgramElement)m));
	}
	if (m instanceof NonTerminalProgramElement) {
	    items.add(createChildrenInfo((NonTerminalProgramElement)m));
	}
	if (m instanceof ProgramModelElement) {
	    items.add(createReferersInfo((ProgramModelElement)m));
	}
	if (m instanceof Reference) {
	    items.add(createRefersToInfo((Reference)m));
	}
	
	items.add(SEPARATOR);
	
	items.add(createTypesInfo(m));
	if (m instanceof ClassType) {
	    items.add(createFieldsInfo((ClassType)m));
	    items.add(createConstructorsInfo((ClassType)m));
	    items.add(createMethodsInfo((ClassType)m));
	}
	
	items.add(SEPARATOR);
	
	if (m instanceof ClassType) {	    
	    items.add(createSupertypesInfo((ClassType)m)); 
	    items.add(createAllSupertypesInfo((ClassType)m)); 
	    items.add(createSubtypesInfo((ClassType)m));
	    items.add(createAllSubtypesInfo((ClassType)m));
	}
	
	if (m instanceof MemberDeclaration) {	
	    items.add(createExitsInfo((MemberDeclaration)m));
	}
	int c;
	for (c = items.size() - 1; c >= 0; c -= 1) {
	    Object o = items.elementAt(c);
	    if (o != null && o != SEPARATOR) {
		break;
	    }
	}
	int itemsBeforeSeparator = 0;
	for (int i = 0; i <= c; i += 1) {
	    Object o = items.elementAt(i);
	    if (o == SEPARATOR) {
		if (itemsBeforeSeparator > 0 && i < c - 1) {
		    if (menu instanceof JPopupMenu) {
			((JPopupMenu)menu).addSeparator();
		    } else if (menu instanceof JMenu) {
			((JMenu)menu).addSeparator();
		    } else if (menu instanceof JToolBar) {
			((JToolBar)menu).addSeparator();
		    }
		    itemsBeforeSeparator = 0;
		}
	    } else if (o instanceof Component) {
		menu.add((Component)o);
		itemsBeforeSeparator += 1;
	    }
	}	
    }

    public ProgramElementList getReferences(ProgramModelElement dest) {
	ProgramElementList list = null;
	CrossReferenceSourceInfo xsi = config.getCrossReferenceSourceInfo();
	if (dest instanceof Method) {
	    list = xsi.getReferences((Method)dest);
	} else if (dest instanceof Constructor) {
	    list = xsi.getReferences((Constructor)dest);
	} else if (dest instanceof Variable) {
	    list = xsi.getReferences((Variable)dest);
	} else if (dest instanceof Type) {
	    list = xsi.getReferences((Type)dest);
	} else if (dest instanceof Namespace) {
	    list = xsi.getReferences((Namespace)dest);
	}
	return list;
    }

    public ProgramModelElement getTarget(Reference r) {
	SourceInfo si = config.getSourceInfo();
	if (r instanceof TypeReference) {
	    return si.getType((TypeReference)r);
	} else if (r instanceof VariableReference) {
	    return si.getVariable((VariableReference)r);
        } else if (r instanceof MethodReference) {
            return si.getMethod((MethodReference)r);
        } else if (r instanceof DelegateCallReference) {
            return si.getVariable((DelegateCallReference)r);
	} else if (r instanceof ConstructorReference) {
	    return si.getConstructor((ConstructorReference)r);
	} else if (r instanceof NamespaceReference) {
	    return si.getNamespace((NamespaceReference)r);
	}
	return null;
    }
    
    JComponent createStringProperty(String title, String description, String value) {
	if (value == null || value.length() == 0) {
	    value = " ";
	}
	JMenuItem label = new JMenuItem(title + ": " + value);
	label.setEnabled(false);
	label.setToolTipText(description);
	return label;
    }
    
    // ignored: description
    JMenuItem createElementProperty(String title, char mnemonic, String description, final ModelElement element, String format) {
	String value;
	if (format.equals("%N") && element instanceof ProgramModelElement) {
	    value = RecoderUtils.getNonTrivialFullName((ProgramModelElement)element);
	} else {
	    value = Format.toString(format, element);
	}
	JMenuItem item = new JMenuItem(title + ": " + value);
	item.setMnemonic(mnemonic);
	item.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    model.setSelectedElement(element);
		}
	    });
	return item;
    }

    // ignored: description
    JMenuItem createElementListProperty(String title, char mnemonic, String description, ModelElement origin, ModelElementList values, String format, boolean autoSort) {
	return createElementListProperty(title, mnemonic, description, origin, values, format, title, format, autoSort);
    }

    JMenuItem createElementListProperty(final String title, char mnemonic, String description, final ModelElement origin, final ModelElementList values, final String format, String listTitle, String listFormat, final boolean autoSort) {
	String fullTitle = title + " (" + values.size() + ")";
	if (values.isEmpty()) {
	    JMenuItem item = new JMenuItem(fullTitle);
	    item.setEnabled(false);
	    return item;
	}	    
	JMenu menu = new JMenu(fullTitle);
	menu.setMnemonic(mnemonic);

	ModelElement[] a = values.toModelElementArray();
	if (autoSort) {
	    Sorting.heapSort(a, new Order.Lexical() {
		    protected String toString(Object x) {
			return Format.toString(format, (ModelElement)x);
		    }
		});
	}
	JMenuItem openList = new JMenuItem("Open Selector...", Resources.SHOW_ICON);
	// (" + a.length + " elements)..."
	openList.setMnemonic('O');
	openList.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    // add element
		    addView(new ListSelector(model, title, values, format, autoSort, title + " of %c", origin, true), true);
		}
	    });
	//menu.addSeparator();
	menu.add(openList);
	menu.addSeparator();
	JMenu orig = menu;
	int len = Math.min(MAX_MENU_PAGES * MENU_PAGE_SIZE, a.length);
	for (int i = 0; i < len; i += 1) {
	    final ModelElement element = a[i];
	    JMenuItem it = new JMenuItem(Format.toString(format, element));
	    it.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			model.setSelectedElement(element);
		    }
		});
	    menu.add(it);
	    if (i > 0 && i % MENU_PAGE_SIZE == 0) {
		int next = i + 1;
		int last = Math.min(len, i + MENU_PAGE_SIZE);
		JMenu more = new JMenu("" + next + "..." + last);
		menu.add(more);
		menu = more;		
	    } else if (i == len - 1 && MAX_MENU_PAGES * MENU_PAGE_SIZE < a.length) {
		JMenuItem nomore = new JMenuItem("(further " + (a.length - len) + " elements truncated)");
		nomore.setEnabled(false);
		menu.add(nomore);
	    }
	}
	return orig;
    }



    JComponent createTypeInfo(ModelElement m) {
	Type t = null;
	if (m instanceof Expression) {
	    t = config.getSourceInfo().getType((Expression)m);
	    if (t != null) { // void method reference
		return createElementProperty("Type", 'y', "Type of the expression", t, "%N");
	    }
	} else if (m instanceof Variable) {
	    t = ((Variable)m).getType();
	    return createElementProperty("Type", 'y', "Type of the variable", t, "%N");
	} else if (m instanceof ArrayType) {
	    t = ((ArrayType)m).getBaseType();
	    return createElementProperty("Base Type", 'y', "Base type of the array type", t, "%N");
	} else if ((m instanceof Method) && !(m instanceof Constructor)) {
	    t = ((Method)m).getReturnType();
	    if (t == null) {
		return createStringProperty("Return Type", "Return type of the method", "void");
	    } else {
		return createElementProperty("Return Type", 'y', "Return type of the method", t, "%N");
	    }
	}
	return null;
    }


    JComponent createParameterTypesInfo(Method m) {
	return createElementListProperty("Parameter Types", '\0', "Parameter types of the method or constructor", (Method)m, ((Method)m).getSignature(), "%N", false);
    }

    JComponent createExceptionTypesInfo(Method m) {
	return createElementListProperty("Throws", 'w', "Declared exceptions of a method or constructor", (Method)m, ((Method)m).getExceptions(), "%N", true);
    }

    JComponent createParentsInfo(ProgramElement m) {
	ModelElementMutableList list = new ModelElementArrayList();
	ProgramElement p = (ProgramElement)m;
	do {
	    p = p.getASTParent();
	    if (p != null) {
		list.add(p);
	    }
	} while (p != null);
	return createElementListProperty("Parents", 'a', "Syntactic parents of the element", m, list, "%c", false);
    }

    JComponent createContainersInfo(ModelElement m) {
	if (m instanceof PrimitiveType || m instanceof ArrayType) {
	    return null;
	}
	ModelElementMutableList list = new ModelElementArrayList();
	if (m instanceof ProgramElement && !(m instanceof ProgramModelElement)) {
	    m = RecoderUtils.getAssociatedProgramModelElement((ProgramElement)m);
	    list.add(m);
	}
	if (m instanceof ProgramModelElement) {
	    ProgramModelElement p = (ProgramModelElement)m;
	    do {
		p = RecoderUtils.getContainer(p);
		if (p != null) {
		    list.add(p);
		}
	    } while (p != null);
	    return createElementListProperty("Containers", 'c', "Logical containers of the element", m, list, "%c \"%n\"", false);
	}
	return null;
    }

    JComponent createChildrenInfo(NonTerminalProgramElement m) {
	NonTerminalProgramElement nt = (NonTerminalProgramElement)m;
	int s = nt.getChildCount();
	ProgramElementMutableList ch = new ProgramElementArrayList();
	for (int i = 0; i < s; i += 1) {
	    ch.add(nt.getChildAt(i));
	}
	return createElementListProperty("Children", 'l', "Syntactic children of the element", m, ch, "%c %4p", false);
    }

    JComponent createSupertypesInfo(ClassType m) {
	ClassTypeList list = ((ClassType)m).getSupertypes();
	return createElementListProperty("Supertypes", '\0', "Direct supertypes of the class type", m, list, "%N", true);
    }

    JComponent createAllSupertypesInfo(ClassType m) {
	ClassTypeList list = ((ClassType)m).getAllSupertypes();
	ClassTypeMutableList list2 = new ClassTypeArrayList();
	list2.add(list);
	list2.remove(0);
	return createElementListProperty("All Supertypes", '\0', "Transitive supertypes of the class type (in topological order)", m, list2, "%N", false);
    }

    
    JComponent createExtendsInfo(ClassType m) {
	if (!((ClassType)m).isInterface()) {
	    return createElementProperty("Extends", 'e', "Superclass of the class", TypeKit.getSuperClass(config.getNameInfo(), (ClassType)m), "%N");
	} else {
	    ClassTypeList list = ((ClassType)m).getSupertypes();
	    return createElementListProperty("Extends", 'e', "Extended interfaces of the interface", m, list, "%N", true);
	}
    }

    // no interface, else returns null
    JComponent createImplementsInfo(ClassType m) {
	if (!((ClassType)m).isInterface()) {
	    ClassTypeList list = ((ClassType)m).getSupertypes();
	    ClassTypeMutableList list2 = new ClassTypeArrayList();
	    for (int i = list.size() - 1; i >= 0; i -= 1) {
		if (list.getClassType(i).isInterface()) {
		    list2.add(list.getClassType(i));
		}
	    }
	    return createElementListProperty("Implements", 'i', "Implemented interfaces of the class", m, list2, "%N", true);
	}	    
	return null;
    }

    // ClassTypeContainer or CompilationUnits, else returns null
    JComponent createTypesInfo(ModelElement m) {
	DeclaredTypeList list;
	if (m instanceof DeclaredTypeContainer) {
	    list = ((DeclaredTypeContainer)m).getDeclaredTypes();
	} else if (m instanceof CompilationUnit) {
	    list = ((CompilationUnit)m).getTypesInScope();
	} else {
	    return null;
	}
	if (list == null) {
	    list = DeclaredTypeList.EMPTY_LIST;
	}
	return createElementListProperty("Types", 'y', "Types defined by the element", m, list, "%n", true);
   }

    JComponent createFieldsInfo(ClassType m) {
	MemberList list = ((ClassType)m).getFields();
	if (list == null) {
	    list = MemberList.EMPTY_LIST;
	}
	return createElementListProperty("Fields", 'f', "Fields defined by the class type", m, list, "%n", true);
    }

    JComponent createConstructorsInfo(ClassType m) {
	MemberList list = ((ClassType)m).getConstructors();
	if (list == null) {
	    list = MemberList.EMPTY_LIST;
	}
	return createElementListProperty("Constructors", 'u', "Constructors defined by the class", m, list, "%m", true);
    }

    JComponent createMethodsInfo(ClassType m) {
	MemberList list = ((ClassType)m).getMethods();
	if (list == null) {
	    list = MemberList.EMPTY_LIST;
	}
	return createElementListProperty("Methods", 'm', "Methods defined by the class type", m, list, "%m", true);
    }

    JComponent createRefersToInfo(final Reference m) {
	ProgramModelElement target = getTarget(m);
	if (target != null) {
	    // void type reference
	    return createElementProperty("Refers To", 'r', "Target of the reference", target, "%N");
	} else {
	    return createStringProperty("Refers To", "Target of the reference", "void");
	}
    }

    JComponent createReferersInfo(final ProgramModelElement m) {
	ProgramElementList list = getReferences(m);
	return createElementListProperty("Referers", 'r', "All known references to the element", (ProgramModelElement)m, list, "%u %4p", "Referers to " + Format.toString("\"%n\"", m), "%u %4p", true);
    }

    JComponent createSubtypesInfo(ClassType m) {
	ClassTypeList list = m.getProgramModelInfo().getSubtypes(m);
	return createElementListProperty("Subtypes", '\0', "Direct subtypes of the class type", m, list, "%N", true);
    }
    
     JComponent createAllSubtypesInfo(ClassType m) {
	ClassTypeList list = m.getProgramModelInfo().getAllSubtypes(m);
	return createElementListProperty("All Subtypes", '\0', "Transitive subtypes of the class type (in topological order)", m, list, "%N", false);
    }

    JComponent createExitsInfo(MemberDeclaration m) {
	if (((m instanceof MethodDeclaration) && (!((MethodDeclaration)m).isAbstract()))) {
	    StatementList list = StatementKit.getExits(m, config.getSourceInfo());
	    return createElementListProperty("Method Exits", 'x', "All statements that exit the method, constructor or initializer", m, list, "%4p %c", true);
	}
	return null;
    }
    
}

