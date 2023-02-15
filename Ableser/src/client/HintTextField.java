package client;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

@SuppressWarnings("serial")
class HintTextField extends JTextField implements FocusListener {

	private final String hint;
	private boolean showingHint;

	public HintTextField(final String hint) {
		super(hint);
		this.hint = hint;
		setshowingHint(true);
		super.addFocusListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (this.getText().isEmpty()) {
			super.setText("");
			setshowingHint(false);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (this.getText().isEmpty()) {
			super.setText(hint);
			setshowingHint(true);
		}
	}

	private void setshowingHint(boolean h) {
		showingHint=h;
		if (h) {
			this.setForeground(Color.gray);			
		} else {
			this.setForeground(Color.black);
		}
		
	}
	
	@Override
	public String getText() {
		return showingHint ? "" : super.getText();
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		setshowingHint(text.length()<1);
	}
	
}