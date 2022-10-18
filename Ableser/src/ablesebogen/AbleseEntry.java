package ablesebogen;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AbleseEntry {

	private String kundenNummer;
	private String zaelerArt;
	private int zaelernummer;
	private Date datum;
	private boolean neuEingebaut;
	private int zaelerstand;
	private String kommentar;
	
	public AbleseEntry(String kundenNummer, String zaelerArt, int zaelernummer, Date datum, boolean neuEingebaut,
			int zaelerstand, String kommentar) {
		this.kundenNummer = kundenNummer;
		this.zaelerArt = zaelerArt;
		this.zaelernummer = zaelernummer;
		this.datum = datum;
		this.neuEingebaut = neuEingebaut;
		this.zaelerstand = zaelerstand;
		this.kommentar = kommentar;
	}
	
	
}
