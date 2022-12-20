package ablesebogen;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import org.glassfish.jaxb.core.v2.model.core.TypeRef;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import client.Service;
import lombok.Getter;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import server.Ablesung;
import server.Database;
import server.Kunde;
import server.Server;

public class Ablesebogen extends JFrame{

	//Datenspeicher
	private AbleseList liste; //Liste von allen Daten
	private AbleseList newList; //Neue Daten, in dieser Session hinzugefügt
	private AbleseEntry curEntry; //Der aktuell zu editierender Datensatz, null falls nicht vorhanden
	
	//UI Panels
	private JPanel inLayout;
	private AbleseOutPanel outLayout;
	private AbleseOutPanel filterOutLayout;

	private JPanel panel;
	private JPanel buttonPanel;
	
	//Eingabefelder
	private JComboBox<Kunde> kundenNummer;
	private JTextField zaelernummer;
	private JTextField zaelerstand;
	private JTextField kommentar;
	private JComboBox<String> zaelerArt;	
	private JCheckBox neuEingebaut;
	private JDatePickerImpl datePicker;
	private JDatePanelImpl datePanel;
	private UtilDateModel model;
	
	private JFrame dialogFrame = new JFrame();
	
	@Getter
	private static Service service;
	
	private static String baseURL = "http://localhost:8081/rest";

	
	//Für den Plausibilitätscheck
	HashMap<String, Integer> DEFAULT_WERTE = new HashMap<String, Integer>();
	private final String[] DEFAULT_ZAELERART = { "Gas", "Strom", "Heizung", "Wasser" };

	public Ablesebogen() {
		super("neuer Datensatz");
		// Für unser eigenes Icon
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("swarm.png")));
		this.setSize(650, 275);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				exit();
			}
		});

		// Startwerte
		liste = AbleseList.importJson();
		newList = new AbleseList();
		DEFAULT_WERTE.put("Gas", 100000);
		DEFAULT_WERTE.put("Strom", 200000);
		DEFAULT_WERTE.put("Wasser", 300000);
		DEFAULT_WERTE.put("Heizung", 400000);
		curEntry = null;

		// Root Container
		final Container con = getContentPane();
		con.setLayout(new CardLayout());

		drawMenu();
		Server.startServer(baseURL,true);
		service = new Service(baseURL);

		// in Layout Base Layout
		inLayout = new JPanel(new BorderLayout());
		con.add(inLayout, "in");

		// in Layout Komponenten
		panel = new JPanel(new GridLayout(7, 2));
		inLayout.add(panel, BorderLayout.CENTER);
		buttonPanel = new JPanel();
		inLayout.add(buttonPanel, BorderLayout.SOUTH);

		model = new UtilDateModel();
		model.setSelected(true); // init DatePicker Value
		datePanel = new JDatePanelImpl(model);


		kundenNummer =  new JComboBox<>(getKundenNrData()); // Holt die Auswahl für die ComboBox
		zaelerArt = new JComboBox<String>(DEFAULT_ZAELERART);
		zaelernummer = new JTextField();
		datePicker = new JDatePickerImpl(datePanel);
		datePicker.setTextEditable(true);

		// neuEingebaut=new JComboBox(DEFAULT_EINGEBAUT);
		neuEingebaut = new JCheckBox();
		zaelerstand = new JTextField();
		kommentar = new JTextField();

		JButton button = new JButton();
		button.setBounds(50, 5, 50, 25);
		button.setBackground(Color.black);
		JTextField textField = new JTextField();
		textField.setBounds(20, 60, 100, 35);
		textField.setBackground(Color.white);
		textField.add(button);

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

		// untere Leiste
		JButton saveButton = new JButton("Speichern");
		JButton toOutButton = new JButton("Liste Anzeigen");
		JButton deleteButton = new JButton("Löschen");
		JButton toFilterOutButton = new JButton("Für diesen Kunden");

		buttonPanel.add(saveButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(toOutButton);
		buttonPanel.add(toFilterOutButton);
		// buttonPanel.add(exportButton);
		saveButton.addActionListener(e -> {
			save();
		});

		toOutButton.addActionListener(e -> {
			if (liste.size() < 1) {
				fehlerMessage("Liste konnte nicht angezeigt werden");
				return;
			}

			outLayout.openTable();
			this.setTitle("Übersichtsliste");
		});
		deleteButton.addActionListener(e -> {
			liste.remove(curEntry);
			newList.remove(curEntry);
			clear();
		});
		toFilterOutButton.addActionListener(e -> {
			if (newList.size() < 1) {
				fehlerMessage("Liste konnte nicht Angezeigt werden");
				return;
			}
			
			Kunde selectedItem = (Kunde) kundenNummer.getSelectedItem();
			filterOutLayout.openTable(selectedItem.getVorname());
			this.setTitle("Daten für " + selectedItem.getVorname());
		});

		outLayout = new AbleseOutPanel(this, liste, "out");
		con.add(outLayout, "out");

		filterOutLayout = new AbleseOutPanel(this, newList, "filter");
		con.add(filterOutLayout, "filter");
		
		// Rendert die List Items in einer ComboBox
		kundenNummer.setRenderer(new ListCellRenderer<Kunde>() {
		    @Override
		    public Component getListCellRendererComponent(JList<? extends Kunde> list, Kunde value, int index, boolean isSelected, boolean cellHasFocus) {
		        JLabel label = new JLabel(value.getName());
		        if (isSelected) {
		            label.setIcon(new ImageIcon(getClass().getResource("swarm.png")));
		        }
		        return label;
		    }
		});

		// Enter zur Navigation
		ArrayList<JComponent> tabOrder=new ArrayList();
		tabOrder.add(kundenNummer);
		tabOrder.add(zaelerArt);
		tabOrder.add(zaelernummer);
		tabOrder.add(datePicker.getJFormattedTextField());
		tabOrder.add(neuEingebaut);
		tabOrder.add(zaelerstand);
		tabOrder.add(kommentar);
		Util.handleTabOrder(tabOrder, e-> {return save();});
		
		this.setVisible(true);
	}

	
	/** 
	 * Speichert den Datensatz aus dem in Layout, entweder als neuer Datensatz, oder
	 * als Update falls vorhanden
	 * 
	 * @return boolean
	 */
	public boolean save() {
		Kunde selectedItem = (Kunde) kundenNummer.getSelectedItem();
		String kn = selectedItem.getId().toString();   //kundenNummer.getSelectedItem().toString();
		if (kn.length() == 0) {
			fehlerMessage("Kundennummer zu lang");
			kundenNummer.requestFocus();
			return false;
		}

		String zA = zaelerArt.getSelectedItem().toString();

		int zN = 0;
		try {
			zN = Integer.parseInt(zaelernummer.getText());
			if (zN<0) {
				fehlerMessage("Zählernummer darf nicht negativ sein");
				return false;				
			}
		} catch (NumberFormatException ec) {
			fehlerMessage("Zählernummer ist nicht Nummerisch");
			zaelernummer.requestFocus();
			return false;
		}

		Date selectedDate = (Date) datePicker.getModel().getValue();

		boolean neuE = neuEingebaut.isSelected();// neuEingebaut.getText();

		int zStand = 0;
		try {
			zStand = Integer.parseInt(zaelerstand.getText());
			if (zStand<0) {
				fehlerMessage("Zählerstand darf nicht Negativ sein");
				zaelerstand.requestFocus();
				return false;				
			}
		} catch (NumberFormatException ec2) {
			fehlerMessage("Zählerstand nicht Nummerisch");
			zaelerstand.requestFocus();
			return false;
		}

		String kom = kommentar.getText();

		// #009 Plausibilitätsprüfung
		if (Plausicheck(zA, zStand) == 1) {
			return false;
		}

		if (curEntry==null) {
			AbleseEntry entry=new AbleseEntry(kn,zA,zN,selectedDate,neuE,zStand,kom);
			liste.add(entry);
			newList.add(entry);
			/*Kunde k = new Kunde("Peter", "Maier");
			Ablesung a = new Ablesung(String.valueOf(zN), convertToLocalDateViaInstant(selectedDate), k, kom, neuE, zStand);
			a.setKundenId(UUID.fromString(String.valueOf("23eef2aa-67b8-4a4a-9777-e7ed8ba7b5d3")));
			System.out.println(a);
			System.out.println(service.post("hausverwaltung/ablesungen", a));*/
			//service.get("");
		} else {
			curEntry.setKundenNummer(kn);
			curEntry.setZaelerArt(zA);
			curEntry.setZaelernummer(zN);
			curEntry.setDatum(selectedDate);
			curEntry.setNeuEingebaut(neuE);
			curEntry.setZaelerstand(zStand);
			curEntry.setKommentar(kom);
			if (newList.indexOf(curEntry)<0) {
				newList.add(curEntry);				
			}
		}
		clear();
		return true;
	}
	
	
	private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}

	
	/** 
	 * Plausibilitatsprüfung für #009,
	 * simpler check ob der eingegebene Wert größer als ein vordefinierter Wert ist
	 * 
	 * @param zA
	 * @param zStand
	 * @return int
	 */
	public int Plausicheck(String zA, int zStand) {
		int result = 0;
		if(zStand > DEFAULT_WERTE.get(zA)) {			   
			result = JOptionPane.showConfirmDialog(dialogFrame, "Werte ungewöhnlich trotzdem Speichern?", "",  JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		}
		return result;
	 }	
		
	//Löscht die Daten aus den Eingabefeldern, nach dem speichern als Vorbereitung auf den nächsten Datensatz
	public void clear() {
		this.setTitle("neuer Datensatz");

		Date zDate = new Date();
		//kundenNummer.setText("");
		//zaelerArt.setSelectedIndex(0);
		zaelernummer.setText("");
		model.setValue(zDate); 
		neuEingebaut.setSelected(false);
		zaelerstand.setText("");;
		kommentar.setText("");
		
		curEntry=null;
	}
	
	
	/** 
	 * Öffnet einen Datensatz zum editieren
	 * 
	 * @param entry
	 */
	public void loadWithValue(AbleseEntry entry) {
		this.setTitle(entry.getKundenNummer()+" bearbeiten");

		kundenNummer.setSelectedItem(entry.getKundenNummer());
		//zaelerArt.setSelectedItem(entry.getZaelerArt());
		zaelernummer.setText(Integer.toString(entry.getZaelernummer()));
		model.setValue(entry.getDatum()); 
		neuEingebaut.setSelected(entry.getNeuEingebaut());
		zaelerstand.setText(Integer.toString(entry.getZaelerstand()));
		kommentar.setText(entry.getKommentar());
		
		curEntry=entry;
	}
	
	//Hilfsfunktion für die Menüleiste
	private void drawMenu() {
		JMenuBar mb=new JMenuBar();  
		 JMenu  menu=new JMenu("Exportieren");  
    

		 JMenuItem subMenuJSON=new JMenuItem("JSON"); 
		 JMenuItem subMenuXML=new JMenuItem("XML");
		 JMenuItem subMenuCSV=new JMenuItem("CSV");
		 
		 subMenuJSON.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		          liste.exportJson();
		        }
		      });
		 subMenuXML.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		          liste.exportXML();
		        }
		      });
		 subMenuCSV.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		          liste.exportCSV();
		        }
		      });
		 menu.add(subMenuJSON);
		 menu.add(subMenuXML);
		 menu.add(subMenuCSV);
        
        mb.add(menu);
        this.setJMenuBar(mb);

	}
	public void fehlerMessage(String Message) {
		JOptionPane.showMessageDialog(dialogFrame, Message, "",
				JOptionPane.ERROR_MESSAGE);
	}

	private Kunde[] getKundenNrData() {
		Response response = service.get("hausverwaltung/kunden"); //Server Anfrage für Kunden Daten
		List<Kunde> objects = new ArrayList<>();
		System.out.println(response);
		if(response.getStatus() >= 200 & response.getStatus() < 400) {
			List<Kunde> serverObjects = response.readEntity(new GenericType<List<Kunde>>() {});
			objects.addAll(serverObjects);
			System.out.println(objects);
			return objects.toArray(new Kunde[0]); //Liste von Objekten zu Array
		}else {
			fehlerMessage(response.readEntity(String.class));
		}
		return null;
	}
	
	
	public void exit() {
		liste.exportJson();
		System.exit(0);
	}
	
   
		
	
	
	/** 
	 * @param args
	 */
	public static void main(String[] args) {
		new Ablesebogen();
	}
}
