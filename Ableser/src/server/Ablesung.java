package server;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@JsonTypeName(value = "ablesung")
public class Ablesung {

	private UUID id;
	private String zaehlernummer;
	private LocalDate datum;
	@JsonIgnore
	private Kunde kunde;
	private String kommentar;
	private boolean neuEingebaut;
	private Integer zaehlerstand;
	
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
	
	public UUID getKundenId() {
		if (kunde!=null) {
			return kunde.getId();
		} else {
			return kundenId;
		}
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
	
	
	
	
}