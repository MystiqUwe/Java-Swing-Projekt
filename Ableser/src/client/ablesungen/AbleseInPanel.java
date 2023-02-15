package client.ablesungen;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import server.Kunde;

@SuppressWarnings("serial")
public class AbleseInPanel extends JAblesebogenPanel {

	public Ablesebogen baseFrame;
	private AbleseEntry curEntry; // Der aktuell zu editierender Datensatz, null falls nicht vorhanden

	// Eingabefelder
	private JComboBox<Kunde> kundenNummer;
	private JTextField zaelernummer;
	private JTextField zaelerstand;
	private JTextField kommentar;
	private JComboBox<String> zaelerArt;
	private JCheckBox neuEingebaut;
	private JDatePickerImpl datePicker;
	private JDatePanelImpl datePanel;
	private UtilDateModel model;

	private JPanel panel;
	private JPanel buttonPanel;

	private JButton toFilterOutButton;
	
	// Für den Plausibilitätscheck
	HashMap<String, Integer> DEFAULT_WERTE = new HashMap<String, Integer>();
	private final String[] DEFAULT_ZAELERART = { "Gas", "Strom", "Heizung", "Wasser" };

	public AbleseInPanel(Ablesebogen bFrame) {
		super(new BorderLayout());
		baseFrame = bFrame;
		DEFAULT_WERTE.put("Gas", 100000);
		DEFAULT_WERTE.put("Strom", 200000);
		DEFAULT_WERTE.put("Wasser", 300000);
		DEFAULT_WERTE.put("Heizung", 400000);
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
		zaelerArt = new JComboBox<String>(DEFAULT_ZAELERART);
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
		panel.add(new JLabel("neu eingebaut"));
		panel.add(neuEingebaut);
		panel.add(new JLabel("Zählerstand"));
		panel.add(zaelerstand);
		panel.add(new JLabel("Kommentar"));
		panel.add(kommentar);

		panel.setBorder(new EmptyBorder(0, 10, 0, 10));
		// untere Leiste
		JButton saveButton = new JButton("Speichern");
		JButton toOutButton = new JButton("Liste Anzeigen");
		JButton deleteButton = new JButton("Löschen");
		toFilterOutButton = new JButton("Für diesen Kunden");

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
			
			DefaultComboBoxModel<Kunde> model = new DefaultComboBoxModel<Kunde>(e.toArray(new Kunde[0]));
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
		} else {
			try {
				UUID kid=UUID.fromString(baseFrame.getFilter());
				kundenNummer.setSelectedItem(baseFrame.getKundenListe().getById(kid));
			} catch (Exception e) {
				kundenNummer.setSelectedItem(null);
			}
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
		
		String zA = zaelerArt.getSelectedItem().toString();
		
		String zN = zaelernummer.getText();
		if (zN.length()<1) {
			Util.errorMessage("Zählernummer darn nicht leer sein");
			zaelernummer.requestFocus();
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

		zA=null;
		AbleseEntry newEntry = new AbleseEntry(null, kn, zA, zN, selectedDate, neuE, zStand, kom);
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
	public boolean Plausicheck(String zA, int zStand, UUID kn, String zN) {
		try {
			AbleseEntry lastAB = baseFrame.getListe().getLast(kn, zN);
			System.out.println(lastAB.getZaelerstand());
			if(lastAB.getZaelerstand() > zStand) {
				return Util.optionMessage("Zählerstand kleiner als zuvor, trotzdem Speichern?");
			}
			if (zStand > (DEFAULT_WERTE.get(zA)+lastAB.getZaelerstand())) {
				return Util.optionMessage("Werte ungewöhnlich trotzdem Speichern?");
			}
			
		} catch(Exception ex) {
			return true;
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
		// zaelerArt.setSelectedItem(entry.getZaelerArt());
		zaelernummer.setText(entry.getZaelernummer());
		model.setValue(Date.from(entry.getDatum().atStartOfDay().toInstant(ZoneOffset.UTC)));
		neuEingebaut.setSelected(entry.getNeuEingebaut());
		zaelerstand.setText(Integer.toString(entry.getZaelerstand()));
		kommentar.setText(entry.getKommentar());

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
		;
		kommentar.setText("");

		curEntry = null;
		kundenNummer.requestFocus();
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
