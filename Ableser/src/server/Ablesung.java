package server;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
	
	public Ablesung(String zaehlernummer, LocalDate datum, Kunde kunde, String kommentar, boolean neuEingebaut,
			Integer zaehlerstand) {
		super();
		this.id=UUID.randomUUID();
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
		this.kundenId=kundenId;
		
		if(Server.isServerReady()) { 		
			this.kunde=Server.getServerData().getKunde(kundenId);
		}
	}
	
	public void updateKunde() {
		this.setKundenId(this.getKundenId());
	}
	
	
	
	
}