package client.zaehlerart;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.Ablesebogen;
import client.JAblesebogenPanel;
import client.Util;
import dataEntities.Zaehlerart;

@SuppressWarnings("serial")
public class ZaehlerartenInPanel extends JAblesebogenPanel {

	private Zaehlerart toEdit;
	public JTextField nameField;
	private JTextField warnField;

	private JButton toAllAblesungButton;
	private JButton toNewAblesungButton;

	public ZaehlerartenInPanel(Ablesebogen bFrame) {
		super(new BorderLayout());

		this.baseFrame = bFrame;
		// Grid
		JPanel grid = new JPanel(new GridLayout(6, 2));
		this.add(grid, BorderLayout.CENTER);
		grid.setBorder(new EmptyBorder(0, 10, 0, 10));
		
		
		nameField = new JTextField();
		warnField = new JTextField();

		grid.add(new JLabel("Name"));
		grid.add(nameField);
		
		grid.add(new JLabel("Warnvalue"));
		grid.add(warnField);

		grid.add(new JLabel(""));
		grid.add(new JLabel(""));

		grid.add(new JLabel(""));
		grid.add(new JLabel(""));

		grid.add(new JLabel(""));
		grid.add(new JLabel(""));

		grid.add(new JLabel(""));
		grid.add(new JLabel(""));

		toAllAblesungButton= new JButton("Ablesungen anzeigen");
		toNewAblesungButton= new JButton("neue Ablesungen");
	
//		grid.add(toAllAblesungButton);
//		grid.add(toNewAblesungButton);
		
		ArrayList<JComponent> tabOrder = new ArrayList<JComponent>();
		tabOrder.add(nameField);
		tabOrder.add(warnField);
		Util.handleTabOrder(tabOrder, e -> {
			return save();
		});

		// untere Leiste
		JPanel southPanel = new JPanel(new GridLayout(2, 1));
		JPanel ablesungPanel = new JPanel(new GridLayout(1, 0));
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
	
		this.add(southPanel, BorderLayout.SOUTH);
		southPanel.add(ablesungPanel);
		southPanel.add(buttonPanel);

		JButton saveButton = new JButton("Speichern");
		JButton toOutButton = new JButton("Liste Anzeigen");
		JButton deleteButton = new JButton("Löschen");
		
		buttonPanel.add(saveButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(toOutButton);

		ablesungPanel.add(toAllAblesungButton);
		ablesungPanel.add(toNewAblesungButton);
		// buttonPanel.add(exportButton);
		saveButton.addActionListener(e -> {
			save();
		});

		toOutButton.addActionListener(e -> {
			baseFrame.openPage(Ablesebogen.ZAEHLERART_OUT);
		});
		deleteButton.addActionListener(e -> {
			if (baseFrame.getZaehlerartenListe().remove(toEdit)) {
				baseFrame.getListe().deleteZaehlerart(toEdit);
				clear();
			}
		});

		toAllAblesungButton.addActionListener(e -> {
			baseFrame.openPage(Ablesebogen.ABLESUNG_OUT, toEdit.getName());
		});
		toNewAblesungButton.addActionListener(e -> {
			baseFrame.openPage(Ablesebogen.ABLESUNG_IN, toEdit);
		});
	}

	public void clear() {
		baseFrame.setTitle("Neue Zählerart");
		this.nameField.setText("");
		this.warnField.setText("");
		toEdit = null;
		toAllAblesungButton.setEnabled(false);
		toNewAblesungButton.setEnabled(false);
	}

	public boolean save() {
		String name = nameField.getText();
		int warnValue=0;
		
		if (name.isBlank()) {
			Util.errorMessage("Name darf nicht leer sein!");
			nameField.requestFocus();
			return false;
		}

		if (warnField.getText().length()>0) {
			try {
				warnValue = Integer.parseInt(warnField.getText());
				if (warnValue < 0) {
					Util.errorMessage("Warnwert darf nicht Negativ sein");
					warnField.requestFocus();
					return false;
				}
			} catch (NumberFormatException ec2) {
				Util.errorMessage("Warnwert nicht Nummerisch");
				warnField.requestFocus();
				return false;
			}
		}
		Zaehlerart toSave = new Zaehlerart(name, warnValue);

		boolean success = false;
		if (toEdit == null) {
			// Neuer Datensatz
			toSave.setId(0);
			success = baseFrame.getZaehlerartenListe().add(toSave);
		} else {
			// Editieren
			toSave.setId(toEdit.getId());
			success = baseFrame.getZaehlerartenListe().update(toEdit, toSave);

		}

		if (success) {
			clear();
		}
		return success;

	}

	public boolean activate(Object eOpts) {
		if (eOpts instanceof Zaehlerart) {
			Zaehlerart k = (Zaehlerart) eOpts;
			baseFrame.setTitle(k.getId() + " editieren");
			this.nameField.setText(k.getName());
			this.warnField.setText(""+k.getWarnValue());
			toEdit = k;
			toAllAblesungButton.setEnabled(true);
			toNewAblesungButton.setEnabled(true);
		} else {
			clear();
		}
		return true;
	}

	@Override
	public void afterActivate(Object eOpts) {
		this.nameField.requestFocus();
	}

	@Override
	public boolean showFilter() {
		return false;
	}

	@Override
	public void filter(String filter) {
		//NOOP
	}
}
