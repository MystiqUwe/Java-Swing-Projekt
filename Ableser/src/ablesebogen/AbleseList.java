package ablesebogen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import client.Service;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.Getter;
import lombok.Setter;

//Wrapper für die verwendete Liste, außerdem verantwortlich für den Export/Import mit den Methoden "export<Dateiformat>()
public class AbleseList {

	private static ObjectMapper obMap = new ObjectMapper();
	private static final String FILE = "target/Ablesewerte.json";
	private static final String XMLFILE = "target/Ablesewerte.xml";
	private static final String CSVFILE = "target/Ablesewerte.csv";

	@Getter
	@Setter
	private ArrayList<AbleseEntry> liste = new ArrayList<AbleseEntry>();

	private Service service;

	public AbleseList(Service service) {
		super();
		this.service = service;
		liste = new ArrayList<>();
		refresh();
	}

	/**
	 * @param e
	 * @return boolean
	 */
	public boolean add(AbleseEntry e) {
		Response res = service.post(Service.endpointAblesungen, e);

		if (res.getStatus() != Status.CREATED.getStatusCode()) {
			Util.errorMessage(res.getStatus() + " - " + res.readEntity(String.class));
			return false;
		}

		AbleseEntry serverEntry = res.readEntity(AbleseEntry.class);

		return liste.add(serverEntry);
	}

	public void clear() {
		liste.clear();
	}

	public boolean update(AbleseEntry oldAbl, AbleseEntry newAbl) {

		switch (checkChanged(oldAbl)) {
		case noSave:
			return false;
		case addNew:
			liste.remove(oldAbl);
			return add(newAbl);
		case doSave:
			break;
		default:
			break;
		}

		Response res = service.put(Service.endpointAblesungen, newAbl);

		if (res.getStatus() != Status.OK.getStatusCode()) {
			Util.errorMessage(res.getStatus() + " - " + res.readEntity(String.class));
			return false;
		}
		oldAbl.setKundenNummer(newAbl.getKundenNummer());
		oldAbl.setZaelerArt(newAbl.getZaelerArt());
		oldAbl.setZaelernummer(newAbl.getZaelernummer());
		oldAbl.setDatum(newAbl.getDatum());
		oldAbl.setNeuEingebaut(newAbl.getNeuEingebaut());
		oldAbl.setZaelerstand(newAbl.getZaelerstand());
		oldAbl.setKommentar(newAbl.getKommentar());

		return true;
	}

	/**
	 * @param index
	 * @return AbleseEntry
	 */
	public AbleseEntry get(int index) {
		return liste.get(index);
	}

	/**
	 * @param entry
	 * @return int
	 */
	public int indexOf(AbleseEntry entry) {
		return liste.indexOf(entry);
	}

	/**
	 * @param entry
	 * @return AbleseEntry
	 */
	public boolean remove(AbleseEntry entry) {
		if (entry == null) {
			return false;
		}

		Response delRes = service.delete(Service.endpointAblesungen+"/" + entry.getId().toString());

		if (delRes.getStatus() != Status.OK.getStatusCode()) {
			if (delRes.getStatus() == Status.NOT_FOUND.getStatusCode()) {
				Util.errorMessage("Datensatz wurde bereits gelöscht\n" + delRes.getStatus() + " -"
						+ delRes.readEntity(String.class));
			} else {
				Util.errorMessage(
						"Löschen fehlgeschlagen\n" + delRes.getStatus() + " -" + delRes.readEntity(String.class));
				return false;
			}
		}

		int index = indexOf(entry);
		if (index < 0) {
			return false;
		}
		return remove(index) != null;
	}

	/**
	 * @param index
	 * @return AbleseEntry
	 */
	private AbleseEntry remove(int index) {
		return liste.remove(index);
	}

	/**
	 * @return int
	 */
	public int size() {
		return liste.size();
	}

	/**
	 * @return Stream<AbleseEntry>
	 */
	public Stream<AbleseEntry> stream() {
		return liste.stream();
	}

	/**
	 * @return String
	 */
	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		liste.stream().forEach(en -> buf.append(en.toString()));
		return buf.toString();
	}
	
	/**
	 * @param kNummer
	 * @param zNummer
	 * @return AbleseEntry
	 */
	public  AbleseEntry getLast(UUID kNummer, String zNummer) {
		AbleseEntry closest = null;
		LocalDate date = LocalDate.now();
		long closestDiff = Long.MAX_VALUE;
		for(AbleseEntry obj : this.getListe()) {
			if(obj.getKundenNummer().equals(kNummer) && obj.getZaelernummer().equals(zNummer)) {
				long diff = Math.abs(date.toEpochDay() - obj.getDatum().toEpochDay());
				if(diff < closestDiff) {
				closest = obj;
				closestDiff = diff;
				}
			}
		}
		return closest;
		//return this.stream().filter((e) -> e.getKundenNummer().equals(kNummer) &&  e.getZaelernummer().equals(zNummer));
	}

	/**
	 * @return AbleseList
	 */
	public static AbleseList importJson(Service service) {
		final File f = new File(FILE);
		if (f.exists()) {
			try {
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
		return new AbleseList(null);
	}

	public void exportJson() {
		try {

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			obMap.setDateFormat(df);
			obMap.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), this);

			System.out.format("Datei %s erzeugt\n", FILE);
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void exportXML() {
		try {
			XmlMapper xmlMapper = new XmlMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			xmlMapper.setDateFormat(df);

			xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(XMLFILE), this);
			System.out.format("Datei %s erzeugt\n", XMLFILE);
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void exportCSV() {
		try {
			final BufferedWriter out = new BufferedWriter(new FileWriter(CSVFILE, StandardCharsets.UTF_8));
			for (final AbleseEntry entry : liste) {
				out.write(entry.getId().toString());
				out.write(";");
				out.write(entry.getKundenNummer().toString());
				out.write(";");
				out.write(entry.getZaelerArt());
				out.write(";");
				out.write("" + entry.getZaelernummer());
				out.write(";");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				out.write(df.format(entry.getDatum()));
				out.write(";");
				out.write("" + entry.getNeuEingebaut());
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
			System.exit(1);
		}
	}

	public boolean refresh() {
		Response res = service.get(Service.endpointAblesungClientStart);

		if (res.getStatus() != 200) {
			System.out.println(
					"Laden der Ablesungen fehlgeschlagen\n" + res.getStatus() + " - " + res.readEntity(String.class));
			Util.errorMessage(res.readEntity(String.class));
			return false;
		}

		liste = res.readEntity(new GenericType<ArrayList<AbleseEntry>>() {
		});
		return true;
	}

	public boolean refresh(ArrayList<String[]> map) {
		Response res = service.get(Service.endpointAblesungen, map);

		if (res.getStatus() != 200) {
			if (res.getStatus() != 404) {
				System.out.println("Laden der Ablesungen fehlgeschlagen\n" + res.getStatus() + " - "
						+ res.readEntity(String.class));
				Util.errorMessage(res.readEntity(String.class));
				return false;
			}
			liste = new ArrayList<>();
			return true;
		}

		liste = res.readEntity(new GenericType<ArrayList<AbleseEntry>>() {
		});
		return true;
	}

	private enum ChangedState {
		noSave, doSave, addNew;
	}

	private ChangedState checkChanged(AbleseEntry abl) {
		Response res = service.get(Service.endpointAblesungen+"/" + abl.getId().toString());
		switch (res.getStatus()) {
		case 200:
			AbleseEntry ablServer = res.readEntity(AbleseEntry.class);
			if (!abl.equals(ablServer)) {
	/*			System.out.println("DIFFTOOL");
				System.out.println(abl.toString());
				System.out.println(ablServer.toString());
				System.out.println("--------");*/
				if (Util.optionMessage("Ablesung hat sich geändert \nTrotzdem speichern?")) {
					return ChangedState.doSave;
				} else {
					return ChangedState.noSave;
				}
			}
			return ChangedState.doSave;
		case 404:
			if (Util.optionMessage(
					"404 - Ablesung nicht gefunden, wurde die Ablesung gelöscht?\nTrotzdem speichern?")) {
				return ChangedState.addNew;
			} else {
				return ChangedState.noSave;
			}

		default:
			if (Util.optionMessage(res.getStatus() + " - " + res.readEntity(String.class) + "\nTrotzdem speichern?")) {
				return ChangedState.doSave;
			} else {
				return ChangedState.noSave;
			}
		}
	}

}
