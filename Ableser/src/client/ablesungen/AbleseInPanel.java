package client.ablesungen;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import client.Ablesebogen;
import client.JAblesebogenPanel;
import client.Util;
import dataEntities.AbleseEntry;
import dataEntities.Kunde;
import dataEntities.Zaehlerart;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

@SuppressWarnings("serial")
public class AbleseInPanel extends JAblesebogenPanel {

	public Ablesebogen baseFrame;
	private AbleseEntry curEntry; // Der aktuell zu editierender Datensatz, null falls nicht vorhanden

	// Eingabefelder
	private JComboBox<Kunde> kundenNummer;
	private JTextField zaelernummer;
	private JTextField zaelerstand;
	private JTextField kommentar;
	private JComboBox<Zaehlerart> zaelerArt;
	private JCheckBox neuEingebaut;
	private JDatePickerImpl datePicker;
	private JDatePanelImpl datePanel;
	private UtilDateModel model;

	private JPanel panel;
	private JPanel buttonPanel;

	private JButton toFilterOutButton;
	JButton saveButton;
	JButton toOutButton;
	JButton deleteButton;
	
	public AbleseInPanel(Ablesebogen bFrame) {
		super(new BorderLayout());
		baseFrame = bFrame;
		curEntry = null;

		// in Layout Komponenten
		panel = new JPanel(new GridLayout(7, 2));
		this.add(panel, BorderLayout.CENTER);
		buttonPanel = new JPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);

		model = new UtilDateModel();
		model.setSelected(true); // init DatePicker Value
		datePanel = new JDatePanelImpl(model);

		kundenNummer = new JComboBox<>(bFrame.getKundenListe().getArray()); // Holt die Auswahl für die ComboBox
		zaelerArt = new JComboBox<>(bFrame.getZaehlerartenListe().getArray());
		zaelernummer = new JTextField();
		datePicker = new JDatePickerImpl(datePanel);
		datePicker.setTextEditable(true);
		neuEingebaut = new JCheckBox();
		zaelerstand = new JTextField();
		kommentar = new JTextField();

		panel.add(new JLabel("Kundennummer"));
		panel.add(kundenNummer);
		panel.add(new JLabel("Zählerart"));
		panel.add(zaelerArt);
		panel.add(new JLabel("Zählernummer"));
		panel.add(zaelernummer);
		panel.add(new JLabel("Datum"));
		panel.add(datePicker);
		panel.add(new JLabel("Neu eingebaut"));
		panel.add(neuEingebaut);
		panel.add(new JLabel("Zählerstand"));
		panel.add(zaelerstand);
		panel.add(new JLabel("Kommentar"));
		panel.add(kommentar);

		panel.setBorder(new EmptyBorder(0, 10, 0, 10));
		// untere Leiste
		saveButton = new JButton("Speichern");
		toOutButton = new JButton("Liste Anzeigen");
		deleteButton = new JButton("Löschen");
		toFilterOutButton = new JButton("Ablesungen dieses Kundens");

		buttonPanel.add(saveButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(toOutButton);
		buttonPanel.add(toFilterOutButton);
		// buttonPanel.add(exportButton);
		saveButton.addActionListener(e -> {
			save();
		});

		toOutButton.addActionListener(e -> {
			baseFrame.openPage(Ablesebogen.ABLESUNG_OUT);
		});
		deleteButton.addActionListener(e -> {
			if (baseFrame.getListe().remove(curEntry)) {
				clear();
			}
		});
		toFilterOutButton.addActionListener(e -> {
			Kunde selectedItem = (Kunde) kundenNummer.getSelectedItem();
			if (selectedItem==null) {
				return;
			}
			baseFrame.openPage(Ablesebogen.ABLESUNG_OUT, selectedItem.getId());
		});

		// Rendert die List Items in einer ComboBox
		kundenNummer.setRenderer(new ListCellRenderer<Kunde>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Kunde> list, Kunde value, int index,
					boolean isSelected, boolean cellHasFocus) {
				if (value == null) {
					return new JLabel("");
				}
				String nameundvorname = (value.getName() + ", " + value.getVorname() + " -> "
						+ value.getId().toString());
				JLabel label = new JLabel(nameundvorname);
				if (isSelected) {
					label.setIcon(new ImageIcon(getClass().getResource("check.png")));
				}
				return label;
			}
		});

		// Kunden Änderungen mitbekommen
		baseFrame.getKundenListe().addChangeListener(e -> {
			Object k=kundenNummer.getSelectedItem();

			DefaultComboBoxModel<Kunde> model = new DefaultComboBoxModel<>(e.toArray(new Kunde[0]));
			kundenNummer.setModel(model);

			if ((k!=null) && (e.indexOf(k)>=0)) {
				kundenNummer.setSelectedItem(k);
			} else {
				kundenNummer.setSelectedItem(null);
			}
			return true;
		});

		kundenNummer.addActionListener(e -> {
			toFilterOutButton.setEnabled(kundenNummer.getSelectedItem()!=null);
		});

		zaelerArt.setRenderer(new ListCellRenderer<Zaehlerart>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Zaehlerart> list, Zaehlerart value, int index,
					boolean isSelected, boolean cellHasFocus) {
				if (value == null) {
					return new JLabel("");
				}

				JLabel label = new JLabel(value.getName());
				if (isSelected) {
					label.setIcon(new ImageIcon(getClass().getResource("check.png")));
				}
				return label;
			}
		});

		// Kunden Änderungen mitbekommen
		baseFrame.getZaehlerartenListe().addChangeListener(e -> {
			Object z = zaelerArt.getSelectedItem();

			DefaultComboBoxModel<Zaehlerart> model = new DefaultComboBoxModel<>(e.toArray(new Zaehlerart[0]));
			zaelerArt.setModel(model);

			if ((z!=null) && (e.indexOf(z)>=0)) {
				zaelerArt.setSelectedItem(z);
			} else {
				zaelerArt.setSelectedItem(null);
			}
			return true;
		});


		// Enter zur Navigation
		ArrayList<JComponent> tabOrder = new ArrayList<>();
		tabOrder.add(kundenNummer);
		tabOrder.add(zaelerArt);
		tabOrder.add(zaelernummer);
		tabOrder.add(datePicker.getJFormattedTextField());
		tabOrder.add(neuEingebaut);
		tabOrder.add(zaelerstand);
		tabOrder.add(kommentar);
		Util.handleTabOrder(tabOrder, e -> {
			return save();
		});

	}

	@Override
	public boolean activate(Object eOpts) {
		//kundenNummer.requestFocusInWindow();
		if (eOpts instanceof AbleseEntry) {
			loadWithValue((AbleseEntry) eOpts);
		} else if (eOpts instanceof Kunde) {
			kundenNummer.setSelectedItem( eOpts);
			zaelerArt.setSelectedItem(null);
		} else if (eOpts instanceof Zaehlerart) {
			kundenNummer.setSelectedItem(null);
			zaelerArt.setSelectedItem(eOpts);
		} else {
			try {
				UUID kid=UUID.fromString(baseFrame.getFilter());
				kundenNummer.setSelectedItem(baseFrame.getKundenListe().getById(kid));
			} catch (Exception e) {
				kundenNummer.setSelectedItem(null);
			}
			zaelerArt.setSelectedItem(null);
			clear();
		}
		return true;
	}

	/**
	 * Speichert den Datensatz aus dem in Layout, entweder als neuer Datensatz, oder
	 * als Update falls vorhanden
	 *
	 * @return boolean
	 */
	public boolean save() {
		Kunde selectedItem = (Kunde) kundenNummer.getSelectedItem();
		if (selectedItem == null) {
			Util.errorMessage("Es muss ein Kunde ausgewählt sein");
			kundenNummer.requestFocus();
			return false;
		}
		UUID kn = selectedItem.getId(); // kundenNummer.getSelectedItem().toString();

		Zaehlerart zA = (Zaehlerart) zaelerArt.getSelectedItem();
		if (zA==null) {
			Util.errorMessage("Zählerart darf nicht leer sein");
			return false;
		}


		String zN = zaelernummer.getText();
		if (zN.length()<1) {
			Util.errorMessage("Zählernummer darf nicht leer sein");
			zaelernummer.requestFocus();
			return false;
		}

		LocalDate selectedDate = Util.dateToLocalDate((Date) datePicker.getModel().getValue());


		boolean neuE = neuEingebaut.isSelected();// neuEingebaut.getText();

		int zStand = 0;
		try {
			zStand = Integer.parseInt(zaelerstand.getText());
			if (zStand < 0) {
				Util.errorMessage("Zählerstand darf nicht Negativ sein");
				zaelerstand.requestFocus();
				return false;
			}
		} catch (NumberFormatException ec2) {
			Util.errorMessage("Zählerstand nicht Nummerisch");
			zaelerstand.requestFocus();
			return false;
		}

		String kom = kommentar.getText();

		// #009 Plausibilitätsprüfung
		if (!Plausicheck(zA, zStand, kn, zN)) {
			return false;
		}

		AbleseEntry newEntry = new AbleseEntry(null, kn, zA.getId(), zN, selectedDate, neuE, zStand, kom);
		boolean success;
		if (curEntry == null) {
			success = baseFrame.getListe().add(newEntry);
		} else {
			newEntry.setId(curEntry.getId());
			success = baseFrame.getListe().update(curEntry, newEntry);
		}
		if (success) {
			clear();
		}
		return success;
	}

	/**
	 * Plausibilitatsprüfung für #009, simpler check ob der eingegebene Wert größer
	 * als ein vordefinierter Wert ist
	 *
	 * @param zA
	 * @param zStand
	 * @param zN
	 * @return int
	 */
	public boolean Plausicheck(Zaehlerart zA, int zStand, UUID kn, String zN) {
		AbleseEntry lastAB = baseFrame.getListe().getLast(kn, zN);
		
		int oldStand=0;
		
		if (lastAB!=null) {
			oldStand=lastAB.getZaelerstand();
			System.out.println(lastAB.getZaelerstand());
		}
		if(oldStand > zStand) {
			return Util.optionMessage("Zählerstand kleiner als zuvor, trotzdem Speichern?");
		}
		if (zStand > (zA.getWarnValue()+oldStand)) {
			return Util.optionMessage("Werte ungewöhnlich trotzdem Speichern?");
		}

		return true;
	}


	/**
	 * Öffnet einen Datensatz zum editieren
	 *
	 * @param entry
	 */
	public void loadWithValue(AbleseEntry entry) {
		baseFrame.setTitle(entry.getId() + " bearbeiten");
		System.out.println("Edit " + entry.getKundenNummer());

		Kunde kunde = baseFrame.getKundenListe().getById(entry.getKundenNummer());

		kundenNummer.setSelectedItem(kunde);
		zaelerArt.setSelectedItem(baseFrame.getZaehlerartenListe().getById(entry.getZId()));
		// zaelerArt.setSelectedItem(entry.getZaelerArt());
		zaelernummer.setText(entry.getZaelernummer());
		model.setValue(Date.from(entry.getDatum().atStartOfDay().toInstant(ZoneOffset.UTC)));
		neuEingebaut.setSelected(entry.isNeuEingebaut());
		zaelerstand.setText(Integer.toString(entry.getZaelerstand()));
		kommentar.setText(entry.getKommentar());
		deleteButton.setEnabled(true);
		curEntry = entry;
	}

	// Löscht die Daten aus den Eingabefeldern, nach dem speichern als Vorbereitung
	// auf den nächsten Datensatz
	public void clear() {
		baseFrame.setTitle("neuer Datensatz");

		Date zDate = new Date();
		// kundenNummer.setText("");
		// zaelerArt.setSelectedIndex(0);
		zaelernummer.setText("");
		model.setValue(zDate);
		neuEingebaut.setSelected(false);
		zaelerstand.setText("");

		kommentar.setText("");

		curEntry = null;
		kundenNummer.requestFocus();
		deleteButton.setEnabled(false);

	}

	@Override
	public void afterActivate(Object eOpts) {
		this.kundenNummer.requestFocus();

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
