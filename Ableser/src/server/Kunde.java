package server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Kunde {

	private String id;
	private String name;
	private String vorname;
	
	public Kunde(String name, String vorname) {
		//TODO
	}
}
