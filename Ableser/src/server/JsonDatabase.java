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
public class JsonDatabase extends AbstractDatabase {

	@Getter
	private ArrayList<Kunde> kundenListe;

	@Getter
	private ArrayList<Ablesung> ablesungListe;

	@Getter
	private ArrayList<Zaehlerart> zaehlerartListe;

	public JsonDatabase() {
		super();
		kundenListe = new ArrayList<Kunde>();
		ablesungListe = new ArrayList<Ablesung>();
	}

	public JsonDatabase(ArrayList<Kunde> kundenListe, ArrayList<Ablesung> ablesungListe) {
		super();
		this.kundenListe = kundenListe;
		this.ablesungListe = ablesungListe;
	}

	@Override
	public OPERATION_RESULT addKunde(Kunde k) {
		if (k.getId() == null) {
			k.setId(UUID.randomUUID());
		}
		kundenListe.add(k);
		return OPERATION_RESULT.SUCCESS;
	}

	@Override
	public Kunde getKunde(UUID id) {
		for (Kunde k : kundenListe) {
			if (k.getId().equals(id)) {
				return k;
			}
		}
		return null;
	}

	@Override
	public OPERATION_RESULT updateKunde(Kunde kunde) {
		Kunde k = this.getKunde(kunde.getId());
		if (k == null) {
			return OPERATION_RESULT.KUNDE_NOT_FOUND;
		}
		
		int index = kundenListe.indexOf(k);
		kundenListe.set(index, kunde);
		return OPERATION_RESULT.SUCCESS;
	}

	@Override
	public Map<Kunde, ArrayList<Ablesung>> removeKunde(UUID id) {
		Kunde k = this.getKunde(id);
		if (k == null)
			return null;

		kundenListe.remove(k);

		ArrayList<Ablesung> aList = getAblesungList(id);
		for (Ablesung a : aList) {
			a.removeKunde();
		}
		Map<Kunde, ArrayList<Ablesung>> map = new HashMap<Kunde, ArrayList<Ablesung>>();
		map.put(k, aList);
		return map;
	}

	/*
	 * public ArrayList<Kunde> getAllKunden() { return kundenListe; }
	 */

	@Override
	public OPERATION_RESULT addAblesung(Ablesung a) {
		if (a==null) {
			return OPERATION_RESULT.ABLESUNG_NOT_FOUND;
		}
		if (a.getId() == null) {
			a.setId(UUID.randomUUID());
		}
		if (a.getKunde()==null) {
			return OPERATION_RESULT.KUNDE_NOT_FOUND;
		}
		ablesungListe.add(a);
		return OPERATION_RESULT.SUCCESS;
	}

	private int getAblesungIndex(UUID id) {
		for (int i = 0; i < ablesungListe.size(); i++) {
			if (ablesungListe.get(i).getId().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Ablesung getAblesung(UUID id) {
		int pos = getAblesungIndex(id);
		if (pos < 0) {
			return null;
		}
		return ablesungListe.get(pos);
	}

	/**
	 * Aktualisiert eine Ablesung mit neuen Werten
	 * 
	 * @param abNeu Die neue Ablesung - basierend auf der angegebenen id wird das
	 *              alte Element gesucht
	 * @return false falls das Update fehlgeschlagen ist
	 */
	@Override
	public OPERATION_RESULT updateAblesung(Ablesung abNeu) {
		if (abNeu.getKunde()==null) {
			return OPERATION_RESULT.KUNDE_NOT_FOUND;
		}
		int pos = getAblesungIndex(abNeu.getId());
		if (pos < 0) {
			return OPERATION_RESULT.ABLESUNG_NOT_FOUND;
		}
		ablesungListe.remove(pos);
		ablesungListe.add(abNeu);
		return OPERATION_RESULT.SUCCESS;
	}

	@Override
	public Ablesung deleteAblesung(UUID id) {
		Ablesung abl = getAblesung(id);
		if (abl != null) {
			ablesungListe.remove(abl);
		}
		return abl;
	}

	
	public void init() {
		ablesungListe.forEach(e -> e.updateKunde());
	}

	@Override
	public ArrayList<Ablesung> getAblesungList(UUID kundenId, LocalDate sDate, LocalDate eDate) {
		ArrayList<Ablesung> ausgabe = new ArrayList<Ablesung>();
		for (Ablesung a : ablesungListe) {
			if (kundenId != null) {
				if (!kundenId.equals(a.getKundenId())) {
					continue;
				}
			}
			if (a.getDatum() != null) {
				if (sDate != null) {
					if (sDate.isAfter(a.getDatum())) {
						continue;
					}
				}
				if (eDate != null) {
					if (eDate.isBefore(a.getDatum())) {
						continue;
					}
				}
			}
			ausgabe.add(a);
		}

		return ausgabe;
	}

	@Override
	protected void saveJSON(String file) {
		saveJSON(file, this);
		
	}


	@Override
	public Zaehlerart addZaehlerart(Zaehlerart za) {
		if (za==null) {
			return null;
		}
		zaehlerartListe.add(za);
		return za;
	}

	@Override
	public OPERATION_RESULT updateZaehlerart(Zaehlerart za) {
		if (za==null) {
			return OPERATION_RESULT.Zaehlerart_NOT_FOUND;
		}
		Zaehlerart oldZa=getZaehlerart(za.getId());
		if (oldZa==null) {
			return OPERATION_RESULT.Zaehlerart_NOT_FOUND;
		}
		
		return null;
	}

	@Override
	public Zaehlerart deleteZaehlerart(int id) {
		Zaehlerart za=getZaehlerart(id);
		if (za!=null) {
			zaehlerartListe.remove(za);
		}
		return za;
	}

	@Override
	public Zaehlerart getZaehlerart(int id) {
		for (Zaehlerart za:zaehlerartListe) {
			if (za.getId()==id) {
				return za;
			}
		}
		return null;
	}
	
	
}
