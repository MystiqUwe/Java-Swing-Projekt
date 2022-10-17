package ablesebogen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Ablesebogen extends JFrame{

	private JPanel panel;
	
	private JTextField kundenNummer;
	private JTextField zaelerArt;
	private JTextField zaelernummer;
	private JTextField datum;
	private JTextField neuEingebaut;
	private JTextField zaelerstand;
	private JTextField kommentar;
	
	public Ablesebogen() {
		super("Ablesebogen");
		this.setSize(400, 250);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				exit();
			}
		});

		final Container con = getContentPane();
		con.setLayout(new BorderLayout());
		panel = new JPanel(new GridLayout(7,2));

		con.add(panel);
		
		kundenNummer=new JTextField();
		zaelerArt=new JTextField();
		zaelernummer=new JTextField();
		datum=new JTextField();
		neuEingebaut=new JTextField();
		zaelerstand=new JTextField();
		kommentar=new JTextField();
		
		panel.add(new JLabel("Kundennummer"));
		panel.add(kundenNummer);
		panel.add(new JLabel("Zählerart"));
		panel.add(zaelerArt);
		panel.add(new JLabel("Zählernummer"));
		panel.add(zaelernummer);
		panel.add(new JLabel("Datum"));
		panel.add(datum);
		panel.add(new JLabel("neu eingebaut"));
		panel.add(neuEingebaut);
		panel.add(new JLabel("Zählerstand"));
		panel.add(zaelerstand);
		panel.add(new JLabel("Kommentar"));
		panel.add(kommentar);
		
		this.setVisible(true);
	}
	
	public void exit() {
		System.exit(0);
	}
	
	public static void main(String[] args) {
		new Ablesebogen();
	}
}
