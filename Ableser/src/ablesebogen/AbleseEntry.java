package ablesebogen;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
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
	@JsonIgnore
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
	
	/** 
	 * lombak erstellt diesen Getter nicht
	 * 
	 * @return boolean
	 */
	public boolean getNeuEingebaut() {
		return neuEingebaut;
	}

	@Override
	public int hashCode() {
		return Objects.hash(kommentar);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbleseEntry other = (AbleseEntry) obj;
		return Objects.equals(datum, other.datum) && Objects.equals(id, other.id)
				&& Objects.equals(kommentar, other.kommentar) && Objects.equals(kundenNummer, other.kundenNummer)
				&& neuEingebaut == other.neuEingebaut && Objects.equals(zaelerArt, other.zaelerArt)
				&& Objects.equals(zaelernummer, other.zaelernummer) && zaelerstand == other.zaelerstand;
	}
	
	
}
