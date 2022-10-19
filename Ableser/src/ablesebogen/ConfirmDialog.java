package ablesebogen;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

class ConfirmDialog extends JDialog {

	  public static final boolean OK_OPTION = true;
	  public static final boolean CANCEL_OPTION = false;

	  private boolean result = false;

	  JPanel content;

	  public ConfirmDialog(Frame parent) {
	    super(parent, true);

	    JPanel gui = new JPanel(new BorderLayout(3, 3));
	    gui.setBorder(new EmptyBorder(5, 5, 5, 5));
	    content = new JPanel(new BorderLayout());
	    gui.add(content, BorderLayout.CENTER);
	    JPanel buttons = new JPanel(new FlowLayout(4));
	    gui.add(buttons, BorderLayout.SOUTH);

	    JButton ok = new JButton("Speichern");
	    buttons.add(ok);
	    ok.addActionListener(e->{
	        result = OK_OPTION;
	        setVisible(false);
	    });

	    JButton cancel = new JButton("Abbrechen");
	    buttons.add(cancel);
	    cancel.addActionListener(e->{
	        result = CANCEL_OPTION;
	        setVisible(false);
	    });
	    setContentPane(gui);
	  }
	  public boolean showConfirmDialog(JComponent child, String label) {
		JLabel zLabel = new JLabel(label);
		child.add(zLabel);
	    setTitle("COPYRIGHT KERSCHER!");
	    content.removeAll();
	    content.add(child, BorderLayout.CENTER);
	    pack();
	    setLocationRelativeTo(getParent());
	    setVisible(true);
	    return result;
	  }
	}
