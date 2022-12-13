package server;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Ablesung {

	private String id;
	private String zaehlernummer;
	private LocalDate datum;
	private Kunde kunde;
	private String kommentar;
	private boolean neuEingebaut;
	private Integer zaehlerstand;

	public Ablesung(String zaehlernummer, LocalDate datum, Kunde kunde, String kommentar, boolean neuEingebaut, int zaelerstand) {
		//TODO
	}
}
