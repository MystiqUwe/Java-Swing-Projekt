package ablesebogen;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import server.Kunde;

@SuppressWarnings("serial")
public class KundenInPanel extends JAblesebogenPanel {

	private Kunde toEdit;
	private JTextField nameField;
	private JTextField vornameField;

	public KundenInPanel(Ablesebogen bFrame) {
		super(new BorderLayout());

		this.baseFrame = bFrame;
		// Grid
		JPanel grid = new JPanel(new GridLayout(7, 2));
		this.add(grid, BorderLayout.CENTER);
		nameField = new JTextField();
		vornameField = new JTextField();

		grid.add(new JLabel("Name"));
		grid.add(nameField);
		grid.add(new JLabel("Vorname"));
		grid.add(vornameField);

		ArrayList<JComponent> tabOrder = new ArrayList<JComponent>();
		tabOrder.add(nameField);
		tabOrder.add(vornameField);
		Util.handleTabOrder(tabOrder, e -> {
			return save();
		});

		// untere Leiste
		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
		this.add(buttonPanel, BorderLayout.SOUTH);

		JButton saveButton = new JButton("Speichern");
		JButton toOutButton = new JButton("Liste Anzeigen");
		JButton deleteButton = new JButton("Löschen");

		buttonPanel.add(saveButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(toOutButton);
		// buttonPanel.add(exportButton);
		saveButton.addActionListener(e -> {
			save();
		});

		toOutButton.addActionListener(e -> {
			baseFrame.openPage(Ablesebogen.KUNDE_OUT);
		});
		deleteButton.addActionListener(e -> {
			if (baseFrame.getKundenListe().remove(toEdit)) {
				clear();
			}
		});

	}

	public void clear() {
		baseFrame.setTitle("Neuer Kunde");
		this.nameField.setText("");
		this.vornameField.setText("");
		toEdit = null;
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
		nameField.requestFocus();

		if (eOpts instanceof Kunde) {
			Kunde k = (Kunde) eOpts;
			baseFrame.setTitle(k.getId() + " editieren");
			this.nameField.setText(k.getName());
			this.vornameField.setText(k.getVorname());
			toEdit = k;
		} else {
			clear();
		}
		return true;
	}

}
