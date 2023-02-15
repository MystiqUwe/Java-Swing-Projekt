package client;

import java.awt.LayoutManager;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class JAblesebogenPanel extends JPanel {

	protected Ablesebogen baseFrame;

	public JAblesebogenPanel(LayoutManager layout) {
		super(layout);
	}

	/**
	 * Öffnet die Seite die zu diesem Objekt gehört
	 * 
	 * @return Ob die Seite geöffnet werden soll
	 */
	public abstract boolean activate(Object eOpts);
	
	public abstract void afterActivate(Object eOpts);
	
	public abstract boolean showFilter();
	
	public abstract void filter(String filter);

}
