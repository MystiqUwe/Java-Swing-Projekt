package ablesebogen;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class Ablesebogen extends JFrame{


	private AbleseList liste;

	private JPanel inLayout;
	private JPanel outLayout;

	private JPanel panel;
	private JPanel buttonPanel;
	
	private JTextField kundenNummer;
	private JTextField zaelernummer;
	private JTextField datum;
	private JTextField zaelerstand;
	private JTextField kommentar;
	
	private JButton saveButton;
	private JButton exportButton;
	
	AbleseOutList outList;

	//private JComboBox neuEingebaut;
	private JComboBox zaelerArt;
	
	private JCheckBox neuEingebaut;
	
	private JDatePickerImpl datePicker;
	
	//private String DEFAULT_EINGEBAUT[] = {"Ja", "Nein"};
	HashMap<String, Integer> DEFAULT_WERTE = new HashMap<String, Integer>();
	private String DEFAULT_ZAELERART[] = {"Gas", "Strom", "Heizug", "Wasser"};
	
	public Ablesebogen() {
		super("Ablesebogen");
		this.setSize(400, 250);
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
		
		UtilDateModel model = new UtilDateModel();
		model.setSelected(true); //init DatePicker Value
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		
		kundenNummer=new JTextField();
		zaelerArt=new JComboBox(DEFAULT_ZAELERART);
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
		
		buttonPanel.add(saveButton);
		buttonPanel.add(exportButton);
		buttonPanel.add(toOutButton);
		
		saveButton.addActionListener(e -> {	
			save();
		});
		
		exportButton.addActionListener(e -> {	
			export();
		});
		toOutButton.addActionListener(e -> {
			outList.showList(liste);
			((CardLayout) con.getLayout()).show(con,"out");
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
		
		outList=new AbleseOutList();
		outLayout.add(outList);
		
		
		this.setVisible(true);
	} 
	
	public void save() {
		
		String kn=kundenNummer.getText();
		String zA=zaelerArt.getSelectedItem().toString();
		int zN=0;
		try {
			zN=Integer.parseInt(zaelernummer.getText());
		}catch (NumberFormatException ec) {
			JFrame Alert_Frame = new JFrame("Alert Window");
			String Allert_Message = "Zaehlernummer nicht Nummerisch";
			Create_Popup(Alert_Frame, Allert_Message);
	        Alert_Frame.setSize(400, 100);
	        Alert_Frame.setLocationRelativeTo(null);
	        Alert_Frame.setVisible(true);
	        return;
		}
		Date selectedDate = (Date) datePicker.getModel().getValue();
		boolean neuE=neuEingebaut.isSelected();// neuEingebaut.getText();
		int zStand=0;
		try {
			zStand=Integer.parseInt(zaelerstand.getText());
		}catch (NumberFormatException ec2) {
			JFrame Alert_Frame = new JFrame("Alert Window");
			String Allert_Message = "Zaehlerstand nicht Nummerisch";
			Create_Popup(Alert_Frame, Allert_Message);
	        Alert_Frame.setSize(400, 100);
	        Alert_Frame.setLocationRelativeTo(null);
	        Alert_Frame.setVisible(true);
	        return;
		}
		Plausicheck(zA, zStand);
		String kom=kommentar.getText();
		AbleseEntry entry=new AbleseEntry(kn,zA,zN,selectedDate,neuE,zStand,kom);
		liste.add(entry);
		
		
		}
		private static void Create_Popup(final JFrame Alert_Frame, String Alert_Massage){
	        JPanel Alert_Panel = new JPanel();
	        LayoutManager Alert_Layout = new FlowLayout();
	        Alert_Panel.setLayout(Alert_Layout);
	        JLabel Alert_Label = new JLabel(Alert_Massage);

	        Alert_Panel.add(Alert_Label);
	        Alert_Frame.getContentPane().add(Alert_Panel, BorderLayout.CENTER);
	}
		public void Plausicheck(String zA, int zStand) {
			if(zStand > DEFAULT_WERTE.get(zA)) {
				JFrame Alert_Frame = new JFrame("Alert Window");
				JPanel Alert_Panel = new JPanel();
				String Allert_Message = "Zählerstand ungewöhlich trotzdem Speichern?";
		        JLabel Alert_Label = new JLabel(Allert_Message);
				JButton Button_Ja = new JButton("Ja");
				JButton Button_Nein = new JButton("Nein");
				Create_Popup(Alert_Frame, Allert_Message);
		        Alert_Frame.setSize(400, 100);
		        Alert_Frame.setLocationRelativeTo(null);
		        Alert_Frame.setVisible(true);
		        Alert_Panel.add(Alert_Label);
		        Alert_Panel.add(Button_Nein);
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
		 }
	
	public void export() {
		liste.exportJson();
	}
	
	public void exit() {
		export();
		System.exit(0);
	}
		
	
	public static void main(String[] args) {
		new Ablesebogen();
	}
}