package ablesebogen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;


import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class Ablesebogen extends JFrame{


	private AbleseList liste;


	private JPanel panel;
	private JPanel buttonPanel;
	
	private JTextField kundenNummer;
	private JTextField zaelernummer;
	private JTextField datum;
	private JTextField zaelerstand;
	private JTextField kommentar;
	
	private JButton saveButton;
	private JButton exportButton;
	
	//private JComboBox neuEingebaut;
	private JComboBox zaelerArt;
	
	private JCheckBox neuEingebaut;
	
	private JDatePickerImpl datePicker;
	
	//private String DEFAULT_EINGEBAUT[] = {"Ja", "Nein"};
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
		
		liste=new AbleseList();
		
		final Container con = getContentPane();
		con.setLayout(new BorderLayout());
		panel = new JPanel(new GridLayout(7,2));
		buttonPanel = new JPanel();
		con.add(panel);
		
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
		
		saveButton=new JButton("Speichern");
		exportButton=new JButton("Exportieren");
		
		buttonPanel.add(saveButton);
		buttonPanel.add(exportButton);
		
		con.add(buttonPanel, BorderLayout.SOUTH);
		
		saveButton.addActionListener(e -> {	
			save();
		});
		
		exportButton.addActionListener(e -> {	
		//	export();
		});
		
		
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
		

		this.setVisible(true);
	} 
	
	public void save() {
		try {
		String kn=kundenNummer.getText();
		String zA=zaelerArt.getSelectedItem().toString();
		int zN=Integer.parseInt(zaelernummer.getText());
		
		Date selectedDate = (Date) datePicker.getModel().getValue();
		boolean neuE=neuEingebaut.isSelected();// neuEingebaut.getText();		
		int zStand=Integer.parseInt(zaelerstand.getText());
		String kom=kommentar.getText();
		AbleseEntry entry=new AbleseEntry(kn,zA,zN,selectedDate,neuE,zStand,kom);
		liste.add(entry);
		} catch (Exception ex) {
			ex.printStackTrace();
		} try {
			int zN=Integer.parseInt(zaelernummer.getText());
		}catch (NumberFormatException ec) {
			JFrame Alert_Frame = new JFrame("Alert Window");
			String Allert_Message = "Zaehlernummer nicht Nummerisch";
			Create_Popup(Alert_Frame, Allert_Message);
	        Alert_Frame.setSize(400, 100);
	        Alert_Frame.setLocationRelativeTo(null);
	        Alert_Frame.setVisible(true);
		}try {
			int zStand=Integer.parseInt(zaelerstand.getText());
		}catch (NumberFormatException ec2) {
			JFrame Alert_Frame = new JFrame("Alert Window");
			String Allert_Message = "Zaehlerstand nicht Nummerisch";
			Create_Popup(Alert_Frame, Allert_Message);
	        Alert_Frame.setSize(400, 100);
	        Alert_Frame.setLocationRelativeTo(null);
	        Alert_Frame.setVisible(true);

		}
		}
		private static void Create_Popup(final JFrame Alert_Frame, String Alert_Massage){
	        JPanel Alert_Panel = new JPanel();
	        LayoutManager Alert_Layout = new FlowLayout();
	        Alert_Panel.setLayout(Alert_Layout);
	        JLabel Alert_Label = new JLabel(Alert_Massage);

	        Alert_Panel.add(Alert_Label);
	        Alert_Frame.getContentPane().add(Alert_Panel, BorderLayout.CENTER);
	}
	public void export() {

	}
	
	public void exit() {
		System.exit(0);
	}
		
	
	public static void main(String[] args) {
		new Ablesebogen();
	}
}
