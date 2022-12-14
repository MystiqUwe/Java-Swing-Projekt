package server;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Kunde {

	private UUID id;
	private String name;
	private String vorname;
	
	public Kunde(String name, String vorname) {
		super();
		id=UUID.randomUUID();
		this.name = name;
		this.vorname = vorname;
	}
	
	
}
