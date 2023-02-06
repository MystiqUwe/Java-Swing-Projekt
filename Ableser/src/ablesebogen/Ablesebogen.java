package ablesebogen;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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

		this.setJMenuBar(mb);

	}

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
		Server.startServer(url, true);
		new Ablesebogen(url);
		//new Ablesebogen(url);
	}

	public void openPage(String page) {
		openPage(page, null);
	}

	public void openPage(String page, Object eOpts) {
		boolean open = false;
		switch (page) {
		case ABLESUNG_OUT:
			open = outLayout.activate(eOpts);
			break;
		case ABLESUNG_IN:
			open = inLayout.activate(eOpts);
			break;
		case KUNDE_OUT:
			open = outLayoutKunde.activate(eOpts);
			break;
		case KUNDE_IN:
			open = inLayoutKunde.activate(eOpts);
			break;
		default:

		}
		if (open) {
			((CardLayout) getContentPane().getLayout()).show(getContentPane(), page);
		}
	}

}
