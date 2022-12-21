package ablesebogen;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import server.Kunde;

/* Datenklasse, repräsentiert einen einzelnen Dateneintrag*/
@Getter
@Setter
@NoArgsConstructor
public class AbleseEntry {

	private UUID id;
	@JsonProperty("kundenId")
	private UUID kundenNummer;
	//@JsonIgnore
	private String zaelerArt;
	@JsonProperty("zaehlernummer")
	private String zaelernummer;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate datum;
	private boolean neuEingebaut;
	@JsonProperty("zaehlerstand")
	private int zaelerstand;
	private String kommentar;
	
	public AbleseEntry(UUID id, UUID kundenNummer, String zaelerArt, String zaelernummer, LocalDate datum, boolean neuEingebaut,
			int zaelerstand, String kommentar) {
		this.id=id;
		this.kundenNummer = kundenNummer;
		this.zaelerArt = zaelerArt;
		this.zaelernummer = zaelernummer;
		this.datum = datum;
		this.neuEingebaut = neuEingebaut;
		this.zaelerstand = zaelerstand;
		this.kommentar = kommentar;
	}
	
	public AbleseEntry(String zaehlernummer, LocalDate datum, Kunde kunde, String kommentar, boolean neuEingebaut,
			Integer zaehlerstand) {
		System.out.println("XXX");
		this.id=null;
		this.kundenNummer = null;
		this.zaelerArt = zaelerArt;
		this.zaelernummer = zaelernummer;
		this.datum = datum;
		this.neuEingebaut = neuEingebaut;
		this.zaelerstand = zaelerstand;
		this.kommentar = kommentar;
		
		
	}

		
	
	/** 
	 * lombak erstellt diesen Getter nicht
	 * 
	 * @return boolean
	 */
	public boolean getNeuEingebaut() {
		return neuEingebaut;
	}
}
