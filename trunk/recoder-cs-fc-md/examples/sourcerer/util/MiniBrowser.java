package sourcerer.util;

import java.awt.*;
import java.net.URL;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;

public class MiniBrowser extends JPanel {
    
    private JLabel label;
    private JButton backButton;

    public MiniBrowser(final URL initialURL) {
	super(new BorderLayout());
	label = new JLabel("Please wait...", JLabel.CENTER);
	label.setPreferredSize(new Dimension(600, 400));	
	add(label);
	new Thread(new Runnable() {
	    public void run() {			    
		try {
		    final JEditorPane text = new JEditorPane(initialURL);
		    text.setEditable(false);
		    text.addHyperlinkListener(new HyperlinkListener() {
			    public void hyperlinkUpdate(HyperlinkEvent hle) {
				if (hle.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				    try {
					text.setPage(hle.getURL());
				    } catch (IOException ioe) {
				    }
				}
			    }
			});
		    SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
				remove(label);
				add(new JScrollPane(text));
				revalidate();
			    }
			});
		} catch (IOException ioe) {
		    label.setText(ioe.getMessage());
		    return;
		}
	    }
	    }).start();
    }

    public static void main(String[] a) throws Exception {
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	File f = new File(a[0]);
	frame.getContentPane().add(new MiniBrowser(f.toURL()));
	frame.pack();
	frame.setVisible(true);
    }
}
				 
