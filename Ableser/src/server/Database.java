package server;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Getter;

public class Database {

	@Getter
	private ArrayList<Kunde> kundenListe;
	
	@Getter
	private ArrayList<Ablesung> ablesungListe;

	public Database() {
		super();
		kundenListe=new ArrayList<Kunde>();
		ablesungListe=new ArrayList<Ablesung>();
	}

	public Database(ArrayList<Kunde> kundenListe, ArrayList<Ablesung> ablesungListe) {
		super();
		this.kundenListe = kundenListe;
		this.ablesungListe = ablesungListe;
	}

	
	public void addKunde(Kunde k) {
		kundenListe.add(k);
	}
	public Kunde getKunde(UUID id) {
		for (Kunde k:kundenListe) {
			if (k.getId().equals(id)) {
				return k;
			}
		}
		return null;
	}
	
	public Kunde updateKunde(Kunde kunde) {
		Kunde k = this.getKunde(kunde.getId());
		if(k == null) return null;
		
		int index = kundenListe.indexOf(k);
		kundenListe.set(index, kunde);
		return k;
	}

	public Kunde removeKunde(UUID id) {
		Kunde k = this.getKunde(id);
		if(k == null) return null;
		
		Kunde tempKunde = k;
		kundenListe.remove(k);
		return tempKunde;
	}
	
	public ArrayList<Kunde> getAllKunden(){
		return kundenListe;
	}
		
	public void addAblesung(Ablesung a) {
		ablesungListe.add(a);
	}
	
	public Ablesung getAblesung(UUID id) {
		for (Ablesung a:ablesungListe) {
			if (a.getId().equals(id)) {
				return a;
			}
		}
		return null;
	}
	
	/**
	 * Aktualisiert eine Ablesung mit neuen Werten
	 * @param abNeu Die neue Ablesung - basierend auf der angegebenen id wird das alte Element gesucht
	 * @return false falls das Update fehlgeschlagen ist
	 */
	public boolean updateAblesung(Ablesung abNeu) {
		Ablesung abAlt=getAblesung(abNeu.getId());
		if (abAlt==null) {
			return false;
		}
		ablesungListe.remove(abAlt);
		ablesungListe.add(abNeu);
		return true;
	}
	
	
	public void init() {
		ablesungListe.forEach(e -> e.updateKunde());
	}
}
