package dataEntities;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* Datenklasse, repräsentiert einen einzelnen Dateneintrag*/
@Getter
@Setter
@NoArgsConstructor
public class AbleseEntry {

	private UUID id;
	@JsonProperty("kundenId")
	private UUID kundenNummer;
	private int zId;
	@JsonProperty("zaehlernummer")
	private String zaelernummer;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate datum;
	private boolean neuEingebaut;
	@JsonProperty("zaehlerstand")
	private int zaelerstand;
	private String kommentar;

	public AbleseEntry(UUID id, UUID kundenNummer, int zaelerArt, String zaelernummer, LocalDate datum,
			boolean neuEingebaut, int zaelerstand, String kommentar) {
		this.id = id;
		this.kundenNummer = kundenNummer;
		this.zId = zaelerArt;
		this.zaelernummer = zaelernummer;
		this.datum = datum;
		this.neuEingebaut = neuEingebaut;
		this.zaelerstand = zaelerstand;
		this.kommentar = kommentar;
	}

	@Override
	public int hashCode() {
		return Objects.hash(kommentar);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		AbleseEntry other = (AbleseEntry) obj;
		return Objects.equals(datum, other.datum) && Objects.equals(id, other.id)
				&& Objects.equals(kommentar, other.kommentar) && Objects.equals(kundenNummer, other.kundenNummer)
				&& neuEingebaut == other.neuEingebaut && Objects.equals(zId, other.zId)
				&& Objects.equals(zaelernummer, other.zaelernummer) && zaelerstand == other.zaelerstand;
	}

	@Override
	public String toString() {
		return "AbleseEntry [id=" + id + ", kundenNummer=" + kundenNummer + ", zId=" + zId + ", zaelernummer="
				+ zaelernummer + ", datum=" + datum + ", neuEingebaut=" + neuEingebaut + ", zaelerstand=" + zaelerstand
				+ ", kommentar=" + kommentar + "]";
	}

}
