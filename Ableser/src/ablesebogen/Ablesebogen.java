package ablesebogen;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import javax.swing.JTextField;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class Ablesebogen extends JFrame{


	private AbleseList liste;
	private AbleseEntry curEntry;
	
	private JPanel inLayout;
	private JPanel outLayout;

	private JPanel panel;
	private JPanel buttonPanel;
	
	private JTextField kundenNummer;
	private JTextField zaelernummer;
	private JTextField zaelerstand;
	private JTextField kommentar;
	
	private JButton saveButton;
	private JButton exportButton;
	private JButton deleteButton;
	
	private AbleseTableModel tableModel;
	private JTable outList;
	
	private UtilDateModel model;

	//private JComboBox neuEingebaut;
	private JComboBox<String> zaelerArt;
	
	private JCheckBox neuEingebaut;
	
	private JDatePickerImpl datePicker;
	
	//private String DEFAULT_EINGEBAUT[] = {"Ja", "Nein"};

	HashMap<String, Integer> DEFAULT_WERTE = new HashMap<String, Integer>();

	private String DEFAULT_ZAELERART[] = {"Gas", "Strom", "Heizung", "Wasser"};

	public Ablesebogen() {
		super("Ablesebogen");
		this.setSize(500, 250);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				exit();
			}
		});
		
		liste=AbleseList.importJson();
		DEFAULT_WERTE.put("Gas", 100000);
		DEFAULT_WERTE.put("Strom", 200000);
		DEFAULT_WERTE.put("Wasser", 300000);
		DEFAULT_WERTE.put("Heizung", 400000);
		curEntry=null;
		
		//Root Container
		final Container con = getContentPane();
		con.setLayout(new CardLayout());
	
		//in Layout Base Layout
		inLayout= new JPanel(new BorderLayout());
		con.add(inLayout,"in");

		//in Layout Komponenten
		panel = new JPanel(new GridLayout(7,2));
		inLayout.add(panel, BorderLayout.CENTER);
		buttonPanel = new JPanel();
		inLayout.add(buttonPanel, BorderLayout.SOUTH);
		
		model = new UtilDateModel();
		model.setSelected(true); //init DatePicker Value
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		
		kundenNummer=new JTextField();
		zaelerArt=new JComboBox<String>(DEFAULT_ZAELERART);
		zaelernummer=new JTextField();
		datePicker = new JDatePickerImpl(datePanel);
		//neuEingebaut=new JComboBox(DEFAULT_EINGEBAUT);
		neuEingebaut=new JCheckBox();
		zaelerstand=new JTextField();
		kommentar=new JTextField();
				
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
		
		//untere Leiste
		saveButton=new JButton("Speichern");
		exportButton=new JButton("Exportieren");
		JButton toOutButton=new JButton("Liste Anzeigen");
		deleteButton=new JButton("Löschen");
		
		buttonPanel.add(saveButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(exportButton);
		buttonPanel.add(toOutButton);
		
		saveButton.addActionListener(e -> {	
			save();
		});
		
		exportButton.addActionListener(e -> {	
			export();
		});
		toOutButton.addActionListener(e -> {
			if(liste.size() < 1 ) {  create_Popup("Liste konnte nicht Angezeigt werden"); return;}
			((CardLayout) con.getLayout()).show(con,"out");
		});
		deleteButton.addActionListener(e -> {
			liste.remove(curEntry);
			tableModel.fireTableDataChanged();
			clear();
		});
				
		//out Layout Base Layout

		outLayout=new JPanel(new BorderLayout());
		con.add(outLayout,"out");
		
		//out Layout Komponenten
		JButton toInButton=new JButton("neuer Datensatz");
		outLayout.add(toInButton,BorderLayout.SOUTH);
		
		toInButton.addActionListener(e -> {
			((CardLayout) con.getLayout()).show(con,"in");
		});
		
		tableModel = new AbleseTableModel(liste);
		outList=new JTable(tableModel);
		outList.setAutoCreateRowSorter(true);
	    JScrollPane scrollPane = new JScrollPane(outList);
	    scrollPane.setPreferredSize(new Dimension(380,280));
		outLayout.add(scrollPane);

		outList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()!=2) {
					return; //nur Doppelklick führt zum editieren
				}
				loadWithValue(liste.get(outList.getSelectedRow()));
				((CardLayout) con.getLayout()).show(con,"in");				
			}
		});
		
		this.setVisible(true);
	} 
	
	public void save() {
		
		String kn=kundenNummer.getText();
		String zA=zaelerArt.getSelectedItem().toString();
		int zN=0;
		try {
			zN=Integer.parseInt(zaelernummer.getText());
		}catch (NumberFormatException ec) {
			create_Popup("Zaehlernummer nicht Nummerisch");
	        return;
		}
		Date selectedDate = (Date) datePicker.getModel().getValue();
		boolean neuE=neuEingebaut.isSelected();// neuEingebaut.getText();
		int zStand=0;
		try {
			zStand=Integer.parseInt(zaelerstand.getText());
		}catch (NumberFormatException ec2) {
			create_Popup("Zaehlerstand nicht Nummerisch");
	        return;
		}
		Plausicheck(zA, zStand);
		String kom=kommentar.getText();
		if (curEntry==null) {
			AbleseEntry entry=new AbleseEntry(kn,zA,zN,selectedDate,neuE,zStand,kom);
			liste.add(entry);
			tableModel.fireTableDataChanged();
		} else {
			curEntry.setKundenNummer(kn);
			curEntry.setZaelerArt(zA);
			curEntry.setZaelernummer(zN);
			curEntry.setDatum(selectedDate);
			curEntry.setNeuEingebaut(neuE);
			curEntry.setZaelerstand(zStand);
			curEntry.setKommentar(kom);
		}
		clear();
	}
	private static void create_PopupWithButton(String Alert_Massage){
        JPanel Alert_Panel = new JPanel();
		JFrame Alert_Frame = new JFrame("Alert Window");
        LayoutManager Alert_Layout = new FlowLayout();
        Alert_Panel.setLayout(Alert_Layout);
        JLabel Alert_Label = new JLabel(Alert_Massage);
		JButton Button_Ja = new JButton("Ja");
		JButton Button_Nein = new JButton("Nein");
        Alert_Panel.add(Alert_Label);
        Alert_Frame.getContentPane().add(Alert_Panel, BorderLayout.CENTER);
        Alert_Frame.setSize(400, 100);
        Alert_Frame.setLocationRelativeTo(null);
        Alert_Frame.setVisible(true);Alert_Panel.add(Button_Nein);
        Alert_Panel.add(Button_Ja);
        Alert_Frame.getContentPane().add(Alert_Panel, BorderLayout.EAST);
        Button_Nein.addActionListener(e -> {
        	Alert_Frame.dispose();
        	return;
        });
        Button_Ja.addActionListener(e -> {
        	Alert_Frame.dispose();
        });
}
		public void Plausicheck(String zA, int zStand) {
			if(zStand > DEFAULT_WERTE.get(zA)) {
				
				create_PopupWithButton("Werte ungewöhnlich trotzdem Speichern?");
			}
		 }
	
	private static void create_Popup(String Alert_Massage){
        JPanel Alert_Panel = new JPanel();
		JFrame Alert_Frame = new JFrame("Alert Window");
        LayoutManager Alert_Layout = new FlowLayout();
        Alert_Panel.setLayout(Alert_Layout);
        JLabel Alert_Label = new JLabel(Alert_Massage);

        Alert_Panel.add(Alert_Label);
        Alert_Frame.getContentPane().add(Alert_Panel, BorderLayout.CENTER);
        Alert_Frame.setSize(400, 100);
        Alert_Frame.setLocationRelativeTo(null);
        Alert_Frame.setVisible(true);
	}

	public void export() {
		liste.exportJson();
	}
	
	public void clear() {
		Date zDate = new Date();
		kundenNummer.setText("");
		zaelerArt.setSelectedIndex(0);
		zaelernummer.setText("");
		model.setValue(zDate); 
		neuEingebaut.setSelected(false);
		zaelerstand.setText("");;
		kommentar.setText("");
		
		curEntry=null;
	}
	
	public void loadWithValue(AbleseEntry entry) {
		kundenNummer.setText(entry.getKundenNummer());
		//zaelerArt.setSelectedItem(entry.getZaelerArt());
		zaelernummer.setText(Integer.toString(entry.getZaelernummer()));
		model.setValue(entry.getDatum()); 
		neuEingebaut.setSelected(entry.getNeuEingebaut());
		zaelerstand.setText(Integer.toString(entry.getZaelerstand()));
		kommentar.setText(entry.getKommentar());
		
		curEntry=entry;
	}
	
	public void exit() {
		export();
		System.exit(0);
	}
		
	
	public static void main(String[] args) {
		new Ablesebogen();
	}
}
