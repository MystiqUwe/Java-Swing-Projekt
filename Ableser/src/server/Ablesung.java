package server;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@JsonTypeName(value = "ablesung")
public class Ablesung {

	private UUID id;
	private String zaehlernummer;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate datum;
	@JsonIgnore
	//@Setter(AccessLevel.NONE)
	private Kunde kunde;
	private String kommentar;
	private boolean neuEingebaut;
	private Number zaehlerstand;
	
	private UUID kundenId;
	
	public Ablesung() {
		this.id=UUID.randomUUID();
	}
	public Ablesung(String zaehlernummer, LocalDate datum, Kunde kunde, String kommentar, boolean neuEingebaut,
			Integer zaehlerstand) {
		this();
		this.zaehlernummer = zaehlernummer;
		this.datum = datum;
		this.kunde = kunde;
		this.kommentar = kommentar;
		this.neuEingebaut = neuEingebaut;
		this.zaehlerstand = zaehlerstand;
	}
	
	public void setKunde(Kunde k) {
		this.kunde=k;
		this.kundenId= (k==null?null:k.getId());
	}
	
	public UUID getKundenId() {
		if (kunde!=null) {
			return kunde.getId();
		} else {
			return kundenId;
		}
	}
	
	public void removeKunde() {
		this.setKunde(null);
	}
	public void setKundenId(UUID kundenId) {
		setKundenId(kundenId,Server.isServerReady());
	}
		
	public void setKundenId(UUID kundenId, boolean updateKunde) {
		this.kundenId=kundenId;
		
		if( updateKunde) { 		
			this.kunde=Server.getServerData().getKunde(kundenId);
		}
	}
	
	public void updateKunde() {
		this.setKundenId(this.getKundenId(),true);
	}
	@Override
	public String toString() {
		return "Ablesung [id=" + id + ", zaehlernummer=" + zaehlernummer + ", datum=" + datum + ", kunde=" + kunde
				+ ", kommentar=" + kommentar + ", neuEingebaut=" + neuEingebaut + ", zaehlerstand=" + zaehlerstand
				+ ", kundenId=" + kundenId + "]";
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Ablesung)) {
			return false;
		}
		Ablesung abl=(Ablesung) obj;
		
		if (!(getId().equals(abl.getId()))) {
			return false;
		}
		if (!getZaehlernummer().equals(abl.getZaehlernummer())) {
			return false;
		}
		if (!getDatum().equals(abl.getDatum())) {
			return false;
		}
		if (getKunde()==null) {
			if(abl.getKunde()!=null) {
				return false;
			}
		} else {
			if (!getKunde().equals(abl.getKunde())) {
				return false;
			}
		}
		if (!getKommentar().equals(abl.getKommentar())) {
			return false;
		}
		if (!this.isNeuEingebaut()==abl.isNeuEingebaut()) {
			return false;
		}		
		if (!getZaehlerstand().equals(abl.getZaehlerstand())) {
			return false;
		}

		return true;
	}
	
	
}