package server;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import lombok.Getter;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
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
		if (k.getId()==null) {
			k.setId(UUID.randomUUID());
		}
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

	public Map<Kunde, ArrayList<Ablesung>> removeKunde(UUID id) {
		Kunde k = this.getKunde(id);
		if(k == null) return null;
		
		Kunde tempKunde = k;
		kundenListe.remove(k);
		
		ArrayList<Ablesung> aList=getAblesungList(id);
		for (Ablesung a:aList) {
			a.removeKunde();
		}
		Map<Kunde, ArrayList<Ablesung>> map = new HashMap<Kunde, ArrayList<Ablesung>>();
		map.put(k, aList);
		return map;
	}
	
	public ArrayList<Kunde> getAllKunden(){
		return kundenListe;
	}
		
	public void addAblesung(Ablesung a) {
		if (a.getId()==null) {
			a.setId(UUID.randomUUID());
		}
		ablesungListe.add(a);
	}
	
	public int getAblesungIndex(UUID id) {
		for (int i=0;i<ablesungListe.size();i++) {
			if (ablesungListe.get(i).getId().equals(id)) {
				return i;
			}
		}
		return -1;
	}
	
	public Ablesung getAblesung(UUID id) {
		int pos=getAblesungIndex(id);
		if (pos<0) {
			return null;
		}
		return ablesungListe.get(pos);
	}
	
	/**
	 * Aktualisiert eine Ablesung mit neuen Werten
	 * @param abNeu Die neue Ablesung - basierend auf der angegebenen id wird das alte Element gesucht
	 * @return false falls das Update fehlgeschlagen ist
	 */
	public boolean updateAblesung(Ablesung abNeu) {
		int pos=getAblesungIndex(abNeu.getId());
		if (pos<0) {
			return false;
		}
		ablesungListe.remove(pos);
		ablesungListe.add(abNeu);
		return true;
	}
	
	public Ablesung deleteAblesung(UUID id) {
		Ablesung abl=getAblesung(id);
		if (abl!=null) {
			ablesungListe.remove(abl);
			abl.removeKunde(); //***Für die Test nötig***
		}
		return abl;
	}
	
	public void init() {
		ablesungListe.forEach(e -> e.updateKunde());
	}

	
	public ArrayList<Ablesung> getAblesungList(UUID kundenId) {
		return getAblesungList(kundenId,null,null);
	}
	public ArrayList<Ablesung> getAblesungList(UUID kundenId, LocalDate sDate, LocalDate eDate) {
		ArrayList<Ablesung> ausgabe=new ArrayList<Ablesung>();
		for (Ablesung a:ablesungListe) {
			if (kundenId!=null) {
				if (!a.getKundenId().equals(kundenId)) {
					continue;
				}
			}
			if (a.getDatum()!=null) {
				if (sDate!=null) {
					if (sDate.isAfter(a.getDatum())) {
						continue;
					}
				}
				if (eDate!=null) {
					if (eDate.isBefore(a.getDatum())) {
						continue;
					}
				}
			}
			ausgabe.add(a);
		}
		
		return ausgabe;
	}
}
