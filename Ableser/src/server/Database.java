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
	
	
	
	public void addAblesung(Ablesung a) {
		ablesungListe.add(a);
	}
	
	
	public void init() {
		ablesungListe.forEach(e -> e.updateKunde());
	}
}
