package server;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractDatabase{

	public abstract ArrayList<Kunde> getKundenListe();
	public abstract ArrayList<Ablesung> getAblesungListe();
	public abstract void init();

	public abstract void addKunde(Kunde k);
	public abstract Kunde getKunde(UUID id);
	public abstract Kunde updateKunde(Kunde kunde);
	public abstract Map<Kunde, ArrayList<Ablesung>> removeKunde(UUID id);

	public abstract void addAblesung(Ablesung a);
	public abstract int getAblesungIndex(UUID id);
	public abstract Ablesung getAblesung(UUID id);
	/**
	 * Aktualisiert eine Ablesung mit neuen Werten
	 * 
	 * @param abNeu Die neue Ablesung - basierend auf der angegebenen id wird das
	 *              alte Element gesucht
	 * @return false falls das Update fehlgeschlagen ist
	 */
	public abstract boolean updateAblesung(Ablesung abNeu);
	public abstract Ablesung deleteAblesung(UUID id);
	
	public ArrayList<Ablesung> getAblesungList(UUID kundenId) {
		return getAblesungList(kundenId, null, null);
	}
	public abstract ArrayList<Ablesung> getAblesungList(UUID kundenId, LocalDate sDate, LocalDate eDate);
}
