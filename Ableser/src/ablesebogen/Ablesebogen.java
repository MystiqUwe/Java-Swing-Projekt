package ablesebogen;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class Ablesebogen extends JFrame{


	private AbleseList liste;
	private AbleseList newList;
	private AbleseEntry curEntry;
	
	private JPanel inLayout;
	private AbleseOutPanel outLayout;
	private AbleseOutPanel filterOutLayout;

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
	
	private JDatePanelImpl datePanel;

	private UtilDateModel model;

	//private JComboBox neuEingebaut;
	private JComboBox<String> zaelerArt;
	
	private JCheckBox neuEingebaut;
	
	private JDatePickerImpl datePicker;
	
	//private String DEFAULT_EINGEBAUT[] = {"Ja", "Nein"};

	HashMap<String, Integer> DEFAULT_WERTE = new HashMap<String, Integer>();

	private String DEFAULT_ZAELERART[] = {"Gas", "Strom", "Heizung", "Wasser"};

	public Ablesebogen() {
		super("neuer Datensatz");
		this.setSize(600, 250);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				exit();
			}
		});
		
		liste=AbleseList.importJson();
		newList=new AbleseList();
		DEFAULT_WERTE.put("Gas", 100000);
		DEFAULT_WERTE.put("Strom", 200000);
		DEFAULT_WERTE.put("Wasser", 300000);
		DEFAULT_WERTE.put("Heizung", 400000);
		curEntry=null;
		
		//Root Container
		final Container con = getContentPane();
		con.setLayout(new CardLayout());
		
		drawMenu();
	
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
		datePanel = new JDatePanelImpl(model);

		kundenNummer=new JTextField();
		zaelerArt=new JComboBox<String>(DEFAULT_ZAELERART);
		zaelernummer=new JTextField();
		datePicker = new JDatePickerImpl(datePanel);
		//neuEingebaut=new JComboBox(DEFAULT_EINGEBAUT);
		neuEingebaut=new JCheckBox();
		zaelerstand=new JTextField();
		kommentar=new JTextField();
		
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
		
		//untere Leiste
		saveButton=new JButton("Speichern");
		exportButton=new JButton("Exportieren");
		JButton toOutButton=new JButton("Liste Anzeigen");
		deleteButton=new JButton("Löschen");
		JButton toFilterOutButton=new JButton("Für diesen Kunden");

		
		buttonPanel.add(saveButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(toOutButton);
		buttonPanel.add(toFilterOutButton);
		//buttonPanel.add(exportButton);
		saveButton.addActionListener(e -> {	
			save();
		});
		
		exportButton.addActionListener(e -> {	
			export();
		});
		toOutButton.addActionListener(e -> {
			if(liste.size() < 1 ) {  create_Popup("Liste konnte nicht Angezeigt werden"); return;}

			outLayout.updateTable();
			this.setTitle("Übersichtsliste");
			((CardLayout) con.getLayout()).show(con,"out");
		});
		deleteButton.addActionListener(e -> {
			liste.remove(curEntry);
			newList.remove(curEntry);
			clear();
		});
		toFilterOutButton.addActionListener(e -> {
			if(newList.size() < 1 ) {  create_Popup("Liste konnte nicht Angezeigt werden"); return;}

			filterOutLayout.updateTable(kundenNummer.getText());
			this.setTitle("Daten für "+kundenNummer.getText());
			((CardLayout) con.getLayout()).show(con,"filter");
			
		});
				
		outLayout=new AbleseOutPanel(this, liste);
		con.add(outLayout,"out");
		
		filterOutLayout= new AbleseOutPanel(this, newList);
		con.add(filterOutLayout,"filter");

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
		if(!Plausicheck(zA, zStand)) return;
		String kom=kommentar.getText();
		if (curEntry==null) {
			AbleseEntry entry=new AbleseEntry(kn,zA,zN,selectedDate,neuE,zStand,kom);
			liste.add(entry);
			newList.add(entry);
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
	}

		public boolean Plausicheck(String zA, int zStand) {
			boolean result = true;
			if(zStand > DEFAULT_WERTE.get(zA)) {
				
				JFrame zFrame = new JFrame();
				JPanel zPanel = new JPanel();
				ConfirmDialog dialog = new ConfirmDialog(zFrame);
				result = dialog.showConfirmDialog(zPanel, "Werte ungewöhnlich trotzdem Speichern? ");	        	
			}
			return result;
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
		this.setTitle("neuer Datensatz");

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
		this.setTitle(entry.getKundenNummer()+" bearbeiten");

		kundenNummer.setText(entry.getKundenNummer());
		//zaelerArt.setSelectedItem(entry.getZaelerArt());
		zaelernummer.setText(Integer.toString(entry.getZaelernummer()));
		model.setValue(entry.getDatum()); 
		neuEingebaut.setSelected(entry.getNeuEingebaut());
		zaelerstand.setText(Integer.toString(entry.getZaelerstand()));
		kommentar.setText(entry.getKommentar());
		
		curEntry=entry;
	}
	
	public void drawMenu() {
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
		         // liste.exportXML();
		        }
		      });
		 subMenuCSV.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ev) {
		          //liste.exportCSV();
		        }
		      });
		 menu.add(subMenuJSON);
		 menu.add(subMenuXML);
		 menu.add(subMenuCSV);
        
		
        mb.add(menu);
        this.setJMenuBar(mb);
	}
	
	public void exit() {
		export();
		System.exit(0);
	}
		
	
	public static void main(String[] args) {
		new Ablesebogen();
	}
}
