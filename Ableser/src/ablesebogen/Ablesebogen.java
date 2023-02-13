package ablesebogen;

import java.awt.CardLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.Service;
import lombok.Getter;
import server.Server;

@SuppressWarnings("serial")
public class Ablesebogen extends JFrame {

	// Datenspeicher
	@Getter
	private KundeList kundenListe;
	@Getter
	private AbleseList liste; // Liste von allen Daten

	// UI Panels
	private JAblesebogenPanel activePanel; 
	protected AbleseInPanel inLayout;
	protected AbleseOutPanel outLayout;
	protected KundeOutPanel outLayoutKunde;
	protected KundenInPanel inLayoutKunde;

	@Getter
	private Service service;
	private String baseURL;

	public final static String ABLESUNG_IN = "ablIn";
	public final static String ABLESUNG_OUT = "ablOut";
	public final static String KUNDE_IN = "kIn";
	public final static String KUNDE_OUT = "kOut";

	private JTextField filterArea;
	
	public Ablesebogen(String baseUrl) {
		super();
		// Für unser eigenes Icon
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("swarm.png")));
		this.setSize(650, 275);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				exit();
			}
		});
		drawMenu();

		// Serververbindung
		this.baseURL = baseUrl;
		service = new Service(baseURL);
		// DATENIMPORT
		this.liste = new AbleseList(service);
		this.kundenListe = new KundeList(service);

		// Root Container
		final Container con = getContentPane();
		con.setLayout(new CardLayout());

		inLayout = new AbleseInPanel(this);
		con.add(inLayout, ABLESUNG_IN);

		inLayoutKunde = new KundenInPanel(this);
		con.add(inLayoutKunde, KUNDE_IN);

		outLayout = new AbleseOutPanel(this, liste);
		con.add(outLayout, ABLESUNG_OUT);

		outLayoutKunde = new KundeOutPanel(this, kundenListe);
		con.add(outLayoutKunde, KUNDE_OUT);

		openPage(ABLESUNG_IN);
		this.setVisible(true);
	}
	
	protected void loadData(ArrayList<String[]> filter) {
		kundenListe.refresh();
		outLayoutKunde.refresh();
		liste.refresh(filter);
		outLayout.refresh();
	}

	protected void loadData() {
		kundenListe.refresh();
		outLayoutKunde.refresh();
		
		liste.refresh();
		outLayout.refresh();
	}

	// Hilfsfunktion für die Menüleiste
	private void drawMenu() {
		JMenuBar mb = new JMenuBar();
		// Context Menu
		JMenu contextMenu = new JMenu("Bereiche");

		JMenuItem toAblesung = new JMenuItem("Ablesungen");
		JMenuItem toKunden = new JMenuItem("Kunden");

		toAblesung.addActionListener(e -> {
			openPage(ABLESUNG_OUT);
		});

		toKunden.addActionListener(e -> {
			openPage(KUNDE_OUT);
		});

		contextMenu.add(toAblesung);
		contextMenu.add(toKunden);
		mb.add(contextMenu);

		JMenu menu = new JMenu("Ex-/Import");

		JMenuItem subReload = new JMenuItem("Daten neuladen");

		JMenuItem subMenuJSON = new JMenuItem("JSON exportieren");
		JMenuItem subMenuXML = new JMenuItem("XML exportieren");
		JMenuItem subMenuCSV = new JMenuItem("CSV exportieren");

		subReload.addActionListener(ev -> {
			loadData();
		});

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
		menu.add(subReload);
		menu.add(subMenuJSON);
		menu.add(subMenuXML);
		menu.add(subMenuCSV);

		mb.add(menu);
		
		
		mb.add(Box.createHorizontalGlue());
		
		
		filterArea=new JTextField("");		
		//TODO Filtertext
		//ta.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		
		filterArea.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				//System.out.println(filterArea.getText());
				activePanel.filter(filterArea.getText());
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		
		mb.add(filterArea,-1);

		this.setJMenuBar(mb);

		
	}

	public String getFilter() {
		return filterArea.getText();
	};
	public void setFilter(String filter) {
		filterArea.setText(filter);
	};

	public void exit() {
		// liste.exportJson(); Kein Lokaler Speicher mehr
		Server.stopServer(true);
		System.exit(0);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://localhost:8081/rest";
		Server.startServer(url);
		new Ablesebogen(url);
		new Ablesebogen(url);
	}

	public void openPage(String page) {
		openPage(page, null);
	}
	

	public void openPage(String page, Object eOpts) {
		JAblesebogenPanel newPanel;
		switch (page) {
		case ABLESUNG_OUT:
			newPanel = outLayout;
			break;
		case ABLESUNG_IN:
			newPanel = inLayout;
			break;
		case KUNDE_OUT:
			newPanel = outLayoutKunde;
			break;
		case KUNDE_IN:
			newPanel = inLayoutKunde;
			break;
		default:
			return;
		}
		boolean open=newPanel.activate(eOpts);
		
		if (open) {
			((CardLayout) getContentPane().getLayout()).show(getContentPane(), page);
			activePanel=newPanel;
			filterArea.setVisible(newPanel.showFilter());
			newPanel.afterActivate(eOpts);
		}
	}

}
