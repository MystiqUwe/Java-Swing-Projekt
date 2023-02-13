package server;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public abstract class AbstractDatabase{

	public abstract ArrayList<Kunde> getKundenListe();
	public abstract ArrayList<Ablesung> getAblesungListe();
	
	public abstract OPERATION_RESULT addKunde(Kunde k);
	public abstract Kunde getKunde(UUID id);
	public abstract OPERATION_RESULT updateKunde(Kunde kunde);
	public abstract Map<Kunde, ArrayList<Ablesung>> removeKunde(UUID id);

	public abstract OPERATION_RESULT addAblesung(Ablesung a);
	//public abstract int getAblesungIndex(UUID id);
	public abstract Ablesung getAblesung(UUID id);
	/**
	 * Aktualisiert eine Ablesung mit neuen Werten
	 * 
	 * @param abNeu Die neue Ablesung - basierend auf der angegebenen id wird das
	 *              alte Element gesucht
	 * @return false falls das Update fehlgeschlagen ist
	 */
	public abstract OPERATION_RESULT updateAblesung(Ablesung abNeu);
	public abstract Ablesung deleteAblesung(UUID id);
	
	public ArrayList<Ablesung> getAblesungList(UUID kundenId) {
		return getAblesungList(kundenId, null, null);
	}
	public abstract ArrayList<Ablesung> getAblesungList(UUID kundenId, LocalDate sDate, LocalDate eDate);
	
	private static ObjectMapper obMap = new ObjectMapper();
	
	protected static AbstractDatabase loadJSON(String file) {
		final File f = new File(file);
		if (f.exists()) {
			try {
				obMap.registerModule(new JavaTimeModule());
				JsonDatabase db = obMap.readValue(f, JsonDatabase.class);

				System.out.format("Datei %s gelesen\n", file);

				return db;
			} catch (final Exception e) {
				e.printStackTrace();
				// ignore
			}
		}
		return new JsonDatabase();

	}

	protected abstract void saveJSON(String file);
		
	protected void saveJSON(String file, JsonDatabase database) {
		try {

			// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			// obMap.setDateFormat(df);
			obMap.registerModule(new JavaTimeModule());
			obMap.writerWithDefaultPrettyPrinter().writeValue(new File(file), database);

			System.out.format("Datei %s erzeugt\n", file);
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	//Zaehlerarten
	public abstract ArrayList<Zaehlerart> getZaehlerartListe();
	public abstract Zaehlerart addZaehlerart(Zaehlerart za);
	public abstract OPERATION_RESULT updateZaehlerart(Zaehlerart za);
	public abstract Zaehlerart deleteZaehlerart(int id);
	public abstract Zaehlerart getZaehlerart(int id);//*/
	
	
}
