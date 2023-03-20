package client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import client.ablesungen.AbleseList;
import client.kunden.KundeList;
import client.zaehlerart.ZaehlerartList;
import dataEntities.AbleseEntry;
import dataEntities.Kunde;
import dataEntities.Zaehlerart;
import lombok.Getter;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@Getter
public class ClientSave {
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private static final String FILE = "target/Ablesewerte.json";
	private static final String XMLFILE = "target/Ablesewerte.xml";
	private static final String CSVFILE = "target/Ablesewerte.csv";

	private ArrayList<Kunde> kundenListe;

	private ArrayList<Zaehlerart> zaehlerArtListe;

	private ArrayList<AbleseEntry> ablesungListe;

	public ClientSave(ArrayList<Kunde> kList, ArrayList<Zaehlerart> zList, ArrayList<AbleseEntry> aList) {
		super();
		this.kundenListe = kList;
		this.zaehlerArtListe = zList;
		this.ablesungListe = aList;


	}

	public ClientSave(KundeList kList, ZaehlerartList zList, AbleseList aList) {
		this(kList.getListe(),zList.getListe(),aList.getListe());
	}

	public ClientSave(KundeList kList, ZaehlerartList zList, AbleseList aList,String filter) {
		this(kList, zList, aList);
		if (filter!=null) {
			this.kundenListe=new ArrayList<>(kList.stream().filter(e -> e.getId().toString().startsWith(filter)).collect(Collectors.toList()));
			this.zaehlerArtListe=zList.getListe();//TODO
			this.ablesungListe=new ArrayList<>(aList.stream().filter(e -> {
				if (e.getKundenNummer()!=null) {
					return e.getKundenNummer().toString().startsWith(filter);
				} else {
					return false;
				}
				}).collect(Collectors.toList()));
		}
	}

	public void exportJson() {
		try {
			ObjectMapper obMap = new ObjectMapper();
			obMap.registerModule(new JavaTimeModule());
			obMap.setDateFormat(df);
			obMap.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), this);
			System.out.format("Datei %s erzeugt\n", FILE);
		} catch (final Exception e) {
			e.printStackTrace();
			Util.errorMessage("Export fehlgeschlagen");
			//System.exit(1);
		}
	}

	public void exportXML() {
		try {
			XmlMapper xmlMapper = new XmlMapper();
			xmlMapper.registerModule(new JavaTimeModule());
			xmlMapper.setDateFormat(df);
			xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(XMLFILE), this);
			System.out.format("Datei %s erzeugt\n", XMLFILE);
		} catch (final Exception e) {
			e.printStackTrace();
			Util.errorMessage("Export fehlgeschlagen");
			//System.exit(1);
		}
	}

	public void exportCSV() {
		try {
			final BufferedWriter out = new BufferedWriter(new FileWriter(CSVFILE, StandardCharsets.UTF_8));

			out.write("kundenListe\n");
			out.write("id;name;vorname\n");

			for (final Kunde entry : kundenListe) {
				out.write(entry.getId().toString());
				out.write(";");
				out.write(entry.getName());
				out.write(";");
				out.write(entry.getVorname());
				out.write("\n");

			}

			out.write("zaehlerArtListe\n");
			out.write("id;name;warnValue\n");

			for (final Zaehlerart entry : zaehlerArtListe) {
				out.write(""+entry.getId());
				out.write(";");
				out.write(entry.getName());
				out.write(";");
				out.write(""+entry.getWarnValue());
				out.write("\n");

			}

			out.write("ablesungListe\n");
			out.write("id;kundenNummer;zaelerArt;zaehlernummer;datum;neuEingebaut;zaehlerstand;kommentar\n");
			for (final AbleseEntry entry : ablesungListe) {
				out.write(entry.getId().toString());
				out.write(";");
				if (entry.getKundenNummer()!=null) {
					out.write(entry.getKundenNummer().toString());
				}
				out.write(";");
				out.write(entry.getZId());
				out.write(";");
				out.write("" + entry.getZaelernummer());
				out.write(";");
				out.write(entry.getDatum().toString());
				out.write(";");
				out.write("" + entry.isNeuEingebaut());
				out.write(";");
				out.write("" + entry.getZaelerstand());
				out.write(";");
				out.write(entry.getKommentar());
				out.write("\n");
			}
			out.close();
			System.out.format("Datei %s erzeugt\n", CSVFILE);
		} catch (final IOException e) {
			e.printStackTrace();
			Util.errorMessage("Export fehlgeschlagen");
			//System.exit(1);
		}
	}


	/**
	 * @return AbleseList
	 */
	public static AbleseList importJson(Service service) {
		final File f = new File(FILE);
		if (f.exists()) {
			try {
				ObjectMapper obMap = new ObjectMapper();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				obMap.setDateFormat(df);

				AbleseList list = obMap.readValue(new File(FILE), AbleseList.class);

				System.out.format("Datei %s gelesen\n", FILE);
				return list;

			} catch (final Exception e) {
				e.printStackTrace();
				// ignore
			}
		}
		return new AbleseList(null,null);
	}

}
