package client.kunden;

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
import server.Kunde;

@SuppressWarnings("serial")
public class KundenInPanel extends JAblesebogenPanel {

	private Kunde toEdit;
	public JTextField nameField;
	private JTextField vornameField;

	private JButton toAllAblesungButton;
	private JButton toNewAblesungButton;

	public KundenInPanel(Ablesebogen bFrame) {
		super(new BorderLayout());

		this.baseFrame = bFrame;
		// Grid
		JPanel grid = new JPanel(new GridLayout(6, 2));
		this.add(grid, BorderLayout.CENTER);
		grid.setBorder(new EmptyBorder(0, 10, 0, 10));
		
		
		nameField = new JTextField();
		vornameField = new JTextField();

		grid.add(new JLabel("Name"));
		grid.add(nameField);
		
		grid.add(new JLabel("Vorname"));
		grid.add(vornameField);

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
		tabOrder.add(vornameField);
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
			baseFrame.openPage(Ablesebogen.KUNDE_OUT);
		});
		deleteButton.addActionListener(e -> {
			if (baseFrame.getKundenListe().remove(toEdit)) {
				baseFrame.getListe().deleteKunde(toEdit);
				clear();
			}
		});

		toAllAblesungButton.addActionListener(e -> {
			baseFrame.openPage(Ablesebogen.ABLESUNG_OUT, toEdit.getId());
		});
		toNewAblesungButton.addActionListener(e -> {
			baseFrame.openPage(Ablesebogen.ABLESUNG_IN, toEdit);
		});
	}

	public void clear() {
		baseFrame.setTitle("Neuer Kunde");
		this.nameField.setText("");
		this.vornameField.setText("");
		toEdit = null;
		toAllAblesungButton.setEnabled(false);
		toNewAblesungButton.setEnabled(false);
	}

	public boolean save() {
		String name = nameField.getText();
		String vorname = vornameField.getText();

		if (name.isBlank()) {
			Util.errorMessage("Name darf nicht leer sein!");
			nameField.requestFocus();
			return false;
		}

		if (vorname.isBlank()) {
			Util.errorMessage("Vorname darf nicht leer sein!");
			vornameField.requestFocus();
			return false;
		}

		Kunde toSave = new Kunde(name, vorname);

		boolean success = false;
		if (toEdit == null) {
			// Neuer Datensatz
			toSave.setId(null);
			success = baseFrame.getKundenListe().add(toSave);
		} else {
			// Editieren
			toSave.setId(toEdit.getId());
			success = baseFrame.getKundenListe().update(toEdit, toSave);

		}

		if (success) {
			clear();
		}
		return success;

	}

	public boolean activate(Object eOpts) {
		if (eOpts instanceof Kunde) {
			Kunde k = (Kunde) eOpts;
			baseFrame.setTitle(k.getId() + " editieren");
			this.nameField.setText(k.getName());
			this.vornameField.setText(k.getVorname());
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
